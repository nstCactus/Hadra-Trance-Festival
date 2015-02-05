/*
 *
 *     Copyright 2013-2014 Yohann Bianchi
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *     or see <http://www.gnu.org/licenses/>.
 *
 */

package com.zion.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.data.SoundcloudTrack;

import java.util.ArrayList;
import java.util.Locale;

import gov.nasa.arc.mct.util.WeakHashSet;

import static com.zion.music.MediaPlayerService.State.End;
import static com.zion.music.MediaPlayerService.State.Idle;
import static com.zion.music.MediaPlayerService.State.Playing;
import static com.zion.music.MediaPlayerService.State.Preparing;
import static com.zion.music.MediaPlayerService.State.Stopped;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener{
	public static final String ACTION_QUEUE_TRACKS      = "com.zion.music.MediaPlayerService.queue_tracks";
	public static final String ACTION_TOGGLE_PLAY_PAUSE = "com.zion.music.MediaPlayerService.toggle_play_pause";
	public static final String ACTION_NEXT_TRACK        = "com.zion.music.MediaPlayerService.next_track";
	public static final String ACTION_PREVIOUS_TRACK    = "com.zion.music.MediaPlayerService.previous_track";
	public static final String ACTION_STOP              = "com.zion.music.MediaPlayerService.stop";
	public static final String ACTION_FOREGROUND_STATE_CHANGED = "com.zion.music.MediaPlayerService.foreground_state_changed";

	public static final String EXTRA_TRACKS             = "com.zion.htf.extra.tracks";
	public static final String EXTRA_ARTIST_ID          = "com.zion.htf.extra.artist_id";
	public static final String EXTRA_ARTIST_PHOTO       = "com.zion.htf.extra.artist_photo";
	public static final String EXTRA_NOW_IN_FOREGROUND  = "com.zion.htf.extra.now_in_foreground";

	/** The current internal state */
	private MediaPlayerService.State state = Idle;

	/** MediaPlayer used for actual music playback */
	private MediaPlayer player;

	/** Binder used to bind the service to an activity which controls it */
	private final MediaPlayerService.LocalBinder binder = new MediaPlayerService.LocalBinder();

	/** Playlist */
	private ArrayList<SoundcloudTrack> tracks = new ArrayList<>();

	/** Position of the current playlist item */
	private int playlistPosition = 0;

	/** Is shuffle enabled? */
	private boolean shuffle = false;

	/** Is repeat enabled? */
	private boolean repeat = false;

	/** Collection of clients to be notified of various status changes */
	private final WeakHashSet<MediaPlayerService.Client> clients = new WeakHashSet<>();

	/** Database identifier of the artist (used to open the correct Artist Details when notification clicked) */
	private int artist_id;

	/** Used to periodically update the player UI (seekbar & elapsed time) */
	private final Handler playerProgressHandler = new Handler();

	/** Used to update periodically the player UI (seekbar & elapsed time) */
	private final MediaPlayerService.PlayerControlsUpdaterRunnable playerProgressRunnable = new PlayerControlsUpdaterRunnable();

	/** The percentage of the track that is buffered */
	private int bufferedPercentage;

    /** Used to pause playback when headphones are unplugged */
    private final BroadcastReceiver audioBecomingNoisyBroadcastReceiver = new AudioBecomingNoisyBroadcastReceiver();
    private final IntentFilter audioBecomingNoisyIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    private AudioManager audioManager;
	private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new OnAudioFocusChangeListener();
	private NotificationHelper notificationHelper;
	private Bitmap artistPhoto;
	private boolean anyActivityInForeground = false;

	public enum State{
		Idle,
		Stopped,    // media player is stopped and not prepared to play
		Preparing,  // media player is preparing...
		Playing,    // playback active (media player ready!). The media player may actually be paused in this state if we don't have audio focus but we stay in this state so that we know we have to resume playback once we get focus back).
		Paused,     // playback paused (media player ready!)
		End         // media player released or not initialized
	}

	@Override
	public void onCreate(){
		this.audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
		this.notificationHelper = new NotificationHelper(this);

		this.initPlayer();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		if(null != intent){
			String action = intent.getAction();
			if(MediaPlayerService.ACTION_QUEUE_TRACKS.equals(action)){
				if(!intent.hasExtra(MediaPlayerService.EXTRA_ARTIST_ID)) throw new RuntimeException(String.format(Locale.ENGLISH, "The intent must contain a %s int extra. Please provide one.", MediaPlayerService.EXTRA_ARTIST_ID));
				if(!intent.hasExtra(MediaPlayerService.EXTRA_ARTIST_PHOTO)) throw new RuntimeException(String.format(Locale.ENGLISH, "The intent must contain a %s int extra. Please provide one.", MediaPlayerService.EXTRA_ARTIST_PHOTO));

				if(intent.hasExtra(MediaPlayerService.EXTRA_TRACKS)) this.tracks = intent.getParcelableArrayListExtra(MediaPlayerService.EXTRA_TRACKS);
				if(0 == this.artist_id) this.artist_id = intent.getIntExtra(MediaPlayerService.EXTRA_ARTIST_ID, 0);
				this.artistPhoto = BitmapFactory.decodeResource(this.getResources(), intent.getIntExtra(MediaPlayerService.EXTRA_ARTIST_PHOTO, R.drawable.no_image_wrapper));
			}
			else if(MediaPlayerService.ACTION_TOGGLE_PLAY_PAUSE.equals(action)){
				if(this.isPlaying()){
					this.pausePlayback();
				}
				else{
					this.startPlayback();
				}
			}
			else if(MediaPlayerService.ACTION_NEXT_TRACK.equals(action)){
				this.playNext();
			}
			else if(MediaPlayerService.ACTION_PREVIOUS_TRACK.equals(action)){
				this.playPrev();
			}
			else if(MediaPlayerService.ACTION_STOP.equals(action)){
				this.stopPlayback();
			}
			else if(MediaPlayerService.ACTION_FOREGROUND_STATE_CHANGED.equals(action)){
				this.anyActivityInForeground = intent.getBooleanExtra(MediaPlayerService.ACTION_FOREGROUND_STATE_CHANGED, false);
				this.updateNotification();
			}
		}
		else{
			//TODO: Find a way to restart the service without an intent
		}
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(null != this.player){
			this.player.release();
		}
		this.changeState(End);
	}

	@Override
	public IBinder onBind(Intent intent){
		return this.binder;
	}

	@Override
	public boolean onUnbind(Intent intent){
		switch(this.state){
			case Playing:
			case Paused:
			case Preparing:
				// set the notification
				// launch itself

				break;
		}

		return super.onUnbind(intent);
	}

	private void changeState(MediaPlayerService.State state){
		if(this.state != state){
			this.playerProgressHandler.removeCallbacks(this.playerProgressRunnable); // Ensure that changing state will not make the Runnable run more than once every second
			if(state.equals(Playing)){
				if(null != this.playerProgressRunnable){
					this.playerProgressHandler.post(this.playerProgressRunnable);
				}
			}
			this.state = state;

			if(state.equals(MediaPlayerService.State.Stopped)){
				this.notificationHelper.killNotification();
			}
			else{
				this.updateNotification();
			}
			this.notifyStateChanged();
		}
	}

	private void initPlayer(){
		if(null == this.player) this.player = new MediaPlayer();
		this.player.setWakeMode(this.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		this.player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		this.player.setOnPreparedListener(this);
		this.player.setOnCompletionListener(this);
		this.player.setOnErrorListener(this);
		this.player.setOnBufferingUpdateListener(this);
	}

	private void preparePlayback(){
		SoundcloudTrack track = this.tracks.get(this.playlistPosition);
		Uri uri = Uri.parse(track.getStreamUrl());
		Uri.Builder uriBuilder = uri.buildUpon();
		uriBuilder.appendQueryParameter("consumer_key", this.getString(R.string.soundcloud_client_id));

		try{
			this.player.reset();
			this.player.setDataSource(this.getApplicationContext(), uriBuilder.build());
			this.player.prepareAsync();
			this.changeState(Preparing);
		}
		catch(Exception e){
			if(BuildConfig.DEBUG) Log.e("MediaPlayerService", "Error setting data source", e);
			this.player.reset();
			this.changeState(Idle);
		}
	}

	/**
	 * Sets the currently playing track to the given {@code position}.
	 * If the given {@code position} matches a track in the {@link MediaPlayerService#tracks playlist},
	 * the {@link MediaPlayerService#player}'s state is set
	 * to {@link MediaPlayerService.State#Idle}.
	 * Otherwise it is set to {@link MediaPlayerService.State#Stopped}
	 *
	 * @param position the position of the track in the playlist (must be between -1 and {@code this.tracks#size()}, both exclusive)
	 */
	private void setTrack(int position){
		if(0 > position || position >= this.tracks.size()) throw new IllegalArgumentException("position must be a valid index the tracks Collection.");
		this.playlistPosition = position;
		if(null == this.player) this.initPlayer();
		this.player.reset();
		this.changeState(Idle);
	}


	////////////////////////////
	// BEGIN Playback control //
	////////////////////////////
	/**
	 * Starts music playback.
	 * If the {@link MediaPlayerService#player}'s current state is
	 * {@link MediaPlayerService.State#Paused}, the playback is resumed instead of started from the begining.
	 * The player's state is set to {@link MediaPlayerService.State#Playing}
	 */
	public void startPlayback(){
		if(this.requestAudioFocus()){
			if(null == this.player) this.initPlayer();

            this.registerReceiver(this.audioBecomingNoisyBroadcastReceiver, this.audioBecomingNoisyIntentFilter);

			if(MediaPlayerService.State.Paused.equals(this.state)){
				this.changeState(Playing);
				this.player.start();
			}
			else{
				this.preparePlayback();
			}
		}
	}

	/**
	 * Starts playing the track at the given {@code position} in the {@link MediaPlayerService#tracks playlist}.
	 * The {@link MediaPlayerService#player}'s state is set to {@link MediaPlayerService.State#Playing}
	 *
	 * @param position the position of the track in the playlist
	 */
	public void startPlayback(int position){
		if(this.isPlaying()) this.player.stop();
		this.setTrack(position);
		this.startPlayback();
	}

	/**
	 * Pauses music playback.
	 * The {@link MediaPlayerService#player}'s state is set to {@link MediaPlayerService.State#Paused}
	 */
	public void pausePlayback(){
		if(this.state.equals(Playing)){
			this.player.pause();
			this.abandonAudioFocus();
			this.changeState(MediaPlayerService.State.Paused);
		}
	}

	/**
	 * Stops music playback.
	 */
	public void stopPlayback(){
		if(null != this.player){
			this.player.stop();
			this.player.release();
			this.player = null;
			this.changeState(Stopped);
			this.abandonAudioFocus();
            this.unregisterReceiver(this.audioBecomingNoisyBroadcastReceiver);

        }
	}

	/**
	 * Plays the next track in the playlist (if the current state is {@link MediaPlayerService.State#Playing} or
	 * {@link MediaPlayerService.State#Paused}).
	 * If there are none, the player's state is set to {@link MediaPlayerService.State#Stopped}, unless
	 * {@link MediaPlayerService#repeat} mode is activated (In this case the first song is played).
	 */
	public void playNext(){
		if(this.isPlaying() || this.isPaused()){
			int position = this.playlistPosition + 1;

			if(this.tracks.size() <= position){
				if(this.repeat){
					position = 0;
					this.setTrack(position);
				}
				else{
					this.stopPlayback();
				}
			}
			else{
				this.setTrack(position);
			}

			if(!Stopped.equals(this.state)){
				this.preparePlayback();
			}

			this.updateNotification();
		}
	}
	/**
	 * Plays the previous track in the playlist (if the current state is {@link MediaPlayerService.State#Playing} or
	 * {@link MediaPlayerService.State#Paused}).
	 * If there are none, the player's state is set to {@link MediaPlayerService.State#Stopped}, unless
	 * {@link MediaPlayerService#repeat} mode is activated (In this case the last song is played).
	 */
	public void playPrev(){
		if(this.isPlaying() || this.isPaused()){
			int position = this.playlistPosition - 1;

			if(0 > position){
				if(this.repeat){
					position = this.tracks.size() - 1;
					this.setTrack(position);
				}
				else{
					this.stopPlayback();
				}
			}
			else{
				this.setTrack(position);
			}

			if(!Stopped.equals(this.state)){
				this.preparePlayback();
			}

			this.updateNotification();
		}
	}

	/**
	 * Moves the player to {@code offset}.
	 *
	 * @param offset whether to turn repeat on or off.
	 */
	public void seekTo(int offset){
        if(this.isPlaying() || this.isPaused()) this.player.seekTo(offset);
	}

	/**
	 * Is shuffle activated?
	 * @return {@code true} is shuffle is activated, {@code false} otherwise
	 */
	public boolean isShuffleActivated(){
		return this.shuffle;
	}

	/**
	 * Is repeat activated?
	 * @return {@code true} is repeat is activated, {@code false} otherwise
	 */
	public boolean isRepeatActivated(){
		return this.repeat;
	}

	/**
	 * Sets the shuffle mode.
	 *
	 * @param activate Whether to turn shuffle on or off.
	 */

	public void setShuffle(boolean activate){
		this.shuffle = activate;
	}

	/**
	 * Sets the repeat mode.
	 *
	 * @param activate whether to turn repeat on or off.
	 */
	public void setRepeat(boolean activate){
		this.repeat = activate;
	}

	//////////////////////////
	// END Playback control //
	//////////////////////////


	///////////////////////////////////
	// BEGIN player state monitoring //
	///////////////////////////////////
	public boolean isPreparing(){
		return this.state.equals(Preparing);
	}

	public boolean isPlaying(){
		return this.state.equals(Playing);
	}

	public boolean isPaused(){
		return this.state.equals(MediaPlayerService.State.Paused);
	}

	/**
	 * Gets the current playback position.
	 *
	 * @return the current position in milliseconds
	 */
	public int getCurrentPosition(){
		return this.player.getCurrentPosition();
	}

	/**
	 * Gets the duration of the currently playing track.
	 *
	 * @return the duration in milliseconds, if no duration is available
	 *         (for example, if streaming live content), -1 is returned.
	 */
	public int getDuration(){
		return this.player.getDuration();
	}

	/**
	 * Returns the percentage (0-100) of the track that is buffered
	 *
	 * @return the percentage of the track that is buffered
	 */
	public int getBufferedPercentage(){
		return this.bufferedPercentage;
	}

	/////////////////////////////////
	// END player state monitoring //
	/////////////////////////////////


	//////////////////////
	// BEGIN AudioFocus //
	//////////////////////
	private boolean requestAudioFocus(){
		boolean ret = true;
		if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED != this.audioManager.requestAudioFocus(this.audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)){
			ret = false;
			Toast.makeText(this.getApplicationContext(), R.string.error_audiofocus_request_failed, Toast.LENGTH_SHORT).show();
		}
		return ret;
	}

	private boolean abandonAudioFocus(){
		boolean ret = true;
		if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED != this.audioManager.abandonAudioFocus(this.audioFocusChangeListener)){
			ret = false;
		}
		return ret;
	}
	////////////////////
	// END AudioFocus //
	////////////////////


	/////////////////////////////////
	// BEGIN MediaPlayer callbacks //
	/////////////////////////////////
	@Override
	public void onCompletion(MediaPlayer mediaPlayer){
		this.playNext();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent){
		this.bufferedPercentage = percent;
		this.notifyBufferProgressChanged(percent);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra){
		if(BuildConfig.DEBUG) Log.e("MediaPlayerService", String.format("Error manipulating MediaPlayer (%d, %d)", what, extra));
		switch(what){
			case MediaPlayer.MEDIA_ERROR_IO:
				Toast.makeText(this, R.string.error_player_IO, Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(this, R.string.error_player, Toast.LENGTH_SHORT).show();
		}
		this.player.reset();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp){
		this.player.start();
		this.changeState(Playing);
	}
	///////////////////////////////
	// END MediaPlayer callbacks //
	///////////////////////////////


	//////////////////////
	// BEGIN Observable //
	//////////////////////
	/**
	 * Register a new client to receive updates about the service status
	 * @param client the client to register
	 */
	public void registerClient(MediaPlayerService.Client client){
		this.clients.add(client);
	}

	/**
	 * Unregister a new client
	 * @param client the client to unregister
	 */
	public void unregisterClient(MediaPlayerService.Client client){
		this.clients.remove(client);
	}

	/**
	 * Notify clients that the internal state of the service has changed
	 */
	private void notifyStateChanged(){
		for(MediaPlayerService.Client client : this.clients){
			client.onPlayerStateChanged(this.state);
		}
	}

	/**
	 * Notify clients of a playing head progress
	 * @param progress the current playhead position, as a millisecond offset since the beginning of the track
	 * @param duration the currently played track duration, in milliseconds
	 */
	private void notifyProgressChanged(int progress, int duration){
		for(MediaPlayerService.Client client : this.clients){
			client.onPlayerProgressChanged(progress, duration);
		}
	}

	/**
	 * Notify clients of a buffering progress
	 * @param bufferedPercentage the percentage of the track that is currently buffered
	 */
	private void notifyBufferProgressChanged(int bufferedPercentage){
		for(MediaPlayerService.Client client : this.clients){
			client.onBufferProgressChanged(bufferedPercentage);
		}
	}
	////////////////////
	// END Observable //
	////////////////////



	public interface Client{
		/**
		 * Called when the percentage of buffered data has changed
		 * @param bufferPercentage the percentage of buffered data
		 */
		void onBufferProgressChanged(int bufferPercentage);

		/**
		 * Called when the {@link MediaPlayerService}'s state has changed
		 * @param state the new {@link MediaPlayerService.State} of the MediaPlayerService
		 */
		void onPlayerStateChanged(MediaPlayerService.State state);

		/**
		 * Called when the playhead moves. Called every second during playback.
		 * @param progress the playhead offset since the begining of the track, in seconds.
		 * @param duration the currently playing track duration, in milliseconds
		 */
		void onPlayerProgressChanged(int progress, int duration);
	}


	public class LocalBinder extends Binder{
		public MediaPlayerService getService(){
			return MediaPlayerService.this;
		}
	}

	private class PlayerControlsUpdaterRunnable implements Runnable{
		@Override
		public void run(){
			MediaPlayerService.this.notifyProgressChanged(MediaPlayerService.this.getCurrentPosition(), MediaPlayerService.this.getDuration());
			if(MediaPlayerService.this.isPlaying()){
				MediaPlayerService.this.playerProgressHandler.postDelayed(this, 1000);
			}
		}
	}

	private class OnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener{
		@Override
		public void onAudioFocusChange(int focusChange){
			switch (focusChange) {
				case AudioManager.AUDIOFOCUS_GAIN:
					//TODO: Check if the player is supposed to be playing
					// resume playback
					if (null == MediaPlayerService.this.player) MediaPlayerService.this.initPlayer();
					else if (!MediaPlayerService.this.isPlaying()) MediaPlayerService.this.startPlayback(MediaPlayerService.this.getCurrentPosition());
					MediaPlayerService.this.player.setVolume(1.0f, 1.0f);
					break;

				case AudioManager.AUDIOFOCUS_LOSS:
					// Lost focus for an unbounded amount of time: stop playback and release mediaPlayer
					if(MediaPlayerService.this.isPlaying()) MediaPlayerService.this.stopPlayback();
					break;

				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
					// Lost focus for a short time, but we have to stop playback.
					// We don't release the mediaPlayer because playback is likely to resume
					if (MediaPlayerService.this.isPlaying()) MediaPlayerService.this.pausePlayback();
					break;

				case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
					// Lost focus for a short time, but it's ok to keep playing at an attenuated level
					if (MediaPlayerService.this.isPlaying()) MediaPlayerService.this.player.setVolume(0.2f, 0.2f);
					break;
			}
		}
	}

	/**
	 * Returns the {@link com.zion.htf.data.SoundcloudTrack} corresponding to the current {@link MediaPlayerService#getCurrentTrack() playlist position}
	 * @return The current {@link com.zion.htf.data.SoundcloudTrack}, or {@code null} if none found
	 */
	private SoundcloudTrack getCurrentTrack(){
		SoundcloudTrack currentTrack = null;
		if(null != this.tracks && 0 < this.tracks.size()) currentTrack = this.tracks.get(this.playlistPosition);
		return currentTrack;
	}

	/**
	 * Updates the notification, considering the current play and activity state
	 */
	private void updateNotification(){
		if(!this.anyActivityInForeground && (this.isPlaying() || this.isPaused() || this.isPreparing())){
			SoundcloudTrack currentTrack = this.getCurrentTrack();
			if(null != currentTrack) this.notificationHelper.buildNotification(currentTrack.getArtist(), currentTrack.getTitle(), this.artist_id, this.artistPhoto, this.isPlaying() || this.isPreparing());
		}
		else if(this.anyActivityInForeground){
			this.notificationHelper.killNotification();
		}
	}

    private class AudioBecomingNoisyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())){
                if(MediaPlayerService.this.isPlaying()) MediaPlayerService.this.pausePlayback();
            }
        }
    }
}

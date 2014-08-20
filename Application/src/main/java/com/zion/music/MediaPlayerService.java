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
import android.content.Context;
import android.content.Intent;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

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

	public static final String EXTRA_TRACKS             = "com.zion.htf.extra.tracks";
	public static final String EXTRA_ARTIST_ID          = "com.zion.htf.extra.artist_id";

	private static final int NOTIFICATION_ID = 193;

//	/** The system NotificationManager service */
//	private static NotificationManager notificationManager;
//
//	/** A notification builder used to generate the sticky notification displayed during music playback */
//	private NotificationCompat.Builder notificationBuilder;

	/** The current internal state */
	private MediaPlayerService.State state = Idle;

	/** MediaPlayer used for actual music playback */
	private MediaPlayer player;

	/** Binder used to bind the service to an activity which controls it */
	private final MediaPlayerService.LocalBinder binder = new MediaPlayerService.LocalBinder();

	/** Playlist */
	private ArrayList<SoundcloudTrack> tracks = new ArrayList<SoundcloudTrack>();

	/** Position of the current playlist item */
	private int playlistPosition = 0;

	/** Is shuffle enabled? */
	private boolean shuffle = false;

	/** Is repeat enabled? */
	private boolean repeat = false;

	/** Collection of clients to be notified of various status changes */
	private final HashSet<WeakReference<MediaPlayerService.Client>> clients = new HashSet<WeakReference<MediaPlayerService.Client>>();

	/** Database identifier of the artist (used to open the correct Artist Details when notification clicked) */
	private int artist_id;

	/** Used to periodically update the player UI (seekbar & elapsed time) */
	private final Handler playerProgressHandler = new Handler();

	/** Used to update periodically the player UI (seekbar & elapsed time) */
	private final MediaPlayerService.PlayerControlsUpdaterRunnable playerProgressRunnable = new PlayerControlsUpdaterRunnable();

	/** The percentage of the track that is buffered */
	private int bufferedPercentage;
	private AudioManager audioManager;
	private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new OnAudioFocusChangeListener();
	private NotificationHelper notificationHelper;
	private Bitmap artistPhoto;

	public int getCurrentArtistId(){
		return this.artist_id;
	}

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
		Log.v("MediaPlayerService", "onCreate() called");

//		MediaPlayerService.notificationManager = (NotificationManager)this.getSystemService(Service.NOTIFICATION_SERVICE);
		this.audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
		this.notificationHelper = new NotificationHelper(this);

		this.initPlayer();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		if(null != intent){
			Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "onStartCommand() called with ACTION = %s", intent.getAction()));

			String action = intent.getAction();
			if(MediaPlayerService.ACTION_QUEUE_TRACKS.equals(action)){
				if(intent.hasExtra(MediaPlayerService.EXTRA_TRACKS)) this.tracks = intent.getParcelableArrayListExtra(MediaPlayerService.EXTRA_TRACKS);
				if(!intent.hasExtra(MediaPlayerService.EXTRA_ARTIST_ID)) throw new RuntimeException(String.format(Locale.ENGLISH, "The intent must contain a %s int extra. Please provide one.", MediaPlayerService.EXTRA_ARTIST_ID));
				if(0 == this.artist_id) this.artist_id = intent.getIntExtra(MediaPlayerService.EXTRA_ARTIST_ID, 0);
				this.artistPhoto = BitmapFactory.decodeResource(this.getResources(), R.drawable.no_image);
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
				this.notificationHelper.killNotification();
				this.stopPlayback();
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
		Log.v("MediaPlayerService", "onBind() called");
		this.notificationHelper.killNotification();
		return this.binder;
	}

	@Override
	public boolean onUnbind(Intent intent){
		boolean ret = false;
		switch(this.state){
			case Playing:
			case Paused:
			case Preparing:
				this.notificationHelper.buildNotification("", this.getCurrentTrack().getTitle(), this.getCurrentTrack().getArtist(), 0l, this.artistPhoto, this.isPlaying());
				break;
		}

		return ret;
	}

	private void changeState(MediaPlayerService.State state){
		if(this.state != state){
			this.playerProgressHandler.removeCallbacks(this.playerProgressRunnable); // Ensure that changing state will not make the Runnable run more than once every second
			if(state.equals(Playing)){
				if(null != this.playerProgressRunnable){
					Log.v("MediaPlayerService", "First UI update, non-delayed");
					this.playerProgressHandler.post(this.playerProgressRunnable);
				}
			}
			Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "State change from %s to %s", this.state, state));
			this.state = state;
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

	private SoundcloudTrack getCurrentTrack(){
		SoundcloudTrack currentTrack = null;
		if(null != this.tracks && 0 < this.tracks.size()) currentTrack = this.tracks.get(this.playlistPosition);
		return currentTrack;
	}


	public void setList(ArrayList<SoundcloudTrack> tracks){
		this.tracks = tracks;
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
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: setTrack(%s)", position));
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
			Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: startPlayback()"));
			if(MediaPlayerService.State.Paused.equals(this.state)){
				this.changeState(Playing);
				this.player.start();
				this.notificationHelper.buildNotification("", this.getCurrentTrack().getTitle(), this.getCurrentTrack().getArtist(), 0l, this.artistPhoto, this.isPlaying());


				// Update notification text, if any
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
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: startPlayback(%s)", position));
		if(this.isPlaying()) this.player.stop();
		this.setTrack(position);
		this.startPlayback();
	}

	/**
	 * Pauses music playback.
	 * The {@link MediaPlayerService#player}'s state is set to {@link MediaPlayerService.State#Paused}
	 */
	public void pausePlayback(){
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: pausePlayback() while in state %s", this.state));
		if(this.state.equals(Playing)){
			this.player.pause();
			this.abandonAudioFocus();
			this.changeState(MediaPlayerService.State.Paused);
			this.notificationHelper.buildNotification("", this.getCurrentTrack().getTitle(), this.getCurrentTrack().getArtist(), 0l, this.artistPhoto, this.isPlaying());
		}
	}

	/**
	 * Stops music playback.
	 */
	public void stopPlayback(){
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: stopPlayback()"));
		this.player.stop();
		this.player.release();
		this.player = null;
		this.changeState(Stopped);
		this.abandonAudioFocus();
	}

	/**
	 * Plays the next track in the playlist (if the current state is {@link MediaPlayerService.State#Playing} or
	 * {@link MediaPlayerService.State#Paused}).
	 * If there are none, the player's state is set to {@link MediaPlayerService.State#Stopped}, unless
	 * {@link MediaPlayerService#repeat} mode is activated (In this case the first song is played).
	 */
	public void playNext(){
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: playNext(). Current playlistPosition = %s", this.playlistPosition));
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
				this.updateNotification(this.getCurrentTrack().getTitle());
			}
		}
	}
	/**
	 * Plays the previous track in the playlist (if the current state is {@link MediaPlayerService.State#Playing} or
	 * {@link MediaPlayerService.State#Paused}).
	 * If there are none, the player's state is set to {@link MediaPlayerService.State#Stopped}, unless
	 * {@link MediaPlayerService#repeat} mode is activated (In this case the last song is played).
	 */
	public void playPrev(){
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: playPrev(). Current playlistPosition = %s", this.playlistPosition));
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
				this.updateNotification(this.getCurrentTrack().getTitle());
			}
		}
	}

	/**
	 * Moves the player to {@code offset}.
	 *
	 * @param offset whether to turn repeat on or off.
	 */
	public void seekTo(int offset){
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: seekTo(%s).", offset));
		this.player.seekTo(offset);
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
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: setShuffle(%b)", activate));
		this.shuffle = activate;
	}

	/**
	 * Sets the repeat mode.
	 *
	 * @param activate whether to turn repeat on or off.
	 */
	public void setRepeat(boolean activate){
		Log.v("MediaPlayerService", String.format(Locale.ENGLISH, "COMMAND: setRepeat(%b)", activate));
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
			Log.e("MediaPlayerService", "requestAudioFocus() failed");
		}
		return ret;
	}

	private boolean abandonAudioFocus(){
		boolean ret = true;
		if(AudioManager.AUDIOFOCUS_REQUEST_GRANTED != this.audioManager.abandonAudioFocus(this.audioFocusChangeListener)){
			ret = false;
			Log.e("MediaPlayerService", "abandonAudioFocus() failed");
		}
		return ret;
	}
	////////////////////
	// END AudioFocus //
	////////////////////


	/////////////////////////
	// BEGIN Notifications //
	/////////////////////////
//	private Notification getNotification(){
//		Intent intent = new Intent(this.getApplicationContext(), ArtistDetailsActivity.class);
//		intent.putExtra(MediaPlayerService.EXTRA_ARTIST_ID, this.artist_id);
//		PendingIntent pi = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.getApplicationContext());
//		notificationBuilder.setSmallIcon(R.drawable.ic_stat_notify_app_icon)
//		                   .setContentText(this.getCurrentTrack().getTitle())
//		                   .setContentIntent(pi)
//		                   .setOngoing(true);
//
//		this.notificationBuilder = notificationBuilder;
//
//		return this.notificationBuilder.build();
//	}

	/** Updates the notification. */
	private void updateNotification(String text){
//		this.notificationBuilder.setContentText(text);
//		MediaPlayerService.notificationManager.notify(MediaPlayerService.NOTIFICATION_ID, this.notificationBuilder.build());
	}
	///////////////////////
	// END Notifications //
	///////////////////////


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
		Log.v("MediaPlayerService", String.format("Error manipulation MediaPlayer (%d, %d)", what, extra));
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

	/**
	 * Register a new client to receive updates about the service status
	 * @param client the client to register
	 */
	public void registerClient(MediaPlayerService.Client client){
		// Check if previously registered clients are still alive
		Iterator<WeakReference<MediaPlayerService.Client>> iterator = this.clients.iterator();
		while(iterator.hasNext()){
			if(null == iterator.next().get()){
				Log.i("MediaPlayerService#registerClient", "Removed a dead client.");
				iterator.remove();
			}
		}

		Log.v("MediaPlayerService", "Got a new client " + client.toString());
		this.clients.add(new WeakReference<MediaPlayerService.Client>(client));
	}

	/**
	 * Unregister a new client
	 * @param client the client to unregister
	 */
	public void unregisterClient(MediaPlayerService.Client client){
		this.clients.remove(new WeakReference<MediaPlayerService.Client>(client));
	}

	/**
	 * Notify clients that the internal state of the service has changed
	 */
	private void notifyStateChanged(){
		Iterator<WeakReference<MediaPlayerService.Client>> iterator = this.clients.iterator();
		while(iterator.hasNext()){
			WeakReference<MediaPlayerService.Client> clientReference = iterator.next();

			try{
				clientReference.get().onPlayerStateChanged(this.state);
			}
			catch(NullPointerException npe){
				iterator.remove();
				Log.w("MediaPlayerService#notifyStateChanged", "Removed client no longer available.");
			}
		}
	}

	/**
	 * Notify clients of a playing head progress
	 * @param progress the current playhead position, as a millisecond offset since the beginning of the track
	 * @param duration the currently played track duration, in milliseconds
	 */
	private void notifyProgressChanged(int progress, int duration){
		Iterator<WeakReference<MediaPlayerService.Client>> iterator = this.clients.iterator();
		while(iterator.hasNext()){
			WeakReference<MediaPlayerService.Client> clientReference = iterator.next();

			try{
				clientReference.get().onPlayerProgressChanged(progress, duration);
			}
			catch(NullPointerException npe){
				iterator.remove();
				Log.w("MediaPlayerService#notifyProgressChanged", "Removed client no longer available.");
			}
		}
	}

	/**
	 * Notify clients of a buffering progress
	 * @param bufferedPercentage the percentage of the track that is currently buffered
	 */
	private void notifyBufferProgressChanged(int bufferedPercentage){
		Iterator<WeakReference<MediaPlayerService.Client>> iterator = this.clients.iterator();
		while(iterator.hasNext()){
			WeakReference<MediaPlayerService.Client> clientReference = iterator.next();

			try{
				clientReference.get().onBufferProgressChanged(bufferedPercentage);
			}
			catch(NullPointerException npe){
				iterator.remove();
				Log.w("MediaPlayerService#notifyBufferProgressChanged", "Removed client no longer available.");
			}
		}
	}



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
			if(Playing == MediaPlayerService.this.state){
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
}

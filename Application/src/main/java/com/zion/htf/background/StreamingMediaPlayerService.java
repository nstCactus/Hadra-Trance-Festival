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

package com.zion.htf.background;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.exception.MissingArgumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class StreamingMediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnCompletionListener {
    public static final String EXTRA_TRACK_STREAM_URL =     "com.zion.htf.extra.track_stream_url";
    public static final String EXTRA_TRACK_TITLE =          "com.zion.htf.extra.track_title";
    public static final String EXTRA_TRACK_ARTIST =         "com.zion.htf.extra.track_artist";
    public static final String EXTRA_TRACK_ID =             "com.zion.htf.extra.soundcloud_track_id";
    public static final String EXTRA_TRACK_DURATION =       "com.zion.htf.extra.track_duration";
    public static final String EXTRA_PLAYLIST_POSITION =    "com.zion.htf.extra.playlist_position";
    public static final String ACTION_PLAY =                "com.zion.htf.action.play";
    public static final String ACTION_QUEUE_TRACK =         "com.zion.htf.action.queue ";

	/** MediaPlayer used for actual music playback */
	MediaPlayer mediaPlayer = null;

	/** Binder used to bind the service to an activity which controls it */
	private final StreamingMediaPlayerService.LocalBinder binder = new StreamingMediaPlayerService.LocalBinder();

	/** Playlist */
	private ArrayList<StreamingMediaPlayerService.TrackDataHolder> tracks = new ArrayList<StreamingMediaPlayerService.TrackDataHolder>();

	/** Position of the current playlist item */
	private int position = 0;

	public class LocalBinder extends Binder{
		public StreamingMediaPlayerService getService(){
			return StreamingMediaPlayerService.this;
		}
	}

	@Override
	public void onCreate(){
		Log.v("StreamingMediaPlayerService", "onCreate() called");
		this.initMediaPlayer();
	}

	@Override
	public IBinder onBind(Intent intent){
		Log.v("StreamingMediaPlayerService", "onBind() called");
		return this.binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		String action = intent.getAction();
		Log.v("StreamingMediaPlayerService", String.format(Locale.ENGLISH, "onStartCommand() called with ACTION = %s", action));

		return Service.START_STICKY;

//		try{
//			if(StreamingMediaPlayerService.ACTION_PLAY.equals(action)){
//				this.playButtonClicked(this.findTrackPositionById(intent.getIntExtra(StreamingMediaPlayerService.EXTRA_PLAYLIST_POSITION, -1)));
//			}
//			else if(StreamingMediaPlayerService.ACTION_QUEUE_TRACK.equals(action)){
//				this.queueTrack(intent);
//			}
//		}
//		catch(MissingArgumentException e){
//			if(BuildConfig.DEBUG) e.printStackTrace();
//			//TODO: notify activity & user
//			//TODO: report through piwik
//		}
//		catch(IllegalStateException e){
//			if(BuildConfig.DEBUG) e.printStackTrace();
//			//TODO: notify activity & user
//			//TODO: report through piwik
//		}
//
//		return 0;
	}

	private void playButtonClicked(int trackPosition){
		if(null != this.mediaPlayer){
			if(this.mediaPlayer.isPlaying()){
				this.mediaPlayer.pause();
			}
		}
		else{
			this.startPlaying(0 > trackPosition ? this.position : trackPosition);
		}
	}

	private int findTrackPositionById(int id){
		int position = 0;
		boolean found = false;
		StreamingMediaPlayerService.TrackDataHolder currentTrack;

		while(!found && position < this.tracks.size()){
			currentTrack = this.tracks.get(position);
			if(id == currentTrack.id) found = true;
			position++;
		}

		return found ? position : -1;
	}

	private void initMediaPlayer(){
		if(null == this.mediaPlayer) this.mediaPlayer = new MediaPlayer();
		this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		this.mediaPlayer.setOnPreparedListener(this);
		this.mediaPlayer.setOnCompletionListener(this);
		this.mediaPlayer.setOnSeekCompleteListener(this);
		this.mediaPlayer.setOnErrorListener(this);
	}

	private void startPlaying(int position){
		try{
			Uri.Builder builder = Uri.parse(this.tracks.get(position).streamUrl).buildUpon();
			builder.appendQueryParameter("consumer_key", Application.getContext().getString(R.string.soundcloud_client_id));

            this.position = position;
			if(0 == this.tracks.size()) throw new IllegalStateException("Playing queue is empty.");
            this.mediaPlayer.setDataSource(builder.build().toString());
            this.mediaPlayer.prepareAsync();
        }
        catch(IOException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            //TODO: notify activity
        }
    }

    private void queueTrack(Intent intent) throws MissingArgumentException {
        if(!intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_STREAM_URL))    throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_STREAM_URL, "String");
        if(!intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_ID))            throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_ID, "int");
        if(!intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_TITLE))         throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_TITLE, "String");
        if(!intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_ARTIST))        throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_ARTIST, "String");
        StreamingMediaPlayerService.TrackDataHolder track = new StreamingMediaPlayerService.TrackDataHolder(intent.getStringExtra(StreamingMediaPlayerService.EXTRA_TRACK_STREAM_URL), intent.getStringExtra(StreamingMediaPlayerService.EXTRA_TRACK_TITLE), intent.getStringExtra(StreamingMediaPlayerService.EXTRA_TRACK_ARTIST), intent.getIntExtra(StreamingMediaPlayerService.EXTRA_TRACK_ID, -1));
        this.tracks.add(track);
    }

    /*******************************/
    /* BEGIN MediaPlayer callbacks */
    /*******************************/
    /** Called when MediaPlayer is ready */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer){
        Log.v("StreamingMediaPlayerService", "Ready to play");
        // TODO: Show visual feedback
        mediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra){
        String whatString;
        switch(what){
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                whatString = "Unknown error";
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                whatString = "Server died";
                break;
            default:
                whatString = "Unexpected error";
        }

        String extraString;
        switch(extra){
            case MediaPlayer.MEDIA_ERROR_IO:
                extraString = "I/O error";
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                extraString = "Malformed";
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                extraString = "Unsupported";
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                extraString = "Timeout";
                break;
            default:
                extraString = "Unexpected";
        }
        Log.e("MediaPlayerImplementation", String.format(Locale.ENGLISH, "MediaPlayer reported error %3$s (#%1$d) with extra %4$s (#%2$d)", what, extra, whatString, extraString));
        return false;
    }

    /**
     * Called to indicate the completion of a seek operation.
     *
     * @param mp the MediaPlayer that issued the seek operation
     */
    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {

    }
    /*******************************/
    /* END MediaPlayer callbacks */
    /*******************************/

    private class TrackDataHolder{
        String title;
        String streamUrl;
        String artistName;
        public int id;

        public TrackDataHolder(String streamUrl, String title, String artistName, int id){
            this.streamUrl = streamUrl;
            this.title = title;
            this.artistName = artistName;
            this.id = id;
        }
    }
}

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
import android.os.IBinder;

import com.zion.htf.BuildConfig;
import com.zion.htf.exception.MissingArgumentException;

import java.io.IOException;
import java.util.ArrayList;

public class StreamingMediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String EXTRA_URL = "com.zion.htf.extra.url";
    public static final String EXTRA_TRACK_STREAM_URL = "com.zion.htf.extra.track_stream_url";
    public static final String EXTRA_TRACK_TITLE = "com.zion.htf.extra.track_title";
    public static final String EXTRA_TRACK_ARTIST = "com.zion.htf.extra.track_artist";
    public static final String EXTRA_TRACK_ID = "com.zion.htf.extra.soundcloud_track_id";
    public static final String ACTION_PLAY = "com.zion.htf.action.play";
    public static final String ACTION_QUEUE_TRACK = "com.zion.htf.action.queue ";
    MediaPlayer mediaPlayer = null;
    ArrayList<StreamingMediaPlayerService.TrackDataHolder> tracks = new ArrayList<StreamingMediaPlayerService.TrackDataHolder>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        String action = intent.getAction();

        try{
            if(StreamingMediaPlayerService.ACTION_PLAY.equals(action)){
                this.startPlaying();
            }
            else if(StreamingMediaPlayerService.ACTION_QUEUE_TRACK.equals(action)){
                this.queueTrack(intent);
            }
        }
        catch(MissingArgumentException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            //TODO: notify activity & user
            //TODO: report through piwik
        }
        catch(IllegalStateException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            //TODO: notify activity & user
            //TODO: report through piwik
        }

        return 0;
    }

    private void initMediaPlayer(){
        if(0 == this.tracks.size()) throw new IllegalStateException("Playing queue is empty.");
        if(null == this.mediaPlayer) this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnErrorListener(this);
    }

    private void startPlaying(){
        try{
            this.initMediaPlayer();
            this.mediaPlayer.setDataSource(this.tracks.get(0).streamUrl);
            this.mediaPlayer.prepareAsync();
        }
        catch (IOException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            //TODO: notify activity
        }
    }

    private void queueTrack(Intent intent) throws MissingArgumentException {
        if(intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_STREAM_URL)) throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_STREAM_URL, "String");
        if(intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_ID)) throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_ID, "int");
        if(intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_TITLE)) throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_TITLE, "String");
        if(intent.hasExtra(StreamingMediaPlayerService.EXTRA_TRACK_ARTIST)) throw new MissingArgumentException(StreamingMediaPlayerService.EXTRA_TRACK_ARTIST, "String");
        StreamingMediaPlayerService.TrackDataHolder track = new StreamingMediaPlayerService.TrackDataHolder(intent.getStringExtra(StreamingMediaPlayerService.EXTRA_TRACK_STREAM_URL), intent.getStringExtra(StreamingMediaPlayerService.EXTRA_TRACK_TITLE), intent.getStringExtra(StreamingMediaPlayerService.EXTRA_TRACK_ARTIST));
        this.tracks.add(track);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer mediaPlayer){
        mediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra){
        return false;
    }

    private class TrackDataHolder{
        String title;
        String streamUrl;
        String artistName;

        public TrackDataHolder(String streamUrl, String title, String artistName){
            this.streamUrl = streamUrl;
            this.title = title;
            this.artistName = artistName;
        }
    }
}
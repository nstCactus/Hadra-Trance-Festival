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

package com.zion.htf.ui.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jakewharton.disklrucache.DiskLruCache;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Request;
import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.adapter.SoundcloudTracksAdapter;
import com.zion.music.MediaPlayerService;
import com.zion.htf.background.StreamingMediaPlayerService;
import com.zion.htf.data.Artist;
import com.zion.htf.data.SoundcloudTrack;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.ui.AbstractServiceProxyActivity;
import com.zion.util.StringUtils;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.michenux.android.info.VersionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtistSoundcloudFragment extends Fragment implements AbstractServiceProxyActivity.ServiceProxyObserver, MediaPlayerService.Client{
    public static final String ARG_ARTIST_ID = "com.zion.htf.arg.artist_id";
    public static final String ARG_SET_ID = "com.zion.htf.arg.set_id";

	private Artist artist;

	private ListView listView;
    private View view;

	private final ArtistSoundcloudFragment.RetrieveTracksTask retrieveTracksTask = new ArtistSoundcloudFragment.RetrieveTracksTask();

	/* Media player controls */
	private ToggleButton playButton;
	private ToggleButton repeatButton;
	private ToggleButton shuffleButton;
	private MediaPlayerService service = null;
	private SeekBar seekBar;
	private TextView timeElapsedTextView;
	private TextView durationTextView;

	private int currentTrackDuration = 0;

	private boolean fragmentReady = false;

	/** Indicates whether the user is currently using the seekbar */
	private boolean userSeeking = false;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle args = this.getArguments();
		try{
			if(!args.containsKey(ArtistSoundcloudFragment.ARG_ARTIST_ID) && !args.containsKey(ArtistSoundcloudFragment.ARG_SET_ID))
				throw new MissingArgumentException(String.format(Locale.ENGLISH, "Either %s or %s is required for this Activity to work properly. Please provide any of them.", ArtistDetailsFragment.ARG_SET_ID, ArtistDetailsFragment.ARG_ARTIST_ID));
			if(args.containsKey(ArtistSoundcloudFragment.ARG_ARTIST_ID)){
				this.artist = Artist.getById(args.getInt(ArtistSoundcloudFragment.ARG_ARTIST_ID));
			}
			else{
				this.artist = Artist.getBySetId(args.getInt(ArtistSoundcloudFragment.ARG_SET_ID));
			}
		}
		catch(Exception e){
			//Report this through Piwik
			if(BuildConfig.DEBUG) e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		this.view = inflater.inflate(R.layout.fragment_artist_soundcloud, container, false);

		this.listView = (ListView)this.view.findViewById(R.id.artist_tracks_listview);
		this.listView.setEmptyView(this.view.findViewById(R.id.empty_view_stub));
		this.listView.setOnItemClickListener(new ListViewClickListener());

		// Start fetching tracks (if soundcloud_url isn't empty)
		String soundcloudUrl = this.artist.getSoundcloud();
		boolean malformedSoundcloudUrl = false;
		if(0 < soundcloudUrl.length()){
			Pattern scUSerIdPattern = Pattern.compile("https?://(?:www\\.)?soundcloud\\.com/(.{3,25})/?$");
			Matcher scUserIdMatcher;
			scUserIdMatcher = scUSerIdPattern.matcher(soundcloudUrl);
			if(scUserIdMatcher.find()){
				String scUserId = scUserIdMatcher.group(1);
				this.retrieveTracksTask.execute(scUserId);
			}
			else{
				// Report this through Piwik
				if(BuildConfig.DEBUG) Log.v(ArtistSoundcloudFragment.RetrieveTracksTask.TAG, String.format(Locale.ENGLISH, "Cannot parse \"%s\" to extract a soundcloud username or user_id", soundcloudUrl));
				malformedSoundcloudUrl = true;
			}
		}

		if(0 == soundcloudUrl.length() || malformedSoundcloudUrl){
			this.setErrorMessage(malformedSoundcloudUrl ? R.string.error_no_soundcloud : R.string.error_finding_soundcloud_profile);
		}

		return this.view;
	}
	@Override
	public void onDestroy(){
		((AbstractServiceProxyActivity)this.getActivity()).unregisterObserver(this);
		this.retrieveTracksTask.cancel(true);
		super.onDestroy();
	}

	@Override
	public void onResume(){
		super.onResume();
		if(this.fragmentReady){
			this.requestServiceBond(this.getActivity());
		}
	}

	private void setErrorMessage(String message){
		ProgressBar progressBar = (ProgressBar)this.view.findViewById(R.id.progressBar);
		progressBar.setVisibility(0 == message.length() ? View.VISIBLE : View.GONE);

		TextView messageTextView = (TextView)this.view.findViewById(R.id.message);
		if(null == messageTextView){
			ViewStub stub = (ViewStub)this.view.findViewById(R.id.empty_view_stub);
			if(null != stub) stub.inflate();
			messageTextView = (TextView)this.view.findViewById(R.id.message);
		}
		messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message);
    }

    private void setErrorMessage(int stringResId){
        this.setErrorMessage(this.getString(stringResId));
    }

	////////////////////////////////////
	// BEGIN MediaPlayerService Proxy //
	////////////////////////////////////
	public void start(){
		this.service.startPlayback();
	}

	public void start(int position){
		this.service.startPlayback(position);
	}

	public void pause(){
		this.service.pausePlayback();
	}

	public void next(){
		this.service.playNext();
	}

	public void prev(){
		this.service.playPrev();
	}

	public void seekTo(int position){
		this.service.seekTo(position);
	}

	public void shuffle(boolean activate){
		this.service.setShuffle(activate);
	}

	public void repeat(boolean activate){
		this.service.setRepeat(activate);
	}
	//////////////////////////////////
	// END MediaPlayerService Proxy //
	//////////////////////////////////


	///////////////////////////////////////////////////////////////////////
	// BEGIN AbstractServiceProxyActivity.ServiceProxyObserver callbacks //
	///////////////////////////////////////////////////////////////////////
	@Override
	public void onServiceRegistered(IBinder serviceBinder){
		Log.v("ArtistSoundcloudFragment", "Got a reference to the service!");
		this.service = ((MediaPlayerService.LocalBinder)serviceBinder).getService();
		this.service.registerClient(this);

		// Synchronise controls' state with the service
		this.playButton.setChecked(this.service.isPlaying());
		this.shuffleButton.setChecked(this.service.isShuffleActivated());
		this.repeatButton.setChecked(this.service.isRepeatActivated());

		if(this.service.isPlaying() || this.service.isPaused()){
			int duration = this.service.getDuration();
			this.seekBar.setMax(duration);
			this.seekBar.setProgress(this.service.getCurrentPosition());
			this.onBufferProgressChanged(this.service.getBufferedPercentage());
			this.durationTextView.setText(StringUtils.formatDuration(duration / 1000));
		}
		else if(this.service.isPreparing()){
			this.setSeekBarIndeterminate(true);
		}
	}

	@Override
	public void onServiceUnregistered(ComponentName name){
		Log.v("ArtistSoundcloudFragment", "Lost the reference to the service :-(");
		this.service = null;
	}
	///////////////////////////////////////////////////////////////////////
	// BEGIN AbstractServiceProxyActivity.ServiceProxyObserver callbacks //
	///////////////////////////////////////////////////////////////////////


	///////////////////////////////////////////////
	// BEGIN MusicPlayerService.Client callbacks //
	///////////////////////////////////////////////
	@Override
	public void onBufferProgressChanged(int bufferredPercentage){
		this.seekBar.setSecondaryProgress(bufferredPercentage * this.currentTrackDuration / 100);
	}

	@Override
	public void onPlayerStateChanged(MediaPlayerService.State state){
		switch(state){
			case Playing:
				this.setSeekBarIndeterminate(false);
				this.playButton.setChecked(true);
				break;
			case Stopped:
				this.playButton.setChecked(false);
				this.currentTrackDuration = 0;
				this.timeElapsedTextView.setText("");
				this.durationTextView.setText("");
				break;
			case Paused:
				this.playButton.setChecked(false);
				break;
			case Preparing:
				Log.v("ArtistSoundcloudFragment", "Service is now Preparing");
				this.setSeekBarIndeterminate(true);
				this.seekBar.setSecondaryProgress(0);
				this.currentTrackDuration = 0;
				this.timeElapsedTextView.setText("");
				this.durationTextView.setText("");
				break;
		}
	}

	@Override
	public void onPlayerProgressChanged(int progress, int duration){
		if(0 == this.currentTrackDuration){
			this.currentTrackDuration = duration;
			this.seekBar.setMax(duration);
			this.durationTextView.setText(StringUtils.formatDuration(duration / 1000));
		}
		if(!this.userSeeking){
			this.seekBar.setProgress(progress);
		}
	}
	/////////////////////////////////////////////
	// END MusicPlayerService.Client callbacks //
	/////////////////////////////////////////////

	class RetrieveTracksTask extends AsyncTask<String, Void, ArrayList<SoundcloudTrack>>{
        private static final String TAG = "RetrieveTracksTasks";
        private Exception exception;
		private final Context applicationContext = Application.getContext();

		protected ArrayList<SoundcloudTrack> doInBackground(String... args){
            if(1 != args.length) throw new IllegalArgumentException("The args parameter must contain only one element: the soundcloud username or user_id");
            String scUserId = args[0];

			DiskLruCache lruCache = null;
			try{
				lruCache = DiskLruCache.open(this.applicationContext.getFilesDir(), VersionUtils.getVersionCode(this.applicationContext), 1, 5 * 1024 * 1024);
			}
			catch(IOException e){
				Log.e(TAG, "Error opening the DiskLruCache.");
				if(BuildConfig.DEBUG) e.printStackTrace();
			}

			ArrayList<SoundcloudTrack> tracks = new ArrayList<SoundcloudTrack>();

            ApiWrapper wrapper = new ApiWrapper(ArtistSoundcloudFragment.this.getString(R.string.soundcloud_client_id), ArtistSoundcloudFragment.this.getString(R.string.soundcloud_client_secret), null, null);
            try{
	            String url = String.format(Locale.ENGLISH, "/users/%s/tracks.json", scUserId);
	            String cacheKey = StringUtils.hashMD5(url);
	            String jsonResponse = null;
	            if(null != lruCache){
		            try{
			            DiskLruCache.Snapshot snapshot = lruCache.get(cacheKey);
			            if(null != snapshot) jsonResponse = snapshot.getString(0);
		            }
		            catch(IOException e){
			            if(BuildConfig.DEBUG) e.printStackTrace();
			            Log.e(TAG, "Cache hit but unreadable.");
		            }
	            }

	            if(null == jsonResponse){
                    HttpResponse trackListResponse = wrapper.get(Request.to(url));
		            jsonResponse = EntityUtils.toString(trackListResponse.getEntity());
		            if(null != lruCache){
			            try{
				            DiskLruCache.Editor editor = lruCache.edit(cacheKey);
				            editor.set(0, jsonResponse);
				            editor.commit();
				            Log.w(TAG, "Cache not hit, but response cached successfully.");
			            }
			            catch(IOException e){
				            if(BuildConfig.DEBUG) e.printStackTrace();
				            Log.e(TAG, "Cache not hit and unable to cache response.");
			            }
		            }
		            else{
			            Log.e(TAG, "Cache unavailable.");
		            }
	            }

                JSONArray trackListJSONArray = new JSONArray(jsonResponse);

                JSONObject trackJSON;
	            SoundcloudTrack track;
                int length = trackListJSONArray.length();
                for(int i = 0; i < length; i++){
                    trackJSON = trackListJSONArray.getJSONObject(i);
                    if("track".equals(trackJSON.get("kind"))){
	                    track = new SoundcloudTrack(trackJSON.getInt("id"), trackJSON.getString("title"), trackJSON.getString("stream_url"));
                        track.setArtist(ArtistSoundcloudFragment.this.artist.getName());
	                    track.setDuration(trackJSON.getInt("duration"));
                        tracks.add(track);
                    }
                    else{
                        if(BuildConfig.DEBUG) Log.v(ArtistSoundcloudFragment.RetrieveTracksTask.TAG, String.format(Locale.ENGLISH, "Unexpected object kind: %s", trackJSON.get("kind")));
                    }
                }
            }
            catch(JSONException e) {
                if(BuildConfig.DEBUG) e.printStackTrace();
                this.exception = e;
            }
            catch(IOException e) {
                if(BuildConfig.DEBUG) e.printStackTrace();
                this.exception = e;
            }

            return tracks;
        }

        protected void onPostExecute(ArrayList<SoundcloudTrack> tracks){
            if(null == this.exception) {
                Intent serviceIntent = new Intent(ArtistSoundcloudFragment.this.getActivity(), StreamingMediaPlayerService.class);
                ArtistSoundcloudFragment.this.getActivity().startService(serviceIntent);

                ArtistSoundcloudFragment.this.listView.setAdapter(new SoundcloudTracksAdapter(Application.getContext(), tracks));

	            ArtistSoundcloudFragment.this.fragmentReady = true;

	            Activity activity = ArtistSoundcloudFragment.this.getActivity();
	            ArtistSoundcloudFragment.this.startService(activity, tracks);
	            ArtistSoundcloudFragment.this.requestServiceBond(activity);

	            // Initialize the media player's controls
	            ArtistSoundcloudFragment.this.initMediaController();
            }
            else{
                ArtistSoundcloudFragment.this.setErrorMessage(R.string.error_finding_soundcloud_profile);
                // Report through Piwik
            }
        }
    }

	/**
	 * Starts the service and request the activity to bind to it
	 * @param activity the fragment's activity
	 * @param tracks the list of {@link com.zion.htf.data.SoundcloudTrack}s to pass to the service
	 */
	private void startService(Activity activity, ArrayList<SoundcloudTrack> tracks){
		// Start the service, passing it the playlist
		Intent intent = new Intent(activity, MediaPlayerService.class);
		intent.putExtra(MediaPlayerService.EXTRA_TRACKS, tracks);
		intent.setAction(MediaPlayerService.ACTION_QUEUE_TRACKS);
		intent.putExtra(MediaPlayerService.EXTRA_ARTIST_ID, this.artist.getId());
		activity.startService(intent);
	}

	/**
	 * Requests the activity to bind to the service, to allow easier communication with the service
	 * @param activity the current fragment's activity, acting as a proxy to the service
	 */
	private void requestServiceBond(Activity activity){
		ArtistSoundcloudFragment.MediaPlayerServiceProxy serviceProxy = (ArtistSoundcloudFragment.MediaPlayerServiceProxy)activity;
		((AbstractServiceProxyActivity)activity).registerObserver(this);
		activity.bindService(new Intent(activity, MediaPlayerService.class), serviceProxy.getServiceConnection(), Context.BIND_AUTO_CREATE);
	}

	private void setSeekBarIndeterminate(boolean indeterminate){
		this.seekBar.setIndeterminate(indeterminate);
		if(16 <= Build.VERSION.SDK_INT) this.seekBar.getThumb().mutate().setAlpha(indeterminate ? 0x00 : 0xFF);
	}

	private void initMediaController(){
		ViewStub stub = (ViewStub)this.view.findViewById(R.id.mediaplayer_controller_view_stub);
		if(null != stub){
			ArtistSoundcloudFragment.Listeners listeners = new ArtistSoundcloudFragment.Listeners();

			stub.inflate();

			ToggleButton playButton      = (ToggleButton)this.view.findViewById(R.id.button_play);
			ToggleButton    shuffleButton   = (ToggleButton)this.view.findViewById(R.id.button_shuffle);
			ToggleButton    repeatButton    = (ToggleButton)this.view.findViewById(R.id.button_repeat);
			ImageButton prevButton      = (ImageButton)this.view.findViewById(R.id.button_prev);
			ImageButton     nextButton      = (ImageButton)this.view.findViewById(R.id.button_next);

			// Set button click listeners
			playButton.setOnClickListener(listeners);
			shuffleButton.setOnClickListener(listeners);
			repeatButton.setOnClickListener(listeners);
			prevButton.setOnClickListener(listeners);
			nextButton.setOnClickListener(listeners);

			// Get a reference to the mediaplayer controls
			this.seekBar = (SeekBar)this.view.findViewById(R.id.seekbar);
			this.timeElapsedTextView = (TextView)this.view.findViewById(R.id.elapsed_time);
			this.durationTextView = (TextView)this.view.findViewById(R.id.duration);
			this.playButton = playButton;
			this.shuffleButton = shuffleButton;
			this.repeatButton = repeatButton;

			this.seekBar.setOnSeekBarChangeListener(listeners);

		}
	}

	/**
	 * Interface used to get a reference to the {@link com.zion.htf.background.StreamingMediaPlayerService} in order to interact with it.
	 * Classes implementing this interface should keep a static reference to the {@link com.zion.htf.background.StreamingMediaPlayerService}
	 */
	public interface MediaPlayerServiceProxy{
		/**
		 * Return a reference to the {@link com.zion.htf.background.StreamingMediaPlayerService}
		 * @return A {@link java.lang.ref.WeakReference} to the {@link com.zion.htf.background.StreamingMediaPlayerService}
		 * @throws java.lang.IllegalStateException When trying to get a reference to a {@link com.zion.htf.background.StreamingMediaPlayerService} before it is bound.
		 */
		ServiceConnection getServiceConnection();
	}

	private class ListViewClickListener implements ListView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			ArtistSoundcloudFragment.this.start(position);
		}
	}

	private class Listeners implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{
		@Override
		public void onClick(View v){
			switch(v.getId()){
				case R.id.button_prev:
					ArtistSoundcloudFragment.this.prev();
					break;
				case R.id.button_next:
					ArtistSoundcloudFragment.this.next();
					break;
				case R.id.button_shuffle:
					ArtistSoundcloudFragment.this.shuffle(ArtistSoundcloudFragment.this.shuffleButton.isChecked());
					break;
				case R.id.button_repeat:
					ArtistSoundcloudFragment.this.repeat(ArtistSoundcloudFragment.this.repeatButton.isChecked());
					break;
				case R.id.button_play:
					if(ArtistSoundcloudFragment.this.playButton.isChecked())    ArtistSoundcloudFragment.this.start();
					else                                                        ArtistSoundcloudFragment.this.pause();
					break;
			}
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
			ArtistSoundcloudFragment.this.timeElapsedTextView.setText(StringUtils.formatDuration(progress / 1000));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar){
			ArtistSoundcloudFragment.this.userSeeking = true;
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar){
			ArtistSoundcloudFragment.this.userSeeking = false;
			ArtistSoundcloudFragment.this.seekTo(seekBar.getProgress());
		}
	}
}

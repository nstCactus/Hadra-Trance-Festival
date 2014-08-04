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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Request;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.background.StreamingMediaPlayerService;
import com.zion.htf.data.Artist;
import com.zion.htf.exception.MissingArgumentException;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtistSoundcloudFragment extends Fragment{
    public static final String ARG_ARTIST_ID = "com.zion.htf.arg.artist_id";
    public static final String ARG_SET_ID = "com.zion.htf.arg.set_id";
    private Artist artist;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        try{
            if(!args.containsKey(ArtistSoundcloudFragment.ARG_ARTIST_ID) && !args.containsKey(ArtistSoundcloudFragment.ARG_SET_ID)) throw new MissingArgumentException(String.format(Locale.ENGLISH, "Either %s or %s is required for this Activity to work properly. Please provide any of them.", ArtistDetailsFragment.ARG_SET_ID, ArtistDetailsFragment.ARG_ARTIST_ID));
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
        return inflater.inflate(R.layout.fragment_artist_soundcloud, container, false);
    }

    public void onPlayButtonClicked(){
        Intent startPlayingIntent = new Intent(this.getActivity(), StreamingMediaPlayerService.class);
        startPlayingIntent.setAction(StreamingMediaPlayerService.ACTION_PLAY);
        this.getActivity().startService(startPlayingIntent);
    }


    class RetrieveTracksTask extends AsyncTask<String, Void, List<Intent>> {
        private static final String TAG = "RetrieveTracksTasks";
        private Exception exception;

        protected List doInBackground(String... args){
            if(1 != args.length) throw new IllegalArgumentException("The args parameter must contain only one element: the soundcloud profile URL");
            String soundcloud_url = args[0];

            ArrayList<Intent> tracks = new ArrayList<Intent>();

            Pattern scUSerIdPattern = Pattern.compile("https?://(?:www\\.)?soundcloud\\.com/(.+)/?$");
            Matcher scUserIdMatcher;
            scUserIdMatcher = scUSerIdPattern.matcher(soundcloud_url);
            if(scUserIdMatcher.find()){
                String scUserId = scUserIdMatcher.group(1);
                ApiWrapper wrapper = new ApiWrapper(ArtistSoundcloudFragment.this.getString(R.string.soundcloud_client_id), ArtistSoundcloudFragment.this.getString(R.string.soundcloud_client_secret), null, null);
                try {
                    HttpResponse trackListResponse = wrapper.get(Request.to(String.format(Locale.ENGLISH, "/users/%s/tracks.json", scUserId), null));

                    JSONArray trackListJSONArray = new JSONArray(EntityUtils.toString(trackListResponse.getEntity()));

                    JSONObject trackJSON;
                    Intent intent;
                    int length = trackListJSONArray.length();
                    for(int i = 0; i < length; i++){
                        trackJSON = trackListJSONArray.getJSONObject(i);
                        if("track".equals(trackJSON.get("kind"))){
                            if(BuildConfig.DEBUG) Log.v(ArtistSoundcloudFragment.RetrieveTracksTask.TAG, String.format(Locale.ENGLISH, "Track found: %s (#%s)", trackJSON.get("title"), trackJSON.get("id")));
                            intent = new Intent();
                            intent.putExtra(StreamingMediaPlayerService.EXTRA_TRACK_STREAM_URL, trackJSON.getString("stream_url"));
                            intent.putExtra(StreamingMediaPlayerService.EXTRA_TRACK_TITLE, trackJSON.getString("title"));
                            intent.putExtra(StreamingMediaPlayerService.EXTRA_TRACK_ARTIST, ArtistSoundcloudFragment.this.artist.getName());
                            intent.putExtra(StreamingMediaPlayerService.EXTRA_TRACK_ID, trackJSON.getInt("id"));
                            tracks.add(intent);
                        }
                        else{
                            if(BuildConfig.DEBUG) Log.v(ArtistSoundcloudFragment.RetrieveTracksTask.TAG, String.format(Locale.ENGLISH, "Unexpected object kind: %s", trackJSON.get("kind")));
                        }
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                if(BuildConfig.DEBUG) Log.v(ArtistSoundcloudFragment.RetrieveTracksTask.TAG, String.format(Locale.ENGLISH, "No match found in string \"%s\"", soundcloud_url));
            }

            return tracks;
        }

        protected void onPostExecute(List<Intent> tracks){
            Intent serviceIntent = new Intent(ArtistSoundcloudFragment.this.getActivity(), StreamingMediaPlayerService.class);
            ArtistSoundcloudFragment.this.getActivity().startService(serviceIntent);

            for(Intent intent : tracks){
                intent.setAction(StreamingMediaPlayerService.ACTION_QUEUE_TRACK);
                ArtistSoundcloudFragment.this.getActivity().startService(intent);
            }
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}

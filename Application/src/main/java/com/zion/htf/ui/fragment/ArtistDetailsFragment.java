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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.data.Artist;
import com.zion.htf.data.MusicSet;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.exception.AlarmNotFoundException;
import com.zion.htf.exception.InconsistentDatabaseException;
import com.zion.htf.exception.MissingArgumentException;

import java.util.Locale;

public class ArtistDetailsFragment extends Fragment implements View.OnClickListener, TimeToPickerFragment.TimeToPickerInterface{
    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "ArtistDetailsFragment";
    public static final String ARG_SET_ID = "com.zion.htf.arg.set_id";
    public static final String ARG_ARTIST_ID = "com.zion.htf.arg.artist_id";
    private String facebook_url;
    private String website_url;
    private String soundcloud_url;
    private Artist artist;
    private int setId;
    private SavedAlarm alarm = null;
    private OnArtistFavoriteStatusChangedListener artistFavoriteStatusChangedListener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            this.artistFavoriteStatusChangedListener = (OnArtistFavoriteStatusChangedListener)activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        this.setHasOptionsMenu(true);
        Bundle args = this.getArguments();

        try{
            if(args.containsKey(ArtistDetailsFragment.ARG_SET_ID)){
                this.setId = args.getInt(ArtistDetailsFragment.ARG_SET_ID, 0);
                MusicSet musicSet = MusicSet.getById(this.setId);
                this.artist = musicSet.getArtist();

                try{
                    this.alarm = SavedAlarm.findBySetId(this.setId);
                }
                catch(AlarmNotFoundException e){
                    // Nothing to do, it's ok for a set not to have an alarm
                }
            }
            else if(args.containsKey(ArtistDetailsFragment.ARG_ARTIST_ID)){
                this.artist = Artist.getById(args.getInt(ArtistDetailsFragment.ARG_ARTIST_ID, 0));
            }
            else{
                throw new MissingArgumentException(String.format(Locale.ENGLISH, "Either %s or %s is required for this Activity to work properly. Please provide any of them.", ArtistDetailsFragment.ARG_SET_ID, ArtistDetailsFragment.ARG_ARTIST_ID));
            }
        }
        catch(Exception e){
            if(BuildConfig.DEBUG) e.printStackTrace();

            // Report this through piwik

            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_artist_details, container, false);

        // Display artist name
        TextView artist_name_field = (TextView)view.findViewById(R.id.artist_name);
        artist_name_field.setText(this.artist.getName());

        // Display label
        TextView label_field = (TextView)view.findViewById(R.id.label);
        String label = this.artist.getLabel();
        String origin = this.artist.getOrigin();
        if(0 < label.length())  label = origin + " / " + label;
        else					label = origin;
        label_field.setText(label);

        // Display picture
        ImageView artist_photo_field = (ImageView)view.findViewById(R.id.artist_photo);
        int resId = this.artist.getPictureResourceId();
        if(R.drawable.no_image == resId) artist_photo_field.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        artist_photo_field.setImageResource(resId);

        // Handle website button
        ImageButton website_button = (ImageButton)view.findViewById(R.id.website);
        this.website_url = this.artist.getWebsite();
        if(0 == this.website_url.length())  this.disable(website_button);
	    else                                website_button.setOnClickListener(this);

        // Handle facebook button
        ImageButton facebook_button = (ImageButton)view.findViewById(R.id.facebook);
        this.facebook_url = this.artist.getFacebook();
        if(0 == this.facebook_url.length()) this.disable(facebook_button);
	    else                                facebook_button.setOnClickListener(this);

        // Handle soundcloud button
        ImageButton soundcloud_button = (ImageButton)view.findViewById(R.id.soundcloud);
        this.soundcloud_url = this.artist.getSoundcloud();
        if(0 == this.soundcloud_url.length())   this.disable(soundcloud_button);
	    else                                    soundcloud_button.setOnClickListener(this);

        // Display bio
        TextView bio_field = (TextView)view.findViewById(R.id.bio);
        String bio;
        try{
            bio = this.artist.getBio("fr".equals(Locale.getDefault().getLanguage()) ? "fr" : "en");
        }
        catch(InconsistentDatabaseException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            // Just display an error message instead of the bio

            bio = this.getString(R.string.error_bio_inconsistent_database);

            // Report this through piwik
        }
        bio_field.setText(bio);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity)this.getActivity()).getSupportActionBar().setTitle(this.artist.getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.artist_details, menu);

        if(0 == this.setId){
            MenuItem addAlarmMenuItem = menu.findItem(R.id.action_addAlarm);
            MenuItem editAlarmMenuItem = menu.findItem(R.id.action_editAlarm);
            if(null != addAlarmMenuItem){
                addAlarmMenuItem.setVisible(false);
                addAlarmMenuItem.setEnabled(false);
            }
            if(null != editAlarmMenuItem){
                editAlarmMenuItem.setVisible(false);
                editAlarmMenuItem.setEnabled(false);
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        MenuItem addAlarmItem = menu.findItem(R.id.action_addAlarm);
        MenuItem editAlarmItem = menu.findItem(R.id.action_editAlarm);
        MenuItem toggleFavorite = menu.findItem(R.id.action_toggle_favorite);

        boolean areAlarmsEnabled = 0 != this.setId;
        boolean hasAlarm = null != this.alarm;

        // Add / edit alarm button
        if(null != addAlarmItem){
            addAlarmItem.setVisible(areAlarmsEnabled && !hasAlarm);
            addAlarmItem.setEnabled(areAlarmsEnabled && !hasAlarm);
        }
        if(null != editAlarmItem){
            editAlarmItem.setVisible(areAlarmsEnabled && hasAlarm);
            editAlarmItem.setEnabled(areAlarmsEnabled && hasAlarm);
        }

        // Favorite button
        if(this.artist.isFavorite()){
            toggleFavorite.setTitle(R.string.action_unset_favorite);
            toggleFavorite.setIcon(R.drawable.ic_menu_favorite);
        }
        else{
            toggleFavorite.setTitle(R.string.action_set_favorite);
            toggleFavorite.setIcon(R.drawable.ic_menu_not_favorite);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean ret = true;

        switch(item.getItemId()){
            case R.id.action_addAlarm:
                this.showAddAlarmDialog();
                break;

            case R.id.action_editAlarm:
                this.showEditAlarmDialog();
                break;

            case R.id.action_toggle_favorite:
                this.toggleFavoriteArtist();
                break;

            default:
                ret = false;
        }

        return ret;
    }

    /**
     * Displays a dialog to configure a new reminder for the current set
     */
    private void showAddAlarmDialog(){
        Bundle args = new Bundle();
        args.putInt("set_id", this.setId);
        DialogFragment newFragment = TimeToPickerFragment.newInstance(args);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(this.getActivity().getSupportFragmentManager(), "timeToPicker");
    }

    /**
     * Displays a dialog to edit a reminder for the current set
     */
    private void showEditAlarmDialog(){
        Bundle args = new Bundle();
        args.putInt("set_id", this.setId);
        args.putInt("alarm_id", this.alarm.getId());
        args.putBoolean("edit_mode", true);
        DialogFragment newFragment = TimeToPickerFragment.newInstance(args);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(this.getActivity().getSupportFragmentManager(), "timeToPicker");
    }

    /**
     * Toggle the favorite status of the current artist
     */
    private void toggleFavoriteArtist(){
        if(this.artist.toggleFavorite()){
            this.getActivity().supportInvalidateOptionsMenu();
            this.artistFavoriteStatusChangedListener.onArtistFavoriteStatusChanged(this.artist.isFavorite());
        }
    }


    /**************************************/
    /* BEGIN View.OnClickListener methods */
    /**************************************/
    @Override
    public void onClick(View v){
        Intent intent = null;

        switch(v.getId()){
            case R.id.website:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.website_url));
                break;

            case R.id.soundcloud:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.soundcloud_url));
                break;

            case R.id.facebook:
                try{
                    String facebookId = this.facebook_url.substring(this.facebook_url.lastIndexOf('/') + 1);
                    if(!facebookId.matches("\\d+")) throw new Exception("Non-numeric facebook id.");

                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + facebookId));
                    this.startActivity(intent);
                }
                catch(Exception e){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.facebook_url));
                }

                break;
        }
        if(null != intent) this.startActivity(intent);
    }
    /*************************************/
    /* END View.OnClickListener methods */
    /************************************/


    /****************************************************/
    /* BEGIN TimeToPickerFragment.TimeToPickerInterface */
    /****************************************************/
    @Override
    public void doPositiveClick(int id) {
        // Change the alarm icon
        try{
            this.alarm = SavedAlarm.getById(id);
        }
        catch(AlarmNotFoundException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            // Nothing to do, worst case scenario, the user will set another alarm for this set.
        }
        this.getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void doNegativeClick(){
        // Nothing to do, the user cancel the action
    }

    @Override
    public void doNeutralClick(int setId, int alarmId) {
        this.alarm = null;
        this.getActivity().supportInvalidateOptionsMenu();
    }
    /**************************************************/
    /* END TimeToPickerFragment.TimeToPickerInterface */
    /**************************************************/


    /**
     * Disable a button (delete the onclick listener and set its alpha to 0.5)
     * @param imageButton The ImageButton to disable
     */
    @SuppressLint("NewApi")
    private void disable(ImageButton imageButton){
        imageButton.setClickable(false);
        if(16 <= Build.VERSION.SDK_INT){
            imageButton.setImageAlpha(64);
        }
        else{
            //noinspection deprecation
            imageButton.setAlpha(64);
        }
    }

    public Artist getArtist(){
        return this.artist;
    }

    public interface OnArtistFavoriteStatusChangedListener{
        void onArtistFavoriteStatusChanged(boolean isFavorite);
    }
}

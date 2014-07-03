/*
    Copyright 2013 Yohann Bianchi

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
    or see <http://www.gnu.org/licenses/>.
 */

package com.zion.htf.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.data.Artist;
import com.zion.htf.data.MusicSet;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.exception.AlarmNotFoundException;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.ui.fragment.TimeToPickerFragment;

import java.util.Locale;

/**
 * This activity displays detailed information about an artist such as its name, label, origin, biography as well as links to its website, facebook page and souncloud profile.
 * The user can add a reminder to its set (if he navigated to the artist from {@link com.zion.htf.ui.LineUpActivity} or {@link com.zion.htf.ui.ArtistListActivity}).
 * For this activity to load properly, a {@code set_id} or {@code artist_id} (which are database identifiers of rows in tables {@code sets} and {@code artists}) is required.
 */
public class ArtistDetailsActivity extends ActionBarActivity implements View.OnClickListener, TimeToPickerFragment.TimeToPickerInterface{
	private static final String TAG = "ArtistDetailsActivity";
	private String facebook_url;
	private String website_url;
	private String soundcloud_url;
    private int setId;
    private int artistId;

    private Artist artist;
    private SavedAlarm alarm = null;

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_artist_details);

        try{
            Intent openingIntent = this.getIntent();
            if(!openingIntent.hasExtra("set_id") && !openingIntent.hasExtra("artist_id")) throw new MissingArgumentException("");
            this.setId = this.getIntent().getIntExtra("set_id", 0);
            this.artistId = openingIntent.getIntExtra("artist_id", 0);

            if(openingIntent.hasExtra("set_id")){
                MusicSet musicSet = MusicSet.getById(this.setId);
                this.artist = musicSet.getArtist();
                this.artistId = this.artist.getId();

                try{
                    this.alarm = SavedAlarm.findBySetId(musicSet.getId());
                }
                catch(AlarmNotFoundException e){
                    // Nothing to do, it's ok for a set not to have an alarm
                }
            }
            else if(openingIntent.hasExtra("artist_id")){
                this.artist = Artist.getById(this.artistId);
            }

            this.getSupportActionBar().setTitle(this.artist.getName());

            // Display artist name
            TextView artist_name_field = (TextView)this.findViewById(R.id.artist_name);
            artist_name_field.setText(this.artist.getName());

            // Display label
            TextView label_field = (TextView)this.findViewById(R.id.label);
            String label = this.artist.getLabel();
            String origin = this.artist.getOrigin();
            if(0 < label.length())  label = origin + " / " + label;
            else					label = origin;
            label_field.setText(label);

            // Display picture
            ImageView artist_photo_field = (ImageView)this.findViewById(R.id.artist_photo);
            int resId = this.artist.getPictureResourceId();
            if(R.drawable.no_image == resId) artist_photo_field.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            artist_photo_field.setImageResource(resId);

            // Handle website button
            ImageButton website_button = (ImageButton)this.findViewById(R.id.website);
            this.website_url = this.artist.getWebsite();
            if(0 == this.website_url.length()) this.disable(website_button);

            // Handle facebook button
            ImageButton facebook_button = (ImageButton)this.findViewById(R.id.facebook);
            this.facebook_url = this.artist.getFacebook();
            if(0 == this.facebook_url.length()) this.disable(facebook_button);

            // Handle soundcloud button
            ImageButton soundcloud_button = (ImageButton)this.findViewById(R.id.soundcloud);
            this.soundcloud_url = this.artist.getSoundcloud();
            if(0 == this.soundcloud_url.length()) this.disable(soundcloud_button);

            // Display bio
            TextView bio_field = (TextView)this.findViewById(R.id.bio);
            String bio = this.artist.getBio("fr".equals(Locale.getDefault().getLanguage()) ? "fr" : "en");
            bio_field.setText(bio);
        }
        catch(MissingArgumentException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            //TODO: Handle this properly
        }
        catch (Exception e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            //TODO: Handle this properly
        }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.artist_details, menu);

        if(!this.getIntent().hasExtra("set_id")){
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
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        MenuItem addAlarmItem = menu.findItem(R.id.action_addAlarm);
        MenuItem editAlarmItem = menu.findItem(R.id.action_editAlarm);
        MenuItem toggleFavorite = menu.findItem(R.id.action_toggle_favorite);

        boolean areAlarmsEnabled = this.getIntent().hasExtra("set_id");
        boolean hasAlarm = null != this.alarm;

        // Add / edit alarm button
        if(null != addAlarmItem){
            addAlarmItem.setVisible(areAlarmsEnabled && null == this.alarm);
            addAlarmItem.setEnabled(areAlarmsEnabled && null == this.alarm);
        }
        if(null != editAlarmItem){
            editAlarmItem.setVisible(areAlarmsEnabled && null != this.alarm);
            editAlarmItem.setEnabled(areAlarmsEnabled && null != this.alarm);
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

        return super.onPrepareOptionsMenu(menu);
    }

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
     * Toggle the favorite status of the current artist
     */
    private void toggleFavoriteArtist(){
        if(this.artist.toggleFavorite()) this.supportInvalidateOptionsMenu();
    }

    @Override
    //Play closing animation when activity is closed (using back button for instance)
    public void finish(){
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Displays a dialog to configure a new reminder for the current set
     */
    private void showAddAlarmDialog(){
        Bundle args = new Bundle();
        args.putInt("set_id", this.setId);
        DialogFragment newFragment = TimeToPickerFragment.newInstance(args);
        newFragment.show(this.getSupportFragmentManager(), "timeToPicker");
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
        newFragment.show(this.getSupportFragmentManager(), "timeToPicker");
    }

    /**
     * Disable an button (delete the onclick listener and set its alpha to 0.5)
     * @param imageButton The ImageButton to disable
     */
    private void disable(ImageButton imageButton){
		imageButton.setClickable(false);
		if(16 <= Build.VERSION.SDK_INT) imageButton.setImageAlpha(64);
		else imageButton.setAlpha(64);
	}

    @Override
    public void doPositiveClick(int id) {
        // Change the alarm icon
        try{
            this.alarm = SavedAlarm.getById(id);
        }
        catch(AlarmNotFoundException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            // TODO: Handle this properly
        }
        this.supportInvalidateOptionsMenu();
    }

    @Override
    public void doNegativeClick() {

    }

    @Override
    public void doNeutralClick(int setId, int alarmId) {
        this.alarm = null;
        this.supportInvalidateOptionsMenu();
    }
}

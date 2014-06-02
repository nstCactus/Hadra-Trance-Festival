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
import android.support.v7.app.ActionBar;
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
import com.zion.htf.ui.fragment.TimeToPickerFragment;

import java.util.Locale;

public class ArtistDetailsActivity extends ActionBarActivity implements View.OnClickListener{
	private static final String TAG = "ArtistDetailsActivity";
	private String facebook_url;
	private String website_url;
	private String soundcloud_url;
    private int setId;

    private Artist artist;

    //TODO: Refactor this Activity so that it holds an instance of Artist with all needed info

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_artist_details);

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

        this.setId = this.getIntent().getIntExtra("set_id", 0);

        try {
            this.artist = Artist.getBySetId(this.setId);

            this.getSupportActionBar().setTitle(this.artist.getArtistName());

            // Display artist name
            TextView artist_name_field = (TextView)this.findViewById(R.id.artist_name);
            artist_name_field.setText(this.artist.getArtistName());

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
        catch (Exception e){
            //TODO: Handle this properly
            e.printStackTrace();
        }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(BuildConfig.DEBUG){// FIXME: Remove this condition when alarms are ready
            this.getMenuInflater().inflate(R.menu.artist_details, menu);
        }
        return true;
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
			case android.R.id.home:
				this.finish();
				break;
            case R.id.action_addAlarm:
                // Open the add alarm popup
                this.showAddAlarmDialog();
                break;
			default:
				ret = false;
		}

		return ret;
	}

    @Override
    //Play closing animation when activity is closed (using back button for instance)
    public void finish(){
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Displays a dialog to configure a new reminder for the current artist
     */
    private void showAddAlarmDialog(){
        Bundle args = new Bundle();
        args.putLong("timestamp", this.artist.getSetBeginDate());
        args.putInt("set_id", this.setId);
        DialogFragment newFragment = TimeToPickerFragment.newInstance(args);
        newFragment.show(this.getSupportFragmentManager(), "timeToPicker");
    }

    /**
     * Disable an button (remove the onclick listener and set its alpha to 0.5)
     * @param imageButton The ImageButton to disable
     */
    private void disable(ImageButton imageButton){
		imageButton.setClickable(false);
		if(Build.VERSION.SDK_INT >= 16) imageButton.setImageAlpha(64);
		else imageButton.setAlpha(64);
	}
}

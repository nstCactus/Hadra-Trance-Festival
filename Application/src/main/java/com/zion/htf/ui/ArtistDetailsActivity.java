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
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.ui.fragment.TimeToPickerFragment;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.Locale;

public class ArtistDetailsActivity extends SherlockFragmentActivity implements View.OnClickListener{
	private static final String TAG               = "ArtistDetailsActivity";
	private static final int    COLUMN_NAME       = 1;
	private static final int    COLUMN_GENRE      = 2;
	private static final int    COLUMN_ORIGIN     = 3;
	private static final int    COLUMN_PICTURE    = 4;
	private static final int    COLUMN_COVER      = 5;
	private static final int    COLUMN_WEBSITE    = 6;
	private static final int    COLUMN_FACEBOOK   = 7;
	private static final int    COLUMN_SOUNDCLOUD = 8;
	private static final int    COLUMN_LABEL      = 9;
	private static final int    COLUMN_BIO        = 11;// Column 10 is foreign key bios(id)
	private static final int	COLUMN_BIO_ID	  = 12;
	private String facebook_url;
	private String website_url;
	private String soundcloud_url;
	private SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_artist_details);

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);


		int artist_id = this.getIntent().getIntExtra("artist_id", 0);

		String langCode = Locale.getDefault().getLanguage().equals("fr") ? "fr" : "en";
		final Cursor cursor = this.dbOpenHelper.getReadableDatabase().rawQuery(String.format("SELECT artists.*, bios.text, bio FROM artists LEFT JOIN bios ON artists.bio = bios.id AND lang_code = '%s' WHERE artists.id = %d;", langCode, artist_id), null);

		if(cursor.moveToNext()){
			String artist_name = cursor.getString(COLUMN_NAME);
			this.getSupportActionBar().setTitle(artist_name);

			TextView artist_name_field = (TextView)this.findViewById(R.id.artist_name);
			artist_name_field.setText(artist_name);

			TextView label_field = (TextView)this.findViewById(R.id.label);
			String label = "";
			if(!cursor.isNull(COLUMN_LABEL)) label = cursor.getString(COLUMN_LABEL);
			if(!cursor.isNull(COLUMN_ORIGIN)){
				String origin = cursor.getString(COLUMN_ORIGIN);
				if(label.length() > 0) label = origin + " / " + label;
				else					label = origin;
			}
			label_field.setText(label);

			ImageView artist_photo_field = (ImageView)this.findViewById(R.id.artist_photo);
			int resId;
			if(cursor.isNull(COLUMN_PICTURE) || 0 == (resId = this.getResources().getIdentifier(cursor.getString(COLUMN_PICTURE), "drawable", "com.zion.htf"))){
				resId = R.drawable.no_image;
				artist_photo_field.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			}
			artist_photo_field.setImageResource(resId);

			ImageButton website_button = (ImageButton)this.findViewById(R.id.website);
			this.website_url = cursor.getString(COLUMN_WEBSITE);
			if(null == this.website_url || this.website_url.length() == 0) this.disable(website_button);

			ImageButton facebook_button = (ImageButton)this.findViewById(R.id.facebook);
			this.facebook_url = cursor.getString(COLUMN_FACEBOOK);
			if(null == this.facebook_url || this.facebook_url.length() == 0) this.disable(facebook_button);

			ImageButton soundcloud_button = (ImageButton)this.findViewById(R.id.soundcloud);
			this.soundcloud_url = cursor.getString(COLUMN_SOUNDCLOUD);
			if(null == this.soundcloud_url || this.soundcloud_url.length() == 0) this.disable(soundcloud_button);

			TextView bio_field = (TextView)this.findViewById(R.id.bio);
			String bio = cursor.getString(COLUMN_BIO);
			if(!cursor.isNull(COLUMN_BIO_ID) && (bio == null || bio.equals(""))){
				Cursor bioCursor = this.dbOpenHelper.getReadableDatabase().rawQuery("SELECT text FROM bios WHERE id = ?;", new String[]{String.valueOf(cursor.getInt(COLUMN_BIO_ID))});
				if(bioCursor.moveToNext()) bio = bioCursor.getString(0);
			}
			if(bio != null) bio_field.setText(bio);
		}
		else{
			Log.e(TAG, "No artist found matching id '" + artist_id + "'.");
		}
		if(!cursor.isClosed()) cursor.close();
		this.dbOpenHelper.close();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getSupportMenuInflater().inflate(R.menu.artist_details, menu);
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

		if(intent != null) this.startActivity(intent);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item){
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

    private void showAddAlarmDialog() {
        DialogFragment newFragment = new TimeToPickerFragment();
        newFragment.show(this.getSupportFragmentManager(), "timeToPicker");
    }

    private void disable(ImageButton iv){
		iv.setClickable(false);
		if(Build.VERSION.SDK_INT >= 16) iv.setImageAlpha(64);
		else iv.setAlpha(64);
	}
}

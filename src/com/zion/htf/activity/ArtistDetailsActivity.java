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

package com.zion.htf.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.zion.htf.DatabaseOpenHelper;
import com.zion.htf.R;

public class ArtistDetailsActivity extends SherlockFragmentActivity implements View.OnClickListener {
    private static final String TAG = "ArtistDetailsActivity";
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_GENRE = 2;
    private static final int COLUMN_ORIGIN = 3;
    private static final int COLUMN_PICTURE = 4;
    private static final int COLUMN_WEBSITE = 5;
    private static final int COLUMN_FACEBOOK = 6;
    private static final int COLUMN_MYSPACE = 7;
    private static final int COLUMN_SOUNDCLOUD = 8;
    private static final int COLUMN_LABEL = 9;
    private static final int COLUMN_BIO = 10;
    private static final int COLUMN_COVER = 11;

    private String facebook_url;
    private String website_url;
    private String soundcloud_url;
    private String myspace_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        int artist_id = this.getIntent().getIntExtra("artist_id", 0);

        DatabaseOpenHelper dbOpenHelper = new DatabaseOpenHelper(this);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        final Cursor cursor = database.rawQuery("SELECT * FROM artists WHERE id = " + artist_id + ";", null);

        if(cursor.moveToNext()){
            this.getSupportActionBar().setTitle(cursor.getString(COLUMN_NAME));

            TextView label_field = (TextView)this.findViewById(R.id.label);
            label_field.setText(cursor.getString(COLUMN_LABEL));

            ImageView artist_cover_field = (ImageView)this.findViewById(R.id.artist_cover);

            TextView origin_field = (TextView)this.findViewById(R.id.origin);
            origin_field.setText(cursor.getString(COLUMN_ORIGIN));

            TextView genre_field = (TextView)this.findViewById(R.id.genre);
            genre_field.setText(cursor.getString(COLUMN_GENRE));

            ImageView website_button = (ImageView)this.findViewById(R.id.website);
            this.website_url = cursor.getString(COLUMN_WEBSITE);
            if(website_url.length() > 0)    website_button.setOnClickListener(this);
            else                            this.dimButton(website_button);

            ImageView facebook_button = (ImageView)this.findViewById(R.id.facebook);
            this.facebook_url = cursor.getString(COLUMN_FACEBOOK);
            if(this.facebook_url.length() > 0)  facebook_button.setOnClickListener(this);
            else                                this.dimButton(facebook_button);

            ImageView myspace_button = (ImageView)this.findViewById(R.id.myspace);
            this.myspace_url = cursor.getString(COLUMN_MYSPACE);
            if(myspace_url.length() > 0)    myspace_button.setOnClickListener(this);
            else                            this.dimButton(myspace_button);

            ImageView soundcloud_button = (ImageView)this.findViewById(R.id.soundcloud);
            this.soundcloud_url = cursor.getString(COLUMN_SOUNDCLOUD);
            if(soundcloud_url.length() > 0) soundcloud_button.setOnClickListener(this);
            else                            this.dimButton(soundcloud_button);

            TextView bio_field = (TextView)this.findViewById(R.id.bio);
            bio_field.setText(cursor.getString(COLUMN_BIO));
        }
        else{
            Log.e(TAG, "No artist found matching id '" + artist_id + "'.");
        }
        if(!cursor.isClosed()) cursor.close();
        database.close();
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

            case R.id.myspace:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.myspace_url));
                break;

            case R.id.facebook:
                try {
                    String facebookId = this.facebook_url.substring(this.facebook_url.lastIndexOf("/") + 1);
                    if(!facebookId.matches("\\d+")) throw new Exception("Non-numeric facebook id.");

                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + facebookId));
                    startActivity(intent);
                }
                catch(Exception e){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.facebook_url));
                }

                break;
        }

        if(intent != null) this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret = true;

        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;

            default:
                ret = false;
        }

        return ret;
    }

        private void dimButton(ImageView iv){
        if(Build.VERSION.SDK_INT >= 16) iv.setImageAlpha(64);
        else                            iv.setAlpha(64);
    }
}

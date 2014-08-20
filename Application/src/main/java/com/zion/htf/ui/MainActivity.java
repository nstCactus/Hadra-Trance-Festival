/*
 *
 *     Copyright 2013-2015 Yohann Bianchi
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

package com.zion.htf.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.data.UpdateArtistOnStageTask;

import java.lang.ref.WeakReference;
import java.util.Timer;

/*
TODO: Implement homeAsUp navigation
TODO: Check that notification back-button and homeAsUp works as it should (http://developer.android.com/training/implementing-navigation/)
 */
public class MainActivity extends ActionBarActivity{
	private static final String TAG                             = "MainActivity";
	private static final int 	ARTIST_ON_STAGE_UPDATE_INTERVAL = 10000;
	protected Timer   	        artistOnStageTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_main);
	}

	@Override
	public void onPause(){
		if(null != this.artistOnStageTimer){
			this.artistOnStageTimer.cancel();
			this.artistOnStageTimer.purge();
		}
		super.onPause();
	}

	@Override
	public void onResume(){
		super.onResume();
		this.initNowOnStageTimer();
	}

	private void initNowOnStageTimer(){
		if(null == this.artistOnStageTimer) this.artistOnStageTimer = null;
		this.artistOnStageTimer = new Timer();
		WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(this);
		this.artistOnStageTimer.schedule(new UpdateArtistOnStageTask(activityWeakReference), 0, MainActivity.ARTIST_ON_STAGE_UPDATE_INTERVAL);
	}

	@Override
	protected void onDestroy(){
		if(null != this.artistOnStageTimer) this.artistOnStageTimer.cancel();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.main, menu);
        if(BuildConfig.DEBUG){
            this.getMenuInflater().inflate(R.menu.main_debug, menu);

            MenuItem helpMenuItem = menu.findItem(R.id.action_help);
            helpMenuItem.setVisible(true);
        }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		boolean ret = true;

		switch(item.getItemId()){
			case R.id.action_donate:
				this.startActivity(new Intent(this, DonateActivity.class));
				break;

            case R.id.action_lineUp:
                this.onButtonClicked(this.findViewById(R.id.button_lineup));
                break;

            case R.id.action_settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.action_alarms:
                this.startActivity(new Intent(this, AlarmManagerActivity.class));
                break;

            case R.id.action_favorite_artists:
                this.startActivity(new Intent(this, FavoriteArtistsManagerActivity.class));
                break;

            case R.id.action_about:
                Intent intent = new Intent(this, InfoDetailsActivity.class);
                intent.putExtra(InfoDetailsActivity.name, "info_about");
                this.startActivity(intent);
                break;

            default:
				ret = false;
		}

		return ret;
	}

	public void onButtonClicked(View view){
		Intent intent = null;
		switch(view.getId()){
			case R.id.button_map:
				intent = new Intent(this, MapActivity.class);
				break;
			case R.id.button_lineup:
				intent = new Intent(this, LineUpActivity.class);
				break;
            case R.id.button_artists:
                intent = new Intent(this, ArtistListActivity.class);
                break;
			case R.id.button_tickets:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hadra.net/shop/new/category.php?id_category=6"));
				break;
			case R.id.button_infos:
				intent = new Intent(this, InfoActivity.class);
				break;
			case R.id.button_facebook:
				// Facebook needs special treatment if we want the mobile app to open
				try{
					this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/327380017292731")));
				}
				catch(ActivityNotFoundException e){
					this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/hadratrancefestival")));
				}
				break;
			case R.id.button_hadra:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hadra.net"));
				break;
			case R.id.button_soundcloud:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://soundcloud.com/hadratrancefestival"));
				break;
			case R.id.button_youtube:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/hadrarecords"));
				break;
            case R.id.button_music:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=hadra+trance+festival&c=music&docType=2"));
                break;
            case R.id.button_flickr:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flickr.com/photos/118919035@N06/sets/"));
                break;
			default:
		}

		if(null != intent) this.startActivity(intent);
	}

	public void onNowOnStageClicked(View view){
		if(null != this.artistOnStageTimer){
			this.artistOnStageTimer.cancel();
			this.artistOnStageTimer.purge();
		}
		this.initNowOnStageTimer();
	}
}

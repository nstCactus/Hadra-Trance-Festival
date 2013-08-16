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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.zion.htf.R;

public class MainActivity extends SherlockActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		this.getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item){
		boolean ret = true;

		switch(item.getItemId()){
			case R.id.action_donate:
				this.startActivity(new Intent(this, DonateActivity.class));
				break;

			case R.id.action_about:
				this.startActivity(new Intent(this, AboutActivity.class));
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
			case R.id.button_tickets:
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.hadra.net/shop/new/category.php?id_category=6"));
				break;
			case R.id.button_infos:
				intent = new Intent(this, AboutActivity.class);
				break;
			case R.id.button_facebook:
				// Facebook needs special treatment if we want the mobile app to open
				try{
					this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/353173845932")));
				}
				catch(ActivityNotFoundException e){
					this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Hadra.official")));
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
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/music/album?id=Bzgehxymgika5tgar5nlhfjkhqq"));
				break;
			default:
		}

		if(null != intent) this.startActivity(intent);
	}
}

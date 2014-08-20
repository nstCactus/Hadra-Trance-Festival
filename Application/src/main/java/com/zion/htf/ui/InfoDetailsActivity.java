/*
 * Copyright 2013 Yohann Bianchi
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or see <http://www.gnu.org/licenses/>.
 */

package com.zion.htf.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zion.htf.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InfoDetailsActivity extends ActionBarActivity{
	private static final String TAG  = "InfoDetailsActivity";
	public static        String name = "name";
	public WebView webView;
	public Context context;

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);

		this.context = this;

		String resourceName = this.getIntent().getStringExtra(InfoDetailsActivity.name);
		if(null == resourceName){
			throw new RuntimeException("This requires a 'name' parameter in its starting intent.");
		}

        this.setTitle(this.getString(this.getResources().getIdentifier(resourceName, "string", "com.zion.htf")));

		this.webView = new WebView(this);
		this.webView.loadDataWithBaseURL("file:///android_res/raw/", this.readTextFromResource(resourceName), "text/html", "utf-8", null);
		this.webView.setBackgroundColor(0x00000000);
		if("info_about".equals(resourceName) || "info_open_source".equals(resourceName)) this.webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				Log.v(InfoDetailsActivity.TAG, url);
				boolean ret = false;
				if(url.contains("github.com/nstCactus")){
                    InfoDetailsActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nstCactus/Hadra-Trance-Festival")));
					ret = true;
				}
				else if(url.contains("donate.me")){
                    InfoDetailsActivity.this.startActivity(new Intent(InfoDetailsActivity.this, DonateActivity.class));
					ret = true;
				}

				return ret;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon){
				Log.v(InfoDetailsActivity.TAG, "onPageStarted called");
				if(shouldOverrideUrlLoading(view, url)){
					view.stopLoading();
				}
			}
		});

		this.setContentView(this.webView);

	}

	private String readTextFromResource(int resourceID){
		InputStream raw = this.getResources().openRawResource(resourceID);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int i;
		try{
			i = raw.read();
			while(-1 != i){
				stream.write(i);
				i = raw.read();
			}
			raw.close();
		}
		catch(IOException e){
			Log.e(InfoDetailsActivity.TAG, "Error reading resource with id " + resourceID, e);
		}
		catch(Resources.NotFoundException e){
			Log.e(InfoDetailsActivity.TAG, "No resource found matching id " + resourceID, e);
		}
		return stream.toString();
	}

	private String readTextFromResource(String resourceEntryName){
		return this.readTextFromResource(this.getResources().getIdentifier(resourceEntryName, "raw", "com.zion.htf"));
	}

	@Override
	//Play closing animation when activity is closed (using back button for instance)
	public void finish(){
		super.finish();
		this.overridePendingTransition(R.anim.slide_and_fade_in_left, R.anim.slide_and_fade_out_right);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		boolean ret = false;

		switch(item.getItemId()){
			case android.R.id.home:
				//Play closing animation on homeAsUp
				NavUtils.navigateUpFromSameTask(this);
				this.overridePendingTransition(R.anim.slide_and_fade_in_left, R.anim.slide_and_fade_out_right);
				ret = true;
				break;
		}

		return ret;
	}
}

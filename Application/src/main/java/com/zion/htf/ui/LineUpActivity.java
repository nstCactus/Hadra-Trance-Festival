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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hb.views.PinnedSectionListView;
import com.viewpagerindicator.PageIndicator;
import com.zion.htf.R;
import com.zion.htf.adapter.LineUpPagerAdapter;
import com.zion.htf.data.Item;
import com.zion.htf.data.MusicSet;

import java.util.Date;

public class LineUpActivity extends ActionBarActivity {
	private static final int CAUSE_TOO_EARLY = 0;
	private static final int CAUSE_TOO_LATE  = 1;

	private LineUpPagerAdapter pagerAdpater;
	private ViewPager          viewPager;
	private PageIndicator      pageIndicator;

	public static int sectionHeaderHeight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_line_up);

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		this.pagerAdpater = new LineUpPagerAdapter(this.getSupportFragmentManager());

		this.viewPager = (ViewPager)this.findViewById(R.id.line_up_pager);
		this.viewPager.setAdapter(this.pagerAdpater);

		this.pageIndicator = (PageIndicator)this.findViewById(R.id.indicator);
		this.pageIndicator.setViewPager(this.viewPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		this.getMenuInflater().inflate(R.menu.line_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		boolean ret = true;

		switch(item.getItemId()){
			case android.R.id.home:
				this.finish();
				break;

			case R.id.action_onStage:
				ret = this.scrollToCurrentSet();
				break;

			default:
				ret = false;
		}

		return ret;
	}

	private boolean scrollToCurrentSet(){
		boolean ret = false;

		Date now = new Date();

		if(now.before(new Date(1408645800000l))){
			this.displayErrorMessage(CAUSE_TOO_EARLY);
		}
		else if(now.after(new Date(1408896000000l))){
			this.displayErrorMessage(CAUSE_TOO_LATE);
		}
		else{
			ret = true;
			boolean found = false;

			Fragment fragment = this.pagerAdpater.getRegisteredFragment(this.viewPager.getCurrentItem());

			PinnedSectionListView listView = (PinnedSectionListView)fragment.getView().findViewById(R.id.line_up_list);

			int i = -1;
			Item item;

			while(!found && ++i < listView.getCount()){
				item = (Item)listView.getItemAtPosition(i);
				if(item.getType() == Item.TYPE_ITEM){
					if(((MusicSet)item).getEndDate().after(now)){
						found = true;

						if(Build.VERSION.SDK_INT >= 11){
							listView.smoothScrollToPositionFromTop(i, LineUpActivity.sectionHeaderHeight);
						}
						else{
							listView.setSelectionFromTop(i, LineUpActivity.sectionHeaderHeight);
						}
					}
				}
			}
		}

		return ret;
	}

	private void displayErrorMessage(int cause){
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
		switch(cause){
			case 0:
				alertBuilder.setTitle(R.string.error_too_early_title)
							.setMessage(R.string.error_too_early_message);
				break;
			default:
				alertBuilder.setTitle(R.string.error_too_late_title)
							.setMessage(R.string.error_too_late_message);
		}
		alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
			}
		});
		alertBuilder.create().show();
	}
}

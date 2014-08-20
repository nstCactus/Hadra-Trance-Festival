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
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zion.content.SQLiteCursorLoader;
import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.adapter.LineUpPagerAdapter;
import com.zion.htf.data.Festival;
import com.zion.htf.data.MusicSet;
import com.zion.htf.ui.fragment.LineUpListFragment;

import java.util.Date;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class LineUpActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final int CAUSE_TOO_EARLY = 0;
	private static final int CAUSE_TOO_LATE  = 1;
    private static final int LOADER_ID = 5003;

    private ViewPager          viewPager;

    private LineUpPagerAdapter pagerAdapter;
    private ActionBar actionBar;

    private final ActionBar.TabListener tabListener = new ActionBar.TabListener() {

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            LineUpActivity.this.viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    };

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_line_up);

        this.actionBar = this.getSupportActionBar();
        this.actionBar.setHomeButtonEnabled(true);
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                LineUpActivity.this.actionBar.setSelectedNavigationItem(position);
            }
        };

		this.viewPager = (ViewPager)this.findViewById(R.id.line_up_pager);

        this.viewPager.getAdapter();
        this.pagerAdapter = new LineUpPagerAdapter(this.getSupportFragmentManager(), LineUpListFragment.class, new String[]{LineUpListFragment.ARG_STAGE_NAME}, null);
        this.getSupportLoaderManager().initLoader(LineUpActivity.LOADER_ID, null, this).forceLoad();

        this.viewPager.setOnPageChangeListener(pageChangeListener);
		this.viewPager.setAdapter(this.pagerAdapter);
	    this.viewPager.setOffscreenPageLimit(2);
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
		boolean found = false;

		Date now = new Date();

		if(now.before(Festival.getFestivalStartDate())){
			this.displayErrorMessage(LineUpActivity.CAUSE_TOO_EARLY);
		}
		else if(now.after(Festival.getFestivalEndDate())){
			this.displayErrorMessage(LineUpActivity.CAUSE_TOO_LATE);
		}
		else{
			ret = true;


			LineUpListFragment currentFragment = this.pagerAdapter.getCurrentFragment();
			View currentView = currentFragment.getView();
			if(null != currentView){
				StickyListHeadersListView listView = (StickyListHeadersListView)currentView.findViewById(R.id.line_up_list);

				int position = -1;
				int itemCount = listView.getCount();

				while(!found && ++position < itemCount){
					Cursor cursor = (Cursor)listView.getItemAtPosition(position);
					if(new Date(cursor.getLong(MusicSet.COLUMN_END_DATE) * 1000).after(now)){
						found = true;
						listView.smoothScrollToPosition(position);
						Log.v("LineUpActivity", String.format(Locale.ENGLISH, "Current set is %s, at pos %d", cursor.getString(MusicSet.COLUMN_ARTIST_NAME), position));
					}
				}
				if(!found){
					// Jump to the last set
					listView.smoothScrollToPosition(itemCount - 1);
				}
			}
			else{
				throw new RuntimeException("Cannot get the currently displayed fragment.");
				// Report through piwik
			}
		}

		return ret && found;
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


    /***********************************************/
    /* BEGIN LoaderManager.LoaderCallbacks methods */
    /***********************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        return new SQLiteCursorLoader(this, Application.getDbHelper().getReadableDatabase(), "SELECT stage FROM lst__stages;", null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
        this.pagerAdapter.swapCursor(cursor);

        this.actionBar.removeAllTabs();
        ActionBar.Tab tab;
        for(int i = 0; i < this.pagerAdapter.getCount(); i++){
            tab = this.actionBar.newTab()
                    .setText(this.pagerAdapter.getPageTitle(i))
                    .setTabListener(this.tabListener);
            this.actionBar.addTab(tab);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader){
        this.pagerAdapter.changeCursor(null);
    }
    /*********************************************/
    /* END LoaderManager.LoaderCallbacks methods */
    /*********************************************/
}

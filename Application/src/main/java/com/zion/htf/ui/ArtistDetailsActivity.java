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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.adapter.ArtistDetailsPagerAdapter;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.ui.fragment.ArtistDetailsFragment;
import com.zion.htf.ui.fragment.ArtistSoundcloudFragment;

import java.util.Locale;

/**
 * This activity displays detailed information about an artist such as its name, label, origin, biography as well as links to its website, facebook page and souncloud profile.
 * The user can add a reminder to its set (if he navigated to the artist from {@link com.zion.htf.ui.LineUpActivity} or {@link com.zion.htf.ui.ArtistListActivity}).
 * For this activity to load properly, a {@code set_id} or {@code artist_id} (which are database identifiers of rows in tables {@code sets} and {@code artists}) is required.
 */
public class ArtistDetailsActivity extends ActionBarActivity implements ArtistDetailsFragment.OnArtistFavoriteStatusChangedListener{
	@SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "ArtistDetailsActivity";
    public static final String EXTRA_SET_ID = "com.zion.htf.extra.set_id";
    public static final String EXTRA_ARTIST_ID = "com.zion.htf.extra.artist_id";
    private ActionBar actionBar;

    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_artist_details);
        this.actionBar = this.getSupportActionBar();

        Intent openingIntent = this.getIntent();
        try{
            Bundle args = new Bundle(1);

            if(openingIntent.hasExtra(ArtistDetailsActivity.EXTRA_SET_ID)){
                args.putInt(ArtistDetailsFragment.ARG_SET_ID, openingIntent.getIntExtra(ArtistDetailsActivity.EXTRA_SET_ID, 0));
                args.putInt(ArtistSoundcloudFragment.ARG_SET_ID, openingIntent.getIntExtra(ArtistDetailsActivity.EXTRA_SET_ID, 0));
            }
            else if(openingIntent.hasExtra(ArtistDetailsActivity.EXTRA_ARTIST_ID)){
                args.putInt(ArtistDetailsFragment.ARG_ARTIST_ID, openingIntent.getIntExtra(ArtistDetailsActivity.EXTRA_ARTIST_ID, 0));
                args.putInt(ArtistSoundcloudFragment.ARG_ARTIST_ID, openingIntent.getIntExtra(ArtistDetailsActivity.EXTRA_ARTIST_ID, 0));
            }
            else{
                throw new MissingArgumentException(String.format(Locale.ENGLISH, "Either %s or %s is required for this Activity to work properly. Please provide any of them.", ArtistDetailsFragment.ARG_SET_ID, ArtistDetailsFragment.ARG_ARTIST_ID));
            }

            this.pagerAdapter = new ArtistDetailsPagerAdapter(this.getSupportFragmentManager(), args);
            this.viewPager = (ViewPager)this.findViewById(R.id.artist_details_pager);

            this.initViewPager();
            this.initTabs();

//            if(0 < this.artist.getSoundcloud().length()){
//                new RetrieveTracksTask().execute(this.artist.getSoundcloud());
//            }
        }
        catch(MissingArgumentException e){
            if(BuildConfig.DEBUG) e.printStackTrace();

            // Report this through piwik

            throw new RuntimeException(e);
        }
    }

    private void initViewPager() {
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(ActionBar.NAVIGATION_MODE_TABS == ArtistDetailsActivity.this.actionBar.getNavigationMode()){
                    ArtistDetailsActivity.this.actionBar.setSelectedNavigationItem(position);
                }
            }
        };
        this.viewPager.setAdapter(this.pagerAdapter);
        this.viewPager.setOnPageChangeListener(pageChangeListener);
    }

    private void initTabs(){
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft){
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){
                ArtistDetailsActivity.this.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft){
            }
        };

        this.actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tab = this.actionBar.newTab()
                .setText("Tab1")
                .setTabListener(tabListener);

        this.actionBar.addTab(tab);

        tab = this.actionBar.newTab()
                .setText("Tab2")
                .setTabListener(tabListener);

        this.actionBar.addTab(tab);
    }

    @Override
    //Play closing animation when activity is closed (using back button for instance)
    public void finish(){
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onArtistFavoriteStatusChanged(boolean isFavorite) {
        this.setResult(isFavorite ? Activity.RESULT_CANCELED : Activity.RESULT_OK);

    }
}

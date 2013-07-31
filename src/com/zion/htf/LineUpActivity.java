package com.zion.htf;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.viewpagerindicator.PageIndicator;

public class LineUpActivity extends FragmentActivity{
    private LineUpPagerAdapter pagerAdpater;
    private ViewPager viewPager;
    private PageIndicator pageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_up);

        pagerAdpater = new LineUpPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdpater);

        pageIndicator = (PageIndicator)findViewById(R.id.indicator);
        pageIndicator.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.line_up, menu);
        return true;
    }

    class LineUpPagerAdapter extends FragmentPagerAdapter{
        private final String[] TAB_TITLES = new String[] { getString(R.string.main_stage), getString(R.string.chill_out) };
        private int count = TAB_TITLES.length;

        public LineUpPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position){
            Fragment fragment;
            switch(position){
                case 0:
                    fragment = new Fragment();
                    break;
                default:
                    fragment = new Fragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount(){
            return this.count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }
}

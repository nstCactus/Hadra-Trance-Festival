package com.zion.htf;

import java.util.List;
import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.viewpagerindicator.PageIndicator;

public class LineUpActivity extends SherlockFragmentActivity {
    private LineUpPagerAdapter pagerAdpater;
    private ViewPager viewPager;
    private PageIndicator pageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_up);

        // Ajout des fragments du viewPager
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, MainStageLineUpFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, AlternativeStageLineUpFragment.class.getName()));
        pagerAdpater = new LineUpPagerAdapter(this.getSupportFragmentManager(), fragments);

        viewPager = (ViewPager)this.findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdpater);

        pageIndicator = (PageIndicator)findViewById(R.id.indicator);
        pageIndicator.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getSupportMenuInflater().inflate(R.menu.line_up, menu);
        return true;
    }

    class LineUpPagerAdapter extends FragmentPagerAdapter{
        private final String[] TAB_TITLES = new String[] { getString(R.string.main_stage), getString(R.string.chill_out) };
        private int count = TAB_TITLES.length;
        private List<Fragment> fragments;

        public LineUpPagerAdapter(FragmentManager fm, List<Fragment> fragments){
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position){
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }
}

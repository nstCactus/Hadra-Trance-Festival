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
import com.actionbarsherlock.view.MenuItem;
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
}

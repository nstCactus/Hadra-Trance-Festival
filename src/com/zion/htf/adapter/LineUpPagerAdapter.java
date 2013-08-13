package com.zion.htf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.zion.htf.fragment.LineUpListFragment;

public class LineUpPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private static final String TAG = "LineUpPagerAdapter";

    public LineUpPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        Log.v(TAG, "Instantiating a new fragment for the viewPager");
        String stageName;
        switch(position){
            case 0:
                stageName = "Main stage";
                break;

            default:
                stageName = "Alternative stage";
        }
        Fragment fragment = LineUpListFragment.newInstance(stageName);

        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles[position];
    }

    public void setTabTitles(String[] titles){
        this.tabTitles = titles;
    }
}

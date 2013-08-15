package com.zion.htf.adapter;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.zion.htf.Application;
import com.zion.htf.ui.LineUpListFragment;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.ArrayList;

public class LineUpPagerAdapter extends FragmentPagerAdapter{
	private String[] tabTitles;
	private              SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
	private static final String                TAG                 = "LineUpPagerAdapter";
	private static       ArrayList<String>     stages              = new ArrayList<String>();

	public LineUpPagerAdapter(FragmentManager fm){
		super(fm);

		if(0 == LineUpPagerAdapter.stages.size()){
			SQLiteDatabaseHelper dbHelper = Application.getDbHelper();
			Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM lst__stages;", null);
			while(cursor.moveToNext()){
				LineUpPagerAdapter.stages.add(cursor.getString(0));
			}
			cursor.close();
			dbHelper.close();
		}
	}

	@Override
	public Fragment getItem(int position){
		Log.v(TAG, "Instantiating a new fragment for the viewPager");

		Fragment fragment = LineUpListFragment.newInstance(this.stages.get(position));

		return fragment;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position){
		Fragment fragment = (Fragment)super.instantiateItem(container, position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object){
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	public Fragment getRegisteredFragment(int position){
		return registeredFragments.get(position);
	}

	@Override
	public int getCount(){
		return LineUpPagerAdapter.stages.size();
	}

	@Override
	public CharSequence getPageTitle(int position){
		return LineUpPagerAdapter.stages.get(position);
	}
}

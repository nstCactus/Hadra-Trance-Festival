package com.zion.htf.adapter;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.ui.fragment.LineUpListFragment;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.ArrayList;

public class LineUpPagerAdapter extends FragmentPagerAdapter{
	private static final ArrayList<String>  stages              = new ArrayList<String>();
	private static final String             TAG                 = "LineUpPagerAdapter";

	public LineUpPagerAdapter(FragmentManager fm){
		super(fm);

		if(0 == LineUpPagerAdapter.stages.size()){
			SQLiteDatabaseHelper dbHelper = Application.getDbHelper();
			Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM lst__stages;", null);
			while(cursor.moveToNext()){
				LineUpPagerAdapter.stages.add(cursor.getString(0));
			}
			cursor.close();
		}
	}

	@Override
	public Fragment getItem(int position){
		if(BuildConfig.DEBUG) Log.v(LineUpPagerAdapter.TAG, "Instantiating a new fragment for the viewPager");

        return LineUpListFragment.newInstance(LineUpPagerAdapter.stages.get(position));
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

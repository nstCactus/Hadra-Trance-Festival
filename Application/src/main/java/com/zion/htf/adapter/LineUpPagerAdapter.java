package com.zion.htf.adapter;

import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.zion.adapter.CursorPagerAdapter;
import com.zion.htf.ui.fragment.LineUpListFragment;

public class LineUpPagerAdapter extends CursorPagerAdapter {
	private static final String TAG = "LineUpPagerAdapter";
	private LineUpListFragment currentFragment;

	public LineUpPagerAdapter(FragmentManager fm, Class fragmentClass, String[] projection, Cursor cursor){
        super(fm, fragmentClass, projection, cursor);
    }

	@Override
	public CharSequence getPageTitle(int position){
        super.cursor.moveToPosition(position);
        return super.cursor.getString(0);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object){
		super.setPrimaryItem(container, position, object);

		if(this.currentFragment != object) this.currentFragment = (LineUpListFragment)object;
	}

	public LineUpListFragment getCurrentFragment(){
		return this.currentFragment;
	}
}

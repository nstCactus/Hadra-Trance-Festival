package com.zion.htf.adapter;

import android.database.Cursor;
import android.support.v4.app.FragmentManager;

public class LineUpPagerAdapter extends CursorPagerAdapter {
	private static final String TAG = "LineUpPagerAdapter";

    public LineUpPagerAdapter(FragmentManager fm, Class fragmentClass, String[] projection, Cursor cursor) {
        super(fm, fragmentClass, projection, cursor);
    }

	@Override
	public CharSequence getPageTitle(int position){
        super.cursor.moveToPosition(position);
        return super.cursor.getString(0);
	}
}

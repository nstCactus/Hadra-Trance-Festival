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

package com.zion.htf.fragment;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.zion.htf.Application;
import com.zion.htf.Item;
import com.zion.htf.R;
import com.zion.htf.Set;
import com.zion.htf.activity.LineUpActivity;
import com.zion.htf.adapter.LineUpAdapter;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LineUpListFragment extends SherlockFragment{
	private static final String               TAG          = "LineUpListFragment";
	private static       SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();

	public static final String STAGE_NAME = "STAGE_NAME";

	protected Item[] list;
	protected String stage;

	/* BEGIN Columns indexes for convenience */
	protected final int COLUMN_ID         = 0;
	protected final int COLUMN_ARTIST     = 1;
	protected final int COLUMN_GENRE      = 2;
	protected final int COLUMN_BEGIN_DATE = 3;
	protected final int COLUMN_END_DATE   = 4;
	protected final int COLUMN_TYPE       = 5;
	protected final int COLUMN_PICTURE    = 6;
	protected final int COLUMN_ARTIST_ID  = 7;
	/* END Columns indexes for convenience */

	protected LineUpListFragment(Bundle args){
		super();

		this.setArguments(args);
	}

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		this.stage = this.getArguments().getString(STAGE_NAME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(this.stage == null) Log.e(TAG, "stageName is null. You must instantiate this using LineUpListFragment.newInstance(String stageName).");

		View view = inflater.inflate(R.layout.fragment_line_up_list, container, false);

		final ListView listView = (ListView)view.findViewById(R.id.line_up_list);
		listView.setAdapter(new LineUpAdapter<Item>(this.getActivity(), R.layout.item_line_up_list, R.id.label, this.getAllSets()));

		if(LineUpActivity.sectionHeaderHeight == 0){
			listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
				@Override
				public void onGlobalLayout(){
					LineUpActivity.sectionHeaderHeight = 64;//listView.getChildAt(0).getMeasuredHeight();
					if(Build.VERSION.SDK_INT >= 16) listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					else listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});
		}

		view.setTag(this.stage);
		return view;
	}

	public static final LineUpListFragment newInstance(String stageName){
		Bundle args = new Bundle(1);
		args.putString(STAGE_NAME, stageName);
		Log.v(TAG, "New instance whith stageName = " + stageName);
		return new LineUpListFragment(args);

	}

	protected List<Item> getAllSets(){
		List<Item> sets = new ArrayList<Item>();

		String query = "SELECT sets.id AS id, artists.name AS artist, artists.genre AS genre, begin_date, end_date, sets.type AS type, picture_name AS picture, artists.id AS artist_id FROM sets JOIN artists on sets.artist = artists.id WHERE stage = ? ORDER BY begin_date ASC;";
		Cursor cursor = LineUpListFragment.dbOpenHelper.getReadableDatabase().rawQuery(query, new String[]{this.stage});

		long previousDate = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy");
		while(cursor.moveToNext()){
			Date beginDate = new Date(cursor.getLong(COLUMN_BEGIN_DATE) * 1000);
			long currentDate = beginDate.getTime() / 3600 / 24 / 1000;

			if(currentDate > previousDate){
				sets.add(new Item(simpleDateFormat.format(beginDate), Item.TYPE_SECTION));
			}
			previousDate = currentDate;

			Set set = new Set(cursor.getString(COLUMN_ARTIST));
			set.setSetType(cursor.getString(COLUMN_TYPE))
			   .setGenre(cursor.getString(COLUMN_GENRE))
			   .setStage(this.stage)
			   .setBeginDate(beginDate)
			   .setEndDate(new Date(cursor.getLong(COLUMN_END_DATE) * 1000))
			   .setId(cursor.getInt(COLUMN_ID))
			   .setPicture(cursor.getString(COLUMN_PICTURE))
			   .setArtistId(cursor.getInt(COLUMN_ARTIST_ID));

			sets.add(set);
		}
		if(!cursor.isClosed()) cursor.close();
		LineUpListFragment.dbOpenHelper.close();

		return sets;
	}
}

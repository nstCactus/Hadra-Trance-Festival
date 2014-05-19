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

package com.zion.htf.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.adapter.LineUpAdapter;
import com.zion.htf.data.Item;
import com.zion.htf.data.Set;
import com.zion.htf.ui.ArtistDetailsActivity;
import com.zion.htf.ui.LineUpActivity;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LineUpListFragment extends SherlockFragment implements AdapterView.OnItemClickListener{
	private static final String               TAG          = "LineUpListFragment";
    public static  final String               STAGE_NAME   = "STAGE_NAME";
	private static       SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();

	protected   String      stage;
    private     ListView    listView;

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

	public LineUpListFragment(){
		super();
	}

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
		if(null == this.stage) Log.e(TAG, "stageName is null. You must instantiate this using LineUpListFragment.newInstance(String stageName).");

		View view = inflater.inflate(R.layout.fragment_line_up_list, container, false);

		this.listView = (ListView)view.findViewById(R.id.line_up_list);
		this.listView.setAdapter(new LineUpAdapter<Item>(this.getActivity(), R.layout.item_line_up_list, R.id.label, this.getAllSets()));

		if(LineUpActivity.sectionHeaderHeight == 0){
			final ViewTreeObserver viewTreeObserver = this.listView.getViewTreeObserver();
			if(null == viewTreeObserver){
				if(BuildConfig.DEBUG) Log.e(TAG, "Can't get a ViewTreeObserver to get the height of a ListView section header. Falling back to default value");
				LineUpActivity.sectionHeaderHeight = 64;
			}
			else{
				viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
					@SuppressWarnings("ConstantConditions")
					@Override
					public void onGlobalLayout(){
						if(1 > listView.getChildCount())	throw new RuntimeException("No children found in ListView. This is most likely due to an empty result set for this.getAllSets()");
						LineUpActivity.sectionHeaderHeight = listView.getChildAt(0).getMeasuredHeight();
						if(Build.VERSION.SDK_INT >= 16) listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						else							listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
			}
		}

		this.listView.setOnItemClickListener(this);

		view.setTag(this.stage);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outstate){
		outstate.putString(STAGE_NAME, this.stage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		Item item = (Item)this.listView.getAdapter().getItem(position);
		if(Item.TYPE_ITEM == item.getType()){
			if(item.getType() == Item.TYPE_ITEM){
				Intent intent = new Intent(this.getActivity(), ArtistDetailsActivity.class);
				intent.putExtra("artist_id", ((Set)item).getArtistId());
				this.getActivity().startActivity(intent);
                this.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		}
	}


	public static final LineUpListFragment newInstance(String stageName){
		Bundle args = new Bundle(1);
		args.putString(STAGE_NAME, stageName);
		return new LineUpListFragment(args);
	}

	protected List<Item> getAllSets(){
		List<Item> sets = new ArrayList<Item>();

		String query = "SELECT sets.id AS id, artists.name AS artist, artists.genre AS genre, begin_date, end_date, sets.type AS type, picture_name AS picture, artists.id AS artist_id FROM sets JOIN artists on sets.artist = artists.id WHERE stage = ? ORDER BY begin_date ASC;";
		Cursor cursor = LineUpListFragment.dbOpenHelper.getReadableDatabase().rawQuery(query, new String[]{this.stage});

		Date previousDate = new Date(0);
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Locale.getDefault().getLanguage().equals("fr") ? "EEEE dd MMMM YYYY" : "EEEE, MMMM DDTH, YYYY");
		DateFormat dateFormat = Locale.getDefault().getLanguage().equals("fr") ? DateFormat.getDateInstance(DateFormat.FULL, Locale.FRENCH) : DateFormat.getDateInstance(DateFormat.FULL);
		while(cursor.moveToNext()){
			Date beginDate = new Date(cursor.getLong(COLUMN_BEGIN_DATE) * 1000);

			if(!areSameDay(previousDate, beginDate)){
				sets.add(new Item(dateFormat.format(beginDate), Item.TYPE_SECTION));
			}
			previousDate = beginDate;

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

	private boolean areSameDay(Date date1, Date date2){
		boolean ret = false;
		if(null != date1 && null != date2){
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			ret = df.format(date1).equals(df.format(date2));
		}
		return ret;
	}
}

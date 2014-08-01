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
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;

import com.zion.content.SQLiteCursorLoader;
import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.adapter.LineUpListAdapter;
import com.zion.htf.data.MusicSet;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.ui.ArtistDetailsActivity;
import com.zion.htf.ui.LineUpActivity;

import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class LineUpListFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{
	private static final String TAG             = "LineUpListFragment";
    public static  final String STAGE_NAME      = "STAGE_NAME";
    private static int autoIncrement            = 0; //FIXME: Find a better way than this hack (possible solution with Fragment#getId())

    protected   String                      stage;
    private     StickyListHeadersListView   listView;
    private     LineUpListAdapter           adapter;

	@Override
	public void onCreate(Bundle savedInstance){
        try{
            if(BuildConfig.DEBUG) Log.v(LineUpListFragment.TAG, String.format(Locale.ENGLISH, "Fragment id = %d", this.getId()));

            int fragmentId = LineUpListFragment.autoIncrement++;

            super.onCreate(savedInstance);

            this.stage = this.getArguments().getString(LineUpListFragment.STAGE_NAME);
            if(null == this.stage) throw new MissingArgumentException(LineUpListFragment.STAGE_NAME, "String");

            this.adapter = new LineUpListAdapter(this.getActivity(), null, false);
            this.getActivity().getSupportLoaderManager().initLoader(fragmentId, null, this).forceLoad();
        }
        catch(MissingArgumentException e){
            throw new RuntimeException(e);
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.fragment_line_up_list, container, false);

        this.listView = (StickyListHeadersListView)view.findViewById(R.id.line_up_list);
		this.listView.setAdapter(this.adapter);

		if(0 == LineUpActivity.sectionHeaderHeight){
			final ViewTreeObserver viewTreeObserver = this.listView.getViewTreeObserver();
			if(null == viewTreeObserver){
				if(BuildConfig.DEBUG) Log.e(LineUpListFragment.TAG, "Can't get a ViewTreeObserver to get the height of a ListView section header. Falling back to default value");
				LineUpActivity.sectionHeaderHeight = 64;
			}
			else{
				viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
					@SuppressWarnings("ConstantConditions")
					@Override
					public void onGlobalLayout(){
						if(1 > LineUpListFragment.this.listView.getChildCount()) throw new RuntimeException("No children found in ListView. This is most likely due to an empty result set for this.getAllSets()");
						LineUpActivity.sectionHeaderHeight = LineUpListFragment.this.listView.getChildAt(0).getMeasuredHeight();
						if(16 <= Build.VERSION.SDK_INT) LineUpListFragment.this.listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						else                            LineUpListFragment.this.listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
			}
		}

		this.listView.setOnItemClickListener(this);

		view.setTag(this.stage);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putString(LineUpListFragment.STAGE_NAME, this.stage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		Cursor cursor = (Cursor)this.listView.getAdapter().getItem(position);
        Intent intent = new Intent(this.getActivity(), ArtistDetailsActivity.class);
        intent.putExtra("set_id", cursor.getInt(MusicSet.COLUMN_ID));
        this.getActivity().startActivity(intent);
        this.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public static LineUpListFragment newInstance(String stageName){
		Bundle args = new Bundle(1);
		args.putString(LineUpListFragment.STAGE_NAME, stageName);
        LineUpListFragment fragment = new LineUpListFragment();
        fragment.setArguments(args);
		return fragment;
	}

    /*
     * Handle CursorLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        Log.v("LineUpListFragment", String.format(Locale.ENGLISH, "Begin loading sets for stage %s", this.stage));
        return new SQLiteCursorLoader(this.getActivity(), Application.getDbHelper().getReadableDatabase(), MusicSet.QUERY + " WHERE stage = ? ORDER BY begin_date ASC", new String[]{ this.stage });
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        Log.v("LineUpListFragment", String.format(Locale.ENGLISH, "Done loading sets for stage %s", this.stage));
        this.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        this.adapter.changeCursor(null);
    }

}

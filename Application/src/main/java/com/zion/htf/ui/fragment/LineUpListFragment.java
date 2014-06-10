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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.adapter.LineUpListAdapter;
import com.zion.htf.data.Item;
import com.zion.htf.data.MusicSet;
import com.zion.htf.ui.ArtistDetailsActivity;
import com.zion.htf.ui.LineUpActivity;

public class LineUpListFragment extends Fragment implements AdapterView.OnItemClickListener{
	private static final String               TAG          = "LineUpListFragment";
    public static  final String               STAGE_NAME   = "STAGE_NAME";

	protected   String      stage;
    private     ListView    listView;

	public LineUpListFragment(){
    }

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
		this.stage = this.getArguments().getString(LineUpListFragment.STAGE_NAME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(null == this.stage) Log.e(LineUpListFragment.TAG, "stageName is null. You must instantiate this using LineUpListFragment.newInstance(String stageName).");

		View view = inflater.inflate(R.layout.fragment_line_up_list, container, false);

		this.listView = (ListView)view.findViewById(R.id.line_up_list);
		this.listView.setAdapter(new LineUpListAdapter<Item>(this.getActivity(), R.layout.item_line_up_list, R.id.label, MusicSet.getListByStage(this.stage, true)));

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
						if(1 > LineUpListFragment.this.listView.getChildCount())	throw new RuntimeException("No children found in ListView. This is most likely due to an empty result set for this.getAllSets()");
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
	public void onSaveInstanceState(Bundle outstate){
		outstate.putString(LineUpListFragment.STAGE_NAME, this.stage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		Item item = (Item)this.listView.getAdapter().getItem(position);
		if(Item.TYPE_ITEM == item.getType()){
            Intent intent = new Intent(this.getActivity(), ArtistDetailsActivity.class);
            intent.putExtra("set_id", ((MusicSet)item).getId());
            this.getActivity().startActivity(intent);
            this.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
	}

	public static LineUpListFragment newInstance(String stageName){
		Bundle args = new Bundle(1);
		args.putString(LineUpListFragment.STAGE_NAME, stageName);
        LineUpListFragment fragment = new LineUpListFragment();
        fragment.setArguments(args);
		return fragment;
	}
}

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

package com.zion.htf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;
import com.zion.htf.R;
import com.zion.htf.data.Item;
import com.zion.htf.data.MusicSet;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LineUpListAdapter<T> extends ArrayAdapter<T> implements PinnedSectionListView.PinnedSectionListAdapter{
	static class ItemViewHolder {
		TextView  artistName;
		TextView  setType;
		TextView  hour;
	}

	static class SectionViewHolder{
		TextView sectionHeader;
	}

	protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", "fr".equals(Locale.getDefault().getLanguage()) ? Locale.FRANCE : Locale.ENGLISH);

	public LineUpListAdapter(Context context, int resource, int textViewResourceId, List<T> objects){
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(Item.TYPE_SECTION == this.getItemViewType(position)){
			LineUpListAdapter.SectionViewHolder holder;

			if(null == convertView){
				// Inflate the view
                LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.section_line_up_list, parent, false);
                assert null != convertView;

				// Get reference to its field and store it in the ViewHolder
				holder = new LineUpListAdapter.SectionViewHolder();
                holder.sectionHeader = (TextView)convertView.findViewById(R.id.list_item_section_header);

				// Store the ViewHolder in the View's tag for future retrieval
				convertView.setTag(holder);
			}
			else{
				holder = (LineUpListAdapter.SectionViewHolder)convertView.getTag();
			}

			holder.sectionHeader.setText(this.getItem(position).toString());
		}
		else{
			LineUpListAdapter.ItemViewHolder holder;
			MusicSet musicSet = (MusicSet)this.getItem(position);

			if(null == convertView){
				// Inflate the view
				convertView = super.getView(position, null, parent);
                assert null != convertView;

                // Get references to its fields and store them in the ViewHolder
				holder = new LineUpListAdapter.ItemViewHolder();
				holder.artistName = (TextView)convertView.findViewById(R.id.label);
				holder.setType = (TextView)convertView.findViewById(R.id.set_type);
				holder.hour = (TextView)convertView.findViewById(R.id.hour);

				convertView.setTag(holder);
			}
			else{
				holder = (LineUpListAdapter.ItemViewHolder)convertView.getTag();
			}

			holder.artistName.setText(musicSet.toString());
			holder.hour.setText(String.format("%s-%s", this.simpleDateFormat.format(musicSet.getBeginDate()), this.simpleDateFormat.format(musicSet.getEndDate())));
			holder.setType.setText(musicSet.getSetType());
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount(){
		return 2;
	}

	@Override
	public int getItemViewType(int position){
		Item item = (Item)this.getItem(position);
		return item.getType();
	}

	@Override
	public boolean isItemViewTypePinned(int viewType){
		return Item.TYPE_SECTION == viewType;
	}
}

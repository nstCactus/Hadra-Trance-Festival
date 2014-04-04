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
import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.data.Item;
import com.zion.htf.data.Set;

import java.text.SimpleDateFormat;
import java.util.List;

public class LineUpAdapter<T> extends ArrayAdapter<T> implements PinnedSectionListView.PinnedSectionListAdapter{
	static class ItemViewHolder {
		TextView  artistName;
		TextView  setType;
		TextView  hour;
	}

	static class SectionViewHolder{
		TextView sectionHeader;
	}

	protected LayoutInflater   layoutInflater   = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

	public LineUpAdapter(Context context, int resource, int textViewResourceId, List<T> objects){
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(this.getItemViewType(position) == Item.TYPE_SECTION){
			SectionViewHolder holder;

			if(convertView == null){
				// Inflate the view
				convertView = this.layoutInflater.inflate(R.layout.section_line_up_list, parent, false);

				// Get reference to its field and store it in the ViewHolder
				holder = new SectionViewHolder();
				holder.sectionHeader = (TextView)convertView.findViewById(R.id.list_item_section_header);

				// Store the ViewHolder in the View's tag for future retrieval
				convertView.setTag(holder);
			}
			else{
				holder = (SectionViewHolder)convertView.getTag();
			}

			holder.sectionHeader.setText(this.getItem(position).toString());
		}
		else{
			ItemViewHolder holder;
			Set set = (Set)this.getItem(position);

			if(null == convertView){
				// Inflate the view
				convertView = super.getView(position, convertView, parent);

				// Get references to its fields and store them in the ViewHolder
				holder = new ItemViewHolder();
				holder.artistName = (TextView)convertView.findViewById(R.id.label);
				holder.setType = (TextView)convertView.findViewById(R.id.set_type);
				holder.hour = (TextView)convertView.findViewById(R.id.hour);

				convertView.setTag(holder);
			}
			else{
				holder = (ItemViewHolder)convertView.getTag();
			}

			holder.artistName.setText(set.toString());
			holder.hour.setText(String.format("%s-%s", this.simpleDateFormat.format(set.getBeginDate()), this.simpleDateFormat.format(set.getEndDate())));
			holder.setType.setText(set.getSetType());
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
		return viewType == Item.TYPE_SECTION;
	}

	public int getPhotoResourceId(String resourceName){
		int ret = 0;
		if(null != resourceName)
			ret = Application.getContext().getResources().getIdentifier(resourceName, "drawable", this.getContext().getPackageName());
		if(ret == 0) ret = R.drawable.no_image;
		return ret;
	}
}

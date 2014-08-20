/*
 *
 *     Copyright 2013-2014 Yohann Bianchi
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 *     or see <http://www.gnu.org/licenses/>.
 *
 */

package com.zion.htf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zion.htf.data.SoundcloudTrack;
import com.zion.util.StringUtils;

import java.util.ArrayList;

public class SoundcloudTracksAdapter extends BaseAdapter{
	private ArrayList<SoundcloudTrack> tracks;
	private final LayoutInflater inflater;

	public ArrayList<SoundcloudTrack> getTracks(){
		return this.tracks;
	}

	public SoundcloudTracksAdapter(Context context, ArrayList<SoundcloudTrack> tracks){
		this.inflater = LayoutInflater.from(context);
		this.tracks = tracks;
	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getCount(){
		return this.tracks.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 *
	 * @param position Position of the item whose data we want within the adapter's
	 *                 data set.
	 *
	 * @return The data at the specified position.
	 */
	@Override
	public Object getItem(int position){
		return this.tracks.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 *
	 * @param position The position of the item within the adapter's data set whose row id we want.
	 *
	 * @return The id of the item at the specified position.
	 */
	@Override
	public long getItemId(int position){
		return this.tracks.get(position).getId();
	}

	/**
	 * Get a View that displays the data at the specified position in the data set. You can either
	 * create a View manually or inflate it from an XML layout file. When the View is inflated, the
	 * parent View (GridView, ListView...) will apply default layout parameters unless you use
	 * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
	 * to specify a root view and to prevent attachment to the root.
	 *
	 * @param position    The position of the item within the adapter's data set of the item whose view
	 *                    we want.
	 * @param convertView The old view to reuse, if possible. Note: You should check that this view
	 *                    is non-null and of an appropriate type before using. If it is not possible to convert
	 *                    this view to display the correct data, this method can create a new view.
	 *                    Heterogeneous lists can specify their number of view types, so that this View is
	 *                    always of the right type (see {@link #getViewTypeCount()} and
	 *                    {@link #getItemViewType(int)}).
	 * @param parent      The parent that this view will eventually be attached to
	 *
	 * @return A View corresponding to the data at the specified position.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		SoundcloudTracksAdapter.ViewHolder holder;
		if(null == convertView){
			convertView = this.inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
			holder = new SoundcloudTracksAdapter.ViewHolder();
			holder.titleTV = (TextView)convertView.findViewById(android.R.id.text1);
			holder.durationTV = (TextView)convertView.findViewById(android.R.id.text2);

			convertView.setTag(holder);
		}
		else{
			holder = (SoundcloudTracksAdapter.ViewHolder)convertView.getTag();
		}

		SoundcloudTrack track = this.tracks.get(position);
		holder.titleTV.setText(track.getTitle());
		holder.durationTV.setText(StringUtils.formatDuration(track.getDuration() /1000));

		return convertView;
	}

	private class ViewHolder{
		TextView titleTV;
		TextView durationTV;
	}
}

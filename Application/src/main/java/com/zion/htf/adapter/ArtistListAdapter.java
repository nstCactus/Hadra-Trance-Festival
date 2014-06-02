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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zion.htf.R;
import com.zion.htf.data.Artist;

import java.util.List;

public class ArtistListAdapter<T> extends ArrayAdapter<T>{
	static class ItemViewHolder {
		TextView  artistName;
		TextView  setType;
	}

	public ArtistListAdapter(Context context, int resource, int textViewResourceId, List<T> objects){
		super(context, resource, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
        ItemViewHolder holder;
        Artist artist = (Artist)this.getItem(position);

        if(null == convertView){
            // Inflate the view
            convertView = super.getView(position, convertView, parent);

            // Get references to its fields and store them in the ViewHolder
            holder = new ItemViewHolder();
            holder.artistName = (TextView)convertView.findViewById(R.id.label);
            holder.setType = (TextView)convertView.findViewById(R.id.set_type);

            convertView.setTag(holder);
        }
        else{
            holder = (ItemViewHolder)convertView.getTag();
        }

        holder.artistName.setText(artist.getArtistName());
        holder.setType.setText(artist.getSetType());
		return convertView;
	}
}

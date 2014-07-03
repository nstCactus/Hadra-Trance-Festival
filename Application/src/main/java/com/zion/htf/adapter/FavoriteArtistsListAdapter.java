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
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zion.htf.R;
import com.zion.htf.data.Artist;

import java.util.List;
import java.util.Locale;

public class FavoriteArtistsListAdapter<T> extends AbstractActionModeListAdapter{
    static class ItemViewHolder{
        TextView artistName;
        ImageView artistPhoto;
    }

    public FavoriteArtistsListAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.v(this.getClass().getName(), String.format(Locale.ENGLISH, "getView(%d, %s, %s)", position, convertView, parent));
        
        FavoriteArtistsListAdapter.ItemViewHolder holder;
        Artist artist = (Artist)this.getItem(position);
        boolean inflateView = null == convertView;

        // Always call the super method as it sets selected items background color
        convertView = super.getView(position, convertView, parent);
        Log.v("FavoriteArtistListAdapter", String.format(Locale.ENGLISH, "The item at position %d is supposed to be %s.", position, this.isSelected(position) ? "selected" : "unselected"));
        if(inflateView){
            // Get references to its fields and store them in the ViewHolder
            holder = new FavoriteArtistsListAdapter.ItemViewHolder();
            holder.artistName = (TextView)convertView.findViewById(R.id.label);
            holder.artistPhoto = (ImageView)convertView.findViewById(R.id.artist_photo);

            convertView.setTag(holder);
        }
        else{
            holder = (FavoriteArtistsListAdapter.ItemViewHolder)convertView.getTag();
        }

        holder.artistName.setText(artist.getName());
        holder.artistPhoto.setImageResource(R.drawable.no_image);
        holder.artistPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        return convertView;
    }

    @Override
    public void removeSelected(){
        SparseBooleanArray selectedIds = this.getSelectedPositions();
        String inClause = null;
        Artist item;
        int id;

        for(int i = selectedIds.size() - 1; 0 <= i; i--){
            item = (Artist)this.getItem(selectedIds.keyAt(i));
            id = item.getId();
            if(null == inClause)    inClause = String.valueOf(id);
            else                    inClause += String.format(",%d", id);

            Log.v("AlarmsListAdapter", String.format("Trying to unfavorite item at position %d.Adapter size = %d", selectedIds.keyAt(i), this.getCount()));
            this.remove(item);
        }

        Artist.unsetFavorite(inClause);
        this.notifyDataSetChanged();
    }
}

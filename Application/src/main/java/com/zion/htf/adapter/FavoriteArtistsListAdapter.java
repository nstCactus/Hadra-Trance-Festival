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
import android.database.Cursor;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zion.htf.R;
import com.zion.htf.data.Artist;
import com.zion.htf.ui.FavoriteArtistsManagerActivity;

public class FavoriteArtistsListAdapter extends AbstractActionModeListAdapter{
    // ViewHolder pattern
    static class ViewHolder {
        TextView artistName;
        ImageView artistPhoto;
    }

    public FavoriteArtistsListAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rowView = super.layoutInflater.inflate(R.layout.item_favorite_artists_list, parent, false);
        FavoriteArtistsListAdapter.ViewHolder holder = new FavoriteArtistsListAdapter.ViewHolder();
        holder.artistName = (TextView)rowView.findViewById(R.id.artist_name);
        holder.artistPhoto = (ImageView)rowView.findViewById(R.id.artist_photo);

        rowView.setTag(holder);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        FavoriteArtistsListAdapter.ViewHolder holder = (FavoriteArtistsListAdapter.ViewHolder)view.getTag();
        holder.artistName.setText(cursor.getString(FavoriteArtistsManagerActivity.COLUMN_NAME));

        holder.artistPhoto.setImageResource(R.drawable.no_image);
        this.loadBitmap(Artist.getPictureResourceId(cursor.getString(FavoriteArtistsManagerActivity.COLUMN_PICTURE)), holder.artistPhoto);
    }

    @Override
    public void removeSelected(){
        SparseBooleanArray selectedIds = this.getSelectedPositions();
        String inClause = null;
        int id;

        for(int i = selectedIds.size() - 1; 0 <= i; i--){
            id = ((Cursor)this.getItem(selectedIds.keyAt(i))).getInt(FavoriteArtistsManagerActivity.COLUMN_ID);
            if(null == inClause)    inClause = String.valueOf(id);
            else                    inClause += String.format(",%d", id);

            Log.v("AlarmsListAdapter", String.format("Trying to unfavorite item at position %d.Adapter size = %d", selectedIds.keyAt(i), this.getCount()));
        }

        Artist.unsetFavorite(inClause);
        this.notifyDataSetChanged();
    }
}

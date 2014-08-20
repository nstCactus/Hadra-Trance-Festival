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
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zion.adapter.CachedImageCursorAdapter;
import com.zion.htf.R;
import com.zion.htf.data.Artist;
import com.zion.htf.data.MusicSet;

public class ArtistListAdapter extends CachedImageCursorAdapter {
    private final LayoutInflater layoutInflater;

	static class ViewHolder {
		TextView  artistName;
		TextView  setType;
        ImageView artistPhoto;
	}

    public ArtistListAdapter(Context context, Cursor cursor, boolean autoRequery){
        super(context, cursor, autoRequery);

        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        // Inflate the view
        View rowView = this.layoutInflater.inflate(R.layout.item_artists_list, parent, false);
        ArtistListAdapter.ViewHolder holder = new ArtistListAdapter.ViewHolder();
        
        // Get references to its fields and store them in the ViewHolder
        holder.artistName = (TextView)rowView.findViewById(R.id.artist_name);
        holder.setType = (TextView)rowView.findViewById(R.id.set_type);
        holder.artistPhoto = (ImageView)rowView.findViewById(R.id.artist_photo);

        rowView.setTag(holder);

        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        ArtistListAdapter.ViewHolder holder = (ArtistListAdapter.ViewHolder)view.getTag();

        holder.artistName.setText(cursor.getString(MusicSet.COLUMN_ARTIST_NAME));
        holder.setType.setText(cursor.getString(MusicSet.COLUMN_TYPE));

        holder.artistPhoto.setImageResource(R.drawable.no_image);
        this.loadBitmap(Artist.getPictureResourceId(cursor.getString(MusicSet.COLUMN_ARTIST_PICTURE_NAME)), holder.artistPhoto);
    }
}

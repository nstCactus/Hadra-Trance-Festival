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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class LineUpListAdapter extends CachedImageCursorAdapter implements StickyListHeadersAdapter{
    private final LayoutInflater layoutInflater;
	private static final SimpleDateFormat   DATE_FORMAT_SET_BOUNDS  = new SimpleDateFormat("HH:mm", "fr".equals(Locale.getDefault().getLanguage()) ? Locale.FRANCE : Locale.ENGLISH);
    private static final SimpleDateFormat   DATE_FORMAT_DAY_IN_YEAR = new SimpleDateFormat("D", Locale.ENGLISH);
    private static final DateFormat         DATE_FORMAT_HEADER_DATE = "fr".equals(Locale.getDefault().getLanguage()) ? DateFormat.getDateInstance(DateFormat.FULL, Locale.FRENCH) : DateFormat.getDateInstance(DateFormat.FULL);


    static class ItemViewHolder{
		TextView  artistName;
		TextView  setType;
		TextView  hour;
        ImageView artistPhoto;
	}

	static class SectionViewHolder{
		TextView sectionHeader;
	}

    public LineUpListAdapter(Context context, Cursor cursor, boolean autoRequery){
        super(context, cursor, autoRequery);

        this.layoutInflater = LayoutInflater.from(context);

	    //TODO: Honor timezone setting
	    //DATE_FORMAT_SET_BOUNDS.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
	    //DATE_FORMAT_DAY_IN_YEAR.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
	    //DATE_FORMAT_HEADER_DATE.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        View rowView = this.layoutInflater.inflate(R.layout.item_line_up_list, parent, false);

        LineUpListAdapter.ItemViewHolder holder = new LineUpListAdapter.ItemViewHolder();

        holder.artistName = (TextView)rowView.findViewById(R.id.artist_name);
        holder.setType = (TextView)rowView.findViewById(R.id.set_type);
        holder.hour = (TextView)rowView.findViewById(R.id.hour);
        holder.artistPhoto = (ImageView) rowView.findViewById(R.id.artist_photo);
        rowView.setTag(holder);

        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        LineUpListAdapter.ItemViewHolder holder = (LineUpListAdapter.ItemViewHolder) view.getTag();

        holder.artistName.setText(cursor.getString(MusicSet.COLUMN_ARTIST_NAME));
        holder.setType.setText(cursor.getString(MusicSet.COLUMN_TYPE));
        holder.hour.setText(String.format(Locale.ENGLISH, "%s - %s", LineUpListAdapter.DATE_FORMAT_SET_BOUNDS.format(cursor.getLong(MusicSet.COLUMN_BEGIN_DATE) * 1000), LineUpListAdapter.DATE_FORMAT_SET_BOUNDS.format(cursor.getLong(MusicSet.COLUMN_END_DATE) * 1000)));

        holder.artistPhoto.setImageResource(R.drawable.no_image);
        this.loadBitmap(Artist.getPictureResourceId(cursor.getString(MusicSet.COLUMN_ARTIST_PICTURE_NAME)), holder.artistPhoto);
    }

    public View newHeaderView(Context context, Cursor cursor, ViewGroup parent){
        View headerView = this.layoutInflater.inflate(R.layout.section_line_up_list, parent, false);

        LineUpListAdapter.SectionViewHolder holder = new LineUpListAdapter.SectionViewHolder();
        holder.sectionHeader = (TextView) headerView.findViewById(R.id.list_item_section_header);

        headerView.setTag(holder);
        return headerView;
    }

    public void bindHeaderView(View view, Context context, Cursor cursor){
        LineUpListAdapter.SectionViewHolder holder = (LineUpListAdapter.SectionViewHolder) view.getTag();

        holder.sectionHeader.setText(LineUpListAdapter.DATE_FORMAT_HEADER_DATE.format(new Date(cursor.getLong(MusicSet.COLUMN_BEGIN_DATE) * 1000)));
    }

    @Override
    public View getHeaderView(int position, View view, ViewGroup parent){
        Cursor cursor = this.getCursor();
        if(!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        if(null == view){
            view = this.newHeaderView(this.context, cursor, parent);
        }
        this.bindHeaderView(view, this.context, cursor);
        return view;

    }

    @Override
    public long getHeaderId(int position){
        Cursor cursor = (Cursor) this.getItem(position);
        return Integer.valueOf(LineUpListAdapter.DATE_FORMAT_DAY_IN_YEAR.format(new Date(cursor.getLong(MusicSet.COLUMN_BEGIN_DATE) * 1000)));
    }
}

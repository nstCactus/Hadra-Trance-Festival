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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.data.Artist;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.exception.AlarmNotFoundException;
import com.zion.htf.ui.AlarmManagerActivity;

public class AlarmListAdapter extends AbstractActionModeListAdapter{
    static class ViewHolder {
        TextView    artistName;
        TextView    whereAndWhen;
        TextView    alarmTime;
        TextView    setType;
        ImageView   artistPhoto;
    }

    public AlarmListAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rowView = super.layoutInflater.inflate(R.layout.item_alarms_list, parent, false);
        AlarmListAdapter.ViewHolder holder = new AlarmListAdapter.ViewHolder();

        holder.artistName   = (TextView)    rowView.findViewById(R.id.artist_name);
        holder.whereAndWhen = (TextView)    rowView.findViewById(R.id.where_and_when);
        holder.alarmTime    = (TextView)    rowView.findViewById(R.id.alarm_time);
        holder.setType      = (TextView)    rowView.findViewById(R.id.set_type);
        holder.artistPhoto  = (ImageView)   rowView.findViewById(R.id.artist_photo);

        rowView.setTag(holder);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AlarmListAdapter.ViewHolder holder = (AlarmListAdapter.ViewHolder) view.getTag();

        holder.artistName.setText(cursor.getString(AlarmManagerActivity.COLUMN_ARTIST_NAME));
        holder.alarmTime.setText(this.simpleDateFormat.format(cursor.getLong(AlarmManagerActivity.COLUMN_ALARM_TIMESTAMP)));
        holder.whereAndWhen.setText(cursor.getString(AlarmManagerActivity.COLUMN_SET_STAGE));
        holder.setType.setText(cursor.getString(AlarmManagerActivity.COLUMN_SET_TYPE));

        holder.artistPhoto.setImageResource(R.drawable.no_image);
        this.loadBitmap(Artist.getPictureResourceId(cursor.getString(AlarmManagerActivity.COLUMN_ARTIST_PICTURE_NAME)), holder.artistPhoto);
    }

    @Override
    public void removeSelected(){
        SparseBooleanArray selectedIds = this.getSelectedPositions();
        String inClause = null;
        int id;

        for(int i = selectedIds.size() - 1; 0 <= i; i--){
            id = ((Cursor)this.getItem(selectedIds.keyAt(i))).getInt(AlarmManagerActivity.COLUMN_ALARM_ID);
            try{
                SavedAlarm.getById(id).unregisterAlarm(this.context);
            }
            catch(AlarmNotFoundException e){
                if(BuildConfig.DEBUG) e.printStackTrace();
                //TODO: Handle this properly
            }
            if(null == inClause)    inClause = String.valueOf(id);
            else                    inClause += String.format(",%d", id);
        }

        SavedAlarm.delete(inClause);
        this.notifyDataSetChanged();
    }
}

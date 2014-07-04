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
import android.widget.TextView;

import com.zion.htf.R;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.ui.AlarmManagerActivity;

public class AlarmListAdapter extends AbstractActionModeListAdapter {
    static class ViewHolder {
        TextView artistName;
        TextView stage;
        TextView time;
    }

    public AlarmListAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    public AlarmListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rowView = super.layoutInflater.inflate(R.layout.item_alarms_list, parent, false);
        AlarmListAdapter.ViewHolder holder = new AlarmListAdapter.ViewHolder();

        holder.artistName = (TextView)rowView.findViewById(R.id.label);
        holder.stage = (TextView)rowView.findViewById(R.id.stage);
        holder.time = (TextView)rowView.findViewById(R.id.time);

        rowView.setTag(holder);
        return rowView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AlarmListAdapter.ViewHolder holder = (AlarmListAdapter.ViewHolder) view.getTag();

        holder.artistName.setText(cursor.getString(AlarmManagerActivity.COLUMN_ARTIST_NAME));
        holder.time.setText(this.simpleDateFormat.format(cursor.getLong(AlarmManagerActivity.COLUMN_ALARM_TIMESTAMP)));
        holder.stage.setText(cursor.getString(AlarmManagerActivity.COLUMN_SET_STAGE));
    }

    @Override
    public void removeSelected(){
        SparseBooleanArray selectedIds = this.getSelectedPositions();
        String inClause = null;
        int id;

        for(int i = selectedIds.size() - 1; 0 <= i; i--){
            id = ((Cursor)this.getItem(selectedIds.keyAt(i))).getInt(AlarmManagerActivity.COLUMN_ALARM_ID);
            if(null == inClause)    inClause = String.valueOf(id);
            else                    inClause += String.format(",%d", id);
        }

        SavedAlarm.delete(inClause);
        this.notifyDataSetChanged();
    }
}

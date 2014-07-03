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
import android.widget.TextView;

import com.zion.htf.R;
import com.zion.htf.data.MusicSet;
import com.zion.htf.data.SavedAlarm;

import java.util.List;
import java.util.Locale;

public class AlarmListAdapter<T> extends AbstractActionModeListAdapter {
    static class ItemViewHolder{
        TextView artistName;
        TextView stage;
        TextView time;
    }

    public AlarmListAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.v(this.getClass().getName(), String.format(Locale.ENGLISH, "getView(%d, %s, %s)", position, convertView, parent));

        AlarmListAdapter.ItemViewHolder holder;
        SavedAlarm alarm = (SavedAlarm)this.getItem(position);
        MusicSet set = alarm.getSet();
        boolean inflateView = null == convertView;

        // Always call the super method as it sets selected items background color
        convertView = super.getView(position, convertView, parent);

        if(inflateView){
            // Get references to the fields and store them in the ViewHolder
            holder = new AlarmListAdapter.ItemViewHolder();
            holder.artistName = (TextView)convertView.findViewById(R.id.label);
            holder.stage = (TextView)convertView.findViewById(R.id.stage);
            holder.time = (TextView)convertView.findViewById(R.id.time);

            convertView.setTag(holder);
        }
        else{
            holder = (AlarmListAdapter.ItemViewHolder)convertView.getTag();
        }

        String stage = set.getStage();
        stage = stage.replace("The ", "");
        stage = stage.substring(0, 1).toUpperCase(Locale.ENGLISH) + stage.substring(1);
        holder.artistName.setText(alarm.getSet().getArtist().getName());
        holder.time.setText(this.simpleDateFormat.format(alarm.getTimestamp()));
        holder.stage.setText(stage);

        return convertView;
    }

    @Override
    public void removeSelected(){
        SparseBooleanArray selectedIds = this.getSelectedPositions();
        String inClause = null;
        SavedAlarm item;
        int id;

        for(int i = selectedIds.size() - 1; 0 <= i; i--){
            item = (SavedAlarm)this.getItem(selectedIds.keyAt(i));
            id = item.getId();
            if(null == inClause)    inClause = String.valueOf(id);
            else                    inClause += String.format(",%d", id);

            Log.v("AlarmsListAdapter", String.format("Trying to delete item at position %d.Adapter size = %d", selectedIds.keyAt(i), this.getCount()));
            this.remove(item);
        }

        SavedAlarm.delete(inClause);
        this.notifyDataSetChanged();
    }

    /**
     * Find an alarm using its database identifier
     * @param id the database identifier of the alarm
     * @return the position of the `SavedAlarm` object matching the given {@code id} or null if none found
     */
    public int findAlarmPositionById(int id){
        int position = -1;
        SavedAlarm current;

        int currentPosition = 0;
        int count = this.getCount();
        while(-1 == position && currentPosition < count){
            current = (SavedAlarm)this.getItem(currentPosition);
            if(null != current && id == current.getId()) position = currentPosition;
            currentPosition++;
        }

        return position;
    }
}

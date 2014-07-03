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

package com.zion.htf.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;

import com.zion.htf.R;
import com.zion.htf.adapter.AlarmListAdapter;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.ui.fragment.TimeToPickerFragment;

public class AlarmManagerActivity extends AbstractActionModeCompatListActivity implements TimeToPickerFragment.TimeToPickerInterface{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.adapter = new AlarmListAdapter<SavedAlarm>(this, R.layout.item_alarms_list, R.id.label, SavedAlarm.getList(false));

        super.setLayoutId(R.layout.activity_alarm_manager);
        super.setListViewId(R.id.alarm_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SavedAlarm alarm = (SavedAlarm)super.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putInt("set_id", alarm.getSet().getId());
        args.putInt("alarm_id", alarm.getId());
        args.putBoolean("edit_mode", true);
        DialogFragment newFragment = TimeToPickerFragment.newInstance(args);
        newFragment.show(this.getSupportFragmentManager(), "timeToPicker");
    }

    @Override
    public void doPositiveClick(int alarmId){
        // Overkill but should be fail-proof
        super.adapter = new AlarmListAdapter<SavedAlarm>(this, R.layout.item_alarms_list, R.id.label, SavedAlarm.getList(false));
        super.listView.setAdapter(this.adapter);
        super.adapter.notifyDataSetChanged();
    }

    @Override
    public void doNegativeClick(){

    }

    @Override
    public void doNeutralClick(int setId, int alarmId){
        super.adapter.delete(((AlarmListAdapter)super.adapter).findAlarmPositionById(alarmId));
    }
}

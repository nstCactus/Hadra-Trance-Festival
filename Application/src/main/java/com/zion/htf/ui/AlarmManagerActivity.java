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
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zion.htf.R;
import com.zion.htf.adapter.AlarmsListAdapter;
import com.zion.htf.data.Item;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.ui.fragment.TimeToPickerFragment;

public class AlarmManagerActivity extends ActionBarActivity implements TimeToPickerFragment.TimeToPickerInterface{
    private ListView listView;
    private AlarmsListAdapter adapter;
    private ActionMode actionMode = null;

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_mode_alarm_manager, menu);

            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_remove_alarms:
                    Log.v("AlarmManagerActivity", "User wants to delete all selected items.");
                    AlarmManagerActivity.this.adapter.removeSelected();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_select_all_alarms:
                    Log.v("AlarmManagerActivity", "User selected all items");
                    AlarmManagerActivity.this.adapter.selectAll();
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode){
            AlarmManagerActivity.this.adapter.clearSelection();
            AlarmManagerActivity.this.actionMode = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_alarm_manager);

        this.adapter = new AlarmsListAdapter<Item>(this, R.layout.item_alarm_list, R.id.label, SavedAlarm.getList(false));

        this.listView = (ListView)this.findViewById(R.id.alarm_list);
        this.listView.setAdapter(this.adapter);

        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                if(null == AlarmManagerActivity.this.actionMode){
                    Log.v("AlarmManagerActivity", "Item LONG_CLICK while in NORMAL mode.");
                    AlarmManagerActivity.this.actionMode = AlarmManagerActivity.this.startSupportActionMode(AlarmManagerActivity.this.actionModeCallback);
                    AlarmManagerActivity.this.adapter.selectItem(position);
                    //FIXME: When entering actionMode, the user-clicked item often gets unselected as the OnItemClickListener is fired
                }
                else{
                    //TODO: Do something
                    Log.v("AlarmManagerActivity", "Item LONG CLICK while in ACTION mode.");

                }
                return false;
            }
        });

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null != AlarmManagerActivity.this.actionMode){
                    AlarmManagerActivity.this.adapter.toggleSelection(position);
                    this.updateTitle();
                    Log.v("AlarmManagerActivity", "Item CLICK while in ACTION mode.");
                    //FIXME: Quit actionMode if no selected item left (when above FIXME is fixed)
                }
                else{
                    Log.v("AlarmManagerActivity", "Item CLICK while in NORMAL mode.");

                    SavedAlarm alarm = (SavedAlarm)AlarmManagerActivity.this.adapter.getItem(position);
                    Bundle args = new Bundle();
                    args.putInt("set_id", alarm.getSet().getId());
                    args.putInt("alarm_id", alarm.getId());
                    args.putBoolean("edit_mode", true);
                    DialogFragment newFragment = TimeToPickerFragment.newInstance(args);
                    newFragment.show(AlarmManagerActivity.this.getSupportFragmentManager(), "timeToPicker");
                }
            }

            private void updateTitle(){
                if(null != AlarmManagerActivity.this.actionMode) {
                    int count = AlarmManagerActivity.this.adapter.getSelectedCount();
                    String title;
                    switch (count) {
                        case 0:
                            title = "Aucun élément sélectionné";
                            break;
                        case 1:
                            title = "1 élément sélectionné";
                            break;
                        default:
                            title = count + " éléments sélectionnés";
                    }
                    AlarmManagerActivity.this.actionMode.setTitle(title);
                }
                else{
                    Log.e("AlarmManagerActivity", "Trying to update actionMode title while not in actionMode.");
                }
            }
        });
    }

    @Override
    public void doPositiveClick(int alarmId){
        // Overkill but should be fail-proof
        this.adapter = new AlarmsListAdapter<Item>(this, R.layout.item_alarm_list, R.id.label, SavedAlarm.getList(false));
        this.listView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void doNegativeClick(){

    }

    @Override
    public void doNeutralClick(int setId, int alarmId){
        this.adapter.delete(this.adapter.findAlarmPositionById(alarmId));
    }
}

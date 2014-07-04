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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;

import com.zion.content.SQLiteCursorLoader;
import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.adapter.AlarmListAdapter;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.ui.fragment.TimeToPickerFragment;

public class AlarmManagerActivity extends AbstractActionModeCompatListActivity implements TimeToPickerFragment.TimeToPickerInterface, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String QUERY = "SELECT alarms.id AS _id, name, timestamp * 1000, (UPPER(SUBSTR(stage, 5, 1)) || SUBSTR(stage, 6)) AS STAGE, sets.id FROM alarms JOIN sets ON set_id = sets.id JOIN artists ON artist = artists.id";
    public static final int COLUMN_ALARM_ID        = 0;
    public static final int COLUMN_ARTIST_NAME     = 1;
    public static final int COLUMN_ALARM_TIMESTAMP = 2;
    public static final int COLUMN_SET_STAGE       = 3;
    public static final int COLUMN_SET_ID          = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.getSupportLoaderManager().initLoader(AbstractActionModeCompatListActivity.LISTVIEW_LOADER_ID, null, this).forceLoad();
        super.adapter = new AlarmListAdapter(this, null, false);

        super.setLayoutId(R.layout.activity_alarm_manager);
        super.setListViewId(R.id.alarm_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor)super.adapter.getItem(position);
        Bundle args = new Bundle();
        args.putInt("set_id", cursor.getInt(AlarmManagerActivity.COLUMN_SET_ID));
        args.putInt("alarm_id", cursor.getInt(AlarmManagerActivity.COLUMN_ALARM_ID));
        args.putBoolean("edit_mode", true);
        DialogFragment newFragment = TimeToPickerFragment.newInstance(args);
        newFragment.show(this.getSupportFragmentManager(), "timeToPicker");
    }

    @Override
    public void doPositiveClick(int alarmId){
        this.getSupportLoaderManager().restartLoader(AbstractActionModeCompatListActivity.LISTVIEW_LOADER_ID, null, this).forceLoad();
    }

    @Override
    public void doNegativeClick(){
        // Nothing to do, user clicked cancel
    }

    @Override
    public void doNeutralClick(int setId, int alarmId){
        SavedAlarm.delete(alarmId);
        this.getSupportLoaderManager().restartLoader(AbstractActionModeCompatListActivity.LISTVIEW_LOADER_ID, null, this).forceLoad();
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        Loader<Cursor> loader = new SQLiteCursorLoader(this, Application.getDbHelper().getReadableDatabase(), AlarmManagerActivity.QUERY, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        super.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        super.adapter.changeCursor(null);
    }
}

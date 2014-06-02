/*
    Copyright 2014 Yohann Bianchi

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
package com.zion.htf.ui.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.receiver.AlarmReceiver;

import java.util.Date;

public class TimeToPickerFragment extends DialogFragment{
    protected static final String TAG = "TimeToPickerFragment";
    private long timestamp;

    private Spinner unitSpinner;
    private EditText numberBox;
    private int setId;
    private String stage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try{
            Bundle args = this.getArguments();
            if(null == args) throw new MissingArgumentException("You must provide the following arguments for this fragment to work properly: timestamp (long), set_id (int)");
            if(0l == (this.timestamp = args.getLong("timestamp", 0l) * 1000)) throw new MissingArgumentException("timestamp", "long");
            if(0 == (this.setId = args.getInt("set_id", 0))) throw new MissingArgumentException("set_id", "int");
        }
        catch(MissingArgumentException e){
            throw new RuntimeException(e.getMessage());
        }

        final AlertDialog dialog =  new AlertDialog.Builder(this.getActivity())
                .setTitle(R.string.title_new_alarm_dialog)
                .setPositiveButton(R.string.alarm_set,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int amount;
                                try {
                                    amount = Integer.parseInt(TimeToPickerFragment.this.numberBox.getText().toString());
                                }
                                catch(NumberFormatException e){
                                    amount = 0;
                                    // TODO: Validate user input
                                }

                                int minutes;
                                Log.v(TimeToPickerFragment.TAG, String.format("User pressed OK, number = %s, unit = %s", amount, TimeToPickerFragment.this.unitSpinner.getSelectedItem()));

                                // Calculate alarm timestamp
                                long alarmTimestamp = TimeToPickerFragment.this.timestamp;

                                switch(TimeToPickerFragment.this.unitSpinner.getSelectedItemPosition()){
                                    case 0:
                                        minutes = amount * 60;
                                        break;

                                    case 1:
                                        minutes = amount;
                                        break;

                                    default:
                                        throw new RuntimeException("Inconsistent item position for time_units array");
                                }
                                alarmTimestamp -= minutes * 60000;// * 60000 to convert to ms

                                Log.v(TimeToPickerFragment.TAG, String.format("Alarm timestamp is %s, or %tc in a human-readable fashion", alarmTimestamp, alarmTimestamp));

                                // Save it in the database so it can be restored after reboots
                                ContentValues values = new ContentValues(2);
                                values.put("timestamp", alarmTimestamp / 1000);
                                values.put("set_id", TimeToPickerFragment.this.setId);
                                try{
                                    Application.getDbHelper().getWritableDatabase().insertOrThrow("alarms", null, values);
                                }
                                catch(SQLException e){
                                    if(BuildConfig.DEBUG) e.printStackTrace();
                                    //TODO: Notify the user that something went wrong
                                }

                                // Set alarm
                                AlarmManager am = (AlarmManager) TimeToPickerFragment.this.getActivity().getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(TimeToPickerFragment.this.getActivity(), AlarmReceiver.class);
                                intent.putExtra("set_id", TimeToPickerFragment.this.setId);
                                intent.setAction("com.zion.htf_" + (new Date().getTime()));// Do not reuse previous PendingIntent.
                                PendingIntent alarmIntent = PendingIntent.getBroadcast(TimeToPickerFragment.this.getActivity(), 0, intent, 0);

                                if(19 <= Build.VERSION.SDK_INT) am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 1000, alarmIntent);// FIXME: use alarmTimestamp once basic testing has been done
                                else                            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60 * 1000, alarmIntent);
                            }
                        }
                )
                .setNegativeButton(R.string.alarm_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Log.v(TimeToPickerFragment.TAG, "User pressed Cancel");
                                //((ArtistDetailsActivity) getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();

        View view = dialog.getLayoutInflater().inflate(R.layout.fragment_time_to_picker, null);
        if(null == view) throw new NullPointerException("Can't inflate the fragment_time_to_picker layout.");
        dialog.setView(view);

        this.unitSpinner = (Spinner)view.findViewById(R.id.unitSpinner);
        if(null == this.unitSpinner) throw new NullPointerException("Couldn't find unitSpinner");
        this.unitSpinner.setSelection(1);

        this.numberBox = (EditText)view.findViewById(R.id.numberBox);
        if(null == this.numberBox) throw new NullPointerException("Couldn't find numberBox");

        return dialog;
    }

    /**
     * Returns a new instance of TimeToPickerFragment
     * @param args A {@link Bundle} that contains at least the following keys: timestamp (long) and set_id (int)
     * @return An instance of TimeToPicker
     */
    public static TimeToPickerFragment newInstance(Bundle args){
        TimeToPickerFragment instance = new TimeToPickerFragment();
        instance.setArguments(args);

        return instance;
    }
}

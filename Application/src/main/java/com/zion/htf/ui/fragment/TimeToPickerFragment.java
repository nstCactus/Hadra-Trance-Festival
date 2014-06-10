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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.data.MusicSet;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.exception.SetNotFoundException;
import com.zion.htf.ui.AlarmManagerActivity;

/**
 * An {@link android.app.AlertDialog} used to set or edit an alarm.
 */
// FIXME: Edit mode title should emphasize the fact it editing rather than creating an alarm
// FIXME: Dialog must state clearly that the input must be in hours/minutes to the begining of the set
// TODO: Consider using a spinner as in Calendar app
public class TimeToPickerFragment extends DialogFragment{
    protected static final String TAG = "TimeToPickerFragment";

    private Spinner unitSpinner;
    private EditText numberBox;

    private long timestamp;
    private int setId;
    private boolean allowDelete;
    private boolean editMode;
    private int alarmId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        try{
            if(null == args) throw new MissingArgumentException("You must provide the following arguments for this fragment to work properly: timestamp (long), set_id (int)");
            if(0l == (this.timestamp = args.getLong("timestamp", 0l))) throw new MissingArgumentException("timestamp", "long");
            if(0 == (this.setId = args.getInt("set_id", 0))) throw new MissingArgumentException("set_id", "int");
            this.allowDelete = args.getBoolean("allow_delete", false);
            this.editMode = args.getBoolean("edit_mode", false);
            if(this.editMode){
                if(0 == (this.alarmId = args.getInt("alarm_id", 0))) throw new MissingArgumentException("alarm_id", "int");
            }

            Log.v(TimeToPickerFragment.TAG, String.format("Timestamp = %s", this.timestamp));
            AlertDialog.Builder builder =  new AlertDialog.Builder(this.getActivity())
                    .setTitle(R.string.title_new_alarm_dialog)
                    .setPositiveButton(R.string.alarm_set, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    int amount;
                                    try {
                                        Editable numberField = TimeToPickerFragment.this.numberBox.getText();
                                        if (null != numberField)
                                            amount = Integer.parseInt(numberField.toString());
                                        else amount = 0;
                                    } catch (NumberFormatException e) {
                                        amount = 0;
                                        // TODO: Validate user input
                                    }

                                    int minutes;
                                    Log.v(TimeToPickerFragment.TAG, String.format("User pressed OK, number = %s, unit = %s", amount, TimeToPickerFragment.this.unitSpinner.getSelectedItem()));

                                    // Compute alarm timestamp
                                    long alarmTimestamp = TimeToPickerFragment.this.timestamp;

                                    switch (TimeToPickerFragment.this.unitSpinner.getSelectedItemPosition()) {
                                        case 0:// Unit = hours
                                            minutes = amount * 60;
                                            break;

                                        case 1:// Unit = minutes
                                            minutes = amount;
                                            break;

                                        default:
                                            throw new RuntimeException("Inconsistent item position for time_units array");
                                    }
                                    alarmTimestamp -= minutes * 60000;// * 60000 to convert to ms

                                    Log.v(TimeToPickerFragment.TAG, String.format("Alarm timestamp is %1$s, or %1$tc in a human-readable fashion", alarmTimestamp));

                                    // Save it in the database so it can be restored after reboots
                                    ContentValues values = new ContentValues(2);
                                    values.put("timestamp", alarmTimestamp / 1000);
                                    values.put("set_id", TimeToPickerFragment.this.setId);
                                    try{
                                        if(TimeToPickerFragment.this.editMode){
                                            // FIXME: Update makes a mess out of the timestamp.
                                            Application.getDbHelper().getWritableDatabase().update("alarms", values, "id = " + TimeToPickerFragment.this.alarmId, null);
                                        }
                                        else{
                                            Application.getDbHelper().getWritableDatabase().insertOrThrow("alarms", null, values);
                                        }
                                    }
                                    catch(SQLException e){
                                        if(BuildConfig.DEBUG) e.printStackTrace();
                                        //TODO: Notify the user that something went wrong
                                    }

                                    // Set alarm
                                    SavedAlarm alarm = new SavedAlarm(TimeToPickerFragment.this.alarmId, alarmTimestamp, TimeToPickerFragment.this.setId);
                                    alarm.registerAlarm(TimeToPickerFragment.this.getActivity());
                                }
                            }
                    )
                    .setNegativeButton(R.string.alarm_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Log.v(TimeToPickerFragment.TAG, "User pressed Cancel");
                                    //((ArtistDetailsActivity) getActivity()).doNegativeClick();
                                }
                            }
                    );

            if(this.allowDelete){
                final int alarmId = args.getInt("alarm_id", 0);
                if(0 == alarmId) throw new MissingArgumentException("alarm_id", "int");
                builder.setNeutralButton(R.string.alarm_delete, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        SavedAlarm.delete(alarmId);
                        ((AlarmManagerActivity)TimeToPickerFragment.this.getActivity()).doNeutralClick(TimeToPickerFragment.this.setId, alarmId);
                    }
                });
            }

            final AlertDialog dialog = builder.create();
            @SuppressLint("InflateParams")
            View view = dialog.getLayoutInflater().inflate(R.layout.fragment_time_to_picker, null);
            if(null == view) throw new NullPointerException("Can't inflate the fragment_time_to_picker layout.");
            dialog.setView(view);

            this.unitSpinner = (Spinner)view.findViewById(R.id.unitSpinner);
            if(null == this.unitSpinner) throw new NullPointerException("Couldn't find unitSpinner");
            this.unitSpinner.setSelection(1);

            this.numberBox = (EditText)view.findViewById(R.id.numberBox);
            if(null == this.numberBox) throw new NullPointerException("Couldn't find numberBox");
            this.numberBox.requestFocus();
            if(this.editMode){
                // Compute the number of minutes between alarm and set. Convert to hours if possible
                MusicSet musicSet = MusicSet.getById(this.setId);
                long setTimestamp = musicSet.getBeginDateAsTimestamp();
                if(BuildConfig.DEBUG && setTimestamp < this.timestamp) Log.w(TimeToPickerFragment.TAG, String.format("The alarm (id = %d, td = %d) is set after the set (id = %d) has begun (ts = %d).", this.alarmId, this.timestamp, this.setId, setTimestamp));

                long defaultValue = (setTimestamp - this.timestamp) / 60000;// Convert the delta between timestamps to minutes
                if(0 == defaultValue % 60){
                    defaultValue /= 60;
                    this.unitSpinner.setSelection(0);
                }

                this.numberBox.setText(String.format("%d", defaultValue));
            }

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            return dialog;
        }
        catch(MissingArgumentException e){
            throw new RuntimeException(e.getMessage());
        }
        catch (SetNotFoundException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
            //TODO: Handle this properly
        }
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

    public interface TimeToPickerInterface{
        void doPositiveClick();
        void doNegativeClick();
        void doNeutralClick(int setId, int alarmId);
    }
}

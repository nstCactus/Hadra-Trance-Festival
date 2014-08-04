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
import android.support.annotation.NonNull;
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
import com.zion.htf.exception.AlarmNotFoundException;
import com.zion.htf.exception.MissingArgumentException;

/**
 * An {@link android.app.AlertDialog} used to set or edit an alarm.
 */
// FIXME: Dialog must state clearly that the input must be in hours/minutes to the begining of the set
// TODO: Consider using a spinner as in Calendar app
public class TimeToPickerFragment extends DialogFragment{
    protected static final String TAG = "TimeToPickerFragment";
    TimeToPickerFragment.TimeToPickerInterface listener;

    private Spinner unitSpinner;
    private EditText numberBox;

    private boolean editMode;
    private SavedAlarm alarm;
    private MusicSet set;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle args = this.getArguments();

        this.listener = (TimeToPickerFragment.TimeToPickerInterface)this.getTargetFragment();
        if(null == this.listener) this.listener = (TimeToPickerFragment.TimeToPickerInterface) this.getActivity();
        
        try{
            // Get required arguments (general)
            if(null == args) throw new MissingArgumentException("You must provide the following arguments for this fragment to work properly: timestamp (long), set_id (int)");
            if(!args.containsKey("set_id")) throw new MissingArgumentException("set_id", "int");
            this.editMode = args.getBoolean("edit_mode", false);

            // Get required arguments (edit mode only)
            if(this.editMode){
                if(!args.containsKey("alarm_id")) throw new MissingArgumentException("alarm_id", "int");
                this.alarm = SavedAlarm.getById(args.getInt("alarm_id", 0));
            }

            // Get the set
            this.set = MusicSet.getById(this.editMode ? this.alarm.getSetId() : args.getInt("set_id", 0));

            // Build the dialog
            AlertDialog.Builder builder =  new AlertDialog.Builder(this.getActivity())
                    .setTitle(this.editMode ? R.string.title_edit_alarm_dialog : R.string.title_new_alarm_dialog)
                    .setPositiveButton(R.string.alarm_set, new PositiveClickListener())
                    .setNegativeButton(R.string.alarm_cancel, new NegativeClickListener());

            if(this.editMode){
                builder.setNeutralButton(R.string.alarm_delete, new NeutralClickListener());
            }
            final AlertDialog dialog = builder.create();

            // Add the custom view to the dialog
            @SuppressLint("InflateParams")
            View view = dialog.getLayoutInflater().inflate(R.layout.fragment_time_to_picker, null);
            if(null == view) throw new NullPointerException("Can't inflate the fragment_time_to_picker layout.");
            dialog.setView(view);

            // Initialize the time unit spinner
            this.unitSpinner = (Spinner)view.findViewById(R.id.unitSpinner);
            if(null == this.unitSpinner) throw new NullPointerException("Couldn't find unitSpinner");
            this.unitSpinner.setSelection(1);

            // Initialize the time input box and focus it
            this.numberBox = (EditText)view.findViewById(R.id.numberBox);
            if(null == this.numberBox) throw new NullPointerException("Couldn't find numberBox");
            this.numberBox.requestFocus();

            // Populate the time input box with the saved value (edit mode only)
            if(this.editMode){
                // Compute the number of minutes between alarm and set. Convert to hours if possible
                long setTimestamp = this.set.getBeginDateAsTimestamp();
                if(BuildConfig.DEBUG && setTimestamp < this.alarm.getTimestamp()) Log.w(TimeToPickerFragment.TAG, String.format("The alarm (id = %d, td = %d) is set after the set (id = %d) has begun (ts = %d).", this.alarm.getId(), this.alarm.getTimestamp(), this.set.getId(), setTimestamp));

                long defaultValue = (setTimestamp - this.alarm.getTimestamp()) / 60000;// Convert the delta between timestamps to minutes

                // Convert default value to hours if possible
                if(0 == defaultValue % 60){
                    defaultValue /= 60;
                    this.unitSpinner.setSelection(0);
                }

                this.numberBox.setText(String.format("%d", defaultValue));
            }

            // Display software keyboard
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            return dialog;
        }
        catch(Exception e){
            if(BuildConfig.DEBUG) e.printStackTrace();

            // These errors are serious enough to trigger a force close
            // Report this through piwik
            throw new RuntimeException(e);
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
        void doPositiveClick(int id);
        void doNegativeClick();
        void doNeutralClick(int setId, int alarmId);
    }

    class PositiveClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            boolean databaseOperationSucceeded = true;
            long amount;
            try{
                Editable numberField = TimeToPickerFragment.this.numberBox.getText();
                if(null != numberField){
                    amount = Integer.parseInt(numberField.toString());
                }
                else{
                    amount = 0;
                }
            }
            catch(NumberFormatException e){
                amount = 0;
                // TODO: Validate user input if not using a spinner
            }

            long minutes;

            // Compute alarm timestamp
            long alarmTimestamp = TimeToPickerFragment.this.set.getBeginDateAsTimestamp();

            switch (TimeToPickerFragment.this.unitSpinner.getSelectedItemPosition()) {
                case 0:// Unit = hours
                    minutes = amount * 60;
                    break;

                case 1:// Unit = minutes
                    minutes = amount;
                    break;

                default:
                    throw new RuntimeException("Inconsistent selected item position for time_units array");
            }
            alarmTimestamp -= minutes * 60000;// * 60000 to convert to ms

            // Save it in the database so it can be restored after reboots
            ContentValues values = new ContentValues(2);
            values.put("timestamp", alarmTimestamp / 1000);
            values.put("set_id", TimeToPickerFragment.this.set.getId());
            try {
                if(TimeToPickerFragment.this.editMode){
                    Application.getDbHelper().getWritableDatabase().update("alarms", values, "id = " + TimeToPickerFragment.this.alarm.getId(), null);

                    // Retrieve an updated version of the alarm from database
                    TimeToPickerFragment.this.alarm = SavedAlarm.getById(TimeToPickerFragment.this.alarm.getId());
                }
                else{
                    int alarmId = (int) Application.getDbHelper().getWritableDatabase().insertOrThrow("alarms", null, values);
                    TimeToPickerFragment.this.alarm = new SavedAlarm(alarmId, alarmTimestamp, TimeToPickerFragment.this.set.getId());
                }
            }
            catch(SQLException e){
                databaseOperationSucceeded = false;
                if(BuildConfig.DEBUG) e.printStackTrace();
                //TODO: Notify the user that something went wrong
            }
            catch(AlarmNotFoundException e){
                databaseOperationSucceeded = false;
                if(BuildConfig.DEBUG) e.printStackTrace();
                // TODO: Try to delete and recreate the alarm
            }

            // Set or update the alarm
            TimeToPickerFragment.this.alarm.registerAlarm(TimeToPickerFragment.this.getActivity());
            
            // Notify the activity that something changed
            if(databaseOperationSucceeded){
                TimeToPickerFragment.this.listener.doPositiveClick(TimeToPickerFragment.this.alarm.getId());
            }
        }
    }

    class NegativeClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            // No action needed
        }
    }

    class NeutralClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            TimeToPickerFragment.this.alarm.unregisterAlarm(TimeToPickerFragment.this.getActivity());
            SavedAlarm.delete(TimeToPickerFragment.this.alarm.getId());
            TimeToPickerFragment.this.listener.doNeutralClick(TimeToPickerFragment.this.set.getId(), TimeToPickerFragment.this.alarm.getId());
        }
    }
}

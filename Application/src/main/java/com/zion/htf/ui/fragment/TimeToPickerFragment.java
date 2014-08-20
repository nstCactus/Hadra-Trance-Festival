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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
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
public class TimeToPickerFragment extends DialogFragment implements DialogInterface.OnClickListener{
    protected static final String TAG = "TimeToPickerFragment";
    TimeToPickerInterface listener;

    private Spinner unitSpinner;
    private EditText numberBox;

    private boolean editMode;
    private SavedAlarm alarm;
    private MusicSet set;
	private int minutesToSet;
	private static int[] valuesInMinutes;

	@Override
	public void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);

		if(null == TimeToPickerFragment.valuesInMinutes) TimeToPickerFragment.valuesInMinutes = this.getActivity().getResources().getIntArray(R.array.minutes_to_set_values);
	}

	@NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle args = this.getArguments();

        this.listener = (TimeToPickerInterface)this.getTargetFragment();
        if(null == this.listener) this.listener = (TimeToPickerInterface) this.getActivity();

        try{
	        int defaultValue = 3;

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

	            // Set the selected item according to the saved value
				defaultValue = TimeToPickerFragment.indexOfMinuteValue((int)(this.set.getBeginDateAsTimestamp() - this.alarm.getTimestamp()) / 60000);
            }

			builder.setSingleChoiceItems(R.array.minutes_to_set_labels, defaultValue, this);

            return builder.create();
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

	@Override
	public void onClick(DialogInterface dialog, int which){
		this.minutesToSet = this.valuesInMinutes[which];
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

            // Compute alarm timestamp
            long alarmTimestamp = TimeToPickerFragment.this.set.getBeginDateAsTimestamp();
            alarmTimestamp -= TimeToPickerFragment.this.minutesToSet * 60000;// * 60000 to convert to ms

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

	/**
	 * Returns the index of the given {@code value} in {@link #valuesInMinutes}
	 * @param value the value to look for
	 * @return the position of the value, or {@code -1} if the value is not in {@link #valuesInMinutes}
	 */
	private static int indexOfMinuteValue(int value){
		int i = -1;
		boolean found = false;

		while(!found && ++i < TimeToPickerFragment.valuesInMinutes.length){
			if(value == TimeToPickerFragment.valuesInMinutes[i]) found = true;
		}

		return i;
	}
}

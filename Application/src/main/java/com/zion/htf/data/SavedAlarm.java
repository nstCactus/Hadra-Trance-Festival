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

package com.zion.htf.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.util.Log;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.exception.AlarmNotFoundException;
import com.zion.htf.exception.SetNotFoundException;
import com.zion.htf.receiver.AlarmReceiver;
import com.zion.util.DateUtils;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The {@code SavedAlarm} class holds data related to user-defined alarms as well as methods to retrieve this data from the {@code alarms} database table.
 * As it extends {@link com.zion.htf.data.Item}, it can be used as an item in a {@link com.hb.views.PinnedSectionListView} using a {@link com.zion.htf.adapter.AlarmsListAdapter} as its source.
 */
public class SavedAlarm extends Item{
    private static final SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();
    /**
     * HACK! Selection order must be the same as in {@link MusicSet.QUERY}. Any added selected column must be APPENDED to the list.
     */
    @SuppressWarnings("JavadocReference")
    private static final String SETS_QUERY = "SELECT sets.*, artists.*, alarms.id AS aId, alarms.timestamp AS aTimestamp FROM sets JOIN artists ON sets.artist = artists.id JOIN alarms ON sets.id = alarms.set_id ";
    private static final String ALARMS_QUERY = "SELECT * FROM alarms ";
    private static final String ALARMS_FROM_SET_ID_QUERY = "SELECT alarms.id, alarms.timestamp, alarms.set_id FROM alarms JOIN sets ON sets.id = set_id ";

    private int id;
    private long timestamp;
    private int setId;
    private MusicSet set;

    public SavedAlarm(String name){
        super(name, Item.TYPE_ITEM);
    }

    public SavedAlarm(int id, long timestamp, int setId){
        super("", Item.TYPE_ITEM);

        this.id = id;
        this.timestamp = timestamp;
        this.setId = setId;
    }

    /*********************/
    /* Getters & Setters */
    /*********************/
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * Get the set associated to the {@code SavedAlarm} instance fetching it if necessary
     * @return The {@link com.zion.htf.data.MusicSet} associated to this {@code SavedAlarm} instance
     */
    public MusicSet getSet(){
        MusicSet set = null;
        if(null != this.set){
            set = this.set;
        }
        else if(0 != this.setId){
            try{
                set = MusicSet.getById(this.setId);
            }
            catch(SetNotFoundException e){
                set = null;
            }
        }
        return set;
    }

    /**
     * Get the database identifier of the {@link com.zion.htf.data.MusicSet} associated to this
     * {@code SavedAlarm} instance, fetching it from the inner {@link com.zion.htf.data.MusicSet}
     * instance if necessary
     * @return The database identifier of the associated {@link com.zion.htf.data.MusicSet}
     */
    public int getSetId(){
        int setId = 0;
        if(0 != this.setId)             setId = this.setId;
        else if(null != this.getSet())  setId = this.getSet().getId();
        return setId;
    }

    public SavedAlarm setSetId(int setId){
        this.setId = setId;
        return this;
    }

    public void setSet(MusicSet set) {
        this.set = set;
    }

    /******************/
    /* Static methods */
    /******************/

    /**
     * Return a {@link List} of the {@code SavedAlarm} that are persisted in the database.
     * @return A {@link List} of {@code SavedAlarm} objects
     */
    public static List<Item> getList(){
        return SavedAlarm.getList(false);
    }
    /**
     * Return a {@link List} of the {@code SavedAlarm} that are persisted in the database.
     * @param addDateSeparators A {@code boolean} indicating whether to add {@link com.zion.htf.data.Item} as date separator (for use in a {@link com.zion.htf.adapter.LineUpListAdapter})
     * @return A {@link List} of {@code SavedAlarm} objects
     */
    public static List<Item> getList(Boolean addDateSeparators){
        List<Item> alarms = new ArrayList<Item>();

        Cursor cursor = SavedAlarm.dbOpenHelper.getReadableDatabase().rawQuery(SavedAlarm.SETS_QUERY, null);
        int columnIndexId = cursor.getColumnIndex("aId");
        int columnIndexTimestamp = cursor.getColumnIndex("aTimestamp");

        Date previousDate = new Date(0);
        DateFormat dateFormat = "fr".equals(Locale.getDefault().getLanguage()) ? DateFormat.getDateInstance(DateFormat.FULL, Locale.FRENCH) : DateFormat.getDateInstance(DateFormat.FULL);

        while(cursor.moveToNext()){
            MusicSet set = MusicSet.newInstance(cursor);
            SavedAlarm alarm = SavedAlarm.newInstance(cursor.getInt(columnIndexId), cursor.getLong(columnIndexTimestamp) * 1000, set);

            // Add date separator items if required
            if(addDateSeparators){
                Date alarmDate = new Date(alarm.getTimestamp());
                if (!DateUtils.areSameDay(previousDate, alarmDate)){
                    alarms.add(new Item(dateFormat.format(alarmDate), Item.TYPE_SECTION));
                }
                previousDate = alarmDate;
            }

            alarms.add(alarm);
        }

        if(!cursor.isClosed()) cursor.close();
        SavedAlarm.dbOpenHelper.close();

        return alarms;
    }

    /**
     * Return a new {@code SavedAlarm} with the given parameters
     * @param id        Database id
     * @param timestamp Timestamp representing the user-defined moment to fire up the alarm
     * @param set       The {@link com.zion.htf.data.MusicSet} with which this alarm is associated
     * @return          A new {@code SavedAlarm} object
     */
    public static SavedAlarm newInstance(int id, long timestamp, MusicSet set){
        SavedAlarm alarm = new SavedAlarm("");
        alarm.id = id;
        alarm.timestamp = timestamp;
        alarm.set = set;

        return alarm;
    }

    /**
     * Delete the alarm having {@code id} as database identifier
     * @param id The database identifier of the alarm
     */
    public static int delete(int id){
        return SavedAlarm.dbOpenHelper.getWritableDatabase().delete("alarms", "id = " + id, null);
    }


    /**
     * Delete the alarms having {@code id} as database identifier
     * @param id The database identifiers of the alarms
     */
    public static void delete(int[] id){
        String inClause = "";
        for (int i = 0; i < id.length; i++){
            if(0 < i) inClause += ",";
            inClause += String.valueOf(id[i]);
        }
        SavedAlarm.delete(inClause);
    }

    /**
     * Delete the alarms of which database identifier matches the given {@code IN} clause
     * @param in The {@code IN} clause (without the {@code IN} keyword itself)
     * @return the number of deleted rows
     */
    public static int delete(String in){
        SQLiteStatement statement = SavedAlarm.dbOpenHelper.getWritableDatabase().compileStatement(String.format(Locale.ENGLISH, "DELETE FROM alarms WHERE id IN(%s);", in));
        return statement.executeUpdateDelete();
    }

    /**
     * Re-set an alarm in {@link android.app.AlarmManager} for each entry in the {@code alarms} table of the database.
     * @param context The Context in which the {@link com.zion.htf.receiver.BootReceiver} was running.
     */
    public static void restoreAlarms(Context context){
        Cursor cursor = SavedAlarm.dbOpenHelper.getReadableDatabase().rawQuery(SavedAlarm.ALARMS_QUERY, null);
        int columnId = cursor.getColumnIndexOrThrow("id");
        int columnTimestamp = cursor.getColumnIndexOrThrow("timestamp");
        int columnSetId = cursor.getColumnIndexOrThrow("set_id");

        SavedAlarm alarm;// Dummy alarm
        while(cursor.moveToNext()){
            if(!cursor.isNull(columnSetId) && !cursor.isNull(columnTimestamp)){
                if(BuildConfig.DEBUG) Log.v("SavedAlarm", String.format(Locale.ENGLISH, "Restoring alarms #%1$d set to go off %2$ta %2$tF %2$tT (set #%3$d)", cursor.getInt(columnId), cursor.getLong(columnTimestamp) * 1000, cursor.getInt(columnSetId)));
                alarm = new SavedAlarm(cursor.getInt(columnId), cursor.getLong(columnTimestamp) * 1000, cursor.getInt(columnSetId));

                alarm.registerAlarm(context);
            }
        }
        if(!cursor.isClosed()) cursor.close();
        SavedAlarm.dbOpenHelper.close();
    }

    /**
     * Register an alarm in the System's {@link android.app.AlarmManager}
     * @param context   The {@link android.content.Context} in which the application is currently running
     */
    public void registerAlarm(Context context){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("set_id", this.getSetId());
        intent.putExtra("alarm_id", this.getId());
        intent.setAction("com.zion.htf_" + this.getSetId());// This only allows one alarm per set.
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (19 <= Build.VERSION.SDK_INT)    am.setExact(AlarmManager.RTC_WAKEUP, this.getTimestamp(), alarmIntent);
        else                                am.set(AlarmManager.RTC_WAKEUP, this.getTimestamp(), alarmIntent);
    }

    /**
     * Unregister an alarm in the System's {@link android.app.AlarmManager}
     * @param context   The {@link android.content.Context} in which the application is currently running
     */
    public void unregisterAlarm(Context context){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("set_id", this.getSetId());
        intent.putExtra("alarm_id", this.getId());
        intent.setAction("com.zion.htf_" + this.getSetId());// This only allows one alarm per set.
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Never null as FLAG_NO_CREATE is not passed to PendingIntent.getBroadcast()t
        alarmIntent.cancel();
        am.cancel(alarmIntent);

    }

    /**
     * Get an alarm using its database identifier
     * @param id The database identifier of the {@code SavedAlarm}
     * @return  a {@code SavedAlarm} instance fetched from the {@code alarms} database table
     * @throws AlarmNotFoundException
     */
    public static SavedAlarm getById(int id) throws AlarmNotFoundException{
        SavedAlarm alarm;
        Cursor cursor = SavedAlarm.dbOpenHelper.getReadableDatabase().rawQuery(String.format(Locale.ENGLISH, "%s WHERE id = %d", SavedAlarm.ALARMS_QUERY, id), null);

        if(null == cursor || !cursor.moveToFirst()) throw new AlarmNotFoundException(id);
        int columnId = cursor.getColumnIndexOrThrow("id");
        int columnTimestamp = cursor.getColumnIndexOrThrow("timestamp");
        int columnSetId = cursor.getColumnIndexOrThrow("set_id");

        alarm = new SavedAlarm(cursor.getInt(columnId), cursor.getLong(columnTimestamp) * 1000, cursor.getInt(columnSetId));

        return alarm;
    }

    /**
     * Get an alarm using the database identifier of its associated {@link com.zion.htf.data.MusicSet}
     * @param setId the database identifier of the associated {@link com.zion.htf.data.MusicSet}
     * @return  a {@code SavedAlarm} instance fetched from the {@code alarms} database table
     */
    public static SavedAlarm findBySetId(int setId) throws AlarmNotFoundException{
        SavedAlarm alarm;
        Cursor cursor = SavedAlarm.dbOpenHelper.getReadableDatabase().rawQuery(String.format(Locale.ENGLISH, "%s WHERE set_id = %d", SavedAlarm.ALARMS_FROM_SET_ID_QUERY, setId), null);

        if(null == cursor || !cursor.moveToFirst()) throw new AlarmNotFoundException(String.format(Locale.ENGLISH, "No alarm found for the set with id = %d", setId));
        int columnId = cursor.getColumnIndexOrThrow("id");
        int columnTimestamp = cursor.getColumnIndexOrThrow("timestamp");
        int columnSetId = cursor.getColumnIndexOrThrow("set_id");

        alarm = new SavedAlarm(cursor.getInt(columnId), cursor.getLong(columnTimestamp) * 1000, cursor.getInt(columnSetId));

        return alarm;
    }
}

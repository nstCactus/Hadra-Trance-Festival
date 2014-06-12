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

import android.database.Cursor;

import com.zion.htf.Application;
import com.zion.htf.exception.InconsistentDatabaseException;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.ArrayList;

public class Stage {
    private String name;
    private double latitude;
    private double longitude;
    private static final String QUERY = "SELECT lst__stages.*, locations.latitude, locations.longitude FROM lst__stages JOIN locations ON location_id = id ";
    private static final SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();

    /* Column indexes for convenience */
    private static final int COLUMN_NAME = 0;
    private static final int COLUMN_LOCATION_ID = 1;
    private static final int COLUMN_LOCATIONS_LATITUDE = 2;
    private static final int COLUMN_LOCATIONS_LONGITUDE = 3;

    public Stage(String stageName, double latitude, double longitude){
        this.name = stageName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Stage getByName(String stageName) throws InconsistentDatabaseException {
        Stage stage = null;
        Cursor cursor = Stage.dbOpenHelper.getReadableDatabase().rawQuery(Stage.QUERY + " WHERE stage = ?", new String[]{stageName});

        if(null != cursor){
            if(1 < cursor.getCount()) throw new InconsistentDatabaseException("Stage names must be unique");
            if(cursor.moveToNext()){
                stage = new Stage(stageName, cursor.getDouble(Stage.COLUMN_LOCATIONS_LATITUDE), cursor.getDouble(Stage.COLUMN_LOCATIONS_LONGITUDE));
            }
            cursor.close();
        }
        Stage.dbOpenHelper.close();

        return stage;
    }

    public String getName() {
        return this.name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Get the list of the stages from the database
     * @return The list of the festival's stages, as an {@link java.util.ArrayList} instance
     */
    public static ArrayList<Stage> getList() {
        ArrayList<Stage> stages = new ArrayList<Stage>();
        Cursor cursor = Stage.dbOpenHelper.getReadableDatabase().rawQuery(Stage.QUERY, null);

        if(null != cursor){
            while(cursor.moveToNext()){
                if(!cursor.isNull(Stage.COLUMN_NAME)) stages.add(new Stage(cursor.getString(Stage.COLUMN_NAME), cursor.getLong(Stage.COLUMN_LOCATIONS_LATITUDE), cursor.getLong(Stage.COLUMN_LOCATIONS_LONGITUDE)));
            }
            cursor.close();
        }
        Stage.dbOpenHelper.close();
        return stages;
    }
}

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
import android.database.sqlite.SQLiteDatabase;

import com.zion.htf.Application;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.Date;

public class Festival {
    private static final String ORDER_ASC   = "ASC";
    private static final String ORDER_DESC  = "DESC";
    private static final SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();

    public static Date getFestivalStartDate(){
        return Festival.getFestivalBound(Festival.ORDER_ASC);
    }

    public static Date getFestivalEndDate(){
        return Festival.getFestivalBound(Festival.ORDER_DESC);
    }

    private static Date getFestivalBound(String order){
        SQLiteDatabase db = Festival.dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT begin_date FROM sets ORDER BY begin_date " + order + " LIMIT 1;", null);
        if(!cursor.moveToNext() || cursor.isNull(0)) throw new RuntimeException("Error getting festival start/begin date");
        return new Date(cursor.getLong(0) * 1000);
    }
}

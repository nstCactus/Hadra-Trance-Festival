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

package com.zion.content;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class SQLiteCursorLoader extends AsyncTaskLoader<Cursor> {
    SQLiteDatabase db;
    String rawQuery;
    String[] queryArgs;

    public SQLiteCursorLoader(Context context, SQLiteDatabase db, String rawQuery, String[] args){
        super(context);

        this.db = db;
        this.rawQuery = rawQuery;
        this.queryArgs = args;
    }

    @Override
    public Cursor loadInBackground(){
        Log.v("SQLiteCursorLoader", "Begin loading data from db");
        return this.db.rawQuery(this.rawQuery, this.queryArgs);
    }
}

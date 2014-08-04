/*
    Copyright 2013 Yohann Bianchi

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
package com.zion.htf;

import android.content.Context;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;
import org.michenux.android.info.VersionUtils;

public class Application extends android.app.Application{
	private static SQLiteDatabaseHelper dbHelper = null;
	private static Context context;
    private static final int PIWIK_SITE_ID = 4;

	@Override
	public void onCreate(){
		super.onCreate();
        Application.context = this;
	}

	public static SQLiteDatabaseHelper getDbHelper(){
		if(null == Application.dbHelper)
			Application.dbHelper = new SQLiteDatabaseHelper(Application.context, "hadra", null, VersionUtils.getVersionCode(Application.context));
		return Application.dbHelper;
	}

	public static Context getContext(){
		return Application.context;
	}
}

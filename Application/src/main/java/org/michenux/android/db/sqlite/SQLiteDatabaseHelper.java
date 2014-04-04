package org.michenux.android.db.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zion.htf.BuildConfig;

import org.michenux.android.db.utils.SqlParser;
import org.michenux.android.resources.AssetUtils;

import java.io.IOException;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper{
	private static final String SQL_DIR            = "sql";
	private static final String CREATEFILE         = "create.sql";
	private static final String UPGRADEFILE_PREFIX = "upgrade-";
	private static final String UPGRADEFILE_SUFFIX = ".sql";
	private static final String TAG                = "SQLiteDatabaseHelper";
	private Context context;

	public SQLiteDatabaseHelper(Context context, String name,
								CursorFactory factory, int version){
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		try{
			if(BuildConfig.DEBUG) Log.v(TAG, ("create database"));
			execSqlFile(CREATEFILE, db);
		}
		catch(IOException exception){
			throw new RuntimeException("Database creation failed", exception);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		try{
			if(BuildConfig.DEBUG) Log.v(TAG, ("upgrade database from " + oldVersion + " to " + newVersion));
			for(String sqlFile : AssetUtils.list(SQL_DIR, this.context.getAssets())){
				if(sqlFile.startsWith(UPGRADEFILE_PREFIX)){
					int fileVersion = Integer.parseInt(sqlFile.substring(UPGRADEFILE_PREFIX.length(), sqlFile.length() - UPGRADEFILE_SUFFIX.length()));
					if(fileVersion > oldVersion && fileVersion <= newVersion){
						execSqlFile(sqlFile, db);
					}
				}
			}
		}
		catch(IOException exception){
			throw new RuntimeException("Database upgrade failed", exception);
		}
	}

	protected void execSqlFile(String sqlFile, SQLiteDatabase db) throws SQLException, IOException{
		if(BuildConfig.DEBUG) Log.v(TAG, ("  exec sql file: " + sqlFile));
		for(String sqlInstruction : SqlParser.parseSqlFile(SQL_DIR + "/" + sqlFile, this.context.getAssets())){
			db.execSQL(sqlInstruction);
		}
	}
}

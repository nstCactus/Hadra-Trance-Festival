package org.michenux.android.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class VersionUtils{
	private static final String TAG = "VersionUtils";

	public static int getVersionCode(Context context){
		PackageInfo manager = null;
		try{
			manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		}
		catch(NameNotFoundException e){
			Log.e(TAG, "Error getting application version", e);
		}
		return manager.versionCode;
	}
}

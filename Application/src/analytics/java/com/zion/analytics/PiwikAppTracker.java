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

package com.zion.analytics;

import android.annotation.SuppressLint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

import com.zion.analytics.db.PiwikDatabaseHelper;
import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.util.StringUtils;

import org.piwik.PiwikException;
import org.piwik.SimplePiwikTracker;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/*
# Analytics in android apps

## Basic workflow

1. The application tracks users actions by making calls to track*() methods
2. These methods records user actions in the database managed by `PiwikDatabaseHelper`
3. A service takes care of periodically submitting the content of the database to the Piwik instance

## Service

* The service should be able to check the device connectivity status and do the following if it is
not connected to the internet:
    1. refrain from uploading results to the database
    2. stop itself
* It should also handle network errors and only purge the database on successful submission.
* It should be stopped when:
    * the app is not running and the database no longer contains unsubmitted entries
    * the device is no longer connected to the internet
* It should be able to hold a `WakeLock` while it is working.

## Connectivity change `BroadcastReceiver`

A `BroadcastReceiver` should be registered to toggle the service when device connectivity changes.
 */
public class PiwikAppTracker{
    private static final int VERSION = 1;
    private static String apiURL;
    private static int idSite = 4;
    private static SimplePiwikTracker tracker;
    private static PiwikDatabaseHelper dbHelper;
    private static Context context;

    private static int screenWidth;
    private static int screenHeight;
    private static int screenPixelDensity;

    public PiwikAppTracker(Context context, String apiURL, int idSite){
        PiwikAppTracker.context = context;
        PiwikAppTracker.apiURL = apiURL;
        PiwikAppTracker.idSite = idSite;

        if(null == PiwikAppTracker.tracker){
            if(null == PiwikAppTracker.dbHelper){
                PiwikAppTracker.dbHelper = new PiwikDatabaseHelper(Application.getContext(), "pending-analytics-submissions.sqlite", null, PiwikAppTracker.VERSION);
            }

            try{
                PiwikAppTracker.tracker = new SimplePiwikTracker(PiwikAppTracker.apiURL);
                PiwikAppTracker.tracker.setIdSite(PiwikAppTracker.idSite);
                PiwikAppTracker.tracker.setVisitorId(PiwikAppTracker.getVisitorUID());
                PiwikAppTracker.tracker.setAcceptLanguage(Locale.getDefault());
                PiwikAppTracker.tracker.setResolution(PiwikAppTracker.getScreenWidth(), PiwikAppTracker.getScreenHeight());
                PiwikAppTracker.tracker.setVisitorCustomVariable("screen density", String.format("%d", PiwikAppTracker.getPixelDensity()));
                PiwikAppTracker.tracker.setVisitorCustomVariable("android version", String.format("%s (%s)", Build.VERSION.CODENAME, Build.VERSION.RELEASE));
                PiwikAppTracker.tracker.setVisitorCustomVariable("android API level", String.format("%d", Build.VERSION.SDK_INT));
            }
            catch(PiwikException e){
                if(BuildConfig.DEBUG) e.printStackTrace();
                // TODO: Decide whether there's something more to do or not
            }
        }
        else{
            PiwikAppTracker.tracker.setLocalTime(new Date());
        }
    }

    public void trackWindow(String name, String referrer){
        SimplePiwikTracker tracker = PiwikAppTracker.tracker;
        tracker.setPageUrl("/window/" + name);
        try{
            tracker.setUrlReferrer("/window/" + referrer);
        }
        catch (PiwikException e){
            if(BuildConfig.DEBUG) e.printStackTrace();
            // TODO: Decide whether there's something more to do or not
        }

        Calendar forcedTime = new GregorianCalendar();
        forcedTime.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        tracker.setForcedDatetime(forcedTime.getTime());

        Date localTime = new GregorianCalendar().getTime();
        tracker.setLocalTime(localTime);

        PiwikAppTracker.queueURL(PiwikAppTracker.tracker.getPageTrackURL(name));
    }

    public static boolean queueURL(URL url){
        return PiwikAppTracker.queueURL(url.toString());
    }

    public static boolean queueURL(String url){
        if(null == url) throw new NullPointerException("The parameter url cannot be null. Please provide a value.");
        boolean insertSuccessful = false;

        SQLiteDatabase db = PiwikAppTracker.dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("url", url);

        db.beginTransaction();
        long id = db.insert("url", null, values);

        if(-1 < id){
            db.setTransactionSuccessful();
            insertSuccessful = true;
        }
        db.endTransaction();
        db.close();

        return insertSuccessful;
    }

    /**
     * Return the visitor's unique identifier from database
     * @return the visitor's UID
     */
    private static String getVisitorUID(){
        String uid;
        SQLiteDatabase db = PiwikAppTracker.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT value FROM config WHERE key = ?", new String[]{"uid"});
        switch(cursor.getCount()){
            case 0:
                uid = PiwikAppTracker.createVisitorUID();
                break;
            case 1:
                cursor.moveToFirst();
                uid = cursor.getString(0);
                break;
            default:
                throw new IllegalStateException("There are several record with key \"uid\" in the \"config\", which is not supposed to happen.");
        }
        cursor.close();

        return uid;
    }

    /**
     * Create an unique identifier for this visitor
     * @throws IllegalStateException if inserting the uid in the database fail
     * @return the visitor's UID
     */
    private static String createVisitorUID()throws IllegalStateException{
        // uid = Android version codename + android_id + device manufacturer + device model + timestamp
        String uid = Build.VERSION.CODENAME;
        uid +=Settings.Secure.getString(Application.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        uid += Build.MODEL.startsWith(Build.MANUFACTURER) ? Build.MODEL : Build.MANUFACTURER + Build.MODEL;
        uid += System.currentTimeMillis();
        uid = StringUtils.hashMD5(uid);

        if(!PiwikAppTracker.saveVisitorUID(uid)) throw new IllegalStateException("An error occurred while saving the visitor UID in the database.");

        return uid;
    }

    /**
     * Save the visitor unique identifier in the databse
     * @param uid the UID
     * @return {@code true} if the UID was saved, {@code false} otherwise.
     */
    private static boolean saveVisitorUID(String uid){
        boolean insertSuccessful = false;

        // Prepare values
        ContentValues values = new ContentValues();
        values.put("key", "uid");
        values.put("value", uid);

        // Insert into db
        SQLiteDatabase db = PiwikAppTracker.dbHelper.getWritableDatabase();
        db.beginTransaction();
        long id = db.insert("config", null, values);
        if(-1 < id){
            db.setTransactionSuccessful();
            insertSuccessful = true;
        }
        db.close();

        return insertSuccessful;
    }

    /**
     * Get the screen width in pixel
     * @return the screen width
     */
    public static int getScreenWidth(){
        if(0 == PiwikAppTracker.screenWidth) PiwikAppTracker.getScreenDimensions();

        return PiwikAppTracker.screenWidth;
    }

    /**
     * Get the screen height in pixel
     * @return the screen height
     */
    public static int getScreenHeight(){
        if(0 == PiwikAppTracker.screenHeight) PiwikAppTracker.getScreenDimensions();

        return PiwikAppTracker.screenHeight;
    }

    /**
     * Get the device's screen pixel density
     * @return the pixel density
     */
    public static int getPixelDensity(){
        if(0 == PiwikAppTracker.screenPixelDensity) {
            PiwikAppTracker.screenPixelDensity = PiwikAppTracker.context.getResources().getDisplayMetrics().densityDpi;
        }

        return PiwikAppTracker.screenPixelDensity;
    }

    /**
     * Fetch screen dimensions in pixel for use by {@code getScreenWidth()} and {@code getScreenHeight()} methods
     */
    @SuppressLint("NewApi")
    private static void getScreenDimensions(){
        WindowManager windowManager = (WindowManager) PiwikAppTracker.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        if(13 > Build.VERSION.SDK_INT){
            PiwikAppTracker.screenWidth = display.getWidth();
            PiwikAppTracker.screenHeight = display.getHeight();
        }
        else{
            Point screenSize = new Point();
            display.getSize(screenSize);
            PiwikAppTracker.screenWidth = screenSize.x;
            PiwikAppTracker.screenHeight = screenSize.y;
        }
    }
}

/*
 * Copyright 2013 Yohann Bianchi
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or see <http://www.gnu.org/licenses/>.
 */

package com.zion.htf.data;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

public class ArtistOnStageTask extends TimerTask{
	private static final String               ORDER_ASC    = "ASC";
	private static final String               ORDER_DESC   = "DESC";
	private static final String               TAG          = "ArtistOnStageTimerTask";
	private static       SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();
	private static ArrayList<String> stages;
	private final  Activity          hostActivity;
	private static int currentItem = -1;
	private WeakReference<TextView> stageField;
	private WeakReference<TextView> artistField;
	private WeakReference<TextView> hourField;

	public ArtistOnStageTask(WeakReference<Activity> hostActivity){
		this.hostActivity = hostActivity.get();
		if(null != this.hostActivity){
			this.stageField		= new WeakReference<TextView>((TextView)((Activity)hostActivity.get()).findViewById(R.id.stage));
			this.artistField	= new WeakReference<TextView>((TextView)((Activity)hostActivity.get()).findViewById(R.id.artist_name));
			this.hourField		= new WeakReference<TextView>((TextView)((Activity)hostActivity.get()).findViewById(R.id.hour));
		}
	}

	@Override
	public void run(){
		Set currentSet = null;
		SQLiteDatabase database = ArtistOnStageTask.dbOpenHelper.getReadableDatabase();
		Log.v(TAG, "Getting artist on stage");
		if(null == ArtistOnStageTask.stages){
			Log.v(TAG, "Fetching stages");
			ArtistOnStageTask.stages = new ArrayList<String>();
			Cursor cursor = database.rawQuery("SELECT stage FROM lst__stages;", null);
			while(cursor.moveToNext()) if(!cursor.isNull(0)) this.stages.add(cursor.getString(0));
			cursor.close();
		}

		if(ArtistOnStageTask.stages.size() < 1) throw new RuntimeException("No stages were found in the databse.");

		Date currentDate = new Date();
		Date bound;
		if(currentDate.before(bound = this.getFestivalStartDate())){
			Log.v(TAG, "Pas commencé. Début à " + bound.toString());
			StringBuilder remainingTime = new StringBuilder();
			int secondsFromStart = (int)(bound.getTime() / 1000 - currentDate.getTime() / 1000);
			int daysFromStart = secondsFromStart / 86400;
			int remainingSeconds = secondsFromStart % 86400;
			int hoursFromStart = remainingSeconds / 3600;
			remainingSeconds = secondsFromStart % 3600;
			int minutesFromStart = remainingSeconds / 60;

			currentSet = new Set(String.format(Locale.getDefault().getLanguage().equals("fr") ? "%dj %dh %dm" : "%dd %dh %dm", daysFromStart, hoursFromStart, minutesFromStart), Item.TYPE_SECTION);
			currentSet.setStage(Application.getContext().getString(R.string.remaining_time));
			currentSet.setGenre(Application.getContext().getString(R.string.before_opening));
		}
		else if(currentDate.after(bound = this.getFestivalEndDate())){
			Log.v(TAG, "Déjà fini depuis " + bound.toString());
			// Message trop tard
			currentSet = new Set(Application.getContext().getString(R.string.festival_over), Item.TYPE_SECTION);
		}
		else{
			int failureCount = 0;
			while(failureCount < ArtistOnStageTask.stages.size() && null == currentSet){
				currentSet = this.fetchCurrentSet();
				failureCount++;
			}
			if(null == currentSet){
				if(BuildConfig.DEBUG) Log.v(TAG, "Daily break on all stages at the same time... Very unlikely to happen.");
				currentSet = new Set(Application.getContext().getString(R.string.daily_break, Item.TYPE_SECTION));
				currentSet.setStage(Application.getContext().getString(R.string.all_stages));
			}
		}
		database.close();

		this.updateUI(currentSet);
	}

	private void updateUI(final Set set){
		this.hostActivity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				if(null != stageField && null != artistField && null != hourField)
				// If set.type == Item.TYPE_ITEM, this is an actual match
				stageField.get().setText(set.getStage());
				artistField.get().setText(set.toString());
				if(set.getType() == Item.TYPE_ITEM){
					hourField.get().setText(String.format(Locale.getDefault().getLanguage().equals("fr") ? "%1$tHh %1$tM - %2$tHh %2$tM" : "%1$tl:%1$tM %1$tp - %2$tl:%2$tM %2$tp", set.getBeginDate(), set.getEndDate()));
				}
				// Otherwise, it is either too early or too late or daily break
				else{
					hourField.get().setText(null != set.getGenre() ? set.getGenre() : "");
				}
			}
		});
	}

	private Date getFestivalStartDate(){
		return this.getFestivalBound(ORDER_ASC);
	}

	private Date getFestivalEndDate(){
		return this.getFestivalBound(ORDER_DESC);
	}

	private Date getFestivalBound(String order){
		SQLiteDatabase db = ArtistOnStageTask.dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT begin_date FROM sets ORDER BY begin_date " + order + " LIMIT 1;", null);
		if(!cursor.moveToNext() || cursor.isNull(0)) throw new RuntimeException("Error getting festival start/begin date");
		return new Date(cursor.getLong(0) * 1000);
	}

	private Set fetchCurrentSet(){
		Set set = null;
		SQLiteDatabase db = ArtistOnStageTask.dbOpenHelper.getReadableDatabase();
		ArtistOnStageTask.currentItem = ++ArtistOnStageTask.currentItem % ArtistOnStageTask.stages.size();
		Cursor cursor = db.rawQuery("SELECT stage, artists.name, begin_date, end_date FROM sets JOIN artists ON sets.artist = artists.id WHERE stage = ? AND ? BETWEEN begin_date AND end_date;", new String[]{this.stages.get(this.currentItem), String.valueOf(System.currentTimeMillis() / 1000)});
		if(cursor.moveToNext()){
			set = new Set(cursor.getString(1));
			set.setBeginDate(new Date(cursor.getLong(2) * 1000));
			set.setEndDate(new Date(cursor.getLong(3) * 1000));
			set.setStage(cursor.getString(0));
		}

		return set;
	}
}

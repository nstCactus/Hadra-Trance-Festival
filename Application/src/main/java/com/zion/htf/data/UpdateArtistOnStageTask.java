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
import android.util.Log;
import android.widget.TextView;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;
/*
This task manages the children tasks that retrieve data from db and updates UI periodically
 */
/* TODO: Create one task per stage and make them smart.
    Each task should execute once and stop until the next set (use an alarm to run it when time comes)
    A parent task should manage the periodical UI updates
*/
public class UpdateArtistOnStageTask extends TimerTask{
	private static final String               TAG          = "ArtistOnStageTimerTask";
	private static ArrayList<Stage> stages;
	private final  Activity         hostActivity;
	private static int              currentItem = -1;

	private WeakReference<TextView> stageField;
	private WeakReference<TextView> artistField;
    private WeakReference<TextView> hourField;
    private WeakReference<TextView> separatorField;

	public UpdateArtistOnStageTask(WeakReference<Activity> hostActivity){
		this.hostActivity = hostActivity.get();
		if(null != this.hostActivity){
			this.stageField		= new WeakReference<TextView>((TextView) hostActivity.get().findViewById(R.id.stage));
			this.artistField	= new WeakReference<TextView>((TextView) hostActivity.get().findViewById(R.id.artist_name));
            this.hourField		= new WeakReference<TextView>((TextView) hostActivity.get().findViewById(R.id.hour));
            this.separatorField = new WeakReference<TextView>((TextView) hostActivity.get().findViewById(R.id.separator));
		}
	}

	@Override
	public void run(){
		Log.v(UpdateArtistOnStageTask.TAG, "Getting artist on stage");
		if(null == UpdateArtistOnStageTask.stages){
			Log.v(UpdateArtistOnStageTask.TAG, "Fetching stages");
            UpdateArtistOnStageTask.stages = Stage.getList();
		}

		if(1 > UpdateArtistOnStageTask.stages.size()) throw new RuntimeException("No stages were found in the database.");

		Date currentDate = new Date();
		Date bound;

        UpdateArtistOnStageTask.DataHolder data = new UpdateArtistOnStageTask.DataHolder();

        if(currentDate.before(bound = Festival.getFestivalStartDate())){
            // Festival hasn't begun yet
			Log.v(UpdateArtistOnStageTask.TAG, "Pas commencé. Début à " + bound.toString());
			int secondsFromStart = (int)(bound.getTime() / 1000 - currentDate.getTime() / 1000);
			int daysFromStart = secondsFromStart / 86400;
			int remainingSeconds = secondsFromStart % 86400;
			int hoursFromStart = remainingSeconds / 3600;
			remainingSeconds = secondsFromStart % 3600;
			int minutesFromStart = remainingSeconds / 60;

            data.main = String.format("fr".equals(Locale.getDefault().getLanguage()) ? "%dj %dh %dm" : "%dd %dh %dm", daysFromStart, hoursFromStart, minutesFromStart);
			data.field1 = Application.getContext().getString(R.string.remaining_time);
            data.field2 = Application.getContext().getString(R.string.before_opening);
            data.separator = " ";
		}
		else if(currentDate.after(bound = Festival.getFestivalEndDate())){
            // Festival is over
			Log.v(UpdateArtistOnStageTask.TAG, "Déjà fini depuis " + bound.toString());
            data.main = Application.getContext().getString(R.string.festival_over);
            data.field1 = data.field2 = data.separator = "";
		}
		else{
            // Festival is on but we may fail to retrieve data so just in case...
			int failureCount = 0;
            MusicSet currentSet = null;
			while(failureCount < UpdateArtistOnStageTask.stages.size() && null == data.main){
                UpdateArtistOnStageTask.currentItem = ++UpdateArtistOnStageTask.currentItem % UpdateArtistOnStageTask.stages.size();
                currentSet = MusicSet.fetchCurrent(UpdateArtistOnStageTask.stages.get(UpdateArtistOnStageTask.currentItem).getName());
                if(null != currentSet){
                    data.main = currentSet.getArtist().getName();
                    data.field1 = currentSet.getStage();
                    data.field2 = String.format("fr".equals(Locale.getDefault().getLanguage()) ? "%1$tHh %1$tM - %2$tHh %2$tM" : "%1$tl:%1$tM %1$tp - %2$tl:%2$tM %2$tp", currentSet.getBeginDate(), currentSet.getEndDate());
                    data.separator = "  /  ";
                    failureCount++;
                }
			}
			if(null == currentSet){
				if(BuildConfig.DEBUG) Log.v(UpdateArtistOnStageTask.TAG, "Daily break on all stages at the same time... Very unlikely to happen.");
				data.main = Application.getContext().getString(R.string.daily_break);
				data.field2 = Application.getContext().getString(R.string.all_stages);
                data.field1 = data.separator = "";
			}
		}

		this.updateUI(data);
	}

	private void updateUI(final UpdateArtistOnStageTask.DataHolder data){
		this.hostActivity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				if(null != UpdateArtistOnStageTask.this.stageField && null != UpdateArtistOnStageTask.this.artistField && null != UpdateArtistOnStageTask.this.hourField)
                UpdateArtistOnStageTask.this.stageField.get().setText(data.field1);
                UpdateArtistOnStageTask.this.artistField.get().setText(data.main);
                UpdateArtistOnStageTask.this.hourField.get().setText(data.field2);
                UpdateArtistOnStageTask.this.separatorField.get().setText(data.separator);
			}
		});
	}

    private class DataHolder{
        public String main;
        public String field1;
        public String field2;
        public String separator;
    }
}

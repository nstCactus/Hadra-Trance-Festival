/*
    Copyright 2014 Yohann Bianchi

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
package com.zion.htf.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.data.Artist;
import com.zion.htf.data.MusicSet;
import com.zion.htf.data.SavedAlarm;
import com.zion.htf.data.Stage;
import com.zion.htf.exception.InconsistentDatabaseException;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.exception.SetNotFoundException;
import com.zion.htf.ui.ArtistDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // Get preferences
        Resources res = context.getResources();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        int setId, alarmId;
        try {
            if (0 == (setId = intent.getIntExtra("set_id", 0))) throw new MissingArgumentException("set_id", "int");
            if (0 == (alarmId = intent.getIntExtra("alarm_id", 0))) throw new MissingArgumentException("alarm_id", "int");
        }
        catch (MissingArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Fetch info about the set
        try {
            // VIBRATE will be added if user did NOT disable notification vibration
            // SOUND won't as it is set even if it is to the default value
            int flags = Notification.DEFAULT_LIGHTS;
            MusicSet set = MusicSet.getById(setId);
            Artist artist = set.getArtist();

            SimpleDateFormat dateFormat;
            if("fr".equals(Locale.getDefault().getLanguage())){
                dateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            }
            else{
                dateFormat = new SimpleDateFormat("h:mm aa", Locale.ENGLISH);
            }

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, ArtistDetailsActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);// Do not start a  new activity but reuse the existing one (if any)
            resultIntent.putExtra("set_id", setId);

            // Manipulate the TaskStack in order to get a good back button behaviour. See http://developer.android.com/guide/topics/ui/notifiers/notifications.html
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(ArtistDetailsActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            // Extract a bitmap from a file to use a large icon
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), artist.getPictureResourceId());

            // Builds the notification
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.ic_stat_notify_app_icon)
                    .setLargeIcon(largeIconBitmap)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(artist.getName())
                    .setContentText(String.format(context.getString(R.string.alarm_notification), artist.getName(), set.getStage(), dateFormat.format(set.getBeginDate())));

            // Vibrate settings
            Boolean defaultVibrate = true;
            if (!pref.contains(res.getString(R.string.pref_key_notifications_alarms_vibrate))){
                // Get the system default for the vibrate setting
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                if (null != audioManager){
                    switch (audioManager.getRingerMode()) {
                        case AudioManager.RINGER_MODE_SILENT:
                            defaultVibrate = false;
                            break;
                        case AudioManager.RINGER_MODE_NORMAL:
                        case AudioManager.RINGER_MODE_VIBRATE:
                        default:
                            defaultVibrate = true;
                    }
                }
            }
            Boolean vibrate = pref.getBoolean(res.getString(R.string.pref_key_notifications_alarms_vibrate), defaultVibrate);

            // Ringtone settings
            String ringtone = pref.getString(res.getString(R.string.pref_key_notifications_alarms_ringtone), Settings.System.DEFAULT_NOTIFICATION_URI.toString());

            // Apply notification settings
            if(!vibrate){
                notificationBuilder.setVibrate(new long[]{0l});
            }
            else{
                flags |= Notification.DEFAULT_VIBRATE;
            }

            notificationBuilder.setSound(Uri.parse(ringtone));

            // Get the stage GPS coordinates
            try {
                Stage stage = Stage.getByName(set.getStage());

                // Add the expandable notification buttons
                PendingIntent directionsButtonPendingIntent = PendingIntent.getActivity(context, 1, new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.ENGLISH, "http://maps.google.com/maps?f=d&daddr=%f,%f", stage.getLatitude(), stage.getLongitude()))), Intent.FLAG_ACTIVITY_NEW_TASK);
                notificationBuilder.addAction(R.drawable.ic_menu_directions, context.getString(R.string.action_directions), directionsButtonPendingIntent);
            }
            catch(InconsistentDatabaseException e){
                // Although this is a serious error, its impact on functionality is minimal.
                // Report this through piwik
                if(BuildConfig.DEBUG) e.printStackTrace();
            }

            // Finalize the notification
            notificationBuilder.setDefaults(flags);
            Notification notification = notificationBuilder.build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(set.getStage(), 0, notification);

            SavedAlarm.delete(alarmId);
        }
        catch(SetNotFoundException e){
            throw new RuntimeException(e.getMessage());
            // TODO: Notify that an alarm was planned but some error prevented to display it properly. Open AlarmManagerActivity on click
            // Report this through piwik
        }
    }

}
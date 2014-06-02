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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.data.Artist;
import com.zion.htf.exception.MissingArgumentException;
import com.zion.htf.exception.SetNotFoundException;
import com.zion.htf.ui.ArtistDetailsActivity;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver{
    private SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int setId;
        if(0 == (setId = intent.getIntExtra("set_id", 0))) try {
            throw new MissingArgumentException("set_id", "int");
        } catch (MissingArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Fetch info about the artist
        Artist artist = null;
        try {
            artist = Artist.getBySetId(setId);

            SimpleDateFormat dateFormat = new SimpleDateFormat("fr".equals(Locale.getDefault().getLanguage()) ? "HH:mm" : "h:mm aa");

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(Application.getContext(), ArtistDetailsActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);// Do not start a  new activity but reuse the existing one (if any)
            resultIntent.putExtra("set_id", setId);

            // Manipulate the TaskStack in order to get a good back button behaviour. See http://developer.android.com/guide/topics/ui/notifiers/notifications.html
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(Application.getContext());
            stackBuilder.addParentStack(ArtistDetailsActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            // Extract a bitmap from a file to use a large icon
            Bitmap largeIconBitmap = BitmapFactory.decodeResource(Application.getContext().getResources(), artist.getPictureResourceId());

            // Builds the notification
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(Application.getContext())
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.hadra_logo)
                    .setLargeIcon(largeIconBitmap)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle(artist.getArtistName())
                    .setContentText(String.format(context.getString(R.string.alarm_notification), artist.getArtistName(), artist.getSetStage(), dateFormat.format(artist.getSetBeginDate())));

            // Add the expandable notification buttons
            PendingIntent directionsButtonPendingIntent = PendingIntent.getActivity(Application.getContext(), 1, new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr=45.12465411273364,5.591188743710518")), Intent.FLAG_ACTIVITY_NEW_TASK);//FIXME: Get the coordinate of the stage instead
            notificationBuilder.addAction(R.drawable.ic_action_directions, Application.getContext().getString(R.string.action_directions), directionsButtonPendingIntent);

            NotificationManager notificationManager = (NotificationManager) Application.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            // id (here uid) allows you to update the notification later on.
            Log.v("AlarmReceiver", String.format("Time to notify user %s will be on %s soon, uid = %d", artist.getArtistName(), artist.getSetStage()));
            notificationManager.notify(artist.getSetStage(), 0, notificationBuilder.build());
        }
        catch (SetNotFoundException e) {
            throw new RuntimeException(e.getMessage());
            //TODO: Handle this properly
        }
    }

}
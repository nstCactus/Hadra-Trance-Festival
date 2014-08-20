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

package com.zion.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.zion.htf.R;
import com.zion.htf.ui.ArtistDetailsActivity;

/** Builds the expendable notification */
public class NotificationHelper{
	/**
	 * Notification ID
	 */
	private static final int NOTIFICATION_ID = 524;

	private static final int ACTION_TOGGLE_PLAY_PAUSE   = 1;
	private static final int ACTION_NEXT_TRACK          = 2;
	private static final int ACTION_PREVIOUS_TRACK      = 3;
	private static final int ACTION_CLOSE_NOTIFICATION  = 4;

	/**
	 * NotificationManager
	 */
	private final NotificationManager notificationManager;

	/**
	 * MediaPlayerService
	 */
	private final MediaPlayerService service;

	/**
	 * the notification
	 */
	private Notification notification = null;

	/**
	 * custom notification layout
	 */
	private RemoteViews notificationTemplate;

	/**
	 * API 16+ expanded view
	 */
	private RemoteViews expandedView;

	public NotificationHelper(final MediaPlayerService service){
		this.service = service;
		this.notificationManager = (NotificationManager)service.getSystemService(Service.NOTIFICATION_SERVICE);
	}

	/**
	 * Call this to build the {@link Notification}.
	 */
	public void buildNotification(final String albumName, final String artistName, final String trackName, final Long albumId, final Bitmap albumArt, final boolean isPlaying) {

		// Default notfication layout
		this.notificationTemplate = new RemoteViews(this.service.getPackageName(), R.layout.notification_mediaplayer);

		// Set up the content view
		initCollapsedLayout(trackName, artistName, albumArt);

		// Notification Builder
		this.notification = new NotificationCompat.Builder(this.service)
				.setSmallIcon(R.drawable.ic_stat_notify_app_icon)
				.setContentIntent(this.getPendingIntent())
				.setPriority(Notification.PRIORITY_DEFAULT)
				.setContent(notificationTemplate)
				.setAutoCancel(true)
				.build();

		// Control playback from the notification
		initPlaybackActions(isPlaying);
		if(16 <= Build.VERSION.SDK_INT){
			// Expanded notification style
			this.expandedView = new RemoteViews(service.getPackageName(), R.layout.notification_template_expanded);
			this.notification.bigContentView = this.expandedView;

			// Control playback from the expanded notification
			initExpandedPlaybackActions(isPlaying);

			// Set up the expanded content view
			initExpandedLayout(trackName, albumName, artistName, albumArt);
		}
		this.service.startForeground(NotificationHelper.NOTIFICATION_ID, this.notification);
	}

	/**
	 * Remove notification
	 */
	public void killNotification() {
		this.service.stopForeground(true);
		this.notificationManager.cancel(NotificationHelper.NOTIFICATION_ID);
		this.notification = null;
	}

	/**
	 * Open to the now playing screen
	 */
	private PendingIntent getPendingIntent() {
		Intent intent = new Intent(this.service, ArtistDetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra(ArtistDetailsActivity.EXTRA_ARTIST_ID, service.getCurrentArtistId());
		Log.v("NotificationHelper", String.format("Artist_id = %d", service.getCurrentArtistId()));
		return PendingIntent.getActivity(this.service, 0, intent, 0);
	}

	/**
	 * Lets the buttons in the remote view control playback in the expanded
	 * layout
	 */
	private void initExpandedPlaybackActions(final boolean isPlaying) {
		this.expandedView.setOnClickPendingIntent(R.id.notification_expanded_toggle_play_pause, this.retreivePlaybackActions(NotificationHelper.ACTION_TOGGLE_PLAY_PAUSE));
		this.expandedView.setOnClickPendingIntent(R.id.notification_expanded_next_track, this.retreivePlaybackActions(NotificationHelper.ACTION_NEXT_TRACK));
		this.expandedView.setOnClickPendingIntent(R.id.notification_expanded_previous_track, this.retreivePlaybackActions(NotificationHelper.ACTION_PREVIOUS_TRACK));
		this.expandedView.setOnClickPendingIntent(R.id.notification_expanded_close, this.retreivePlaybackActions(NotificationHelper.ACTION_CLOSE_NOTIFICATION));
		this.expandedView.setImageViewResource(R.id.notification_expanded_toggle_play_pause, isPlaying ? R.drawable.ic_media_pause : R.drawable.ic_media_play);
	}

	/**
	 * Lets the buttons in the remote view control playback in the normal layout
	 */
	private void initPlaybackActions(final boolean isPlaying) {
		this.notificationTemplate.setOnClickPendingIntent(R.id.notification_toggle_play_pause, this.retreivePlaybackActions(NotificationHelper.ACTION_TOGGLE_PLAY_PAUSE));
		this.notificationTemplate.setOnClickPendingIntent(R.id.notification_next_track, this.retreivePlaybackActions(NotificationHelper.ACTION_NEXT_TRACK));
		this.notificationTemplate.setOnClickPendingIntent(R.id.notification_previous_track, this.retreivePlaybackActions(NotificationHelper.ACTION_PREVIOUS_TRACK));
		this.notificationTemplate.setOnClickPendingIntent(R.id.notification_close, this.retreivePlaybackActions(NotificationHelper.ACTION_CLOSE_NOTIFICATION));
		this.notificationTemplate.setImageViewResource(R.id.notification_toggle_play_pause, isPlaying ? R.drawable.ic_media_pause : R.drawable.ic_media_play);
	}

	private void initCollapsedLayout(final String trackName, final String artistName, final Bitmap albumArt){
		this.notificationTemplate.setTextViewText(R.id.notification_base_line_one, trackName);
		this.notificationTemplate.setTextViewText(R.id.notification_base_line_two, artistName);
		this.notificationTemplate.setImageViewBitmap(R.id.notification_base_image, albumArt);
	}

	private void initExpandedLayout(final String trackName, final String albumName, final String artistName, final Bitmap albumArt){
		this.expandedView.setTextViewText(R.id.notification_expanded_base_line_one, trackName);
		this.expandedView.setTextViewText(R.id.notification_expanded_base_line_two, albumName);
		this.expandedView.setTextViewText(R.id.notification_expanded_base_line_three, artistName);
		this.expandedView.setImageViewBitmap(R.id.notification_expanded_base_image, albumArt);
	}

	/**
	 * Returns a {@link android.app.PendingIntent} for the giver {@link com.zion.music.MediaPlayerService} {@code action}
	 * @param action the action to perform in the {@code service}
	 * @return the {@code PendingIntent} associated to the {@code action}
	 */
	private PendingIntent retreivePlaybackActions(int action){
		Intent intent = new Intent(this.service, MediaPlayerService.class);
		PendingIntent pendingIntent;
		switch (action) {
			case NotificationHelper.ACTION_TOGGLE_PLAY_PAUSE:
				intent.setAction(MediaPlayerService.ACTION_TOGGLE_PLAY_PAUSE);
				break;
			case NotificationHelper.ACTION_NEXT_TRACK:
				intent.setAction(MediaPlayerService.ACTION_NEXT_TRACK);
				break;
			case NotificationHelper.ACTION_PREVIOUS_TRACK:
				intent.setAction(MediaPlayerService.ACTION_PREVIOUS_TRACK);
				break;
			case NotificationHelper.ACTION_CLOSE_NOTIFICATION:
				intent.setAction(MediaPlayerService.ACTION_STOP);
				break;
			default:
				return null;
		}
		pendingIntent = PendingIntent.getService(this.service, action, intent, 0);
		return pendingIntent;
	}
}

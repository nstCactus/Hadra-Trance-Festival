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

import android.content.Context;
import android.content.Intent;

public class ServiceUtils{
	private static int activitiesInForeground = 0;

	public static void notifyForegroundStateChanged(Context context, boolean inForeground){
		int old = ServiceUtils.activitiesInForeground;
		if (inForeground) {
			ServiceUtils.activitiesInForeground++;
		} else {
			ServiceUtils.activitiesInForeground--;
		}

		if (0 == old || 0 == ServiceUtils.activitiesInForeground) {
			final Intent intent = new Intent(context, MediaPlayerService.class);
			intent.setAction(MediaPlayerService.ACTION_FOREGROUND_STATE_CHANGED);
			intent.putExtra(MediaPlayerService.EXTRA_NOW_IN_FOREGROUND, 0 != ServiceUtils.activitiesInForeground);
			context.startService(intent);
		}
	}
}

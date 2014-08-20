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

package com.zion.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class StringUtils{
    public static String hashMD5(String string) {
        StringBuilder result = new StringBuilder();
        try{
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(string.getBytes("UTF8"));

            byte[] s = m.digest();

            for(byte value : s){
                result.append(Integer.toHexString((0x000000ff & value) | 0xffffff00).substring(6));
            }
        }
        catch(NoSuchAlgorithmException e){
            throw new IllegalStateException("MD5 algorithm is unsupported by this device's android implementation.", e);
        }
        catch(UnsupportedEncodingException e){
            throw new IllegalStateException("UTF-8 encoding is unsupported by this device's android implementation.", e);
        }
        return result.toString();
    }

	/**
	 * Format a duration in seconds
	 * @param duration the duration in seconds
	 * @return the duration formatted as mm:ss
	 */
	public static String formatDuration(int duration){
		int minutes = duration / 60;
		int seconds = duration % 60;
		return String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);
	}
}

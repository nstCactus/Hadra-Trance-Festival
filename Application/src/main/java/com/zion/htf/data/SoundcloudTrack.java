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

package com.zion.htf.data;

import android.os.Parcel;
import android.os.Parcelable;

public class SoundcloudTrack implements Parcelable{
	private final int    id;
	private       int    duration;
	private final String streamUrl;
	private final String title;
	private       String artist;

	public static final Parcelable.Creator<SoundcloudTrack> CREATOR = new Parcelable.Creator<SoundcloudTrack>(){
		public SoundcloudTrack createFromParcel(Parcel in){
			return new SoundcloudTrack(in);
		}

		public SoundcloudTrack[] newArray(int size){
			return new SoundcloudTrack[size];
		}
	};

	public SoundcloudTrack(int id, String title, String streamUrl){
		this.id = id;
		this.title = title;
		this.streamUrl = streamUrl;
	}

	public SoundcloudTrack(Parcel in){
		String[]    strings = new String[3];
		int[]       ints    = new int[2];

		in.readStringArray(strings);
		this.streamUrl = strings[0];
		this.title = strings[1];
		this.artist = strings[2];

		in.readIntArray(ints);
		this.id = ints[0];
		this.duration = ints[1];
	}

	public String getStreamUrl(){
		return this.streamUrl;
	}

	public int getId(){
		return this.id;
	}

	public int getDuration(){
		return this.duration;
	}

	public void setDuration(int duration){
		this.duration = duration;
	}

	public String getTitle(){
		return this.title;
	}

	public String getArtist(){
		return this.artist;
	}

	public void setArtist(String artist){
		this.artist = artist;
	}

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeStringArray(new String[]{this.streamUrl, this.title, this.artist});
		dest.writeIntArray(new int[]{this.id, this.duration});
	}
}

/*
    Copyright 2013 Yohann Bianchi

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

package com.zion.htf.data;

import java.util.Date;

public class Set extends Item{
	protected String name;
	protected int type = TYPE_ITEM;

	/* Sets table fields */
	protected int    id;
	protected String artist_name;
	protected String set_type;
	protected Date   begin_date;
	protected Date   end_date;
	protected String genre;
	protected String stage;
	protected String picture;
	protected int    artist_id;

	public Set(String name){
		super(name, TYPE_ITEM);
	}

	public Set(String name, int type){
		super(name, type);
	}

	/* Getters & Setters */
	public String getSetType(){
		return this.set_type;
	}

	public Set setSetType(String setType){
		this.set_type = setType;
		return this;
	}

	public Date getBeginDate(){
		return this.begin_date;
	}

	public Set setBeginDate(Date beginDate){
		this.begin_date = beginDate;
		return this;
	}

	public Date getEndDate(){
		return this.end_date;
	}

	public Set setEndDate(Date endDate){
		this.end_date = endDate;
		return this;
	}

	public String getGenre(){
		return this.genre;
	}

	public Set setGenre(String genre){
		this.genre = genre;
		return this;
	}

	public String getStage(){
		return this.stage;
	}

	public Set setStage(String stage){
		this.stage = stage;
		return this;
	}

	public String getArtistName(){
		return this.artist_name;
	}

	public Set setArtistName(String artistName){
		this.artist_name = artistName;
		return this;
	}

	public int getId(){
		return this.id;
	}

	public Set setId(int id){
		this.id = id;
		return this;
	}

	public String getPhotoResourceName(){
		return this.picture;
	}

	public Set setPicture(String picture){
		this.picture = picture;
		return this;
	}

	public int getArtistId(){
		return this.artist_id;
	}

	public Set setArtistId(int id){
		this.artist_id = id;
		return this;
	}
}

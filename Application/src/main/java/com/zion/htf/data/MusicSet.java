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

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.zion.htf.Application;
import com.zion.htf.exception.SetNotFoundException;
import com.zion.util.DateUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The {@code MusicSet} class holds data related to an {@link com.zion.htf.data.Artist}'s set as well as methods to retrieve this data from the {@code sets} database table.
 * As it extends {@link com.zion.htf.data.Item}, it can be used as an item in a {@link com.hb.views.PinnedSectionListView} using a {@link com.zion.htf.adapter.LineUpListAdapter} as its source.
 * FIXME: Delegate the Artist object creation to {@link com.zion.htf.data.Artist} to ensure all values are set correctly
 */
public class MusicSet extends Item{
    private static final SQLiteOpenHelper dbOpenHelper = Application.getDbHelper();
    private static final String QUERY = "SELECT sets.*, artists.* FROM sets JOIN artists ON sets.artist = artists.id ";
    protected int type = Item.TYPE_ITEM;

	/* Sets table fields */
	protected int    id;
	protected long   begin_date;
	protected long   end_date;
    protected String setType;
	protected Artist artist;

    protected String stage;

    /* Column indexes for convenience */
    protected static final int COLUMN_ID                    = 0;
    protected static final int COLUMN_BEGIN_DATE            = 1;
    protected static final int COLUMN_END_DATE              = 2;
    protected static final int COLUMN_TYPE                  = 3;
    protected static final int COLUMN_ARTIST_ID             = 4;
    protected static final int COLUMN_STAGE                 = 5;
    //protected static final int COLUMN_ARTIST_ID  = 6;// Duplicate
    protected static final int COLUMN_ARTIST_NAME           = 7;
    protected static final int COLUMN_ARTIST_GENRE          = 8;
    protected static final int COLUMN_ARTIST_ORIGIN         = 9;
    protected static final int COLUMN_ARTIST_PICTURE_NAME   = 10;
    protected static final int COLUMN_ARTIST_COVER_NAME     = 11;
    protected static final int COLUMN_ARTIST_WEBSITE        = 12;
    protected static final int COLUMN_ARTIST_FACEBOOK       = 13;
    protected static final int COLUMN_ARTIST_SOUNDCLOUD     = 14;
    protected static final int COLUMN_ARTIST_LABEL          = 15;
    protected static final int COLUMN_ARTIST_BIO_ID         = 16;
    protected static final int COLUMN_ARTIST_FAVORITE       = 17;

    public MusicSet(String name){
		super(name, Item.TYPE_ITEM);
	}

    /*********************/
    /* Getters & Setters */
    /*********************/
    public int getId() {
        return this.id;
    }

    public MusicSet setId(int id) {
        this.id = id;
        return this;
    }

    public Date getBeginDate(){
        return new Date(this.begin_date);
    }

    /**
     * Get the {@code MusicSet}'s begin date as a timestamp in milliseconds since UNIX epoch
     * @return The {@code MusicSet}'s timestamp
     */
    public long getBeginDateAsTimestamp() {
        return this.begin_date;
    }

    public MusicSet setBeginDate(long timestamp){
        this.begin_date = timestamp;
        return this;
    }

    public Date getEndDate() {
        return new Date(this.end_date);
    }

    public MusicSet setEndDate(long timestamp){
        this.end_date = timestamp;
        return this;
    }

    public String getSetType() {
        return this.setType;
    }

    public MusicSet setSetType(String setType) {
        this.setType = setType;
        return this;
    }

    public Artist getArtist(){
        return this.artist;
    }

    public MusicSet setArtist(Artist artist){
        this.artist = artist;
        return this;
    }

    public String getStage() {
        return this.stage;
    }

    public MusicSet setStage(String stage) {
        this.stage = stage;
        return this;
    }

    /******************/
    /* Static methods */
    /******************/
    public static MusicSet getById(int id) throws SetNotFoundException {
        Cursor cursor = MusicSet.dbOpenHelper.getReadableDatabase().rawQuery(String.format("%s WHERE sets.id = %d ", MusicSet.QUERY, id), null);
        if(null == cursor || !cursor.moveToNext()) throw new SetNotFoundException(id);
        MusicSet musicSet = MusicSet.newInstance(cursor);

        if(!cursor.isClosed()) cursor.close();
        MusicSet.dbOpenHelper.close();
        return musicSet;
    }

    public static List<Item> getListByStage(String stage, String order, Boolean addDateSeparators){
        Cursor cursor = MusicSet.dbOpenHelper.getReadableDatabase().rawQuery(String.format("%s WHERE stage = ? %s", MusicSet.QUERY, order), new String[]{ stage });
        List<Item> list = MusicSet.getList(cursor, addDateSeparators);
                if(!cursor.isClosed()) cursor.close();
        MusicSet.dbOpenHelper.close();

        return list;
    }

    public static List<Item> getListByStage(String stage, Boolean addDateSeparators){
        return MusicSet.getListByStage(stage, "ORDER BY begin_date ASC", addDateSeparators);
    }
    public static List<Item> getList(){
        return MusicSet.getList("ORDER BY begin_date ASC");
    }

    public static List<Item> getList(String order){
        if(!order.matches("^\\s*ORDER\\s+BY\\b.*")) order = "ORDER BY " + order;
        return MusicSet.getList(String.format("%s %s", MusicSet.QUERY, order), false);
    }

    public static List<Item> getList(String query, Boolean addDateSeparators){
        return MusicSet.getList(MusicSet.dbOpenHelper.getReadableDatabase().rawQuery(query, null), addDateSeparators);
    }

    protected static List<Item> getList(Cursor cursor, Boolean addDateSeparators){
        List<Item> sets = new ArrayList<Item>();

        Date previousDate = new Date(0);
        DateFormat dateFormat = "fr".equals(Locale.getDefault().getLanguage()) ? DateFormat.getDateInstance(DateFormat.FULL, Locale.FRENCH) : DateFormat.getDateInstance(DateFormat.FULL);
        while(cursor.moveToNext()){
            MusicSet musicSet = MusicSet.newInstance(cursor);

            // Add date separator items if required
            if(addDateSeparators){
                Date beginDate = musicSet.getBeginDate();

                if (!DateUtils.areSameDay(previousDate, beginDate)) {
                    sets.add(new Item(dateFormat.format(beginDate), Item.TYPE_SECTION));
                }
                previousDate = beginDate;
            }

            sets.add(musicSet);
        }

        if(!cursor.isClosed()) cursor.close();
        MusicSet.dbOpenHelper.close();

        return sets;
    }

    /**
     * Get the {@code MusicSet} currently playing on the given {@code stage}
     * @param stage The name of the stage
     * @return A new {@code MusicSet} instance corresponding to the set currently played
     */
    public static MusicSet fetchCurrent(String stage){
        Cursor cursor = MusicSet.dbOpenHelper.getReadableDatabase().rawQuery(String.format(Locale.ENGLISH, "%s WHERE stage = ? AND %d BETWEEN begin_date AND end_date;", MusicSet.QUERY, new Date().getTime() / 1000), new String[]{stage});
        MusicSet musicSet = null;
        if(null != cursor) {
            if(cursor.moveToFirst()) musicSet = MusicSet.newInstance(cursor);
            cursor.close();
        }
        MusicSet.dbOpenHelper.close();
        return musicSet;
    }

    /**
     * Get a new instance of {@code MusicSet} built from data extracted from {@code cursor}
     * @param cursor A {@link android.database.Cursor} instance representing a query similar to {@link MusicSet.QUERY}
     * @return A new instance of {@code MusicSet} or null if the {@link android.database.Cursor} is empty
     */
    @SuppressWarnings("JavadocReference")
    public static MusicSet newInstance(Cursor cursor){
        MusicSet musicSet;
        musicSet = new MusicSet(cursor.getString(MusicSet.COLUMN_ARTIST_NAME));

        Artist artist = new Artist(cursor.getString(MusicSet.COLUMN_ARTIST_NAME))
                .setId(cursor.getInt(MusicSet.COLUMN_ARTIST_ID))
                .setGenre(cursor.getString(MusicSet.COLUMN_ARTIST_GENRE), "")
                .setOrigin(cursor.getString(MusicSet.COLUMN_ARTIST_ORIGIN), "")
                .setPicture(cursor.getString(MusicSet.COLUMN_ARTIST_PICTURE_NAME), "")
                .setCoverName(cursor.getString(MusicSet.COLUMN_ARTIST_COVER_NAME))
                .setWebsite(cursor.getString(MusicSet.COLUMN_ARTIST_WEBSITE), "")
                .setFacebook(cursor.getString(MusicSet.COLUMN_ARTIST_FACEBOOK), "")
                .setSoundcloud(cursor.getString(MusicSet.COLUMN_ARTIST_SOUNDCLOUD), "")
                .setLabel(cursor.getString(MusicSet.COLUMN_ARTIST_LABEL), "")
                .setBioId(cursor.getInt(MusicSet.COLUMN_ARTIST_BIO_ID))
                .setFavorite(cursor.getInt(MusicSet.COLUMN_ARTIST_FAVORITE));

        musicSet.setSetType(cursor.getString(MusicSet.COLUMN_TYPE))
                .setStage(cursor.getString(MusicSet.COLUMN_STAGE))
                .setBeginDate(cursor.getLong(MusicSet.COLUMN_BEGIN_DATE) * 1000)
                .setEndDate(cursor.getLong(MusicSet.COLUMN_END_DATE) * 1000)
                .setId(cursor.getInt(MusicSet.COLUMN_ID))
                .setArtist(artist);

        return musicSet;
    }
}

package com.zion.htf.data;

import android.database.Cursor;
import android.util.Log;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.exception.ArtistNotFoundException;
import com.zion.htf.exception.InconsistentDatabaseException;
import com.zion.htf.exception.SetNotFoundException;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

public class Artist {
    private static final String TAG = "ArtistDetailsActivity";
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_GENRE = 2;
    private static final int COLUMN_ORIGIN = 3;
    private static final int COLUMN_PICTURE = 4;
    private static final int COLUMN_COVER = 5;
    private static final int COLUMN_WEBSITE = 6;
    private static final int COLUMN_FACEBOOK = 7;
    private static final int COLUMN_SOUNDCLOUD = 8;
    private static final int COLUMN_LABEL = 9;
    private static final int COLUMN_BIO_ID = 10;
    private static final int COLUMN_SET_ID = 11;
    private static final int COLUMN_SET_BEGIN = 12;
    private static final int COLUMN_SET_END = 13;
    private static final int COLUMN_SET_TYPE = 14;
    private static final int COLUMN_SET_STAGE = 15;
    private static final String QUERY = "SELECT artists.*, sets.id, sets.begin_date, sets.end_date, sets.type, sets.stage " +
            "FROM artists " +
            "INNER JOIN sets ON sets.artist = artists.id " +
            "WHERE ";

    /* Sets table fields */
    protected int id;
    private String name;
    private String artist_name;
    private String genre;
    private String origin;
    private String picture;
    private String cover;
    private String website;
    private String facebook;
    private String soundcloud;
    private String label;
    private int bioId;
    private int setId;
    private String setType;
    private long setBeginDate;
    private long setEndDate;
    private String setStage;
    private int pictureResourceId;
    private String bioEn;
    private String bioFr;

    public Artist(String name) {
        if (null == name || 0 == name.length())
            throw new IllegalArgumentException("The value of the name field must not null and not empty");
        this.artist_name = name;
    }

    /**
     * Get an artist using its id
     *
     * @param artistId the identifier of the artist
     * @return an {@link Artist} object corresponding to the provided artistId
     * @throws ArtistNotFoundException if no artist matches the given {@code artistId}
     */
    public static Artist getById(int artistId) throws ArtistNotFoundException {
        try {
            return Artist.newInstance(String.format("artists.id = %d", artistId));
        } catch (ArtistNotFoundException e) {
            throw new ArtistNotFoundException(artistId);
        }
    }

    /**
     * Get an artist using a setId
     *
     * @param setId the identifier of the set
     * @return an {@link Artist} object corresponding to the provided {@code setId}
     * @throws SetNotFoundException if no set matches the given {@code setId}
     */
    public static Artist getBySetId(int setId) throws SetNotFoundException {
        try {
            return Artist.newInstance(String.format("sets.id = %d", setId));
        } catch (ArtistNotFoundException e) {
            throw new SetNotFoundException(setId);
        }
    }

    /**
     * Return a new {@code Artist} instance
     *
     * @param where a string representing the {@code WHERE} clause of the query used to get the artist
     * @return a new Artist instance with all fields correctly bound
     */
    private static Artist newInstance(String where) throws ArtistNotFoundException {
        SQLiteDatabaseHelper dbHelper = Application.getDbHelper();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(Artist.QUERY + where, null);

        if (!cursor.moveToNext()) throw new ArtistNotFoundException("No artist found");

        Artist artist = new Artist(cursor.getString(Artist.COLUMN_NAME));
        artist.setId(cursor.getInt(Artist.COLUMN_ID))
                .setGenre(cursor.getString(Artist.COLUMN_GENRE), "")
                .setOrigin(cursor.getString(Artist.COLUMN_ORIGIN), "")
                .setPicture(cursor.getString(Artist.COLUMN_PICTURE), "")
                .setWebsite(cursor.getString(Artist.COLUMN_WEBSITE), "")
                .setFacebook(cursor.getString(Artist.COLUMN_FACEBOOK), "")
                .setSoundcloud(cursor.getString(Artist.COLUMN_SOUNDCLOUD), "")
                .setLabel(cursor.getString(Artist.COLUMN_LABEL), "")
                .setSetId(cursor.getInt(Artist.COLUMN_SET_ID))
                .setSetBeginDate(cursor.getLong(Artist.COLUMN_SET_BEGIN) * 1000)
                .setSetEndDate(cursor.getLong(Artist.COLUMN_SET_END) * 1000)
                .setSetType(cursor.getString(Artist.COLUMN_SET_TYPE))
                .setSetStage(cursor.getString(Artist.COLUMN_SET_STAGE))
                .setBioId(cursor.getInt(Artist.COLUMN_BIO_ID));

        if (!cursor.isClosed()) cursor.close();
        dbHelper.close();

        return artist;
    }

    public String getArtistName() {
        return this.artist_name;
    }

    public String getSetType() {
        return this.setType;
    }

    public Artist setSetType(String setType) {
        if (null == setType || 0 == setType.length())
            throw new IllegalArgumentException("The value of the type field of the sets table must not null and not empty");
        this.setType = setType;
        return this;
    }

    public String getGenre() {
        return this.genre;
    }

    public Artist setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getPictureResName() {
        return this.picture;
    }

    public Artist setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public Artist setId(int id) {
        if (0 == id) throw new IllegalArgumentException("The value of the id field cannont be 0");
        this.id = id;
        return this;
    }

    private Artist setWebsite(String value, String defaultValue) {
        return this.setWebsite(null == value ? defaultValue : value);
    }

    private Artist setFacebook(String value, String defaultValue) {
        return this.setFacebook(null == value ? defaultValue : value);
    }

    private Artist setSoundcloud(String value, String defaultValue) {
        return this.setSoundcloud(null == value ? defaultValue : value);
    }

    private Artist setLabel(String value, String defaultValue) {
        return this.setLabel(null == value ? defaultValue : value);
    }

    private Artist setPicture(String value, String defaultValue) {
        return this.setPicture(null == value ? defaultValue : value);
    }

    private Artist setGenre(String value, String defaultValue) {
        return this.setGenre(null == value ? defaultValue : value);
    }

    public Artist setOrigin(String value, String defaultValue) {
        return this.setOrigin(null == value ? defaultValue : value);
    }

    public Artist setSetEndDate(long setEndDate) {
        if (0l == setEndDate)
            throw new IllegalArgumentException("The value of the end_date field in the sets table cannont be 0");
        this.setEndDate = setEndDate;
        return this;
    }

    public String getSetStage() {
        return this.setStage;
    }

    public Artist setSetStage(String setStage) {
        if (null == setStage || 0 == setStage.length())
            throw new IllegalArgumentException("The value of the stage field of the sets table must not null and not empty");
        this.setStage = setStage;
        return this;
    }

    public long getSetBeginDate() {
        return this.setBeginDate;
    }

    public Artist setSetBeginDate(long setBeginDate) {
        if (0l == setBeginDate)
            throw new IllegalArgumentException("The value of the begin_date field in the sets table cannont be 0");
        this.setBeginDate = setBeginDate;
        return this;
    }

    public int getPictureResourceId() {
        if(0 == this.pictureResourceId){
            String picResName = this.getPictureResName();
            int resId;

            // Sanitize picture resource name
            picResName = picResName.toLowerCase()
                    .replaceAll("\\.(?:jpe?g|png|gif)$", "")// Remove the extension if present in DB
                    .replace(".", "_");

            if (0 == (resId = Application.getContext().getResources().getIdentifier(picResName, "drawable", "com.zion.htf"))) {
                resId = R.drawable.no_image;
                if (BuildConfig.DEBUG) Log.w(Artist.TAG, "Not found: " + picResName);
            }
            this.setPictureResourceId(resId);
        }
        return this.pictureResourceId;
    }

    public Artist setPictureResourceId(int pictureResourceId) {
        this.pictureResourceId = pictureResourceId;
        return this;
    }

    public String getLabel() {
        return this.label;
    }

    public Artist setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Artist setOrigin(String origin) {
        this.origin = origin;

        return this;
    }

    public String getWebsite() {
        return this.website;
    }

    public Artist setWebsite(String website) {
        this.website = website;

        return this;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public Artist setFacebook(String facebook) {
        this.facebook = facebook;
        return this;
    }

    public String getSoundcloud() {
        return this.soundcloud;
    }

    public Artist setSoundcloud(String soundcloud) {
        this.soundcloud = soundcloud;
        return this;
    }

    /**
     * Fetches (if needed) and return the artist bio, if possible corresponding to the provided {@code langCode}
     *
     * @return the artist's biography if possible in the language specified
     * @throws InconsistentDatabaseException if more than 2 entries are found in {@code bios} table for the same artist
     */
    public String getBio(String langCode) throws InconsistentDatabaseException {
        if(null == this.bioFr || null == this.bioEn){
            SQLiteDatabaseHelper dbHelper = Application.getDbHelper();
            Cursor cursor = dbHelper.getReadableDatabase().rawQuery(String.format("SELECT text, lang_code FROM bios WHERE id = %d", this.bioId), null);
            String dbLangCode, bio;

            if(2 < cursor.getCount()) throw new InconsistentDatabaseException("An artist should have at most two bios entries");
            while(cursor.moveToNext()){
                dbLangCode = cursor.getString(1);
                bio = cursor.isNull(0) ? "" : cursor.getString(0);
                if("fr".equals(dbLangCode)){
                    this.bioFr = bio;
                }
                else if("en".equals(dbLangCode)){
                    this.bioEn = bio;
                }
            }

            if (!cursor.isClosed()) cursor.close();
            dbHelper.close();
        }
        return "fr".equals(langCode) ? this.bioFr : this.bioEn;
    }

    public int getSetId() {
        return this.setId;
    }

    public Artist setSetId(int setId) {
        if (0 == setId) throw new IllegalArgumentException("The value of the id field in the sets table cannont be 0.");
        this.setId = setId;
        return this;
    }

    public Artist setBioId(int bioId){
        if(0 == bioId) throw new IllegalArgumentException("The value of the bio_id filed in the artists table cannot be 0.");
        this.bioId = bioId;
        return this;
    }

    public int getBioId(){
        return this.bioId;
    }
}
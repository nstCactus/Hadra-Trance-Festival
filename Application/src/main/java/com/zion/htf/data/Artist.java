package com.zion.htf.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.zion.htf.Application;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.exception.ArtistNotFoundException;
import com.zion.htf.exception.InconsistentDatabaseException;
import com.zion.htf.exception.SetNotFoundException;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The {@code Artist} class holds data related to an artist as well as methods to retrieve this data from the {@code artists} database table.
 */
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
    private static final int COLUMN_FAVORITE = 11;
    private static final String QUERY = "SELECT artists.* FROM artists INNER JOIN sets ON sets.artist = artists.id ";

    /* Sets table fields */
    protected int id;
    private final String name;
    private String genre;
    private String origin;
    private String picture;
    private String cover;
    private String website;
    private String facebook;
    private String soundcloud;
    private String label;
    private int bioId;
    private boolean isFavorite;
    private int pictureResourceId;
    private String bioEn;
    private String bioFr;
    private static final SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();
    private boolean biosFetched = false;

    public Artist(String name) {
        if (null == name || 0 == name.length())
            throw new IllegalArgumentException("The value of the name field must not null and not empty");
        this.name = name;
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
            return Artist.newInstance(String.format(Locale.ENGLISH, "artists.id = %d", artistId));
        } catch (ArtistNotFoundException e) {
            // Enrich the exception including the artist id
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
            return Artist.newInstance(String.format(Locale.ENGLISH, "sets.id = %d", setId));
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
    public static Artist newInstance(String where) throws ArtistNotFoundException{
        if(!where.matches("^\\s*WHERE\\b.*")) where = " WHERE " + where;

        Cursor cursor = Artist.dbOpenHelper.getReadableDatabase().rawQuery(Artist.QUERY + where, null);

        if (!cursor.moveToNext()) throw new ArtistNotFoundException("No artist found");
        Artist artist = Artist.newInstance(cursor);

        if (!cursor.isClosed()) cursor.close();
        Artist.dbOpenHelper.close();

        return artist;
    }

    /**
     * Return a new {@code Artist} instance
     *
     * @param cursor A {@link android.database.Cursor} instance representing a query similar to {@link Artist.QUERY}
     * @return a new Artist instance with all fields correctly bound
     */
    @SuppressWarnings("JavadocReference")
    public static Artist newInstance(Cursor cursor){
        Artist artist = new Artist(cursor.getString(Artist.COLUMN_NAME));
        artist.setId(cursor.getInt(Artist.COLUMN_ID))
                .setGenre(cursor.getString(Artist.COLUMN_GENRE), "")
                .setOrigin(cursor.getString(Artist.COLUMN_ORIGIN), "")
                .setPicture(cursor.getString(Artist.COLUMN_PICTURE), "")
                .setWebsite(cursor.getString(Artist.COLUMN_WEBSITE), "")
                .setFacebook(cursor.getString(Artist.COLUMN_FACEBOOK), "")
                .setSoundcloud(cursor.getString(Artist.COLUMN_SOUNDCLOUD), "")
                .setLabel(cursor.getString(Artist.COLUMN_LABEL), "")
                .setBioId(cursor.getInt(Artist.COLUMN_BIO_ID))
                .setFavorite(cursor.getInt(Artist.COLUMN_FAVORITE));

        return artist;
    }

    public String getName() {
        return this.name;
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

    public Artist setWebsite(String value, String defaultValue) {
        return this.setWebsite(null == value ? defaultValue : value);
    }

    public Artist setFacebook(String value, String defaultValue) {
        return this.setFacebook(null == value ? defaultValue : value);
    }

    public Artist setSoundcloud(String value, String defaultValue) {
        return this.setSoundcloud(null == value ? defaultValue : value);
    }

    public Artist setLabel(String value, String defaultValue) {
        return this.setLabel(null == value ? defaultValue : value);
    }

    public Artist setPicture(String value, String defaultValue) {
        return this.setPicture(null == value ? defaultValue : value);
    }

    public Artist setGenre(String value, String defaultValue) {
        return this.setGenre(null == value ? defaultValue : value);
    }

    public Artist setOrigin(String value, String defaultValue) {
        return this.setOrigin(null == value ? defaultValue : value);
    }


    public int getPictureResourceId() {
        if(0 == this.pictureResourceId){
            String picResName = this.getPictureResName();
            int resId;

            // Sanitize picture resource name
            picResName = picResName.toLowerCase(Locale.ENGLISH)
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
     * Return the artist bio, if possible corresponding to the provided {@code langCode}
     *
     * @return the artist's biography if possible in the language specified
     * @throws InconsistentDatabaseException if more than 2 entries are found in {@code bios} table for the same artist
     */
    public String getBio(String langCode) throws InconsistentDatabaseException {
        if(!this.biosFetched){
            this.fetchBios();
        }

        return "fr".equals(langCode) ? this.bioFr : this.bioEn;
    }

    /**
     * Fetch the biography in french and english and store them in {@code this.bioFr} and {@code this.bioEn}
     * @throws InconsistentDatabaseException if more than 2 entries are found in {@code bios} table for the same artist
     */
    private void fetchBios() throws InconsistentDatabaseException {
        SQLiteDatabaseHelper dbHelper = Application.getDbHelper();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(String.format("SELECT text, lang_code FROM bios WHERE id = %d", this.bioId), null);
        String dbLangCode, bio;

        if(2 < cursor.getCount()) throw new InconsistentDatabaseException("An artist should have at most two bios entries");
        while(cursor.moveToNext()){
            dbLangCode = cursor.getString(1);
            bio = cursor.getString(0);
            if("fr".equals(dbLangCode)){
                this.bioFr = bio;
            }
            else if("en".equals(dbLangCode)){
                this.bioEn = bio;
            }
        }

        if (!cursor.isClosed()) cursor.close();
        dbHelper.close();

        // Fallback to the other lang if empty
        if(null == this.bioFr || 0 == this.bioFr.length()) this.bioFr = this.bioEn;
        if(null == this.bioEn || 0 == this.bioEn.length()) this.bioEn = this.bioFr;

        this.biosFetched = true;
    }

    public Artist setBioId(int bioId){
        if(0 == bioId) throw new IllegalArgumentException("The value of the bio_id filed in the artists table cannot be 0.");
        this.bioId = bioId;
        return this;
    }

    public int getBioId(){
        return this.bioId;
    }

    public Artist setCoverName(String cover){
        this.cover = cover;

        return this;
    }

    public static List<Artist> getFavoriteArtistsList(){
        List<Artist> artists = new ArrayList<Artist>();
        SQLiteDatabaseHelper dbHelper = Application.getDbHelper();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(String.format("%s WHERE artists.favorite = 1", Artist.QUERY), null);//FIXME: Change the WHERE clause to select only artists marked as favorite

        while(cursor.moveToNext()){
            artists.add(Artist.newInstance(cursor));
        }

        if (!cursor.isClosed()) cursor.close();
        dbHelper.close();

        return artists;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    public Artist setFavorite(boolean favorite){
        this.isFavorite = favorite;
        return this;
    }

    public Artist setFavorite(int favorite){
        return this.setFavorite(0 != favorite);
    }

    /**
     * Toggle the artist's favorite status in database
     * @return boolean Whether or not the query was successful
     */
    public boolean toggleFavorite(){
        boolean ret = false;
        boolean newValue = !this.isFavorite;

        ContentValues values = new ContentValues();
        values.put("favorite", newValue);
        SQLiteDatabase db = Artist.dbOpenHelper.getWritableDatabase();

        db.beginTransaction();

        int affectedRows = db.update("artists", values, "id = " + this.id, null);

        if(1 == affectedRows){
            db.setTransactionSuccessful();
            this.setFavorite(newValue);
            ret = true;
        }
        else if(BuildConfig.DEBUG){
            Log.e(Artist.TAG, String.format(Locale.ENGLISH, "Failed to toggle favorite status of artist %s (%d). Transaction rolled back as it would have updated %d rows instead of 1.", this.name, this.id, affectedRows));
        }

        db.endTransaction();
        db.close();
        Artist.dbOpenHelper.close();

        return ret;
    }

    /**
     * Remove the favorite status from the artist whose database identifier matches the given {@code IN} clause
     * @param inClause The {@code IN} clause (without the {@code IN} keyword itself)
     * @return int the number of updated artists
     */
    public static int unsetFavorite(String inClause){
        SQLiteStatement statement = Artist.dbOpenHelper.getWritableDatabase().compileStatement(String.format(Locale.ENGLISH, "UPDATE artists SET favorite = 0 WHERE id IN(%s);", inClause));
        return statement.executeUpdateDelete();
    }
}

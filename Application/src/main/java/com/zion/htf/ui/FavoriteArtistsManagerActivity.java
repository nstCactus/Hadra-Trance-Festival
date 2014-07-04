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

package com.zion.htf.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.zion.content.SQLiteCursorLoader;
import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.adapter.FavoriteArtistsListAdapter;

public class FavoriteArtistsManagerActivity extends AbstractActionModeCompatListActivity{
    private static final String QUERY = "SELECT id AS _id, name, picture_name FROM artists WHERE favorite = 1 ORDER BY name ASC;";
    public static final int COLUMN_ID      = 0;
    public static final int COLUMN_NAME    = 1;
    public static final int COLUMN_PICTURE = 2;
    private static final int ARTIST_DETAILS_REQUEST_CODE = 1;

    protected FavoriteArtistsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        this.getSupportLoaderManager().initLoader(AbstractActionModeCompatListActivity.LISTVIEW_LOADER_ID, null, this).forceLoad();
        super.adapter = new FavoriteArtistsListAdapter(this, null, false);

        super.setLayoutId(R.layout.activity_favorite_artists_manager);
        super.setListViewId(R.id.favorite_artists_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        super.adapter.getItem(position);
        intent.putExtra("artist_id", ((Cursor)super.adapter.getItem(position)).getInt(FavoriteArtistsManagerActivity.COLUMN_ID));
        this.startActivityForResult(intent, FavoriteArtistsManagerActivity.ARTIST_DETAILS_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.favorite_artists_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean ret;

        switch(id){
            case R.id.action_settings:
                ret = true;
                break;

            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if(FavoriteArtistsManagerActivity.ARTIST_DETAILS_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode){
            this.getSupportLoaderManager().restartLoader(AbstractActionModeCompatListActivity.LISTVIEW_LOADER_ID, null, this).forceLoad();
        }
    }

        @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        Log.v("FavoriteArtistsManagerActivity", "Creating loader");
        Loader<Cursor> loader = new SQLiteCursorLoader(this, Application.getDbHelper().getReadableDatabase(), FavoriteArtistsManagerActivity.QUERY, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        Log.v("FavoriteArtistsManagerActivity", "Done loading data");
        super.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        super.adapter.changeCursor(null);
    }
}

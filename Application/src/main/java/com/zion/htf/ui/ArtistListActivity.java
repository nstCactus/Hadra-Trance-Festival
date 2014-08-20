/*
 *
 *     Copyright 2013-2015 Yohann Bianchi
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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.support.v4.widget.CursorAdapter;
import android.widget.ListView;

import com.zion.content.SQLiteCursorLoader;
import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.adapter.ArtistListAdapter;
import com.zion.htf.data.MusicSet;

public class ArtistListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final    int             LISTVIEW_LOADER_ID = 5002;
    private                 ListView        listView;
    private                 CursorAdapter   adapter;

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_artist_list);

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

        this.listView = (ListView)this.findViewById(R.id.artist_list);
        this.adapter = new ArtistListAdapter(this, null, false);
        this.listView.setAdapter(this.adapter);

        this.getSupportLoaderManager().initLoader(ArtistListActivity.LISTVIEW_LOADER_ID, null, this).forceLoad();

        this.listView.setOnItemClickListener(this);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		boolean ret = true;

		switch(item.getItemId()){
			case android.R.id.home:
				this.finish();
				break;

			default:
				ret = false;
		}

		return ret;
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra(ArtistDetailsActivity.EXTRA_SET_ID, ((Cursor)this.listView.getAdapter().getItem(position)).getInt(MusicSet.COLUMN_ID));
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /*
     * Handle CursorLoader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new SQLiteCursorLoader(this, Application.getDbHelper().getReadableDatabase(), MusicSet.getSetsQuery("sets.stage ASC, artists.name ASC"), null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        this.adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        this.adapter.changeCursor(null);
    }
}

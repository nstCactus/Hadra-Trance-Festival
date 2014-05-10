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

package com.zion.htf.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.zion.htf.Application;
import com.zion.htf.R;
import com.zion.htf.adapter.ArtistListAdapter;
import com.zion.htf.data.Artist;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ArtistListActivity extends SherlockFragmentActivity implements AdapterView.OnItemClickListener{
    private static  SQLiteDatabaseHelper    dbOpenHelper = Application.getDbHelper();
    private         ListView                listView;

    /* BEGIN Columns indexes for convenience */
    protected final int COLUMN_ID         = 0;
    protected final int COLUMN_ARTIST     = 1;
    protected final int COLUMN_GENRE      = 2;
    protected final int COLUMN_TYPE       = 3;
    protected final int COLUMN_PICTURE    = 4;
	/* END Columns indexes for convenience */

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_artist_list);

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

        this.listView = (ListView)this.findViewById(R.id.artist_list);
        this.listView.setAdapter(new ArtistListAdapter<Artist>(this, R.layout.item_artist_list, R.id.label, this.getAllArtists()));
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

    protected List<Artist> getAllArtists(){
        List<Artist> artists = new ArrayList<Artist>();

        String query = " SELECT a.id, a.name, a.genre, s.type, a.picture_name FROM artists AS a JOIN sets AS s ON s.artist = a.id GROUP BY a.name ORDER BY name ASC";// FIXME: get artists instead
        Cursor cursor = this.dbOpenHelper.getReadableDatabase().rawQuery(query, null);

        while(cursor.moveToNext()){
            Artist artist = new Artist(cursor.getString(COLUMN_ARTIST));
            artist.setSetType(cursor.getString(COLUMN_TYPE))
                  .setGenre(cursor.getString(COLUMN_GENRE))
                  .setId(cursor.getInt(COLUMN_ID))
                  .setPicture(cursor.getString(COLUMN_PICTURE));

            artists.add(artist);
        }
        if(!cursor.isClosed()) cursor.close();
        this.dbOpenHelper.close();

        return artists;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Artist item = (Artist)this.listView.getAdapter().getItem(position);
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra("artist_id", item.getId());
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}

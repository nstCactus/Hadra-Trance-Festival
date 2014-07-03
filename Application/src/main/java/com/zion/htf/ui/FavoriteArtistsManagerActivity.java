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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.zion.htf.BuildConfig;
import com.zion.htf.R;
import com.zion.htf.adapter.FavoriteArtistsListAdapter;
import com.zion.htf.data.Artist;
import com.zion.htf.exception.ArtistNotFoundException;

public class FavoriteArtistsManagerActivity extends AbstractActionModeCompatListActivity{
    protected FavoriteArtistsListAdapter<Artist> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.adapter = new FavoriteArtistsListAdapter<Artist>(this, R.layout.item_favorite_artists_list, R.id.label, Artist.getFavoriteArtistsList());

        super.setLayoutId(R.layout.activity_favorite_artists_manager);
        super.setListViewId(R.id.favorite_artists_list);
        super.onCreate(savedInstanceState);
    }

    @Override
    // FIXME: find a better way to refresh this.
    protected void onResume(){
        // Refresh the list of favorite artists to handle the cases where a user unfavorites an artist coming from this activity and then comes back to it
        boolean dataSetChanged = false;
        Artist artist;

        // Loop through the Artist object present in adapter to check if their favorite status changed.
        // If it is no longer favorite, then remove it from the adapter
        for(int i = 0; i < super.adapter.getCount(); i++){
            artist = (Artist)super.adapter.getItem(i);
            try{
                if(!Artist.getById(artist.getId()).isFavorite()){
                    super.adapter.remove(artist);
                    dataSetChanged = true;
                }
            }
            catch(ArtistNotFoundException e){
                if(BuildConfig.DEBUG) e.printStackTrace();
            }
        }
        if(dataSetChanged) super.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra("artist_id", ((Artist)super.adapter.getItem(position)).getId());
        this.startActivity(intent);
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
}

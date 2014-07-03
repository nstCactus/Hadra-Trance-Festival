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
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zion.htf.R;
import com.zion.htf.adapter.ArtistListAdapter;
import com.zion.htf.data.Item;
import com.zion.htf.data.MusicSet;

public class ArtistListActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{
    private         ListView                listView;

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_artist_list);

		ActionBar actionBar = this.getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

        this.listView = (ListView)this.findViewById(R.id.artist_list);
        this.listView.setAdapter(new ArtistListAdapter<Item>(this, R.layout.item_artists_list, R.id.label, MusicSet.getList()));
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
        MusicSet item = (MusicSet)this.listView.getAdapter().getItem(position);
        Intent intent = new Intent(this, ArtistDetailsActivity.class);
        intent.putExtra("set_id", item.getId());
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}

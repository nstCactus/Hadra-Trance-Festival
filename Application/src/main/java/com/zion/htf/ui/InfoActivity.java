/*
 * Copyright 2013 Yohann Bianchi
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or see <http://www.gnu.org/licenses/>.
 */

package com.zion.htf.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.zion.htf.Application;
import com.zion.htf.R;

public class InfoActivity extends SherlockListActivity implements AdapterView.OnItemClickListener{
	private static final String TAG = "InfoActivity";
	private ListAdapter listAdapter;
	private static Context  context = Application.getContext();
	private static String[] items   = new String[]{
			context.getString(R.string.info_festival),
			context.getString(R.string.info_transport),
			context.getString(R.string.info_camp),
			context.getString(R.string.info_village),
			context.getString(R.string.info_dogs),
			context.getString(R.string.info_faq),
			context.getString(R.string.info_about),
			context.getString(R.string.info_open_source),
			context.getString(R.string.action_donate)
	};

	@Override
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);

		// Set up ActionBar
		this.getSupportActionBar().setHomeButtonEnabled(true);

		// Set up ListView
		this.listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, InfoActivity.items);
		this.setListAdapter(this.listAdapter);
		this.getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		String item = (String)parent.getAdapter().getItem(position);

		Intent intent = null;

		int strId;
		if(item.equals(this.getString(R.string.action_donate))){
			intent = new Intent(this, DonateActivity.class);
		}
		else if(
				item.equals(this.getString(strId = R.string.info_festival)) ||
				item.equals(this.getString(strId = R.string.info_transport)) ||
				item.equals(this.getString(strId = R.string.info_camp)) ||
				item.equals(this.getString(strId = R.string.info_dogs)) ||
				item.equals(this.getString(strId = R.string.info_faq)) ||
				item.equals(this.getString(strId = R.string.info_about)) ||
				item.equals(this.getString(strId = R.string.info_open_source))||
				item.equals(this.getString(strId = R.string.info_village))
				){
			try{
				String name = this.getResources().getResourceEntryName(strId);
				intent = new Intent(this, InfoDetailsActivity.class);
				intent.putExtra(InfoDetailsActivity.name, name);
			}
			catch(Resources.NotFoundException e){
				Log.e(TAG, String.format("Resource entry name not found for string \"%s\" (id: %d)", item, strId), e);
			}
		}

		if(null != intent){
			this.startActivity(intent);
			this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		}
	}
}

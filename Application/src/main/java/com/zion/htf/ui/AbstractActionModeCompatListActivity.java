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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zion.htf.R;
import com.zion.htf.adapter.AbstractActionModeListAdapter;

public abstract class AbstractActionModeCompatListActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    protected ListView listView;
    protected AbstractActionModeListAdapter<Object> adapter;
    protected ActionMode actionMode = null;
    protected final ActionMode.Callback actionModeCallback = new ActionModeCallback();
    private int listViewId;
    private int layoutId;
    protected static final int LISTVIEW_LOADER_ID = 5433;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(null == this.adapter) throw new RuntimeException("The adapter member must be initialized before calling super.onCreate() in classes derived from AbstractActionModeCompatListActivity.");
        super.onCreate(savedInstanceState);

        if(0 == this.layoutId) throw new RuntimeException("You must call super.setLayoutId(int) before calling super.onCreate() in classes derived from AbstractActionModeCompatListActivity.");
        this.setContentView(this.layoutId);

        if(0 == this.listViewId) throw new RuntimeException("You must call super.setListViewId(int) before calling super.onCreate() in classes derived from AbstractActionModeCompatListActivity.");
        this.listView = (ListView)this.findViewById(this.listViewId);
        if(null == this.listView) throw new RuntimeException("ListView can't be found.");

        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemLongClickListener(new AdapterViewOnItemLongClickListener());
        this.listView.setOnItemClickListener(new AdapterViewOnItemClickListener());
    }

    public void setListViewId(int listViewId) {
        this.listViewId = listViewId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    private void updateTitle(){
        if(null != this.actionMode) {
            int count = this.adapter.getSelectedCount();
            this.actionMode.setTitle(0 == count ? this.getString(R.string.no_item_selected): this.getResources().getQuantityString(R.plurals.item_selected, count, count));
        }
        else{
            Log.e("AlarmManagerActivity", "Trying to update actionMode title while not in actionMode.");
        }
    }

    @Override
    abstract public Loader<Cursor> onCreateLoader(int i, Bundle bundle);

    @Override
    abstract public void onLoadFinished(Loader<Cursor> loader, Cursor cursor);

    @Override
    abstract public void onLoaderReset(Loader<Cursor> loader);

    protected class ActionModeCallback implements ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.action_mode_compat_list, menu);

            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_remove_selected:
                    Log.v("AlarmManagerActivity", "User wants to delete all selected items.");
                    AbstractActionModeCompatListActivity.this.adapter.removeSelected();
                    AbstractActionModeCompatListActivity.this.getSupportLoaderManager().restartLoader(AbstractActionModeCompatListActivity.LISTVIEW_LOADER_ID, null, AbstractActionModeCompatListActivity.this).forceLoad();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_select_all:
                    Log.v("AlarmManagerActivity", "User selected all items");
                    AbstractActionModeCompatListActivity.this.adapter.selectAll();
                    AbstractActionModeCompatListActivity.this.updateTitle();
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode){
            AbstractActionModeCompatListActivity.this.adapter.clearSelection();
            AbstractActionModeCompatListActivity.this.actionMode = null;
        }
    }

    private class AdapterViewOnItemLongClickListener implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
            if(null == AbstractActionModeCompatListActivity.this.actionMode){
                Log.v("AlarmManagerActivity", "Item LONG_CLICK while in NORMAL mode.");
                AbstractActionModeCompatListActivity.this.actionMode = AbstractActionModeCompatListActivity.this.startSupportActionMode(AbstractActionModeCompatListActivity.this.actionModeCallback);
                AbstractActionModeCompatListActivity.this.adapter.selectItem(position);
                //FIXME: When entering actionMode, the user-clicked item often gets unselected as the OnItemClickListener is fired
            }
            else{
                //TODO: Do something
                Log.v("AlarmManagerActivity", "Item LONG CLICK while in ACTION mode.");

            }
            return false;
        }
    }

    private class AdapterViewOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(null != AbstractActionModeCompatListActivity.this.actionMode){
                AbstractActionModeCompatListActivity.this.adapter.toggleSelection(position);
                AbstractActionModeCompatListActivity.this.updateTitle();
                Log.v("AlarmManagerActivity", "Item CLICK while in ACTION mode.");
                //FIXME: Quit actionMode if no selected item left (when above FIXME is fixed)
            }
            else{
                Log.v("AlarmManagerActivity", "Item CLICK while in NORMAL mode.");
                AbstractActionModeCompatListActivity.this.onItemClick(parent, view, position, id);
            }
        }
    }

    /**
     * Called when an item in the listView is clicked, when not in action mode
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    protected abstract void onItemClick(AdapterView<?> parent, View view, int position, long id);
}

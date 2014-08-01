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

package com.zion.htf.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zion.htf.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public abstract class AbstractActionModeListAdapter<T> extends CachedImageCursorAdapter{
    protected final Context context;
    protected final LayoutInflater layoutInflater;
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();

	protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE HH:mm", "fr".equals(Locale.getDefault().getLanguage()) ? Locale.FRANCE : Locale.ENGLISH);

    public AbstractActionModeListAdapter(Context context, Cursor cursor, boolean autoRequery){
        super(context, cursor, autoRequery);
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    abstract public View newView(Context context, Cursor cursor, ViewGroup parent);

    @Override
    abstract public void bindView(View view, Context context, Cursor cursor);

    /*********************/
    /* Handle selections */
    /*********************/

    @Override
    /**
     * {@inheritDoc}
     */
    public View getView(int position, View convertView, ViewGroup parent){
        convertView = super.getView(position, null, parent);

        if(this.isSelected(position))   convertView.setBackgroundColor(this.context.getResources().getColor(R.color.brand_orange));
        else                            convertView.setBackgroundColor(this.context.getResources().getColor(android.R.color.transparent));

        return convertView;
    }

    /**
     * Get the number of selected items
     * @return the number of selected items
     */
    public int getSelectedCount() {
        return this.selectedPositions.size();
    }

    /**
     * Get the positions of selected items
     * @return A {@link android.util.SparseBooleanArray} containing the ids of the selected items
     */
    public SparseBooleanArray getSelectedPositions() {
        return this.selectedPositions;
    }

    /**
     * Remove the selected status of every item
     */
    public void clearSelection() {
        this.selectedPositions = new SparseBooleanArray();
        this.notifyDataSetChanged();
    }

    /**
     * Add the item at the given {@code position} to the selection
     * @param position The position of the item
     */
    public void selectItem(int position){
        if(position < this.getCount()){
            Log.v(this.getClass().getName(), String.format(Locale.ENGLISH, "User selected item at position %d.", position));
            this.selectedPositions.put(position, true);
            this.notifyDataSetChanged();
        }
    }

    /**
     * Remove the item at {@code position} from the selection
     * @param position The position of the item
     */
    public void deselectItem(int position){
        if(position < this.getCount()){
            this.selectedPositions.delete(position);
            this.notifyDataSetChanged();
        }
    }

    /**
     * Toggle the selection of the item at {@code position}
     * @param position The position of the item
     */
    public void toggleSelection(int position) {
        if(this.isSelected(position))   this.deselectItem(position);
        else                            this.selectItem(position);
    }

    /**
     * Returns whether the item at {@code position} is selected or not
     * @param position The position of the item
     * @return {@code true} if the item is selected, {@code false} otherwise
     */
    public boolean isSelected(int position){
        return this.selectedPositions.get(position);
    }

    /**
     * Add all items to the selection
     */
    public void selectAll(){
        this.clearSelection();
        for(int position = 0; position < this.getCount(); position++){
            this.selectItem(position);
        }

        this.notifyDataSetChanged();
    }

    /*******************/
    /* Manipulate data */
    /*******************/

    /**
     * Remove the specified object from the list and the database.
     * @param position The position of the object to delete.
     */
    public void delete(int position){
        if(0 < this.getSelectedCount()) throw new RuntimeException("Trying to delete a single item while several items are selected");
        this.selectItem(position);
        this.removeSelected();
    }

    /**
     * Remove the selection items from the list and the database
     */
    abstract public void removeSelected();
}

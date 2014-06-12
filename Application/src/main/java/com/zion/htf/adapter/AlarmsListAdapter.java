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
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zion.htf.R;
import com.zion.htf.data.Item;
import com.zion.htf.data.MusicSet;
import com.zion.htf.data.SavedAlarm;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AlarmsListAdapter<T> extends ArrayAdapter<T> {
    static class ItemViewHolder {
		TextView artistName;
		TextView stage;
		TextView time;

	}

	static class SectionViewHolder{
		TextView sectionHeader;
	}

    private final Context context;
    private SparseBooleanArray selectedPositions = new SparseBooleanArray();

	protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE HH:mm", "fr".equals(Locale.getDefault().getLanguage()) ? Locale.FRANCE : Locale.ENGLISH);

	public AlarmsListAdapter(Context context, int resource, int textViewResourceId, List<T> objects){
		super(context, resource, textViewResourceId, objects);
        this.context = context;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(Item.TYPE_SECTION == this.getItemViewType(position)){
			AlarmsListAdapter.SectionViewHolder holder;

			if(null == convertView){
				// Inflate the view
                LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.section_line_up_list, parent, false);
                assert null != convertView;

				// Get reference to its field and store it in the ViewHolder
				holder = new AlarmsListAdapter.SectionViewHolder();
                holder.sectionHeader = (TextView)convertView.findViewById(R.id.list_item_section_header);

				// Store the ViewHolder in the View's tag for future retrieval
				convertView.setTag(holder);
			}
			else{
				holder = (AlarmsListAdapter.SectionViewHolder)convertView.getTag();
			}

			holder.sectionHeader.setText(this.getItem(position).toString());
		}
		else{
			AlarmsListAdapter.ItemViewHolder holder;
			SavedAlarm alarm = (SavedAlarm)this.getItem(position);
            MusicSet set = alarm.getSet();

			if(null == convertView){
				// Inflate the view
				convertView = super.getView(position, null, parent);
                assert null != convertView;

                // Get references to its fields and store them in the ViewHolder
				holder = new AlarmsListAdapter.ItemViewHolder();
				holder.artistName = (TextView)convertView.findViewById(R.id.label);
				holder.stage = (TextView)convertView.findViewById(R.id.stage);
				holder.time = (TextView)convertView.findViewById(R.id.time);

				convertView.setTag(holder);
			}
			else{
				holder = (AlarmsListAdapter.ItemViewHolder)convertView.getTag();
			}

            String stage = set.getStage();
            stage = stage.replace("The ", "");
            stage = stage.substring(0, 1).toUpperCase(Locale.ENGLISH) + stage.substring(1);
			holder.artistName.setText(alarm.getSet().getArtist().getName());
			holder.time.setText(this.simpleDateFormat.format(alarm.getTimestamp()));
			holder.stage.setText(stage);

            if(this.isSelected(position))   convertView.setBackgroundColor(this.context.getResources().getColor(R.color.brand_orange));
            else                            convertView.setBackgroundColor(this.context.getResources().getColor(android.R.color.transparent));
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount(){
		return 2;
	}

	@Override
	public int getItemViewType(int position){
		Item item = (Item)this.getItem(position);
		return item.getType();
	}

	//@Override
	public boolean isItemViewTypePinned(int viewType){
		return Item.TYPE_SECTION == viewType;
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
     * @return A {@link com.zion.htf.data.SavedAlarm} containing the ids of the selected items
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
        if(Item.TYPE_ITEM == ((Item)this.getItem(position)).getType() && position < this.getCount()){
            this.selectedPositions.put(position, true);
            this.notifyDataSetChanged();
        }
    }

    /**
     * Remove the item at {@code position} from the selection
     * @param position The position of the item
     */
    public void deselectItem(int position){
        if(Item.TYPE_ITEM == ((Item)this.getItem(position)).getType() && position < this.getCount()){
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
            if(Item.TYPE_ITEM == ((Item)this.getItem(position)).getType()){
                this.selectItem(position);
            }
        }

        this.notifyDataSetChanged();
    }

    /**
     * Remove the specified object from the list and the database.
     * @param position The position of the object to delete.
     */
    public void delete(int position){
        T object = this.getItem(position);
        if(!(object instanceof SavedAlarm)) throw new RuntimeException(String.format("Trying to delete something that is not a SavedAlarm instance (actually it is an instance of %s).", object.getClass().getName()));

        // Remove from list
        super.remove(object);
        // Remove from db
        int id = ((SavedAlarm)object).getId();
        Log.v("AlarmsListAdapter", String.format("Single alarm deletion. id = %d", id));
        int affectedRows = SavedAlarm.delete(id);
        Log.v("AlarmsListAdapter", String.format("Deleted rows: %d", affectedRows));

        this.notifyDataSetChanged();
    }

    /**
     * Remove the selection items from the list and the database
     */
    public void removeSelected(){
        SparseBooleanArray selectedIds = this.getSelectedPositions();
        String inClause = null;
        Item item;
        int id;

        for(int i = selectedIds.size() - 1; 0 <= i; i--){
            item = (Item)this.getItem(selectedIds.keyAt(i));
            if(Item.TYPE_SECTION == item.getType()) throw new RuntimeException("Something went terribly wrong. Can't delete a section. Section name = " + item.toString());
            id = ((SavedAlarm)item).getId();
            if(null == inClause)    inClause = String.valueOf(id);
            else                    inClause += String.format(",%d", id);

            Log.v("AlarmsListAdapter", String.format("Trying to delete item at position %d.Adapter size = %d", selectedIds.keyAt(i), this.getCount()));
            this.remove((T)item);
        }

        SavedAlarm.delete(inClause);
        this.notifyDataSetChanged();
    }

    /**
     * Find an alarm using its database identifier
     * @param id the database identifier of the alarm
     * @return the position of the `SavedAlarm` object matching the given {@code id} or null if none found
     */
    public int findAlarmPositionById(int id){
        int position = -1;
        T current;

        int currentPosition = 0;
        int count = this.getCount();
        while(-1 == position && currentPosition < count){
            current = this.getItem(currentPosition);
            if(current instanceof SavedAlarm && id == ((SavedAlarm)current).getId()) position = currentPosition;
            currentPosition++;
        }

        return position;
    }
}

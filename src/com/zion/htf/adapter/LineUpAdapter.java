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

package com.zion.htf.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;
import com.zion.htf.Item;
import com.zion.htf.R;
import com.zion.htf.Set;
import com.zion.htf.activity.ArtistDetailsActivity;
import com.zion.htf.activity.LineUpActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class LineUpAdapter<T> extends ArrayAdapter<T> implements PinnedSectionListView.PinnedSectionListAdapter, AdapterView.OnItemClickListener{
    protected LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    protected SimpleDateFormat simpleDateFormat = Locale.getDefault().getLanguage().equals("fr") ? new SimpleDateFormat("HH:mm") : new SimpleDateFormat("h:mm a");

    public LineUpAdapter(Context context, int resource, int textViewResourceId, List<T> objects){
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view;
        if(this.getItemViewType(position) == Item.TYPE_SECTION){
            if(convertView == null){
                view = this.layoutInflater.inflate(R.layout.section_line_up_list, parent, false);
            }
            else{
                view = convertView;
            }

            TextView sectionHeader = (TextView)view.findViewById(R.id.list_item_section_header);
            sectionHeader.setText(this.getItem(position).toString());
        }
        else{
            Set set = (Set)this.getItem(position);
            view = super.getView(position, convertView, parent);

            TextView genreField = (TextView)view.findViewById(R.id.genre);
            genreField.setText(set.getGenre());

            TextView beginDateField = (TextView)view.findViewById(R.id.start_hour);
            beginDateField.setText(simpleDateFormat.format(set.getBeginDate()));

            TextView endDateField = (TextView)view.findViewById(R.id.end_hour);
            endDateField.setText(simpleDateFormat.format(set.getEndDate()));

            ImageView artistPhoto = (ImageView)view.findViewById(R.id.artist_photo);
            artistPhoto.setImageResource(view.getResources().getIdentifier(set.getPicture(), "drawable", this.getContext().getPackageName()));

            ((ListView)parent).setOnItemClickListener(this);
        }
        return view;
    }

    @Override public int getViewTypeCount(){
        return 2;
    }

    @Override public int getItemViewType(int position) {
        Item item = (Item)this.getItem(position);
        return item.getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType){
        return viewType == Item.TYPE_SECTION;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = (Item)this.getItem(position);
        if(item.getType() == Item.TYPE_ITEM){
            Intent intent = new Intent(this.getContext(), ArtistDetailsActivity.class);
            intent.putExtra("artist_id", ((Set)item).getArtistId());
            this.getContext().startActivity(intent);
        }
    }
}
package com.zion.htf;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;

public class LineUpListFragment extends Fragment{
    private final String TAG = "LineUpListFragment";
    protected Item[] list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_line_up_list, container, false);
        ListView listView = (ListView)view.findViewById(R.id.line_up_list);
        listView.setAdapter(new LineUpAdapter<Item>(this.getActivity(), R.layout.item_line_up_list, R.id.artist_name, this.list));

        return view;
    }

    private class LineUpAdapter<T> extends ArrayAdapter<T> implements PinnedSectionListView.PinnedSectionListAdapter{
        protected LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        public LineUpAdapter(Context context, int resource, int textViewResourceId, T[] objects){
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
                view = super.getView(position, convertView, parent);
                TextView genreField = (TextView)view.findViewById(R.id.genre);
                genreField.setText(position % 2 == 0 ? "Psytrance" : "Full-on");
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
    }
}

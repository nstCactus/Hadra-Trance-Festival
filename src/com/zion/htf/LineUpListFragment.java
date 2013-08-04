package com.zion.htf;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView;

public class LineUpListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String TAG = "LineUpListFragment";
    protected int ID;

    protected SimpleCursorAdapter adapter;
    protected String[] dbFields = new String[]{"_id", "artist", "genre", "begin_date", "end_date", "lst__set_types.label" };
    protected int[] viewFields = new int[]{R.id.photo, R.id.artist_name, R.id.genre, R.id.start_hour, R.id.end_hour, R.id.set_type};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        this.adapter = new LineUpAdapter(this.getActivity(), R.layout.item_line_up_list, null, this.dbFields, this.viewFields, 0);

        View view = inflater.inflate(R.layout.fragment_line_up_list, container, false);
        ListView listView = (ListView)view.findViewById(R.id.line_up_list);
        listView.setAdapter(this.adapter);

        this.getActivity().getSupportLoaderManager().initLoader(this.ID, null, this);

        return view;
    }

    /***********************************************/
    /* BEGIN LoaderManager.LoaderCallbacks methods */
    /***********************************************/
    /** A callback method invoked by the loader when initLoader() is called */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this.getActivity(), Artist.CONTENT_URI, null, null, null, null);
    }

    /** A callback method, invoked after the requested content provider returned all the data */
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        this.adapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        this.adapter.swapCursor(null);
    }
    /*********************************************/
    /* END LoaderManager.LoaderCallbacks methods */
    /*********************************************/


    private class LineUpAdapter extends SimpleCursorAdapter implements PinnedSectionListView.PinnedSectionListAdapter{
        protected LayoutInflater layoutInflater;

        public LineUpAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags){
            super(context, layout, c, from, to, flags);
            this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            }
            return view;
        }

        @Override public int getViewTypeCount(){
            return 2;
        }

        @Override public int getItemViewType(int position){
            return Item.TYPE_ITEM;
            //Item item = (Item)this.getItem(position);
            //return item.getType();
        }

        @Override
        public boolean isItemViewTypePinned(int viewType){
             return viewType == Item.TYPE_SECTION;
        }
    }
}

package com.zion.htf;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.views.PinnedSectionListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LineUpListFragment extends Fragment{
    private final String TAG = "LineUpListFragment";
    protected Item[] list;
    protected String stage;

    /* BEGIN Columns indexes for convenience */
    protected final int COLUMN_ID           = 0;
    protected final int COLUMN_ARTIST       = 1;
    protected final int COLUMN_GENRE        = 2;
    protected final int COLUMN_BEGIN_DATE   = 3;
    protected final int COLUMN_END_DATE     = 4;
    protected final int COLUMN_TYPE         = 5;
    protected final int COLUMN_PICTURE      = 6;
    protected final int COLUMN_ARTIST_ID    = 7;
    /* END Columns indexes for convenience */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_line_up_list, container, false);

        ListView listView = (ListView)view.findViewById(R.id.line_up_list);
        listView.setAdapter(new LineUpAdapter<Item>(this.getActivity(), R.layout.item_line_up_list, R.id.label, this.getAllSets()));

        return view;
    }

    protected List<Item> getAllSets(){
        List<Item> sets = new ArrayList<Item>();

        SQLiteOpenHelper dbOpenHelper = new DatabaseOpenHelper(this.getActivity());
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        String query =    "SELECT " +
                "sets.id AS id, " +
                "artists.name AS artist, " +
                "artists.genre AS genre, " +
                "begin_date, " +
                "end_date, " +
                "sets.type AS type, " +
                "picture_name AS picture, " +
                "artists.id AS artist_id " +
                "FROM sets " +
                "JOIN artists on sets.artist = artists.id " +
                "WHERE stage = ? " +
                "ORDER BY begin_date ASC;";
        Cursor cursor = database.rawQuery(query, new String[]{this.stage});

        long previousDate = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy");
        while(cursor.moveToNext()){
            Date beginDate = new Date(cursor.getLong(COLUMN_BEGIN_DATE) * 1000);
            long currentDate = beginDate.getTime() / 3600 / 24 / 1000;

            if(currentDate > previousDate){
                sets.add(new Item(simpleDateFormat.format(beginDate), Item.TYPE_SECTION));
            }
            previousDate = currentDate;

            Set set = new Set(cursor.getString(COLUMN_ARTIST));
            set.setSetType(cursor.getString(COLUMN_TYPE))
                    .setGenre(cursor.getString(COLUMN_GENRE))
                    .setStage(this.stage)
                    .setBeginDate(beginDate)
                    .setEndDate(new Date(cursor.getLong(COLUMN_END_DATE) * 1000))
                    .setId(cursor.getInt(COLUMN_ID))
                    .setPicture(cursor.getString(COLUMN_PICTURE))
                    .setArtistId(cursor.getInt(COLUMN_ARTIST_ID));

            sets.add(set);
        }
        cursor.close();

        return sets;
    }

    private class LineUpAdapter<T> extends ArrayAdapter<T> implements PinnedSectionListView.PinnedSectionListAdapter, AdapterView.OnItemClickListener{
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
}

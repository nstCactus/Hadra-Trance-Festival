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

package com.zion.htf.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zion.htf.DatabaseOpenHelper;
import com.zion.htf.Item;
import com.zion.htf.R;
import com.zion.htf.Set;
import com.zion.htf.adapter.LineUpAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}

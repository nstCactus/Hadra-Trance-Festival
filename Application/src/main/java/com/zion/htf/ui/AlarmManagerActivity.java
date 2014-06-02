package com.zion.htf.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hb.views.PinnedSectionListView;
import com.zion.htf.R;
import com.zion.htf.adapter.LineUpAdapter;
import com.zion.htf.data.Item;

public class AlarmManagerActivity extends ActionBarActivity {

    private PinnedSectionListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);

        this.listView = (PinnedSectionListView)this.findViewById(R.id.line_up_list);
        this.listView.setAdapter(new LineUpAdapter<Item>(this, R.layout.item_line_up_list, R.id.label, null));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

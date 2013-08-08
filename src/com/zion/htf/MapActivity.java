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

package com.zion.htf;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener {
    private GoogleMap map;
    protected int spinnerPosition;
    private SQLiteDatabase database;
    private final String TAG = "MapActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);
        this.map = supportMapFragment.getMap();
        
        // Feature enablement
        this.map.setMyLocationEnabled(true);
        this.map.getUiSettings().setCompassEnabled(true);
        this.map.getUiSettings().setAllGesturesEnabled(true);
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Initial location
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.115137, 5.609024), 16.0f));

        // List navigation
        ActionBar actionBar = getSupportActionBar();
        ArrayAdapter<String> nav =  new ArrayAdapter<String>(actionBar.getThemedContext(), R.layout.sherlock_spinner_item, new String[] { getString(R.string.action_switchToSatellite), getString(R.string.action_switchToTerrain) });
        nav.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(nav, this);

        // Populate the map with locations
        DatabaseOpenHelper dbOpenHelper = new DatabaseOpenHelper(this);
        this.database = dbOpenHelper.getReadableDatabase();
        assert this.database != null;

        try{
            String langCode = Locale.getDefault().getLanguage().equals("fr") ? "fr" : "en";
            String query = "SELECT latitude, longitude, label, icon, description FROM locations AS loc INNER JOIN lst__location_types AS lty ON lty.id = loc.location_type AND lty.lang_code = ? LEFT JOIN location_descriptions AS ldc ON ldc.id = loc.location_description AND ldc.lang_code = ?;";
            Cursor result = this.database.rawQuery(query, new String[]{langCode, langCode});

            while(result.moveToNext()){
                int iconResId = this.getResources().getIdentifier(result.getString(3), "drawable", "com.zion.htf");
                String description = result.isNull(4) ? "" : result.getString(4);
                this.addLocation(new LatLng(result.getDouble(0), result.getDouble(1)), result.getString(2), iconResId, description);
            }
        }
        catch(SQLException e){
            Toast.makeText(this, this.getString(R.string.error_failed_fetching_poi), Toast.LENGTH_SHORT).show();
            Log.e(this.TAG, "Impossible de récupérer la liste des POI", e);
        }
        finally {
            this.database.close();
        }
    }

    private void addLocation(LatLng latLng, String type, int iconResId){
        this.addLocation(latLng, type, iconResId, "");
    }

    private void addLocation(LatLng latLng, String type, int iconResId, String description){
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title(type).snippet(description);
        options.icon(BitmapDescriptorFactory.fromResource(iconResId));
        this.map.addMarker(options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        this.spinnerPosition = itemPosition;
        switch(itemPosition){
            case 0:
                this.map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                this.map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item){
        boolean ret = true;
        switch(item.getItemId()){
            case R.id.action_getDirections:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr=45.12465411273364,5.591188743710518"));
                intent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));
                startActivity(intent);
                break;

            case R.id.action_legalNotices:
                String licenseInfo = GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getApplicationContext());
                AlertDialog.Builder licenseDialog = new AlertDialog.Builder(this);
                licenseDialog.setTitle(this.getString(R.string.action_legalNotices));
                licenseDialog.setMessage(licenseInfo);
                licenseDialog.show();
                break;

            case android.R.id.home:
                this.finish();
                break;

            default:
                ret = false;
        }
        return ret;
    }
}

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

package com.zion.htf.ui;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zion.htf.Application;
import com.zion.htf.R;

import org.michenux.android.db.sqlite.SQLiteDatabaseHelper;

import java.util.Locale;

public class MapActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener, LocationListener{
	private static final String GOOGLE_MAP_LATITUDE   = "google_map_latitude";
	private static final String GOOGLE_MAP_LONGITUDE  = "google_map_longitude";
	private static final String GOOGLE_MAP_ZOOM_LEVEL = "google_map_zoom_level";
	private static final String GOOGLE_MAP_BEARING    = "google_map_bearing";
	private static final String GOOGLE_MAP_TILT       = "google_map_tilt";
	private static final String GOOGLE_MAP_TYPE       = "google_map_type";
	private   GoogleMap map;
	protected int       spinnerPosition;
	private final String               TAG          = "MapActivity";
	private       SQLiteDatabaseHelper dbOpenHelper = Application.getDbHelper();
	private LocationManager  locationManager;
	private String locationProviderName;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_map);

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getBaseContext());

		// Showing status
		if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}
		else{
			SupportMapFragment supportMapFragment = (SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map);
			this.map = supportMapFragment.getMap();

			// Feature enablement
			this.map.setMyLocationEnabled(true);
			this.map.getUiSettings().setCompassEnabled(true);
			this.map.getUiSettings().setAllGesturesEnabled(true);
			this.map.getUiSettings().setMyLocationButtonEnabled(true);
			this.map.getUiSettings().setZoomControlsEnabled(true);
			this.map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

			// Configure location polling
			// Getting LocationManager object from System Service LOCATION_SERVICE
			this.locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			this.locationProviderName = this.locationManager.getBestProvider(criteria, true);
			// Initial location
			this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.115137, 5.609024), 16.0f));

			// List navigation
			ActionBar actionBar = this.getSupportActionBar();
			ArrayAdapter<String> nav = new ArrayAdapter<String>(actionBar.getThemedContext(), R.layout.sherlock_spinner_item, new String[]{
					this.getString(R.string.action_switchToSatellite),
					this.getString(R.string.action_switchToTerrain)
			});
			nav.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setListNavigationCallbacks(nav, this);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);

			// Populate the map with locations
			try{
				String langCode = Locale.getDefault().getLanguage().equals("fr") ? "fr" : "en";
				String query = "SELECT latitude, longitude, label, icon, description FROM locations AS loc INNER JOIN lst__location_types AS lty ON lty.id = loc.location_type AND lty.lang_code = ? LEFT JOIN location_descriptions AS ldc ON ldc.id = loc.location_description AND ldc.lang_code = ?;";
				Cursor cursor = this.dbOpenHelper.getReadableDatabase().rawQuery(query, new String[]{langCode, langCode});

				while(cursor.moveToNext()){
					int iconResId = this.getResources().getIdentifier(cursor.getString(3), "drawable", "com.zion.htf");
					String description = cursor.isNull(4) ? "" : cursor.getString(4);
					//if(iconResId == 0) iconResId = R.drawable.marker_generic;
					this.addLocation(new LatLng(cursor.getDouble(0), cursor.getDouble(1)), cursor.getString(2), iconResId, description);
				}
				if(!cursor.isClosed()) cursor.close();
			}
			catch(SQLException e){
				Toast.makeText(this, this.getString(R.string.error_failed_fetching_poi), Toast.LENGTH_SHORT).show();
				Log.e(this.TAG, "Impossible de récupérer la liste des POI", e);
			}
			finally{
				this.dbOpenHelper.close();
			}
		}
	}

	private void addLocation(LatLng latLng, String type, int iconResId){
		this.addLocation(latLng, type, iconResId, "");
	}

	private void addLocation(LatLng latLng, String type, int iconResId, String description){
		MarkerOptions options = new MarkerOptions();
		options.position(latLng)
			   .title(type)
			   .snippet(description);
		if(0 != iconResId) options.icon(BitmapDescriptorFactory.fromResource(iconResId));
		this.map.addMarker(options);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		this.getSupportMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId){
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
				this.startActivity(intent);
				break;

			case android.R.id.home:
				this.finish();
				break;

			default:
				ret = false;
		}
		return ret;
	}

	@Override
	public void onLocationChanged(Location location){
		this.locationManager.removeUpdates(this);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras){

	}

	@Override
	public void onProviderEnabled(String provider){

	}

	@Override
	public void onProviderDisabled(String provider){

	}

	@Override
	public void onResume(){
		super.onResume();

		// Getting Current Location
		if(null != this.locationProviderName){
			Location location = this.locationManager.getLastKnownLocation(this.locationProviderName);

			this.locationManager.requestLocationUpdates(this.locationProviderName, 20000, 0, this);
		}
	}

	@Override
	public void onPause(){
		if(null != this.locationProviderName){
			this.locationManager.removeUpdates(this);
		}

		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putDouble(GOOGLE_MAP_LATITUDE, this.map.getCameraPosition().target.latitude);
		savedInstanceState.putDouble(GOOGLE_MAP_LONGITUDE, this.map.getCameraPosition().target.longitude);
		savedInstanceState.putFloat(GOOGLE_MAP_ZOOM_LEVEL, this.map.getCameraPosition().zoom);
		savedInstanceState.putFloat(GOOGLE_MAP_TILT, this.map.getCameraPosition().tilt);
		savedInstanceState.putFloat(GOOGLE_MAP_BEARING, this.map.getCameraPosition().bearing);
		savedInstanceState.putInt(GOOGLE_MAP_TYPE, this.map.getMapType());

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		if(null != this.map){
			double latitude, longitude;
			float zoom, bearing, tilt;
			int mapType;

			CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
			if(91l != (latitude = savedInstanceState.getDouble(GOOGLE_MAP_LATITUDE, 91l))
			   && 181l != (longitude = savedInstanceState.getDouble(GOOGLE_MAP_LONGITUDE, 181))) cameraPositionBuilder.target(new LatLng(latitude, longitude));

			if(23 > (zoom = savedInstanceState.getFloat(GOOGLE_MAP_ZOOM_LEVEL, 22))) cameraPositionBuilder.zoom(zoom);
			if(92 > (tilt = savedInstanceState.getFloat(GOOGLE_MAP_TILT, 91))) cameraPositionBuilder.tilt(tilt);
			if(362 > (bearing = savedInstanceState.getFloat(GOOGLE_MAP_BEARING))) cameraPositionBuilder.bearing(bearing);
			if(GoogleMap.MAP_TYPE_SATELLITE == (mapType = savedInstanceState.getInt(GOOGLE_MAP_TYPE))
			   || GoogleMap.MAP_TYPE_TERRAIN == mapType) this.map.setMapType(mapType);

			this.map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionBuilder.build()));
		}

		super.onRestoreInstanceState(savedInstanceState);
	}
}

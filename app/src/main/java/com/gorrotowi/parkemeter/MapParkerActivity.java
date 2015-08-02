package com.gorrotowi.parkemeter;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapParkerActivity extends AppCompatActivity implements LocationListener {

    Toolbar toolbar;
    LocationManager locationManager;
    GoogleMap googleMap;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_parker);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Parkimetros");
        toolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        setSupportActionBar(toolbar);

        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//        googleMap = ((MapFragment) getMapfra()
//                .findFragmentById(R.id.map)).getMap();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng localizacionLtLn = new LatLng(19.432675, -99.16300443);

        CameraPosition camPos = new CameraPosition.Builder()
                .target(localizacionLtLn)
                .zoom(10)
                .build();
        CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
        googleMap.animateCamera(camUpd);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        try {
            Location location = locationManager.getLastKnownLocation(provider);
            location.getLongitude();
            location.getAltitude();
            location.getLatitude();
            //Log.e("Log location", location.toString());
//            Log.e("latitude", location.getLatitude() + "");
//            Log.e("longitud", location.getLongitude() + "");
//            Log.e("altitude", location.getAltitude() + "");
            onLocationChanged(location);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MapParkerActivity.this, "En estos momentos no podemos obtener tu ubicacion, intenta mas tarde", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_parker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double latitude, longitud, altitud;
        latitude = (double) (location.getLatitude());
        longitud = (double) (location.getLongitude());
        altitud = (double) (location.getAltitude());

        LatLng localizacion = new LatLng(latitude, longitud);

        CameraPosition camPos = new CameraPosition.Builder()
                .target(localizacion).zoom(18).tilt(45).build();
        CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
        googleMap.animateCamera(camUpd);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

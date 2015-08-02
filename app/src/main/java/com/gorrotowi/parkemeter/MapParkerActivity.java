package com.gorrotowi.parkemeter;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gorrotowi.parkemeter.customelements.TextViewBariol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapParkerActivity extends AppCompatActivity implements LocationListener {

    Toolbar toolbar;
    LocationManager locationManager;
    GoogleMap googleMap;
    String provider;
    FloatingActionButton fabButton;
    LinearLayout contentDetail;
    TextViewBariol txtStreet;
    JsonObjectRequest jsonRegisterUser;
    RequestQueue rq;
    MaterialDialog.Builder progressDialogBuild;
    MaterialDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_parker);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Parkimetros");
        toolbar.setTitleTextColor(getResources().getColor(R.color.icons));
        setSupportActionBar(toolbar);

        fabButton = (FloatingActionButton) findViewById(R.id.fabMapWallet);
        contentDetail = (LinearLayout) findViewById(R.id.layoutMapDetail);
        txtStreet = (TextViewBariol) findViewById(R.id.txtMapNameStreet);

        rq = Volley.newRequestQueue(this);

        progressDialogBuild = new MaterialDialog.Builder(MapParkerActivity.this)
                .content(R.string.wait_dialog)
                .cancelable(false)
                .progress(true, 0);

        progressDialog = progressDialogBuild.build();
        progressDialog.show();

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapParkerActivity.this, PayParkemeterActivity.class));
            }
        });

        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

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
            onLocationChanged(location);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MapParkerActivity.this, "En estos momentos no podemos obtener tu ubicacion, intenta mas tarde", Toast.LENGTH_LONG).show();
        }


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapParkerActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    txtStreet.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fabButton.setVisibility(View.VISIBLE);
                contentDetail.setVisibility(View.VISIBLE);
                return false;
            }
        });

        jsonRegisterUser = new JsonObjectRequest(Request.Method.GET, "http://parqueamesta-49762.onmodulus.net/v1/geolocs", new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                parseParkemeters(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(MapParkerActivity.this, "Algo ocurrio mal, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });

        rq.add(jsonRegisterUser);


    }

    private void parseParkemeters(JSONObject response) {
        try {
            progressDialog.hide();
            JSONArray jsonArrayParks = response.getJSONArray("features");

            for (int i = 0; i < jsonArrayParks.length(); i++) {
                Double lat = Double.parseDouble(jsonArrayParks.getJSONObject(i).getJSONObject("properties").getString("POINT_Y"));
                Double lng = Double.parseDouble(jsonArrayParks.getJSONObject(i).getJSONObject("properties").getString("POINT_X"));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
        Double latitude, longitud;
        latitude = (double) (location.getLatitude());
        longitud = (double) (location.getLongitude());

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

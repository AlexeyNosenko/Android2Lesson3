package com.example.nosenko.android2lesson3;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void viewDialogAddMarker(final LatLng latLng){
        AlertDialog.Builder dl = new AlertDialog.Builder(this);
        dl.setTitle(R.string.dl_title);
        dl.setMessage(R.string.dl_message);

        final EditText newTask = new EditText(this);
        dl.setView(newTask);

        dl.setPositiveButton(R.string.dl_btn_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMap.addMarker(new MarkerOptions().position(latLng).title(newTask.getText().toString()));
            }
        });

        dl.setNegativeButton(R.string.dl_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dl.show();
    }

    private void onParams(){
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    private void sendGeoQuery(String street){
        Log.d("sendGeoQuery", street);
        final String urlJson = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        try {
            final JSONObject response = JsonReader.read(urlJson + street + "&key=AIzaSyDQ-aVd4sBsgEewO3_-47tnTzhbFhIuZvs");
            Log.d("sendGeoQuery", urlJson + street + "&key=AIzaSyDQ-aVd4sBsgEewO3_-47tnTzhbFhIuZvs = " + response.getJSONArray("results").length());
            for(int i = 0; i < response.getJSONArray("results").length(); i++){
                JSONObject location = response.getJSONArray("results").getJSONObject(i);
                location = location.getJSONObject("geometry");
                location = location.getJSONObject("location");
                final double lng = location.getDouble("lng");// долгота
                final double lat = location.getDouble("lat");// широта
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(String.valueOf(i)));
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_action_clear) {
            mMap.clear();
        }

        if (id == R.id.menu_action_geo){
            sendGeoQuery("Россия, Москва, улица Поклонная, 12");
        }
        return super.onMenuItemSelected(featureId, item);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                viewDialogAddMarker(latLng);
                sendGeoQuery("Россия, Москва, улица Поклонная, 12");
            }

        });
        onParams();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}

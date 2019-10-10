package com.example.locatesamurai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String root;
    File myDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        root = android.os.Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/GPSLoggerGaurav");
        Intent second_intent_maps = getIntent();

        String fname = myDir + "/" + second_intent_maps.getStringExtra("ITEM_SELECTED");
        Toast.makeText(getApplicationContext(),fname, Toast.LENGTH_SHORT).show();

        InputStream instream = null;
        try {
            instream = new FileInputStream(fname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader inputreader = new InputStreamReader(instream);
        BufferedReader reader= new BufferedReader(inputreader);
        List<LatLng> latLngList = new ArrayList<LatLng>();
        String line = "";

        try {
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) // Read until end of file
        {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            double lat = Double.parseDouble(line.split(",")[0]);
            double lon = Double.parseDouble(line.split(",")[1]);
            latLngList.add(new LatLng(lat, lon));
        }
        Toast.makeText(getApplicationContext(),String.valueOf(latLngList.size()), Toast.LENGTH_SHORT).show();

// Add them to map
        for(LatLng pos : latLngList)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("Title!")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))); // Don't necessarily need title
           // Toast.makeText(getApplicationContext(),String.valueOf(pos), Toast.LENGTH_SHORT).show();

            //mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            float zoomLevel = 16.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoomLevel));
        }
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}

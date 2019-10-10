package com.example.locatesamurai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView listView;
    String root;
    File myDir;
    private Button start_button;
    private Button stop_button;
    private Button plot_button;
    private TextView latitude_value;
    private TextView longitude_value;
    private TextView speed_value;
    //private TextView debug_value;
    private LocationManager locationManager;
    private LocationListener listener;
    private int i;
    private StringBuilder data;
    private ArrayList<String> fileArray;
    String plot_button_value;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plot_button_value = "PLOT MAP";
        setContentView(R.layout.activity_main);
        root = android.os.Environment.getExternalStorageDirectory().toString();
        myDir = new File(root + "/GPSLoggerGaurav");
        //Toast.makeText(getApplicationContext(),"path is "+ root, Toast.LENGTH_SHORT).show();

        fileArray = new ArrayList<String>();
        File directory = new File(String.valueOf(myDir));
        File[] files = directory.listFiles();
        if(files != null) {
            Log.d("Files", "Size: " + files.length);

            for (int i = files.length - 1; i >= 0; i--) {
                Log.d("Files", "FileName:" + files[i].getName());
                fileArray.add(files[i].getName());
            }
        }
        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_list, fileArray);

        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String itemValue = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("ITEM_SELECTED", itemValue);

                plot_button.setVisibility(View.VISIBLE);
                plot_button_value = "PLOT MAP";
                plot_button.setText(plot_button_value);
                plot_button.setEnabled(true);
                listView.setVisibility(View.GONE);
                startActivity(intent);
            }
        });

        plot_button = (Button) findViewById(R.id.plot_button);
        plot_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),plot_button_value, Toast.LENGTH_SHORT).show();
                //String plot_button_value = (String) plot_button.getText();
                if (plot_button_value == "PLOT MAP") {
                    listView.setAdapter(adapter);
                    //plot_button.setVisibility(View.GONE);
                    plot_button_value = "SCROLL BELOW AND SELECT ANY FILE";
                    plot_button.setText(plot_button_value);
                    plot_button.setEnabled(false);
                    listView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Click any of the files to show MAP", Toast.LENGTH_SHORT).show();

                    //   plot_button.setEnabled(false);
                }
                //if(plot_button.getText() == "CLOSE")
                else {
                    //plot_button.setVisibility(View.GONE);
                    plot_button_value = "PLOT MAP";
                    plot_button.setText(plot_button_value);
                    plot_button.setEnabled(true);
                    listView.setVisibility(View.GONE);
                    //   plot_button.setEnabled(false);
                }
            }
        });


        latitude_value = (TextView) findViewById(R.id.latitude_value);
        longitude_value = (TextView) findViewById(R.id.longitude_value);
        speed_value = (TextView) findViewById(R.id.speed_value);
        //debug_value = (TextView) findViewById(R.id.debug_value);
        start_button = (Button) findViewById(R.id.start_button);
        stop_button = (Button) findViewById(R.id.stop_button);
        i = 0;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude_value.setText("" + location.getLatitude());
                longitude_value.setText("" + location.getLongitude());
                speed_value.setText("" + location.getSpeed());
                //debug_value.setText("" + i);
                i += 1;
                data.append("\n" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()) + "," + String.valueOf(location.getSpeed()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                i = 0;
                start_button.setBackgroundColor(Color.YELLOW);
                stop_button.setBackgroundColor(Color.LTGRAY);
                plot_button_value = "PLOT MAP";
                plot_button.setText(plot_button_value);
                plot_button.setEnabled(true);
                listView.setVisibility(View.GONE);

                //plot_button.setVisibility(View.VISIBLE);
                //plot_button.setEnabled(false);
                listView.setVisibility(View.GONE);
                data = new StringBuilder();
                data.append("Latitude, Longitude, Speed");
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 1000, 0, listener);
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //stopButton.setBackgroundColor(65535);
                //startButton.setBackgroundColor(12632256);
                stop_button.setBackgroundColor(Color.YELLOW);
                start_button.setBackgroundColor(Color.LTGRAY);
                //plot_button.setEnabled(true);
                locationManager.removeUpdates(listener);

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Do the file write
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }

                    Date currentTime = Calendar.getInstance().getTime();
                    String fname = "GPS_data_" + currentTime + ".csv";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        //FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
                        //debug_value.setText(String.valueOf(fname));
                        Log.d("Files", "Path: " + myDir);

                        out.write((data.toString()).getBytes());
                        out.flush();
                        out.close();
                        Toast.makeText(getApplicationContext(), "File stored in " + file, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }

                root = android.os.Environment.getExternalStorageDirectory().toString();
                myDir = new File(root + "/GPSLoggerGaurav");

                fileArray = new ArrayList<String>();
                File directory = new File(String.valueOf(myDir));
                File[] files = directory.listFiles();
                Log.d("Files", "Size: " + files.length);

                for (int i = files.length - 1; i >= 0; i--) {
                    Log.d("Files", "FileName:" + files[i].getName());
                    fileArray.add(files[i].getName());
                }
                //ArrayAdapter<String>
                adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_list, fileArray);

                final ListView listView = (ListView) findViewById(R.id.mobile_list);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long id) {

                        String itemValue = (String) listView.getItemAtPosition(position);
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("ITEM_SELECTED", itemValue);
                        startActivity(intent);
                    }
                });
                listView.setAdapter(adapter);


            }
        });
    }
}



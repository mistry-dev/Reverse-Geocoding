package com.example.reversegeolocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText editText1, editText2;
    TextView textView;
    Button button;

    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocationListener();
    String lat, lon;
    private boolean gps = false;
    private boolean network = false;
    Geocoder geocoder;
    List<Address> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.e_latitude);
        editText2 = (EditText) findViewById(R.id.e_longtitoude);
        button = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.text);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });
        checkLocationPermission();
    }

    private boolean checkLocationPermission() {
        int location = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listpermission=new ArrayList<>();

        if (location != PackageManager.PERMISSION_GRANTED) {
listpermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (location2!=PackageManager.PERMISSION_GRANTED){
            listpermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listpermission.isEmpty()){
            ActivityCompat.requestPermissions(MainActivity.this, listpermission.toArray(new String[listpermission.size()]),1);
        }
        return true;
    }
    private void getMyLocation() {
        try {
            gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {

        }
        try {
            network = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
        }
        if (!gps && !network) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Please enable the location...");

            builder.create().show();
        }
        if (gps) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        if (network){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }
        }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location!=null){
                locationManager.removeUpdates(locationListener);
                lat=""+location.getLatitude();
                lon=""+location.getLongitude();

                editText1.setText(lat);
                editText2.setText(lon);

                geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String add=addressList.get(0).getAddressLine(0);
                textView.setText(add);
            }
        }
    }
}
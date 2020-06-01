package com.example.shake2;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class gps_location extends FragmentActivity {

    private GoogleMap mMap;
    Location mlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView maplink;
    private static final int Request_code = 101;
    public static String lat;
    public static String lon;
    double vaibhav=1.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_location);

        maplink = (TextView)findViewById(R.id.maplink);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        //String lon = Double.toString(mlocation.getLongitude());

       // maplink.setText(lat);






    }

    private void getLastLocation()
    {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null)
                {
                    mlocation = location;
                    Toast.makeText(gps_location.this, mlocation.getLatitude() + "" + mlocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                   // mapFragment.getMapAsync(gps_location.this);
                    lat = String.valueOf(mlocation.getLatitude());
                    lon = String.valueOf(mlocation.getLongitude());
                    maplink.setText("http://www.google.com/maps/place/"+lat+","+lon);
                }

            }
        });
    }


   /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng = new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("You re here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }*/
}

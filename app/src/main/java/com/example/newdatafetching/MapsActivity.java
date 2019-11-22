package com.example.newdatafetching;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Double lat, longt;
    String name;
    Marker myMarker;
    MyDatabaseHelper myDatabaseHelper;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        myDatabaseHelper = new MyDatabaseHelper(this);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        //double result = b.getDouble("key");
        name = intent.getStringExtra("name");
        lat = b.getDouble("lat");
        longt = b.getDouble("longt");
        mContext = getApplicationContext();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * There are if and else conditions for handling the null locations.
     *
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
        //System.out.println(lat+"*************vhghgvghvh**********************"+longt);
        mMap = googleMap;
        if(lat!=0.0 && longt!=0.0) {
            LatLng latLng = new LatLng(lat, longt);
            mMap.addMarker(new MarkerOptions().position(latLng).title(name)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    openDialog();
                    return false;
                }
            });
        }
        else{
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng point) {
                    //mMap.addMarker(new MarkerOptions().position(point).title("Custom location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    lat = point.latitude;
                    longt = point.longitude;
                    LatLng latLng = new LatLng(lat, longt);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    myDatabaseHelper.updateData(name,lat,longt);
                }
            });
        }
    }

    /**
     *
     * Opens the dialog to set the timer setting menu
     */

    public void openDialog(){
        TimerDialog timerDialog = new TimerDialog(getApplicationContext());
        timerDialog.show(getSupportFragmentManager(),"Time dialog");
    }

    @Override
    public void onBackPressed()
    {
        Intent myIntent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(myIntent);
        super.onBackPressed();
    }
}

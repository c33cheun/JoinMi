package com.android.joinmi.app;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.android.joinmi.app.util.AppUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener {

    // Our Map
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    //To store long and lat
    private double longitude;
    private double latitude;

    //Buttons
    private ImageButton saveButton;
    private ImageButton currentButton;
    private ImageButton viewButton;

    //Google Api Client
    private GoogleApiClient googleApiClient;

    //Last location
    private Location mLastLocation;

    //flag for saving location
    private Boolean saveEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Initializing views and adding onclick listeners
        saveButton = (ImageButton) findViewById(R.id.buttonSave);
        currentButton = (ImageButton) findViewById(R.id.buttonCurrent);
        viewButton = (ImageButton) findViewById(R.id.buttonView);
        saveButton.setOnClickListener(this);
        currentButton.setOnClickListener(this);
        viewButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        if(v == currentButton) {
            getCurrentLocation();
            moveMap();
        } else if (v == saveButton) {
            String toastText;
            int duration = Toast.LENGTH_SHORT;

            if (saveEnabled) {
                saveEnabled = false;
                toastText = "Saving points disabled!";
            } else {
                saveEnabled = true;
                toastText = "Saving points enabled!";
            }
            AppUtils.showShortToastMessage(toastText, getApplicationContext());
        } else if (v == viewButton) {
            //Clearing all the markers
            AppUtils.showShortToastMessage("Clearing saved points!", getApplicationContext());
            mMap.clear();
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        AppUtils.showShortToastMessage("No Google Maps Connection!", getApplicationContext());

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (saveEnabled) {
            //Adding a new marker to the current pressed position we are also making the draggable true
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true));
            AppUtils.showShortToastMessage("Added point!", getApplicationContext());

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Initializing our map
        mMap = googleMap;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        LatLng latLng;
        //if we can get the current location use it
        if (mLastLocation != null) {
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            // set location to waterloo
            latLng = new LatLng(43.473283, -80.531380);
        }

        //Adding marker to that coordinate
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true))
                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //Setting onMarkerDragListener to track the marker drag
        mMap.setOnMarkerDragListener(this);
        //Adding a long click listener to the map
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();

    }

    //Getting current location
    private void getCurrentLocation() {
        //Creating a location object
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
        }
    }

    //Move map to specified location
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

        //Displaying current coordinates in toast
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }
}

package com.example.quickcashg18;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcashg18.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MapsActivity opens a Google Maps interface which automatically detects the user's
 * location and allows them to specify an alternative location. Whichever location the user
 * selects using the MapsActivity interface, it is returned by MapsActivity to the previous
 * activity for processing. For example, it is useful to get the user's current location when
 * they are logging in, and this information may be stored to the user's account section
 * in the database.
 *
 * MapsActivity is an extension of the Google Maps search functionality constructed in a
 * class tutorial session.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Boolean mLocationPermissionGranted = false;

    // The location selected by the user.
    private Location selectedLocation;
    /**
     * A tag used by the activity that called MapsActivity to access the
     * location returned by MapsActivity.
     */
    public static final String LOCATION_TAG_RESULT = "Location";

    /**
     * The database key corresponding to the user's current location.
     */
    public static final String CURRENT_LOCATION = "CurrentLocation";

    private Button yesButton;
    private Button noButton;

    // Widget
    private EditText mSearchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starts");
        super.onCreate(savedInstanceState);

        ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initListeners();

        mSearchText = (EditText) findViewById(R.id.input_search);
        getLocationPermission();
        Log.d(TAG, "onCreate: Ends");

    }

    private void init() {
        setTitle("Please select the desired location");

        selectedLocation = new Location("");

        yesButton = findViewById(R.id.locationConfirmButtonYes);
        noButton = findViewById(R.id.locationConfirmButtonNo);
    }

    private void setSelectedLocation(double lat, double lon) {
        selectedLocation.setLatitude(lat);
        selectedLocation.setLongitude(lon);
    }

    private void initListeners() {
        yesButton.setOnClickListener(this::onClickYes);
        noButton.setOnClickListener(this::onClickNo);
    }

    /**
     * When the user clicks "Yes" to confirm their selected location, it is
     * returned via setResult to the activity that called MapsActivity for processing.
     */
    public void onClickYes(View view) {
        Intent locationResult = new Intent().putExtra(LOCATION_TAG_RESULT, selectedLocation);
        setResult(Activity.RESULT_OK, locationResult);
        finish();
    }

    private void onClickNo(View view) {
        makeConfirmationInvisible();
    }

    private void makeConfirmationVisible() {
        findViewById(R.id.mapConfirmationLayout).setVisibility(View.VISIBLE);
    }

    private void makeConfirmationInvisible() {
        findViewById(R.id.mapConfirmationLayout).setVisibility(View.INVISIBLE);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission : starts");
        String[] permissions = {FINE_LOCATION, COURSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                Log.d(TAG, "getLocationPermission : Permissions already granted");
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions, LOCATION_PERMISSION_REQUEST_CODE);
                Log.d(TAG, "getLocationPermission : Request for permissions");
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions, LOCATION_PERMISSION_REQUEST_CODE);
            Log.d(TAG, "getLocationPermission : Request for permissions");
        }
        Log.d(TAG, "getLocationPermission :  ends");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: Requesting for permissions");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE)
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mLocationPermissionGranted = true;
                Log.d(TAG, "onRequestPermissionsResult: permissions given by user");
                //initialize our map
                initMap();
            }
    }

    private void initMap() {
        Log.d(TAG, "initMap: starts");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG, "initMap: ends");

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
        Log.d(TAG, "onMapReady: starts");
        mMap = googleMap;
        Toast.makeText(this, "Google Map is ready", Toast.LENGTH_SHORT).show();
        if (mLocationPermissionGranted != null && mLocationPermissionGranted) {
            Log.d(TAG, "onMapReady: getting Device current location!!");
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            // Invoke Search Location service
            searchInitialize();
        }
    }

    /**
     * Get the user's current location and store it internally in case it will
     * be returned to the activity that called MapsActivity.
     */
    public void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: starts");
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted != null && mLocationPermissionGranted){
                Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "getDeviceLocation: onComplete: found location");
                        Location currentLocation = task.getResult();
                        if(currentLocation != null) {
                            Log.d(TAG, "getDeviceLocation: currentLocation Lattitude: " + currentLocation.getLatitude());
                            Log.d(TAG, "getDeviceLocation: currentLocation Longitude: " + currentLocation.getLongitude());
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,"current location");

                            // The user's current location has been determined.
                            // Check if they will use this as their selected location.
                            setSelectedLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
                            makeConfirmationVisible();
                        }else
                            Log.d(TAG, "getDeviceLocation: Current location is null");
                    }else {
                        Log.d(TAG, "getDeviceLocation: Current location is null");
                        Toast.makeText(MapsActivity.this, "Unable to get curent location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException se){
            Log.d(TAG, "getDeviceLocation: SecurityException: =" + se.getMessage());
        }
        Log.d(TAG, "getDeviceLocation: ends");
    }

    public void moveCamera(LatLng latlng, float zoom, String title){
        Log.d(TAG, "moveCamera: starts with latitude: "+ latlng.latitude + " and Longitude: " + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        MarkerOptions options = new MarkerOptions()
                .position(latlng)
                .title(title);
        mMap.addMarker(options);
        hideSoftKeyboard();
    }
    // Search Functionality starts
    private void searchInitialize(){
        Log.d(TAG, "init tsarts");

        mSearchText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER){
                // perform search
                geoLocate();
            }
            return false;
        });

    }

    private void geoLocate() {
        Log.d(TAG, "GeoLocate: starts");
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder((MapsActivity.this));
        List<Address> addressLists = new ArrayList<>();

        try {
            addressLists = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException ex){
            Log.d(TAG, "GeoLocate: exception " + ex.getMessage());
        }

        if(addressLists.size() > 0){
            Address address = addressLists.get(0);

            Log.d(TAG, "GeoLocate: Found a location" + address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),
                    DEFAULT_ZOOM,
                    address.getAddressLine(0));

            // The user has selected a location on the map.
            // Check if they wish to use this as their desired location.
            setSelectedLocation(address.getLatitude(), address.getLongitude());
            makeConfirmationVisible();
        }
        Log.d(TAG, "GeoLocate: ends");
    }

    // Search Functionality Ends

    // Check Services are working fine or not

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: Google Services is working fine");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: An error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You cannot make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void hideSoftKeyboard(){
        Log.d(TAG, "hideSoftKeyboard: called");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}

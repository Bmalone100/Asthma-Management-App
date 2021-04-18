package com.example.fypasthmaapp.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fypasthmaapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Activity for search a map object
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "MapsActivity";
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private GeoApiContext mGeoApiContext;
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();
    //default location for Limerick Ireland
    private final LatLng defaultLocation = new LatLng(-52.6638, 8.6267);
    //Default variables
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    //LatLng bounds for Ireland
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-50.999929, -10.854492), new LatLng(55.354135, 5.339355));
    //Widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;


    /**
     * Initialise processes used in this activity
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSearchText = findViewById(R.id.input_search);
        mGps = findViewById(R.id.ic_gps);

        Places.initialize(getApplicationContext(),
                getString(R.string.google_api_key),
                Locale.ENGLISH);

        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSessionToken autocompleteSessionToken = AutocompleteSessionToken.newInstance();

        PlaceAutocompleteAdapterNew mAdapter =
                new PlaceAutocompleteAdapterNew(this, placesClient, autocompleteSessionToken);

        mSearchText.setAdapter(mAdapter);

        getLocationPermission();

    }

    /**
     * Map initialising
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mMap.setOnPolylineClickListener(this::onPolylineClick);

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            //mMap.setLatLngBoundsForCameraTarget(LAT_LNG_BOUNDS);

            init();
        }
    }

    /**
     * Text listener and keyboard actions
     */
    private void init() {
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                //execute our method for searching
                geoLocate();
                calculateDirections(geoLocate(), getDeviceLocation());
            }

            return false;
        });

        mGps.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked gps icon");
            getDeviceLocation();
        });
    }


    /**
     * Locate coords and retrieve information
     */
    private Location geoLocate() {
        Location location = null;
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
            location = new Location(String.valueOf(new LatLng(address.getLatitude(), address.getLongitude())));
            Log.d(TAG, "geoLocate: location: " + location);
        }
        return location;
    }

    /**
     * Get device location, move camera here
     */
    private Location getDeviceLocation() {
        AtomicReference<Location> currentLocation = new AtomicReference<>();
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        currentLocation.set((Location) task.getResult());

                        moveCamera(new LatLng(currentLocation.get().getLatitude(), currentLocation.get().getLongitude()),
                                DEFAULT_ZOOM,
                                "My Location");

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
        return currentLocation.get();
    }


    /**
     * Move camera to coords
     */
    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.clear();

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
    }


    /**
     * Initialise map fragment
     */
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }


    /**
     * User giving maps permissions
     */
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    /**
     * Catches result of permissions access request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = false;
                        Log.d(TAG, "onRequestPermissionsResult: permission failed");
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                mLocationPermissionsGranted = true;
                //initialize our map
                initMap();
            }
        }
    }

    private void calculateDirections(Location location, Location origin) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                location.getLatitude(),
                location.getLongitude()
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        origin.getLatitude(),
                        origin.getLongitude()
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                Log.d(TAG, "onResult: successfully retrieved directions.");
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());

            }
        });
    }


    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Log.d(TAG, "run: result routes: " + result.routes.length);
            if (mPolyLinesData.size() > 0) {
                for (PolylineData polylineData : mPolyLinesData) {
                    polylineData.getPolyline().remove();
                }
                mPolyLinesData.clear();
                mPolyLinesData = new ArrayList<>();
            }

            for (DirectionsRoute route : result.routes) {
                Log.d(TAG, "run: leg: " + route.legs[0].toString());
                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                // This loops through all the LatLng coordinates of ONE polyline.
                for (com.google.maps.model.LatLng latLng : decodedPath) {

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                    newDecodedPath.add(new LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                }
                Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_grey900));
                polyline.setClickable(true);
                mPolyLinesData.add(new PolylineData(polyline, route.legs[0]));

            }
        });
    }

    public void onPolylineClick(Polyline polyline) {

        for (PolylineData polylineData : mPolyLinesData) {
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if (polyline.getId().equals(polylineData.getPolyline().getId())) {
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_googblue));
                polylineData.getPolyline().setZIndex(1);
            } else {
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.quantum_grey900));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }
}
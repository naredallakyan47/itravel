package com.example.itravel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.location.LocationRequest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private Marker yerevanMarker;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentMarker;
    private LatLng myLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private SearchView searchView;

    private List<TouristPlace> touristPlaces;

    private List<Marker> allMarkers = new ArrayList<>();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    for (android.location.Location location : locationResult.getLocations()) {
                        updateLocation(location);
                    }
                }
            }
        };

        touristPlaces = new ArrayList<>();

        addTouristPlaces();


        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void addTouristPlaces() {
        touristPlaces.add(new TouristPlace("Yerevan", new LatLng(40.1772, 44.5035), "Capital of Armenia"));
        touristPlaces.add(new TouristPlace("Gyumri", new LatLng(40.7894, 43.8478), "City with rich cultural heritage"));
        touristPlaces.add(new TouristPlace("Vanadzor", new LatLng(40.8128, 44.4880), "Third largest city in Armenia"));
        touristPlaces.add(new TouristPlace("Dilijan", new LatLng(40.7408, 44.8631), "Town in Tavush province known for its spa resorts and nature"));
        touristPlaces.add(new TouristPlace("Sevan", new LatLng(40.5519, 44.9566), "Medieval monastery complex"));
        touristPlaces.add(new TouristPlace("Tatev", new LatLng(39.375665164, 46.237332384), ""));
        touristPlaces.add(new TouristPlace("Garni", new LatLng(40.11833286 , 44.720497118), ""));
        touristPlaces.add(new TouristPlace("Geghard", new LatLng(40.155332712, 44.791330168), ""));
        touristPlaces.add(new TouristPlace("Haghpat", new LatLng(41.090332972, 44.706163842), ""));
    }






    private void handleSearch(String query) {
        boolean placeFound = false;
        for (TouristPlace place : touristPlaces) {
            if (place.getName().equalsIgnoreCase(query)) {
                LatLng location = place.getLatLng();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
                placeFound = true;
                break;
            }
        }
        if (!placeFound) {
            for (Marker marker : allMarkers) {
                if (marker.getTitle().equalsIgnoreCase(query)) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 12));
                    placeFound = true;
                    break;
                }
            }

        }
        if (!placeFound) {
            Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);

        startLocationUpdates();

        for (TouristPlace place : touristPlaces) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
            allMarkers.add(marker);
        }



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                for (TouristPlace place : touristPlaces) {
                    if (marker.getPosition().equals(place.getLatLng())) {

                        showPlaceFragment(place.getName());
                        return true;
                    }
                }
                return false;
            }
        });
    }


    private void showPlaceFragment(String placeName) {
        stopLocationUpdates();
        PlaceFragment fragment = new PlaceFragment(placeName);
        fragment.show(getSupportFragmentManager(), "place_dialog");
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    startLocationUpdates();
                }
            }
        }
    }

    public void Home(View view) {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    public void Profile(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void Info(View view) {
        Intent intent = new Intent(this, Information.class);
        startActivity(intent);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            };

            fusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, null);
        }
    }


    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void updateLocation(android.location.Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        } else {
            currentMarker.setPosition(currentLocation);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

    private void showPlaceInfo(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    private static class TouristPlace {
        private String name;
        private LatLng latLng;
        private String description;

        public TouristPlace(String name, LatLng latLng, String description) {
            this.name = name;
            this.latLng = latLng;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public LatLng getLatLng() {
            return latLng;
        }

        public String getDescription() {
            return description;
        }
    }
}
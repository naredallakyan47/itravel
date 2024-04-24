package com.example.itravel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private Marker yerevanMarker;
    private GoogleMap mMap;
    private SearchView searchView;
    private List<TouristPlace> touristPlaces;
    private DatabaseReference mDatabase;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker currentMarker;
    private LatLng myLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        touristPlaces = new ArrayList<>();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        updateLocation(location);
                    }
                }
            }
        };

        searchView = findViewById(R.id.searchView);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void addTouristPlaces() {

        touristPlaces.add(new TouristPlace("Venice, Italy", new LatLng(45.438759, 12.327145), ""));
        touristPlaces.add(new TouristPlace("Statue of Liberty, NY, USA", new LatLng(40.689247, -74.044502), ""));
        touristPlaces.add(new TouristPlace("CHRIST THE REDEEMER (STATUE), BRAZIL", new LatLng(-22.950996196, -43.206499174), ""));
        touristPlaces.add(new TouristPlace("Machu Picchu, Peru", new LatLng(-13.163068, -72.545128), ""));
        touristPlaces.add(new TouristPlace("Yerevan", new LatLng(40.1772, 44.5035), "Capital of Armenia"));
        touristPlaces.add(new TouristPlace("Gyumri", new LatLng(40.7894, 43.8478), "City with rich cultural heritage"));
        touristPlaces.add(new TouristPlace("Vanadzor", new LatLng(40.8128, 44.4880), "Third largest city in Armenia"));
        touristPlaces.add(new TouristPlace("Dilijan", new LatLng(40.7408, 44.8631), "Town in Tavush province known for its spa resorts and nature"));
        touristPlaces.add(new TouristPlace("Sevan", new LatLng(40.5519, 44.9566), "Medieval monastery complex"));
        touristPlaces.add(new TouristPlace("Tatev", new LatLng(39.375665164, 46.237332384), ""));
        touristPlaces.add(new TouristPlace("Garni", new LatLng(40.11833286 , 44.720497118), ""));
        touristPlaces.add(new TouristPlace("Geghard", new LatLng(40.155332712, 44.791330168), ""));
        touristPlaces.add(new TouristPlace("Haghpat", new LatLng(41.090332972, 44.706163842), ""));
        touristPlaces.add(new TouristPlace("Eiffel Tower, France, Paris", new LatLng(48.858093, 2.294694), ""));
        touristPlaces.add(new TouristPlace("Musée du Louvre, Paris", new LatLng(48.860294, 2.338629), ""));
        touristPlaces.add(new TouristPlace("Big Ben, London", new LatLng(51.510357, -0.116773), ""));
        touristPlaces.add(new TouristPlace("Colosseum, Italy", new LatLng(41.890251, 12.492373), ""));
        touristPlaces.add(new TouristPlace("Pisa Tower, Italy", new LatLng(43.720663784, 10.389831774), ""));
        mDatabase.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot placeSnapshot : dataSnapshot.getChildren()) {
                    String name = placeSnapshot.getKey();
                    String latitudeStr = placeSnapshot.child("Latitude").getValue(String.class);
                    String longitudeStr = placeSnapshot.child("Longitude").getValue(String.class);

                    if (name != null && latitudeStr != null && longitudeStr != null) {
                        double latitude = Double.parseDouble(latitudeStr);
                        double longitude = Double.parseDouble(longitudeStr);
                        LatLng latLng = new LatLng(latitude, longitude);

                        touristPlaces.add(new TouristPlace(name, latLng, ""));
                    }
                }

                displayMarkers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void searchLocation(String address) {
        GeocodingTask geocodingTask = new GeocodingTask();
        geocodingTask.execute(address);
    }

    private void displayMarkers() {
        if (mMap != null) {
            for (TouristPlace place : touristPlaces) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
            }
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


        addTouristPlaces();

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


        zoomToMyLocation();
    }
    private void showPlaceFragment(String placeName) {
        stopLocationUpdates();
        PlaceFragment fragment = new PlaceFragment(placeName);
        fragment.show(getSupportFragmentManager(), "place_dialog");
    }
    private void zoomToMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
                }
            }
        });
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

    private void updateLocation(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (currentMarker == null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        } else {
            currentMarker.setPosition(currentLocation);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
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

    public void like(View v) {
        Intent intent = new Intent(this, Likes.class);
        startActivity(intent);
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

    private class GeocodingTask extends AsyncTask<String, Void, LatLng> {

        @Override
        protected LatLng doInBackground(String... strings) {
            String locationName = strings[0];
            Geocoder geocoder = new Geocoder(Map.this);
            try {
                List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    return new LatLng(address.getLatitude(), address.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            if (latLng != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Log.e("GeocodingTask", "Failed to find coordinates");
                Toast.makeText(Map.this, "Failed to find location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

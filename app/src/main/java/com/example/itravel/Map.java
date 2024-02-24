package com.example.itravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng dilijanLocation = new LatLng(40.7401, 44.8622);

        mMap.addMarker(new MarkerOptions().position(dilijanLocation).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dilijanLocation));
    }

    public void Home(View view){
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
    public void Profile(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}

package com.example.itravel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private static final LatLng ARMENIA_CENTER = new LatLng(40.1792, 44.4991); // Центр Армении
    private static final float DEFAULT_ZOOM = 8.0f; // Масштаб по умолчанию

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Устанавливаем камеру на центр Армении с масштабом по умолчанию
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ARMENIA_CENTER, DEFAULT_ZOOM));

        // Добавляем маркеры туристических мест
        addTouristPlacesMarkers();
    }

    private void addTouristPlacesMarkers() {
        // Добавляем маркеры для туристических мест
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.1833, 44.5167)).title("Цитадель Эребуни"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.1792, 44.4991)).title("Ереван, столица Армении"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.1001, 44.6514)).title("Храм Гарни"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.5400, 44.7650)).title("Севанаванк, монастырь на берегу озера Севан"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(39.9578, 44.5446)).title("Гегард, средневековый монастырь"));
        // Добавьте здесь другие места, если необходимо
    }
}

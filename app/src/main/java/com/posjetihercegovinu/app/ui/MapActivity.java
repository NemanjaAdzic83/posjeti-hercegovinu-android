package com.posjetihercegovinu.app.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.posjetihercegovinu.app.R;
import com.posjetihercegovinu.app.model.Place;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private PlaceViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);


        viewModel = new ViewModelProvider(this).get(PlaceViewModel.class);

        // trazimo od fragmenta da nas obavjesti kad je mapa spremna
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (fragment != null){
            fragment.getMapAsync(this);
        }

    }

    // Poziva se kad je mapa ucitana i moze se koristiti
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Klik na oblacic(info window) iznad markera otvara detalj mjesta
        googleMap.setOnInfoWindowClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof Long){
                long id = (Long) tag;
                Intent intent = new Intent(this, PlaceDetailActivity.class);
                intent.putExtra(PlaceDetailActivity.EXTRA_PLACE_ID,id);
                startActivity(intent);
            }
        });

        // Cim lista mjesta stigne (ili se promjeni) ctamo markere
        viewModel.getPlaces().observe(this, this:: showMarkers);
    }

    private void showMarkers(List<Place> places) {

        if (googleMap == null || places == null) return;
        googleMap.clear(); // ukloni stare markere

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        int count = 0;

        // Mjesta bez koordinata preskacemo
        for (Place place : places){
            if (place.getLatitude() == null || place.getLongitude() == null) continue;

            LatLng position = new LatLng(
                    place.getLatitude().doubleValue(),
                    place.getLongitude().doubleValue()
            );

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(place.getName())
                    .snippet(place.getCategoryName()));

            if (marker != null){
                marker.setTag(place.getId()); // pamti se ID za klik
            }

            bounds.include(position);
            count++;
        }

        // Kamera - jedan marker -> zumiraj na njega; vise -> prikazi sve
        if (count == 1){
            Place p = places.get(0);
            for (Place place : places){
                if (place.getLatitude() != null && place.getLongitude() != null){
                    p = place;
                    break;
                }
            }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(p.getLatitude().doubleValue(), p.getLongitude().doubleValue()), 12f));

        }else if (count > 1){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),120));
        }

    }
}
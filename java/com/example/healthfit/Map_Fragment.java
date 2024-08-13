package com.example.healthfit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;

public class Map_Fragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.MY_MAP);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // Set the map boundaries to Shah Alam, Malaysia
        LatLngBounds shahAlamBounds = new LatLngBounds(
                new LatLng(3.0601, 101.5350), // Southwest corner
                new LatLng(3.1135, 101.5333)  // Northeast corner
        );

        // Restrict the map to show only Shah Alam area
        googleMap.setLatLngBoundsForCameraTarget(shahAlamBounds);

        // Add a marker for Vitality Fitness & Mixed Martial Arts Shah Alam
        LatLng vitalityFitness = new LatLng(3.0674, 101.4826);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(vitalityFitness)
                .title("Vitality Fitness & Mixed Martial Arts Shah Alam");
        googleMap.addMarker(markerOptions);

        // Set the initial camera position to Vitality Fitness & Mixed Martial Arts Shah Alam
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(vitalityFitness)
                .zoom(14)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}

package com.example.racegame.View.Fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.racegame.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * class MapsFragment hold google map
 */
public class MapsFragment extends Fragment {

    public final int ZOOM_VAL = 15;

    private GoogleMap map;

    /**
     * onCreateView method
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                                                    .findFragmentById(R.id.fragment_map_score);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> map = googleMap);
        return view;
    }

    /**
     * zoom method zoom on google map
     * @param latLng-LatLng
     */
    public void zoom(LatLng latLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_VAL));
        addMarkers(latLng);
    }

    /**
     * addMarkers method add marker on LatLng location
     * @param latLng-LatLng
     */
    public void addMarkers(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        map.addMarker(markerOptions);
    }
}
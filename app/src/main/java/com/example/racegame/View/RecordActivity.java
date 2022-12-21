package com.example.racegame.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.racegame.Interfaces.CallbackMaps;
import com.example.racegame.R;
import com.example.racegame.View.Fragments.ListFragment;
import com.example.racegame.View.Fragments.MapsFragment;

public class RecordActivity extends AppCompatActivity {
    private ListFragment listFragment;
    private MapsFragment mapsFragment;


    CallbackMaps callbackMaps = new CallbackMaps() {
        @Override
        public void zoomMap() {
            double latitude = 1.5;
            double longitude = 1.4;
            showUserLocation(latitude, longitude);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        listFragment = new ListFragment();
        listFragment.setCallbackMaps(callbackMaps);

        mapsFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.record_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.record_FRAME_map, mapsFragment).commit();
    }


    private void showUserLocation(double latitude, double longitude) {
        mapsFragment.zoom(latitude, longitude);
    }

}
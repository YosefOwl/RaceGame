package com.example.racegame.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.racegame.DataHandler.RaceRecords;
import com.example.racegame.DataHandler.SharePreference;
import com.example.racegame.Interfaces.MapsCallback;
import com.example.racegame.Model.StudentRacer;
import com.example.racegame.R;
import com.example.racegame.View.Fragments.ListFragment;
import com.example.racegame.View.Fragments.MapsFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

/**
 * class RecordActivity display map and record list
 */
public class RecordActivity extends AppCompatActivity{
    public static final String SP_KEY_RECORDS = "SP_KEY_RECORDS";
    public static final String KEY_LAT = "KEY_LAT";
    public static final String KEY_LNG = "KEY_LNG";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_SCORE = "KEY_SCORE";

    private RaceRecords records;
    private MapsFragment mapsFragment;

    /**
     * define a callback to connect between ListFrame to MapFrame
     */
    MapsCallback callbackMaps = (racer, position) -> {
        // SignalUser.getInstance().toast("" + racer.getName() + " has been clicked!");
        showUserLocation(racer.getLooseLocation());
    };

    /**
     * onCreate method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        records = new RaceRecords();

        loadRecords();
        initFragments();
        initButton();
        setMarkers();
    }

    /**
     * loadRecords method load all record data
     * from SP and from Intent
     * finally save data to SP.
     */
    private void loadRecords() {
        loadSP();
        getLastRaceRecord();
        saveToSP();
    }

    /**
     *  getLastRaceRecord method get record data from pre intent
     */
    private void getLastRaceRecord() {
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            records.addRecord(new StudentRacer()
                    .setScore(extras.getInt(KEY_SCORE))
                    .setName(extras.getString(KEY_NAME))
                    .setLooseLocation(new LatLng(extras.getDouble(KEY_LAT), extras.getDouble(KEY_LNG))));
        }
    }

    /**
     * setMarkers method set markers on map to all records
     */
    private void setMarkers() {

        if (records != null) {

            records.getRecordList().forEach((racer) -> {
                    //mapsFragment.addMarkers(racer.getLooseLocation());
            });
        }
    }


    /**
     * saveToSP method save all records to SP as string
     */
    private void saveToSP() {
        if (!records.getRecordList().isEmpty()) {
            String recordsAsJson = new Gson().toJson(records);
            SharePreference.getInstance().putString(SP_KEY_RECORDS, recordsAsJson);
        }
    }

    /**
     * loadSP method load saved data from SP
     */
    private void loadSP() {
        String raceRecords = SharePreference.getInstance().getString(SP_KEY_RECORDS, "");
        if(raceRecords.isEmpty()) {
            return;
        }

        records = new Gson().fromJson(raceRecords, RaceRecords.class);
    }

    /**
     * initFragments method init fragments
     */
    private void initFragments() {
        ListFragment listFragment = new ListFragment();
        listFragment.setCallbackMaps(callbackMaps);
        listFragment.setRecords(records.getRecordList());
        mapsFragment = new MapsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.record_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.record_FRAME_map, mapsFragment).commit();
    }

    /**
     * initButton method initiate all btn
     */
    private void initButton() {
        MaterialButton playGameBtn = findViewById(R.id.record_BTN_newGame);
        playGameBtn.setOnClickListener(view -> playGame());
    }

    /**
     * playGame method create Intent and move to menu Activity
     */
    private void playGame() {
        Intent menuIntent = new Intent(RecordActivity.this, MenuActivity.class);
        startActivity(menuIntent);
        finish();
    }

    /**
     * showUserLocation method display clicked user from list on map
     * @param latLng-LatLng
     */
    private void showUserLocation(LatLng latLng) {
        mapsFragment.zoom(latLng);
    }

}
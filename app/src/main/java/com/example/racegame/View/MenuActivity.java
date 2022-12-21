package com.example.racegame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.racegame.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MenuActivity extends AppCompatActivity {

    final static int LOW_SPEED_MS = 900;
    final static int HIGH_SPEED_MS = 500;

    private AppCompatImageView backgroundIMG;

    private MaterialButton recordBTN;
    private MaterialButton startGameBTN;

    private SwitchMaterial speedSWBTN;
    private SwitchMaterial controlSWBTN;

    private int speed;
    private boolean control; // true is motion control, false is button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initBackground();
        initButtons();
    }



    private void startPlayGame() {

        if (speedSWBTN.isChecked())
            speed = HIGH_SPEED_MS;
        else
            speed = LOW_SPEED_MS;

        control = controlSWBTN.isChecked();

        Intent raceIntent = new Intent(this, RaceActivity.class);

        raceIntent.putExtra(RaceActivity.KEY_SPEED, speed);
        raceIntent.putExtra(RaceActivity.KEY_CONTROL, control);
        startActivity(raceIntent);
        finish();
    }

    private void openRecordScreen() {
        Intent recordIntent = new Intent(this, RecordActivity.class);
        startActivity(recordIntent);
        finish();
    }

    /**
     * methode initBackground init background screen of game.
     * using Glide library to load IMG.
     */
    // TODO: move to .App
    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.img_menu_background)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(backgroundIMG);
    }

    private void findViews() {
        backgroundIMG = findViewById(R.id.menu_IMG_background);

        recordBTN = findViewById(R.id.menu_BTN_records);
        startGameBTN = findViewById(R.id.menu_BTN_start);

        speedSWBTN = findViewById(R.id.menu_SWBTN_speed);
        controlSWBTN = findViewById(R.id.menu_SWBTN_control);
    }

    private void initButtons() {
        recordBTN.setOnClickListener(view -> openRecordScreen());
        startGameBTN.setOnClickListener(view -> startPlayGame());
    }
}
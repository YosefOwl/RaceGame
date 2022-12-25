package com.example.racegame.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;

import com.example.racegame.R;
import com.example.racegame.Utils.ImageLoader;
import com.example.racegame.Utils.SignalUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    final static int LOW_SPEED_MS = 750;
    final static int HIGH_SPEED_MS = 500;

    private AppCompatImageView backgroundIMG;
    private MaterialButton recordBTN;
    private MaterialButton startGameBTN;
    private AppCompatEditText editName;
    private SwitchMaterial speedSWBTN;
    private SwitchMaterial controlSWBTN;

    /**
     * onCreate method create the menu Activity
     * @param savedInstanceState-Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initBackground();
        initButtons();
    }

    /**
     * startPlayGame call when start btn pushed
     * method get user input and open Race Activity with this values
     */
    private void startPlayGame() {

        String name = Objects.requireNonNull(editName.getText()).toString();

        if (name.matches("")) {
            SignalUser.getInstance().toast("You didn't enter a name");
            return;
        }

        // true is motion control, false is button
        boolean control = controlSWBTN.isChecked();
        int speed;
        if (speedSWBTN.isChecked())
            speed = HIGH_SPEED_MS;
        else
            speed = LOW_SPEED_MS;

        Intent raceIntent = new Intent(this, RaceActivity.class);

        raceIntent.putExtra(RaceActivity.KEY_SPEED, speed);
        raceIntent.putExtra(RaceActivity.KEY_CONTROL, control);
        raceIntent.putExtra(RaceActivity.KEY_NAME, editName.getText().toString());

        startActivity(raceIntent);
        finish();
    }

    /**
     * openRecordScreen method move to Records Activity
     */
    private void openRecordScreen() {
        Intent recordIntent = new Intent(this, RecordActivity.class);
        startActivity(recordIntent);
        finish();
    }

    /**
     * initBackground method load Img background
     */
    private void initBackground() {
        ImageLoader.getInstance().load(R.drawable.img_menu_background, backgroundIMG);
    }

    /**
     * findViews method find all views in this Activity
     */
    private void findViews() {
        editName = findViewById(R.id.menu_TXTV_name);
        backgroundIMG = findViewById(R.id.menu_IMG_background);
        recordBTN = findViewById(R.id.menu_BTN_records);
        startGameBTN = findViewById(R.id.menu_BTN_start);
        speedSWBTN = findViewById(R.id.menu_SWBTN_speed);
        controlSWBTN = findViewById(R.id.menu_SWBTN_control);
    }

    /**
     * initButtons method initiate buttons
     */
    private void initButtons() {
        recordBTN.setOnClickListener(view -> openRecordScreen());
        startGameBTN.setOnClickListener(view -> startPlayGame());
    }

}
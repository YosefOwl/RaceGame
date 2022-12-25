package com.example.racegame.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import com.example.racegame.Controller.RaceController;
import com.example.racegame.Interfaces.OrientationDetectorCallback;
import com.example.racegame.R;
import com.example.racegame.Utils.ImageLoader;
import com.example.racegame.Utils.OrientationDetector;
import com.example.racegame.Utils.SignalUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * class RaceActivity is activity that race occur there
 */

public class RaceActivity extends AppCompatActivity {

    public static final String KEY_SPEED = "KEY_SPEED";
    public static final String KEY_CONTROL = "KEY_CONTROL";
    public static final String KEY_NAME = "KEY_NAME";
    public final int VIBRATE_TIME_IN_MS = 200;
    public final int EXAM_OBS = 1;
    public final int COIN_OBS = 2;
    public final String TOAST_MSG = " Bad Exam!! ";

    // View objects in activity main.
    private AppCompatImageView backgroundIMG;
    private ExtendedFloatingActionButton btnFABRight;
    private ExtendedFloatingActionButton btnFABLeft;
    private MaterialTextView scoreLBL;

    // screenImgObj matrix represent all entity's that displayed on screen
    // the racer-student, the obstacles and coins.
    private ShapeableImageView[][] screenImgObj;
    private ShapeableImageView[] heartsIMG;


    private Timer timer;                // use timer for obstacles movement.
    private RaceController controller;  // controller of MVC design pattern.
    private SignalUser signal;          // signal the user on fail in game.

    private OrientationDetector orientationDetector;

    private int gameSpeed;
    private boolean isSensorControl;

    /**
     * onCreate method
     * @param savedInstanceState-Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        findViews();
        initBackground();
        initButtons();

        Intent menuIntent = getIntent();
        gameSpeed = menuIntent.getIntExtra(KEY_SPEED, MenuActivity.LOW_SPEED_MS);
        isSensorControl = menuIntent.getBooleanExtra(KEY_CONTROL, false);

        initMoveControl();

        signal = SignalUser.getInstance();
        controller = new RaceController(this, heartsIMG.length, screenImgObj.length, screenImgObj[0].length);

        controller.setRacer(menuIntent.getStringExtra(KEY_NAME));
        timer = new Timer();
        updateRacerUI();
    }

    /**
     * initMoveControl method set the moveBtn as Gone
     * when sensor control on
     */
    private void initMoveControl() {
        if (isSensorControl) {
            btnFABRight.setVisibility(View.GONE);
            btnFABLeft.setVisibility(View.GONE);
            initOrientationDetector();
        }
    }

    /**
     * initOrientationDetector method initiate the detector
     */
    private void initOrientationDetector() {
        orientationDetector = new OrientationDetector(this, new OrientationDetectorCallback() {
            @Override
            public void stepRight() {
                rightClicked();
            }

            @Override
            public void stepLeft() {
                leftClicked();
            }
        });
    }


    /**
     * onResume Override method
     *
     */
    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();
        if(isSensorControl){
            orientationDetector.start();
        }
        startGame();
    }

    /**
     * onPause method
     */
    @Override
    protected void onPause() {
        super.onPause();

        timer.cancel();
        if(isSensorControl){
            orientationDetector.stop();
        }
    }

    /**
     * startGame methode run runOnUiThread in schedule timer.
     * Time delay determine the speed of obstacles on screen.
     * in runOnUiThread calling  moveObstacles() updateUI() methods.
     */
    private void startGame() {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    controller.moveObstacles();
                    updateUI();
                });
            }
        }, gameSpeed, gameSpeed);
    }

    /**
     * updateUI methode run on array of view object
     * that shall be display on screen.
     * call to updateRacerUI and updateObstacleUI.
     * finally call to checkCrash and checkGameState via controller.
     */
    private void updateUI() {
        int obstacleType;

        for (int i = 0; i < screenImgObj.length; i++) {
            for (int j = 0; j < screenImgObj[0].length; j++) {
                obstacleType = controller.getObstacleType(i, j);

                if (i == screenImgObj.length - 2 && j == controller.getRacerPos()) {
                    controller.checkCrash();      // check coin or exam crashes
                    controller.isGameEnded();    // check if game ended
                } else {
                    updateObstacleUI(screenImgObj[i][j], obstacleType);
                }
            }
        }
    }

    /**
     * methode updateObstacleUI set resource and visibility of obstacle depend on type.
     * @param imageView-ShapeableImageView.
     * @param obstacleType-int, 1 = exam obstacle type, 2=coin obstacle type.
     */
    private void updateObstacleUI(ShapeableImageView imageView, int obstacleType) {

        if (obstacleType == EXAM_OBS)
            imageView.setImageResource(R.drawable.exam);
        else if (obstacleType == COIN_OBS)
            imageView.setImageResource(R.drawable.grade);
        else
            imageView.setImageResource(0);

        imageView.setVisibility(View.VISIBLE);
    }

    /**
     * methode updateRacerUI set resource and visibility of racer.
     */
    private void updateRacerUI() {
        ShapeableImageView imageView;

        imageView = screenImgObj[screenImgObj.length - 2][controller.getRacerPos()];
        imageView.setImageResource(R.drawable.student);
        imageView.setVisibility(View.VISIBLE);

        controller.checkCrash();      // check coin or exam crashes
        controller.isGameEnded();    // check if game ended
    }

    /**
     * obstacleCrash methode display toast msg vibrate and play sound.
     */
    public void obstacleCrash() {

        signal.playSound(this, R.raw.examcrashsound);
        signal.toast(TOAST_MSG);
        signal.vibrate(VIBRATE_TIME_IN_MS);
        if (controller.getCrashes() <= heartsIMG.length)
            heartsIMG[heartsIMG.length - controller.getCrashes()].setVisibility(View.INVISIBLE);
    }


    /**
     * leftClicked methode update controller on clicked left button
     * and call updateUI.
     */
    private void leftClicked() {
        screenImgObj[screenImgObj.length - 2][controller.getRacerPos()].setImageResource(0);
        controller.onMoveLeft();
        updateRacerUI();
    }

    /**
     * rightClicked methode update controller on clicked right button
     * and call updateUI.
     */
    private void rightClicked() {
        screenImgObj[screenImgObj.length - 2][controller.getRacerPos()].setImageResource(0);
        controller.onMoveRight();
        updateRacerUI();
    }

    /**
     * initBackground methode load IMG background
     */
    private void initBackground() {
        ImageLoader.getInstance().load(R.drawable.img_board_background, backgroundIMG);
    }

    /**
     * findViews methode init all activity obj by findViewById(R.id.some_id);.
     */
    private void findViews() {

        backgroundIMG = findViewById(R.id.game_IMG_background);

        scoreLBL = findViewById(R.id.game_LBL_score);
        scoreLBL.setText(R.string.game_LBL_score_atStart);
        btnFABLeft = findViewById(R.id.game_FAB_left);
        btnFABRight = findViewById(R.id.game_FAB_right);

        screenImgObj = new ShapeableImageView[][]{{findViewById(R.id.game_IMG_Obstacle1), findViewById(R.id.game_IMG_Obstacle2), findViewById(R.id.game_IMG_Obstacle3), findViewById(R.id.game_IMG_Obstacle4), findViewById(R.id.game_IMG_Obstacle5)},

                {findViewById(R.id.game_IMG_Obstacle6), findViewById(R.id.game_IMG_Obstacle7), findViewById(R.id.game_IMG_Obstacle8), findViewById(R.id.game_IMG_Obstacle9), findViewById(R.id.game_IMG_Obstacle10)},

                {findViewById(R.id.game_IMG_Obstacle11), findViewById(R.id.game_IMG_Obstacle12), findViewById(R.id.game_IMG_Obstacle13), findViewById(R.id.game_IMG_Obstacle14), findViewById(R.id.game_IMG_Obstacle15)},

                {findViewById(R.id.game_IMG_Obstacle16), findViewById(R.id.game_IMG_Obstacle17), findViewById(R.id.game_IMG_Obstacle18), findViewById(R.id.game_IMG_Obstacle19), findViewById(R.id.game_IMG_Obstacle20)},

                {findViewById(R.id.game_IMG_Obstacle21), findViewById(R.id.game_IMG_Obstacle22), findViewById(R.id.game_IMG_Obstacle23), findViewById(R.id.game_IMG_Obstacle24), findViewById(R.id.game_IMG_Obstacle25)}, {findViewById(R.id.game_IMG_Obstacle26), findViewById(R.id.game_IMG_Obstacle27), findViewById(R.id.game_IMG_Obstacle28), findViewById(R.id.game_IMG_Obstacle29), findViewById(R.id.game_IMG_Obstacle30)}, {findViewById(R.id.game_IMG_Obstacle31), findViewById(R.id.game_IMG_Obstacle32), findViewById(R.id.game_IMG_Obstacle33), findViewById(R.id.game_IMG_Obstacle34), findViewById(R.id.game_IMG_Obstacle35)}, {findViewById(R.id.game_IMG_Obstacle36), findViewById(R.id.game_IMG_Obstacle37), findViewById(R.id.game_IMG_Obstacle38), findViewById(R.id.game_IMG_Obstacle39), findViewById(R.id.game_IMG_Obstacle40)}, {findViewById(R.id.game_IMG_Obstacle41), findViewById(R.id.game_IMG_Obstacle42), findViewById(R.id.game_IMG_Obstacle43), findViewById(R.id.game_IMG_Obstacle44), findViewById(R.id.game_IMG_Obstacle45)}};

        heartsIMG = new ShapeableImageView[]{findViewById(R.id.game_IMG_heart1), findViewById(R.id.game_IMG_heart2), findViewById(R.id.game_IMG_heart3)};
    }

    /**
     * initButtons methode init setOnClickListener for entire buttons.
     */
    private void initButtons() {
        btnFABLeft.setOnClickListener(view -> leftClicked());
        btnFABRight.setOnClickListener(view -> rightClicked());
    }


    /**
     * coinCrash methode display play sound and set score on screen.
     */
    public void coinCrash() {
        scoreLBL.setText("" + controller.getScores());
        signal.playSound(this, R.raw.coincrashsound);
    }

    /**
     * gameLoos method create Intent and move to Record Activity
     */
    public void gameLoos() {

        Intent recordIntent = new Intent(this, RecordActivity.class);

        recordIntent.putExtra(RecordActivity.KEY_NAME, controller.getRacer().getName());
        recordIntent.putExtra(RecordActivity.KEY_SCORE, controller.getRacer().getScore());
        recordIntent.putExtra(RecordActivity.KEY_LAT, controller.getRacer().getLooseLocation().latitude);
        recordIntent.putExtra(RecordActivity.KEY_LNG, controller.getRacer().getLooseLocation().longitude);

        startActivity(recordIntent);
        finish();
    }


    /**
     * gameEnded method stop timer and request current location
     */
    public void gameEnded() {
        timer.cancel();
        setLocationProvider();
    }

    /**
     * setLocation method send location to controller
     * @param lat-double
     * @param lng-double
     */
    private void setLocation(double lat, double lng) {
        controller.gameEnded(new LatLng(lat, lng));
    }

    /**
     *  setLocationProvider method get current location
     */
    private void setLocationProvider() {
        FusedLocationProviderClient fusedLocationProviderClient;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            setLocation(32.100, 34.100); // set default location
        }
        else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null)
                    setLocation(location.getLatitude(), location.getLongitude());
            });
        }
    }

}
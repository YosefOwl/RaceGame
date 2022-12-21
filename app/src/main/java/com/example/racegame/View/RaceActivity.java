package com.example.racegame.View;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.racegame.Controller.RaceController;
import com.example.racegame.R;
import com.example.racegame.Utils.BackgroundSound;
import com.example.racegame.Utils.SignalUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Game description :
 * A student-racer need to runaway from exams-obstacles by buttons (Left / Right).
 * game include 3 roads.
 * game start with a 3 life.
 * on crash app vibrate, toast and reduce life.
 * for now the game is endless (even if life finished).
 *
 */

public class RaceActivity extends AppCompatActivity {

    public static final String KEY_SPEED = "KEY_SPEED";
    public static final String KEY_CONTROL = "KEY_CONTROL";

    private final int VIBRATE_TIME_IN_MS = 200;

    final private int EMPTY_OBS = 0;//TODO
    final private int EXAM_OBS = 1;
    final private int COIN_OBS = 2;

    final private String TOAST_MSG = " Bad Exam!! ";

    // View objects in activity main.
    private AppCompatImageView backgroundIMG;
    //private MaterialButton btnStart; // TODO may need for continue option
    private ExtendedFloatingActionButton btnFABRight;
    private ExtendedFloatingActionButton btnFABLeft;
    private MaterialTextView scoreLBL;

    // screenImgObj matrix represent all entity's that displayed on screen
    // the racer-student, the obstacles and coins.
    private ShapeableImageView[][] screenImgObj;
    private ShapeableImageView[] heartsIMG;

    private int gameSpeed;
    private boolean controlFlag;

    // use timer for obstacles movement.
    private Timer timer;
    // controller of MVC design pattern.
    private RaceController controller;
    // signal the user on fail in game.
    private SignalUser signal;

    //TODO
    private Intent soundIntent;
    private Intent menuIntent;
    private Intent recordIntent;

    /**
     * onCreate method set the activity_game init ..... TODO
     * @param savedInstanceState-Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        findViews();
        initBackground();
        initButtons();

        initIntents();

        signal = SignalUser.getInstance(this);
        controller = new RaceController(this, heartsIMG.length,
                screenImgObj.length, screenImgObj[0].length, "Yosef"); //TODO name

        updateRacerUI();

        startGame();
    }

    //TODO
    private void initIntents() {
        menuIntent = getIntent();
        gameSpeed = menuIntent.getIntExtra(KEY_SPEED, MenuActivity.LOW_SPEED_MS);
        controlFlag = menuIntent.getBooleanExtra(KEY_CONTROL, false);

        soundIntent = new Intent(RaceActivity.this, BackgroundSound.class);

        recordIntent = new Intent(RaceActivity.this, RecordActivity.class);
    }


    /**
     * onResume Override method TODO
     */
    @Override
    protected void onResume() {
        super.onResume();

        playBackgroundSound();
        startGame();
    }

    /**
     * onPause method TODO
     */
    @Override
    protected void onPause() {
        super.onPause();

        timer.cancel();
        stopService(soundIntent);

        // TODO: save data to SharePreferences
    }

    /**
     * startGame methode run runOnUiThread in schedule timer.
     * Time delay determine the speed of obstacles on screen.
     * in runOnUiThread calling  moveObstacles() updateUI() and checkCrash() methods.
     */
    private void startGame() {
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    controller.moveObstacles();
                    updateUI();
                    controller.checkCrash();        // check coin or exam crashes
                    controller.isGameEnded();    // check if game ended
                });
            }
        }, gameSpeed,gameSpeed);
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
                    updateRacerUI();
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

        if (obstacleType == 0) {
            imageView.setVisibility(View.INVISIBLE);
            return;
        }

        if (obstacleType == EXAM_OBS) {
            imageView.setImageResource(R.drawable.exam);
        }

        if (obstacleType == COIN_OBS)
            imageView.setImageResource(R.drawable.grade);

        imageView.setVisibility(View.VISIBLE);
    }

    /**
     * methode updateRacerUI set resource and visibility of racer.
     */
    private void updateRacerUI() {
        ShapeableImageView imageView;

        imageView = screenImgObj[screenImgObj.length-2][controller.getRacerPos()];
        imageView.setImageResource(R.drawable.student);
        imageView.setVisibility(View.VISIBLE);
    }

    /**
     * obstacleCrash methode display toast msg and vibrate.
     */
    public void obstacleCrash() {

        signal.toast(TOAST_MSG);
        signal.vibrate(VIBRATE_TIME_IN_MS);

        if (controller.getCrashes() <= heartsIMG.length)
            heartsIMG[heartsIMG.length - controller.getCrashes()].setVisibility(View.INVISIBLE);

        signal.playSound(this, R.raw.examcrashsound);
    }



    /**
     * leftClicked methode update controller on clicked left button
     * and call updateUI.
     */
    private void leftClicked() {
        screenImgObj[screenImgObj.length-2][controller.getRacerPos()].setImageResource(0);
        //screenImgObj[screenImgObj.length-2][controller.getRacerPos()].setVisibility(View.INVISIBLE);
        controller.onMoveLeft();
        updateRacerUI();
    }

    /**
     * rightClicked methode update controller on clicked right button
     * and call updateUI.
     */
    private void rightClicked() {
        screenImgObj[screenImgObj.length-2][controller.getRacerPos()].setImageResource(0);

//        screenImgObj[screenImgObj.length-2][controller.getRacerPos()].setVisibility(View.INVISIBLE);
        controller.onMoveRight();
        updateRacerUI();

    }

    /**
     * methode initBackground init background screen of game.
     * using Glide library to load IMG.
     */
    private void initBackground() {
        Glide
                .with(this)
                .load(R.drawable.img_board_background)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(backgroundIMG);
    }

    /**
     * findViews methode init all activity obj by findViewById(R.id.some_id);.
     */
    private void findViews() {

        backgroundIMG = findViewById(R.id.game_IMG_background);

        scoreLBL = findViewById(R.id.game_LBL_score);
        btnFABLeft = findViewById(R.id.game_FAB_left);
        btnFABRight = findViewById(R.id.game_FAB_right);


        screenImgObj = new ShapeableImageView[][]{
                {findViewById(R.id.game_IMG_Obstacle1), findViewById(R.id.game_IMG_Obstacle2), findViewById(R.id.game_IMG_Obstacle3),
                 findViewById(R.id.game_IMG_Obstacle4), findViewById(R.id.game_IMG_Obstacle5)},

                {findViewById(R.id.game_IMG_Obstacle6), findViewById(R.id.game_IMG_Obstacle7), findViewById(R.id.game_IMG_Obstacle8),
                 findViewById(R.id.game_IMG_Obstacle9) , findViewById(R.id.game_IMG_Obstacle10)},

                {findViewById(R.id.game_IMG_Obstacle11), findViewById(R.id.game_IMG_Obstacle12),
                 findViewById(R.id.game_IMG_Obstacle13), findViewById(R.id.game_IMG_Obstacle14), findViewById(R.id.game_IMG_Obstacle15)},

                {findViewById(R.id.game_IMG_Obstacle16), findViewById(R.id.game_IMG_Obstacle17), findViewById(R.id.game_IMG_Obstacle18),
                findViewById(R.id.game_IMG_Obstacle19), findViewById(R.id.game_IMG_Obstacle20)},

                {findViewById(R.id.game_IMG_Obstacle21), findViewById(R.id.game_IMG_Obstacle22), findViewById(R.id.game_IMG_Obstacle23),
                        findViewById(R.id.game_IMG_Obstacle24), findViewById(R.id.game_IMG_Obstacle25)},
                {findViewById(R.id.game_IMG_Obstacle26), findViewById(R.id.game_IMG_Obstacle27), findViewById(R.id.game_IMG_Obstacle28),
                        findViewById(R.id.game_IMG_Obstacle29), findViewById(R.id.game_IMG_Obstacle30)},
                {findViewById(R.id.game_IMG_Obstacle31), findViewById(R.id.game_IMG_Obstacle32), findViewById(R.id.game_IMG_Obstacle33),
                        findViewById(R.id.game_IMG_Obstacle34), findViewById(R.id.game_IMG_Obstacle35)},
                {findViewById(R.id.game_IMG_Obstacle36), findViewById(R.id.game_IMG_Obstacle37), findViewById(R.id.game_IMG_Obstacle38),
                        findViewById(R.id.game_IMG_Obstacle39), findViewById(R.id.game_IMG_Obstacle40)},
                {findViewById(R.id.game_IMG_Obstacle41), findViewById(R.id.game_IMG_Obstacle42), findViewById(R.id.game_IMG_Obstacle43),
                        findViewById(R.id.game_IMG_Obstacle44), findViewById(R.id.game_IMG_Obstacle45)}
        };

        heartsIMG = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
    }

    /**
     * initButtons methode init setOnClickListener for entire buttons.
     */
    private void initButtons() {
        btnFABLeft.setOnClickListener(view -> leftClicked());
        btnFABRight.setOnClickListener(view -> rightClicked());
    }


    /**
     * coinCrash methode
     */
    public void coinCrash() {
        scoreLBL.setText("" + controller.getScores());
        signal.playSound(this, R.raw.coincrashsound);
    }


    // TODO
    public void playBackgroundSound() {
        startService(soundIntent);
    }

    protected LocationManager locationManager;
    // TODO
    public void gameEnded() {


        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // gameOver();  TODO: display screen of GameOver
        // TODO: load data from SharePreferences
        // TODO: send data to recordACTIVITY
        timer.cancel();
        stopService(soundIntent);
        startActivity(recordIntent);
        finish();
    }

}
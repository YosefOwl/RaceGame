package com.example.racegame.View;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.racegame.Controller.RaceController;
import com.example.racegame.R;
import com.example.racegame.Utils.SignalUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

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

    final static int VIBRATE_TIME_IN_MS = 200;
    final static int SPEED_IN_MS = 750;
    final static String TOAST_MSG = " Bad Exam!! ";


    // View objects in activity main.
    private AppCompatImageView backgroundIMG;
    private MaterialButton btnStart;
    private ExtendedFloatingActionButton btnFABRight;
    private ExtendedFloatingActionButton btnFABLeft;

    // screenImgObj matrix represent all entity's that displayed on screen
    // the racer-student, the obstacles and coins.
    private ShapeableImageView[][] screenImgObj;
    private ShapeableImageView[] heartsIMG;

    boolean startGame = false;
    private int gameSpeed;

    // use timer for obstacles movement.
    private Timer timer;
    // controller of MVC design pattern.
    private RaceController controller;
    // signal the user on fail in game.
    private SignalUser signal;

    /**
     * onCreate method set the activity_game init view obj, Timer,
     * GameController, and NotificationSignal.
     * @param savedInstanceState-Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        findViews();
        initBackground();
        initButtons();
        gameSpeed = SPEED_IN_MS;
        signal = SignalUser.getInstance(this);
        timer = new Timer();
        controller = new RaceController(this, heartsIMG.length,
                screenImgObj.length, screenImgObj[0].length);

        updateRacerUI(screenImgObj[screenImgObj.length - 2][controller.getRacerPos()]);
    }

    /**
     * onStop Override method stop the timer
     */
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    /**
     * onResume Override method init new timer
     * and init buttons.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (startGame)
            btnStart.setText("CONTINUE");

        btnStart.setVisibility(View.VISIBLE);
        btnFABLeft.setEnabled(false);
        btnFABRight.setEnabled(false);
        timer = new Timer();
    }

    /**
     * onPause method do nothing right now
     */
    @Override
    protected void onPause() {
        super.onPause();
        // TODO: save data to SharePreferences
    }

    /**
     * startGame methode run runOnUiThread in schedule timer.
     * Time delay determine the speed of obstacles on screen.
     * in runOnUiThread calling  moveObstacles(); and updateUI(); methods.
     */
    private void startGame() {
        btnStart.setVisibility(View.GONE);
        btnFABLeft.setEnabled(true);
        btnFABRight.setEnabled(true);
        startGame = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    controller.moveObstacles();
                    updateUI();
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
        controller.checkCrash();     // check coin or exam crashes

        for (int i = 0; i < screenImgObj.length; i++) {
            for (int j = 0; j < screenImgObj[0].length; j++) {
                obstacleType = controller.getObstacleType(i, j);
                Log.d("TYPE", "screenImgObj[i][j]" +"["+i+"]"+"["+j+"] --> "+"type" + obstacleType);
                if (i == screenImgObj.length - 2 && j == controller.getRacerPos()) {
                    updateRacerUI(screenImgObj[i][j]);
                } else {
                    updateObstacleUI(screenImgObj[i][j], obstacleType);
                }
            }
        }
        // for next episode : controller.checkState(); // check game ended or loos
    }

    /**
     * methode updateObstacleUI set resource and visibility of obstacle depend on type.
     * @param imageView-ShapeableImageView.
     * @param obstacleType-int, 1 = exam obstacle type, 2=coin obstacle type.
     */
    private void updateObstacleUI(ShapeableImageView imageView, int obstacleType) {
        imageView.setImageResource(R.drawable.exam);

        // TODO: in next exercise should set if for obstacle type.
        if (obstacleType != 0)
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.INVISIBLE);
    }

    /**
     * methode updateRacerUI set resource and visibility of racer.
     * @param imageView-ShapeableImageView
     */
    private void updateRacerUI(ShapeableImageView imageView) {
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
    }

    /**
     * leftClicked methode update controller on clicked left button
     * and call updateUI.
     */
    private void leftClicked() {
        controller.onMoveLeft();
        updateUI();
    }

    /**
     * rightClicked methode update controller on clicked right button
     * and call updateUI.
     */
    private void rightClicked() {
        controller.onMoveRight();
        updateUI();
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

        backgroundIMG = findViewById(R.id.class_IMG_background);

        btnFABLeft = findViewById(R.id.game_FAB_left);
        btnFABRight = findViewById(R.id.game_FAB_right);
        btnStart = findViewById(R.id.game_BTN_start);

        screenImgObj = new ShapeableImageView[][]{
                {findViewById(R.id.game_IMG_Obstacle1), findViewById(R.id.game_IMG_Obstacle2), findViewById(R.id.game_IMG_Obstacle3)},
                {findViewById(R.id.game_IMG_Obstacle4), findViewById(R.id.game_IMG_Obstacle5), findViewById(R.id.game_IMG_Obstacle6)},
                {findViewById(R.id.game_IMG_Obstacle7), findViewById(R.id.game_IMG_Obstacle8), findViewById(R.id.game_IMG_Obstacle9)},
                {findViewById(R.id.game_IMG_Obstacle10), findViewById(R.id.game_IMG_Obstacle11), findViewById(R.id.game_IMG_Obstacle12)},
                {findViewById(R.id.game_IMG_Obstacle13), findViewById(R.id.game_IMG_Obstacle14), findViewById(R.id.game_IMG_Obstacle15)},
                {findViewById(R.id.game_IMG_Obstacle16), findViewById(R.id.game_IMG_Obstacle17), findViewById(R.id.game_IMG_Obstacle18)},
                {findViewById(R.id.game_IMG_Obstacle19), findViewById(R.id.game_IMG_Obstacle20), findViewById(R.id.game_IMG_Obstacle21)},
                {findViewById(R.id.game_IMG_Obstacle22), findViewById(R.id.game_IMG_Obstacle23), findViewById(R.id.game_IMG_Obstacle24)}
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
        btnStart.setOnClickListener(view -> startGame());
        btnStart.setText("START");
        btnFABLeft.setEnabled(false);
        btnFABRight.setEnabled(false);
    }

    //region Description : methods not using for now, for next episode
    /**
     * coinCrash methode
     */
    public void coinCrash() {
        // TODO:
    }

    /**
     * stopGame methode
     */
    public void stopGame(){
        // TODO:
    }

    /**
     * gameLoos methode
     */
    public void gameLoos() {
        stopGame();
        // TODO:
    }

    /**
     * gameWin methode
     */
    public void gameWin(){
        stopGame();
        // TODO:
    }
    //endregion

}
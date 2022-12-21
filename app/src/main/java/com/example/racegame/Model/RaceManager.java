package com.example.racegame.Model;

import android.util.Log;

import com.example.racegame.Controller.RaceController;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Random;

public class RaceManager {

    private static  RaceManager gameManager = null;

    private int[][] obstaclesState; // represent all types of obstacles, 0=empty, 1=exam, 2=coin
    private final StudentRacer racer;
    private int crashes = 0;
    private final int life;
    private final RaceController controller;

    /**
     * GameManager constructor
     * @param controller-GameController
     * @param life-int
     * @param rows-int
     * @param columns-int
     * @param racerName-String
     */

    private RaceManager(RaceController controller, int life, int rows, int columns, String racerName) {
        this.life = life;
        this.controller = controller;
        obstaclesState = new int[rows][columns];
        this.racer = new StudentRacer().setPosition(columns / 2).setName(racerName).setScore(0);
    }

    /**
     * getInstance static method , implement singleton design pattern
     * @param controller-GameController
     * @param life-int
     * @param rows-int
     * @param columns-int
     * @param racerName-String
     * @return GameManager
     */
    public static RaceManager getInstance(RaceController controller, int life, int rows,
                                          int columns, String racerName) {
        if (gameManager == null)
            gameManager =  new RaceManager(controller, life, rows, columns, racerName);
        return gameManager;
    }

    /**
     * rollNewObstacle methode roll a position and obstacles (Exam=1 or Coin=2)
     * that coming on screen
     */
    private void rollNewObstacle() {
        int roadPos, obstacle, bound;
        Random rand = new Random();

        // zero first row before fill with rand value
        Arrays.fill(obstaclesState[0], 0);

        // 1 row space between obstacles, loop check the next row for that.
        for (int i = 0; i < obstaclesState[0].length; i++) {
            if (obstaclesState[1][i] == 1)
                return;
        }

        bound =  obstaclesState[0].length;

        // set at obstacles (for now only 1=exam or 0=empty ) and position by rand
        // obstacles roll at most  firstObstacleRow.length - 1, possibility to escape
        for (int j = 0; j < obstaclesState[0].length -1 ; j++) {
            roadPos = rand.nextInt(bound);
            obstacle = rand.nextInt(bound);
            obstaclesState[0][roadPos] = obstacle;
        }
    }

    /**
     * setObstaclesPosition method down up the positions of
     * obstacles 1 row down.
     * finally call rollNewObstacle method.
     */
    public void setObstaclesPosition() {

        int[] previous;
        if (obstaclesState.length < 2)
            return;

        // move obstacles 1 row down.
        for (int i = obstaclesState.length - 1; i > 0; i--) {
            previous = Arrays.copyOf(obstaclesState[i-1], obstaclesState[i-1].length);
            obstaclesState[i] = Arrays.copyOf(previous, previous.length);
        }
        rollNewObstacle(); // roll new values to first row.
    }

    /**
     * checkCrashesOccur method check if any crash occur
     * if so call to controller and set a value (crashes++ \ set new score)
     */
    public void  checkCrashesOccur() {

        if (isCoinCrash()) {

            obstaclesState[obstaclesState.length - 2][racer.getPosition()] = 0;
            racer.setScore(racer.getScore() + 1);
            controller.onCoinCrash();
        }

        if (isObstacleCrash()) {
            obstaclesState[obstaclesState.length - 2][racer.getPosition()] = 0;
            crashes++;
            controller.onObstacleCrash();
        }
    }

    /**
     * getType method return type according position
     * @param row-int
     * @param col-int
     * @return obstacle type-int
     */
    public int getType(int row, int col) {
        if (row >= obstaclesState.length || col >= obstaclesState[0].length)
            return 0;
        return obstaclesState[row][col];
    }

    /**
     * isObstacleCrash method return true if racer crash on exam.
     * coin represent by 1.
     * @return boolean
     */
    public boolean isObstacleCrash() {
        // obstaclesState[obstaclesState.length - 2] is 1 line before bottom
        return (obstaclesState[obstaclesState.length - 2][racer.getPosition()] == 1);
    }


    /**
     * moveRacerRight method set the position of racer
     * on move right
     */
    public void moveRacerRight() {
        if (racer.getPosition() < obstaclesState[0].length - 1)
            racer.setPosition(racer.getPosition() + 1);
    }

    /**
     * moveRacerLeft method set the position of racer
     * on move left
     */
    public void moveRacerLeft() {
        if (racer.getPosition() > 0)
            racer.setPosition(racer.getPosition() - 1);
    }

    /**
     * getRacerPos method
     * @return racerPosition-int
     */
    public int getRacerPos() { return racer.getPosition(); }

    /**
     * getCrashes method
     * @return crashes-int
     */
    public int getCrashes() { return crashes; }

    /**
     * isLose methode return true if racer loos all life
     * @return boolean
     */
    public boolean isLose(){ return life == crashes; }

    /**
     * isCoinCrash method return true if racer crash on coin.
     * coin represent by 2.
     * @return boolean
     */
    public boolean isCoinCrash() {
        // obstaclesState[obstaclesState.length - 2] is 1 line from bottom
        return obstaclesState[obstaclesState.length - 2][racer.getPosition()] == 2;
    }

    /**
     * checkGameState method TODO
     */
    public void  checkGameState() {

        if (isLose()) {
            // TODO:  get location
            racer.setLoseLocation(new LatLng(0.0, 5.5));
            controller.onLoos();
        }
    }

    //TODO
    public int getScore() {
        return racer.getScore();
    }
}
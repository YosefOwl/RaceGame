package com.example.racegame.Model;

import com.example.racegame.Controller.RaceController;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Random;

/**
 * class RaceManager is a BL in this game
 */
public class RaceManager {

    private static  RaceManager instance = null;

    private int[][] obstaclesState; // represent all types of obstacles, 0=empty, 1=exam, 2=coin
    private StudentRacer racer;
    private int racerPosition;
    private int crashes = 0;
    private final int life;
    private final RaceController controller;

    /**
     * GameManager constructor
     * @param controller-GameController
     * @param life-int
     * @param rows-int
     * @param columns-int
     */
    private RaceManager(RaceController controller, int life, int rows, int columns) {
        this.life = life;
        this.controller = controller;
        obstaclesState = new int[rows][columns];
        racerPosition = columns / 2 ;
    }

    /**
     * initInstance static method , initiate instance as singleton design pattern
     * @param controller-GameController
     * @param life-int
     * @param rows-int
     * @param columns-int
     */
    public static void initInstance(RaceController controller, int life, int rows, int columns) {
        if (instance == null)
            instance =  new RaceManager(controller, life, rows, columns);
    }


    /**
     * getInstance method return instance
     * @return instance-RaceManager
     */
    public static RaceManager getInstance(){
        return instance;
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

        if (isObstacleCrash()) {
            obstaclesState[obstaclesState.length - 2][racerPosition] = 0;
            crashes++;
            controller.onObstacleCrash();
        }
        else if (isCoinCrash()) {
            obstaclesState[obstaclesState.length - 2][racerPosition] = 0;
            racer.setScore(racer.getScore() + 1);
            controller.onCoinCrash();
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
        return obstaclesState[obstaclesState.length - 2][racerPosition] == 1;
    }


    /**
     * moveRacerRight method set the position of racer
     * on move right
     */
    public void moveRacerRight() {
        if (racerPosition < obstaclesState[0].length - 1)
            racerPosition++;
    }

    /**
     * moveRacerLeft method set the position of racer
     * on move left
     */
    public void moveRacerLeft() {
        if (racerPosition > 0)
            racerPosition--;
    }

    /**
     * getRacerPos method
     * @return racerPosition-int
     */
    public int getRacerPos() { return racerPosition; }

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
        return obstaclesState[obstaclesState.length - 2][racerPosition] == 2;
    }

    /**
     * checkGameState method check if game ended and call to controller
     */
    public void  checkGameState() {
        if (isLose()) {
            controller.endGame();
            crashes = 0;
            obstaclesState = new int[obstaclesState.length][obstaclesState[0].length];
            racerPosition = obstaclesState[0].length / 2;
        }
    }

    /**
     * gameEnded method call when game is ended and got a location loose
     * method set the racer location and call to onLoos() via controller
     * @param latLng-LatLng
     */
    public void gameEnded (LatLng latLng){
        racer.setLooseLocation(latLng);
        controller.onLoos();
    }

    /**
     * getRacer method
     * @return racer-StudentRacer
     */
    public StudentRacer getRacer(){
        return racer;
    }

    /**
     * getScore method
     * @return racerScore-int
     */
    public int getScore() {
        return racer.getScore();
    }

    /**
     * setRacer method used for new game (singleton issue )
     * @param name-String
     */
    public void setRacer(String name) {
        racer = new StudentRacer().setName(name).setScore(0);
    }

}
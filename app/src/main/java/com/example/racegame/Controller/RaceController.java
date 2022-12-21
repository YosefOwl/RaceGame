package com.example.racegame.Controller;


import com.example.racegame.Model.RaceManager;
import com.example.racegame.View.RaceActivity;

/**
 * class RaceController  is a controller in this game.
 * controller in MVC design pattern.
 * the controller knows the model, view and connect between them.
 * GameController do anything logic or draw view, only call view \ model methods.
 */

public class RaceController {

    private final RaceManager model;
    private final RaceActivity view;

    public RaceController(RaceActivity raceActivity, int life, int rows, int columns, String name) {
        this.view = raceActivity;
        this.model = RaceManager.getInstance(this, life, rows, columns, name);
    }

    public int getObstacleType(int row, int col){ return model.getType(row, col); }

    public void onCoinCrash(){ view.coinCrash(); }

    public void onObstacleCrash(){ view.obstacleCrash(); }

    public void onLoos(){ view.gameEnded(); }

    public void checkCrash() { model.checkCrashesOccur(); }

    public int getCrashes() { return model.getCrashes(); }

    public int getRacerPos() { return model.getRacerPos(); }

    public void moveObstacles() { model.setObstaclesPosition(); }

    public void onMoveRight(){ model.moveRacerRight(); }

    public void onMoveLeft(){ model.moveRacerLeft(); }

    public int getScores() { return model.getScore(); }

    public void isGameEnded() { model.checkGameState(); }
}

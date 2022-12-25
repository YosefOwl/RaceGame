package com.example.racegame.Controller;


import com.example.racegame.Model.RaceManager;
import com.example.racegame.Model.StudentRacer;
import com.example.racegame.View.RaceActivity;
import com.google.android.gms.maps.model.LatLng;

/**
 * class RaceController  is a controller in this game.
 * controller in MVC design pattern.
 * the controller knows the model, view and connect between them.
 * GameController do anything logic or draw view, only call view \ model methods.
 */

public class RaceController {

    private final RaceManager model;
    private final RaceActivity view;

    public RaceController(RaceActivity raceActivity, int life, int rows, int columns) {
        this.view = raceActivity;
        RaceManager.initInstance(this, life, rows, columns);
        this.model = RaceManager.getInstance();
    }

    public void setRacer(String name) {
        model.setRacer(name);
    }
    public int getObstacleType(int row, int col){ return model.getType(row, col); }

    public void onCoinCrash(){ view.coinCrash(); }

    public void onObstacleCrash(){ view.obstacleCrash(); }

    public void checkCrash() { model.checkCrashesOccur(); }

    public int getCrashes() { return model.getCrashes(); }

    public int getRacerPos() { return model.getRacerPos(); }

    public void moveObstacles() { model.setObstaclesPosition(); }

    public void onMoveRight(){ model.moveRacerRight(); }

    public void onMoveLeft(){ model.moveRacerLeft(); }

    public void isGameEnded() { model.checkGameState(); }

    public void onLoos(){ view.gameLoos(); }

    public void endGame() { view.gameEnded(); }

    public void gameEnded(LatLng latLng) { model.gameEnded(latLng); }

    public int getScores() { return model.getScore(); }

    public StudentRacer getRacer(){ return model.getRacer(); }

}

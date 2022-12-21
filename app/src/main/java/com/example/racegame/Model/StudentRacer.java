package com.example.racegame.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * class StudentRacer contain the racer info.
 * Racer build as Builder design pattern.
 */
public class StudentRacer {
    private String name;
    private int score;
    private int position;
    private LatLng loseLocation;

    /**
     * empty Racer constructor
     */
    public StudentRacer() {}

    /**
     * getName methode
     * @return name-String
     */
    public String getName() {
        return name;
    }

    /**
     * setName methode
     * @param name-String, set name of Racer.
     * @return this Racer
     */
    public StudentRacer setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * getScore methode
     * @return score-int, represent racer's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * setScore methode
     * @param score int, represent racer's score.
     * @return this Racer
     */
    public StudentRacer setScore(int score) {
        this.score = score;
        return this;
    }

    /**
     * methode getPosition
     * @return position-int , represent racer's current position on screen.
     */
    public int getPosition() {
        return position;
    }

    /**
     * setPosition methode
     * @param position int, represent racer's current position on screen.
     * @return this Racer
     */
    public StudentRacer setPosition(int position) {
        this.position = position;
        return this;
    }

    /**
     * getLoseLocation methode
     * @return loseLocation-LatLng
     */
    public LatLng getLoseLocation() {
        return loseLocation;
    }

    /**
     * setLoseLocation method
     * @param loseLocation-LatLng
     * @return this Racer
     */
    public StudentRacer setLoseLocation(LatLng loseLocation) {
        this.loseLocation = loseLocation;
        return this;
    }
}
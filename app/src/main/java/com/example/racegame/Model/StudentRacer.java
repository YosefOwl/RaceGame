package com.example.racegame.Model;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

/**
 * class StudentRacer contain the racer info.
 * Racer build as Builder design pattern.
 */
public class StudentRacer {

    private String name;
    private int score;
    private LatLng looseLocation;

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
     * getLoseLocation methode
     * @return looseLocation-LatLng
     */
    public LatLng getLooseLocation() {
        return looseLocation;
    }

    /**
     * setLoseLocation method
     * @param looseLocation-LatLng
     * @return this Racer
     */
    public StudentRacer setLooseLocation(LatLng looseLocation) {
        this.looseLocation = looseLocation;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "StudentRacer{" + "name='" + name + '\'' + ", score=" + score + ", loseLocation=" + looseLocation + '}';
    }
}
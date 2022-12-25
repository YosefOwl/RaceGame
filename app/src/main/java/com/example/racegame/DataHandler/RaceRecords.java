package com.example.racegame.DataHandler;

import androidx.annotation.NonNull;

import com.example.racegame.Model.StudentRacer;

import java.util.ArrayList;

/**
 * class RaceRecords hold the data of records
 */
public class RaceRecords {

    ArrayList<StudentRacer> records = new ArrayList<>();

    /**
     * RaceRecords empty contractor
     */
    public RaceRecords(){

    }

    /**
     * getRecordList method
     * @return ArrayList<StudentRacer>
     */
    public ArrayList<StudentRacer> getRecordList() {
        return records;
    }

    /**
     * addRecord method add StudentRacer to list
     * @param record-StudentRacer
     * @return RaceRecords
     */
    public RaceRecords addRecord(StudentRacer record){
        records.add(record);
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "RaceRecords{" + "records=" + records + '}';
    }

}

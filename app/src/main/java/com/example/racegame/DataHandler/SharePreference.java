package com.example.racegame.DataHandler;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * class SharePreference is to save and load
 * only strings to SP
 */
public class SharePreference {

    private static final String DB_FILE = "DB_FILE_RACE_GAME";
    private static SharePreference instance = null;
    private final SharedPreferences preferences;

    /**
     * SharePreference contractor
     * @param context-Context
     */
    private SharePreference(Context context){
        preferences = context.getSharedPreferences(DB_FILE,Context.MODE_PRIVATE);
    }

    /**
     * initInstance method init this (singleton)
     * @param context-Context
     */
    public static void initInstance(Context context){
        if (instance == null)
            instance = new SharePreference(context);
    }

    /**
     * getInstance method
     * @return instance-SharePreference
     */
    public static SharePreference getInstance() {
        return instance;
    }

    /**
     * putString method save String into SP.
     * @param key-String
     * @param value-String
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * getString method load data from SP and return it
     * @param key-String
     * @param defValue-String
     * @return String
     */
    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

}

package com.example.racegame;

import android.app.Application;

import com.example.racegame.DataHandler.SharePreference;
import com.example.racegame.Utils.ImageLoader;
import com.example.racegame.Utils.SignalUser;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharePreference.initInstance(this);
        SignalUser.initInstance(this);
        ImageLoader.initInstance(this);
    }
}
package com.example.racegame.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

/**
 *
 */
public class SignalUser {

    @SuppressLint("StaticFieldLeak")
    private static SignalUser signalUser = null;

    private final Context context;
    private static Vibrator vibrator;

    /**
     * SignalUser constructor
     * @param context
     */
    private SignalUser(Context context) {
        this.context = context;
    }

    /**
     * getInstance methode create / return instance of
     * SignalUser using singleton design pattern.
     * and init Vibrator.
     * @param context
     * @return
     */
    public static void initInstance(Context context) {
        if (signalUser == null) {
            signalUser = new SignalUser(context);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    /**
     *
     * @return
     */
    public static SignalUser getInstance() {
        return signalUser;
    }

    /**
     * toast methode display a toast massage
     * @param toastMsg
     */
    public void toast(String toastMsg) {
        Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * vibrate methode activate vibration
     * @param vibrateTime in millisecond
     */
    public void vibrate(int vibrateTime) {
        vibrator.vibrate(VibrationEffect.createOneShot(vibrateTime,
                VibrationEffect.DEFAULT_AMPLITUDE));
    }

    /**
     * playSound method play sound
     * @param context-Context
     * @param soundID-int
     */
    public void playSound(Context context, int soundID) {
        MediaPlayer player = MediaPlayer.create(context, soundID);
        player.setVolume(110, 110);
        player.start();
    }
}

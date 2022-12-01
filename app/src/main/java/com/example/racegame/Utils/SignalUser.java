package com.example.racegame.Utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

public class SignalUser {
    private static SignalUser signalUser = null;

    private Context context;
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
     * @return notificationSignal
     */
    public static SignalUser getInstance(Context context) {
        if (signalUser == null) {
            signalUser = new SignalUser(context);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(vibrateTime, VibrationEffect.DEFAULT_AMPLITUDE));
        else
            vibrator.vibrate(vibrateTime);
    }
}

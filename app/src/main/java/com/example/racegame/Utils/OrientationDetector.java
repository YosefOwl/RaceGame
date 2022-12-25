package com.example.racegame.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.racegame.Interfaces.OrientationDetectorCallback;

/**
 * class OrientationDetector is for tilt device
 */
public class OrientationDetector {

    private final OrientationDetectorCallback orientationDetectorCallback;
    private SensorEventListener sensorEventListener;
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private long timeStamp = 0;

    /**
     * OrientationDetector constructor
     * @param context-Context
     * @param orientationDetectorCallback-OrientationDetectorCallback
     */
    public OrientationDetector(Context context, OrientationDetectorCallback orientationDetectorCallback) {

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.orientationDetectorCallback = orientationDetectorCallback;
        initEventListener();
    }

    /**
     * initEventListener method init listeners of sensors
     */
    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                move(sensorEvent.values[0]); // x
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }



    /**
     * move method do action of move by callback
     * @param x-float
     */
    private void move(float x) {

        if (System.currentTimeMillis() - timeStamp > 750) {
            timeStamp = System.currentTimeMillis();

            if(orientationDetectorCallback != null) {

                if (x >= 3.0)
                    orientationDetectorCallback.stepLeft();

                if (x <= -3.0)
                    orientationDetectorCallback.stepRight();
            }
        }
    }

    /**
     *
     */
    public void start() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     *
     */
    public void stop() {
        sensorManager.unregisterListener(sensorEventListener, sensor);
    }
}

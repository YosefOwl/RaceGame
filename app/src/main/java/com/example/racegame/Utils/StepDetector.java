package com.example.racegame.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.racegame.Interfaces.StepDetectorCallback;

public class StepDetector {

    private StepDetectorCallback stepDetectorCallback;
    private SensorManager sensorManager;
    private Sensor sensor;

    private int stepCountX = 0;
    private int stepCountY = 0;
    private long timeStemp = 0;

    private SensorEventListener sensorEventListener;

    public StepDetector(Context context, StepDetectorCallback stepDetectorCallback) {

        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.stepDetectorCallback = stepDetectorCallback;
        initEventListener();

    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];
                calculateStep(x, y, z);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    private void calculateStep(float x, float y, float z) {

        if (System.currentTimeMillis() - timeStemp > 500) {
            timeStemp = System.currentTimeMillis();


            if (x >= 3.0) {
                stepCountX++;
                if(stepDetectorCallback != null)
                    stepDetectorCallback.stepLeft();
            }
            if (x <= -3.0) {
                stepCountX++;
                if(stepDetectorCallback != null)
                    stepDetectorCallback.stepRight();
            }

            if (y <= 4.5) {
                stepCountY++;
                if(stepDetectorCallback != null)
                    stepDetectorCallback.speedUp();
            }

            if (y > 4.5) {
                stepCountY++;
                if(stepDetectorCallback != null)
                    stepDetectorCallback.speedDown();
            }
        }
    }
    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );

    }

    public void stop() {
        // if we have multiply sensors we can pass to unregister the specific sensor
        sensorManager.unregisterListener(sensorEventListener, sensor);

    }

    public int  getStepCountX() {
        return stepCountX;
    }

    public int  getStepCountY() {
        return stepCountY;
    }
}

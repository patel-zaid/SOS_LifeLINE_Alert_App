package com.lifeline.safety.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener{
    public interface OnShakeConfirmed {
        void onShakeConfirmed();
    }

    private static final float SHAKE_THRESHOLD = 2.5f;
    private static final int REQUIRED_SHAKES = 2;
    private static final long SHAKE_WINDOW_MS = 2000;

    private int shakeCount = 0;
    private long firstShakeTime = 0;

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final OnShakeConfirmed listener;

    private long lastShakeTime = 0;

    public ShakeDetector(Context context, OnShakeConfirmed listener){
        this.listener = listener;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start(){
        if(accelerometer != null){
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop(){
        sensorManager.unregisterListener(this);
        shakeCount = 0;
        firstShakeTime = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        if(gForce > SHAKE_THRESHOLD){
            long now = System.currentTimeMillis();

            if (shakeCount == 0){
                firstShakeTime = now;
            }

            shakeCount++;

            if (shakeCount >= REQUIRED_SHAKES && (now - firstShakeTime) <= SHAKE_WINDOW_MS){
                shakeCount = 0;
                listener.onShakeConfirmed();
            }

            if((now - firstShakeTime) > SHAKE_WINDOW_MS){
                shakeCount = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        // Not required
    }
}
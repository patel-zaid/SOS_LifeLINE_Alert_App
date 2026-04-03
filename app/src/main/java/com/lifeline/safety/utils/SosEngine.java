package com.lifeline.safety.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.lifeline.safety.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class SosEngine {
    public interface SosCallback{
        void onStarted();
        void onSmsIntentReady(Intent intent);
        void onCompleted();
        void onFailed(String reason);
    }

    private final Context context;
    private final DatabaseHelper db;
    private final LocationHelper locationHelper;

    private static final AtomicBoolean isRunning = new AtomicBoolean(false);

    public SosEngine(Context context){
        this.context = context.getApplicationContext();
        this.db = new DatabaseHelper(context);
        this.locationHelper = new LocationHelper(context);
    }

    public void triggerSOS(SosCallback callback){
        if(!isRunning.compareAndSet(false, true)){
            logFailure("Duplicate SOS trigger blocked");
            callback.onFailed("SOS already in progress");
            return;
        }

        callback.onStarted();

        List<String> phones = db.getAllContactPhones();
        if(phones.isEmpty()){
            logFailure("No emergency contacts");
            isRunning.set(false);
            callback.onFailed("No emergency contacts found");
            return;
        }

        locationHelper.fetchLocation(new LocationHelper.LocationResultListener() {
            @Override
            public void onLocationFetched(String locationLink) {
                prepareSmsIntentAndLog(phones, locationLink, callback);
            }

            @Override
            public void onLocationFailed() {
                prepareSmsIntentAndLog(phones, null, callback);
            }
        });
    }

    private void prepareSmsIntentAndLog(List<String> phones, String locationLink, SosCallback callback){
        saveHistory(locationLink);

        String defaultMessage = "🚨 EMERGENCY ALERT!\nI need immediate help.\n";
        android.content.SharedPreferences prefs =
                context.getSharedPreferences("lifeline_prefs", android.content.Context.MODE_PRIVATE);
        String message = prefs.getString("sos_message", defaultMessage);

        if (locationLink != null) {
            message += "\nLocation:\n" + locationLink;
        } else {
            message += "\nLocation unavailable.";
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + String.join(";", phones)));
        intent.putExtra("sms_body", message);

        new Handler(Looper.getMainLooper()).post(() -> {
            callback.onSmsIntentReady(intent);
            isRunning.set(false);
            callback.onCompleted();
        });
    }

    private void saveHistory(String location){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        db.insertAlertHistory(date, time, location != null ? location : "Unavailable");
    }

    private void logFailure(String reason){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        db.insertAlertHistory(date, time, "FAILED: " + reason);
    }
}
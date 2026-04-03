package com.lifeline.safety.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.location.*;

public class LocationHelper {

    public interface LocationResultListener{
        void onLocationFetched(String locationLink);
        void onLocationFailed();
    }

    private final FusedLocationProviderClient locationClient;

    public LocationHelper(Context context){
        locationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public void fetchLocation(LocationResultListener listener) {
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null){
                listener.onLocationFetched(generateMapsLink(location));
            } else {
                requestCurrentLocation(listener);
            }
        }).addOnFailureListener(e -> listener.onLocationFailed());
    }

    @SuppressLint("MissingPermission")
    private void requestCurrentLocation(LocationResultListener listener){
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000).setMinUpdateIntervalMillis(2000).setMaxUpdates(1).build();

        locationClient.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result){
                locationClient.removeLocationUpdates(this);
                Location location = result.getLastLocation();
                if (location != null){
                    listener.onLocationFetched(generateMapsLink(location));
                } else {
                    listener.onLocationFailed();
                }
            }
        }, Looper.getMainLooper());
    }

    private String generateMapsLink(Location location){
        return "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
    }
}
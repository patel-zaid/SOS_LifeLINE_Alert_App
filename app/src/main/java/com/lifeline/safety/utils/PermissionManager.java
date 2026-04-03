package com.lifeline.safety.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    public static final int SOS_PERMISSION_CODE = 100;

    private static boolean hasLocationPermission(Activity activity){
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean hasSmsPermission(Activity activity){
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasSOSPermissions(Activity activity){
        return hasLocationPermission(activity) && hasSmsPermission(activity);
    }

    public static void requestSOSPermissions(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS
        }, SOS_PERMISSION_CODE);
    }

    public static boolean shouldShowRationale(Activity activity){
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS);
    }

    public static void showRationaleDialog(Activity activity){
        new AlertDialog.Builder(activity).setTitle("Permissions Required").setMessage("LIFELINE needs location (precise or approximate) and SMS permissions to send emergency alerts with your live location.").setCancelable(false).setPositiveButton("Allow", (d, w) -> requestSOSPermissions(activity)).setNegativeButton("Cancel", null).show();
    }

    public static void showSettingsDialog(Activity activity){
        new AlertDialog.Builder(activity).setTitle("Enable Permissions").setMessage("Permissions are permanently denied. Please enable them manually from app settings.").setCancelable(false).setPositiveButton("Open Settings", (d, w) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivity(intent);
        }).setNegativeButton("Cancel", null).show();
    }
}
package com.lifeline.safety.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;
import java.util.List;

public class SmsHelper {
    public interface SmsStatusListener{
        void onAllMessagesAttempted();
    }

    private final Context context;

    public SmsHelper(Context context){
        this.context = context.getApplicationContext();
    }

    public void sendEmergencySms(List<String> phoneNumbers, String message, SmsStatusListener listener){
        SmsManager smsManager = SmsManager.getDefault();

        for (String phone : phoneNumbers){
            try{
                PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                smsManager.sendTextMessage(phone, null, message, sentIntent, null);
            } catch (Exception e){
                Toast.makeText(context, "Failed to send SMS to " + phone, Toast.LENGTH_SHORT).show();
            }
        }

        if(listener != null){
            listener.onAllMessagesAttempted();
        }
    }
}
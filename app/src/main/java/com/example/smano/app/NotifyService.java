package com.example.smano.app;

import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Georgios.Manoliadis on 1/3/2017.
 */

public class NotifyService {

    private Context context;

    public NotifyService(Context context) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.lightbulb)
                .setContentTitle("Notification Alert, Click Me!")
                .setContentText("Hi, This is Android Notification Detail!");

         }

}
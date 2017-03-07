package com.example.smano.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Georgios.Manoliadis on 1/3/2017.
 */

public class NotifyService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.lightbulb)
                .setContentTitle("Notification Alert, Click Me!")
                .setContentText("Hi, This is Android Notification Detail!")
                .setAutoCancel(true);

        notificationManager.notify(100, mBuilder.build());

    }
}
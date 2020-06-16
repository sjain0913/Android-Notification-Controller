package com.oblivion.test1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

public class NotificationUtils extends ContextWrapper {
    private NotificationManager mManager;
    public static final String CHANNEL_ID = "com.oblivion.test1.android";
    public static final String CHANNEL_NAME = "myChannel";

    public NotificationUtils(Context base) {
        super(base);
        createChannel();
    }

    public void createChannel() {
        NotificationChannel myChannel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        myChannel.enableLights(true);
        myChannel.enableVibration(true);
        myChannel.setLightColor(Color.GREEN);
        myChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(myChannel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String image) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(image)
                .setContentText("You are on " + image)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return builder;
    }

}

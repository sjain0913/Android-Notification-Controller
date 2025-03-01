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
        final Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        //This is the intent of PendingIntent
        Intent intentAction = new Intent(this,MyBroadcastReceiver.class);

        intentAction.putExtra("action","next");

        PendingIntent nextIntent = PendingIntent.getBroadcast(this,1,intentAction,PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(image)
                .setContentText("You are on " + image)
                .setSmallIcon(android.R.drawable.star_big_on)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.icon, "Next", nextIntent)
                .setOngoing(true);
    }

}

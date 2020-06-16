package com.oblivion.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public boolean isStarted = false;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String[] PERMISSIONS_BATTERY = {
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    };
    ViewFlipper viewFlipper;
    private NotificationUtils mNotificationUtils;
    public int image = 0;
    MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(broadcastReceiver, new IntentFilter("NEXT"));
        isStarted = true;
        instance = this;
        mNotificationUtils = new NotificationUtils(this);
        NotificationCompat.Builder nb = mNotificationUtils.
                getChannelNotification("Image" + image);

        mNotificationUtils.getManager().notify(101, nb.build());

        // checking if the required permissions are granted, if not it asks
        verifyStoragePermissions(this);
        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        viewFlipper.setDisplayedChild(image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nextWallpaper();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onNextWallpaperClick(View v) {
        nextWallpaper();
    }

    public void nextWallpaper() {
        viewFlipper.showNext();
        image = viewFlipper.getDisplayedChild();
        changeNotif(image);
    }

    public void onPrevWallpaperClick(View v) {
        viewFlipper.showPrevious();
        image = viewFlipper.getDisplayedChild();
        changeNotif(image);
    }

    @Override
    public void onPause() {
        super.onPause();
        image = viewFlipper.getDisplayedChild();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewFlipper.setDisplayedChild(image);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);

        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_BATTERY,
                    0
            );
        }
    }

    public void changeNotif(int image) {
        NotificationCompat.Builder nb = mNotificationUtils.
                getChannelNotification("Image" + image);
        mNotificationUtils.getManager().notify(101, nb.build());
    }
}

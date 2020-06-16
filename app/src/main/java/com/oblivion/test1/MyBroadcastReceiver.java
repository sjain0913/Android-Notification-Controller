package com.oblivion.test1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action=intent.getStringExtra("action");
        if(action.equals("next")){
            performAction1();
        }
    }

    public void performAction1(){
        Log.v("perform", "1");

    }

}

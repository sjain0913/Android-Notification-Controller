package com.oblivion.test1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("NEXT"));
        String action=intent.getStringExtra("action");
        if(action.equals("next")){
            performNext();
        }
    }

    public void performNext(){
        Log.v("perform", "next");

    }

}

package com.ubiporspy.keeperprotector.main.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ubiporspy.keeperprotector.main.services.MainService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainService.class);
        context.startService(myIntent);

        Log.e("XXX", "123");

    }

}

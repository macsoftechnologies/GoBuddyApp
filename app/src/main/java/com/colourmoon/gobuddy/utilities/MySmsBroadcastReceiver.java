package com.colourmoon.gobuddy.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class MySmsBroadcastReceiver extends BroadcastReceiver {

    private static SmsBroadcastReceiverListener smsBroadcastReceiverListener;

    public interface SmsBroadcastReceiverListener {
        void onSmsReceived(String message);
    }

    public static void setSmsBroadcastReceiverListener(SmsBroadcastReceiverListener mSmsBroadcastReceiverListener) {
        smsBroadcastReceiverListener = mSmsBroadcastReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Retriever", "broadCast triggered");
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    if (smsBroadcastReceiverListener != null) {
                        smsBroadcastReceiverListener.onSmsReceived(message);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    break;
            }
        }
    }
}

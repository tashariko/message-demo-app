package com.message.tasha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.message.tasha.MessageApplicatio;

/**
 * Created by tashariko on 20/7/16.
 */

public class MessageStatusReciever extends BroadcastReceiver {

    public final static String TAG="MSG_RECIEVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(MessageApplicatio.SMS_SENT)){
            Log.i(TAG,"SENT");

            Bundle bundle=intent.getExtras();

            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }


        }else if(intent.getAction().equals(MessageApplicatio.SMS_DELIVERED)){
            Log.i(TAG,"DELIVERED");

            Bundle bundle=intent.getExtras();

            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }

        }else{
            Log.i(TAG,"RECIEVED");
        }
    }
}

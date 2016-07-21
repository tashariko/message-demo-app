package com.message.tasha.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
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

        }else{
            Log.i(TAG,"RECIEVED");

            Bundle bundle = intent.getExtras();

            SmsMessage[] msgs = null;

            String msg = "";
            String mobile="";
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];

                for (int i=0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    mobile=msgs[i].getOriginatingAddress();
                    // Fetch the text message
                    msg +=msgs[i].getMessageBody().toString();
                    // Newline <img draggable="false" class="emoji" alt="🙂" src="https://s.w.org/images/core/emoji/72x72/1f642.png">
                    msg+= "\n";
                }

                // Display the entire SMS Message
                Log.d(TAG, msg);

                new CustomNotification().addNotiMessage(context,mobile,msg);
            }


        }
    }
}

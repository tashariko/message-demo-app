package com.message.tasha;

import android.app.Application;


/**
 * Created by tashariko on 19/7/16.
 */
public class MessageApplicatio extends Application {

    public static MessageApplicatio instance;
    public static String SMS_SENT = "tasha.SMS_SENT";
    public static String SMS_DELIVERED = "tasha.SMS_DELIVERED";


    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public MessageApplicatio getInstance(){
        return instance;
    }
}

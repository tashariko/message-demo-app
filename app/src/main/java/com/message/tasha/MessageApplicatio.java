package com.message.tasha;

import android.app.Application;


/**
 * Created by tashariko on 19/7/16.
 */
public class MessageApplicatio extends Application {

    private static MessageApplicatio instance;
    public static String SMS_SENT = "tasha.SMS_SENT";
    public static String SMS_DELIVERED = "tasha.SMS_DELIVERED";

    public static int id=0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static MessageApplicatio getInstance(){
        return instance;
    }
}

package com.message.tasha.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.message.tasha.MessageApplicatio;
import com.message.tasha.R;
import com.message.tasha.activity.MessageActivity;
import com.message.tasha.activity.StartUpActivity;

import java.util.Random;

/**
 * Created by tashariko on 21/7/16.
 */

public class CustomNotification {

    public static NotificationManager notiManager;
    public static String mobile="";
    private PendingIntent resultPendingIntent;
    private Notification noti;
    public static int number=1;
    public static String inboxMsg="";

    public void addNotiMessage(Context context, String mobile, String msg) {
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(mobile)
                        .setContentText(msg);

        notiManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(this.mobile.equals("")) {

            inboxMsg=msg;
            MessageApplicatio.id++;
            Intent resultIntent = new Intent(context, MessageActivity.class);
            resultIntent.putExtra("address",mobile);
            resultIntent.putExtra("person","");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(StartUpActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            this.mobile=mobile;
            mBuilder.setContentIntent(resultPendingIntent);
            noti=new Notification.BigTextStyle(mBuilder.setContentText(msg)).bigText(msg).build();
        }else{
            number++;

            Intent resultIntent = new Intent(context, StartUpActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            stackBuilder.addParentStack(StartUpActivity.class);
            resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            noti=new Notification.InboxStyle(mBuilder.setContentTitle(String.valueOf(number)+" Messages").setNumber(number)).addLine(inboxMsg).addLine(msg).build();
        }


        notiManager.notify(MessageApplicatio.id, noti);

    }
}
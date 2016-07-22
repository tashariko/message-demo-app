package com.message.tasha.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import com.message.tasha.R;
import com.message.tasha.activity.MessageActivity;
import com.message.tasha.activity.StartUpActivity;

import java.util.Random;

/**
 * Created by tashariko on 21/7/16.
 */

public class CustomNotification {

    public static String GRP_KEY="GROUP_KEY";
    private Notification.InboxStyle inboxStyle;
    private String mobile="";

    int id=0;
    int numMsg=0;

    public static NotificationManager notiManager;

    public void addNotiMessage(Context context, String mobile, String msg) {
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(mobile)
                        .setContentText(msg);

        Random r = new Random();
        id = r.nextInt(10000);

        Intent resultIntent = new Intent(context, MessageActivity.class);
        resultIntent.putExtra("address",mobile);
        resultIntent.putExtra("person","");
        resultIntent.putExtra("notiId",id);

        inboxStyle=new Notification.InboxStyle(mBuilder);
        //if(!mobile.equals(CustomNotification.this.mobile)){
            inboxStyle.setBigContentTitle(mobile);
            inboxStyle.addLine(msg);
        //}

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(StartUpActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent).setNumber(++numMsg);
        notiManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setStyle(inboxStyle);

        Notification noti=new Notification.BigTextStyle(mBuilder.setNumber(++numMsg).setContentText(msg)).bigText(msg).build();

        CustomNotification.this.mobile=mobile;
        notiManager.notify(0, noti);
    }
}
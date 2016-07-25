package com.message.tasha.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.Telephony;
import android.util.Log;
import android.widget.CursorAdapter;

import com.message.tasha.model.UserModel;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tashariko on 20/7/16.
 */

public class RetrieveMsgForHome {



    public void getMessage(String type, Context context, MessageListCallback callback) {
        ArrayList<UserModel> adapterList = new ArrayList<>();

        String[] request = new String[]{"_id", "body", "date", "address","person"};
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, request, null, null, "date DESC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String address = "";
            while (!cursor.isAfterLast()) {
                UserModel model = new UserModel();

                address = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS));

                int flag = 0;
                for (UserModel mod : adapterList) {
                    if (mod.address.equals(address)){
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0) {
                    model.id = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
                    model.msg = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY));
                    model.time = TimeSpentManager.setTimeAgo(context, Long.parseLong(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE))));
                    model.address = address;

                    model.person=cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.PERSON));

                    adapterList.add(model);
                }
                cursor.moveToNext();
            }

            cursor.close();
        }

        Log.i("MSG_LENGTH", String.valueOf(adapterList.size()));
        callback.list(adapterList);
    }



    public void getMessagesFromText(String text,Context context,MessageListCallback callback){
        ArrayList<UserModel> adapterList = new ArrayList<>();

        String[] request = new String[]{"_id", "body", "date", "address","person"};
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, request,Telephony.TextBasedSmsColumns.BODY+" like ?",
                new String[]{"%"+text+"%"}, "date ASC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                UserModel model = new UserModel();

                model.address= cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS));
                model.id = cursor.getString(cursor.getColumnIndex(BaseColumns._ID));
                model.msg = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY));
                model.time = TimeSpentManager.setTimeAgo(context, Long.parseLong(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE))));
                model.person=cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.PERSON));

                adapterList.add(model);
                cursor.moveToNext();
            }
        }

        Log.i("MSG_LENGTH", String.valueOf(adapterList.size()));
        callback.list(adapterList);
    }



    public void getMessageCursor(String add, Context context, MessageCursorCallback callback){

        String[] request = new String[]{"_id", "body", "date", "address","person","status","seen"};
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, request,
                Telephony.TextBasedSmsColumns.ADDRESS+"=?",new String[]{add}, "date ASC");

        try {
            callback.list(cursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface MessageListCallback {
        public void list(ArrayList<UserModel> list);
    }

    public interface MessageCursorCallback {
        public void list(Cursor cursor) throws IOException;
    }

}

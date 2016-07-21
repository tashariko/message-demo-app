package com.message.tasha.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.message.tasha.R;
import com.message.tasha.activity.MessageActivity;
import com.message.tasha.util.TimeSpentManager;

/**
 * Created by tashariko on 20/7/16.
 */

public class MessageCursorAdapter extends CursorAdapter {

    public static final int SELF= 0;
    public static final int OTHER= 1;

    private MessageActivity parentActivity;
    private LayoutInflater inflater;
    private ViewHolderSelf selfHolder;
    private ViewHolderOther otherHolder;

    public MessageCursorAdapter(Context context, MessageActivity messageActivity, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.parentActivity=messageActivity;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        selfHolder=new ViewHolderSelf();
        otherHolder=new ViewHolderOther();
        int type;

        if(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.PERSON))!=null){
            type=OTHER;
        }else{
            type=SELF;
        }

        if(type==OTHER){
            View otherView;
            otherView=inflater.inflate(R.layout.list_item_msg_other, null);

            otherHolder.userSelfImg= (ImageView) otherView.findViewById(R.id.userSelfImg);
            otherHolder.textMsgTv= (TextView) otherView.findViewById(R.id.textMsgTv);
            otherHolder.justNowText= (TextView) otherView.findViewById(R.id.justNowText);
            otherView.setTag(otherHolder);

            return otherView;
        }else{
            View selfView;
            selfView=inflater.inflate(R.layout.list_item_msg_self, null);

            selfHolder.userSelfImg= (ImageView) selfView.findViewById(R.id.userSelfImg);
            selfHolder.textMsgTv= (TextView) selfView.findViewById(R.id.textMsgTv);
            selfHolder.justNowText= (TextView) selfView.findViewById(R.id.justNowText);
            selfHolder.sentInfo= (ImageView) selfView.findViewById(R.id.sentInfo);
            selfView.setTag(selfHolder);

            return selfView;
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int type;
        if(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.PERSON))!=null){
            type=OTHER;
        }else{
            type=SELF;
        }

        if(type==SELF){

            selfHolder= (ViewHolderSelf) view.getTag();

            /*TextDrawable drawable = TextDrawable.builder()
                    .buildRect(getInitials(position), getColor(position));
            holder.userImg.setImageDrawable(drawable);*/
           if(cursor.getInt(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.STATUS))==-1 ||
                   cursor.getInt(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.STATUS))==0){
                selfHolder.sentInfo.setVisibility(View.VISIBLE);
               if(parentActivity!=null)
                   parentActivity.updateEditText();
            }else{
                selfHolder.sentInfo.setVisibility(View.GONE);
            }

            selfHolder.textMsgTv.setText(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY)));
            String date= TimeSpentManager.setTimeAgo(context, Long.parseLong(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE))));
            selfHolder.justNowText.setText(date);
        }else{
            otherHolder= (ViewHolderOther) view.getTag();

            otherHolder.textMsgTv.setText(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY)));
            String date= TimeSpentManager.setTimeAgo(context, Long.parseLong(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE))));
            otherHolder.justNowText.setText(date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        if(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.PERSON))!=null){
            return OTHER;
        }else{
            return  SELF;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    private class ViewHolderSelf{
        ImageView userSelfImg,sentInfo;
        TextView textMsgTv,justNowText;
    }

    private class ViewHolderOther{
        ImageView userSelfImg;
        TextView textMsgTv,justNowText;
    }

}

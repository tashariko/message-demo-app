package com.message.tasha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.message.tasha.R;
import com.message.tasha.model.UserModel;

import java.util.ArrayList;

/**
 * Created by tashariko on 20/7/16.
 */

public class MessageAdapter extends ArrayAdapter<UserModel> {

    private LayoutInflater inflater;
    private ArrayList<UserModel> list;
    private View view;
    private ViewHolderOther holderOther;
    private ViewHolderSelf holderSelf;

    public MessageAdapter(Context context, int resource, ArrayList<UserModel> adapterList) {
        super(context, resource);
        this.list=adapterList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        holderSelf = new ViewHolderSelf();
        holderOther=new ViewHolderOther();


        if(list.get(position).isMine){
            view = inflater.inflate(R.layout.list_item_msg_self, null);

            holderSelf.userSelfImg= (ImageView) view.findViewById(R.id.userSelfImg);
            holderSelf.textMsgTv= (TextView) view.findViewById(R.id.textMsgTv);
            holderSelf.justNowText= (TextView) view.findViewById(R.id.justNowText);
            holderSelf.sentInfo= (ImageView) view.findViewById(R.id.sentInfo);

            /*TextDrawable drawable = TextDrawable.builder()
                    .buildRect(getInitials(position), getColor(position));
            holder.userImg.setImageDrawable(drawable);*/

            if(list.get(position).sentStatus==-1){
                holderSelf.sentInfo.setVisibility(View.VISIBLE);
            }else{
                holderSelf.sentInfo.setVisibility(View.GONE);
            }

            holderSelf.textMsgTv.setText(list.get(position).msg);
            holderSelf.justNowText.setText(list.get(position).time);
        }else{
            view = inflater.inflate(R.layout.list_item_msg_other, null);

            holderOther.userSelfImg= (ImageView) view.findViewById(R.id.userSelfImg);
            holderOther.textMsgTv= (TextView) view.findViewById(R.id.textMsgTv);
            holderOther.justNowText= (TextView) view.findViewById(R.id.justNowText);

            holderOther.textMsgTv.setText(list.get(position).msg);
            holderOther.justNowText.setText(list.get(position).time);
        }

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
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

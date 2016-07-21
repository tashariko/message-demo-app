package com.message.tasha.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.message.tasha.R;
import com.message.tasha.model.UserModel;

import java.util.ArrayList;

/**
 * Created by tashariko on 19/7/16.
 */
public class UserMessageAdapter extends ArrayAdapter<UserModel> {

    private LayoutInflater inflater;
    private ArrayList<UserModel> list;
    private View view;
    private ViewHolder holder;

    public UserMessageAdapter(Context context, int resource, ArrayList<UserModel> adapterList) {
        super(context, resource);
        this.list=adapterList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view==null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item_user_list, null);
            holder.msgName = (TextView) view.findViewById(R.id.msgCreator);
            holder.msgBody = (TextView) view.findViewById(R.id.msgBody);
            holder.msgDate = (TextView) view.findViewById(R.id.msgDate);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.msgName.setText(list.get(position).address);
        holder.msgBody.setText(list.get(position).msg);
        holder.msgDate.setText(list.get(position).time);

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private class ViewHolder{
        TextView msgName,msgBody,msgDate;
    }
}

package com.message.tasha.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.message.tasha.R;
import com.message.tasha.model.ContactModel;
import com.message.tasha.model.UserModel;

import java.util.ArrayList;

/**
 * Created by tashariko on 19/7/16.
 */
public class ContactListAdapter extends ArrayAdapter<ContactModel> {

    private LayoutInflater inflater;
    private ArrayList<ContactModel> list;
    private View view;
    private ViewHolder holder;

    public ContactListAdapter(Context context, int resource, ArrayList<ContactModel> adapterList) {
        super(context, resource);
        this.list=adapterList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view==null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item_contact, null);
            holder.userImg = (ImageView) view.findViewById(R.id.userImg);
            holder.contactName= (TextView) view.findViewById(R.id.contactName);
            holder.contactNumber= (TextView) view.findViewById(R.id.contactNumber);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        TextDrawable drawable = TextDrawable.builder()
                .buildRect(getInitials(position), getColor(position));

        holder.userImg.setImageDrawable(drawable);

        holder.contactNumber.setText(list.get(position).number);
        holder.contactName.setText(list.get(position).name+" / "+list.get(position).id);

        return view;
    }

    private int getColor(int position) {
        switch (position%4){
            case 0:
                return getContext().getResources().getColor(R.color.red);
            case 1:
                return getContext().getResources().getColor(R.color.purple);
            case 2:
                return getContext().getResources().getColor(R.color.green);
            case 3:
                return getContext().getResources().getColor(R.color.orange);
        }
        return Color.GRAY;
    }

    private String getInitials(int position) {
        return list.get(position).name.substring(0,1).toUpperCase();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    class ViewHolder{
        TextView contactName,contactNumber;
        ImageView userImg;
    }

}

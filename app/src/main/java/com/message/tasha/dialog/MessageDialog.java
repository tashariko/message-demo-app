package com.message.tasha.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.message.tasha.R;
import com.message.tasha.model.UserModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Puru Chauhan on 23/07/16.
 */

public class MessageDialog extends Dialog {

    @Bind(R.id.sender)TextView sender;

    @Bind(R.id.body)TextView body;

    @Bind(R.id.time)TextView time;

    private UserModel user;

    public MessageDialog(Context context, UserModel userModel) {
        super(context);
        this.user=userModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_message);

        ButterKnife.bind(this);

        if(user.person!=null) {
            sender.setText(user.address);
        }else{
            sender.setText("Your message");
        }

        body.setText(user.msg);

        time.setText(user.time);

    }
}

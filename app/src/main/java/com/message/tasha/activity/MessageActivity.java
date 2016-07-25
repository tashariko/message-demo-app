package com.message.tasha.activity;

import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.message.tasha.R;
import com.message.tasha.adapter.MessageCursorAdapter;
import com.message.tasha.model.UserModel;
import com.message.tasha.service.CustomNotification;
import com.message.tasha.util.RetrieveMsgForHome;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.message.tasha.MessageApplicatio.SMS_DELIVERED;
import static com.message.tasha.MessageApplicatio.SMS_SENT;

/**
 * Created by tashariko on 20/7/16.
 */

public class MessageActivity extends AppCompatActivity {

    private String person,address;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.messageList)
    ListView messageList;

    @Bind(R.id.textMsgEt)
    EditText sendText;

    @Bind(R.id.sendMsgButton)
    ImageButton sendButton;

    private ArrayList<UserModel> adapterList;
    private MessageCursorAdapter msgAdapter;
    private Cursor dataCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        address=bundle.getString("address");
        person=bundle.getString("person");


        if(CustomNotification.notiManager!=null)
            CustomNotification.notiManager.cancelAll();

        CustomNotification.mobile="";
        CustomNotification.inboxMsg="";
        title.setText(address);

        updateCursor();

        listener();

    }

    private void listener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendText.getText().toString().trim().length()>0){
                    sendMsg();
                }else{
                    Toast.makeText(MessageActivity.this,"Please provide the message first.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendMsg(){

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
        PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

        try {
            String msg = sendText.getText().toString();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(address, null, msg, sentPendingIntent, deliveredPendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
        sendText.setText("");

    }

    public void updateCursor(){
        new RetrieveMsgForHome().getMessageCursor(address, getApplicationContext(), new RetrieveMsgForHome.MessageCursorCallback() {
            @Override
            public void list(Cursor cursor) {

                dataCursor=cursor;

                Log.i("SIZE_LIST", String.valueOf(cursor.getCount()));

                msgAdapter = new MessageCursorAdapter(getApplicationContext(), MessageActivity.this, cursor, true);
                messageList.setAdapter(msgAdapter);


            }
        });
    }

    public void updateEditText() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(dataCursor!=null && !dataCursor.isClosed())
            dataCursor.close();

    }
}

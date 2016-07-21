package com.message.tasha.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.message.tasha.R;
import com.message.tasha.adapter.ContactListAdapter;
import com.message.tasha.adapter.UserMessageAdapter;
import com.message.tasha.model.ContactModel;
import com.message.tasha.model.UserModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactActivity extends AppCompatActivity {

    @Bind(R.id.contactList)
    ListView contactList;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<ContactModel> adapterList;
    private ContactListAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapterList=new ArrayList<>();
        userAdapter = new ContactListAdapter(getApplicationContext(), R.layout.list_item_contact, adapterList);
        contactList.setAdapter(userAdapter);

        initUserListWithMessage();

    }

    private void initUserListWithMessage() {
        ContentResolver contentResolver=getContentResolver();

        Cursor cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+ " ASC");

        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                ContactModel model = null;
                String id=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                    Cursor contactCursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",new String[]{id},null);

                    contactCursor.moveToFirst();
                    while(contactCursor.moveToNext()){
                        String number= contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        model=new ContactModel();

                        model.id=id;
                        model.number=number;
                        model.name=name;

                        adapterList.add(model);
                    }

                    contactCursor.close();

                }
                cursor.moveToNext();
            }
        }

        Log.i("MSG_LENGTH", String.valueOf(cursor.getCount( )));

        userAdapter.notifyDataSetChanged();


    }
}

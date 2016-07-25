package com.message.tasha.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.message.tasha.R;
import com.message.tasha.adapter.MessageCursorAdapter;
import com.message.tasha.adapter.UserMessageAdapter;
import com.message.tasha.dialog.MessageDialog;
import com.message.tasha.model.UserModel;
import com.message.tasha.util.RetrieveMsgForHome;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Puru Chauhan on 22/07/16.
 */

public class SearchMsgActivity extends AppCompatActivity {

    @Bind(R.id.emptyText)TextView emptyText;

    @Bind(R.id.messageView)
    ListView messageView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.title)
    TextView title;


    private String query = "";
    private ArrayAdapter<UserModel> userAdapter;
    private ArrayList<UserModel> adapterList=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleSearch();

        listeners();
    }

    private void handleSearch() {
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            query = getIntent().getStringExtra(SearchManager.QUERY);

            title.setText("Search for: "+query);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new RetrieveMsgForHome().getMessagesFromText(query, getApplicationContext(), new RetrieveMsgForHome.MessageListCallback() {
                        @Override
                        public void list(final ArrayList<UserModel> list) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {



                                    if(list.size()>0) {
                                        adapterList = list;
                                        Log.i("SIZE_LIST", String.valueOf(adapterList.size()));
                                        updateList();
                                    }else{
                                        emptyText.setVisibility(View.VISIBLE);
                                    }
                                }
                            });

                        }
                    });
                }
            }).start();

        }
    }

    public void updateList(){
        userAdapter = new UserMessageAdapter(getApplicationContext(), R.layout.list_item_user_list, adapterList);
        messageView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    private void listeners() {

        messageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDialog dialog=new MessageDialog(SearchMsgActivity.this,adapterList.get(position));
                dialog.show();
                Window win=dialog.getWindow();
                win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

    }
}

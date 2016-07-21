package com.message.tasha.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.message.tasha.R;
import com.message.tasha.adapter.UserMessageAdapter;
import com.message.tasha.model.UserModel;
import com.message.tasha.util.RetrieveMsgForHome;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartUpActivity extends AppCompatActivity {



    @Bind(R.id.messageView) ListView messageView;

    @Bind(R.id.newMessageButton) FloatingActionButton newMessage;

    @Bind(R.id.toolbar) Toolbar toolbar;


    private ArrayAdapter<UserModel> userAdapter;
    private UserModel model=new UserModel();
    private ArrayList<UserModel> adapterList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        ButterKnife.bind(StartUpActivity.this);

        setSupportActionBar(toolbar);

        newMessage.setVisibility(View.GONE);

        listeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserListWithMessage();
    }

    private void listeners() {

        messageView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_TOUCH_SCROLL){
                    newMessage.hide(true);
                }else if(scrollState==SCROLL_STATE_IDLE){
                    newMessage.show(true);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ContactActivity.class));
            }
        });

        messageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),MessageActivity.class);
                intent.putExtra("address",adapterList.get(position).address);
                intent.putExtra("person",adapterList.get(position).person);
                startActivity(intent);
            }
        });

    }

    private void initUserListWithMessage() {
        new RetrieveMsgForHome().getMessage("0", getApplicationContext(), new RetrieveMsgForHome.MessageListCallback() {
            @Override
            public void list(ArrayList<UserModel> list) {



                adapterList=list;
                Log.i("SIZE_LIST", String.valueOf(adapterList.size()));
                updateList();
            }
        });
    }

    public void updateList(){
        userAdapter = new UserMessageAdapter(getApplicationContext(), R.layout.list_item_user_list, adapterList);
        messageView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }
}

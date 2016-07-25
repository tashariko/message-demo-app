package com.message.tasha.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.message.tasha.R;
import com.message.tasha.adapter.UserMessageAdapter;
import com.message.tasha.model.UserModel;
import com.message.tasha.util.CheckGServices;
import com.message.tasha.util.NetworkChecker;
import com.message.tasha.util.RetrieveMsgForHome;
import com.message.tasha.util.UploadFileToDrive;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
public class StartUpActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int PERMISSION_SMS_CODE = 2307;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1;
    private static final String TAG = "##StartUpActivity##";
    private GoogleApiClient mGoogleApiClient;


    @Bind(R.id.messageView)
    ListView messageView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;


    private ArrayAdapter<UserModel> userAdapter;
    private ArrayList<UserModel> adapterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);

        ButterKnife.bind(StartUpActivity.this);

        setSupportActionBar(toolbar);

        listeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("DebugInfo: Permission", "Location Permission Required");
            ActivityCompat.requestPermissions(StartUpActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_SMS_CODE);
            return;
        } else {
            initUserListWithMessage();
        }
    }

    private void listeners() {


        messageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("address", adapterList.get(position).address);
                intent.putExtra("person", adapterList.get(position).person);
                startActivity(intent);
            }
        });

    }

    private void initUserListWithMessage() {
        new RetrieveMsgForHome().getMessage("0", getApplicationContext(), new RetrieveMsgForHome.MessageListCallback() {
            @Override
            public void list(ArrayList<UserModel> list) {


                adapterList = list;
                Log.i("SIZE_LIST", String.valueOf(adapterList.size()));
                updateList();
            }
        });
    }

    public void updateList() {
        userAdapter = new UserMessageAdapter(getApplicationContext(), R.layout.list_item_user_list, adapterList);
        messageView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_msg)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search_msg) {

            onSearchRequested();
            return true;
        }

        if (id == R.id.action_drive) {

            if(new CheckGServices(StartUpActivity.this).checkPlayServices()) {
                if(new NetworkChecker(getApplicationContext()).nwChecker())
                    intializeGoogleCLient();
                else
                    Toast.makeText(this, "Not connected to the internet.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "The device dont have play services, cant do this task.", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {

        startSearch(null, false, null, false);
        return true;
    }















    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SMS_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initUserListWithMessage();
                    Log.d("Request Permission", "GRANTED");
                } else {
                    Toast.makeText(this, "Without permission the app will not work.", Toast.LENGTH_SHORT).show();
                    StartUpActivity.this.finish();
                    Log.d("Request Permission", "DENIED");
                }
            }
        }
    }

    private void intializeGoogleCLient() {
        if(mGoogleApiClient==null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        new UploadFileToDrive(StartUpActivity.this).startUploading(mGoogleApiClient,adapterList);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());

        if (!result.hasResolution()) {

            Log.i(TAG,"result is not null");

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        try {
            result.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);

        } catch (IntentSender.SendIntentException e) {

            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG,"here");
        switch (requestCode){
            case RESOLVE_CONNECTION_REQUEST_CODE:{
                Log.i(TAG,"Here too");

                if(!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting())
                    mGoogleApiClient.connect();
                else
                    new UploadFileToDrive(StartUpActivity.this).startUploading(mGoogleApiClient, adapterList);
                break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }
}

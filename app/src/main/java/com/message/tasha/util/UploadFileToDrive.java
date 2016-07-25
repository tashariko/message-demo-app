package com.message.tasha.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;/*
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;*/
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.message.tasha.activity.StartUpActivity;
import com.message.tasha.model.UserModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Puru Chauhan on 24/07/16.
 */

public class UploadFileToDrive {


    private static final String TAG = "##UploadFileToDrive##";
    private final StartUpActivity actContext;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<UserModel> adapterList;

    public UploadFileToDrive(StartUpActivity actContext){
        this.actContext=actContext;
    }

    public void startUploading(GoogleApiClient mGoogleApiClient, ArrayList<UserModel> adapterList){
        this.mGoogleApiClient=mGoogleApiClient;
        this.adapterList=adapterList;
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }


    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {

                    Log.i(TAG,"DriveCakkback");

                    if (result.getStatus().isSuccess()) {
                        createFileOnGoogleDrive(result);

                    }else{
                        Log.i(TAG,result.getStatus().getStatusMessage());
                    }
                }
            };

    private void createFileOnGoogleDrive(DriveApi.DriveContentsResult result) {
        final DriveContents driveContents = result.getDriveContents();

        // Perform I/O off the UI thread.
        new Thread() {
            @Override
            public void run() {


                Log.i(TAG,"File creation starting");
                // write content to DriveContents

                addDataToFile(driveContents);

                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("Message App Data")
                        .setMimeType("text/plain")
                        .setStarred(true).build();

                Log.i(TAG,"File created");

                // create a file in root folder
                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFile(mGoogleApiClient, changeSet, driveContents).
                        setResultCallback(fileCallback);
            }
        }.start();
    }

    private void addDataToFile(DriveContents driveContents) {
        OutputStream outputStream = driveContents.getOutputStream();
        final Writer writer = new OutputStreamWriter(outputStream);
        try {

            Log.i(TAG, String.valueOf(adapterList.size()));

            new RetrieveMsgForHome().getMessage("0", actContext, new RetrieveMsgForHome.MessageListCallback() {
                @Override
                public void list(ArrayList<UserModel> list) {

                    for(UserModel model:list) {

                        try {
                            writer.write(model.address+"\n\n");

                        new RetrieveMsgForHome().getMessageCursor(model.address, actContext, new RetrieveMsgForHome.MessageCursorCallback() {
                            @Override
                            public void list(Cursor cursor) throws IOException {
                                cursor.moveToFirst();

                                while(!cursor.isAfterLast()){
                                    writer.write(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE))+"\t");

                                    writer.write(cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY))+"\n");
                                    cursor.moveToNext();
                                }

                                cursor.close();

                            }
                        });

                            writer.write("\n\n\n");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Log.i("SIZE_LIST", String.valueOf(adapterList.size()));
                }
            });

            writer.close();

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(final DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        actContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(actContext, "File uploaded to drive.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    return;

                }
            };





}

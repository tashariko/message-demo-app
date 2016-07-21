package com.message.tasha.model;

/**
 * Created by tashariko on 19/7/16.
 */
public class UserModel {


    //type :: 1-> inbox     2-> sent
    //sentStatus :: 0->complete
    public String id,type,msg,person,time,img,status;
    public boolean isMine;
    public int sentStatus;

    public String address;
}

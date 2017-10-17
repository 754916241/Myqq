package com.wyj.myqq.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by wyj on 2016/7/1.
 */
public class Friends implements Serializable{
    private static final long serialVersionUID = 7894568754L;
    private String friendQQ;
    private String friendNick;
    private String friendImg;
    private String friendSign;
    private String friendToken;
    private Bitmap friendBm;
    private String friendSex,friendPhone;
    private int status;

    public Friends(String friendQQ, String friendNick, String friendImg) {
        this.friendQQ = friendQQ;
        this.friendNick = friendNick;
        this.friendImg = friendImg;
    }

    @Override
    public boolean equals(Object o) {
        if(this.toString().equals(o.toString())){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "friendPhone='" + friendPhone + '\'' +
                ", friendSex='" + friendSex + '\'' +
                ", friendToken='" + friendToken + '\'' +
                ", friendSign='" + friendSign + '\'' +
                ", friendImg='" + friendImg + '\'' +
                ", friendNick='" + friendNick + '\'' +
                ", friendQQ='" + friendQQ + '\'' +
                '}';
    }

    public Friends(String friendQQ, String friendNick, String friendSex, String friendPhone, String friendToken, String friendImg, String friendSign) {
        this.friendQQ = friendQQ;
        this.friendNick = friendNick;
        this.friendSign = friendSign;
        this.friendImg = friendImg;
        this.friendToken = friendToken;
        this.friendSex = friendSex;
        this.friendPhone = friendPhone;
    }

    public String getFriendSex() {
        return friendSex;
    }

    public void setFriendSex(String friendSex) {
        this.friendSex = friendSex;
    }

    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public Friends(String friendQQ, String friendNick, String friendToken, String friendImg, String friendSign) {
        this.friendQQ = friendQQ;
        this.friendNick = friendNick;
        this.friendSign = friendSign;
        this.friendImg = friendImg;
        this.friendToken = friendToken;
    }

    public String getFriendQQ() {
        return friendQQ;
    }

    public void setFriendQQ(String friendQQ) {
        this.friendQQ = friendQQ;
    }

    public String getFriendNick() {
        return friendNick;
    }

    public void setFriendNick(String friendNick) {
        this.friendNick = friendNick;
    }

    public String getFriendImg() {
        return friendImg;
    }

    public void setFriendImg(String friendImg) {
        this.friendImg = friendImg;
    }

    public String getFriendSign() {
        return friendSign;
    }

    public void setFriendSign(String friendSign) {
        this.friendSign = friendSign;
    }

    public String getFriendToken() {
        return friendToken;
    }

    public void setFriendToken(String friendToken) {
        this.friendToken = friendToken;
    }

    public Bitmap getFriendBm() {
        return friendBm;
    }

    public void setFriendBm(Bitmap friendBm) {
        this.friendBm = friendBm;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

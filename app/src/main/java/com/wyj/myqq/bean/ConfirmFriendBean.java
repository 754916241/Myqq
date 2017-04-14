package com.wyj.myqq.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by wyj on 2017/3/25.
 */

public class ConfirmFriendBean implements Serializable{

    private String friendQQ;
    private String friendNick;
    private String friendImg;
    private String applyMessage;
    private Bitmap friendBm;

    public ConfirmFriendBean(String friendQQ, String friendNick, String friendImg, String applyMessage) {
        this.friendQQ = friendQQ;
        this.friendNick = friendNick;
        this.applyMessage = applyMessage;
        this.friendImg = friendImg;
    }

    public ConfirmFriendBean(String friendQQ, String friendNick,String friendImg) {
        this.friendQQ = friendQQ;
        this.friendNick = friendNick;
        this.friendImg = friendImg;
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

    public String getApplyMessage() {
        return applyMessage;
    }

    public void setApplyMessage(String applyMessage) {
        this.applyMessage = applyMessage;
    }

    public Bitmap getFriendBm() {
        return friendBm;
    }

    public void setFriendBm(Bitmap friendBm) {
        this.friendBm = friendBm;
    }
}

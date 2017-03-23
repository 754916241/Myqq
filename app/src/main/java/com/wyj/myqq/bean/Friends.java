package com.wyj.myqq.bean;

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

    public Friends(String friendQQ, String friendNick,String friendToken,String friendImg, String friendSign) {
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
}

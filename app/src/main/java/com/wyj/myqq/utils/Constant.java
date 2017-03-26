package com.wyj.myqq.utils;

import static java.lang.reflect.Modifier.FINAL;

/**
 * Created by wyj on 2016/7/1.
 */
public class Constant {

    public static final String KEY_USER = "user";
    public static final String KEY_QQNUMBER = "qqnumber";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SEX = "sex";
    public static final String KEY_AGE = "age";
    public static final String KEY_SIGNATURE = "signature";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_NICK = "nickname";
    public static final String KEY_TRUE = "truename";
    public static final String KEY_PHONE = "phone";

    public static final String KEY_FRIENDS = "friends";
    public static final String KEY_FRIENDS_QQNUMBER = "friendqqnumber";
    public static final String KEY_FRIENDS_SIGNATURE = "friendsignature";
    public static final String KEY_FRIENDS_SEX = "friendsex";
    public static final String KEY_FRIENDS_PHONE = "friendphone";
    public static final String KEY_FRIENDS_IMAGE = "friendimage";
    public static final String KEY_FRIENDS_TOKEN = "friendtoken";
    public static final String KEY_FRIENDS_NICK = "friendnickname";
    public static final String KEY_FRIENDS_ITEM = "friend_item";
    public static final String KEY_APPLY_MESSAGE = "message";


    public static final String KEY_ADDFRIEND_RESULT = "result";
    public static final String KEY_ACCEPT = "accept";
    public static final String KEY_REFUSE = "refuse";

    //192.168.0.108  192.168.1.9
    private static final String HTTP_URL = "192.168.0.108";
    public static final String HTTPURL_REGIST = "http://"+HTTP_URL+":82/htdocs/qq/Regist.php";
    public static final String HTTPURL_LOGIN = "http://"+HTTP_URL+":82/htdocs/qq/Login.php";
    public static final String HTTPURL_ADDFRIENDS = "http://"+HTTP_URL+":82/htdocs/qq/AddFriends.php";
    public static final String HTTPURL_CHANGEMYDATA = "http://"+HTTP_URL+":82/htdocs/qq/ChangeData.php";
    public static final String HTTP_SEARCH_FRIEND = "http://"+HTTP_URL+":82/htdocs/qq/SearchFriend.php";
    public static final String HTTP_RESULT_ADDFRIEND = "http://"+HTTP_URL+":82/htdocs/qq/ResultAddFriend.php";
    //保存在本地的个人信息
    public static final String KEY_SHARE_REMEBER = "perference_key";
    public static final String KEY_SET_SOURCEID = "sourceId";
    public static final String KEY_SET_MESSAGE = "message";


    public static final int REQUEST_CODE_FROM_CAMERA = 5001;
    public static final int REQUEST_CODE_FROM_ALBUM = 5002;
    public static final int CROP_REQUEST = 5003;
    public static final int REQUEST_CODE_CHANGEPASSWORD = 5004;
    public static final int RESULT_CODE_CHANGEPASSWORD = 5005;
    public static final int REQUEST_CODE_ADDFRIENDS = 5006;
    public static final int RESULT_CODE_ADDFRIENDS = 5007;
    public static final int REQUEST_CODE_CHANGENICK = 5008;
    public static final int RESULT_CODE_CHANGENICK = 5009;
    public static final int REQUEST_CODE_CHANGETRUE = 5010;
    public static final int RESULT_CODE_CHANGETRUE = 5011;
    public static final int REQUEST_CODE_CHANGESEX = 5012;
    public static final int RESULT_CODE_CHANGESEX = 5013;
    public static final int REQUEST_CODE_CHANGEAGE = 5014;
    public static final int RESULT_CODE_CHANGEAGE = 5015;
    public static final int REQUEST_CODE_CHANGESIGNATURE= 5016;
    public static final int RESULT_CODE_CHANGESIGNATURE = 5017;
    public static final int REQUEST_CODE_CHANGEPHONE = 5018;
    public static final int RESULT_CODE_CHANGEPHONE = 5019;
    public static final int REQUEST_CODE_SUGGEST = 5020;
    public static final int RESULT_CODE_SUGGEST = 5021;
    public static final int REQUEST_CODE_CONFIRM_FRIEND = 5022;
    public static final int RESULT_CODE_CONFIRM_FRIEND = 5023;

    public static final String MOB_APP_KEY = "1c31c4dd9f304";
    public static final String MOB_APP_SECRETE = "5e063382d632069c76a99d73921965b6";
    public static final String COUNTRY_ID_DEFAULT = "86";



}

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
    public static final String KEY_IMAGE_BASE64 = "imagebase64";
    public static final String KEY_IMAGE_URI = "imageuri";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_NICK = "nickname";
    public static final String KEY_TRUE = "truename";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_SUGGEST = "suggest";

    public static final String KEY_FRIENDS = "friends";
    public static final String KEY_FRIENDS_QQNUMBER = "friendqqnumber";
    public static final String KEY_FRIENDS_SIGNATURE = "friendsignature";
    public static final String KEY_FRIENDS_SEX = "friendsex";
    public static final String KEY_FRIENDS_PHONE = "friendphone";
    public static final String KEY_FRIENDS_IMAGE = "friendimage";
    public static final String KEY_FRIENDS_TOKEN = "friendtoken";
    public static final String KEY_FRIENDS_NICK = "friendnickname";
    public static final String KEY_APPLY_MESSAGE = "message";

    public static final String KEY_ADDFRIEND_RESULT = "result";
    public static final String KEY_ACCEPT = "accept";
    public static final String KEY_REFUSE = "refuse";
    public static final String KEY_DELETE = "delete";

    public static final String KEY_DISCUSSION_ID = "discussionId";
    public static final String KEY_DISCUSSION_NAME = "discussionName";
    public static final String KEY_CONVERSATION_TYPE = "conversationType";

    //192.168.0.108  192.168.1.9
    public static final String HTTP_URL = "172.20.10.8";
    public static final String HTTPURL_REGIST = "http://"+HTTP_URL+":82/htdocs/qq/Regist.php";
    public static final String HTTPURL_LOGIN = "http://"+HTTP_URL+":82/htdocs/qq/Login.php";
    public static final String HTTPURL_ADDFRIENDS = "http://"+HTTP_URL+":82/htdocs/qq/AddFriends.php";
    public static final String HTTPURL_CHANGEMYDATA = "http://"+HTTP_URL+":82/htdocs/qq/ChangeData.php";
    public static final String HTTPURL_SEARCH_FRIEND = "http://"+HTTP_URL+":82/htdocs/qq/SearchFriend.php";
    public static final String HTTPURL_CONFIRM_FRIEND = "http://"+HTTP_URL+":82/htdocs/qq/ConfirmFriend.php";
    public static final String HTTPURL_RESULT_ADDFRIEND = "http://"+HTTP_URL+":82/htdocs/qq/ResultAddFriend.php";
    public static final String HTTPURL_DELETE_FRIEND = "http://"+HTTP_URL+":82/htdocs/qq/DeleteFriend.php";
    public static final String HTTPURL_GET_FRIEND_INFO = "http://"+HTTP_URL+":82/htdocs/qq/GetFriendInfo.php";
    public static final String HTTPURL_SUGGEST = "http://"+HTTP_URL+":82/htdocs/qq/Suggest.php";
    //保存在本地的个人信息
    public static final String KEY_SHARE_REMEBER = "perference_key";
    public static final String KEY_SET_SOURCEID = "sourceId";
    public static final String KEY_SET_MESSAGE = "message";
    public static final String KEY_IS_FIRST = "first";

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
    public static final int REQUEST_CODE_DELETE_FRIEND = 5024;
    public static final int RESULT_CODE_DELETE_FRIEND = 5025;
    public static final int RESULT_CODE_CONFIRM_FRIEND_REFUSE = 5026;
    public static final int REQUEST_CODE_CHANGE_DISCUSSION_NAME = 5027;
    public static final int RESULT_CODE_CHANGE_DISCUSSION_NAME = 5028;
    /*public static final int REQUEST_CODE_ADD_DISCUSSION_MEMBERS = 5029;
    public static final int RESULT_CODE_ADD_DISCUSSION_MEMBERS = 5030;*/

    public static final String MOB_APP_KEY = "1c31c4dd9f304";
    public static final String MOB_APP_SECRETE = "5e063382d632069c76a99d73921965b6";
    public static final String COUNTRY_ID_DEFAULT = "86";


}

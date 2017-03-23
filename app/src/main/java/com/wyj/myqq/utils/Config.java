package com.wyj.myqq.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wyj on 2017/3/17.
 */

public class Config {

    public static int generateqqNumber() {
        String uuid = UUID.randomUUID().toString(); // xxxx-xxx-xxx-xxxx-xxx
        int hashCode = Math.abs(uuid.hashCode());
        return  hashCode;
    }

    /**
     * 判断电话号码是否符合格式.
     *
     * @param inputText the input text
     * @return true, if is phone
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }



}

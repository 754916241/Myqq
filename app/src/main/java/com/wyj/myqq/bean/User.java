package com.wyj.myqq.bean;

import java.io.Serializable;

/**
 * Created by wyj on 2016/7/1.
 */
public class User implements Serializable{
    private static final long serialVersionUID = 7894568755L;
    private String qqnumber;
    private String nickname, sex, age, truename, password, signature, token, image,phone;

    public User() {
    }

    public User(String qqnumber, String nickname, String truename, String sex, String age,String phone, String token, String signature, String image) {
        this.qqnumber = qqnumber;
        this.phone = phone;
        this.image = image;
        this.token = token;
        this.signature = signature;
        this.truename = truename;
        this.age = age;
        this.sex = sex;
        this.nickname = nickname;
    }

    public User(String qqnumber, String nickname, String truename, String sex, String age, String token, String signature, String image) {
        this.qqnumber = qqnumber;
        this.nickname = nickname;
        this.truename = truename;
        this.sex = sex;
        this.age = age;
        this.token = token;
        this.signature = signature;
        this.image = image;
    }


    public String getQQnumber() {
        return qqnumber;
    }

    public void setQQnumber(String qqnumber) {
        this.qqnumber = qqnumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String name) {
        this.nickname = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String username) {
        this.truename = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
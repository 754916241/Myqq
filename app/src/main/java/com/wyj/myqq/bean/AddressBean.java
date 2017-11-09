package com.wyj.myqq.bean;

/**
 * Created by 180321 on 2017/11/7.
 */

public class AddressBean {

    private String description,address;

    public AddressBean(String description, String address) {
        this.description = description;
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

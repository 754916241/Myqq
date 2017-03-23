package com.example.festival_ui.bean;

import java.util.Date;

/**
 * Created by wyj on 2016/6/27.
 */
public class Festival {

    private int id;

    public Festival(int id, String name) {
        this.id = id;
        this.name = name;
//        this.desc = desc;
//        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name,desc;
    private Date data;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}

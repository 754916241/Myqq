package com.example.festival_ui.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyj on 2016/6/27.
 */
public class FesitivalLab {

    public  static FesitivalLab instance;
    private List<Festival> festivals = new ArrayList<Festival>();
    private List<Msg> msgs = new ArrayList<Msg>();
    /**
     * 单例模式
     */
    private FesitivalLab(){
        festivals.add(new Festival(1,"国庆节"));
        festivals.add(new Festival(2,"中秋节"));
        festivals.add(new Festival(3,"儿童节"));
        festivals.add(new Festival(4,"端午节"));
        festivals.add(new Festival(5,"七夕节"));
        festivals.add(new Festival(6,"春节"));
        msgs.add(new Msg(1,1,"哈哈"));
        msgs.add(new Msg(2,1,"嘻嘻"));
        msgs.add(new Msg(3,1,"拉拉"));
        msgs.add(new Msg(4,1,"嗯嗯"));
        msgs.add(new Msg(5,1,"吼吼"));
        msgs.add(new Msg(6,1,"嘎嘎"));
        msgs.add(new Msg(7,1,"么么"));
        msgs.add(new Msg(8,1,"讷讷"));


    }


    public List<Msg> getMsgByFestivalId(int id){
        List<Msg> msgs1 = new ArrayList<>();
        for (Msg msg : msgs1) {
            if (msg.getFestivalId() == id) {
               msgs.add(msg);
            }
        }
        return msgs;
    }

    public Msg getMsgById(int id){
        for (Msg  msg : msgs) {
            if (msg.getId() == id) {
                return msg;
            }
        }
        return null;
    }

    public List<Festival> getFestivals(){
        return  new ArrayList<Festival>(festivals);
    }

    public Festival getFestivalById(int id){
        for (Festival festival : festivals) {
            if (festival.getId() == id) {
                return festival;
            }
        }
        return null;
    }

    public  static FesitivalLab getInstance(){
        if(instance==null){
            synchronized (FesitivalLab.class){
                if(instance==null){
                    instance = new FesitivalLab();
                }
            }
        }
        return  instance;
    }
}

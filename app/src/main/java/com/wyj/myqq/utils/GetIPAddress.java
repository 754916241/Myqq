package com.wyj.myqq.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wyj on 2017/6/11.
 */

public class GetIPAddress {

    private static String localip;

    public static String getLocalIP(){


            new AsyncTask<Void,Void,String>(){

                @Override
                protected String doInBackground(Void... params) {
                    InetAddress ia= null;
                    try {
                        ia=ia.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    localip=ia.getHostAddress();
                    Log.d("localip",localip);
                    return localip;
                }

            }.execute();

            return localip;

    }
}

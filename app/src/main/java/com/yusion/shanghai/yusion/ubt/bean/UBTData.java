package com.yusion.shanghai.yusion.ubt.bean;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion.ubt.sql.UBTEvent;

import java.util.List;

/**
 * Created by ice on 2017/9/15.
 */

public class UBTData {
    public String mobile;
    public String token;
    public List<UBTEvent> data;
    public String imei;
    public Gps gps = new Gps();
    public boolean rooted;
    public String imsi;
    public String app;
    public String system;
    public String brand;
    public String os_version;
    public String factory;
    public String model;

    public static class Gps {
        public String longitude;
        public String latitude;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

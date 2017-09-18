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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

package com.yusion.shanghai.yusion.bean.upload;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/7/19.
 */

public class ListImgsReq {
    public String clt_id;
    public String app_id;
    public String label;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

package com.yusion.shanghai.yusion.bean.auth;

/**
 * Created by ice on 2017/12/15.
 */

//校验用户三要素

import com.google.gson.Gson;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class Check3ElementsResp {

    /**
     * match : 1
     */

    public String match;


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

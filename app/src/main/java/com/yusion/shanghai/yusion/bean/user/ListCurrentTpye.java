package com.yusion.shanghai.yusion.bean.user;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/8/23.
 */

public class ListCurrentTpye {
    public String lender="";
    public String lender_sp="";
    public String guarantor="";
    public String guarantor_sp="";

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

/*
            "code": 0,
            "msg": "获取成功",
            "data": {
                        "lender": "5be9490a871f11e7b7f902f1f38b2f4a"，
                        "lender_sp":"ssss",
                        "guarantor":"ssss",
                        "guarantor_sp":"asd"
                     }
*/


}

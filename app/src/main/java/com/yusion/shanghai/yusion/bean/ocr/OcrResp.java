package com.yusion.shanghai.yusion.bean.ocr;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by ice on 2017/7/24.
 */

public class OcrResp implements Serializable {

    /**
     * showapi_res_code : 0
     * showapi_res_body : {"area":"宁夏回族自治区 吴忠市 利通区","idNo":"640302198809070033","birthday":"1988-09-07","nationality":"回","ret_code":0,"msg":"识别成功!","town":"利通区","province":"宁夏","city":"吴忠","birth":"1988年09月07日","addr":"江苏省苏州市工业园区七爱路258号402室","lastflag":"0","sex":"男","flag":true,"name":"殷龙"}
     * showapi_res_error :
     */

    public int showapi_res_code;
    public ShowapiResBodyBean showapi_res_body;
    public String showapi_res_error;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class ShowapiResBodyBean implements Serializable {
        /**
         * area : 宁夏回族自治区 吴忠市 利通区
         * idNo : 640302198809070033
         * birthday : 1988-09-07
         * nationality : 回
         * ret_code : 0
         * msg : 识别成功!
         * town : 利通区
         * province : 宁夏
         * city : 吴忠
         * birth : 1988年09月07日
         * addr : 江苏省苏州市工业园区七爱路258号402室
         * lastflag : 0
         * sex : 男
         * flag : true
         * name : 殷龙
         */

        public String area = "";
        public String idNo = "";
        public String birthday = "";
        public String nationality = "";
        public int ret_code;
        public String msg = "";
        public String town = "";
        public String province = "";
        public String city = "";
        public String birth = "";
        public String addr = "";
        public String lastflag = "";
        public String sex = "";
        public boolean flag;
        public String name = "";

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}

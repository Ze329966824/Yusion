package com.yusion.shanghai.yusion.bean.upload;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by aa on 2017/9/18.
 */

public class ContactPersonInfoReq {

    /**
     * imei :
     * imsi :
     * mac :
     * app : Yusion/Yusion4s
     * gps :
     * token :
     * mobile : 13892839048
     * fingerprint : 同盾设备指纹
     * system : 手机系统
     * factory : 厂家
     * model : 型号
     * brand : 手机品牌
     * os_version : 操作系统版本
     * data : {"name":"姓名","mobile":"手机号","contact_list":[{"display_name":"老哥","data1":"13323432354"},{"display_name":"老马","data1":"13823432354"}]}
     */

    public String imei;
    public String imsi;
    public String mac;
    public String app;
    public String gps;
    public String token;
    public String mobile;
    public String fingerprint;
    public String system;
    public String factory;
    public String model;
    public String brand;
    public String os_version;
    public DataBean data = new DataBean();


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class DataBean {
        /**
         * name : 姓名
         * mobile : 手机号
         * contact_list : [{"display_name":"老哥","data1":"13323432354"},{"display_name":"老马","data1":"13823432354"}]
         */
        public String action;
        public String clt_nm;
        public String mobile;
        public List<String> raw_data;
        public List<ContactListBean> contact_list;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }


        public static class ContactListBean {
            /**
             * display_name : 老哥
             * data1 : 13323432354
             */

            public String display_name;
            public String data1;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }

        }
    }
}

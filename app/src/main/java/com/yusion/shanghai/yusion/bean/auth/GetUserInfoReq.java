package com.yusion.shanghai.yusion.bean.auth;

/**
 * Created by ice on 2017/8/4.
 */

public class GetUserInfoReq {
    public String id_no = "";
    public String clt_nm = "";

    public GetUserInfoReq() {
    }

    public GetUserInfoReq(String id_no, String clt_nm) {
        this.id_no = id_no;
        this.clt_nm = clt_nm;
    }
}
package com.yusion.shanghai.yusion.bean.user;

/**
 * Created by ice on 2017/8/4.
 */

public class GetClientInfoReq {
    public String id_no = "";
    public String clt_nm = "";
    public String update = "";//update为1时更新用户

    public GetClientInfoReq() {
    }

    public GetClientInfoReq(String id_no, String clt_nm) {
        this.id_no = id_no;
        this.clt_nm = clt_nm;
    }

    public GetClientInfoReq(String id_no, String clt_nm, String update) {
        this.id_no = id_no;
        this.clt_nm = clt_nm;
        this.update = update;
    }
}
package com.yusion.shanghai.yusion.bean.user;

/**
 * Created by ice on 2017/8/4.
 */

public class GetGuarantorInfoReq {
    public String id_no = "";
    public String clt_nm = "";
    public String mobile = "";
    public String social_ship = "";

    //用户有担保人时候可以不用传姓名等字段
    public GetGuarantorInfoReq() {
    }

    public GetGuarantorInfoReq(String id_no, String clt_nm, String mobile, String social_ship) {
        this.id_no = id_no;
        this.clt_nm = clt_nm;
        this.mobile = mobile;
        this.social_ship = social_ship;
    }
}
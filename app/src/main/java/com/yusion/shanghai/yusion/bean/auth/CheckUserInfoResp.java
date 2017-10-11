package com.yusion.shanghai.yusion.bean.auth;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/8/4.
 */

public class CheckUserInfoResp {

    /**
     * name : 黄耿嘉
     * mobile : 17621066549
     * role : 客户
     */

    public String name;
    public String mobile;
    public String role;
    public boolean commited;
    public boolean is_agree;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

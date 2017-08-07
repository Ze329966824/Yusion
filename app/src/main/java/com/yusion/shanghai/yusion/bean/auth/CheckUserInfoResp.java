package com.yusion.shanghai.yusion.bean.auth;

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

    @Override
    public String toString() {
        return "CheckUserInfoResp{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", role='" + role + '\'' +
                ", commited=" + commited +
                '}';
    }
}

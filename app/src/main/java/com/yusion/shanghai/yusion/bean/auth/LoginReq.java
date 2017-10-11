package com.yusion.shanghai.yusion.bean.auth;

import com.google.gson.Gson;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class LoginReq {
    public String mobile;
    public String verify_code;
    public String reg_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

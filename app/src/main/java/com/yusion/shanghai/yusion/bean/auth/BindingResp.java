package com.yusion.shanghai.yusion.bean.auth;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/9/26.
 */

public class BindingResp {
    public String token;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

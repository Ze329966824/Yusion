package com.yusion.shanghai.yusion.bean.auth;

import com.google.gson.Gson;

/**
 * Created by aa on 2017/9/21.
 */

public class CheckHasAgreedReq {
    public boolean is_agree;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

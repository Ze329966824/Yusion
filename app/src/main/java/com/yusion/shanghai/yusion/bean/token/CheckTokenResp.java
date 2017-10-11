package com.yusion.shanghai.yusion.bean.token;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/7/26.
 */

public class CheckTokenResp {
    public boolean valid;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

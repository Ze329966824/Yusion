package com.yusion.shanghai.yusion.bean.auth;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/9/26.
 */

public class BindingReq {
    /**
     * mobile :
     * verify_code :
     * open_id :
     * source :
     * reg_id :
     * unionid :
     */

    public String mobile;
    public String verify_code;
    public String open_id;
    public String source;
    public String reg_id;
    public String unionid;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

package com.yusion.shanghai.yusion.bean.auth;

import com.google.gson.Gson;

/**
 * Created by LX on 2017/9/12.
 */

public class UpdateResp {


    /**
     * version : 3.0.0
     * file_name : prod-yusion-3.0.0-build-56.apk
     * download_url : http://yusiontech-test.oss-cn-hangzhou.aliyuncs.com/dist%2Fprod-yusion-3.0.0-build-56.apk?OSSAccessKeyId=LTAIZ6W1Y7Q8aqTx&Expires=1505210973&Signature=8T%2F5kEqvfvT5u9LQI0leURmgM1Q%3D
     * change_log : change log1
     * change log2
     * change log3
     */


    public String download_url;



    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

package com.yusion.shanghai.yusion.bean.order;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/7/28.
 */

public class ConfirmFinancePlanReq {
    public String app_id;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

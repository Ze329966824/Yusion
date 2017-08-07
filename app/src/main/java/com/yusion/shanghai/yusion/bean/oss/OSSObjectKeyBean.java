package com.yusion.shanghai.yusion.bean.oss;

import com.yusion.shanghai.yusion.YusionApp;

/**
 * Created by ice on 2017/8/4.
 */

public class OSSObjectKeyBean {
    public OSSObjectKeyBean(String role, String category, String suffix) {
        this.role = role;
        this.category = category;
        this.suffix = suffix;
    }

    public OSSObjectKeyBean() {
    }

    public String client;
    public String mobile = YusionApp.MOBILE;
    public String role;
    public String category;
    public String suffix;
}

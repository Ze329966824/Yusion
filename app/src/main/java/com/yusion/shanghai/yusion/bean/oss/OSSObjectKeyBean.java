package com.yusion.shanghai.yusion.bean.oss;

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
    public String mobile;
    public String role;
    public String category;
    public String suffix;
}

package com.yusion.shanghai.yusion;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion.bean.config.ConfigResp;
import com.yusion.shanghai.yusion.bean.user.UserInfoBean;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ice on 2017/8/3.
 */
public class YusionApp extends Application {

    //是否使用back健退出程序
    public static boolean isBack2Home = false;

    public static String TOKEN;
    public static String MOBILE;
    public static ConfigResp CONFIG_RESP;

    public static UserInfoBean USERINFOBEAN;
    public static String reg_id;

    public static boolean isLogin;
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
        jpush();
    }

    public void clearUserData() {
        TOKEN = "";
        MOBILE = "";
        USERINFOBEAN = null;

        SharedPrefsUtil.getInstance(this).putValue("token", TOKEN);
        SharedPrefsUtil.getInstance(this).putValue("mobile", MOBILE);
    }

    private void jpush() {
        JPushInterface.setDebugMode(true);
        // 初始化 JPush
        JPushInterface.init(this);
        while (reg_id == null) {
            reg_id = JPushInterface.getRegistrationID(YusionApp.this);
        }
    }
}

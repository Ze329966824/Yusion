package com.yusion.shanghai.yusion;

import android.app.Application;
import android.text.TextUtils;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion.bean.config.ConfigResp;
import com.yusion.shanghai.yusion.bean.user.UserInfoBean;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import cn.jpush.android.api.JPushInterface;

import static com.yusion.shanghai.yusion.utils.SharedPrefsUtil.getInstance;

/**
 * Created by ice on 2017/8/3.
 */
public class YusionApp extends Application {

    public static boolean ishaveGuarantee = false;

    //是否使用back健退出程序
    public static boolean isBack2Home = false;

    public static String TOKEN;
    public static String MOBILE;
    public static ConfigResp CONFIG_RESP;

    public static UserInfoBean USERINFOBEAN;
    private static String reg_id;

    public static boolean isLogin;

    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
        jpush();
        TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");
        MOBILE = SharedPrefsUtil.getInstance(this).getValue("mobile", "");
    }

    public void clearUserData() {
        TOKEN = "";
        MOBILE = "";
        USERINFOBEAN = null;

        getInstance(this).putValue("token", TOKEN);
        getInstance(this).putValue("mobile", MOBILE);
    }

    private void jpush() {
        JPushInterface.setDebugMode(true);
        // 初始化 JPush
        JPushInterface.init(this);
        while (TextUtils.isEmpty(reg_id)) {
            reg_id = JPushInterface.getRegistrationID(YusionApp.this);
            getInstance(this).putValue("reg_id", reg_id);
        }
    }
}

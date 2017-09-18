package com.yusion.shanghai.yusion;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.bean.config.ConfigResp;
import com.yusion.shanghai.yusion.bean.user.UserInfoBean;
import com.yusion.shanghai.yusion.ubt.sql.SqlLiteUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

import static com.yusion.shanghai.yusion.utils.SharedPrefsUtil.getInstance;

/**
 * Created by ice on 2017/8/3.
 */
public class YusionApp extends MultiDexApplication {

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
        TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");
        MOBILE = SharedPrefsUtil.getInstance(this).getValue("mobile", "");
        PgyCrashManager.register(this);
        if (BuildConfig.isOnline) {
            Sentry.init("http://6f7b892c19314579936f5c8c6903b64a:50f288ab49d546269f1957df37db6b85@116.62.161.180:9002/5", new AndroidSentryClientFactory(this));
        } else {
            Sentry.init("http://309b4e6a4dc648c9a662e791dbd57cdb:b5ad052a9a154e06aa884ff6781b0f84@116.62.161.180:9002/7", new AndroidSentryClientFactory(this));
        }
        jpush();
        umeng();
        instabug();
        SqlLiteUtil.init(this);
    }


    private void instabug() {
        new Instabug.Builder(this, "41759404bd869009a8eb4ba00967e1f5")
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build();
    }

    private void umeng() {
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //捕获程序崩溃日志
        MobclickAgent.setCatchUncaughtExceptions(true);
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

package com.yusion.shanghai.yusion;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion.bean.config.ConfigResp;

/**
 * Created by ice on 2017/8/3.
 */
public class YusionApp extends Application {

    public static String TOKEN;
    public static String MOBILE;
    public static ConfigResp CONFIG_RESP;

    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
    }
}

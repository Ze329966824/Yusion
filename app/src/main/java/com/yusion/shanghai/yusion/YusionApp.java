package com.yusion.shanghai.yusion;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by ice on 2017/8/3.
 */

public class YusionApp extends Application {

    public static String TOKEN;

    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
    }
}

package com.yusion.shanghai.yusion.settings;

import android.util.Log;

import com.yusion.shanghai.yusion.BuildConfig;

/**
 * Created by ice on 2017/8/3.
 */

public class Settings {
    public static boolean isOnline = BuildConfig.isOnline;
    public static boolean isShameData = false;

    static {
        if (isOnline && isShameData) {
            isShameData = false;
            Log.e("TAG", "如果是线上，记得改isShameData为false");
        }
    }

    private static String ALPHA_SERVER_URL = "http://api.alpha.yusiontech.com:8000/";
//    private static String ALPHA_SERVER_URL = "http://192.168.0.165:8000/";

    public static String PERSON_SERVER_URL = "http://ubt.yusiontech.com:5141/";

    private static String ALPHA_OSS_SERVER_URL = "http://oss.alpha.yusiontech.com:9100";
    public static String SERVER_URL = isOnline ? "http://api.yusiontech.com:8000/" : ALPHA_SERVER_URL;
    public static String OSS_SERVER_URL = isOnline ? "http://oss.yusiontech.com:9100" : ALPHA_OSS_SERVER_URL;
    public static String TEST_TOKEN = "94332209404d79860339284100000000";
    public static String WX_SERVER_URL = "https://api.weixin.qq.com/";
}

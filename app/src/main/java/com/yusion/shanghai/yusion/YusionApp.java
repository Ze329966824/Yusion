package com.yusion.shanghai.yusion;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.bean.config.ConfigResp;
import com.yusion.shanghai.yusion.ubt.sql.SqlLiteUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import java.util.FormatFlagsConversionMismatchException;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;

import static com.yusion.shanghai.yusion.utils.SharedPrefsUtil.getInstance;

/**
 * Created by ice on 2017/8/3.
 */
public class YusionApp extends MultiDexApplication {

    public static String TOKEN;
    public static String MOBILE;
    public static ConfigResp CONFIG_RESP;
    public static boolean isLogin;

    //定位服务类
    public static AMapLocationClient aMapLocationClient;
    //定位参数设置
    public AMapLocationClientOption aMapLocationClientOption;

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
        initPgy();
        initSentry();
        initAMap();
        initJpush();
        initUmeng();
        initInstabug();
        SqlLiteUtil.init(this);
    }

    private void initData() {
        TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");
        MOBILE = SharedPrefsUtil.getInstance(this).getValue("mobile", "");
    }

    private void initPgy() {
        PgyCrashManager.register(this);
    }

    private void initSentry() {
        if (BuildConfig.isOnline) {
            Sentry.init("http://6f7b892c19314579936f5c8c6903b64a:50f288ab49d546269f1957df37db6b85@116.62.161.180:9002/5", new AndroidSentryClientFactory(this));
        } else {
            Sentry.init("http://309b4e6a4dc648c9a662e791dbd57cdb:b5ad052a9a154e06aa884ff6781b0f84@116.62.161.180:9002/7", new AndroidSentryClientFactory(this));
        }
    }

    private void initInstabug() {
        new Instabug.Builder(this, "41759404bd869009a8eb4ba00967e1f5").setInvocationEvent(InstabugInvocationEvent.SHAKE).build();
    }

    private void initUmeng() {
        //禁止默认的页面统计方式
        MobclickAgent.openActivityDurationTrack(false);
        //捕获程序崩溃日志
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    public void clearUserData() {
        TOKEN = "";
        MOBILE = "";
        getInstance(this).putValue("token", TOKEN);
        getInstance(this).putValue("mobile", MOBILE);
    }

    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String reg_id;
        do {
            reg_id = JPushInterface.getRegistrationID(YusionApp.this);
        } while (TextUtils.isEmpty(reg_id));
        getInstance(this).putValue("reg_id", reg_id);
    }

    private void initAMap() {
        aMapLocationClient = new AMapLocationClient(this);
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精准模式
        aMapLocationClientOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        aMapLocationClientOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        aMapLocationClientOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        aMapLocationClientOption.setWifiScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        aMapLocationClientOption.setMockEnable(false);
        //设置定位请求超时时间,默认是30000毫秒，建议不低于8000毫秒
        aMapLocationClientOption.setHttpTimeOut(10000);
        //关闭缓存机制
        aMapLocationClientOption.setLocationCacheEnable(false);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        requestLocation(null);
    }

    public void requestLocation(AMapLocationListener listener) {
        aMapLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功
                String longitude = String.valueOf(aMapLocation.getLongitude());
                String latitude = String.valueOf(aMapLocation.getLatitude());
                Log.e("GPS", String.format(Locale.CHINA, "location Success: {\"latitude\":\"%s\",\"longitude\":\"%s\"}", latitude, longitude));
                getInstance(YusionApp.this).putValue("longitude", longitude);
                getInstance(YusionApp.this).putValue("latitude", latitude);
            } else {
                String errorInfo = String.format(Locale.CHINA, "location Error, ErrCode:%d, errInfo:%s", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
                Log.e("GPS", errorInfo);
                Sentry.capture("GPS:" + errorInfo);
            }
            if (listener != null) {
                listener.onLocationChanged(aMapLocation);
            }
        });
        aMapLocationClient.startLocation();
    }
}

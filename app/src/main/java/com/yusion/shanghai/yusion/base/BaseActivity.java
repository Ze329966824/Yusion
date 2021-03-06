package com.yusion.shanghai.yusion.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion.ui.main.mine.SettingsActivity;
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.util.List;


/**
 * Created by ice on 2017/8/3.
 */


public class BaseActivity extends AppCompatActivity {
    public static final String WX_APP_ID = "wxf2c47c30395cfb84";
    public static final String QQ_APP_ID = "101425795";
    public IWXAPI api;
    public Tencent tencent;
    protected YusionApp myApp;

    public int WIDTH;
    public int HEIGHT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        initData();
    }

    private void initData() {
        WIDTH = this.getWindowManager().getDefaultDisplay().getWidth();
        HEIGHT = this.getWindowManager().getDefaultDisplay().getHeight();
        myApp = ((YusionApp) getApplication());
//        PgyCrashManager.register(this);

        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        api.registerApp(WX_APP_ID);
    }

    public TitleBar initTitleBar(final Activity activity, String title) {
        TitleBar titleBar = (TitleBar) activity.findViewById(R.id.title_bar);
        titleBar.setLeftClickListener(view -> activity.finish());
        titleBar.setImmersive(false);
        titleBar.setTitle(title);
        titleBar.setLeftImageResource(R.mipmap.title_back_arrow);
        titleBar.setBackgroundResource(R.color.system_color);
        titleBar.setTitleColor(activity.getResources().getColor(R.color.title_bar_text_color));
        titleBar.setDividerColor(activity.getResources().getColor(R.color.system_color));
        return titleBar;
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (!isAppOnForeground()) {
//            YusionApp.isForeground = false;
//        }
        MobclickAgent.onPause(this);
        if (getClass().getSimpleName().equals(SettingsActivity.class.getSimpleName())) {
            SettingsActivity settingsActivity = (SettingsActivity) this;
            if (settingsActivity.finishByLoginOut) {
                return;
            }
        }
//        Log.e("isForeground-----------",YusionApp.isForeground+"");
        UBT.addPageEvent(this, "page_hidden", "activity", getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            YusionApp.isForeground = false;
            UBT.addAppEvent(this, "app_pause");
        }
        Log.e("onStop isForeground-----------",YusionApp.isForeground+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getClass().getSimpleName().equals(LaunchActivity.class.getSimpleName())) {
            UBT.addAppEvent(this, "app_start");
        }
        if (!YusionApp.isForeground) {
            YusionApp.isForeground = true;
            UBT.addAppEvent(this, "app_awake");
        }
        UBT.addPageEvent(this, "page_show", "activity", getClass().getSimpleName());
        MobclickAgent.onResume(this);
        for (StackTraceElement i : Thread.currentThread().getStackTrace()) {
            Log.i("getStackTrace-----------", i.toString());
        }

    }
    private boolean isAppOnForeground() {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

}


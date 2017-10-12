package com.yusion.shanghai.yusion.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.util.List;

/**
 * Created by ice on 2017/8/3.
 */
public class BaseActivity extends AppCompatActivity {

    protected YusionApp myApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        initData();
    }

    private void initData() {
        myApp = ((YusionApp) getApplication());
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
        UBT.addPageEvent(this, "page_hidden", "activity", getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            YusionApp.isForeground = false;
            UBT.addAppEvent(this, "app_pause");
        }
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
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        UBT.addAppEvent(this, "app_pause");
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


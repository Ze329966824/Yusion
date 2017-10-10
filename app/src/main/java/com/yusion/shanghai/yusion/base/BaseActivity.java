package com.yusion.shanghai.yusion.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.ui.update.CommitActivity;
import com.yusion.shanghai.yusion.widget.TitleBar;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity {

    protected YusionApp myApp;
    private String dialogMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        myApp = ((YusionApp) getApplication());

//        if (getClass().getSimpleName().equals(LaunchActivity.class.getSimpleName())) {
//            UBT.sendAllUBTEvents(this);
//            UBT.addAppEvent(BaseActivity.this, "app_start");
//        }
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
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UBT.addPageEvent(this, "page_show", "activity", getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    public void toCommitActivity(String clt_id, String role, String title, String state) {
        switch (title) {
            case "个人影像件资料":
                dialogMsg = "个人";
                break;
            case "个人配偶影像件资料":
                dialogMsg = "配偶";
                break;
            case "担保人影像件资料":
                dialogMsg = "担保人";
                break;
            case "担保人配偶影像件资料":
                dialogMsg = "担保人配偶";
                break;
        }
        new AlertDialog.Builder(this)
                .setMessage("确认要更改" + dialogMsg + "信息？")
                .setCancelable(false)
                .setPositiveButton("确认更改", (dialog, which) -> {
                    Intent intent1 = new Intent(BaseActivity.this, CommitActivity.class);
                    intent1.putExtra("clt_id", clt_id);
                    intent1.putExtra("role", role);
                    intent1.putExtra("title", title);
                    intent1.putExtra("commit_state", state);
                    startActivity(intent1);
                    finish();
                })
                .setNegativeButton("放弃更改", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        UBT.addAppEvent(this, "app_end");
    }
}


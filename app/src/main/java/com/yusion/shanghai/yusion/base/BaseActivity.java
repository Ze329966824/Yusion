package com.yusion.shanghai.yusion.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.ui.update.CommitActivity;
import com.yusion.shanghai.yusion.widget.TitleBar;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String APP_ID = "wxf2c47c30395cfb84";
    public IWXAPI api;
    protected YusionApp myApp;
    private String dialogMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        myApp = ((YusionApp) getApplication());
//        PgyCrashManager.register(this);

        api = WXAPIFactory.createWXAPI(this,APP_ID,false);
        api.registerApp(APP_ID);
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
    public void onClick(View v) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
//        PgyFeedbackShakeManager.setShakingThreshold(1000);
        // 以对话框的形式弹出
//        PgyFeedbackShakeManager.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UBT.addPageEvent(this, "onPause", "activity", getClass().getSimpleName());
//        PgyFeedbackShakeManager.unregister();
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
        UBT.addPageEvent(this, "onResume", "activity", getClass().getSimpleName());
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
}


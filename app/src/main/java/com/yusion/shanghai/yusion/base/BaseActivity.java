package com.yusion.shanghai.yusion.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;
import com.umeng.analytics.MobclickAgent;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.ui.update.CommitActivity;
import com.yusion.shanghai.yusion.widget.TitleBar;

/**
 * Created by ice on 2017/8/3.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected YusionApp myApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        myApp = ((YusionApp) getApplication());
//        PgyCrashManager.register(this);
        instabug();
    }

    private void instabug() {
        new Instabug.Builder(myApp, "41759404bd869009a8eb4ba00967e1f5")
                .setInvocationEvent(InstabugInvocationEvent.SHAKE)
                .build();
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
        MobclickAgent.onResume(this);
    }

    public void toCommitActivity(String clt_id, String role, String title, String state) {
        new AlertDialog.Builder(this)
                .setMessage("您确认要更改您的配偶信息？")
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

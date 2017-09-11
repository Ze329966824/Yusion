package com.yusion.shanghai.yusion.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.ui.update.CommitActivity;
import com.yusion.shanghai.yusion.ui.upload.UploadLabelListActivity;
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
        PgyCrashManager.register(this);

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
        PgyFeedbackShakeManager.setShakingThreshold(1000);
        // 以对话框的形式弹出
        PgyFeedbackShakeManager.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PgyFeedbackShakeManager.unregister();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.removeActivity(this);
    }


    public void toUploadLabelListDialog(String clt_id,String role,String title){
        new AlertDialog.Builder(this)
                .setMessage("资料上传成功,请继续上传影像件")
                .setPositiveButton("立即上传", (dialog, which) -> {
                    dialog.dismiss();
                    toUploadLabelListActivity(clt_id,role,title);
                })
                .setNegativeButton("稍后上传", (dialog, which) -> {
                    Intent intent = new Intent(this, CommitActivity.class);
                    dialog.dismiss();

                    startActivity(intent);
                    finish();
                }).show();
    }


    public void toUploadLabelListActivity(String clt_id,String role,String title){
        Intent intent = new Intent(this,UploadLabelListActivity.class);
        intent.putExtra("clt_id", clt_id);
        intent.putExtra("role", role);
        intent.putExtra("title",title);
        startActivity(intent);
        finish();
    }
}

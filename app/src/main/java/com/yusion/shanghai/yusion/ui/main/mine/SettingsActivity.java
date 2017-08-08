package com.yusion.shanghai.yusion.ui.main.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.yusion.shanghai.yusion.BuildConfig;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;

import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;


public class SettingsActivity extends BaseActivity {
    private String desc;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initTitleBar(this, getResources().getString(R.string.main_setting_title));
        TextView versionCodeTv = (TextView) findViewById(R.id.settings_version_code_tv);
        versionCodeTv.setText(BuildConfig.VERSION_NAME);

//        initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.main_setting_agreement_layout:  //用户协议
//                Intent intent2 = new Intent(SettingActivity.this, WebViewActivity.class);
//                intent2.putExtra("title", getResources().getString(R.string.main_setting_agreement));
//                intent2.putExtra("url", JsonUtils.getConfigValueFromKey("agreement_url"));
//                startActivity(intent2);
                break;
            case R.id.main_setting_logout_layout:    //退出登录
                showLogoutDialog();
                break;
            case R.id.main_setting_version_name_layout:   //版本信息
                PgyUpdateManager.setIsForced(true); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
                PgyUpdateManager.register(this, null);
//                initUpdate();
                break;
        }
    }

    private void initUpdate() {

        PgyUpdateManager.register(SettingsActivity.this, null, new UpdateManagerListener() {
            @Override
            public void onNoUpdateAvailable() {
            }

            @Override
            public void onUpdateAvailable(String s) {
                //                Log.e("result:           ", s);
                final AppBean appBean = getAppBeanFromString(s);
                desc = appBean.getReleaseNote();
                url = appBean.getDownloadURL();
//                Log.e("desc=           ", desc);
//                Log.e("url=           ", url);
//                UpdateUtil.showUpdateDialog(SettingActivity.this, desc, false, url);
                UpdateManagerListener.updateLocalBuildNumber(s);
            }
        });


    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(SettingsActivity.this)
                .setCancelable(true)
                .setTitle(getResources().getString(R.string.mine_logout_dialog_title))
                .setPositiveButton(getResources().getString(R.string.mine_logout_dialog_sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout();

                    }
                })
                .setNegativeButton(getResources().getString(R.string.mine_logout_dialog_cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                })
                .setMessage(getResources().getString(R.string.mine_logout_dialog_msg))
                .show();
    }

    private void logout() {
        myApp.clearUserData();

        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

package com.yusion.shanghai.yusion.ui.main.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        initTitleBar(this, getResources().getString(R.string.main_setting_title));
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
//                AuthApi.checkUpdate(this, "正在检查更新...", new OnDataCallBack<CheckUpdateResp>() {
//                    @Override
//                    public void callBack(CheckUpdateResp resp) {
//                        Log.e("TAG", "callBack: " + resp);
//                        if (resp.update == 1) {
//                            UpdateUtil.showUpdateDialog(SettingActivity.this, resp.desc, false, resp.url);
//                        } else {
//                            Toast.makeText(SettingActivity.this, "您的版本已是最高版本", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                break;
        }
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
//        ((WangDaiApp) getApplication()).clearUserData();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        finish();
    }
}
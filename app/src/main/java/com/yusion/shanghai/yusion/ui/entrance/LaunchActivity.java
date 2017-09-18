package com.yusion.shanghai.yusion.ui.entrance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.yusion.shanghai.yusion.BuildConfig;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.utils.UpdateUtil;

import java.util.Date;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (Settings.isOnline) {
            checkVersion();
        } else {
            checkServerUrl();
        }
    }

    private void checkVersion() {
        String versionCode =  BuildConfig.VERSION_NAME;
        //product：调用oss接口更新
        AuthApi.update(this, "yusion", data -> {
            if (data != null) {
                if (!versionCode.contains(data.version)) {
                    UpdateUtil.showUpdateDialog(LaunchActivity.this, data.change_log, true, data.download_url);
                } else {
                    getConfigJson();
                }
            }else {
                getConfigJson();
            }

        });
    }

    private void checkServerUrl() {
        if (!Settings.isOnline) {
            String str = SharedPrefsUtil.getInstance(this).getValue("SERVER_URL", "");
            if (!TextUtils.isEmpty(str)) {
                new AlertDialog.Builder(this)
                        .setTitle("请确认服务器地址：")
                        .setMessage(str)
                        .setPositiveButton("是", (dialog, which) -> {
                            Settings.SERVER_URL = str;
                            getConfigJson();
                            dialog.dismiss();
                        })
                        .setNegativeButton("否", (dialog, which) -> {
                            getConfigJson();
                            dialog.dismiss();
                        })
                        .show();
            } else {
                getConfigJson();
            }
        } else {
            getConfigJson();
        }
    }

    private void getConfigJson() {
        ConfigApi.getConfigJson(LaunchActivity.this, resp -> {
            goNextActivity();
        });
    }

    private void goNextActivity() {
        AuthApi.checkToken(this, new OnItemDataCallBack<CheckTokenResp>() {
            @Override
            public void onItemDataCallBack(CheckTokenResp data) {
                if (data.valid) {
                    onTokenValid();
                } else {
                    onTokenInvalid();
                }
            }
        });
    }

    private void onTokenInvalid() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private void onTokenValid() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private long lastClickBackTime = 0;

    @Override
    public void onBackPressed() {
        if (lastClickBackTime == 0) {
            lastClickBackTime = new Date().getTime();
        } else {
            long currentClickBackTime = new Date().getTime();
            if (currentClickBackTime - lastClickBackTime < 1000) {
                new AlertDialog.Builder(this).setMessage("是否退出APP?")
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                            lastClickBackTime = currentClickBackTime;
                        })
                        .setPositiveButton("确定", (dialog, which) -> super.onBackPressed())
                        .show();
            } else {
                lastClickBackTime = currentClickBackTime;
            }
        }
    }
}

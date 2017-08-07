package com.yusion.shanghai.yusion.ui.entrance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import java.util.Date;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        YusionApp.TOKEN = SharedPrefsUtil.getInstance(this).getValue("token", "");
        getConfigJson();
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

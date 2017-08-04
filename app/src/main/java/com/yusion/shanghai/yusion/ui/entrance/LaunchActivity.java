package com.yusion.shanghai.yusion.ui.entrance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;

import java.util.Date;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

//        getConfigJson();
//        有用
//        ClearEditText clearEditText = new ClearEditText(this);
//        clearEditText.setText(Settings.server_url);
//        if (Settings.isDevelopment) {
//            new AlertDialog.Builder(this).setTitle("服务器地址:").setView(clearEditText).setPositiveButton("确定", (dialog, which) -> {
//                Settings.server_url = clearEditText.getText().toString();
//
//                checkUpdate();
//                startService(new Intent(LauncherActivity.this, PostCacheMobileDataService.class));
//
//                dialog.dismiss();
//            }).show();
//        } else {
//            checkUpdate();
//            startService(new Intent(LauncherActivity.this, PostCacheMobileDataService.class));
//        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                finish();

            }
        },2000);


    }

//    private void checkUpdate() {
//        AuthApi.checkUpdate(this, "正在检查更新...", resp -> {
//            if (resp.update == 1) {
//                UpdateUtil.showUpdateDialog(LauncherActivity.this, resp.desc, true, resp.url);
//            } else {
//                getConfigJson();
//            }
//        });
//    }

    private void getConfigJson() {
        /*暂时*/
//        ConfigApi.getConfigJson(LauncherActivity.this, resp -> {
//            WangDaiApp.mConfigResp = resp;
        goNextActivity();
//        });
    }

    private void goNextActivity() {
        //        if (SharedPrefsUtil.getInstance(LauncherActivity.this).getValue("isFirstLaunch", true)) {
//            SharedPrefsUtil.getInstance(LauncherActivity.this).putValue("isFirstLaunch", false);
//            startActivity(new Intent(LauncherActivity.this, SplashActivity.class));
//            LauncherActivity.this.finish();
//        } else {


//        WangDaiApp.mToken = SharedPrefsUtil.getInstance(LauncherActivity.this).getValue("token", "");
//        AuthApi.checkToken(LauncherActivity.this, new OnDataCallBack<CheckTokenResp>() {
//            @Override
//            public void callBack(CheckTokenResp resp) {
//                if (resp.valid) {
//                    onTokenValid();
//                } else {
//                    onTokenInvalid();
//                }
//            }
//        });
//        }
    }

//    private void onTokenInvalid() {
//        WangDaiApp.isLogin = true;
//        startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
//        LaunchActivity.this.finish();
//    }


//    private void onTokenValid() {
//        WangDaiApp.isLogin = false;
//        startActivity(new Intent(LauncherActivity.this, MainActivity.class));
//        LauncherActivity.this.finish();
//    }

    private long lastClickBackTime = 0;


//    @Override
//    public void onBackPressed() {
//        if (lastClickBackTime == 0) {
//            lastClickBackTime = new Date().getTime();
//        } else {
//            long currentClickBackTime = new Date().getTime();
//            if (currentClickBackTime - lastClickBackTime < 1000) {
//                new AlertDialog.Builder(this).setMessage("是否退出APP?")
//                        .setNegativeButton("取消", (dialog, which) -> {
//                            dialog.dismiss();
//                            lastClickBackTime = currentClickBackTime;
//                        })
//                        .setPositiveButton("确定", (dialog, which) -> super.onBackPressed())
//                        .show();
//            } else {
//                lastClickBackTime = currentClickBackTime;
//            }
//        }
//    }
}

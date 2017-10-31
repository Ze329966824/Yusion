package com.yusion.shanghai.yusion.ui.entrance;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;
import com.yusion.shanghai.yusion.BuildConfig;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.utils.MobileDataUtil;
import com.yusion.shanghai.yusion.utils.PopupDialogUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.utils.UpdateUtil;

import org.json.JSONArray;

import java.util.Date;

import static com.yusion.shanghai.yusion.ui.entrance.LaunchActivity.READ_CONTACTS_CODE;
import static com.yusion.shanghai.yusion.ui.entrance.LaunchActivity.READ_PHONESTATE_CODE;

@PermissionsRequestSync(permission = {Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_PHONE_STATE},
        value = {READ_CONTACTS_CODE, READ_PHONESTATE_CODE})
public class LaunchActivity extends BaseActivity {
    public static final int READ_CONTACTS_CODE = 10;
    public static final int READ_PHONESTATE_CODE = 9;

    private boolean isRead;
    private boolean isPhoneState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
//        if (Build.MANUFACTURER.toUpperCase().equals("MEIZU")) {
//            onAllPermissionGranted();
//        } else {
//            getPermisson();
//        }
        if (Settings.isOnline) {
            checkVersion();
        } else {
            checkServerUrl();
        }
    }

    public void getPermisson() {
        Permissions4M
                .get(LaunchActivity.this)
                .requestSync();
    }

    private void checkVersion() {
        String versionCode = BuildConfig.VERSION_NAME;
        //product：调用oss接口更新
        AuthApi.update(this, "yusion", data -> {
            if (data != null) {
                int result = splitVersion(versionCode.substring(1)).compareTo(splitVersion(data.version));
                if (result < 0) {
                    UpdateUtil.showUpdateDialog(LaunchActivity.this, data.change_log, true, data.download_url);
                } else {
                    getConfigJson();
                }
            } else {
                getConfigJson();
            }

        });
    }
    private String str = SharedPrefsUtil.getInstance(this).getValue("SERVER_URL", "");

    private void checkServerUrl() {
        if (!Settings.isOnline) {
            Log.e("当前服务器地址：","启动前"+str);
            if (!TextUtils.isEmpty(str)) {
                PopupDialogUtil.showTwoButtonsDialog(this, "还原", "确定", "服务器地址为：\n" + str, new PopupDialogUtil.OnOkClickListener() {
                            @Override
                            public void onOkClick(Dialog dialog) {
//                                YusionApp.isChangeURL = false;
                                Settings.SERVER_URL = "http://api.alpha.yusiontech.com:8000/";
//                                Api.initRetrofit();
                                dialog.dismiss();
                                getConfigJson();
                            }
                        }, new PopupDialogUtil.OnCancelClickListener() {
                            @Override
                            public void onCancelClick(Dialog dialog) {
                                Settings.SERVER_URL = str;
//                                Api.initRetrofit();
                                dialog.dismiss();
                                getConfigJson();
                            }
                        }

                );
//                new AlertDialog.Builder(this)
//                        .setTitle("请确认服务器地址：")
//                        .setMessage(str)
//                        .setPositiveButton("是", (dialog, which) -> {
//                            Settings.SERVER_URL = str;
//                            getConfigJson();
//                            dialog.dismiss();
//                        })
//                        .setNegativeButton("否", (dialog, which) -> {
//                            getConfigJson();
//                            dialog.dismiss();
//                        })
//                        .show();
            } else {
                getConfigJson();
            }
        } else {
            getConfigJson();
        }
    }

    private void getConfigJson() {
        Log.e("当前服务器地址：","启动后"+str);
        ConfigApi.getConfigJson(LaunchActivity.this, () -> goNextActivity());
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

    private void onTokenInvalid() {//无token
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    private void onTokenValid() {//有token
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

    @PermissionsGranted({READ_CONTACTS_CODE, READ_PHONESTATE_CODE})
    public void syncGranted(int code) {

        if (code == READ_CONTACTS_CODE) {
            isRead = true;
        }
        if (code == READ_PHONESTATE_CODE) {
            isPhoneState = true;
        }
        if (isRead && isPhoneState) {
            onAllPermissionGranted();
        }
    }

    private void onAllPermissionGranted() {
        JSONArray jsonArray = MobileDataUtil.getUserData(this, "contact");
        if (jsonArray.length() > 0 && !jsonArray.toString().equals("")) {
            if (Settings.isOnline) {
                checkVersion();
            } else {
                checkServerUrl();
            }
        }
    }

    @PermissionsDenied({READ_CONTACTS_CODE, READ_PHONESTATE_CODE})
    public void syncDenied(int code) {
        switch (code) {
            case READ_CONTACTS_CODE:
                finish();
                break;
            case READ_PHONESTATE_CODE:
                finish();
                break;
            default:
                break;
        }
    }

    //    @PermissionsRationale
//二次授权时回调，用于解释为何需要此权限
    /*
    @PermissionsCustomRationale({READ_CONTACTS_CODE, READ_PHONESTATE_CODE})
    public void rationale(int code) {
        switch (code) {
            case READ_CONTACTS_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("通讯录权限申请:\n需要您打开相应权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.get(LaunchActivity.this)
                                        .requestOnRationale()
                                        .requestPermissions(Manifest.permission.READ_CONTACTS)
                                        .requestCodes(READ_CONTACTS_CODE)
                                        .request();

                            }
                        })
                        .show();
                break;
            case READ_PHONESTATE_CODE:
                new AlertDialog.Builder(this)
                        .setMessage("手机状态权限申请:\n需要您打开相应权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Permissions4M.get(LaunchActivity.this)
                                        .requestOnRationale()
                                        .requestPermissions(Manifest.permission.READ_PHONE_STATE)
                                        .requestCodes(READ_PHONESTATE_CODE)
                                        .request();
                            }
                        })
                        .show();
                break;
        }
    }
    */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permissions4M.onRequestPermissionsResult(LaunchActivity.this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private String splitVersion(String s) {
        String ss = null;
        char[] str = s.toCharArray();
        ss = String.valueOf(str[0]) + String.valueOf(str[2]) + String.valueOf(str[4]);
        return ss;
    }
}

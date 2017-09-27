package com.yusion.shanghai.yusion.jpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class JpushDialogActivity extends BaseActivity {
    private String mobile = null;
    private String title = null;
    private String content = null;
    private String app_st = null;
    private String app_id = null;
    private String category = null;

/*    "reg_id":xxxx,
            "mobile": 138xxx,
            "title":xxxx,
            "content":xxxx,
            "app_st": "SubmitApplication_Submit_PASS",
            "app_id": 可为空,
            "category": "login",*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_dialog);
        try {
            initJpush();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initJpush() throws JSONException {
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra("jsonObject");
            JSONObject jo = new JSONObject(stringExtra);
            mobile = jo.optString("mobile");
            title = jo.optString("title");
            content = jo.optString("content");
            app_st = jo.optString("app_st");
            app_id = jo.optString("app_id");
            category = jo.optString("category");
            Log.e("jpush", "category:" + category);
            JpushDialog();
        } else {
            finish();
        }

    }

    private void JpushDialog() {

        if (YusionApp.isLogin) {
            if (this.mobile.equals(YusionApp.MOBILE)) {

                switch (category) {
                    case "login":
                        new AlertDialog.Builder(JpushDialogActivity.this)
                                .setCancelable(false)
                                .setMessage(content)
                                .setPositiveButton("确定", (dialog, which) -> {
                                    myApp.clearUserData();
                                    startActivity(new Intent(JpushDialogActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .show();
                        break;

                    case "application":
                        new AlertDialog.Builder(JpushDialogActivity.this)
                                .setCancelable(false)
                                .setTitle(title)
                                .setMessage(content)
                                .setPositiveButton("知道啦", (dialog, which) -> {
                                    dialog.dismiss();
                                    finish();
                                })
                                .show();
                        break;
                    default:
                        new AlertDialog.Builder(JpushDialogActivity.this)
                                .setTitle(title)
                                .setMessage(content)
                                .setCancelable(false)
                                .setPositiveButton("这不是一个推送", (dialog, which) -> {
                                    dialog.dismiss();
                                    finish();
                                })
                                .show();
                        break;
                }
            } else {finish();}
        } else {finish();}
    }
}

package com.yusion.shanghai.yusion.jpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

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
            JpushDialog();
        }

    }

    private void JpushDialog() {
        switch (category) {
            case "login":
                if (YusionApp.isLogin) {
                    if (mobile.equals(YusionApp.MOBILE)) {
                        new AlertDialog.Builder(JpushDialogActivity.this)
                                .setCancelable(false)
                                .setTitle("")
                                .setMessage(content)
                                .setPositiveButton("确定", (dialog, which) -> {
                                    myApp.clearUserData();
                                    startActivity(new Intent(JpushDialogActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .show();
                    } else {
                        finish();
                    }
                }
                break;
            default:
                new AlertDialog.Builder(JpushDialogActivity.this)
                        .setTitle(title)
                        .setMessage(content)
                        .setCancelable(false)
                        .setPositiveButton("知道啦", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        });
                break;
        }
    }
}

package com.yusion.shanghai.yusion.jpush;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class JpushDialogActivity extends Activity {
    private String mobile = null;
    private String title = null;
    private String content = null;
    private String app_st = null;
    private String app_id = null;
    private String stringExtra = null;

/*    "reg_id":xxxx,
            "mobile": 138xxx,
            "title":xxxx,
            "content":xxxx,
            "app_st": "SubmitApplication_Submit_PASS",
            "app_id": 可为空,*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_dialog);
        try {
            initJpush();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        * m每一步都要finish
        * JpushDialogActivity要透明，只显示dialog，，，、、。·
        * */
    }



    private void initJpush() throws JSONException {

            Intent intent = getIntent();
            if (intent != null) {
                stringExtra = intent.getStringExtra("jsonObject");
                JSONObject jo = new JSONObject(stringExtra);
                if (jo != null) {
                    mobile = jo.optString("mobile");
                    title = jo.optString("title");
                    content = jo.optString("content");
                    app_st = jo.optString("app_st");
                    app_id = jo.optString("app_id");
                    JpushDialog();
                }
            }

    }

    private void JpushDialog() {
//        switch (app_st == ){
//            case "1":
//
//                break;
//
//            case "2":
//
//                break;
//
//            case "3":
//
//                break;
//        }

        Log.e("jpush","----------------------------------------");
        new AlertDialog.Builder(JpushDialogActivity.this)
                .setCancelable(true)
                .setTitle("")
//                .setMessage("mobile="+mobile+"\ntitle="+title+"\ncontent="+content+"\napp_st="+app_st+"\napp_id"+app_id)
                .setMessage(stringExtra)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(JpushDialogActivity.this,LoginActivity.class));
                        finish();
                    }
                })
                .show();


    }
}

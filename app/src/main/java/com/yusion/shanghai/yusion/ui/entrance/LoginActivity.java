package com.yusion.shanghai.yusion.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.utils.CheckDataFormatUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginMobileTV;
    private EditText mLoginCodeTV;
    private Button mLoginCodeBtn;
    private Button mLoginSubmitBtn;
    private TextView mLoginAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginMobileTV = (EditText) findViewById(R.id.login_mobile_edt);
        mLoginCodeTV = (EditText) findViewById(R.id.login_code_edt);
        mLoginCodeBtn = (Button) findViewById(R.id.login_code_btn);
        mLoginSubmitBtn = (Button) findViewById(R.id.login_submit_btn);
        mLoginAgreement = (TextView) findViewById(R.id.login_agreement_tv);

        mLoginMobileTV.setText("17621098734");
        mLoginCodeBtn.setOnClickListener(v -> {
            if (!CheckDataFormatUtil.checkMobile(mLoginMobileTV.getText().toString())) {
                Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            } else {
                AuthApi.getVCode(LoginActivity.this, mLoginMobileTV.getText().toString(), data -> {
                    if (data != null) {
                        mLoginCodeTV.setText(data.verify_code);
                    }
                });
            }
        });
        mLoginSubmitBtn.setOnClickListener(v -> {
            LoginReq req = new LoginReq();
            req.mobile = mLoginMobileTV.getText().toString();
            req.verify_code = mLoginCodeTV.getText().toString();
            AuthApi.login(LoginActivity.this, req, data -> {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            });
        });

        mLoginAgreement.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
            intent.putExtra("type", "Agreement");
            startActivity(intent);
        });

    }

    private void loginSuccess(LoginResp resp) {

//        WangDaiApp.isLogin = true;
//        WangDaiApp.mToken = resp.token;
//        WangDaiApp.mMobile = mLoginMobileTV.getText().toString();
//
//        SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", WangDaiApp.mToken);
//        SharedPrefsUtil.getInstance(LoginActivity.this).putValue("mobile", WangDaiApp.mMobile);
//
//        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        finish();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //每次回到登陆界面都需清除缓存
//        myApp.clearUserData();
//    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        //case 1:如果是从SettingActivity注销登录时，stack中有MainActivity和LoginActivity，所以退出应用需要先结束 MainActivity
//        ActivityManager.finishOtherActivityEx(LoginActivity.class);
//    }
}

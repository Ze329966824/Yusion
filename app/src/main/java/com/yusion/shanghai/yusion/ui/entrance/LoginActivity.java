package com.yusion.shanghai.yusion.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.CheckDataFormatUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginMobileTV;
    private EditText mLoginCodeTV;
    private Button mLoginCodeBtn;
    private Button mLoginSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginMobileTV = (EditText) findViewById(R.id.login_mobile_edt);
        mLoginCodeTV = (EditText) findViewById(R.id.login_code_edt);
        mLoginCodeBtn = (Button) findViewById(R.id.login_code_btn);
        mLoginSubmitBtn = (Button) findViewById(R.id.login_submit_btn);
        mLoginMobileTV.setText("17621098734");
        mLoginCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckDataFormatUtil.checkMobile(mLoginMobileTV.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                } else {
                    AuthApi.getVCode(LoginActivity.this, mLoginMobileTV.getText().toString(), new OnItemDataCallBack<GetVCodeResp>() {
                        @Override
                        public void onItemDataCallBack(GetVCodeResp data) {
                            mLoginCodeTV.setText(data.verify_code);
                        }
                    });
                }
            }
        });
        mLoginSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginReq req = new LoginReq();
                req.mobile = mLoginMobileTV.getText().toString();
                req.verify_code = mLoginCodeTV.getText().toString();
                AuthApi.login(LoginActivity.this, req, new OnItemDataCallBack<LoginResp>() {
                    @Override
                    public void onItemDataCallBack(LoginResp data) {

                    }
                });
            }
        });

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}

package com.yusion.shanghai.yusion.ui.entrance;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.Tencent;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.ActivityManager;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.qqapi.QQLoginListener;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.widget.CountDownButtonWrap;

public class LoginActivity extends BaseActivity {

    private EditText mLoginMobileTV;
    private EditText mLoginCodeTV;
    private Button mLoginCodeBtn;
    private Button mLoginSubmitBtn;
    private TextView mLoginAgreement;
    private CountDownButtonWrap mCountDownBtnWrap;
    private QQLoginListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_wx).setOnClickListener(v -> {
            if (!api.isWXAppInstalled()) {
                Toast.makeText(this, "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
                return;
            }
            // 应用的作用域，获取个人信息
            SendAuth.Req req = new SendAuth.Req();
            /**  用于保持请求和回调的状态，授权请求后原样带回给第三方  * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验   */
            req.scope = "snsapi_userinfo";
            req.state = "diandi_wx_login";
            api.sendReq(req);
        });

        tencent = Tencent.createInstance(QQ_APP_ID, LoginActivity.this);
        findViewById(R.id.btn_qq).setOnClickListener(v -> {
            //如果session不可用，则登录，否则说明已经登录
            if (!tencent.isSessionValid()) {
                tencent.login(this, "all", mListener);
            }
        });


        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String pgyer_appid = applicationInfo.metaData.getString("PGYER_APPID");
            Log.e("TAG", "onCreate: pgyer_appid = " + pgyer_appid);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TAG", "onCreate: " + e);
            e.printStackTrace();
        }
        Log.e("TAG", "onCreate: Settings.isOnline = " + Settings.isOnline);
        Log.e("TAG", "onCreate: Settings.SERVER_URL = " + Settings.SERVER_URL);
        Log.e("TAG", "onCreate: Settings.OSS_SERVER_URL = " + Settings.OSS_SERVER_URL);


        YusionApp.isLogin = false;
        mLoginMobileTV = (EditText) findViewById(R.id.login_mobile_edt);
        mLoginCodeTV = (EditText) findViewById(R.id.login_code_edt);
        mLoginCodeBtn = (Button) findViewById(R.id.login_code_btn);
        if (Settings.isOnline) {
            mCountDownBtnWrap = new CountDownButtonWrap(mLoginCodeBtn, "重试", 30, 1);
        } else {
            mCountDownBtnWrap = new CountDownButtonWrap(mLoginCodeBtn, "重试", 5, 1);
        }
        mLoginSubmitBtn = (Button) findViewById(R.id.login_submit_btn);
        mLoginAgreement = (TextView) findViewById(R.id.login_agreement_tv);
        mLoginCodeBtn.setOnClickListener(v -> {

            if (!CheckMobileUtil.checkMobile(mLoginMobileTV.getText().toString())) {
                Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            } else {
                mCountDownBtnWrap.start();
                AuthApi.getVCode(LoginActivity.this, mLoginMobileTV.getText().toString(), data -> {
                    if (data != null) {
                        if (!Settings.isOnline) {
                            mLoginCodeTV.setText(data.verify_code);
                        }
                    }
                });
            }
        });
        mLoginSubmitBtn.setOnClickListener(v -> {
//            startActivity(new Intent(this, UploadLabelListActivity.class));
            if (!CheckMobileUtil.checkMobile(mLoginMobileTV.getText().toString())) {
                Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mLoginCodeTV.getText())) {
                Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            } else {
                LoginReq req = new LoginReq();
                req.mobile = mLoginMobileTV.getText().toString();
                req.verify_code = mLoginCodeTV.getText().toString();
                AuthApi.login(LoginActivity.this, req, this::loginSuccess);
            }
        });

        mLoginAgreement.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
            intent.putExtra("type", "Agreement");
            startActivity(intent);
        });

        if (Settings.isShameData) {
//            mLoginMobileTV.setText("17621066549");
//            mLoginCodeTV.setText("6666");
        }

    }

    private void loginSuccess(LoginResp resp) {
        if (resp != null) {
            YusionApp.TOKEN = resp.token;
            YusionApp.MOBILE = mLoginMobileTV.getText().toString();
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", YusionApp.TOKEN);
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("mobile", YusionApp.MOBILE);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //每次回到登陆界面都需清除缓存
        myApp.clearUserData();

        ConfigApi.getConfigJson(LoginActivity.this, resp -> {
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //case 1:如果是从SettingActivity注销登录时，stack中有MainActivity和LoginActivity，所以退出应用需要先结束 MainActivity
        ActivityManager.finishOtherActivityEx(LoginActivity.class);
    }

}
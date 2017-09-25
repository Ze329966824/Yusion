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

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.ActivityManager;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.widget.CountDownButtonWrap;

import org.json.JSONException;
import org.json.JSONObject;

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
        //微信登录
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
        //qq登录
        tencent = Tencent.createInstance(QQ_APP_ID, LoginActivity.this);
        mListener = new QQLoginListener();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }
    private class QQLoginListener extends BaseActivity implements IUiListener {
        private UserInfo mInfo;
        @Override
        public void onComplete(Object o) {
            //TODO  同样都有QQ登录和分享的回调，这个可以分开写。
            //QQ登录先初始化openId 和 Token
            initOpenidAndToken(o);
            //再获取用户信息
//        getUserInfo();

        }


        private void getUserInfo() {
            QQToken token = tencent.getQQToken();
            mInfo = new UserInfo(QQLoginListener.this, token);
            mInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object object) {
                    JSONObject jb = (JSONObject) object;
                    try {
//                    name = jb.getString("nickname");
//                    figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
//                    nickName.setText(name);
//                    Picasso.with(QQLoginListener.this).load(figureurl).into(figure);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {
                }
            });
        }

        private void initOpenidAndToken(Object object) {
            JSONObject jb = (JSONObject) object;
            try {
                String openID = jb.getString("openid"); //openid用户唯一标识
                String access_token = jb.getString("access_token");
                String expires = jb.getString("expires_in");
                Log.e("qqlogin    ","openid："+openID);
                Log.e("qqlogin    ","access_token："+access_token);
                Log.e("qqlogin    ","expires："+expires);
                tencent = Tencent.createInstance(QQ_APP_ID, LoginActivity.this);

                tencent.setOpenId(openID);
                tencent.setAccessToken(access_token, expires);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }

    }

}
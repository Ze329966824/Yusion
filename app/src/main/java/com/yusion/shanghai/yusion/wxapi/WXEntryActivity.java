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
import com.yusion.shanghai.yusion.bean.auth.OpenIdReq;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.ui.entrance.BindingActivity;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LX on 2017/9/20.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    public static final String APP_ID = "wxf2c47c30395cfb84";
    public static final String APP_SECRET = "1ec792a6c092d7803672c5fe8e99cfd4";
    private final OkHttpClient client = new OkHttpClient();
    private OpenIdReq req;
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

                try {
                    getAccess_token(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                startActivity(new Intent(WXEntryActivity.this, LoginActivity.class));
                finish();
        }
    }

    private void getAccess_token(String code) throws Exception {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + APP_ID +
                "&secret=" + APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        run(url);

    }

    private void run(String url) throws Exception {
        final Request request = new Request.Builder()
                //.url("http://publicobject.com/helloworld.txt")
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                try {
                    String responseStr = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseStr);
                    Log.e("TAG", "access_token = " + jsonObject.getString("access_token"));//在回调中获取access_token
                    String access_token = jsonObject.optString("access_token");
                    String openid = jsonObject.optString("openid");
                    Log.e("openid", "=====" + openid);
//                    YusionApp.OPEN_ID = openid;
//                    SharedPrefsUtil.getInstance(WXEntryActivity.this).putValue("open_id", YusionApp.OPEN_ID);
                    req.open_id = openid;
                    req.source = "wechat";
                    runOnUiThread(() -> {
                        AuthApi.thirdLogin(WXEntryActivity.this, req, data -> {
                            if (data != null) {

                                Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
                                intent.putExtra("token", data.token);
                                startActivity(intent);
                                finish();


                            } else {
                                Intent intent = new Intent(WXEntryActivity.this, BindingActivity.class);
                                intent.putExtra("source", "wechat");
                                intent.putExtra("open_id", req.open_id);
                                startActivity(intent);
                                finish();
                            }
                        });

                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
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

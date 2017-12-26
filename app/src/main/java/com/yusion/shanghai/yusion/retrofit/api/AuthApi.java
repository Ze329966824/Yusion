package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yusion.shanghai.yusion.bean.auth.AccessTokenResp;
import com.yusion.shanghai.yusion.bean.auth.BindingReq;
import com.yusion.shanghai.yusion.bean.auth.BindingResp;
import com.yusion.shanghai.yusion.bean.auth.CheckHasAgreedReq;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.bean.auth.OpenIdReq;
import com.yusion.shanghai.yusion.bean.auth.OpenIdResp;
import com.yusion.shanghai.yusion.bean.auth.UpdateResp;
import com.yusion.shanghai.yusion.bean.auth.WXUserInfoResp;
import com.yusion.shanghai.yusion.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion.utils.LoadingUtils;

import java.net.UnknownHostException;
import java.util.Locale;

import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.data;

/**
 * Created by ice on 2017/8/3.
 */

public class AuthApi {
    public static void getVCode(Context context, String mobile, final OnItemDataCallBack<GetVCodeResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().getVCode(mobile).enqueue(new CustomCallBack<GetVCodeResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetVCodeResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void login(Context context, LoginReq req, final OnItemDataCallBack<LoginResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().login(req).enqueue(new CustomCallBack<LoginResp>(context, dialog) {
            @Override
            public void onCustomResponse(LoginResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void thirdLogin(Context context, OpenIdReq req, final OnItemDataCallBack<OpenIdResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().openId(req).enqueue(new CustomCallBack<OpenIdResp>(context, dialog) {
            @Override
            public void onCustomResponse(OpenIdResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void binding(Context context, BindingReq req, final OnItemDataCallBack<BindingResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().binding(req).enqueue(new CustomCallBack<BindingResp>(context, dialog) {
            @Override
            public void onCustomResponse(BindingResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void checkOpenID(Context context, String mobile, String source, final OnItemDataCallBack<Integer>onItemDataCallBack){
        Api.getAuthService().checkOpenID(mobile,source).enqueue(new CustomCallBack<Integer>(context) {
            @Override
            public void onCustomResponse(Integer data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }


    public static void checkUserInfo(Context context, final OnItemDataCallBack<CheckUserInfoResp> onItemDataCallBack) {
        Api.getAuthService().checkUserInfo().enqueue(new CustomCallBack<CheckUserInfoResp>(context) {
            @Override
            public void onCustomResponse(CheckUserInfoResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void isAgree(final Context context, CheckHasAgreedReq req, final OnCodeAndMsgCallBack codeAndMsgCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().isAgree(req).enqueue(new CustomCodeAndMsgCallBack(context) {
            @Override
            public void onCustomResponse(int code, String msg) {
                codeAndMsgCallBack.callBack(code, msg);
            }
        });
    }


    public static void checkToken(final Context context, final OnItemDataCallBack<CheckTokenResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().checkToken().enqueue(new CustomCallBack<CheckTokenResp>(context) {
            @Override
            public void onCustomResponse(CheckTokenResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void update(Context context, String frontend, final OnItemDataCallBack<UpdateResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().update(frontend).enqueue(new CustomCallBack<UpdateResp>(context, dialog) {
            @Override
            public void onCustomResponse(UpdateResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void getWXUserInfo(Context context, String access_token,String openid, final OnItemDataCallBack<WXUserInfoResp> onItemDataCallBack) {
        Api.getWXService().getWXUserInfo(access_token,openid).enqueue(new Callback<WXUserInfoResp>() {
            @Override
            public void onResponse(Call<WXUserInfoResp> call, Response<WXUserInfoResp> data) {
                onItemDataCallBack.onItemDataCallBack(data.body());
            }
            @Override
            public void onFailure(Call<WXUserInfoResp> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    Toast.makeText(context, "网络繁忙,请检查网络", Toast.LENGTH_SHORT).show();
                } else if (Settings.isOnline) {
                    Toast.makeText(context, "接口调用失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                }
                Sentry.capture(t);
            }
        });
    }

    public static void getAccessToken(Context context, String appid, String secret, String code, String grant_type, final OnItemDataCallBack<AccessTokenResp> onItemDataCallBack){
        Api.getWXService().getAccessToken(appid,secret,code,grant_type).enqueue(new Callback<AccessTokenResp>() {
            @Override
            public void onResponse(Call<AccessTokenResp> call, Response<AccessTokenResp> data) {
                onItemDataCallBack.onItemDataCallBack(data.body());
            }
            @Override
            public void onFailure(Call<AccessTokenResp> call, Throwable t) {
                if (t instanceof UnknownHostException) {
                    Toast.makeText(context, "网络繁忙,请检查网络", Toast.LENGTH_SHORT).show();
                } else if (Settings.isOnline) {
                    Toast.makeText(context, "接口调用失败,请稍后再试...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
                }
                Sentry.capture(t);
            }
        });
    }
}

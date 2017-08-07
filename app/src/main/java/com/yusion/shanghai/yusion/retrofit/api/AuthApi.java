package com.yusion.shanghai.yusion.retrofit.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;

/**
 * Created by ice on 2017/8/3.
 */

public class AuthApi {
    public static void getVCode(Context context, String mobile, final OnItemDataCallBack<GetVCodeResp> onItemDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getAuthService().getVCode(mobile).enqueue(new CustomCallBack<GetVCodeResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetVCodeResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void login(Context context, LoginReq req, final OnItemDataCallBack<LoginResp> onItemDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getAuthService().login(req).enqueue(new CustomCallBack<LoginResp>(context, dialog) {
            @Override
            public void onCustomResponse(LoginResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void checkUserInfo(Context context, final OnItemDataCallBack<CheckUserInfoResp> onItemDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getAuthService().checkUserInfo().enqueue(new CustomCallBack<CheckUserInfoResp>(context, dialog) {
            @Override
            public void onCustomResponse(CheckUserInfoResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }


    public static void checkToken(final Context context, final OnItemDataCallBack<CheckTokenResp> onItemDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getAuthService().checkToken().enqueue(new CustomCallBack<CheckTokenResp>(context, dialog) {
            @Override
            public void onCustomResponse(CheckTokenResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }
}

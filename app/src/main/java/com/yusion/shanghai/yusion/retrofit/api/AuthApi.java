package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.auth.CheckIsAgreeReq;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.bean.auth.UpdateResp;
import com.yusion.shanghai.yusion.bean.token.CheckTokenResp;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        req.reg_id = SharedPrefsUtil.getInstance(context).getValue("reg_id", "");
//        Log.e("reg_id",req.reg_id);
        Api.getAuthService().login(req).enqueue(new CustomCallBack<LoginResp>(context, dialog) {
            @Override
            public void onCustomResponse(LoginResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void checkUserInfo(Context context, final OnItemDataCallBack<CheckUserInfoResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().checkUserInfo().enqueue(new CustomCallBack<CheckUserInfoResp>(context, dialog) {
            @Override
            public void onCustomResponse(CheckUserInfoResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void isAgree(final Context context, CheckIsAgreeReq req, final OnCodeAndMsgCallBack codeAndMsgCallBack) {
        Api.getAuthService().isAgree(req).enqueue(new CustomCodeAndMsgCallBack(context) {
            @Override
            public void onCustomResponse(int code, String msg) {
                codeAndMsgCallBack.callBack(code, msg);
            }
        });

    }


    public static void checkToken(final Context context, final OnItemDataCallBack<CheckTokenResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getAuthService().checkToken().enqueue(new CustomCallBack<CheckTokenResp>(context, dialog) {
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
}

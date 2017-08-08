package com.yusion.shanghai.yusion.retrofit.service;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.auth.GetUserInfoReq;
import com.yusion.shanghai.yusion.bean.user.UserInfoBean;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class ProductApi {

    public static void getUserInfo(final Context context, GetUserInfoReq req, final OnItemDataCallBack<UserInfoBean> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().getUserInfo(req.id_no, req.clt_nm).enqueue(
                new CustomCallBack<UserInfoBean>(context, dialog) {
                    @Override
                    public void onCustomResponse(UserInfoBean data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }

    public static void updateUserInfo(final Context context, UserInfoBean req, final OnItemDataCallBack<UserInfoBean> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().updateUserInfo(req).enqueue(
                new CustomCallBack<UserInfoBean>(context, dialog) {
                    @Override
                    public void onCustomResponse(UserInfoBean data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }
}

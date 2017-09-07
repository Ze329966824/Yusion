package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.user.GetClientInfoReq;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;
import com.yusion.shanghai.yusion.bean.user.GetGuarantorInfoReq;
import com.yusion.shanghai.yusion.bean.user.GuarantorInfo;
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
    //不能用
    public static void getUserInfo(final Context context, GetClientInfoReq req, final OnItemDataCallBack<UserInfoBean> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().getUserInfo(req.id_no, req.clt_nm).enqueue(
                new CustomCallBack<UserInfoBean>(context, dialog) {
                    @Override
                    public void onCustomResponse(UserInfoBean data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }

    public static void getClientInfo(final Context context, GetClientInfoReq req, final OnItemDataCallBack<ClientInfo> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().getClientInfo(req.id_no, req.clt_nm, req.update).enqueue(
                new CustomCallBack<ClientInfo>(context, dialog) {
                    @Override
                    public void onCustomResponse(ClientInfo data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }

    public static void updateGuarantorInfo(final Context context, GuarantorInfo req, final OnItemDataCallBack<GuarantorInfo> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().updateGuarantorInfo(req).enqueue(
                new CustomCallBack<GuarantorInfo>(context, dialog) {
                    @Override
                    public void onCustomResponse(GuarantorInfo data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }

    public static void getGuarantorInfo(final Context context, GetGuarantorInfoReq req, final OnItemDataCallBack<GuarantorInfo> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().getGuarantorInfo(req.id_no, req.clt_nm, req.mobile, req.social_ship, req.update).enqueue(
                new CustomCallBack<GuarantorInfo>(context, dialog) {
                    @Override
                    public void onCustomResponse(GuarantorInfo data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }

    public static void updateClientInfo(final Context context, ClientInfo req, final OnItemDataCallBack<ClientInfo> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getProductService().updateClientInfo(req).enqueue(
                new CustomCallBack<ClientInfo>(context, dialog) {
                    @Override
                    public void onCustomResponse(ClientInfo data) {
                        onItemDataCallBack.onItemDataCallBack(data);
                    }
                });
    }

    //不能用
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

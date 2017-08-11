package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.order.ConfirmFinancePlanReq;
import com.yusion.shanghai.yusion.bean.order.GetAppDetailResp;
import com.yusion.shanghai.yusion.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion.bean.order.GetFinancePlanDetailResp;
import com.yusion.shanghai.yusion.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;

import java.util.List;

/**
 * Created by aa on 2017/8/4.
 */

public class OrderApi {
    public static void getAppList(final Context context, String st, final OnItemDataCallBack<List<GetAppListResp>> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getAppList(st).enqueue(new CustomCallBack<List<GetAppListResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetAppListResp> data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void confirmFinancePlan(final Context context, ConfirmFinancePlanReq req, final OnCodeAndMsgCallBack codeAndMsgCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().confirmFinancePlan(req).enqueue(new CustomCodeAndMsgCallBack(context, dialog) {
            @Override
            public void onCustomResponse(int code, String msg) {
                codeAndMsgCallBack.callBack(code, msg);
            }
        });
    }

    public static void getFinancePlanDetail(final Context context, String plan_id, final OnItemDataCallBack<GetFinancePlanDetailResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getFinancePlanDetail(plan_id).enqueue(new CustomCallBack<GetFinancePlanDetailResp>(context, dialog) {
            @Override
            public void onCustomResponse(GetFinancePlanDetailResp resp) {
                onItemDataCallBack.onItemDataCallBack(resp);
            }
        });
    }

    public static void getAppDetails(final Context context, String app_id, final OnItemDataCallBack<OrderDetailBean> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getOrderService().getAppDetails(app_id).enqueue(new CustomCallBack<OrderDetailBean>(context, dialog) {
            @Override
            public void onCustomResponse(OrderDetailBean resp) {
                onItemDataCallBack.onItemDataCallBack(resp);
            }
        });
    }

}

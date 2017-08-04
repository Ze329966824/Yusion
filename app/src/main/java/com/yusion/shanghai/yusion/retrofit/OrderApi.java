package com.yusion.shanghai.yusion.retrofit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnDataCallBack;

import java.util.List;

/**
 * Created by aa on 2017/8/4.
 */

public class OrderApi {
    public static void getAppList(final Context context, String st, final OnDataCallBack<List<GetAppListResp>> onDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getOrderService().getAppList(st).enqueue(new CustomCallBack<List<GetAppListResp>>(context, dialog) {
            @Override
            public void onCustomResponse(List<GetAppListResp> data) {
                onDataCallBack.callBack(data);
            }
        });
    }
}

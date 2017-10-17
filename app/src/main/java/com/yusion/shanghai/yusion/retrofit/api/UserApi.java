package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.user.ListCurrentTpye;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;

/**
 * Created by LX on 2017/8/23.
 */

public class UserApi {
    public static void getListCurrentTpye(Context context, final OnItemDataCallBack<ListCurrentTpye> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getUserApi().getListCurrentType().enqueue(new CustomCallBack<ListCurrentTpye>(context, dialog) {
            @Override
            public void onCustomResponse(ListCurrentTpye data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }
}

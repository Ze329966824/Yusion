package com.yusion.shanghai.yusion.retrofit.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.ListImgsResp;
import com.yusion.shanghai.yusion.bean.upload.ListLabelsErrorReq;
import com.yusion.shanghai.yusion.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/20 下午2:30
 */

public class UploadApi {

    public static void uploadFileUrl(final Context context, UploadFilesUrlReq req, final OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
//        ProgressDialog dialog = new ProgressDialog(context);
        Api.getUploadService().uploadFileUrl(req).enqueue(new CustomCodeAndMsgCallBack(context) {
            @Override
            public void onCustomResponse(int code, String msg) {
                onCodeAndMsgCallBack.callBack(code, msg);
            }
        });
    }

    public static void listImgs(final Context context, ListImgsReq req, OnItemDataCallBack<ListImgsResp> onItemDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getUploadService().listImgs(req).enqueue(new CustomCallBack<ListImgsResp>(context, dialog) {
            @Override
            public void onCustomResponse(ListImgsResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }

    public static void listLabelsError(final Context context, ListLabelsErrorReq req, OnItemDataCallBack<ListLabelsErrorResp> onItemDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getUploadService().listLabelsError(req).enqueue(new CustomCallBack<ListLabelsErrorResp>(context, dialog) {
            @Override
            public void onCustomResponse(ListLabelsErrorResp data) {
                onItemDataCallBack.onItemDataCallBack(data);
            }
        });
    }
}

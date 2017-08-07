package com.yusion.shanghai.yusion.retrofit.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/20 下午2:30
 */

public class UploadApi {

    public static void uploadFileUrl(final Context context, UploadFilesUrlReq req, final OnCodeAndMsgCallBack onCodeAndMsgCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        Api.getUploadService().uploadFileUrl(req).enqueue(new CustomCodeAndMsgCallBack(context, dialog) {
            @Override
            public void onCustomResponse(int code, String msg) {
                onCodeAndMsgCallBack.callBack(code, msg);
            }
        });
    }

//    public static void listImgs(final Context context, String loadMsg, ListImgsReq req, OnDataCallBack<ListImgsResp> onDataCallBack) {
//        Dialog loadDialog = LoadingUtils.showCustomLoadDialog(context, loadMsg);
//        Api.getUploadService().listImgs(req).enqueue(new CustomCallBack<ListImgsResp>(context, loadDialog) {
//            @Override
//            public void onCustomResponse(ListImgsResp data) {
//                onDataCallBack.callBack(data);
//            }
//        });
//    }
//
//    public static void listLabelsError(final Context context, String loadMsg, ListLabelsErrorReq req, OnDataCallBack<ListLabelsErrorResp> onDataCallBack) {
//        Dialog loadDialog = LoadingUtils.showCustomLoadDialog(context, loadMsg);
//        Api.getUploadService().listLabelsError(req).enqueue(new CustomCallBack<ListLabelsErrorResp>(context, loadDialog) {
//            @Override
//            public void onCustomResponse(ListLabelsErrorResp data) {
//                onDataCallBack.callBack(data);
//            }
//        });
//    }
}

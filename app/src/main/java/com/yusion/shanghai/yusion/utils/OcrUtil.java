package com.yusion.shanghai.yusion.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.yusion.shanghai.yusion.bean.ocr.OcrReq;
import com.yusion.shanghai.yusion.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.retrofit.api.OcrApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnMultiDataCallBack;
import io.sentry.Sentry;

/**
 * Created by ice on 2017/8/4.
 */
public class OcrUtil {
    public static void requestOcr(final Context context, final String localPath, @NonNull OSSObjectKeyBean objectKeyBean, String type,
                                  @NonNull final OnOcrSuccessCallBack onSuccessCallBack, @NonNull final OnMultiDataCallBack<Throwable, String> onFailureCallBack) {

        OssUtil.uploadOss(context, false, localPath, objectKeyBean, objectKey -> {
            OcrReq ocrReq = new OcrReq();
            ocrReq.fid = objectKey;
            ocrReq.region = SharedPrefsUtil.getInstance(context).getValue("region", "");
            ocrReq.bucket = SharedPrefsUtil.getInstance(context).getValue("bucket", "");
            ocrReq.type = type;
            OcrApi.requestOcr(ocrReq, new OnItemDataCallBack<OcrResp>() {
                @Override
                public void onItemDataCallBack(OcrResp data) {
                    onSuccessCallBack.OnOcrSuccess(data, objectKey);
                }
            }, new OnItemDataCallBack<Throwable>() {
                @Override
                public void onItemDataCallBack(Throwable throwable) {
                    String errorInfo = "onFailure() called with:  throwable = [" + throwable + "]";
                    Sentry.capture(errorInfo);
                    Log.e("TAG", errorInfo);
                    onFailureCallBack.onMultiDataCallBack(throwable, objectKey);
                }
            });
        }, new OnItemDataCallBack<Throwable>() {
            @Override
            public void onItemDataCallBack(Throwable throwable) {
                onFailureCallBack.onMultiDataCallBack(throwable, null);
            }
        });
    }

    public interface OnOcrSuccessCallBack {
        void OnOcrSuccess(OcrResp ocrResp, String objectKey);
    }
}

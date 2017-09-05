package com.yusion.shanghai.yusion.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.yusion.shanghai.yusion.bean.ocr.OcrReq;
import com.yusion.shanghai.yusion.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion.bean.oss.GetOssTokenBean;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.retrofit.api.OcrApi;
import com.yusion.shanghai.yusion.retrofit.api.OssApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnMultiDataCallBack;
import com.yusion.shanghai.yusion.retrofit.service.OssService;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yusion.shanghai.yusion.utils.OssUtil.getObjectKey;
import static com.yusion.shanghai.yusion.utils.OssUtil.getSignature;

/**
 * Created by ice on 2017/8/4.
 */

public class OcrUtil {
    public static void requestOcr(final Context context, final String localPath, @NonNull OSSObjectKeyBean objectKeyBean, String type,
                                  @NonNull final OnOcrSuccessCallBack onSuccessCallBack, @NonNull final OnMultiDataCallBack<Throwable, String> onFailureCallBack) {

        Map<String, String> body = new LinkedHashMap<>();
        body.put("duration_second", "1800");
        body.put("method", "put");
        body.put("timestamp", new Date().getTime() + "");
        body.put("signature", getSignature(body));
        OssApi.retrofit.create(OssService.class).getOSSToken(body).enqueue(new Callback<GetOssTokenBean>() {
            @Override
            public void onResponse(Call<GetOssTokenBean> call, Response<GetOssTokenBean> response) {
                GetOssTokenBean ossTokenBean = response.body();
                final String objectKey = getObjectKey(objectKeyBean.role, objectKeyBean.category, objectKeyBean.suffix);
                PutObjectRequest request = new PutObjectRequest(ossTokenBean.FidDetail.Bucket, objectKey, localPath);

                OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossTokenBean.AccessKeyId, ossTokenBean.AccessKeySecret, ossTokenBean.SecurityToken);
                OSS oss = new OSSClient(context, ossTokenBean.FidDetail.Region, credentialProvider);
                SharedPrefsUtil.getInstance(context).putValue("region",ossTokenBean.FidDetail.Region);
                SharedPrefsUtil.getInstance(context).putValue("bucket",ossTokenBean.FidDetail.Bucket);

                oss.asyncPutObject(request, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        //请求ocr
                        OcrReq ocrReq = new OcrReq();
                        ocrReq.fid = objectKey;
                        ocrReq.bucket = ossTokenBean.FidDetail.Bucket;
                        ocrReq.region = ossTokenBean.FidDetail.Region;
                        ocrReq.type = type;
                        OcrApi.requestOcr(ocrReq, new OnItemDataCallBack<OcrResp>() {
                            @Override
                            public void onItemDataCallBack(OcrResp data) {
                                onSuccessCallBack.OnOcrSuccess(data, objectKey);
                            }
                        }, new OnItemDataCallBack<Throwable>() {
                            @Override
                            public void onItemDataCallBack(Throwable resp) {
                                onFailureCallBack.onMultiDataCallBack(resp, objectKey);
                            }
                        });
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        Log.e("API", "onFailure() called with: request = [" + request + "], clientExcepion = [" + clientExcepion + "], serviceException = [" + serviceException + "]");
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                            if (onFailureCallBack != null) {
                                onFailureCallBack.onMultiDataCallBack(clientExcepion, objectKey);
                            }
                        }
                        if (serviceException != null) {
                            // 服务异常
                            serviceException.printStackTrace();
                            if (onFailureCallBack != null) {
                                onFailureCallBack.onMultiDataCallBack(serviceException, objectKey);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<GetOssTokenBean> call, Throwable t) {
                Log.e("API", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                if (onFailureCallBack != null) {
                    onFailureCallBack.onMultiDataCallBack(t, null);
                }

            }
        });
    }

    public interface OnOcrSuccessCallBack {
        void OnOcrSuccess(OcrResp ocrResp, String objectKey);
    }
}

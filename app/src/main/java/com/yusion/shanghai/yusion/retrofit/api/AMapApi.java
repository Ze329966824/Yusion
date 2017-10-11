package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.yusion.shanghai.yusion.bean.amap.PoiResp;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.service.AMapService;
import com.yusion.shanghai.yusion.utils.LoadingUtils;

import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class AMapApi {

    private static Retrofit retrofit = Api.createRetrofit("http://restapi.amap.com/");
    private static AMapService mapService = retrofit.create(AMapService.class);

    public static void getPoiResp(final Context context, String key, String keywords, String city, final OnItemDataCallBack<PoiResp> onItemDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        dialog.show();
        mapService.getPoiResp(key, keywords, city).enqueue(new Callback<PoiResp>() {
            @Override
            public void onResponse(Call<PoiResp> call, Response<PoiResp> response) {
                dialog.dismiss();
                PoiResp body = response.body();
                if (body != null) {
                    if ("0".equals(body.status)) {
                        String errorInfo = "请求失败 错误原因:" + body.info;
                        Toast.makeText(context, errorInfo, Toast.LENGTH_SHORT).show();
                        Sentry.capture("AMAP:" + errorInfo);
                    } else {
                        onItemDataCallBack.onItemDataCallBack(body);
                    }
                } else {
                    Toast.makeText(context, "返回数据为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PoiResp> call, Throwable t) {
                dialog.dismiss();
                Sentry.capture("AMAP:" + t);
                Toast.makeText(context, "网络繁忙", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
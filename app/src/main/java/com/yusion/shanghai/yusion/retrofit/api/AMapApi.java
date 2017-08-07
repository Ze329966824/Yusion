package com.yusion.shanghai.yusion.retrofit.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.yusion.shanghai.yusion.bean.amap.PoiResp;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.service.AMapService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class AMapApi {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://restapi.amap.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static AMapService mapService = retrofit.create(AMapService.class);

    public static void getPoiResp(final Context context, String key, String keywords, String city, final OnItemDataCallBack<PoiResp> onItemDataCallBack) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        mapService.getPoiResp(key, keywords, city).enqueue(new Callback<PoiResp>() {
            @Override
            public void onResponse(Call<PoiResp> call, Response<PoiResp> response) {
                dialog.dismiss();
                PoiResp body = response.body();
                if (body != null) {
                    if ("0".equals(body.status)) {
                        Toast.makeText(context, "请求失败 错误原因:" + body.info, Toast.LENGTH_SHORT).show();
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
                PgyCrashManager.reportCaughtException(context, (Exception) t);
                Toast.makeText(context, "网络繁忙", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

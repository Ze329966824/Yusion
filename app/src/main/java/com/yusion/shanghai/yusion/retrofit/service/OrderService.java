package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.bean.order.GetAppListResp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by aa on 2017/8/4.
 */

public interface OrderService {
    //获取订单列表
    @GET("/api/application/get_app_list/")
    Call<BaseResult<List<GetAppListResp>>> getAppList(@Query("st") String st);
}

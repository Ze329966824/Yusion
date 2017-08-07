package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.bean.order.ConfirmFinancePlanReq;
import com.yusion.shanghai.yusion.bean.order.GetAppDetailResp;
import com.yusion.shanghai.yusion.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion.bean.order.GetFinancePlanDetailResp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by aa on 2017/8/4.
 */

public interface OrderService {
    //获取订单列表
    @GET("/api/application/get_app_list/")
    Call<BaseResult<List<GetAppListResp>>> getAppList(@Query("st") String st);

    //获取订单详情
    @GET("/api/application/get_app_details/")
    Call<BaseResult<GetAppDetailResp>> getAppDetails(@Query("app_id") String app_id);

    //确认金融方案
    @POST("/api/application/get_confirm_financial_plan/")
    Call<BaseResult> confirmFinancePlan(@Body ConfirmFinancePlanReq req);

    //获取金融方案详情
    @GET("/api/application/get_confirm_financial_plan/")
    Call<BaseResult<GetFinancePlanDetailResp>> getFinancePlanDetail(@Query("app_id") String app_id);
}

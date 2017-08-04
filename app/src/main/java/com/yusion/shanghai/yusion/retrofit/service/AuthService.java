package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.base.BaseResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ice on 2017/8/3.
 */

public interface AuthService {
    @GET("/api/auth/user_login/")
    Call<BaseResult<GetVCodeResp>> getVCode(@Query("mobile") String mobile);

    @POST("/api/auth/user_login/")
    Call<BaseResult<LoginResp>> login(@Body LoginReq req);

    @GET("/api/client/check_user_info/")
    Call<BaseResult<CheckUserInfoResp>> checkUserInfo();

}

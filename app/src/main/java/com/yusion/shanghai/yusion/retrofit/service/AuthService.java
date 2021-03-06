package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.bean.auth.BindingReq;
import com.yusion.shanghai.yusion.bean.auth.BindingResp;
import com.yusion.shanghai.yusion.bean.auth.CheckHasAgreedReq;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.auth.GetVCodeResp;
import com.yusion.shanghai.yusion.bean.auth.LoginReq;
import com.yusion.shanghai.yusion.bean.auth.LoginResp;
import com.yusion.shanghai.yusion.bean.auth.OpenIdReq;
import com.yusion.shanghai.yusion.bean.auth.OpenIdResp;
import com.yusion.shanghai.yusion.bean.auth.UpdateResp;
import com.yusion.shanghai.yusion.bean.token.CheckTokenResp;

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

    @POST("/api/client/check_user_info/")
    Call<BaseResult> isAgree(@Body CheckHasAgreedReq req);

    @POST("/api/auth/check_token/")
    Call<BaseResult<CheckTokenResp>> checkToken();

    @GET("/api/check_new_app/")
    Call<BaseResult<UpdateResp>> update(@Query("frontend") String frontend);

    @POST("/api/auth/auth_open_id/")
    Call<BaseResult<OpenIdResp>> openId(@Body OpenIdReq req);

    @GET("/api/auth/auth_open_id/")
    Call<BaseResult<Integer>>   checkOpenID(@Query("mobile") String mobile,@Query("source") String source);

    @POST("/api/auth/auth_user_login/")
    Call<BaseResult<BindingResp>> binding(@Body BindingReq req);
}

package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.bean.auth.UpdateResp;
import com.yusion.shanghai.yusion.bean.auth.WXUserInfoResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by LX on 2017/12/25.
 */

public interface WXService {
    @GET("/sns/userinfo")
    Call<WXUserInfoResp> getWXUserInfo(@Query("access_token") String access_token, @Query("openid") String openid);
}

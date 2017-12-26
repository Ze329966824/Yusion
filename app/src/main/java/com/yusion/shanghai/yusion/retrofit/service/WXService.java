package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.bean.auth.AccessTokenResp;
import com.yusion.shanghai.yusion.bean.auth.UpdateResp;
import com.yusion.shanghai.yusion.bean.auth.WXUserInfoResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by LX on 2017/12/25.
 */

public interface WXService {

    @GET("/sns/oauth2/access_token")
    Call<AccessTokenResp> getAccessToken(@Query("appid") String appid,@Query("secret") String secret,@Query("code") String code,@Query("grant_type") String grant_type);

    @GET("/sns/userinfo")
    Call<WXUserInfoResp> getWXUserInfo(@Query("access_token") String access_token, @Query("openid") String openid);
}

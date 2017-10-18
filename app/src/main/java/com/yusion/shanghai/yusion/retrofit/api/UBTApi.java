package com.yusion.shanghai.yusion.retrofit.api;

import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.service.UBTService;

import retrofit2.Retrofit;

public class UBTApi {
    public static Retrofit retrofit = Api.createRetrofit("http://ubt.yusiontech.com:5141/");
    public static UBTService getUBTService() {
        return retrofit.create(UBTService.class);
    }
}

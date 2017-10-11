package com.yusion.shanghai.yusion.retrofit.api;

import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.service.OssService;
import com.yusion.shanghai.yusion.settings.Settings;

import retrofit2.Retrofit;

public class OssApi {
    public static Retrofit retrofit = Api.createRetrofit(Settings.OSS_SERVER_URL);
    public static OssService ossService = retrofit.create(OssService.class);
}

package com.yusion.shanghai.yusion.retrofit.api;

import com.yusion.shanghai.yusion.settings.Settings;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description : 图片上传
 * Author : suijin
 * Date   : 17/04/10
 */
public class OssApi {

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Settings.OSS_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}

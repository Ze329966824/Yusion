package com.yusion.shanghai.yusion.retrofit.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public interface ConfigService {
    @GET("/api/material/global_config/")
    Call<ResponseBody> getConfigJson();
}

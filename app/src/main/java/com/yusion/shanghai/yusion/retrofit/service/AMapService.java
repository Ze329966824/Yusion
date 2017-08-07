package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.bean.amap.PoiResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public interface AMapService {
    @GET("v3/place/text")
    Call<PoiResp> getPoiResp(@Query("key") String key, @Query("keywords") String keywords, @Query("city") String city);
}

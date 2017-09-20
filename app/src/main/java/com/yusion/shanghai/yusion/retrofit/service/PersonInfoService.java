package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.bean.upload.ContactPersonInfoReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by aa on 2017/9/18.
 */

public interface PersonInfoService {
    @POST("contact/")
    Call<Void> uploadPersonAndDeviceInfo(@Body ContactPersonInfoReq req);
}

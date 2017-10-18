package com.yusion.shanghai.yusion.retrofit.service;

import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.bean.user.ListCurrentTpye;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by LX on 2017/8/23.
 */

public interface UserService {
    //用户资料列表类型接口
    @GET("api/client/client_guarantor_info_list")
    Call<BaseResult<ListCurrentTpye>> getListCurrentType();
}

package com.yusion.shanghai.yusion.retrofit.service;


import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;
import com.yusion.shanghai.yusion.bean.user.UserInfoBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductService {

    @POST("api/client/client_info/")
    Call<BaseResult<UserInfoBean>> updateUserInfo(@Body UserInfoBean req);

    //删
    @GET("api/client/client_info/")
    Call<BaseResult<UserInfoBean>> getUserInfo(@Query("id_no") String id_no, @Query("clt_nm") String clt_nm);


    @GET("api/client/client_info/")
    Call<BaseResult<ClientInfo>> getClientInfo(@Query("id_no") String id_no, @Query("clt_nm") String clt_nm);
}

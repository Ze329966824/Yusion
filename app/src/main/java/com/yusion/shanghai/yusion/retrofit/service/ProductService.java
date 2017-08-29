package com.yusion.shanghai.yusion.retrofit.service;


import com.yusion.shanghai.yusion.base.BaseResult;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;
import com.yusion.shanghai.yusion.bean.user.GuarantorInfo;
import com.yusion.shanghai.yusion.bean.user.UserInfoBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductService {

    @POST("api/client/client_info/")
    Call<BaseResult<UserInfoBean>> updateUserInfo(@Body UserInfoBean req);

    //更新用户资料
    @POST("api/client/client_info/")
    Call<BaseResult<ClientInfo>> updateClientInfo(@Body ClientInfo req);

    @GET("api/client/client_info/")
    Call<BaseResult<UserInfoBean>> getUserInfo(@Query("id_no") String id_no, @Query("clt_nm") String clt_nm);

    //获取用户信息
    @GET("api/client/client_info/")
    Call<BaseResult<ClientInfo>> getClientInfo(@Query("id_no") String id_no, @Query("clt_nm") String clt_nm, @Query("update") String update);

    @POST("api/client/guarantor_info/")
    Call<BaseResult<GuarantorInfo>> updateGuarantorInfo(@Body GuarantorInfo req);

    @GET("api/client/guarantor_info/")
    Call<BaseResult<GuarantorInfo>> getGuarantorInfo(@Query("id_no") String id_no, @Query("clt_nm") String clt_nm, @Query("mobile") String mobile, @Query("social_ship") String social_ship, @Query("update") String update);
}

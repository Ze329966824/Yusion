package com.yusion.shanghai.yusion.retrofit.api;

import android.text.TextUtils;
import android.util.Log;

import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.bean.upload.ContactPersonInfoReq;
import com.yusion.shanghai.yusion.retrofit.service.PersonInfoService;
import com.yusion.shanghai.yusion.settings.Settings;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aa on 2017/9/18.
 */

public class PersonApi {
    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(chain -> {
                Request request = chain.request();
                Request realRequest = request.newBuilder()
                        .method(request.method(), request.body())
                        .addHeader("authentication", String.format(Locale.CHINA, "token %s", TextUtils.isEmpty(YusionApp.TOKEN) ? Settings.TEST_TOKEN : YusionApp.TOKEN))
                        .build();
                Response response = chain.proceed(realRequest);
                logResponseInfo(response);
                return response;
            }).build();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Settings.PERSON_SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static PersonInfoService getPersonInfoService() {
        return retrofit.create(PersonInfoService.class);
    }


    private static void logRequestInfo(Request request) {
        Log.e("API", "\n");
        Log.e("API", "\n******** log request start ******** ");
        Log.e("API", "url: " + request.url());
        Log.e("API", "method: " + request.method());
        Headers headers = request.headers();
        for (int i = 0; i < headers.size(); i++) {
            Log.e("API", headers.name(i) + " : " + headers.value(i));
        }

        //如果是post请求还需打印参数
        String method = request.method();
        if ("POST".equals(method)) {
            RequestBody requestBody = request.body();
            Buffer buffer = new Buffer();
            try {
                requestBody.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String paramsStr = buffer.readString(Charset.forName("UTF-8")).replaceAll("\"", "\\\"");
            Log.e("API", "requestParameters: " + paramsStr);
        }

        Log.e("API", "******** log request end ******** \n");
        Log.e("API", "\n");
    }

    private static void logResponseInfo(Response response) {
        logRequestInfo(response.request());
    }

    public static void uploadPersonAndDeviceInfo(ContactPersonInfoReq req) {

        PersonApi.getPersonInfoService().uploadPersonAndDeviceInfo(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

}

package com.yusion.shanghai.yusion.retrofit.api;

import android.text.TextUtils;
import android.util.Log;

import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.retrofit.service.UBTService;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description : 图片上传
 * Author : suijin
 * Date   : 17/04/10
 */
public class UBTApi {

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
//                        logRequestInfo(realRequest);
                Response response = chain.proceed(realRequest);
                logResponseInfo(response);
                return response;
            }).build();
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.203:5141/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

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

    public static UBTService getUBTService() {
        return retrofit.create(UBTService.class);
    }
}

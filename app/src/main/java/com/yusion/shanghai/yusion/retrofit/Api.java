package com.yusion.shanghai.yusion.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.retrofit.service.AuthService;
import com.yusion.shanghai.yusion.retrofit.service.ConfigService;
import com.yusion.shanghai.yusion.retrofit.service.OcrService;
import com.yusion.shanghai.yusion.retrofit.service.OrderService;
import com.yusion.shanghai.yusion.retrofit.service.ProductService;
import com.yusion.shanghai.yusion.retrofit.service.UploadService;
import com.yusion.shanghai.yusion.retrofit.service.UserService;
import com.yusion.shanghai.yusion.settings.Settings;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ice on 2017/8/3.
 */

public class Api {
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request realRequest = request.newBuilder()
                                .method(request.method(), request.body())
                                .addHeader("authentication", String.format(Locale.CHINA, "token %s", TextUtils.isEmpty(YusionApp.TOKEN) ? Settings.TEST_TOKEN : YusionApp.TOKEN))
                                .build();
//                        logRequestInfo(realRequest);
                        Response response = chain.proceed(realRequest);
                        logResponseInfo(response);
                        return response;
                    }
                })
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Settings.SERVER_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).serializeNulls().create()))
                .build();

    }

    public static AuthService getAuthService() {
        return retrofit.create(AuthService.class);
    }

    public static UserService getUserApi() {
        return retrofit.create(UserService.class);
    }

    public static OcrService getOcrService() {
        return retrofit.create(OcrService.class);
    }

    public static UploadService getUploadService() {
        return retrofit.create(UploadService.class);
    }

    private static void logRequestInfo(Request request) {
        StringBuilder tagBuilder = new StringBuilder("API");
        if (request.url().toString().contains("ubt")) {
            tagBuilder.append("-UBT");
        } else if (request.url().toString().contains("application")) {
            tagBuilder.append("-APPLICATION");
        } else if (request.url().toString().contains("client")) {
            tagBuilder.append("-CLIENT");
        } else if (request.url().toString().contains("auth")) {
            tagBuilder.append("-UBT");
        } else if (request.url().toString().contains("material")) {
            tagBuilder.append("-MATERIAL");
        } else {
            tagBuilder.append("-OTHER");
        }
        tagBuilder.append("-").append(request.method().toUpperCase());
        String tag = tagBuilder.toString();

        Log.e(tag, "\n");
        Log.e(tag, "\n******** log request start ******** ");
        Log.e(tag, "url: " + request.url());
        Log.e(tag, "method: " + request.method());
        Headers headers = request.headers();
        for (int i = 0; i < headers.size(); i++) {
            Log.e(tag, headers.name(i) + " : " + headers.value(i));
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
            Log.e(tag, "requestParameters: " + paramsStr);
        }

        Log.e(tag, "******** log request end ********\n");
        Log.e(tag, "\n");
    }

    private static void logResponseInfo(Response response) {
        logRequestInfo(response.request());
    }

    public static ProductService getProductService() {
        return retrofit.create(ProductService.class);
    }

    public static OrderService getOrderService() {
        return retrofit.create(OrderService.class);
    }

    public static ConfigService getConfigService() {
        return retrofit.create(ConfigService.class);
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
}

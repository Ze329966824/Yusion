package com.yusion.shanghai.yusion.glide;


import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by ice on 2017/8/14.
 */

public class ProgressInterceptor implements Interceptor {

    private ProgressListener progressListener;
    private StatusImageRel imageView;

    public ProgressInterceptor(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public ProgressInterceptor(StatusImageRel imageView) {
        this.imageView = imageView;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), imageView)).build();
    }

}

package com.yusion.shanghai.yusion.glide;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ice on 2017/8/14.
 */

public class ProgressDataFetcher implements DataFetcher<InputStream> {

    private String url;
    private Call progressCall;
    private InputStream stream;
    private boolean isCancelled;
    private ProgressListener listener;
    private StatusImageView imageView;

    public ProgressDataFetcher(String url, ProgressListener listener) {
        this.url = url;
        this.listener = listener;
    }

    public ProgressDataFetcher(String model, StatusImageView imageView) {
        this.url = model;
        this.imageView = imageView;
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new ProgressInterceptor(imageView));
        try {
            progressCall = client.newCall(request);
            Response response = progressCall.execute();
            if (isCancelled) {
                return null;
            }
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            stream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stream;
    }

    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (IOException e) {
                stream = null;
            }
        }
        if (progressCall != null) {
            progressCall.cancel();
        }
    }

    @Override
    public String getId() {
        return url;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
}
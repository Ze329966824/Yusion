package com.yusion.shanghai.yusion.glide;


import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.Locale;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by ice on 2017/8/14.
 */

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private ProgressListener progressListener;
    private BufferedSource bufferedSource;
    private StatusImageView imageView;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    public ProgressResponseBody(ResponseBody body, StatusImageView imageView) {
        this.responseBody = body;
        this.imageView = imageView;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return responseBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            try {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                final long contentLength = responseBody.contentLength();
                StatusImageView statusImageView = (StatusImageView) imageView;
                statusImageView.getProgressTv().post(new Runnable() {
                    @Override
                    public void run() {
                        float v = totalBytesRead * 100f / contentLength;
                        Log.e("TAG", "progress1 called with: bytesRead = [" + totalBytesRead + "], contentLength = [" + contentLength + "], done = [" + (bytesRead == -1) + "]");
                        statusImageView.getProgressTv().setText(String.format(Locale.CHINA, "%.2f%%", v));
                    }
                });
                Log.e("TAG", "progress2 called with: bytesRead = [" + totalBytesRead + "], contentLength = [" + contentLength + "], done = [" + (bytesRead == -1) + "]");
                if (progressListener != null)
                    progressListener.progress(totalBytesRead, contentLength, bytesRead == -1);
                return bytesRead;
            }
        };
    }
}

package com.yusion.shanghai.yusion.glide;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

/**
 * Created by ice on 2017/8/14.
 */

public class ProgressModelLoader implements StreamModelLoader<String> {

    private ProgressListener listener;
    private StatusImageView imageView;

    public ProgressModelLoader(ProgressListener listener) {
        this.listener = listener;
    }

    public ProgressModelLoader(StatusImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
        return new ProgressDataFetcher(model, imageView);
    }
}

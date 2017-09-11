package com.yusion.shanghai.yusion.glide;

/**
 * Created by ice on 2017/8/14.
 */

public interface ProgressListener {

    void progress(long bytesRead, long contentLength, boolean done);

}
package com.yusion.shanghai.yusion.bean.upload;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by ice on 2017/7/31.
 */

public class ListLabelsErrorResp {

    public int err_num;
    public List<String> err_labels;
    public List<String> has_img_labels;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

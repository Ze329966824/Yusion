package com.yusion.shanghai.yusion.bean.upload;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by ice on 2017/7/31.
 */

public class ListLabelsErrorReq {


    /**
     * clt_id : 62c6b748736611e78ead0242ac110004
     * label_list : ["id_card_back"]
     */

    public String clt_id;
    public List<String> label_list;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

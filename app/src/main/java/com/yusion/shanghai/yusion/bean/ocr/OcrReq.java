package com.yusion.shanghai.yusion.bean.ocr;

import com.google.gson.Gson;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class OcrReq {
    /**
     * type : id_card
     * region : oss-cn-hangzhou.aliyuncs.com
     * bucket : yusiontech-test
     * fid : zhm.jpg
     */

    public String type;
    public String region;
    public String bucket;
    public String fid;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

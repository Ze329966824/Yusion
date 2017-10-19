package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.bean.config.ConfigResp;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomResponseBodyCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ConfigApi {
    /**
     * 获取配置文件
     * 如果返回的responseBody为空则从缓存文件中取
     * 否则直接返回并存入缓存文件
     */
    public static void getConfigJson(final Context context, final OnVoidCallBack onVoidCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getConfigService().getConfigJson().enqueue(new CustomResponseBodyCallBack(context) {
            @Override
            public void onCustomResponse(String body) {
                JSONObject data;
                try {
                    if (TextUtils.isEmpty(body)) {
                        //new JsonObject("")会throw new JSONException();
                        data = new JSONObject(SharedPrefsUtil.getInstance(context).getValue("config_json", ""));
                    } else {
                        data = new JSONObject(body).getJSONObject("data");
                        SharedPrefsUtil.getInstance(context).putValue("config_json", data.toString());
                    }
                    YusionApp.CONFIG_RESP = parseJsonObject2ConfigResp(data);
                    if (onVoidCallBack != null)
                        onVoidCallBack.callBack();
                } catch (JSONException e) {
                    //两种可能 一种是用户第一次使用APP时未能成功拉取服务器配置文件 一种是parseJsonObject2ConfigResp解析出错
                    Toast.makeText(context, "静态文件获取失败，请重新打开APP。", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public static ConfigResp parseJsonObject2ConfigResp(JSONObject jsonObject) throws JSONException {
        ConfigResp configResp = new ConfigResp();

        configResp.confident_policy_url = jsonObject.optString("confident_policy_url");
        configResp.agreement_url = jsonObject.optString("agreement_url");

        fullConfigResp(jsonObject, "education_list", configResp.education_list_key, configResp.education_list_value);
        fullConfigResp(jsonObject, "marriage", configResp.marriage_key, configResp.marriage_value);
        fullConfigResp(jsonObject, "work_position", configResp.work_position_key, configResp.work_position_value);
        fullConfigResp(jsonObject, "gender_list", configResp.gender_list_key, configResp.gender_list_value);
        fullConfigResp(jsonObject, "house_type_list", configResp.house_type_list_key, configResp.house_type_list_value);
        fullConfigResp(jsonObject, "house_relationship_list", configResp.house_relationship_list_key, configResp.house_relationship_list_value);
        fullConfigResp(jsonObject, "urg_other_relationship_list", configResp.urg_other_relationship_list_key, configResp.urg_other_relationship_list_value);
        fullConfigResp(jsonObject, "urg_rela_relationship_list", configResp.urg_rela_relationship_list_key, configResp.urg_rela_relationship_list_value);
        fullConfigResp(jsonObject, "drv_lic_relationship_list", configResp.drv_lic_relationship_list_key, configResp.drv_lic_relationship_list_value);
        fullConfigResp(jsonObject, "busi_type_list", configResp.busi_type_list_key, configResp.busi_type_list_value);
        fullConfigResp(jsonObject, "guarantor_relationship_list", configResp.guarantor_relationship_list_key, configResp.guarantor_relationship_list_value);

        JSONArray client_material = jsonObject.optJSONArray("client_material");
        configResp.client_material = client_material != null ? client_material.toString() : "";

        return configResp;
    }

    private static void fullConfigResp(JSONObject jsonObject, String arrayName, List<String> keyList, List<String> valueList) throws JSONException {
        JSONArray education_list = jsonObject.optJSONArray(arrayName);
        for (int i = 0; education_list != null && i < education_list.length(); i++) {
            JSONObject o = education_list.getJSONObject(i);
            String next = o.keys().next();
            keyList.add(next);
            valueList.add(o.getString(next));
        }
    }
}

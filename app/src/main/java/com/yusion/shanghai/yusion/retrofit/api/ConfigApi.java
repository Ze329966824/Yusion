package com.yusion.shanghai.yusion.retrofit.api;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.bean.config.ConfigResp;
import com.yusion.shanghai.yusion.retrofit.Api;
import com.yusion.shanghai.yusion.retrofit.callback.CustomResponseBodyCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigApi {
    /**
     * 获取配置文件
     * 如果返回的responseBody为空则从缓存文件中取
     * 否则直接返回并存入缓存文件
     */
    public static void getConfigJson(final Context context, final OnDataCallBack<ConfigResp> onDataCallBack) {
        Dialog dialog = LoadingUtils.createLoadingDialog(context);
        Api.getConfigService().getConfigJson().enqueue(new CustomResponseBodyCallBack(context, dialog) {
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
                    onDataCallBack.callBack(parseJsonObject2ConfigResp(context, data));
                } catch (JSONException e) {
                    //两种可能 一种是用户第一次使用APP时未能成功拉取服务器配置文件 一种是parseJsonObject2ConfigResp解析出错
                    Toast.makeText(context, "静态文件获取失败，请重新打开APP。", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public static ConfigResp parseJsonObject2ConfigResp(Context context, JSONObject jsonObject) throws JSONException {
        ConfigResp configResp = new ConfigResp();

        String agreement_url = jsonObject.optString("agreement_url");
        configResp.agreement_url = agreement_url;

        JSONArray education_list = jsonObject.optJSONArray("education_list");
        for (int i = 0; education_list != null && i < education_list.length(); i++) {
            JSONObject o = education_list.getJSONObject(i);
            String next = o.keys().next();
            configResp.education_list_key.add(next);
            configResp.education_list_value.add(o.getString(next));
        }

        JSONArray marriage_list = jsonObject.optJSONArray("marriage");
        for (int i = 0; marriage_list != null && i < marriage_list.length(); i++) {
            JSONObject o = marriage_list.getJSONObject(i);
            String next = o.keys().next();
            configResp.marriage_key.add(next);
            configResp.marriage_value.add(o.getString(next));
        }

        JSONArray work_position_list = jsonObject.optJSONArray("work_position");
        for (int i = 0; work_position_list != null && i < work_position_list.length(); i++) {
            JSONObject o = work_position_list.getJSONObject(i);
            String next = o.keys().next();
            configResp.work_position_key.add(next);
            configResp.work_position_value.add(o.getString(next));
        }

        JSONArray gender_list = jsonObject.optJSONArray("gender_list");
        for (int i = 0; gender_list != null && i < gender_list.length(); i++) {
            JSONObject item = gender_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.gender_list_key.add(key);
            configResp.gender_list_value.add(value);
        }

        JSONArray house_type_list = jsonObject.optJSONArray("house_type_list");
        for (int i = 0; house_type_list != null && i < house_type_list.length(); i++) {
            JSONObject item = house_type_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.house_type_list_key.add(key);
            configResp.house_type_list_value.add(value);
        }

        JSONArray house_relationship_list = jsonObject.optJSONArray("house_relationship_list");
        for (int i = 0; house_relationship_list != null && i < house_relationship_list.length(); i++) {
            JSONObject item = house_relationship_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.house_relationship_list_key.add(key);
            configResp.house_relationship_list_value.add(value);
        }

        JSONArray urg_other_relationship_list = jsonObject.optJSONArray("urg_other_relationship_list");
        for (int i = 0; urg_other_relationship_list != null && i < urg_other_relationship_list.length(); i++) {
            JSONObject item = urg_other_relationship_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.urg_other_relationship_list_key.add(key);
            configResp.urg_other_relationship_list_value.add(value);
        }

        JSONArray urg_rela_relationship_list = jsonObject.optJSONArray("urg_rela_relationship_list");
        for (int i = 0; urg_rela_relationship_list != null && i < urg_rela_relationship_list.length(); i++) {
            JSONObject item = urg_rela_relationship_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.urg_rela_relationship_list_key.add(key);
            configResp.urg_rela_relationship_list_value.add(value);
        }

        JSONArray drv_lic_relationship_list = jsonObject.optJSONArray("drv_lic_relationship_list");
        for (int i = 0; drv_lic_relationship_list != null && i < drv_lic_relationship_list.length(); i++) {
            JSONObject item = drv_lic_relationship_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.drv_lic_relationship_list_key.add(key);
            configResp.drv_lic_relationship_list_value.add(value);
        }

        JSONArray busi_type_list = jsonObject.optJSONArray("busi_type_list");
        for (int i = 0; busi_type_list != null && i < busi_type_list.length(); i++) {
            JSONObject item = busi_type_list.getJSONObject(i);
            String key = item.keys().next();
            String value = item.getString(key);
            configResp.busi_type_list_key.add(key);
            configResp.busi_type_list_value.add(value);
        }


        JSONArray client_material = jsonObject.optJSONArray("client_material");
        configResp.client_material = client_material != null ? client_material.toString() : "";

        YusionApp.CONFIG_RESP = configResp;
        return configResp;
    }
}

package com.yusion.shanghai.yusion.ui.update;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.adapter.UploadLabelListAdapter;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.upload.ListLabelsErrorReq;
import com.yusion.shanghai.yusion.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.ui.apply.DocumentActivity;
import com.yusion.shanghai.yusion.ui.info.UploadLabelListActivity;
import com.yusion.shanghai.yusion.ui.info.UploadListActivity;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateImgsLabelFragment extends BaseFragment {

    private UploadLabelListAdapter mAdapter;
    private List<UploadLabelItemBean> mItems = new ArrayList<>();
    private String mCltId;
    private String mRole;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_img, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.title_bar).setVisibility(View.GONE);
        rv = (RecyclerView) view.findViewById(R.id.update_img_rv);
        UpdateSpouseInfoActivity usia = new UpdateSpouseInfoActivity();

        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new UploadLabelListAdapter(mContext, mItems);
        mAdapter.setOnItemClick(new UploadLabelListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadLabelItemBean item, int index) {
                Intent intent = new Intent();
                if (item.label_list.size() == 0) {
                    //下级目录为图片页
                    intent.setClass(mContext, UploadListActivity.class);
                    intent.putExtra("role", mRole);
                    if (item.name.contains("授权书")) {
                        intent.setClass(mContext, OnlyReadUploadListActivity.class);
                    }
                    if (item.name.contains("人像面")) {
                        intent.setClass(mContext, DocumentActivity.class);
                    } else if (item.name.contains("国徽面")) {
                        intent.setClass(mContext, DocumentActivity.class);
                    } else if (item.name.contains("驾驶证")) {
                        intent.setClass(mContext, DocumentActivity.class);
                    }
                } else {
                    //下级目录为标签页
                    intent.setClass(mContext, UploadLabelListActivity.class);
                }
                intent.putExtra("topItem", item);
                //clt_id取图片
                intent.putExtra("clt_id", mCltId);
                intent.putExtra("index", index);
                startActivityForResult(intent, 100);
            }
        });
        rv.setAdapter(mAdapter);
//
//        if (!usia.ishaveImgs){
//            rv.removeAllViews();
//        }else {
//            //initShamData();
        initData();
//        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (getActivity() instanceof UpdateSpouseInfoActivity) {
                if (((UpdateSpouseInfoActivity) getActivity()).clientInfo.marriage.equals("已婚")) {
                    rv.setEnabled(true);
                } else {
                    rv.setEnabled(false);
                }
            }
        }
    }

    private void initData() {
        try {
            JSONArray jsonArray = new JSONArray(YusionApp.CONFIG_RESP.client_material);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UploadLabelItemBean uploadLabelItemBean = new Gson().fromJson(jsonObject.toString(), UploadLabelItemBean.class);
                mItems.add(uploadLabelItemBean);
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initShamData() {
        mItems = new ArrayList<>();
        UploadLabelItemBean itemBean1 = new UploadLabelItemBean();
        itemBean1.name = "征信授权书";
        UploadLabelItemBean itemBean2 = new UploadLabelItemBean();
        itemBean2.name = "身份证人像面";
        UploadLabelItemBean itemBean3 = new UploadLabelItemBean();
        itemBean3.name = "身份证国徽面";
        UploadLabelItemBean itemBean4 = new UploadLabelItemBean();
        itemBean4.name = "婚姻材料";
        UploadLabelItemBean itemBean5 = new UploadLabelItemBean();
        itemBean5.name = "户口本";
        UploadLabelItemBean itemBean6 = new UploadLabelItemBean();
        itemBean6.name = "居住材料";
        UploadLabelItemBean itemBean7 = new UploadLabelItemBean();
        itemBean7.name = "驾照";
        UploadLabelItemBean itemBean8 = new UploadLabelItemBean();
        itemBean8.name = "收入类材料";

        List<UploadLabelItemBean> labelList = new ArrayList<>();
        UploadLabelItemBean itemBea81 = new UploadLabelItemBean();
        itemBea81.name = "半年以上银行流水";
        UploadLabelItemBean itemBea82 = new UploadLabelItemBean();
        itemBea82.name = "资产证明";
        labelList.add(itemBea81);
        labelList.add(itemBea82);
        itemBean8.label_list = labelList;

        mItems.add(itemBean1);
        mItems.add(itemBean2);
        mItems.add(itemBean3);
        mItems.add(itemBean4);
        mItems.add(itemBean5);
        mItems.add(itemBean6);
        mItems.add(itemBean7);
        mItems.add(itemBean8);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                UploadLabelItemBean item = (UploadLabelItemBean) data.getSerializableExtra("topItem");
                mItems.set(data.getIntExtra("index", -1), item);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setCltIdAndRole(String clt_id, String role) {
        mCltId = clt_id;
        mRole = role;

        ListLabelsErrorReq req = new ListLabelsErrorReq();
        req.clt_id = mCltId;
        ArrayList<String> labelsList = new ArrayList<>();
        for (UploadLabelItemBean itemBean : mItems) {
            labelsList.add(itemBean.value);
        }
        req.label_list = labelsList;
        UploadApi.listLabelsError(mContext, req, new OnItemDataCallBack<ListLabelsErrorResp>() {
            @Override
            public void onItemDataCallBack(ListLabelsErrorResp resp) {
                loop:
                for (UploadLabelItemBean uploadLabelItemBean : mItems) {
                    for (String hasImgLabel : resp.has_img_labels) {
                        if (uploadLabelItemBean.value.equals(hasImgLabel)) {
                            uploadLabelItemBean.hasImg = true;
                            continue loop;
                        } else {
                            uploadLabelItemBean.hasImg = false;
                        }
                    }
                }

                loop:
                for (UploadLabelItemBean uploadLabelItemBean : mItems) {
                    for (String hasErrorLabel : resp.err_labels) {
                        if (uploadLabelItemBean.value.equals(hasErrorLabel)) {
                            uploadLabelItemBean.hasError = true;
                            continue loop;
                        } else {
                            uploadLabelItemBean.hasError = false;
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private Dialog mUploadFileDialog;

    public void requestUpload(String clt_id, OnVoidCallBack onVoidCallBack) {
        List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
        for (UploadLabelItemBean item : mItems) {
            for (UploadImgItemBean imgItemBean : item.img_list) {
                UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                if (!TextUtils.isEmpty(imgItemBean.s_url)) {
                    continue;
                }
                fileUrlBean.clt_id = clt_id;
                fileUrlBean.file_id = imgItemBean.objectKey;
                fileUrlBean.label = imgItemBean.type;
                uploadFileUrlBeanList.add(fileUrlBean);
            }
        }

        if (mUploadFileDialog == null) {
            mUploadFileDialog = LoadingUtils.createLoadingDialog(mContext);
            mUploadFileDialog.setCancelable(false);
        }
        mUploadFileDialog.show();
        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
        uploadFilesUrlReq.files = uploadFileUrlBeanList;
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "");
        UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq, (code, msg) -> {
            mUploadFileDialog.dismiss();
            onVoidCallBack.callBack();
        });
    }
}
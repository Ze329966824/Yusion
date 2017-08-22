package com.yusion.shanghai.yusion.ui.update;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.adapter.UploadLabelListAdapter;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.ui.info.UpdateUserInfoActivity;
import com.yusion.shanghai.yusion.ui.info.UploadLabelListActivity;
import com.yusion.shanghai.yusion.ui.info.UploadListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateImgsLabelFragment extends BaseFragment {

    private UploadLabelListAdapter mAdapter;
    private List<UploadLabelItemBean> mItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_img, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.title_bar).setVisibility(View.GONE);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.update_img_rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mItems = new ArrayList<>();
        initShamData();
        mAdapter = new UploadLabelListAdapter(mContext, mItems);
        mAdapter.setOnItemClick(new UploadLabelListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadLabelItemBean item, int index) {
                Intent intent = new Intent();
                if (item.label_list.size() == 0) {
                    //下级目录为图片页
                    intent.setClass(mContext, UploadListActivity.class);
//                    intent.putExtra("role", "lender");
                } else {
                    //下级目录为标签页
                    intent.setClass(mContext, UploadLabelListActivity.class);
                }
                intent.putExtra("topItem", item);
                //clt_id取图片
//                intent.putExtra("clt_id", ((UpdateUserInfoActivity) getActivity()).getUserInfoBean().clt_id);
                intent.putExtra("index", index);
                startActivityForResult(intent, 100);
            }
        });
        rv.setAdapter(mAdapter);
    }

    private void initShamData() {
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
}
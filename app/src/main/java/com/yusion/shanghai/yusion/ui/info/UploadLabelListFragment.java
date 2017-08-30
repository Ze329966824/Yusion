package com.yusion.shanghai.yusion.ui.info;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.adapter.UploadLabelListAdapter;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion.ui.update.OnlyReadUploadListActivity;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
//del
public class UploadLabelListFragment extends BaseFragment {


    private UploadLabelListAdapter mAdapter;
    private List<UploadLabelItemBean> mItems;
    private boolean isPage1;

    public static UploadLabelListFragment newInstance(List<UploadLabelItemBean> list, String type) {

        Bundle args = new Bundle();
        args.putSerializable("list", (Serializable) list);
        args.putString("type", type);
        UploadLabelListFragment fragment = new UploadLabelListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.upload_label_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPage1 = getArguments().getString("type").equals("page1");
        view.findViewById(R.id.title_bar).setVisibility(View.GONE);//该类和UploadLabelListActivity复用一个布局,一个需要titlebar一个不需要titlebar
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        mItems = (List<UploadLabelItemBean>) getArguments().getSerializable("list");
        mAdapter = new UploadLabelListAdapter(mContext, mItems);

        mAdapter.setOnItemClick(new UploadLabelListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadLabelItemBean item, int index) {
                Intent intent = new Intent();
                if (item.label_list.size() == 0) {
                    //下级目录为图片页
                    intent.setClass(mContext, UploadListActivity.class);
                    intent.putExtra("role", "lender");
                    if (item.name.contains("授权书")) {
                        intent.setClass(mContext, OnlyReadUploadListActivity.class);
                    }
                } else {
                    //下级目录为标签页
                    intent.setClass(mContext, UploadLabelListActivity.class);
                    intent.putExtra("role", item.value);
                }
                intent.putExtra("item", item);
                intent.putExtra("clt_id", ((UpdateUserInfoActivity) getActivity()).getUserInfoBean().clt_id);
                intent.putExtra("name", item.name);
                intent.putExtra("index", index);
                startActivityForResult(intent, 100);
            }
        });
        rv.setAdapter(mAdapter);
    }

    public void refresh(ListLabelsErrorResp resp) {
//        if (isPage1 && resp.lender.err_num > 0) {
//            for (UploadLabelItemBean item : mItems) {
//                for (String errLabel : resp.lender.err_labels) {
//                    if (item.value.equals(errLabel)) {
//                        item.hasError = true;
//                    }
//                }
//            }
//        } else {
//            if (resp.lender_sp.err_num > 0) {
//                for (UploadLabelItemBean mItem : mItems) {
//                    if (mItem.value.equals("lender_sp")) {
//                        mItem.hasError = true;
//                        for (UploadLabelItemBean labelItemBean : mItem.label_list) {
//                            for (String errLabel : resp.lender_sp.err_labels) {
//                                if (labelItemBean.value.equals(errLabel)) {
//                                    labelItemBean.hasError = true;
//                                } else {
//                                    labelItemBean.hasError = false;
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//            if (resp.guarantor.err_num > 0) {
//                for (UploadLabelItemBean mItem : mItems) {
//                    if (mItem.value.equals("guarantor")) {
//                        mItem.hasError = true;
//                        for (UploadLabelItemBean labelItemBean : mItem.label_list) {
//                            for (String errLabel : resp.guarantor.err_labels) {
//                                if (labelItemBean.value.equals(errLabel)) {
//                                    labelItemBean.hasError = true;
//                                } else {
//                                    labelItemBean.hasError = false;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (resp.guarantor_sp.err_num > 0) {
//                for (UploadLabelItemBean mItem : mItems) {
//                    if (mItem.value.equals("guarantor_sp")) {
//                        mItem.hasError = true;
//                        for (UploadLabelItemBean labelItemBean : mItem.label_list) {
//                            for (String errLabel : resp.guarantor_sp.err_labels) {
//                                if (labelItemBean.value.equals(errLabel)) {
//                                    labelItemBean.hasError = true;
//                                } else {
//                                    labelItemBean.hasError = false;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (resp.counter_guarantor.err_num > 0) {
//                for (UploadLabelItemBean mItem : mItems) {
//                    if (mItem.value.equals("counter_guarantor")) {
//                        mItem.hasError = true;
//                        for (UploadLabelItemBean labelItemBean : mItem.label_list) {
//                            for (String errLabel : resp.counter_guarantor.err_labels) {
//                                if (labelItemBean.value.equals(errLabel)) {
//                                    labelItemBean.hasError = true;
//                                } else {
//                                    labelItemBean.hasError = false;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (resp.counter_guarantor_sp.err_num > 0) {
//                for (UploadLabelItemBean mItem : mItems) {
//                    if (mItem.value.equals("counter_guarantor_sp")) {
//                        mItem.hasError = true;
//                        for (UploadLabelItemBean labelItemBean : mItem.label_list) {
//                            for (String errLabel : resp.counter_guarantor_sp.err_labels) {
//                                if (labelItemBean.value.equals(errLabel)) {
//                                    labelItemBean.hasError = true;
//                                } else {
//                                    labelItemBean.hasError = false;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                UploadLabelItemBean item = (UploadLabelItemBean) data.getSerializableExtra("item");
                mItems.set(data.getIntExtra("index", -1), item);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

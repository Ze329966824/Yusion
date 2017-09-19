package com.yusion.shanghai.yusion.ui.update;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.user.ListCurrentTpye;
import com.yusion.shanghai.yusion.retrofit.api.UserApi;
import com.yusion.shanghai.yusion.ui.upload.UploadLabelListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImgsListFragment extends BaseFragment {

    private String lender_clt_id = "";
    private String lender_sp_clt_id = "";
    private String guarantor_clt_id = "";
    private String guarantor_sp_clt_id = "";

    private RelativeLayout personal_imgs;
    private RelativeLayout personalspouse_imgs;
    private RelativeLayout guarantor_imgs;
    private RelativeLayout guarantorspouse_imgs;
    private LinearLayout guarantee_info;

    private View view;

    public ImgsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_imgs_list, container, false);
        initView(view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            UserApi.getListCurrentTpye(mContext, data -> {
                if (data != null) {
                    refresh(data);
                }
            });
        }
    }

    private void initView(View view) {
        personal_imgs = (RelativeLayout) view.findViewById(R.id.list_personal_imgs_layout);
        personalspouse_imgs = (RelativeLayout) view.findViewById(R.id.list_personalspouse_imgs_layout);
        guarantor_imgs = (RelativeLayout) view.findViewById(R.id.list_guarantor_imgs_layout);
        guarantorspouse_imgs = (RelativeLayout) view.findViewById(R.id.list_guarantorspouse_imgs_layout);
        guarantee_info = (LinearLayout) view.findViewById(R.id.guarantee_info);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(view);
        FragmentActivity activity = getActivity();
        //个人
        activity.findViewById(R.id.list_personal_imgs_layout).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UploadLabelListActivity.class);
            intent.putExtra("clt_id", lender_clt_id);
            intent.putExtra("role", "lender");
            intent.putExtra("title", "个人影像件资料");
            startActivity(intent);
        });

        //配偶
        activity.findViewById(R.id.list_personalspouse_imgs_layout).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UploadLabelListActivity.class);
            intent.putExtra("clt_id", lender_sp_clt_id);
            intent.putExtra("role", "lender_sp");
            intent.putExtra("title", "个人配偶影像件资料");
            startActivity(intent);
        });
        //担保人
        activity.findViewById(R.id.list_guarantor_imgs_layout).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UploadLabelListActivity.class);
            intent.putExtra("clt_id", guarantor_clt_id);
            intent.putExtra("role", "guarantor");
            intent.putExtra("title", "担保人影像件资料");
            startActivity(intent);
        });

        //担保人配偶
        activity.findViewById(R.id.list_guarantorspouse_imgs_layout).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UploadLabelListActivity.class);
            intent.putExtra("clt_id", guarantor_sp_clt_id);
            intent.putExtra("role", "guarantor_sp");
            intent.putExtra("title", "担保人配偶影像件资料");
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
    }

    public void refresh(ListCurrentTpye data) {
        initView(view);
        if (data != null) {
            //这个时候 我们需要时刻刷新四个人的clt_id
            lender_clt_id = data.lender;
            lender_sp_clt_id = data.lender_sp;
            guarantor_clt_id = data.guarantor;
            guarantor_sp_clt_id = data.guarantor_sp;


            if (TextUtils.isEmpty(lender_sp_clt_id)) {
                personalspouse_imgs.setVisibility(View.GONE);
            } else {
                personalspouse_imgs.setVisibility(View.VISIBLE);
            }
            if (data.guarantor_commited) {
                guarantor_imgs.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(data.guarantor_sp)) {
                    guarantorspouse_imgs.setVisibility(View.GONE);
                } else {
                    guarantorspouse_imgs.setVisibility(View.VISIBLE);
                }
            } else {
                guarantee_info.setVisibility(View.GONE);
            }


//
//            if (!TextUtils.isEmpty(data.guarantor)) {
//                guarantor_imgs.setVisibility(View.VISIBLE);
//                if (TextUtils.isEmpty(data.guarantor_sp)) {
//                    guarantorspouse_imgs.setVisibility(View.GONE);
//                }else {
//                    guarantorspouse_imgs.setVisibility(View.VISIBLE);
//                }
//            }
//
//            if (!TextUtils.isEmpty(data.lender)) {
//                personal_imgs.setVisibility(View.VISIBLE);
//
//                if (TextUtils.isEmpty(data.lender_sp)) {
//                    personalspouse_imgs.setVisibility(View.GONE);
//                }else {
//                    personalspouse_imgs.setVisibility(View.VISIBLE);
//
//                }
//            }


        }
    }
}

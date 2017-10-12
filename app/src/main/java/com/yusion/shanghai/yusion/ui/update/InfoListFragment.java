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

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.retrofit.api.UserApi;
import com.yusion.shanghai.yusion.ui.apply.guarantor.AddGuarantorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoListFragment extends BaseFragment {

    private LinearLayout guarantee_info;
    private LinearLayout add_guarantee;
    private PtrClassicFrameLayout ptr;

    public InfoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_list, container, false);

        initView(view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();

        //个人信息
        activity.findViewById(R.id.list_personal_info_layout).setOnClickListener(v -> startActivity(new Intent(mContext, UpdatePersonalInfoActivity.class)));
        //配偶信息
        activity.findViewById(R.id.list_personalspouse_info_layout).setOnClickListener(v -> startActivity(new Intent(mContext, UpdateSpouseInfoActivity.class)));
        //担保人信息
        activity.findViewById(R.id.list_guarantor_info).setOnClickListener(v -> startActivity(new Intent(mContext, UpdateGuarantorInfoActivity.class)));

        //担保人配偶信息
        activity.findViewById(R.id.list_guarantorspouse_info).setOnClickListener(v -> startActivity(new Intent(mContext, UpdateGuarantorSpouseInfoActivity.class)));
        //添加担保人
        activity.findViewById(R.id.icon_add_guarantee).setOnClickListener(v -> startActivity(new Intent(mContext, AddGuarantorActivity.class)));


    }

    private void initView(View view) {
        guarantee_info = (LinearLayout) view.findViewById(R.id.guarantee_info);
        add_guarantee = (LinearLayout) view.findViewById(R.id.add_guarantee);
        ptr = (PtrClassicFrameLayout) view.findViewById(R.id.List_info_ptr);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });

    }


    public void refresh() {
        UserApi.getListCurrentTpye(mContext, data -> {
            if (data != null) {
                ptr.refreshComplete();

                if (!TextUtils.isEmpty(data.lender)){
                    getView().findViewById(R.id.personal_info).setVisibility(View.VISIBLE);
                }


                if (data.guarantor_commited) {
                    getView().findViewById(R.id.add_guarantee).setVisibility(View.GONE);
                    getView().findViewById(R.id.guarantee_info).setVisibility(View.VISIBLE);
                } else {
                    getView().findViewById(R.id.add_guarantee).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.guarantee_info).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}

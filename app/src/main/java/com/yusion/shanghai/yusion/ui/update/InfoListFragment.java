package com.yusion.shanghai.yusion.ui.update;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.user.ListCurrentTpye;
import com.yusion.shanghai.yusion.ui.apply.guarantor.AddGuarantorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoListFragment extends BaseFragment {

    private LinearLayout guarantee_info;
    private LinearLayout add_guarantee;

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
    }



    public void refresh(ListCurrentTpye data) {
        if (data != null) {
            if (data.guarantor_commited) {
                add_guarantee.setVisibility(View.GONE);
                guarantee_info.setVisibility(View.VISIBLE);
            } else {
                guarantee_info.setVisibility(View.GONE);
                add_guarantee.setVisibility(View.VISIBLE);
            }
        }
    }
}

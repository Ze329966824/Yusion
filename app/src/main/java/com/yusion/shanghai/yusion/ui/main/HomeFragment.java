package com.yusion.shanghai.yusion.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.ui.apply.ApplyActivity;
import com.yusion.shanghai.yusion.ui.info.InfoListActivity;
import com.yusion.shanghai.yusion.ui.update.UpdatePersonalInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdateSecuritySpouseInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdateSpouseInfoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment {


    private Button bottomBtn;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomBtn = (Button) view.findViewById(R.id.home_bottom_btn);
        Button btn = (Button) view.findViewById(R.id.btn_test);
        Button btn2 = (Button) view.findViewById(R.id.btn_test2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateSecuritySpouseInfoActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateSpouseInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    public void refresh(CheckUserInfoResp userInfo) {
        if (userInfo.commited) {
            bottomBtn.setText("修改资料");
//            bottomBtn.setOnClickListener(v -> startActivity(new Intent(mContext, UpdateUserInfoActivity.class)));
            bottomBtn.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, UpdatePersonalInfoActivity.class);
//                Intent intent = new Intent(mContext, InfoListActivity.class);
//                intent.putExtra("ishaveGuarantee", true);
                startActivity(intent);
            });
        } else {
            bottomBtn.setText("立即申请");
            bottomBtn.setOnClickListener(v -> startActivity(new Intent(mContext, ApplyActivity.class)));
//            bottomBtn.setOnClickListener(v -> startActivity(new Intent(mContext, AddGuarantorActivity.class)));
        }
    }
}

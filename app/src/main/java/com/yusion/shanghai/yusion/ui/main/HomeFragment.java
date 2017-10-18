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
import com.yusion.shanghai.yusion.bean.user.ListCurrentTpye;
import com.yusion.shanghai.yusion.retrofit.api.UserApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.ui.apply.ApplyActivity;
import com.yusion.shanghai.yusion.ui.apply.DocumentActivity;
import com.yusion.shanghai.yusion.ui.order.FinancePlanActivity;
import com.yusion.shanghai.yusion.ui.update.InfoListActivity;
import com.yusion.shanghai.yusion.ui.update.UpdateGuarantorSpouseInfoActivity;
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
        Button btn4 = (Button) view.findViewById(R.id.btn4);
        Button btn3 = (Button) view.findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DocumentActivity.class);
                intent.putExtra("type", "id_card_back");
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FinancePlanActivity.class);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateGuarantorSpouseInfoActivity.class);
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
            bottomBtn.setText("查看个人资料");
//            bottomBtn.setOnClickListener(v -> startActivity(new Intent(mContext, UpdateUserInfoActivity.class)));
            bottomBtn.setOnClickListener(v -> {
                UserApi.getListCurrentTpye(mContext, new OnItemDataCallBack<ListCurrentTpye>() {
                    @Override
                    public void onItemDataCallBack(ListCurrentTpye data) {
                        if (data != null) {
                            if (data.guarantor_commited) {
                                Intent intent = new Intent(mContext, InfoListActivity.class);
                                intent.putExtra("ishaveGuarantee", true);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, InfoListActivity.class);
                                intent.putExtra("ishaveGuarantee", false);
                                startActivity(intent);
                            }
                        }
                    }
                });


            });
        } else {
            bottomBtn.setText("立即申请");
            bottomBtn.setOnClickListener(v -> startActivity(new Intent(mContext, ApplyActivity.class)));
//            bottomBtn.setOnClickListener(v -> startActivity(new Intent(mContext, AddGuarantorActivity.class)));
        }
    }
}

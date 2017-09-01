package com.yusion.shanghai.yusion.ui.main;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.bean.user.ListCurrentTpye;
import com.yusion.shanghai.yusion.retrofit.api.UserApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.ui.main.mine.SettingsActivity;
import com.yusion.shanghai.yusion.ui.update.InfoListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mineRepaymentPlanLin;
    private LinearLayout mineUserInfoLin;
    private LinearLayout mineSettingLin;
    private LinearLayout mineCustomerServiceLin;

    private LinearLayout mMineFragmentInfoLin;
    private TextView mMineFragmentNameTV;
    private TextView mMineFragmentPhoneTV;


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        mineRepaymentPlanLin = (LinearLayout) view.findViewById(R.id.mine_repayment_plan_lin);
        mineUserInfoLin = (LinearLayout) view.findViewById(R.id.mine_user_info_lin);
        mineSettingLin = (LinearLayout) view.findViewById(R.id.mine_setting_lin);
        mineCustomerServiceLin = (LinearLayout) view.findViewById(R.id.mine_customer_service_lin);

        mMineFragmentInfoLin = (LinearLayout) view.findViewById(R.id.mine_fragment_info_lin);
        mMineFragmentNameTV = (TextView) view.findViewById(R.id.mine_name_tv);
        mMineFragmentPhoneTV = (TextView) view.findViewById(R.id.mine_mobile_tv);

        mineRepaymentPlanLin.setOnClickListener(this);
        mineUserInfoLin.setOnClickListener(this);
        mineSettingLin.setOnClickListener(this);
        mineCustomerServiceLin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_repayment_plan_lin:
//                if (WangDaiApp.isLogin) {
//                    Toast.makeText(mContext, "还款计划", Toast.LENGTH_SHORT).show();
//                } else {
//                    requestLogin();
//                }
                break;
            case R.id.mine_user_info_lin:
//                if (WangDaiApp.isLogin) {
                UserApi.getListCurrentTpye(mContext, new OnItemDataCallBack<ListCurrentTpye>() {
                    @Override
                    public void onItemDataCallBack(ListCurrentTpye data) {
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
                });
//                } else {
//                    requestLogin();
//                }
                break;
            case R.id.mine_setting_lin:
//                if (WangDaiApp.isLogin) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
//                } else {
//                    requestLogin();
//                }
                break;
            case R.id.mine_customer_service_lin:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "13888888888"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

//                new UpdateAppManager.Builder()
//                        .setActivity(getActivity())
//                        .setUpdateUrl("http://imtt.dd.qq.com/16891/11BAB00A69C38A508A6EBF62D5CBA234.apk?fsname=com.qq.reader_6.5.2.888_101.apk&csr=1bbd")
//                        //实现httpManager接口的对象
//                        .setHttpManager(new HttpManager() {
//                            @Override
//                            public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {
//
//                            }
//
//                            @Override
//                            public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {
//
//                            }
//
//                            @Override
//                            public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull FileCallback callback) {
//
//                            }
//                        })
//                        .build()
//                        .update();
                break;
        }
    }


    public void refresh(CheckUserInfoResp userInfo) {
        mMineFragmentNameTV.setText(userInfo.name);
        mMineFragmentPhoneTV.setText(userInfo.mobile);
        if (!userInfo.commited) {
            mineUserInfoLin.setVisibility(View.GONE);
        } else {
            mineUserInfoLin.setVisibility(View.VISIBLE);
        }
    }
}

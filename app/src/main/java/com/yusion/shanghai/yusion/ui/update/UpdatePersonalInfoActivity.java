package com.yusion.shanghai.yusion.ui.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;
import com.yusion.shanghai.yusion.bean.user.GetClientInfoReq;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.service.ProductApi;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class UpdatePersonalInfoActivity extends BaseActivity {

    private UpdatePersonalInfoFragment mUpdatePersonalInfoFragment;
    private UpdateImgsLabelFragment mUpdateImgsLabelFragment;
    private String[] mTabTitle = {"个人资料", "影像件"};


    private EditText update_personal_info_clt_nm_edt;                       //姓名
    private EditText update_personal_info_id_no_edt;                        //身份证号
    private TextView update_personal_info_gender_tv;                        //性别
    private TextView update_personal_info_reg_tv;                           //户籍
    private EditText update_personal_info_mobile_edt;                       //手机号
    private TextView update_personal_info_education_tv;                     //学历
    private TextView update_personal_info_current_address_tv;               //现住地址
    private TextView update_personal_info_current_address1_tv;              //详细地址
    private EditText update_personal_info_current_address2_tv;              //门牌号
    private TextView update_personal_info_income_from_tv;                   //主要收入来源
    private EditText update_personal_info_from_income_year_edt;             //主要-工资-年收入
    private EditText update_personal_info_from_income_company_name_edt;     //主要-工资-单位名称
    private TextView update_personal_info_from_income_company_address_tv;   //主要-工资-单位地址
    private TextView update_personal_info_from_income_company_address1_tv;  //主要-工资-详细地址
    private EditText update_personal_info_from_income_company_address2_tv;  //主要-工资-门牌号
    private TextView update_personal_info_work_position_tv;                 //主要-工资-职务
    private EditText update_personal_info_from_income_work_phone_num_edt;   //主要-工资-单位座机
    private EditText update_personal_info_from_self_year_edt;               //主要-自营-年收入
    private TextView update_personal_info_from_self_type_tv;                //主要-自营-业务类型
    private EditText update_personal_info_from_self_company_name_edt;       //主要-自营-店铺名称
    private TextView update_personal_info_from_self_company_address_tv;     //主要-自营-单位地址
    private TextView update_personal_info_from_self_company_address1_tv;    //主要-自营-详细地址
    private EditText update_personal_info_from_self_company_address2_tv;    //主要-自营-门牌号
    private EditText update_personal_info_from_other_year_edt;              //主要-其他-年收入
    private EditText update_personal_info_from_other_remark_tv;             //主要-其他-备注
    private TextView update_personal_info_extra_income_from_tv;             //额外收入来源
    private EditText update_personal_info_extra_from_income_year_edt;            //额外-工资-年收入
    private EditText update_personal_info_extra_from_income_company_name_edt;    //额外-工资-单位名称
    private TextView update_personal_info_extra_from_income_company_address_tv;  //额外-工资-公司地址
    private TextView update_personal_info_extra_from_income_company_address1_tv; //额外-工资-详细地址
    private EditText update_personal_info_extra_from_income_company_address2_tv; //额外-工资-门牌号
    private TextView update_personal_extra_info_work_position_tv;                //额外-工资-职务
    private EditText update_personal_info_extra_from_income_work_phone_num_edt;  //额外-工资-单位座机
    private TextView update_personal_info_house_type_tv;                     //房屋性质
    private EditText update_personal_info_house_area_edt;                    //房屋面积
    private EditText update_personal_info_house_owner_name_edt;              //房屋所有人
    private TextView update_personal_info_house_owner_relation_tv;           //与申请人关系
    private TextView update_personal_info_urg_relation1_tv;           //紧急联系人-与申请人关系1
    private EditText update_personal_info_urg_mobile1_edt;            //紧急联系人-手机号1
    private EditText update_personal_info_urg_contact1_edt;           //紧急联系人-姓名1
    private TextView update_personal_info_urg_relation2_tv;           //紧急联系人-与申请人关系2
    private EditText update_personal_info_urg_mobile2_edt;            //紧急联系人-手机号2
    private EditText update_personal_info_urg_contact2_edt;           //紧急联系人-姓名2


    private ClientInfo clientInfo;
    public ImageView submit_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personal_info);
        initTitleBar(this, "个人资料").setLeftClickListener(v -> showDoubleCheckForExit());

        initView();


        getInfo();  //获取用户信息

        submit();   //更新用户信息


    }

    private void submit() {
        findViewById(R.id.submit_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });

    }

    private void initAllData() {
        update_personal_info_clt_nm_edt = (EditText) findViewById(R.id.update_personal_info_clt_nm_edt);
        update_personal_info_id_no_edt = (EditText) findViewById(R.id.update_personal_info_id_no_edt);
        update_personal_info_gender_tv = (TextView) findViewById(R.id.update_personal_info_gender_tv);
        update_personal_info_reg_tv = (TextView) findViewById(R.id.update_personal_info_reg_tv);
        update_personal_info_mobile_edt = (EditText) findViewById(R.id.update_personal_info_mobile_edt);
        update_personal_info_education_tv = (TextView) findViewById(R.id.update_personal_info_education_tv);
        update_personal_info_current_address_tv = (TextView) findViewById(R.id.update_personal_info_current_address_tv);
        update_personal_info_current_address1_tv = (TextView) findViewById(R.id.update_personal_info_current_address1_tv);
        update_personal_info_current_address2_tv = (EditText) findViewById(R.id.update_personal_info_current_address2_tv);
        update_personal_info_income_from_tv = (TextView) findViewById(R.id.update_personal_info_income_from_tv);
        update_personal_info_from_income_year_edt = (EditText) findViewById(R.id.update_personal_info_from_income_year_edt);
        update_personal_info_from_income_company_name_edt = (EditText) findViewById(R.id.update_personal_info_from_income_company_name_edt);
        update_personal_info_from_income_company_address_tv = (TextView) findViewById(R.id.update_personal_info_from_income_company_address_tv);
        update_personal_info_from_income_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_from_income_company_address1_tv);
        update_personal_info_from_income_company_address2_tv = (EditText) findViewById(R.id.update_personal_info_from_income_company_address2_tv);
        update_personal_info_work_position_tv = (TextView) findViewById(R.id.update_personal_info_work_position_tv);
        update_personal_info_from_income_work_phone_num_edt = (EditText) findViewById(R.id.update_personal_info_from_income_work_phone_num_edt);
        update_personal_info_from_self_year_edt = (EditText) findViewById(R.id.update_personal_info_from_self_year_edt);
        update_personal_info_from_self_type_tv = (TextView) findViewById(R.id.update_personal_info_from_self_type_tv);
        update_personal_info_from_self_company_name_edt = (EditText) findViewById(R.id.update_personal_info_from_self_company_name_edt);
        update_personal_info_from_self_company_address_tv = (TextView) findViewById(R.id.update_personal_info_from_self_company_address_tv);
        update_personal_info_from_self_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_from_self_company_address1_tv);
        update_personal_info_from_self_company_address2_tv = (EditText) findViewById(R.id.update_personal_info_from_self_company_address2_tv);
        update_personal_info_from_other_year_edt = (EditText) findViewById(R.id.update_personal_info_from_other_year_edt);
        update_personal_info_from_other_remark_tv = (EditText) findViewById(R.id.update_personal_info_from_other_remark_tv);
        update_personal_info_extra_income_from_tv = (TextView) findViewById(R.id.update_personal_info_extra_income_from_tv);
        update_personal_info_extra_from_income_year_edt = (EditText) findViewById(R.id.update_personal_info_extra_from_income_year_edt);
        update_personal_info_extra_from_income_company_name_edt = (EditText) findViewById(R.id.update_personal_info_extra_from_income_company_name_edt);
        update_personal_info_extra_from_income_company_address_tv = (TextView) findViewById(R.id.update_personal_info_extra_from_income_company_address_tv);
        update_personal_info_extra_from_income_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_extra_from_income_company_address1_tv);
        update_personal_info_extra_from_income_company_address2_tv = (EditText) findViewById(R.id.update_personal_info_extra_from_income_company_address2_tv);
        update_personal_extra_info_work_position_tv = (TextView) findViewById(R.id.update_personal_extra_info_work_position_tv);
        update_personal_info_extra_from_income_work_phone_num_edt = (EditText) findViewById(R.id.update_personal_info_extra_from_income_work_phone_num_edt);
        update_personal_info_house_type_tv = (TextView) findViewById(R.id.update_personal_info_house_type_tv);
        update_personal_info_house_area_edt = (EditText) findViewById(R.id.update_personal_info_house_area_edt);
        update_personal_info_house_owner_name_edt = (EditText) findViewById(R.id.update_personal_info_house_owner_name_edt);
        update_personal_info_house_owner_relation_tv = (TextView) findViewById(R.id.update_personal_info_house_owner_relation_tv);
        update_personal_info_urg_relation1_tv = (TextView) findViewById(R.id.update_personal_info_urg_relation1_tv);
        update_personal_info_urg_mobile1_edt = (EditText) findViewById(R.id.update_personal_info_urg_mobile1_edt);
        update_personal_info_urg_contact1_edt = (EditText) findViewById(R.id.update_personal_info_urg_contact1_edt);
        update_personal_info_urg_relation2_tv = (TextView) findViewById(R.id.update_personal_info_urg_relation2_tv);
        update_personal_info_urg_mobile2_edt = (EditText) findViewById(R.id.update_personal_info_urg_mobile2_edt);
        update_personal_info_urg_contact2_edt = (EditText) findViewById(R.id.update_personal_info_urg_contact2_edt);

    }


    private void getInfo() {
        ProductApi.getClientInfo(this, new GetClientInfoReq(), new OnItemDataCallBack<ClientInfo>() {
            @Override
            public void onItemDataCallBack(ClientInfo data) {
                if (data != null) {
                    clientInfo = data;
                    mUpdatePersonalInfoFragment.getClientinfo(clientInfo);
                    mUpdateImgsLabelFragment.setCltIdAndRole(clientInfo.clt_id, "lender");
                }
                return;

            }
        });
    }

    public void commit() {
        mUpdatePersonalInfoFragment.updateClientinfo();
        ProductApi.updateClientInfo(UpdatePersonalInfoActivity.this, clientInfo, new OnItemDataCallBack<ClientInfo>() {
            @Override
            public void onItemDataCallBack(ClientInfo data) {
                if (data != null) {
                    Intent intent = new Intent(UpdatePersonalInfoActivity.this, CommitActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }


    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mUpdatePersonalInfoFragment = new UpdatePersonalInfoFragment();
        mUpdateImgsLabelFragment = new UpdateImgsLabelFragment();
        mFragments.add(mUpdatePersonalInfoFragment);
        mFragments.add(mUpdateImgsLabelFragment);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new InfoViewPagerAdapter(getSupportFragmentManager(), mFragments));
        MagicIndicator mMagicIndicator = (MagicIndicator) findViewById(R.id.tab_layout);
        ImageView mSubmitImg = (ImageView) findViewById(R.id.submit_img);
        mSubmitImg.setOnClickListener(v -> finish());
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTabTitle == null ? 0 : mTabTitle.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(0xFF999999);
                colorTransitionPagerTitleView.setSelectedColor(0xFF06B7A3);
                colorTransitionPagerTitleView.setText(mTabTitle[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.system_color));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(mMagicIndicator, viewPager);
    }

    private class InfoViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments;

        InfoViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

    }


    private void showDoubleCheckForExit() {
        new AlertDialog.Builder(this).setMessage("退出时未提交的更新将会被舍弃")
                .setPositiveButton("确定该操作", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("取消该操作", (dialog, which) -> dialog.dismiss()).show();
    }

}

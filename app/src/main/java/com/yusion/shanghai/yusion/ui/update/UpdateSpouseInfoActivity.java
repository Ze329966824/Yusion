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
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
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

public class UpdateSpouseInfoActivity extends BaseActivity {
    private UpdateSpouseInfoFragment mUpdateSpouseInfoFragment;
    private UpdateImgsLabelFragment mUpdateImgsLabelFragment;
    private String[] mTabTitle = {"配偶信息", "影像件"};

    private ClientInfo clientInfo;

    private TextView update_spouse_info_marriage_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spouse_info);
        initTitleBar(this, "配偶资料").setLeftClickListener(v -> showDoubleCheckForExit());
        initView();

        getInfo();  //获取配偶信息
        submit();   //更新配偶信息
    }

    private void getInfo() {
        ProductApi.getClientInfo(this, new GetClientInfoReq(), new OnItemDataCallBack<ClientInfo>() {
            @Override
            public void onItemDataCallBack(ClientInfo data) {
                if (data != null) {
                    clientInfo = data;
                    mUpdateSpouseInfoFragment.getClientinfo(clientInfo);
                    mUpdateImgsLabelFragment.setCltIdAndRole(clientInfo.clt_id, "lender");
                }
            }
        });
    }

    private void submit() {
        findViewById(R.id.submit_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    private void commit() {

        mUpdateSpouseInfoFragment.updateimgUrl();
        ProductApi.updateClientInfo(UpdateSpouseInfoActivity.this, clientInfo, new OnItemDataCallBack<ClientInfo>() {
            @Override
            public void onItemDataCallBack(ClientInfo data) {
                if (data != null) {
                    Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private void initCommit() {
        findViewById(R.id.submit_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mUpdateSpouseInfoFragment = new UpdateSpouseInfoFragment();
        mUpdateImgsLabelFragment = new UpdateImgsLabelFragment();
        mFragments.add(mUpdateSpouseInfoFragment);
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

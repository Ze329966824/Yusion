package com.yusion.shanghai.yusion.ui.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.user.GetGuarantorInfoReq;
import com.yusion.shanghai.yusion.bean.user.GuarantorInfo;
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

public class UpdateGuarantorInfoActivity extends BaseActivity {
    private UpdateGuarantorInfoFragment mUpdateGuarantorInfoFragment;
    private UpdateImgsLabelFragment mUpdateImgsLabelFragment;
    private String[] mTabTitle = {"担保人资料", "影像件"};
    private GuarantorInfo guarantorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_guarantor_info);
        initTitleBar(this, "担保人资料").setLeftClickListener(v -> showDoubleCheckForExit());
        initView();

        getInfo();  //获取担保人信息
        findViewById(R.id.submit_img).setOnClickListener(v -> {
            submit();   //更新用户信息
        });
    }


    private void getInfo() {
        ProductApi.getGuarantorInfo(this, new GetGuarantorInfoReq(), data -> {
            if (data != null) {
                guarantorInfo = data;
                mUpdateGuarantorInfoFragment.getGuarantorInfo(guarantorInfo);
                mUpdateImgsLabelFragment.setCltIdAndRole(guarantorInfo.clt_id, "lender");
            }
        });


    }

    private void submit() {
        //提交用户资料
        mUpdateGuarantorInfoFragment.updateGuarantorinfo(() -> ProductApi.updateGuarantorInfo(UpdateGuarantorInfoActivity.this, guarantorInfo, data -> {
            //上传影像件
            mUpdateImgsLabelFragment.requestUpload(guarantorInfo.clt_id, () -> {
                if (data == null) return;
                Intent intent = new Intent(UpdateGuarantorInfoActivity.this, CommitActivity.class);
                startActivity(intent);
                finish();
            });
        }));


//        mUpdateImgsLabelFragment.requestUpload(guarantorInfo.clt_id, new OnVoidCallBack() {
//            @Override
//            public void callBack() {
//                if (mUpdateGuarantorInfoFragment.updateGuarantorinfo()) {
//                    ProductApi.updateGuarantorInfo(UpdateGuarantorInfoActivity.this, guarantorInfo, new OnItemDataCallBack<GuarantorInfo>() {
//                        @Override
//                        public void onItemDataCallBack(GuarantorInfo data) {
//                            if (data == null) return;
//                            Intent intent = new Intent(UpdateGuarantorInfoActivity.this, CommitActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                }
//            }
//        });
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mUpdateGuarantorInfoFragment = new UpdateGuarantorInfoFragment();
        mUpdateImgsLabelFragment = new UpdateImgsLabelFragment();
        mFragments.add(mUpdateGuarantorInfoFragment);
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
            public IPagerTitleView getTitleView(Context context, int i) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(0xFF999999);
                colorTransitionPagerTitleView.setSelectedColor(0xFF06B7A3);
                colorTransitionPagerTitleView.setText(mTabTitle[i]);
                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(i));
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

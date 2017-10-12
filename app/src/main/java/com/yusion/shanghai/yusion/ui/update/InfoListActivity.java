package com.yusion.shanghai.yusion.ui.update;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;

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

public class InfoListActivity extends BaseActivity {


    private InfoListFragment mInfoListFragment;
    private ImgsListFragment mImgsListFragment;
    private String[] mTabTitle = {"关联人资料", "关联人影像件"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);

        initTitleBar(InfoListActivity.this, "资料列表");


        initView();

//        initTitleBar(this, getResources().getString(R.string.list_info));
//        guarantee_info = (LinearLayout) findViewById(R.id.guarantee_info);
//        add_guarantee = (LinearLayout) findViewById(R.id.add_guarantee);

    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mInfoListFragment = new InfoListFragment();
        mImgsListFragment = new ImgsListFragment();
        mFragments.add(mInfoListFragment);
        mFragments.add(mImgsListFragment);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new InfoViewPagerAdapter(getSupportFragmentManager(), mFragments));
        MagicIndicator mMagicIndicator = (MagicIndicator) findViewById(R.id.tab_layout);
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


//    private void updateHaveGuaranteeStatus() {
//        UserApi.getListCurrentTpye(InfoListActivity.this, contact -> {
//            if (contact != null) {
//                if (contact.guarantor_commited) {
//                    add_guarantee.setVisibility(View.GONE);
//                    guarantee_info.setVisibility(View.VISIBLE);
//                } else {
//                    guarantee_info.setVisibility(View.GONE);
//                    add_guarantee.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }

//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.list_personal_info_layout:
//                startActivity(new Intent(InfoListActivity.this, UpdatePersonalInfoActivity.class));
//                break;
//            case R.id.list_personalspouse_info_layout:
//                startActivity(new Intent(InfoListActivity.this, UpdateSpouseInfoActivity.class));
//                break;
//            case R.id.list_guarantor_info:
//                startActivity(new Intent(InfoListActivity.this, UpdateGuarantorInfoActivity.class));
//                break;
//            case R.id.list_guarantorspouse_info:
//                startActivity(new Intent(InfoListActivity.this, UpdateGuarantorSpouseInfoActivity.class));
//                break;
//            case R.id.icon_add_guarantee:
//                startActivity(new Intent(InfoListActivity.this, AddGuarantorActivity.class));
//                break;
//
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        updateHaveGuaranteeStatus();
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
}

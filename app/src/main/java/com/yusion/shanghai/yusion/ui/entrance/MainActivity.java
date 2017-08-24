package com.yusion.shanghai.yusion.ui.entrance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.CheckUserInfoResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.ui.main.HomeFragment;
import com.yusion.shanghai.yusion.ui.main.MineFragment;
import com.yusion.shanghai.yusion.ui.main.MyOrderFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private HomeFragment mHomeFragment;
    private MyOrderFragment mOrderFragment;
    private MineFragment mMineFragment;
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApp.isBack2Home = false;
        myApp.isLogin = true;

//        Log.e("TAG", "token: " + WangDaiApp.mToken);

//        WangDaiApp.isBack2Home = false;

        setContentView(R.layout.activity_main);

//        if (getIntent().getBooleanExtra("toJPushDialogActivity", false)) {
//            Intent intent = getIntent();
//            intent.setClass(this, JPushDialogActivity.class);
//            startActivity(getIntent());
//        }

        mHomeFragment = new HomeFragment();
        mOrderFragment = new MyOrderFragment();
        mMineFragment = new MineFragment();
        mCurrentFragment = mHomeFragment;
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container, mHomeFragment)
                .add(R.id.main_container, mOrderFragment)
                .add(R.id.main_container, mMineFragment)
                .hide(mHomeFragment)
                .hide(mOrderFragment)
                .hide(mMineFragment)
                .show(mHomeFragment)
                .commit();
        mCurrentFragment = mHomeFragment;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.main_tab_home_rb:
                transaction.hide(mCurrentFragment).show(mHomeFragment);
                mCurrentFragment = mHomeFragment;
                break;
            case R.id.main_tab_apply_rb:
                transaction.hide(mCurrentFragment).show(mOrderFragment);
                mCurrentFragment = mOrderFragment;
                break;
            case R.id.main_tab_mine_rb:
                transaction.hide(mCurrentFragment).show(mMineFragment);
                mCurrentFragment = mMineFragment;
                break;
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApp.isBack2Home = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigApi.getConfigJson(this, resp -> {});
        AuthApi.checkUserInfo(this, new OnItemDataCallBack<CheckUserInfoResp>() {
            @Override
            public void onItemDataCallBack(CheckUserInfoResp data) {
                mHomeFragment.refresh(data);
                mMineFragment.refresh(data);
            }
        });
    }
}

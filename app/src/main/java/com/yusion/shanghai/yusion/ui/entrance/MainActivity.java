package com.yusion.shanghai.yusion.ui.entrance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.api.ConfigApi;
import com.yusion.shanghai.yusion.ui.main.HomeFragment;
import com.yusion.shanghai.yusion.ui.main.MineFragment;
import com.yusion.shanghai.yusion.ui.main.MyOrderFragment;
import com.yusion.shanghai.yusion.widget.SelfDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private HomeFragment mHomeFragment;
    private MyOrderFragment mOrderFragment;
    private MineFragment mMineFragment;
    private Fragment mCurrentFragment;
    private FragmentManager mFragmentManager;
    private boolean isNeedAgreement;
    private boolean isNoAccept;
    private boolean hasaccept;
    private SelfDialog selfDialog;
    private boolean is_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YusionApp.isLogin = true;
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


        AuthApi.checkUserInfo(this, data -> {

            Log.e("sss", String.valueOf(data.is_agree));
            is_agree = data.is_agree;
            if (!is_agree) {
                Log.e("sssss", String.valueOf(is_agree));
                Intent intent = new Intent(MainActivity.this, AgreeMentActivity.class);
                startActivityForResult(intent, 101);
                // 一个是新启动的activity进入时的动画，另一个是当前activity消失时的动画
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            }
        });
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
    protected void onResume() {
        super.onResume();
        ConfigApi.getConfigJson(this, resp -> {
        });

        AuthApi.checkUserInfo(this, data -> {
            mHomeFragment.refresh(data);
            mMineFragment.refresh(data);
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101) {
            isNoAccept = data.getBooleanExtra("noAccept", false);
            if (isNoAccept) {
                selfDialog = new SelfDialog(this);
                selfDialog.setCancelable(false);
                selfDialog.setCanceledOnTouchOutside(false);
                selfDialog.setYesOnclickListener("我知道了", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        Intent intent = new Intent(MainActivity.this, AgreeMentActivity.class);
                        startActivityForResult(intent, 101);
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
                        selfDialog.dismiss();
                    }
                });
                selfDialog.show();
            }
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//            return true;//不执行父类点击事件
//        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
//    }
}

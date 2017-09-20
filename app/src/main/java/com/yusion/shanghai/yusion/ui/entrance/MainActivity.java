package com.yusion.shanghai.yusion.ui.entrance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
    private SelfDialog selfDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isNeedAgreement = getIntent().getBooleanExtra("isNeedAgreement", false);
        YusionApp.isBack2Home = false;
        YusionApp.isLogin = true;

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

        if (isNeedAgreement) {
            Intent intent = new Intent(MainActivity.this, AgreeMentActivity.class);
            //startActivity(intent);
            startActivityForResult(intent, 101);
            // 一个是新启动的activity进入时的动画，另一个是当前activity消失时的动画
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            isNeedAgreement = false;
        }

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
        YusionApp.isBack2Home = true;
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
            selfDialog = new SelfDialog(this);
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


//            if (isNoAccept) {
//                new AlertDialog.Builder(this)
//                        .setMessage("当您同意\n《客户信息获取、保密政策》\n方可使用予见汽车")
//                        .setNeutralButton("我知道了", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(MainActivity.this, AgreeMentActivity.class);
//                                //startActivity(intent);
//                                startActivityForResult(intent, 101);
//                                overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
//                            }
//                        })
//                        .show();
//            }
        }
    }
}

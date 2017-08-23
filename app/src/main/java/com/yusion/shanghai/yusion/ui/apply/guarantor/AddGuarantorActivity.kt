package com.yusion.shanghai.yusion.ui.apply.guarantor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.base.BaseActivity
import com.yusion.shanghai.yusion.bean.user.GuarantorInfo
import com.yusion.shanghai.yusion.event.AddGuarantorActivityEvent
import com.yusion.shanghai.yusion.retrofit.service.ProductApi
import com.yusion.shanghai.yusion.ui.update.CommitActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class AddGuarantorActivity : BaseActivity() {
    private var mGuarantorCreditInfoFragment: GuarantorCreditInfoFragment? = null
    private var mGuarantorInfoFragment: GuarantorInfoFragment? = null
    private var mGuarantorSpouseInfoFragment: GuarantorSpouseInfoFragment? = null
    private var mCurrentFragment: Fragment? = null
    var mGuarantorInfo = GuarantorInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guarantor)
        initView()
        EventBus.getDefault().register(this)
    }

    private fun initView() {
        initTitleBar(this, "添加担保人信息").setLeftClickListener {
            AlertDialog.Builder(this).setMessage("您确定退出该页面返回首页?")
                    .setPositiveButton("确定退出") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }.setNegativeButton("取消退出") { dialog, _ -> dialog.dismiss() }.show()
        }
        mGuarantorCreditInfoFragment = GuarantorCreditInfoFragment()
        mGuarantorInfoFragment = GuarantorInfoFragment()
        mGuarantorSpouseInfoFragment = GuarantorSpouseInfoFragment()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, mGuarantorCreditInfoFragment)
                .add(R.id.container, mGuarantorInfoFragment)
                .add(R.id.container, mGuarantorSpouseInfoFragment)
                .hide(mGuarantorInfoFragment)
                .hide(mGuarantorSpouseInfoFragment)
                .commit()
        mCurrentFragment = mGuarantorCreditInfoFragment
    }

    override fun onBackPressed() {
    }

    fun requestSubmit() {
        ProductApi.updateGuarantorInfo(this, mGuarantorInfo) {
            if (it != null) {
                startActivity(Intent(this,CommitActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }

    @Subscribe fun changeFragment(event: AddGuarantorActivityEvent) {
        val transaction = supportFragmentManager.beginTransaction()
        when (event) {
            AddGuarantorActivityEvent.showGuarantorCreditInfoFragment -> {
                transaction.hide(mCurrentFragment).show(mGuarantorCreditInfoFragment)
                mCurrentFragment = mGuarantorCreditInfoFragment
            }
            AddGuarantorActivityEvent.showGuarantorInfoFragment -> {
                transaction.hide(mCurrentFragment).show(mGuarantorInfoFragment)
                mCurrentFragment = mGuarantorInfoFragment
            }
            AddGuarantorActivityEvent.showGuarantorSpouseInfoFragment -> {
                transaction.hide(mCurrentFragment).show(mGuarantorSpouseInfoFragment)
                mCurrentFragment = mGuarantorSpouseInfoFragment
            }
        }
        transaction.commit()
    }
}

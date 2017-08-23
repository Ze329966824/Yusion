package com.yusion.shanghai.yusion.ui.apply


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.base.BaseActivity
import com.yusion.shanghai.yusion.bean.ocr.OcrResp
import com.yusion.shanghai.yusion.bean.user.ClientInfo
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.retrofit.service.ProductApi
import com.yusion.shanghai.yusion.ui.update.CommitActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ApplyActivity : BaseActivity() {
    private var mAutonymCertifyFragment: AutonymCertifyFragment? = null
    private var mPersonalInfoFragment: PersonalInfoFragment? = null
    private var mSpouseInfoFragment: SpouseInfoFragment? = null
    private var mCurrentFragment: Fragment? = null
    var mOcrRespByAutonymCertify: OcrResp = OcrResp()
    var mClientInfo = ClientInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)
        initView()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }

    fun requestSubmit() {
        ProductApi.updateClientInfo(this, mClientInfo) {
            if (it != null) {
                startActivity(Intent(this, CommitActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
    }

    @Subscribe fun changeFragment(event: ApplyActivityEvent) {
        val transaction = supportFragmentManager.beginTransaction()
        when (event) {
            ApplyActivityEvent.showAutonymCertifyFragment -> {
                transaction.hide(mCurrentFragment).show(mAutonymCertifyFragment)
                mCurrentFragment = mAutonymCertifyFragment
            }
            ApplyActivityEvent.showPersonalInfoFragment -> {
                transaction.hide(mCurrentFragment).show(mPersonalInfoFragment)
                mCurrentFragment = mPersonalInfoFragment
            }
            ApplyActivityEvent.showCommonRepaymentPeople -> {
                transaction.hide(mCurrentFragment).show(mSpouseInfoFragment)
                mCurrentFragment = mSpouseInfoFragment
            }
        }
        transaction.commit()
    }

    private fun initView() {
        initTitleBar(this, "填写个人资料").setLeftClickListener {
            AlertDialog.Builder(this).setMessage("您确定退出该页面返回首页?")
                    .setPositiveButton("确定退出") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }.setNegativeButton("取消退出") { dialog, _ -> dialog.dismiss() }.show()
        }
        mAutonymCertifyFragment = AutonymCertifyFragment()
        mPersonalInfoFragment = PersonalInfoFragment()
        mSpouseInfoFragment = SpouseInfoFragment()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, mAutonymCertifyFragment)
                .add(R.id.container, mPersonalInfoFragment)
                .add(R.id.container, mSpouseInfoFragment)
                .hide(mPersonalInfoFragment)
                .hide(mSpouseInfoFragment)
                .commit()
        mCurrentFragment = mAutonymCertifyFragment
    }
}

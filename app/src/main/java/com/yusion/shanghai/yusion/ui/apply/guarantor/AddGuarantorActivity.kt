package com.yusion.shanghai.yusion.ui.apply.guarantor

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.base.BaseActivity

class AddGuarantorActivity : BaseActivity() {
    private var mGuarantorInfoFragment: GuarantorInfoFragment? = null
    private var mGuarantorSpouseInfoFragment: GuarantorSpouseInfoFragment? = null
    private var mCurrentFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_guarantor)
        initView()
    }

    private fun initView() {
        initTitleBar(this, "添加担保人信息").setLeftClickListener {
            AlertDialog.Builder(this).setMessage("您确定退出该页面返回首页?")
                    .setPositiveButton("确定退出") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }.setNegativeButton("取消退出") { dialog, _ -> dialog.dismiss() }.show()
        }
        mGuarantorInfoFragment = GuarantorInfoFragment()
        mGuarantorSpouseInfoFragment = GuarantorSpouseInfoFragment()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, mGuarantorInfoFragment)
                .add(R.id.container, mGuarantorSpouseInfoFragment)
                .hide(mGuarantorSpouseInfoFragment)
                .commit()
        mCurrentFragment = mGuarantorInfoFragment
    }

    override fun onBackPressed() {
    }
}

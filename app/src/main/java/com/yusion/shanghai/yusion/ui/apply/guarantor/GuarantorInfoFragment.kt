package com.yusion.shanghai.yusion.ui.apply.guarantor

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.event.AddGuarantorActivityEvent
import kotlinx.android.synthetic.main.guarantor_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by ice on 2017/8/21.
 */
class GuarantorInfoFragment : DoubleCheckFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.guarantor_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guarantor_info_next_btn.setOnClickListener {
            EventBus.getDefault().post(AddGuarantorActivityEvent.showGuarantorSpouseInfoFragment)
        }
        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");

    }
}
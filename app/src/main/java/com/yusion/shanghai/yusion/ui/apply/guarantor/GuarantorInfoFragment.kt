package com.yusion.shanghai.yusion.ui.apply.guarantor

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.event.AddGuarantorActivityEvent
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ubt.UBT
import com.yusion.shanghai.yusion.ubt.annotate.BindView
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity
import com.yusion.shanghai.yusion.utils.InputMethodUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import com.yusion.shanghai.yusion.widget.NoEmptyEditText
import kotlinx.android.synthetic.main.guarantor_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by ice on 2017/8/21.
 */
class GuarantorInfoFragment : DoubleCheckFragment() {

    var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1
    var _GENDER_INDEX: Int = 0
    var _INCOME_FROM_INDEX: Int = 0
    var _EDUCATION_INDEX: Int = 0
    var _EXTRA_INCOME_FROME_INDEX: Int = 0
    var _FROM_INCOME_WORK_POSITION_INDEX: Int = 0
    var _FROM_SELF_TYPE_INDEX: Int = 0
    var _FROM_EXTRA_WORK_POSITION_INDEX: Int = 0
    var _HOUSE_TYPE_INDEX: Int = 0
    var _HOUSE_OWNER_RELATION_INDEX: Int = 0


    @BindView(id = R.id.guarantor_info_gender_tv, widgetName = "guarantor_info_gender_tv")
    var guarantor_info_gender_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_reg_tv, widgetName = "guarantor_info_reg_tv")
    var guarantor_info_reg_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_education_tv, widgetName = "guarantor_info_education_tv")
    var guarantor_info_education_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_current_address_tv, widgetName = "guarantor_info_current_address_tv")
    var guarantor_info_current_address_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_current_address1_tv, widgetName = "guarantor_info_current_address1_tv")
    var guarantor_info_current_address1_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_current_address2_tv, widgetName = "guarantor_info_current_address2_tv")
    var guarantor_info_current_address2_tv: NoEmptyEditText? = null

    //主要收入来源
    @BindView(id = R.id.guarantor_info_income_from_tv, widgetName = "guarantor_info_income_from_tv")
    var guarantor_info_income_from_tv: TextView? = null
    //主要工资
    @BindView(id = R.id.guarantor_info_from_income_year_edt, widgetName = "guarantor_info_from_income_year_edt")
    var guarantor_info_from_income_year_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_from_income_company_name_edt, widgetName = "guarantor_info_from_income_company_name_edt")
    var guarantor_info_from_income_company_name_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_from_income_company_address_tv, widgetName = "guarantor_info_from_income_company_address_tv")
    var guarantor_info_from_income_company_address_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_from_income_company_address1_tv, widgetName = "guarantor_info_from_income_company_address1_tv")
    var guarantor_info_from_income_company_address1_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_from_income_company_address2_tv, widgetName = "guarantor_info_from_income_company_address2_tv")
    var guarantor_info_from_income_company_address2_tv: NoEmptyEditText? = null

    @BindView(id = R.id.guarantor_info_from_income_work_position_tv, widgetName = "guarantor_info_from_income_work_position_tv")
    var guarantor_info_from_income_work_position_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_from_income_work_phone_num_edt, widgetName = "guarantor_info_from_income_work_phone_num_edt")
    var guarantor_info_from_income_work_phone_num_edt: EditText? = null

    //主要自营
    @BindView(id = R.id.guarantor_info_from_self_year_edt, widgetName = "guarantor_info_from_self_year_edt")
    var guarantor_info_from_self_year_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_from_self_type_tv, widgetName = "guarantor_info_from_self_type_tv")
    var guarantor_info_from_self_type_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_from_self_company_name_edt, widgetName = "guarantor_info_from_self_company_name_edt")
    var guarantor_info_from_self_company_name_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_from_self_company_address_tv, widgetName = "guarantor_info_from_self_company_address_tv")
    var guarantor_info_from_self_company_address_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_from_self_company_address1_tv, widgetName = "guarantor_info_from_self_company_address1_tv")
    var guarantor_info_from_self_company_address1_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_from_self_company_address2_tv, widgetName = "guarantor_info_from_self_company_address2_tv")
    var guarantor_info_from_self_company_address2_tv: NoEmptyEditText? = null

    //额外收入来源
    @BindView(id = R.id.guarantor_info_extra_income_from_tv, widgetName = "guarantor_info_extra_income_from_tv")
    var guarantor_info_extra_income_from_tv: TextView? = null
    //额外工资
    @BindView(id = R.id.guarantor_info_extra_from_income_year_edt, widgetName = "guarantor_info_extra_from_income_year_edt")
    var guarantor_info_extra_from_income_year_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_extra_from_income_company_name_edt, widgetName = "guarantor_info_extra_from_income_company_name_edt")
    var guarantor_info_extra_from_income_company_name_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_extra_from_income_company_address_tv, widgetName = "guarantor_info_extra_from_income_company_address_tv")
    var guarantor_info_extra_from_income_company_address_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_extra_from_income_company_address1_tv, widgetName = "guarantor_info_extra_from_income_company_address_tv")
    var guarantor_info_extra_from_income_company_address1_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_extra_from_income_company_address2_tv, widgetName = "guarantor_info_extra_from_income_company_address2_tv")
    var guarantor_info_extra_from_income_company_address2_tv: NoEmptyEditText? = null

    @BindView(id = R.id.guarantor_info_extra_from_income_work_position_tv, widgetName = "guarantor_info_extra_from_income_work_position_tv")
    var guarantor_info_extra_from_income_work_position_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_extra_from_income_work_phone_num_edt, widgetName = "guarantor_info_extra_from_income_work_phone_num_edt")
    var guarantor_info_extra_from_income_work_phone_num_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_house_type_tv, widgetName = "guarantor_info_house_type_tv")
    var guarantor_info_house_type_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_house_address_tv, widgetName = "guarantor_info_house_address_tv")
    var guarantor_info_house_address_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_house_address1_tv, widgetName = "guarantor_info_house_address1_tv")
    var guarantor_info_house_address1_tv: TextView? = null

    @BindView(id = R.id.guarantor_info_house_address2_tv, widgetName = "guarantor_info_house_address2_tv")
    var guarantor_info_house_address2_tv: NoEmptyEditText? = null

    @BindView(id = R.id.guarantor_info_house_owner_name_edt, widgetName = "guarantor_info_house_owner_name_edt")
    var guarantor_info_house_owner_name_edt: EditText? = null

    @BindView(id = R.id.guarantor_info_house_owner_relation_tv, widgetName = "guarantor_info_house_owner_relation_tv")
    var guarantor_info_house_owner_relation_tv: TextView? = null


    @BindView(id = R.id.guarantor_info_next_btn, widgetName = "guarantor_info_next_btn", onClick = "submitGuarantorInfo")
    var guarantor_info_next_btn: Button? = null

    fun submitGuarantorInfo(view: View?) {
        if (checkCanNextStep()) {
            clearDoubleCheckItems()
            addDoubleCheckItem("姓名", guarantor_info_clt_nm_edt.text.toString())
            addDoubleCheckItem("身份证号", guarantor_info_id_no_edt.text.toString())
            mDoubleCheckDialog.show()
        }
//        (guarantor_info_next_btn as Button).setFocusable(true)
//        (guarantor_info_next_btn as Button).setFocusableInTouchMode(true)
//        (guarantor_info_next_btn as Button).requestFocus()
//       (guarantor_info_next_btn as Button).requestFocusFromTouch()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.guarantor_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UBT.bind(this, view, AddGuarantorActivity::class.java.getSimpleName())
        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step1.setOnClickListener { EventBus.getDefault().post(AddGuarantorActivityEvent.showGuarantorCreditInfoFragment) }


        mDoubleCheckChangeBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
        }
        mDoubleCheckSubmitBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
            var addGuarantorActivity = activity as AddGuarantorActivity
            if ((guarantor_info_reg_tv as TextView).text.toString().isNotEmpty()) {
                addGuarantorActivity.mGuarantorInfo.reg_addr.province = (guarantor_info_reg_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                addGuarantorActivity.mGuarantorInfo.reg_addr.city = (guarantor_info_reg_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                addGuarantorActivity.mGuarantorInfo.reg_addr.district = (guarantor_info_reg_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
            }
            addGuarantorActivity.mGuarantorInfo.gender = (guarantor_info_gender_tv as TextView).text.toString()
            addGuarantorActivity.mGuarantorInfo.edu = (guarantor_info_education_tv as TextView).text.toString()

            //现住地址
            if ((guarantor_info_current_address_tv as TextView).text.toString().isNotEmpty()) {
                addGuarantorActivity.mGuarantorInfo.current_addr.province = (guarantor_info_current_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                addGuarantorActivity.mGuarantorInfo.current_addr.city = (guarantor_info_current_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                addGuarantorActivity.mGuarantorInfo.current_addr.district = (guarantor_info_current_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
            }
            addGuarantorActivity.mGuarantorInfo.current_addr.address1 = (guarantor_info_current_address1_tv as TextView).text.toString()
            addGuarantorActivity.mGuarantorInfo.current_addr.address2 = (guarantor_info_current_address2_tv as TextView).text.toString()

            //主要收入来源
            when ((guarantor_info_income_from_tv as TextView).text.toString()) {
                "工资" -> {
                    addGuarantorActivity.mGuarantorInfo.major_income_type = "工资"
                    addGuarantorActivity.mGuarantorInfo.major_income = (guarantor_info_from_income_year_edt as EditText).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_company_name = (guarantor_info_from_income_company_name_edt as EditText).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_company_addr.province = (guarantor_info_from_income_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                    addGuarantorActivity.mGuarantorInfo.major_company_addr.city = (guarantor_info_from_income_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                    addGuarantorActivity.mGuarantorInfo.major_company_addr.district = (guarantor_info_from_income_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                    addGuarantorActivity.mGuarantorInfo.major_company_addr.address1 = (guarantor_info_from_income_company_address1_tv as TextView).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_company_addr.address2 = (guarantor_info_from_income_company_address2_tv as NoEmptyEditText).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_work_position = (guarantor_info_from_income_work_position_tv as TextView).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_work_phone_num = (guarantor_info_from_income_work_phone_num_edt as EditText).text.toString()
                }
                "自营" -> {
                    addGuarantorActivity.mGuarantorInfo.major_income_type = "自营"
                    addGuarantorActivity.mGuarantorInfo.major_income = (guarantor_info_from_self_year_edt as EditText).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_busi_type = (guarantor_info_from_self_type_tv as TextView).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_company_name = (guarantor_info_from_self_company_name_edt as EditText).text.toString()
                    if (TextUtils.isEmpty((guarantor_info_from_self_company_address_tv as TextView).text)) {
                        addGuarantorActivity.mGuarantorInfo.major_company_addr.province = ""
                        addGuarantorActivity.mGuarantorInfo.major_company_addr.city = ""
                        addGuarantorActivity.mGuarantorInfo.major_company_addr.district = ""
                    } else {
                        addGuarantorActivity.mGuarantorInfo.major_company_addr.province = (guarantor_info_from_self_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        addGuarantorActivity.mGuarantorInfo.major_company_addr.city = (guarantor_info_from_self_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                        addGuarantorActivity.mGuarantorInfo.major_company_addr.district = (guarantor_info_from_self_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                    }
                    addGuarantorActivity.mGuarantorInfo.major_company_addr.address1 = (guarantor_info_from_self_company_address1_tv as TextView).text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_company_addr.address2 = (guarantor_info_from_self_company_address2_tv as NoEmptyEditText).text.toString()
                }
                "其他" -> {
                    addGuarantorActivity.mGuarantorInfo.major_income_type = "其他"
                    addGuarantorActivity.mGuarantorInfo.major_income = guarantor_info_from_other_year_edt.text.toString()
                    addGuarantorActivity.mGuarantorInfo.major_remark = guarantor_info_from_other_remark_edt.text.toString()
                }
            }
            //额外收入来源
            when ((guarantor_info_extra_income_from_tv as TextView).text.toString()) {
                "工资" -> {
                    addGuarantorActivity.mGuarantorInfo.extra_income_type = "工资"
                    addGuarantorActivity.mGuarantorInfo.extra_income = (guarantor_info_extra_from_income_year_edt as EditText).text.toString()
                    addGuarantorActivity.mGuarantorInfo.extra_company_name = (guarantor_info_extra_from_income_company_name_edt as EditText).text.toString()
                    addGuarantorActivity.mGuarantorInfo.extra_company_addr.province = (guarantor_info_extra_from_income_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                    addGuarantorActivity.mGuarantorInfo.extra_company_addr.city = (guarantor_info_extra_from_income_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                    addGuarantorActivity.mGuarantorInfo.extra_company_addr.district = (guarantor_info_extra_from_income_company_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                    addGuarantorActivity.mGuarantorInfo.extra_company_addr.address1 = (guarantor_info_extra_from_income_company_address1_tv as TextView).text.toString()
                    addGuarantorActivity.mGuarantorInfo.extra_company_addr.address2 = (guarantor_info_extra_from_income_company_address2_tv as NoEmptyEditText).text.toString()
                    addGuarantorActivity.mGuarantorInfo.extra_work_position = (guarantor_info_extra_from_income_work_position_tv as TextView).text.toString()
                    addGuarantorActivity.mGuarantorInfo.extra_work_phone_num = (guarantor_info_extra_from_income_work_phone_num_edt as EditText).text.toString()
                }
                "无" -> {
                    addGuarantorActivity.mGuarantorInfo.extra_income_type = "无"
                }
            }

            //房屋性质
            addGuarantorActivity.mGuarantorInfo.house_owner_name = (guarantor_info_house_owner_name_edt as TextView).text.toString()
            //现住地址
            if ((guarantor_info_house_address_tv as TextView).text.isNotEmpty()) {
                addGuarantorActivity.mGuarantorInfo.house_addr.province = (guarantor_info_house_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                addGuarantorActivity.mGuarantorInfo.house_addr.city = (guarantor_info_house_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                addGuarantorActivity.mGuarantorInfo.house_addr.district = (guarantor_info_house_address_tv as TextView).text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
            }
            addGuarantorActivity.mGuarantorInfo.house_addr.address1 = (guarantor_info_house_address1_tv as TextView).text.toString()
            addGuarantorActivity.mGuarantorInfo.house_addr.address2 = (guarantor_info_house_address2_tv as NoEmptyEditText).text.toString()
            addGuarantorActivity.mGuarantorInfo.house_owner_relation = (guarantor_info_house_owner_relation_tv as TextView).text.toString()
            addGuarantorActivity.mGuarantorInfo.house_type = (guarantor_info_house_type_tv as TextView).text.toString()
            Log.e("houseeee","1----"+addGuarantorActivity.mGuarantorInfo.house_addr.province)
            Log.e("houseeee","1----"+addGuarantorActivity.mGuarantorInfo.house_addr.city)
            Log.e("houseeee","1----"+addGuarantorActivity.mGuarantorInfo.house_addr.district)
            Log.e("houseeee","1----"+addGuarantorActivity.mGuarantorInfo.house_addr.address1)
            Log.e("houseeee","1----"+addGuarantorActivity.mGuarantorInfo.house_addr.address2)

            nextStep()
        }
//
//        (guarantor_info_next_btn as Button).setOnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                (guarantor_info_next_btn as Button).clearFocus();
//                if (checkCanNextStep()) {
//                    clearDoubleCheckItems()
//                    addDoubleCheckItem("姓名", guarantor_info_clt_nm_edt.text.toString())
//                    addDoubleCheckItem("身份证号", guarantor_info_id_no_edt.text.toString())
//                    mDoubleCheckDialog.show()
//                }
//            }
//        }
        guarantor_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>((activity.application as YusionApp).configResp.gender_list_key, _GENDER_INDEX, guarantor_info_gender_lin, guarantor_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
            })
        }
        guarantor_info_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "自营"), _INCOME_FROM_INDEX, guarantor_info_income_from_lin, guarantor_info_income_from_tv, "请选择", { _, index ->
                _INCOME_FROM_INDEX = index
                guarantor_info_from_income_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROM_INDEX] == "工资") View.VISIBLE else View.GONE
                guarantor_info_from_self_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROM_INDEX] == "自营") View.VISIBLE else View.GONE
//                guarantor_info_from_other_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROM_INDEX] == "其他") View.VISIBLE else View.GONE
            })
        }
        guarantor_info_extra_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "无"), _EXTRA_INCOME_FROME_INDEX, guarantor_info_extra_income_from_lin, guarantor_info_extra_income_from_tv, "请选择", { _, index ->
                _EXTRA_INCOME_FROME_INDEX = index
                guarantor_info_extra_from_income_group_lin.visibility = if (listOf("工资", "无")[_EXTRA_INCOME_FROME_INDEX] == "工资") View.VISIBLE else View.GONE
            })
        }
        guarantor_info_reg_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_info_reg_lin, guarantor_info_reg_tv, "请选择所在地区") { _, _ -> }
        }
        guarantor_info_education_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>((activity.application as YusionApp).configResp.education_list_key, _EDUCATION_INDEX, guarantor_info_education_lin, guarantor_info_education_tv, "请选择", { _, index ->
                _EDUCATION_INDEX = index
            })
        }
        guarantor_info_current_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_info_current_address_lin, guarantor_info_current_address_tv, "请选择所在地区") { _, _ -> (guarantor_info_current_address1_tv as TextView).text = "" }
        }
        guarantor_info_current_address1_lin.setOnClickListener {
            if ((guarantor_info_current_address_tv as TextView).text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_info_current_address1_lin.id
                requestPOI((guarantor_info_current_address_tv as TextView).text.toString())
            }
        }

        //工资
        guarantor_info_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_info_from_income_company_address_lin, (guarantor_info_from_income_company_address_tv as TextView), "请选择所在地区") { _, _ -> (guarantor_info_from_income_company_address1_tv as TextView).text = "" }
        }
        guarantor_info_from_income_company_address1_lin.setOnClickListener {
            if ((guarantor_info_from_income_company_address_tv as TextView).text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_info_from_income_company_address1_lin.id
                requestPOI((guarantor_info_from_income_company_address_tv as TextView).text.toString())
            }
        }
        guarantor_info_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>((activity.application as YusionApp).configResp.work_position_key, _FROM_INCOME_WORK_POSITION_INDEX, guarantor_info_from_income_work_position_lin, guarantor_info_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_INCOME_WORK_POSITION_INDEX = index
            })
        }

        //自营
        guarantor_info_from_self_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_info_from_self_company_address_lin, guarantor_info_from_self_company_address_tv, "请选择所在地区") { _, _ -> (guarantor_info_from_self_company_address1_tv as TextView).text = "" }
        }
        guarantor_info_from_self_type_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.busi_type_list_key, _FROM_SELF_TYPE_INDEX, guarantor_info_from_self_type_lin, guarantor_info_from_self_type_tv, "请选择", { _, index ->
                _FROM_SELF_TYPE_INDEX = index
                if (YusionApp.CONFIG_RESP.busi_type_list_value[_FROM_SELF_TYPE_INDEX] == "其他") {
                    val editText = EditText(mContext)
                    AlertDialog.Builder(mContext)
                            .setTitle("请输入业务类型")
                            .setView(editText)
                            .setCancelable(false)
                            .setPositiveButton("确定") { dialog, which ->
                                (guarantor_info_from_self_type_tv as TextView).text = editText.text
                                _FROM_SELF_TYPE_INDEX = 0
                                InputMethodUtil.hideInputMethod(mContext)
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消") { dialog, which ->
                                dialog.dismiss()
                                InputMethodUtil.hideInputMethod(mContext)
                                _FROM_SELF_TYPE_INDEX = 0;
                                (guarantor_info_from_self_type_tv as TextView).text = null
                            }.show()
                }
            })
        }
        guarantor_info_from_self_company_address1_lin.setOnClickListener {
            if ((guarantor_info_from_self_company_address_tv as TextView).text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_info_from_self_company_address1_lin.id
                requestPOI((guarantor_info_from_self_company_address_tv as TextView).text.toString())
            }
        }

        //额外工资
        guarantor_info_extra_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_info_extra_from_income_company_address_lin, guarantor_info_extra_from_income_company_address_tv, "请选择所在地区") { _, _ -> (guarantor_info_extra_from_income_company_address1_tv as TextView).text = "" }
        }
        guarantor_info_extra_from_income_company_address1_lin.setOnClickListener {
            if ((guarantor_info_extra_from_income_company_address_tv as TextView).text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_info_extra_from_income_company_address1_lin.id
                requestPOI((guarantor_info_extra_from_income_company_address_tv as TextView).text.toString())
            }
        }
        guarantor_info_extra_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _FROM_EXTRA_WORK_POSITION_INDEX, guarantor_info_extra_from_income_work_position_lin, guarantor_info_extra_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_EXTRA_WORK_POSITION_INDEX = index
            })
        }

        //房屋地址
        guarantor_info_house_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_info_house_address_lin, guarantor_info_house_address_tv, "请选择所在地区") { _, _ -> (guarantor_info_house_address1_tv as TextView).text = "" }
        }
        guarantor_info_house_address1_lin.setOnClickListener {
            if ((guarantor_info_house_address_tv as TextView).text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_info_house_address1_lin.id
                requestPOI((guarantor_info_house_address_tv as TextView).text.toString())
            }
        }
        guarantor_info_house_type_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.house_type_list_key, _HOUSE_TYPE_INDEX, guarantor_info_house_type_lin, guarantor_info_house_type_tv, "请选择", { _, index ->
                _HOUSE_TYPE_INDEX = index
            })
        }
        guarantor_info_house_owner_relation_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.house_relationship_list_key, _HOUSE_OWNER_RELATION_INDEX, guarantor_info_house_owner_relation_lin, guarantor_info_house_owner_relation_tv, "请选择", { _, index ->
                _HOUSE_OWNER_RELATION_INDEX = index
            })
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            var guarantorInfo = (activity as AddGuarantorActivity).mGuarantorInfo
            guarantor_info_clt_nm_edt.text = guarantorInfo.clt_nm
            guarantor_info_id_no_edt.text = guarantorInfo.id_no
            if (guarantorInfo.gender.isNotEmpty()) {
                (guarantor_info_gender_tv as TextView).text = guarantorInfo.gender
            }
            if (guarantorInfo.reg_addr.province.isNotEmpty() && guarantorInfo.reg_addr.city.isNotEmpty() && guarantorInfo.reg_addr.district.isNotEmpty()) {
                (guarantor_info_reg_tv as TextView).text = guarantorInfo.reg_addr.province + "/" + guarantorInfo.reg_addr.city + "/" + guarantorInfo.reg_addr.district
            }
        }
    }

    fun requestPOI(city: String = "上海市/上海市/浦东新区") {
        val intent = Intent(mContext, AMapPoiListActivity::class.java)
        val addressArray = city.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        intent.putExtra("city", addressArray[1].substring(0, addressArray[1].length - 1))
        intent.putExtra("keywords", addressArray[2].substring(0, addressArray[2].length - 1))
        startActivityForResult(intent, Constants.REQUEST_ADDRESS)
    }

    fun checkCanNextStep(): Boolean {
//        return true
        if ((guarantor_info_reg_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "户籍地不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_gender_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_education_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "学历不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_current_address_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_current_address1_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_current_address2_tv as NoEmptyEditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "主要收入来源不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_from_income_year_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_from_income_company_name_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_from_income_company_address_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_from_income_company_address1_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_from_income_company_address2_tv as NoEmptyEditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_from_income_work_position_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "自营" && (guarantor_info_from_self_year_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "自营" && (guarantor_info_from_self_type_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "业务类型不能为空", Toast.LENGTH_SHORT).show()
        }
//        else if (guarantor_info_income_from_tv as TextView).text.toString() == "自营" && guarantor_info_from_self_company_name_edt as EditText).text.toString().isEmpty()) {
//            Toast.makeText(mContext, "店铺名称不能为空", Toast.LENGTH_SHORT).show()
//        }
        else if ((guarantor_info_income_from_tv as TextView).text.toString() == "自营" && (guarantor_info_from_self_company_address_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "项目经营地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "自营" && (guarantor_info_from_self_company_address1_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "自营的详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "自营" && (guarantor_info_from_self_company_address2_tv as NoEmptyEditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "自营的门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "其他" && (guarantor_info_from_other_year_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_income_from_tv as TextView).text.toString() == "其他" && (guarantor_info_from_other_remark_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "备注不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_extra_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_extra_from_income_year_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_extra_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_extra_from_income_company_name_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_extra_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_extra_from_income_company_address_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_extra_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_extra_from_income_company_address1_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_extra_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_extra_from_income_company_address2_tv as NoEmptyEditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_extra_income_from_tv as TextView).text.toString() == "工资" && (guarantor_info_extra_from_income_work_position_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_house_type_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "房屋性质不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_house_address_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "房屋地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_house_address1_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "房屋地址的详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_house_address2_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "房屋地址的门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_house_owner_name_edt as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show()
        } else if ((guarantor_info_house_owner_relation_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    fun nextStep() {
        EventBus.getDefault().post(AddGuarantorActivityEvent.showGuarantorSpouseInfoFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_ADDRESS) {
                when (CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    guarantor_info_current_address1_lin.id -> {
                        (guarantor_info_current_address1_tv as TextView).text = data.getStringExtra("result");
                    }
                    guarantor_info_from_income_company_address1_lin.id -> {
                        (guarantor_info_from_income_company_address1_tv as TextView).text = data.getStringExtra("result");
                    }
                    guarantor_info_from_self_company_address1_lin.id -> {
                        (guarantor_info_from_self_company_address1_tv as TextView).text = data.getStringExtra("result");
                    }
                    guarantor_info_extra_from_income_company_address1_lin.id -> {
                        (guarantor_info_extra_from_income_company_address1_tv as TextView).text = data.getStringExtra("result");
                    }
                    guarantor_info_house_address1_lin.id -> {
                        (guarantor_info_house_address1_tv as TextView).text = data.getStringExtra("result");
                    }
                }
            }
        }
    }
}
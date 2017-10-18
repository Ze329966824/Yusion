package com.yusion.shanghai.yusion.ui.apply

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
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
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ubt.UBT
import com.yusion.shanghai.yusion.ubt.annotate.BindView
import com.yusion.shanghai.yusion.utils.CheckMobileUtil
import com.yusion.shanghai.yusion.utils.ContactsUtil
import com.yusion.shanghai.yusion.utils.InputMethodUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import com.yusion.shanghai.yusion.widget.NoEmptyEditText
import kotlinx.android.synthetic.main.personal_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by ice on 17/7/5.
 */
class PersonalInfoFragment : DoubleCheckFragment() {

    var CURRENT_CLICKED_VIEW_FOR_CONTACT: Int = -1
    var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1
    var _GENDER_INDEX: Int = 0
    var _INCOME_FROM_INDEX: Int = 0
    var _EDUCATION_INDEX: Int = 0
    var _EXTRA_INCOME_FROM_INDEX: Int = 0
    var _LIVE_WITH_PARENT_INDEX: Int = 0
    var _FROM_INCOME_WORK_POSITION_INDEX: Int = 0
    var _FROM_SELF_TYPE_INDEX: Int = 0
    var _FROM_EXTRA_WORK_POSITION_INDEX: Int = 0
    var _HOUSE_TYPE_INDEX: Int = 0
    var _HOUSE_OWNER_RELATION_INDEX: Int = 0
    var _URG_RELATION_INDEX1: Int = 0
    var _URG_RELATION_INDEX2: Int = 0

    @BindView(id = R.id.personal_info_gender_tv, widgetName = "personal_info_gender_tv")
    var personal_info_gender_tv: TextView? = null

    @BindView(id = R.id.personal_info_reg_tv, widgetName = "personal_info_reg_tv")
    var personal_info_reg_tv: TextView? = null

    @BindView(id = R.id.personal_info_mobile_edt, widgetName = "personal_info_mobile_edt")
    var personal_info_mobile_edt: EditText? = null

    @BindView(id = R.id.personal_info_education_tv, widgetName = "personal_info_education_tv")
    var personal_info_education_tv: TextView? = null

    @BindView(id = R.id.personal_info_current_address_tv, widgetName = "personal_info_current_address_tv")
    var personal_info_current_address_tv: TextView? = null

    @BindView(id = R.id.personal_info_current_address1_tv, widgetName = "personal_info_current_address1_tv")
    var personal_info_current_address1_tv: TextView? = null

    @BindView(id = R.id.personal_info_current_address2_tv, widgetName = "personal_info_current_address2_tv")
    var personal_info_current_address2_tv: NoEmptyEditText? = null

    @BindView(id = R.id.personal_info_live_with_parent_tv, widgetName = "personal_info_live_with_parent_tv")
    var personal_info_live_with_parent_tv: TextView? = null

    //主要
    @BindView(id = R.id.personal_info_income_from_tv, widgetName = "personal_info_income_from_tv")
    var personal_info_income_from_tv: TextView? = null

    //主要工资
    @BindView(id = R.id.personal_info_from_income_year_edt, widgetName = "personal_info_from_income_year_edt")
    var personal_info_from_income_year_edt: EditText? = null

    @BindView(id = R.id.personal_info_from_income_company_name_edt, widgetName = "personal_info_from_income_company_name_edt")
    var personal_info_from_income_company_name_edt: EditText? = null

    @BindView(id = R.id.personal_info_from_income_company_address_tv, widgetName = "personal_info_from_income_company_address_tv")
    var personal_info_from_income_company_address_tv: TextView? = null

    @BindView(id = R.id.personal_info_from_income_company_address1_tv, widgetName = "personal_info_from_income_company_address1_tv")
    var personal_info_from_income_company_address1_tv: TextView? = null

    @BindView(id = R.id.personal_info_from_income_company_address2_tv, widgetName = "personal_info_from_income_company_address2_tv")
    var personal_info_from_income_company_address2_tv: NoEmptyEditText? = null

    @BindView(id = R.id.personal_info_from_income_work_position_tv, widgetName = "personal_info_from_income_work_position_tv")
    var personal_info_from_income_work_position_tv: TextView? = null

    @BindView(id = R.id.personal_info_from_income_work_phone_num_edt, widgetName = "personal_info_from_income_work_phone_num_edt")
    var personal_info_from_income_work_phone_num_edt: EditText? = null

    //主要自营
    @BindView(id = R.id.personal_info_from_self_year_edt, widgetName = "personal_info_from_self_year_edt")
    var personal_info_from_self_year_edt: EditText? = null

    @BindView(id = R.id.personal_info_from_self_type_tv, widgetName = "personal_info_from_self_type_tv")
    var personal_info_from_self_type_tv: TextView? = null

    @BindView(id = R.id.personal_info_from_self_company_name_edt, widgetName = "personal_info_from_self_company_name_edt")
    var personal_info_from_self_company_name_edt: EditText? = null

    @BindView(id = R.id.personal_info_from_self_company_address_tv, widgetName = "personal_info_from_self_company_address_tv")
    var personal_info_from_self_company_address_tv: TextView? = null

    @BindView(id = R.id.personal_info_from_self_company_address1_tv, widgetName = "personal_info_from_self_company_address1_tv")
    var personal_info_from_self_company_address1_tv: TextView? = null

    @BindView(id = R.id.personal_info_from_self_company_address2_tv, widgetName = "personal_info_from_self_company_address2_tv")
    var personal_info_from_self_company_address2_tv: NoEmptyEditText? = null


    //额外工资
    @BindView(id = R.id.personal_info_extra_income_from_tv, widgetName = "personal_info_extra_income_from_tv")
    var personal_info_extra_income_from_tv: TextView? = null

    @BindView(id = R.id.personal_info_extra_from_income_year_edt, widgetName = "personal_info_extra_from_income_year_edt")
    var personal_info_extra_from_income_year_edt: EditText? = null

    @BindView(id = R.id.personal_info_extra_from_income_company_name_edt, widgetName = "personal_info_extra_from_income_company_name_edt")
    var personal_info_extra_from_income_company_name_edt: EditText? = null

    @BindView(id = R.id.personal_info_extra_from_income_company_address_tv, widgetName = "personal_info_extra_from_income_company_address_tv")
    var personal_info_extra_from_income_company_address_tv: TextView? = null

    @BindView(id = R.id.personal_info_extra_from_income_company_address1_tv, widgetName = "personal_info_extra_from_income_company_address1_tv")
    var personal_info_extra_from_income_company_address1_tv: TextView? = null

    @BindView(id = R.id.personal_info_extra_from_income_company_address2_tv, widgetName = "personal_info_extra_from_income_company_address2_tv")
    var personal_info_extra_from_income_company_address2_tv: NoEmptyEditText? = null

    @BindView(id = R.id.personal_info_extra_from_income_work_position_tv, widgetName = "personal_info_extra_from_income_work_position_tv")
    var personal_info_extra_from_income_work_position_tv: TextView? = null

    @BindView(id = R.id.personal_info_extra_from_income_work_phone_num_edt, widgetName = "personal_info_extra_from_income_work_phone_num_edt")
    var personal_info_extra_from_income_work_phone_num_edt: EditText? = null


    @BindView(id = R.id.personal_info_house_type_tv, widgetName = "personal_info_house_type_tv")
    var personal_info_house_type_tv: TextView? = null

    @BindView(id = R.id.personal_info_house_area_edt, widgetName = "personal_info_house_area_edt")
    var personal_info_house_area_edt: EditText? = null

    @BindView(id = R.id.personal_info_house_owner_name_edt, widgetName = "personal_info_house_owner_name_edt")
    var personal_info_house_owner_name_edt: EditText? = null

    @BindView(id = R.id.personal_info_house_owner_relation_tv, widgetName = "personal_info_house_owner_relation_tv")
    var personal_info_house_owner_relation_tv: TextView? = null

    @BindView(id = R.id.personal_info_urg_relation1_tv, widgetName = "personal_info_urg_relation1_tv")
    var personal_info_urg_relation1_tv: TextView? = null

    @BindView(id = R.id.personal_info_urg_mobile1_edt, widgetName = "personal_info_urg_mobile1_edt")
    var personal_info_urg_mobile1_edt: EditText? = null

    @BindView(id = R.id.personal_info_urg_contact1_edt, widgetName = "personal_info_urg_contact1_edt")
    var personal_info_urg_contact1_edt: EditText? = null

    @BindView(id = R.id.personal_info_urg_relation2_tv, widgetName = "personal_info_urg_relation2_tv")
    var personal_info_urg_relation2_tv: TextView? = null

    @BindView(id = R.id.personal_info_urg_mobile2_edt, widgetName = "personal_info_urg_mobile2_edt")
    var personal_info_urg_mobile2_edt: EditText? = null

    @BindView(id = R.id.personal_info_urg_contact2_edt, widgetName = "personal_info_urg_contact2_edt")
    var personal_info_urg_contact2_edt: EditText? = null

    @BindView(id = R.id.personal_info_next_btn, widgetName = "personal_info_next_btn", onClick = "submitPersonalInfo")
    var personal_info_next_btn: Button? = null

    fun submitPersonalInfo(view: View?) {
        personal_info_next_btn?.setFocusable(true)
        personal_info_next_btn?.setFocusableInTouchMode(true)
        personal_info_next_btn?.requestFocus()
        personal_info_next_btn?.requestFocusFromTouch()
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.personal_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UBT.bind(this, view, ApplyActivity::class.java.getSimpleName())
        (personal_info_next_btn as Button).setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (personal_info_next_btn as Button).clearFocus();
                if (checkCanNextStep()) {
                    clearDoubleCheckItems()
                    addDoubleCheckItem("姓名", personal_info_clt_nm_edt.text.toString())
                    addDoubleCheckItem("身份证号", personal_info_id_no_edt.text.toString())
                    addDoubleCheckItem("手机号", personal_info_mobile_edt?.text.toString())
                    mDoubleCheckDialog.show()
                }
            }
        }

        mDoubleCheckChangeBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
        }
        mDoubleCheckSubmitBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
            var applyActivity = activity as ApplyActivity
            if (personal_info_reg_tv?.text?.isNotEmpty() as Boolean) {
                applyActivity.mClientInfo.reg_addr.province = personal_info_reg_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                applyActivity.mClientInfo.reg_addr.city = personal_info_reg_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                applyActivity.mClientInfo.reg_addr.district = personal_info_reg_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
            }
            applyActivity.mClientInfo.gender = personal_info_gender_tv?.text.toString()
            applyActivity.mClientInfo.mobile = personal_info_mobile_edt?.text.toString()
            applyActivity.mClientInfo.edu = personal_info_education_tv?.text.toString()

            //现住地址
            if (personal_info_current_address_tv?.text?.isNotEmpty() as Boolean) {
                applyActivity.mClientInfo.current_addr.province = personal_info_current_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                applyActivity.mClientInfo.current_addr.city = personal_info_current_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                applyActivity.mClientInfo.current_addr.district = personal_info_current_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
            }
            applyActivity.mClientInfo.current_addr.address1 = personal_info_current_address1_tv?.text.toString()
            applyActivity.mClientInfo.current_addr.address2 = personal_info_current_address2_tv?.text.toString()
            applyActivity.mClientInfo.is_live_with_parent = personal_info_live_with_parent_tv?.text.toString()


            //主要收入来源
            when (personal_info_income_from_tv?.text) {
                "工资" -> {
                    applyActivity.mClientInfo.major_income_type = "工资"
                    applyActivity.mClientInfo.major_income = personal_info_from_income_year_edt?.text.toString()
                    applyActivity.mClientInfo.major_company_name = personal_info_from_income_company_name_edt?.text.toString()
                    applyActivity.mClientInfo.major_company_addr.province = personal_info_from_income_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                    applyActivity.mClientInfo.major_company_addr.city = personal_info_from_income_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                    applyActivity.mClientInfo.major_company_addr.district = personal_info_from_income_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                    applyActivity.mClientInfo.major_company_addr.address1 = personal_info_from_income_company_address1_tv?.text.toString()
                    applyActivity.mClientInfo.major_company_addr.address2 = personal_info_from_income_company_address2_tv?.text.toString()
                    applyActivity.mClientInfo.major_work_position = personal_info_from_income_work_position_tv?.text.toString()
                    applyActivity.mClientInfo.major_work_phone_num = personal_info_from_income_work_phone_num_edt?.text.toString()
                }
                "自营" -> {
                    applyActivity.mClientInfo.major_income_type = "自营"
                    applyActivity.mClientInfo.major_income = personal_info_from_self_year_edt?.text.toString()
                    applyActivity.mClientInfo.major_busi_type = personal_info_from_self_type_tv?.text.toString()
                    applyActivity.mClientInfo.major_company_name = personal_info_from_self_company_name_edt?.text.toString()
                    if (TextUtils.isEmpty(personal_info_from_self_company_address_tv?.text)) {
                        applyActivity.mClientInfo.major_company_addr.province = ""
                        applyActivity.mClientInfo.major_company_addr.city = ""
                        applyActivity.mClientInfo.major_company_addr.district = ""
                    } else {
                        applyActivity.mClientInfo.major_company_addr.province = personal_info_from_self_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        applyActivity.mClientInfo.major_company_addr.city = personal_info_from_self_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                        applyActivity.mClientInfo.major_company_addr.district = personal_info_from_self_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                    }
                    applyActivity.mClientInfo.major_company_addr.address1 = personal_info_from_self_company_address1_tv?.text.toString()
                    applyActivity.mClientInfo.major_company_addr.address2 = personal_info_from_self_company_address2_tv?.text.toString()
                }
                "其他" -> {
                    applyActivity.mClientInfo.major_income_type = "其他"
                    applyActivity.mClientInfo.major_income = personal_info_from_other_year_edt.text.toString()
                    applyActivity.mClientInfo.major_remark = personal_info_from_other_remark_edt.text.toString()
                }
            }
            //额外收入来源
            when (personal_info_extra_income_from_tv?.text) {
                "工资" -> {
                    applyActivity.mClientInfo.extra_income_type = "工资"
                    applyActivity.mClientInfo.extra_income = personal_info_extra_from_income_year_edt?.text.toString()
                    applyActivity.mClientInfo.extra_company_name = personal_info_extra_from_income_company_name_edt?.text.toString()
                    applyActivity.mClientInfo.extra_company_addr.province = personal_info_extra_from_income_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                    applyActivity.mClientInfo.extra_company_addr.city = personal_info_extra_from_income_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                    applyActivity.mClientInfo.extra_company_addr.district = personal_info_extra_from_income_company_address_tv?.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                    applyActivity.mClientInfo.extra_company_addr.address1 = personal_info_extra_from_income_company_address1_tv?.text.toString()
                    applyActivity.mClientInfo.extra_company_addr.address2 = personal_info_extra_from_income_company_address2_tv?.text.toString()
                    applyActivity.mClientInfo.extra_work_position = personal_info_extra_from_income_work_position_tv?.text.toString()
                    applyActivity.mClientInfo.extra_work_phone_num = personal_info_extra_from_income_work_phone_num_edt?.text.toString()
                }
                "无" -> {
                    applyActivity.mClientInfo.extra_income_type = "无"
                }
            }

            //房屋性质
            applyActivity.mClientInfo.house_owner_name = personal_info_house_owner_name_edt?.text.toString()
            applyActivity.mClientInfo.house_owner_relation = personal_info_house_owner_relation_tv?.text.toString()
            applyActivity.mClientInfo.house_type = personal_info_house_type_tv?.text.toString()
            applyActivity.mClientInfo.house_area = personal_info_house_area_edt?.text.toString()
            applyActivity.mClientInfo.urg_contact1 = personal_info_urg_contact1_edt?.text.toString()
            applyActivity.mClientInfo.urg_mobile1 = personal_info_urg_mobile1_edt?.text.toString()
            applyActivity.mClientInfo.urg_relation1 = personal_info_urg_relation1_tv?.text.toString()
            applyActivity.mClientInfo.urg_contact2 = personal_info_urg_contact2_edt?.text.toString()
            applyActivity.mClientInfo.urg_mobile2 = personal_info_urg_mobile2_edt?.text.toString()
            applyActivity.mClientInfo.urg_relation2 = personal_info_urg_relation2_tv?.text.toString()
            nextStep()
        }

        personal_info_mobile_edt?.setText(YusionApp.MOBILE)
        personal_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, _GENDER_INDEX, personal_info_gender_lin, personal_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
            })
        }
        personal_info_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "自营"), _INCOME_FROM_INDEX, personal_info_income_from_lin, personal_info_income_from_tv, "请选择", { _, index ->
                _INCOME_FROM_INDEX = index
                personal_info_from_income_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROM_INDEX] == "工资") View.VISIBLE else View.GONE
                personal_info_from_self_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROM_INDEX] == "自营") View.VISIBLE else View.GONE
//                personal_info_from_other_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROM_INDEX] == "其他") View.VISIBLE else View.GONE
            })
        }
        personal_info_extra_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "无"), _EXTRA_INCOME_FROM_INDEX, personal_info_extra_income_from_lin, personal_info_extra_income_from_tv, "请选择", { _, index ->
                _EXTRA_INCOME_FROM_INDEX = index
                personal_info_extra_from_income_group_lin.visibility = if (listOf("工资", "无")[_EXTRA_INCOME_FROM_INDEX] == "工资") View.VISIBLE else View.GONE
            })
        }

        //是否与父母同住
        personal_info_live_with_parent_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("是", "否"), _LIVE_WITH_PARENT_INDEX, personal_info_live_with_parent_lin, personal_info_live_with_parent_tv, "请选择", { _, index ->
                _LIVE_WITH_PARENT_INDEX = index
            })
        }

        personal_info_reg_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, personal_info_reg_lin, personal_info_reg_tv, "请选择所在地区") { _, _ -> }
        }
        personal_info_education_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.education_list_key, _EDUCATION_INDEX, personal_info_education_lin, personal_info_education_tv, "请选择", { _, index ->
                _EDUCATION_INDEX = index
            })
        }
        personal_info_current_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, personal_info_current_address_lin, personal_info_current_address_tv, "请选择所在地区") { _, _ -> personal_info_current_address1_tv?.text = "" }
        }
        personal_info_current_address1_lin.setOnClickListener {
            if (personal_info_current_address_tv?.text?.isNotEmpty() as Boolean) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_current_address1_lin.id
                requestPOI(personal_info_current_address_tv?.text.toString())
            }
        }

        //工资
        personal_info_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, personal_info_from_income_company_address_lin, personal_info_from_income_company_address_tv, "请选择所在地区") { _, _ -> personal_info_from_income_company_address1_tv?.text = "" }
        }
        personal_info_from_income_company_address1_lin.setOnClickListener {
            if (personal_info_from_income_company_address_tv?.text?.isNotEmpty() as Boolean) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_from_income_company_address1_lin.id
                requestPOI(personal_info_from_income_company_address_tv?.text.toString())
            }
        }
        personal_info_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _FROM_INCOME_WORK_POSITION_INDEX, personal_info_from_income_work_position_lin, personal_info_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_INCOME_WORK_POSITION_INDEX = index
            })
        }

        //自营
        personal_info_from_self_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, personal_info_from_self_company_address_lin, personal_info_from_self_company_address_tv, "请选择所在地区") { _, _ -> personal_info_from_self_company_address1_tv?.text = "" }
        }
        personal_info_from_self_type_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.busi_type_list_key, _FROM_SELF_TYPE_INDEX, personal_info_from_self_type_lin, personal_info_from_self_type_tv, "请选择", { _, index ->
                _FROM_SELF_TYPE_INDEX = index
                if (YusionApp.CONFIG_RESP.busi_type_list_value[_FROM_SELF_TYPE_INDEX] == "其他") {
                    val editText = EditText(mContext)
                    AlertDialog.Builder(mContext)
                            .setTitle("请输入业务类型")
                            .setView(editText)
                            .setCancelable(false)
                            .setPositiveButton("确定") { dialog, _ ->
                                personal_info_from_self_type_tv?.text = editText.text
                                _FROM_SELF_TYPE_INDEX = 0
                                InputMethodUtil.hideInputMethod(mContext)
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消") { dialog, _ ->
                                dialog.dismiss()
                                _FROM_SELF_TYPE_INDEX = 0;
                                personal_info_from_self_type_tv?.text = null
                                InputMethodUtil.hideInputMethod(mContext)
                            }.show()
                }
            })
        }
        personal_info_from_self_company_address1_lin.setOnClickListener {
            if (personal_info_from_self_company_address_tv?.text?.isNotEmpty() as Boolean) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_from_self_company_address1_lin.id
                requestPOI(personal_info_from_self_company_address_tv?.text.toString())
            }
        }

        //额外工资
        personal_info_extra_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, personal_info_extra_from_income_company_address_lin, personal_info_extra_from_income_company_address_tv, "请选择所在地区") { _, _ -> personal_info_extra_from_income_company_address1_tv?.text = "" }
        }
        personal_info_extra_from_income_company_address1_lin.setOnClickListener {
            if (personal_info_extra_from_income_company_address_tv?.text?.isNotEmpty() as Boolean) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_extra_from_income_company_address1_lin.id
                requestPOI(personal_info_extra_from_income_company_address_tv?.text.toString())
            }
        }
        personal_info_extra_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _FROM_EXTRA_WORK_POSITION_INDEX, personal_info_extra_from_income_work_position_lin, personal_info_extra_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_EXTRA_WORK_POSITION_INDEX = index
            })
        }


        personal_info_house_type_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.house_type_list_key, _HOUSE_TYPE_INDEX, personal_info_house_type_lin, personal_info_house_type_tv, "请选择", { _, index ->
                _HOUSE_TYPE_INDEX = index
            })
        }
        personal_info_house_owner_relation_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.house_relationship_list_key, _HOUSE_OWNER_RELATION_INDEX, personal_info_house_owner_relation_lin, personal_info_house_owner_relation_tv, "请选择", { _, index ->
                _HOUSE_OWNER_RELATION_INDEX = index
            })
        }
        personal_info_urg_relation1_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.urg_rela_relationship_list_key, _URG_RELATION_INDEX1, personal_info_urg_relation1_lin, personal_info_urg_relation1_tv, "请选择", { _, index ->
                _URG_RELATION_INDEX1 = index
            })
        }
        personal_info_urg_relation2_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.urg_other_relationship_list_key, _URG_RELATION_INDEX2, personal_info_urg_relation2_lin, personal_info_urg_relation2_tv, "请选择", { _, index ->
                _URG_RELATION_INDEX2 = index
            })
        }

        personal_info_urg_mobile1_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = it.id
            selectContact()
        }
        personal_info_urg_mobile2_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = it.id
            selectContact()
        }

        step1.setOnClickListener { EventBus.getDefault().post(ApplyActivityEvent.showAutonymCertifyFragment) }

        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            var clientInfoBean = (activity as ApplyActivity).mClientInfo
            personal_info_clt_nm_edt.text = clientInfoBean.clt_nm
            personal_info_id_no_edt.text = clientInfoBean.id_no
            if (clientInfoBean.gender.isNotEmpty()) {
                personal_info_gender_tv?.text = clientInfoBean.gender
            }
            var empty = clientInfoBean.reg_addr.province.isNotEmpty()
            var notEmpty = clientInfoBean.reg_addr.city.isNotEmpty()
            var notEmpty1 = clientInfoBean.reg_addr.district.isNotEmpty()
            if (empty && notEmpty && notEmpty1) {
                personal_info_reg_tv?.text = clientInfoBean.reg_addr.province + "/" + clientInfoBean.reg_addr.city + "/" + clientInfoBean.reg_addr.district
            }
        }
    }

    fun checkCanNextStep(): Boolean {
//        return true
        if ((personal_info_gender_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_reg_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "户籍地不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_mobile_edt as EditText).text.isEmpty()) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile((personal_info_mobile_edt as EditText).text.toString())) {
            Toast.makeText(mContext, "手机号码格式错误", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_education_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "学历不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_current_address_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "现住地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_current_address1_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_current_address2_tv as NoEmptyEditText).text.isEmpty()) {
            Toast.makeText(mContext, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_live_with_parent_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "是否与父母同住不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "主要收入来源不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "工资" && (personal_info_from_income_year_edt as EditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "工资" && (personal_info_from_income_company_name_edt as EditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "工资" && (personal_info_from_income_company_address_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "工资" && (personal_info_from_income_company_address1_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "工资" && (personal_info_from_income_company_address2_tv as NoEmptyEditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "工资" && (personal_info_from_income_work_position_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "自营" && (personal_info_from_self_year_edt as EditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "自营" && (personal_info_from_self_type_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "业务类型不能为空", Toast.LENGTH_SHORT).show()
        }
//        else if (personal_info_income_from_tv.text == "自营" && personal_info_from_self_company_name_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "店铺名称不能为空", Toast.LENGTH_SHORT).show()
//        }
        else if ((personal_info_income_from_tv as TextView).text == "自营" && (personal_info_from_self_company_address_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "项目经营地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "自营" && (personal_info_from_self_company_address1_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "自营的详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_income_from_tv as TextView).text == "自营" && (personal_info_from_self_company_address2_tv as NoEmptyEditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "自营的门牌号不能为空", Toast.LENGTH_SHORT).show()
        }
//        else if ((personal_info_income_from_tv as TextView).text == "其他" && personal_info_from_other_year_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
//        } else if ((personal_info_income_from_tv as TextView).text == "其他" && personal_info_from_other_remark_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "备注不能为空", Toast.LENGTH_SHORT).show()
//        }
        else if ((personal_info_extra_income_from_tv as TextView).text == "工资" && (personal_info_extra_from_income_year_edt as EditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_extra_income_from_tv as TextView).text == "工资" && (personal_info_extra_from_income_company_name_edt as EditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_extra_income_from_tv as TextView).text == "工资" && (personal_info_extra_from_income_company_address_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_extra_income_from_tv as TextView).text == "工资" && (personal_info_extra_from_income_company_address1_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_extra_income_from_tv as TextView).text == "工资" && (personal_info_extra_from_income_company_address2_tv as NoEmptyEditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_extra_income_from_tv as TextView).text == "工资" && (personal_info_extra_from_income_work_position_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_house_type_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "房屋性质不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_house_area_edt as EditText).text.isEmpty()) {
            Toast.makeText(mContext, "房屋面积不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_house_owner_name_edt as EditText).text.isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_house_owner_relation_tv as TextView).text.isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_urg_contact1_edt as EditText).text.isEmpty()) {
            Toast.makeText(mContext, "紧急联系人人姓名不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_urg_mobile1_edt as EditText).text.isEmpty()) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile((personal_info_urg_mobile1_edt as EditText).text.toString())) {
            Toast.makeText(mContext, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_urg_relation1_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_urg_contact2_edt as EditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "紧急联系人姓名不能为空", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_urg_mobile2_edt as EditText).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile((personal_info_urg_mobile2_edt as EditText).text.toString())) {
            Toast.makeText(mContext, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show()
        } else if ((personal_info_urg_relation2_tv as TextView).text.isEmpty() as Boolean) {
            Toast.makeText(mContext, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    fun nextStep() {
        EventBus.getDefault().post(ApplyActivityEvent.showCommonRepaymentPeople)
    }

    fun requestPOI(city: String = "上海市/上海市/浦东新区") {
        val intent = Intent(mContext, AMapPoiListActivity::class.java)
        val addressArray = city.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        intent.putExtra("city", addressArray[1].substring(0, addressArray[1].length - 1))
        intent.putExtra("keywords", addressArray[2].substring(0, addressArray[2].length - 1))
        startActivityForResult(intent, Constants.REQUEST_ADDRESS)
    }

    fun selectContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, Constants.REQUEST_CONTACTS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_CONTACTS) {
                val uri = data.data
                val contacts = ContactsUtil.getPhoneContacts(mContext, uri)
                val result = arrayOf("", "")
                if (contacts != null) {
                    System.arraycopy(contacts, 0, result, 0, contacts.size)
                }
                when (CURRENT_CLICKED_VIEW_FOR_CONTACT) {
                    personal_info_urg_mobile1_img.id -> {
                        personal_info_urg_contact1_edt?.setText(result[0])
                        personal_info_urg_mobile1_edt?.setText(result[1].replace(" ", ""))
                    }
                    personal_info_urg_mobile2_img.id -> {
                        personal_info_urg_contact2_edt?.setText(result[0])
                        personal_info_urg_mobile2_edt?.setText(result[1].replace(" ", ""))
                    }
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                when (CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    personal_info_current_address1_lin.id -> {
                        personal_info_current_address1_tv?.text = data.getStringExtra("result");
                    }
                    personal_info_from_income_company_address1_lin.id -> {
                        personal_info_from_income_company_address1_tv?.text = data.getStringExtra("result");
                    }
                    personal_info_from_self_company_address1_lin.id -> {
                        personal_info_from_self_company_address1_tv?.text = data.getStringExtra("result");
                    }
                    personal_info_extra_from_income_company_address1_lin.id -> {
                        personal_info_extra_from_income_company_address1_tv?.text = data.getStringExtra("result");
                    }
                }
            }
        }
    }
}
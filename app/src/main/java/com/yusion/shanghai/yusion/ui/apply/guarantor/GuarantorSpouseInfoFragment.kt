package com.yusion.shanghai.yusion.ui.apply.guarantor

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.bean.ocr.OcrResp
import com.yusion.shanghai.yusion.event.AddGuarantorActivityEvent
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity
import com.yusion.shanghai.yusion.ui.apply.DocumentActivity
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.guarantor_spouse_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by ice on 2017/8/21.
 */
class GuarantorSpouseInfoFragment : DoubleCheckFragment() {

    var START_FOR_DRIVING_SINGLE_IMG_ACTIVITY = 1000
    var _GENDER_INDEX: Int = 0
    var _MARRIAGE_INDEX: Int = 0
    var idBackImgUrl = ""
    var idFrontImgUrl = ""
    var START_FOR_SPOUSE_ID_CARD_ACTIVITY = 1001
    var _WORK_POSITION_INDEX: Int = 0
    var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1
    var _INCOME_FROME_INDEX: Int = 0
    var _EXTRA_INCOME_FROME_INDEX: Int = 0
    var _FROM_INCOME_WORK_POSITION_INDEX: Int = 0
    var _FROM_EXTRA_WORK_POSITION_INDEX: Int = 0
    var CURRENT_CLICKED_VIEW_FOR_CONTACT: Int = -1
    var _FROM_SELF_TYPE_INDEX: Int = 0
    var _EDUCATION_INDEX: Int = 0
    var _HOUSE_TYPE_INDEX: Int = 0
    var _HOUSE_OWNER_RELATION_INDEX: Int = 0
    var _URG_RELATION_INDEX1: Int = 0
    var _URG_RELATION_INDEX2: Int = 0
    var ocrResp = OcrResp.ShowapiResBodyBean()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.guarantor_spouse_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        guarantor_spouse_info_submit_btn.setOnClickListener {
            (activity as AddGuarantorActivity).requestSubmit()
        }
        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step1.setOnClickListener { EventBus.getDefault().post(AddGuarantorActivityEvent.showGuarantorCreditInfoFragment) }
        step2.setOnClickListener { EventBus.getDefault().post(AddGuarantorActivityEvent.showGuarantorInfoFragment) }

        guarantor_spouse_info_id_back_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_back")
            intent.putExtra("role", "lender_sp")
            intent.putExtra("ocrResp", ocrResp)
            intent.putExtra("imgUrl", idBackImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        guarantor_spouse_info_id_front_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_front")
            intent.putExtra("role", "lender_sp")
            intent.putExtra("imgUrl", idFrontImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        guarantor_spouse_info_marriage_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.marriage_key, _MARRIAGE_INDEX, guarantor_spouse_info_marriage_lin, guarantor_spouse_info_marriage_tv, "请选择", { _, index ->
                _MARRIAGE_INDEX = index
                guarantor_spouse_info_marriage_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "已婚") View.VISIBLE else View.GONE
                guarantor_spouse_info_divorced_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "离异") View.VISIBLE else View.GONE
                guarantor_spouse_info_die_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "丧偶") View.VISIBLE else View.GONE
            })
        }
        guarantor_spouse_info_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "自营", "其他"), _INCOME_FROME_INDEX, guarantor_spouse_info_income_from_lin, guarantor_spouse_info_income_from_tv, "请选择", { _, index ->
                _INCOME_FROME_INDEX = index
                guarantor_spouse_info_from_income_group_lin.visibility = if (listOf("工资", "自营", "其他")[_INCOME_FROME_INDEX] == "工资") View.VISIBLE else View.GONE
                guarantor_spouse_info_from_self_group_lin.visibility = if (listOf("工资", "自营", "其他")[_INCOME_FROME_INDEX] == "自营") View.VISIBLE else View.GONE
                guarantor_spouse_info_from_other_group_lin.visibility = if (listOf("工资", "自营", "其他")[_INCOME_FROME_INDEX] == "其他") View.VISIBLE else View.GONE
            })
        }
        guarantor_spouse_info_extra_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资"), _EXTRA_INCOME_FROME_INDEX, guarantor_spouse_info_extra_income_from_lin, guarantor_spouse_info_extra_income_from_tv, "请选择", { _, index ->
                _EXTRA_INCOME_FROME_INDEX = index
                guarantor_spouse_info_extra_from_income_group_lin.visibility = if (listOf("工资")[_EXTRA_INCOME_FROME_INDEX] == "工资") View.VISIBLE else View.GONE
            })
        }
//        guarantor_spouse_info_divorced_lin.setOnClickListener {
//            var intent = Intent(mContext, SingleImgUploadActivity::class.java)
//            intent.putExtra("type", "divorce_proof")
//            intent.putExtra("role", "lender")
////            intent.putExtra("clt_id", (activity as addGuarantorActivity).mGuarantorInfo.clt_id)
//            intent.putExtra("imgUrl", divorceImgUrl)
//            startActivityForResult(intent, START_FOR_DRIVING_SINGLE_IMG_ACTIVITY)
//        }
        guarantor_spouse_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, _GENDER_INDEX, guarantor_spouse_info_gender_lin, guarantor_spouse_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
            })
        }

        guarantor_spouse_info_mobile_img.setOnClickListener { selectContact() }
        guarantor_spouse_info_submit_btn.setOnClickListener {
            if (checkCanNextStep()) {
                var addGuarantorActivity = activity as AddGuarantorActivity
                addGuarantorActivity.mGuarantorInfo.marriage = guarantor_spouse_info_marriage_tv.text.toString()
                if (addGuarantorActivity.mGuarantorInfo.marriage == "已婚") {
                    addGuarantorActivity.mGuarantorInfo.spouse.marriage = "已婚"
                    ocrResp?.let {
                        addGuarantorActivity.mGuarantorInfo.spouse.reg_addr_details = if (TextUtils.isEmpty(ocrResp.addr)) "" else ocrResp.addr
                        addGuarantorActivity.mGuarantorInfo.spouse.reg_addr.province = ocrResp.province
                        addGuarantorActivity.mGuarantorInfo.spouse.reg_addr.city = ocrResp.city
                        addGuarantorActivity.mGuarantorInfo.spouse.reg_addr.district = ocrResp.town
                    }
                    addGuarantorActivity.mGuarantorInfo.spouse.clt_nm = guarantor_spouse_info_clt_nm_edt.text.toString()
                    addGuarantorActivity.mGuarantorInfo.spouse.id_no = guarantor_spouse_info_id_no_edt.text.toString()
                    addGuarantorActivity.mGuarantorInfo.spouse.gender = guarantor_spouse_info_gender_tv.text.toString()
                    addGuarantorActivity.mGuarantorInfo.spouse.mobile = guarantor_spouse_info_mobile_edt.text.toString()

                    //主要收入来源
                    when (guarantor_spouse_info_income_from_tv.text) {
                        "工资" -> {
                            addGuarantorActivity.mGuarantorInfo.spouse.major_income_type = "工资"
                            addGuarantorActivity.mGuarantorInfo.spouse.major_income = guarantor_spouse_info_from_income_year_edt.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_name = guarantor_spouse_info_from_income_company_name_edt.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.province = guarantor_spouse_info_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.city = guarantor_spouse_info_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.district = guarantor_spouse_info_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.address1 = guarantor_spouse_info_from_income_company_address1_tv.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.address2 = guarantor_spouse_info_from_income_company_address2_tv.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_work_position = guarantor_spouse_info_from_income_work_position_tv.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_work_phone_num = guarantor_spouse_info_from_income_work_phone_num_edt.text.toString()
                        }
                        "自营" -> {
                            Toast.makeText(mContext, "业务类型", Toast.LENGTH_SHORT).show()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_income_type = "自营"
                            addGuarantorActivity.mGuarantorInfo.spouse.major_income = guarantor_spouse_info_from_self_year_edt.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_name = guarantor_spouse_info_from_self_company_name_edt.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.province = guarantor_spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.city = guarantor_spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.district = guarantor_spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.address1 = guarantor_spouse_info_from_self_company_address1_tv.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.address2 = guarantor_spouse_info_from_self_company_address2_tv.text.toString()
                        }
                        "其他" -> {
                            addGuarantorActivity.mGuarantorInfo.spouse.major_income_type = "其他"
                            addGuarantorActivity.mGuarantorInfo.spouse.major_income = guarantor_spouse_info_from_other_year_edt.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.major_remark = guarantor_spouse_info_from_other_remark_edt.text.toString()
                        }
                    }
                    //主要收入来源
                    when (guarantor_spouse_info_extra_income_from_tv.text) {
                        "工资" -> {
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_income_type = "工资"
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_income = guarantor_spouse_info_extra_from_income_year_edt.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_company_name = guarantor_spouse_info_extra_from_income_company_name_edt.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_company_addr.province = guarantor_spouse_info_extra_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_company_addr.city = guarantor_spouse_info_extra_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_company_addr.district = guarantor_spouse_info_extra_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_company_addr.address1 = guarantor_spouse_info_extra_from_income_company_address1_tv.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_company_addr.address2 = guarantor_spouse_info_extra_from_income_company_address2_tv.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_work_position = guarantor_spouse_info_extra_from_income_work_position_tv.text.toString()
                            addGuarantorActivity.mGuarantorInfo.spouse.extra_work_phone_num = guarantor_spouse_info_extra_from_income_work_phone_num_edt.text.toString()
                        }
                    }
                }
                nextStep()
            }
        }


        //工资
        guarantor_spouse_info_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_spouse_info_from_income_company_address_lin, guarantor_spouse_info_from_income_company_address_tv, "请选择所在地区") { _, _ -> guarantor_spouse_info_from_income_company_address1_tv.text = "" }
        }
        guarantor_spouse_info_from_income_company_address1_lin.setOnClickListener {
            if (guarantor_spouse_info_from_income_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_spouse_info_from_income_company_address1_lin.id
                requestPOI(guarantor_spouse_info_from_income_company_address_tv.text.toString())
            }
        }
        guarantor_spouse_info_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _FROM_INCOME_WORK_POSITION_INDEX, guarantor_spouse_info_from_income_work_position_lin, guarantor_spouse_info_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_INCOME_WORK_POSITION_INDEX = index
            })
        }

        //自营
        guarantor_spouse_info_from_self_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_spouse_info_from_self_company_address_lin, guarantor_spouse_info_from_self_company_address_tv, "请选择所在地区") { _, _ -> guarantor_spouse_info_from_self_company_address1_tv.text = "" }
        }
        guarantor_spouse_info_from_self_company_address1_lin.setOnClickListener {
            if (guarantor_spouse_info_from_self_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_spouse_info_from_self_company_address1_lin.id
                requestPOI(guarantor_spouse_info_from_self_company_address_tv.text.toString())
            }
        }
        guarantor_spouse_info_from_self_type_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.busi_type_list_key, _FROM_SELF_TYPE_INDEX, guarantor_spouse_info_from_self_type_lin, guarantor_spouse_info_from_self_type_tv, "请选择", { _, index ->
                _FROM_SELF_TYPE_INDEX = index
                if (YusionApp.CONFIG_RESP.busi_type_list_value[_FROM_SELF_TYPE_INDEX] == "其他") {
                    val editText = EditText(mContext)
                    AlertDialog.Builder(mContext)
                            .setTitle("请输入业务类型")
                            .setView(editText)
                            .setCancelable(false)
                            .setPositiveButton("确定") { dialog, which ->
                                guarantor_spouse_info_from_self_type_tv.text = editText.text
                                _FROM_SELF_TYPE_INDEX = 0
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }.show()
                }
            })
        }

        //额外工资
        guarantor_spouse_info_extra_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, guarantor_spouse_info_extra_from_income_company_address_lin, guarantor_spouse_info_extra_from_income_company_address_tv, "请选择所在地区") { _, _ -> guarantor_spouse_info_extra_from_income_company_address1_tv.text = "" }
        }
        guarantor_spouse_info_extra_from_income_company_address1_lin.setOnClickListener {
            if (guarantor_spouse_info_extra_from_income_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = guarantor_spouse_info_extra_from_income_company_address1_lin.id
                requestPOI(guarantor_spouse_info_extra_from_income_company_address_tv.text.toString())
            }
        }
        guarantor_spouse_info_extra_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _FROM_EXTRA_WORK_POSITION_INDEX, guarantor_spouse_info_extra_from_income_work_position_lin, guarantor_spouse_info_extra_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_EXTRA_WORK_POSITION_INDEX = index
            })
        }
    }

    fun checkCanNextStep(): Boolean {
        return true
//        if (guarantor_spouse_info_marriage_tv.text == "已婚") {
//            if (guarantor_spouse_info_clt_nm_edt.text.isEmpty()) {
//                Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show()
//            } else if (guarantor_spouse_info_id_no_edt.text.isEmpty()) {
//                Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show()
//            } else if (guarantor_spouse_info_gender_tv.text.isEmpty()) {
//                Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show()
//            } else if (guarantor_spouse_info_mobile_edt.text.isEmpty()) {
//                Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show()
//            } else {
//                return true
//            }
//        } else if (guarantor_spouse_info_marriage_tv.text == "离异") {
//            if (guarantor_spouse_info_divorced_tv.text == "请上传") {
//                Toast.makeText(mContext, "请上传 离婚证(法院判决书)", Toast.LENGTH_SHORT).show()
//            } else {
//                return true
//            }
//        } else {
//            return true
//        }
//        return false
    }

    fun nextStep() {
        (activity as AddGuarantorActivity).requestSubmit()
    }

    fun selectContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, Constants.REQUEST_CONTACTS)
    }

    fun requestPOI(city: String = "上海市/上海市/浦东新区") {
        val intent = Intent(mContext, AMapPoiListActivity::class.java)
        val addressArray = city.split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        intent.putExtra("city", addressArray[1].substring(0, addressArray[1].length - 1))
        intent.putExtra("keywords", addressArray[2].substring(0, addressArray[2].length - 1))
        startActivityForResult(intent, Constants.REQUEST_ADDRESS)
    }
}
package com.yusion.shanghai.yusion.ui.apply.guarantor

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
import android.widget.EditText
import android.widget.Toast
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.bean.ocr.OcrResp
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean
import com.yusion.shanghai.yusion.event.AddGuarantorActivityEvent
import com.yusion.shanghai.yusion.retrofit.api.ProductApi
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity
import com.yusion.shanghai.yusion.ui.upload.img.DocumentActivity
import com.yusion.shanghai.yusion.ui.upload.img.UploadListActivity
import com.yusion.shanghai.yusion.utils.*
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.guarantor_spouse_info.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by ice on 2017/8/21.
 */
class GuarantorSpouseInfoFragment : DoubleCheckFragment() {

    var _GENDER_INDEX: Int = 0
    var _MARRIAGE_INDEX: Int = 0
    var idBackImgUrl = ""
    var idFrontImgUrl = ""
    var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1
    var _INCOME_FROME_INDEX: Int = 0
    var _EXTRA_INCOME_FROME_INDEX: Int = 0
    var _FROM_INCOME_WORK_POSITION_INDEX: Int = 0
    var _FROM_EXTRA_WORK_POSITION_INDEX: Int = 0
    var _FROM_SELF_TYPE_INDEX: Int = 0
    var ID_BACK_FID = ""
    var ID_FRONT_FID = ""
    var ocrResp = OcrResp.ShowapiResBodyBean()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.guarantor_spouse_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDoubleCheckChangeBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
        }
        mDoubleCheckSubmitBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
            submit()
        }

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
            intent.putExtra("type", Constants.FileLabelType.ID_BACK)
            intent.putExtra("role", Constants.PersonType.GUARANTOR_SP)
            intent.putExtra("needUploadFidToServer", false)
            intent.putExtra("ocrResp", ocrResp)
            intent.putExtra("objectKey", ID_BACK_FID)
            intent.putExtra("imgUrl", idBackImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        guarantor_spouse_info_id_front_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.ID_FRONT)
            intent.putExtra("role", Constants.PersonType.GUARANTOR_SP)
            intent.putExtra("needUploadFidToServer", false)
            intent.putExtra("objectKey", ID_FRONT_FID)
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
            WheelViewUtil.showWheelView<String>(listOf("工资", "自营"), _INCOME_FROME_INDEX, guarantor_spouse_info_income_from_lin, guarantor_spouse_info_income_from_tv, "请选择", { _, index ->
                _INCOME_FROME_INDEX = index
                guarantor_spouse_info_from_income_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROME_INDEX] == "工资") View.VISIBLE else View.GONE
                guarantor_spouse_info_from_self_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROME_INDEX] == "自营") View.VISIBLE else View.GONE
//                guarantor_spouse_info_from_other_group_lin.visibility = if (listOf("工资", "自营")[_INCOME_FROME_INDEX] == "其他") View.VISIBLE else View.GONE
            })
        }
        guarantor_spouse_info_extra_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "无"), _EXTRA_INCOME_FROME_INDEX, guarantor_spouse_info_extra_income_from_lin, guarantor_spouse_info_extra_income_from_tv, "请选择", { _, index ->
                _EXTRA_INCOME_FROME_INDEX = index
                guarantor_spouse_info_extra_from_income_group_lin.visibility = if (listOf("工资", "无")[_EXTRA_INCOME_FROME_INDEX] == "工资") View.VISIBLE else View.GONE
            })
        }
        guarantor_spouse_info_divorced_lin.setOnClickListener {
            var intent = Intent(mContext, UploadListActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.MARRIAGE_PROOF)
            intent.putExtra("needUploadFidToServer", false)
            intent.putExtra("role", Constants.PersonType.LENDER)
            intent.putExtra("imgList", divorceImgsList)
            intent.putExtra("title", "离婚证")
            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT)
        }
        guarantor_spouse_info_register_addr_lin.setOnClickListener {
            var intent = Intent(mContext, UploadListActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.RES_BOOKLET)
            intent.putExtra("needUploadFidToServer", false)
            intent.putExtra("role", Constants.PersonType.LENDER)
            intent.putExtra("imgList", resBookList)
            intent.putExtra("title", "户口本")
            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT)
        }
        guarantor_spouse_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, _GENDER_INDEX, guarantor_spouse_info_gender_lin, guarantor_spouse_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
            })
        }

        guarantor_spouse_info_mobile_img.setOnClickListener { selectContact() }
        guarantor_spouse_info_submit_btn.setOnClickListener {
            if (checkCanNextStep()) {
                if (guarantor_spouse_info_marriage_tv.text.toString() == "已婚") {
                    clearDoubleCheckItems()
                    addDoubleCheckItem("姓名", guarantor_spouse_info_clt_nm_edt.text.toString())
                    addDoubleCheckItem("身份证号", guarantor_spouse_info_id_no_edt.text.toString())
                    addDoubleCheckItem("手机号", guarantor_spouse_info_mobile_edt.text.toString())
                    mDoubleCheckDialog.show()
                } else {
                    submit()
                }
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
                                InputMethodUtil.hideInputMethod(mContext)
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消") { dialog, _ ->
                                dialog.dismiss()
                                InputMethodUtil.hideInputMethod(mContext)
                                _FROM_SELF_TYPE_INDEX = 0;
                                guarantor_spouse_info_from_self_type_tv.text = null
                            }.show()
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

    private fun submit() {
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
                    addGuarantorActivity.mGuarantorInfo.spouse.major_income_type = "自营"
                    addGuarantorActivity.mGuarantorInfo.spouse.major_income = guarantor_spouse_info_from_self_year_edt.text.toString()
                    addGuarantorActivity.mGuarantorInfo.spouse.major_busi_type = guarantor_spouse_info_from_self_type_tv.text.toString()
                    addGuarantorActivity.mGuarantorInfo.spouse.major_company_name = guarantor_spouse_info_from_self_company_name_edt.text.toString()
                    if (TextUtils.isEmpty(guarantor_spouse_info_from_self_company_address_tv.text)) {
                        addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.province = ""
                        addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.city = ""
                        addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.district = ""
                    } else {
                        addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.province = guarantor_spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                        addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.city = guarantor_spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                        addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.district = guarantor_spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                    }
                    addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.address1 = guarantor_spouse_info_from_self_company_address1_tv.text.toString()
                    addGuarantorActivity.mGuarantorInfo.spouse.major_company_addr.address2 = guarantor_spouse_info_from_self_company_address2_tv.text.toString()
                }
                "其他" -> {
                    addGuarantorActivity.mGuarantorInfo.spouse.major_income_type = "其他"
                    addGuarantorActivity.mGuarantorInfo.spouse.major_income = guarantor_spouse_info_from_other_year_edt.text.toString()
                    addGuarantorActivity.mGuarantorInfo.spouse.major_remark = guarantor_spouse_info_from_other_remark_edt.text.toString()
                }
            }
            //额外收入来源
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
                "工资" -> {
                    addGuarantorActivity.mGuarantorInfo.spouse.extra_income_type = "无"
                }
            }
        }
        //nextStep()
        ProductApi.updateGuarantorInfo(mContext, addGuarantorActivity.mGuarantorInfo) {
            if (it != null) {
                addGuarantorActivity.mGuarantorInfo = it
                uploadUrl(addGuarantorActivity.mGuarantorInfo.clt_id, addGuarantorActivity.mGuarantorInfo.spouse.clt_id)
            }
        }
    }

    fun checkCanNextStep(): Boolean {
//        return true
        if (guarantor_spouse_info_marriage_tv.text.isEmpty()) {
            Toast.makeText(mContext, "请选择婚姻状况", Toast.LENGTH_SHORT).show()
            return false
        }
        if (guarantor_spouse_info_marriage_tv.text == "已婚") {
            if (ID_BACK_FID.isEmpty()) {
                Toast.makeText(mContext, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show()
            } else if (ID_FRONT_FID.isEmpty()) {
                Toast.makeText(mContext, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_clt_nm_edt.text.isEmpty()) {
                Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_id_no_edt.text.isEmpty()) {
                Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show()
            } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(guarantor_spouse_info_id_no_edt.text.toString())) {
                Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_gender_tv.text.isEmpty()) {
                Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_mobile_edt.text.isEmpty()) {
                Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(guarantor_spouse_info_mobile_edt.text.toString())) {
                Toast.makeText(mContext, "手机号码格式错误", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text.isEmpty()) {
                Toast.makeText(mContext, "主要收入来源不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "工资" && guarantor_spouse_info_from_income_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "工资" && guarantor_spouse_info_from_income_company_name_edt.text.isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "工资" && guarantor_spouse_info_from_income_company_address_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "工资" && guarantor_spouse_info_from_income_company_address1_tv.text.isEmpty()) {
                Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "工资" && guarantor_spouse_info_from_income_company_address2_tv.text.isEmpty()) {
                Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "工资" && guarantor_spouse_info_from_income_work_position_tv.text.isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "自营" && guarantor_spouse_info_from_self_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "自营" && guarantor_spouse_info_from_self_type_tv.text.isEmpty()) {
                Toast.makeText(mContext, "业务类型不能为空", Toast.LENGTH_SHORT).show()
            }
//            else if (guarantor_spouse_info_income_from_tv.text == "自营" && guarantor_spouse_info_from_self_company_name_edt.text.isEmpty()) {
//                Toast.makeText(mContext, "店铺名称不能为空", Toast.LENGTH_SHORT).show()
//            }
            else if (guarantor_spouse_info_income_from_tv.text == "自营" && guarantor_spouse_info_from_self_company_address_tv.text.isEmpty()) {
                Toast.makeText(mContext, "项目经营地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "自营" && guarantor_spouse_info_from_self_company_address1_tv.text.isEmpty()) {
                Toast.makeText(mContext, "自营的详细地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "自营" && guarantor_spouse_info_from_self_company_address2_tv.text.isEmpty()) {
                Toast.makeText(mContext, "自营的门牌号不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "其他" && guarantor_spouse_info_from_other_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_income_from_tv.text == "其他" && guarantor_spouse_info_from_other_remark_edt.text.isEmpty()) {
                Toast.makeText(mContext, "备注不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_extra_income_from_tv.text == "工资" && guarantor_spouse_info_extra_from_income_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_extra_income_from_tv.text == "工资" && guarantor_spouse_info_extra_from_income_company_name_edt.text.isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_extra_income_from_tv.text == "工资" && guarantor_spouse_info_extra_from_income_company_address_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_extra_income_from_tv.text == "工资" && guarantor_spouse_info_extra_from_income_company_address1_tv.text.isEmpty()) {
                Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_extra_income_from_tv.text == "工资" && guarantor_spouse_info_extra_from_income_company_address2_tv.text.isEmpty()) {
                Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
            } else if (guarantor_spouse_info_extra_income_from_tv.text == "工资" && guarantor_spouse_info_extra_from_income_work_position_tv.text.isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
            } else {
                return true
            }
            return false
        } else {
            return true
        }
        return false
    }

    fun uploadUrl(cltId: String, spouseCltId: String) {
        var addGuarantorActivity = activity as AddGuarantorActivity
        val files = ArrayList<UploadFilesUrlReq.FileUrlBean>()
        when (addGuarantorActivity.mGuarantorInfo.marriage) {
            "离异" -> {
                for (divorceItem in divorceImgsList) {
                    val divorceFileItem = UploadFilesUrlReq.FileUrlBean()
                    divorceFileItem.file_id = divorceItem.objectKey
                    divorceFileItem.label = Constants.FileLabelType.MARRIAGE_PROOF
                    divorceFileItem.clt_id = cltId
                    files.add(divorceFileItem)
                }
            }
            "丧偶" -> {
                for (resItem in resBookList) {
                    val resBookFileItem = UploadFilesUrlReq.FileUrlBean()
                    resBookFileItem.file_id = resItem.objectKey
                    resBookFileItem.label = Constants.FileLabelType.RES_BOOKLET
                    resBookFileItem.clt_id = cltId
                    files.add(resBookFileItem)
                }
            }
            "已婚" -> {
                val idBackBean = UploadFilesUrlReq.FileUrlBean()
                idBackBean.file_id = ID_BACK_FID
                idBackBean.label = Constants.FileLabelType.ID_BACK
                idBackBean.clt_id = spouseCltId
                files.add(idBackBean)

                val idFrontBean = UploadFilesUrlReq.FileUrlBean()
                idFrontBean.file_id = ID_FRONT_FID
                idFrontBean.label = Constants.FileLabelType.ID_FRONT
                idFrontBean.clt_id = spouseCltId
                files.add(idFrontBean)
            }
        }
        val uploadFilesUrlReq = UploadFilesUrlReq()
        uploadFilesUrlReq.files = files
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "")
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "")
        UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq) { code, _ ->
            if (code >= 0) {
                nextStep()
            }
        }
    }

    fun nextStep() {
        (activity as AddGuarantorActivity).requestSubmit()
    }

    fun selectContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, Constants.REQUEST_CONTACTS)
    }

    private var divorceImgsList = ArrayList<UploadImgItemBean>()
    private var resBookList = ArrayList<UploadImgItemBean>()
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
                guarantor_spouse_info_mobile_edt.setText(result[1].replace(" ", ""))
            } else if (requestCode == Constants.REQUEST_DOCUMENT) {
                when (data.getStringExtra("type")) {
                    Constants.FileLabelType.ID_BACK -> {
                        ID_BACK_FID = data.getStringExtra("objectKey")
                        idBackImgUrl = data.getStringExtra("imgUrl")
                        if (ID_BACK_FID.isNotEmpty()) {
                            guarantor_spouse_info_id_back_tv.text = "已上传"
                            guarantor_spouse_info_id_back_tv.setTextColor(resources.getColor(R.color.system_color))
                            ocrResp = data.getSerializableExtra("ocrResp") as OcrResp.ShowapiResBodyBean
                        } else {
                            guarantor_spouse_info_id_back_tv.text = "请上传"
                            guarantor_spouse_info_id_back_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                        guarantor_spouse_info_id_no_edt.setText(ocrResp.idNo)
                        guarantor_spouse_info_clt_nm_edt.setText(ocrResp.name)
                    }
                    Constants.FileLabelType.ID_FRONT -> {
                        ID_FRONT_FID = data.getStringExtra("objectKey")
                        idFrontImgUrl = data.getStringExtra("imgUrl")
                        if (ID_FRONT_FID.isNotEmpty()) {
                            guarantor_spouse_info_id_front_tv.text = "已上传"
                            guarantor_spouse_info_id_front_tv.setTextColor(resources.getColor(R.color.system_color))
                        } else {
                            guarantor_spouse_info_id_front_tv.text = "请上传"
                            guarantor_spouse_info_id_front_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                    }
                }
            } else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
                when (data.getStringExtra("type")) {
                    Constants.FileLabelType.RES_BOOKLET -> {
                        resBookList = data.getSerializableExtra("imgList") as ArrayList<UploadImgItemBean>
                        if (resBookList.size > 0) {
                            guarantor_spouse_info_register_addr_tv.text = "已上传"
                            guarantor_spouse_info_register_addr_tv.setTextColor(resources.getColor(R.color.system_color))
                        } else {
                            guarantor_spouse_info_register_addr_tv.text = "请上传"
                            guarantor_spouse_info_register_addr_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                    }
                    Constants.FileLabelType.MARRIAGE_PROOF -> {
                        divorceImgsList = data.getSerializableExtra("imgList") as ArrayList<UploadImgItemBean>
                        if (divorceImgsList.size > 0) {
                            guarantor_spouse_info_divorced_tv.text = "已上传"
                            guarantor_spouse_info_divorced_tv.setTextColor(resources.getColor(R.color.system_color))
                        } else {
                            guarantor_spouse_info_divorced_tv.text = "请上传"
                            guarantor_spouse_info_divorced_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                    }
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                when (CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    guarantor_spouse_info_from_income_company_address1_lin.id -> {
                        guarantor_spouse_info_from_income_company_address1_tv.text = data.getStringExtra("result");
                    }
                    guarantor_spouse_info_from_self_company_address1_lin.id -> {
                        guarantor_spouse_info_from_self_company_address1_tv.text = data.getStringExtra("result");
                    }
                    guarantor_spouse_info_extra_from_income_company_address1_lin.id -> {
                        guarantor_spouse_info_extra_from_income_company_address1_tv.text = data.getStringExtra("result");
                    }
                }
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
}
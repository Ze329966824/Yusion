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
import android.widget.EditText
import android.widget.Toast
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.BaseFragment
import com.yusion.shanghai.yusion.bean.ocr.OcrResp
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ui.info.UploadListActivity
import com.yusion.shanghai.yusion.utils.CheckIdCardValidUtil
import com.yusion.shanghai.yusion.utils.ContactsUtil
import com.yusion.shanghai.yusion.utils.InputMethodUtil
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.spouse_info.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by ice on 17/7/5.
 */
class SpouseInfoFragment : BaseFragment() {

    var _GENDER_INDEX: Int = 0
    var _MARRIAGE_INDEX: Int = 0
    var idBackImgUrl = ""
    var idFrontImgUrl = ""
    var ID_BACK_FID = ""
    var ID_FRONT_FID = ""
    var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1
    var _INCOME_FROME_INDEX: Int = 0
    var _EXTRA_INCOME_FROME_INDEX: Int = 0
    var _FROM_INCOME_WORK_POSITION_INDEX: Int = 0
    var _FROM_EXTRA_WORK_POSITION_INDEX: Int = 0
    var _FROM_SELF_TYPE_INDEX: Int = 0

    var ocrResp = OcrResp.ShowapiResBodyBean()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.spouse_info, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spouse_info_id_back_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.ID_BACK)
            intent.putExtra("role", Constants.PersonType.LENDER_SP)
            intent.putExtra("ocrResp", ocrResp)
            intent.putExtra("imgUrl", idBackImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        spouse_info_id_front_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.ID_FRONT)
            intent.putExtra("role", Constants.PersonType.LENDER_SP)
            intent.putExtra("imgUrl", idFrontImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        spouse_info_marriage_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.marriage_key, _MARRIAGE_INDEX, spouse_info_marriage_lin, spouse_info_marriage_tv, "请选择", { _, index ->
                _MARRIAGE_INDEX = index
                spouse_info_marriage_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "已婚") View.VISIBLE else View.GONE
                spouse_info_divorced_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "离异") View.VISIBLE else View.GONE
                spouse_info_die_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "丧偶") View.VISIBLE else View.GONE
            })
        }
        spouse_info_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "自营", "其他"), _INCOME_FROME_INDEX, spouse_info_income_from_lin, spouse_info_income_from_tv, "请选择", { _, index ->
                _INCOME_FROME_INDEX = index
                spouse_info_from_income_group_lin.visibility = if (listOf("工资", "自营", "其他")[_INCOME_FROME_INDEX] == "工资") View.VISIBLE else View.GONE
                spouse_info_from_self_group_lin.visibility = if (listOf("工资", "自营", "其他")[_INCOME_FROME_INDEX] == "自营") View.VISIBLE else View.GONE
                spouse_info_from_other_group_lin.visibility = if (listOf("工资", "自营", "其他")[_INCOME_FROME_INDEX] == "其他") View.VISIBLE else View.GONE
            })
        }
        spouse_info_extra_income_from_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(listOf("工资", "无"), _EXTRA_INCOME_FROME_INDEX, spouse_info_extra_income_from_lin, spouse_info_extra_income_from_tv, "请选择", { _, index ->
                _EXTRA_INCOME_FROME_INDEX = index
                spouse_info_extra_from_income_group_lin.visibility = if (listOf("工资", "无")[_EXTRA_INCOME_FROME_INDEX] == "工资") View.VISIBLE else View.GONE
            })
        }
        spouse_info_divorced_lin.setOnClickListener {
            var intent = Intent(mContext, UploadListActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.MARRIAGE_PROOF)
            intent.putExtra("role", Constants.PersonType.LENDER)
            intent.putExtra("imgList", divorceImgsList)
            intent.putExtra("title", "离婚证")
            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT)
        }
        spouse_info_register_addr_lin.setOnClickListener {
            var intent = Intent(mContext, UploadListActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.RES_BOOKLET)
            intent.putExtra("role", Constants.PersonType.LENDER)
            intent.putExtra("imgList", resBookList)
            intent.putExtra("title", "户口本")
            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT)
        }
        spouse_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, _GENDER_INDEX, spouse_info_gender_lin, spouse_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
            })
        }

        spouse_info_mobile_img.setOnClickListener { selectContact() }
        spouse_info_submit_btn.setOnClickListener {
            if (checkCanNextStep()) {
                var applyActivity = activity as ApplyActivity
                applyActivity.mClientInfo.marriage = spouse_info_marriage_tv.text.toString()
                if (applyActivity.mClientInfo.marriage == "已婚") {
                    applyActivity.mClientInfo.spouse.marriage = "已婚"
                    ocrResp?.let {
                        applyActivity.mClientInfo.spouse.reg_addr_details = if (TextUtils.isEmpty(ocrResp.addr)) "" else ocrResp.addr
                        applyActivity.mClientInfo.spouse.reg_addr.province = ocrResp.province
                        applyActivity.mClientInfo.spouse.reg_addr.city = ocrResp.city
                        applyActivity.mClientInfo.spouse.reg_addr.district = ocrResp.town
                    }
                    applyActivity.mClientInfo.spouse.clt_nm = spouse_info_clt_nm_edt.text.toString()
                    applyActivity.mClientInfo.spouse.id_no = spouse_info_id_no_edt.text.toString()
                    applyActivity.mClientInfo.spouse.gender = spouse_info_gender_tv.text.toString()
                    applyActivity.mClientInfo.spouse.mobile = spouse_info_mobile_edt.text.toString()
                    applyActivity.mClientInfo.child_num = spouse_info_child_count_edt.text.toString()

                    //主要收入来源
                    when (spouse_info_income_from_tv.text) {
                        "工资" -> {
                            applyActivity.mClientInfo.spouse.major_income_type = "工资"
                            applyActivity.mClientInfo.spouse.major_income = spouse_info_from_income_year_edt.text.toString()
                            applyActivity.mClientInfo.spouse.major_company_name = spouse_info_from_income_company_name_edt.text.toString()
                            applyActivity.mClientInfo.spouse.major_company_addr.province = spouse_info_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            applyActivity.mClientInfo.spouse.major_company_addr.city = spouse_info_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                            applyActivity.mClientInfo.spouse.major_company_addr.district = spouse_info_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                            applyActivity.mClientInfo.spouse.major_company_addr.address1 = spouse_info_from_income_company_address1_tv.text.toString()
                            applyActivity.mClientInfo.spouse.major_company_addr.address2 = spouse_info_from_income_company_address2_tv.text.toString()
                            applyActivity.mClientInfo.spouse.major_work_position = spouse_info_from_income_work_position_tv.text.toString()
                            applyActivity.mClientInfo.spouse.major_work_phone_num = spouse_info_from_income_work_phone_num_edt.text.toString()
                        }
                        "自营" -> {
                            Toast.makeText(mContext, "业务类型", Toast.LENGTH_SHORT).show()
                            applyActivity.mClientInfo.spouse.major_income_type = "自营"
                            applyActivity.mClientInfo.spouse.major_income = spouse_info_from_self_year_edt.text.toString()
                            applyActivity.mClientInfo.spouse.major_company_name = spouse_info_from_self_company_name_edt.text.toString()
                            applyActivity.mClientInfo.spouse.major_company_addr.province = spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            applyActivity.mClientInfo.spouse.major_company_addr.city = spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                            applyActivity.mClientInfo.spouse.major_company_addr.district = spouse_info_from_self_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                            applyActivity.mClientInfo.spouse.major_company_addr.address1 = spouse_info_from_self_company_address1_tv.text.toString()
                            applyActivity.mClientInfo.spouse.major_company_addr.address2 = spouse_info_from_self_company_address2_tv.text.toString()
                        }
                        "其他" -> {
                            applyActivity.mClientInfo.spouse.major_income_type = "其他"
                            applyActivity.mClientInfo.spouse.major_income = spouse_info_from_other_year_edt.text.toString()
                            applyActivity.mClientInfo.spouse.major_remark = spouse_info_from_other_remark_edt.text.toString()
                        }
                    }
                    //额外收入来源
                    when (spouse_info_extra_income_from_tv.text) {
                        "工资" -> {
                            applyActivity.mClientInfo.spouse.extra_income_type = "工资"
                            applyActivity.mClientInfo.spouse.extra_income = spouse_info_extra_from_income_year_edt.text.toString()
                            applyActivity.mClientInfo.spouse.extra_company_name = spouse_info_extra_from_income_company_name_edt.text.toString()
                            applyActivity.mClientInfo.spouse.extra_company_addr.province = spouse_info_extra_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                            applyActivity.mClientInfo.spouse.extra_company_addr.city = spouse_info_extra_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                            applyActivity.mClientInfo.spouse.extra_company_addr.district = spouse_info_extra_from_income_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
                            applyActivity.mClientInfo.spouse.extra_company_addr.address1 = spouse_info_extra_from_income_company_address1_tv.text.toString()
                            applyActivity.mClientInfo.spouse.extra_company_addr.address2 = spouse_info_extra_from_income_company_address2_tv.text.toString()
                            applyActivity.mClientInfo.spouse.extra_work_position = spouse_info_extra_from_income_work_position_tv.text.toString()
                            applyActivity.mClientInfo.spouse.extra_work_phone_num = spouse_info_extra_from_income_work_phone_num_edt.text.toString()
                        }
                        "无" -> {
                            applyActivity.mClientInfo.spouse.extra_income_type = "无"
                        }

                    }
                } else if (applyActivity.mClientInfo.marriage == "离异") {
                    applyActivity.mClientInfo.child_num = spouse_info_divorced_child_count_edt.text.toString()
                } else if (applyActivity.mClientInfo.marriage == "丧偶") {
                    applyActivity.mClientInfo.child_num = spouse_info_die_child_count_edt.text.toString()
                }
                uploadUrl(applyActivity.mClientInfo.clt_id, applyActivity.mClientInfo.spouse.clt_id)
//                nextStep()
            }
        }


        //工资
        spouse_info_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, spouse_info_from_income_company_address_lin, spouse_info_from_income_company_address_tv, "请选择所在地区") { _, _ -> spouse_info_from_income_company_address1_tv.text = "" }
        }
        spouse_info_from_income_company_address1_lin.setOnClickListener {
            if (spouse_info_from_income_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = spouse_info_from_income_company_address1_lin.id
                requestPOI(spouse_info_from_income_company_address_tv.text.toString())
            }
        }
        spouse_info_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _FROM_INCOME_WORK_POSITION_INDEX, spouse_info_from_income_work_position_lin, spouse_info_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_INCOME_WORK_POSITION_INDEX = index
            })
        }

        //自营
        spouse_info_from_self_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, spouse_info_from_self_company_address_lin, spouse_info_from_self_company_address_tv, "请选择所在地区") { _, _ -> spouse_info_from_self_company_address1_tv.text = "" }
        }
        spouse_info_from_self_company_address1_lin.setOnClickListener {
            if (spouse_info_from_self_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = spouse_info_from_self_company_address1_lin.id
                requestPOI(spouse_info_from_self_company_address_tv.text.toString())
            }
        }
        spouse_info_from_self_type_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.busi_type_list_key, _FROM_SELF_TYPE_INDEX, spouse_info_from_self_type_lin, spouse_info_from_self_type_tv, "请选择", { _, index ->
                _FROM_SELF_TYPE_INDEX = index
                if (YusionApp.CONFIG_RESP.busi_type_list_value[_FROM_SELF_TYPE_INDEX] == "其他") {
                    val editText = EditText(mContext)
                    AlertDialog.Builder(mContext)
                            .setTitle("请输入业务类型")
                            .setView(editText)
                            .setCancelable(false)
                            .setPositiveButton("确定") { dialog, _ ->
                                spouse_info_from_self_type_tv.text = editText.text
                                _FROM_SELF_TYPE_INDEX = 0
                                dialog.dismiss()
                                InputMethodUtil.hideInputMethod(mContext)
                            }
                            .setNegativeButton("取消") { dialog, _ ->
                                dialog.dismiss()
                                InputMethodUtil.hideInputMethod(mContext)
                                _FROM_SELF_TYPE_INDEX = 0;
                                spouse_info_from_self_type_tv.text = null
                            }.show()
                }
            })
        }

        //额外工资
        spouse_info_extra_from_income_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, spouse_info_extra_from_income_company_address_lin, spouse_info_extra_from_income_company_address_tv, "请选择所在地区") { _, _ -> spouse_info_extra_from_income_company_address1_tv.text = "" }
        }
        spouse_info_extra_from_income_company_address1_lin.setOnClickListener {
            if (spouse_info_extra_from_income_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = spouse_info_extra_from_income_company_address1_lin.id
                requestPOI(spouse_info_extra_from_income_company_address_tv.text.toString())
            }
        }
        spouse_info_extra_from_income_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _FROM_EXTRA_WORK_POSITION_INDEX, spouse_info_extra_from_income_work_position_lin, spouse_info_extra_from_income_work_position_tv, "请选择", { _, index ->
                _FROM_EXTRA_WORK_POSITION_INDEX = index
            })
        }


        step1.setOnClickListener { EventBus.getDefault().post(ApplyActivityEvent.showAutonymCertifyFragment) }
        step2.setOnClickListener { EventBus.getDefault().post(ApplyActivityEvent.showPersonalInfoFragment) }

        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
    }

    fun checkCanNextStep(): Boolean {
//        return true
        if (spouse_info_marriage_tv.text.isEmpty()) {
            Toast.makeText(mContext, "请选择婚姻状况", Toast.LENGTH_SHORT).show()
            return false
        }
        if (spouse_info_marriage_tv.text == "已婚") {
            if (ID_BACK_FID.isEmpty()) {
                Toast.makeText(mContext, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show()
            } else if (ID_FRONT_FID.isEmpty()) {
                Toast.makeText(mContext, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_clt_nm_edt.text.isEmpty()) {
                Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_id_no_edt.text.isEmpty()) {
                Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show()
            } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(spouse_info_id_no_edt.text.toString())) {
                Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_gender_tv.text.isEmpty()) {
                Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_mobile_edt.text.isEmpty()) {
                Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_child_count_edt.text.isEmpty()) {
                Toast.makeText(mContext, "子女数量不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text.isEmpty()) {
                Toast.makeText(mContext, "主要收入来源不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "工资" && spouse_info_from_income_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "工资" && spouse_info_from_income_company_name_edt.text.isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "工资" && spouse_info_from_income_company_address_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "工资" && spouse_info_from_income_company_address1_tv.text.isEmpty()) {
                Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "工资" && spouse_info_from_income_company_address2_tv.text.isEmpty()) {
                Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "工资" && spouse_info_from_income_work_position_tv.text.isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "自营" && spouse_info_from_self_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "自营" && spouse_info_from_self_type_tv.text.isEmpty()) {
                Toast.makeText(mContext, "业务类型不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "自营" && spouse_info_from_self_company_name_edt.text.isEmpty()) {
                Toast.makeText(mContext, "店铺名称不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "自营" && spouse_info_from_self_company_address_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "自营" && spouse_info_from_self_company_address1_tv.text.isEmpty()) {
                Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "自营" && spouse_info_from_self_company_address2_tv.text.isEmpty()) {
                Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "其他" && spouse_info_from_other_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_income_from_tv.text == "其他" && spouse_info_from_other_remark_edt.text.isEmpty()) {
                Toast.makeText(mContext, "备注不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_extra_income_from_tv.text.isEmpty()) {
                Toast.makeText(mContext, "额外来源不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_extra_income_from_tv.text == "工资" && spouse_info_extra_from_income_year_edt.text.isEmpty()) {
                Toast.makeText(mContext, "年收入不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_extra_income_from_tv.text == "工资" && spouse_info_extra_from_income_company_name_edt.text.isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_extra_income_from_tv.text == "工资" && spouse_info_extra_from_income_company_address_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_extra_income_from_tv.text == "工资" && spouse_info_extra_from_income_company_address1_tv.text.isEmpty()) {
                Toast.makeText(mContext, "详细地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_extra_income_from_tv.text == "工资" && spouse_info_extra_from_income_company_address2_tv.text.isEmpty()) {
                Toast.makeText(mContext, "门牌号不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_extra_income_from_tv.text == "工资" && spouse_info_extra_from_income_work_position_tv.text.isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
            } else {
                return true
            }
            return false
        } else if (spouse_info_marriage_tv.text == "丧偶") {
            if (spouse_info_die_child_count_edt.text.isEmpty()) {
                Toast.makeText(mContext, "子女数量不能为空", Toast.LENGTH_SHORT).show()
            } else {
                return true
            }
            return false
        } else if (spouse_info_marriage_tv.text == "离异") {
            if (spouse_info_divorced_child_count_edt.text.isEmpty()) {
                Toast.makeText(mContext, "子女数量不能为空", Toast.LENGTH_SHORT).show()
            } else {
                return true
            }
            return false
        } else {
            return true
        }
        return false
    }

    fun nextStep() {
        (activity as ApplyActivity).requestSubmit()
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

    fun uploadUrl(cltId: String, spouseCltId: String) {
        var applyActivity = activity as ApplyActivity
        val files = ArrayList<UploadFilesUrlReq.FileUrlBean>()
        when (applyActivity.mClientInfo.marriage) {
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
                spouse_info_clt_nm_edt.setText(result[0])
                spouse_info_mobile_edt.setText(result[1].replace(" ", ""))
            } else if (requestCode == Constants.REQUEST_DOCUMENT) {
                when (data.getStringExtra("type")) {
                    Constants.FileLabelType.ID_BACK -> {
                        ID_BACK_FID = data.getStringExtra("objectKey")
                        idBackImgUrl = data.getStringExtra("imgUrl")
                        if (ID_BACK_FID.isNotEmpty()) {
                            spouse_info_id_back_tv.text = "已上传"
                            spouse_info_id_back_tv.setTextColor(resources.getColor(R.color.system_color))
                            ocrResp = data.getSerializableExtra("ocrResp") as OcrResp.ShowapiResBodyBean
                        } else {
                            spouse_info_id_back_tv.text = "请上传"
                            spouse_info_id_back_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                        spouse_info_id_no_edt.setText(ocrResp.idNo)
                        spouse_info_clt_nm_edt.setText(ocrResp.name)
                    }
                    Constants.FileLabelType.ID_FRONT -> {
                        ID_FRONT_FID = data.getStringExtra("objectKey")
                        idFrontImgUrl = data.getStringExtra("imgUrl")
                        if (ID_FRONT_FID.isNotEmpty()) {
                            spouse_info_id_front_tv.text = "已上传"
                            spouse_info_id_front_tv.setTextColor(resources.getColor(R.color.system_color))
                        } else {
                            spouse_info_id_front_tv.text = "请上传"
                            spouse_info_id_front_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                    }
                }
            } else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
                when (data.getStringExtra("type")) {
                    Constants.FileLabelType.RES_BOOKLET -> {
                        resBookList = data.getSerializableExtra("imgList") as ArrayList<UploadImgItemBean>
                        if (resBookList.size > 0) {
                            spouse_info_register_addr_tv.text = "已上传"
                            spouse_info_register_addr_tv.setTextColor(resources.getColor(R.color.system_color))
                        } else {
                            spouse_info_register_addr_tv.text = "请上传"
                            spouse_info_register_addr_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                    }
                    Constants.FileLabelType.MARRIAGE_PROOF -> {
                        divorceImgsList = data.getSerializableExtra("imgList") as ArrayList<UploadImgItemBean>
                        if (divorceImgsList.size > 0) {
                            spouse_info_divorced_tv.text = "已上传"
                            spouse_info_divorced_tv.setTextColor(resources.getColor(R.color.system_color))
                        } else {
                            spouse_info_divorced_tv.text = "请上传"
                            spouse_info_divorced_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                        }
                    }
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                when (CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    spouse_info_from_income_company_address1_lin.id -> {
                        spouse_info_from_income_company_address1_tv.text = data.getStringExtra("result");
                    }
                    spouse_info_from_self_company_address1_lin.id -> {
                        spouse_info_from_self_company_address1_tv.text = data.getStringExtra("result");
                    }
                    spouse_info_extra_from_income_company_address1_lin.id -> {
                        spouse_info_extra_from_income_company_address1_tv.text = data.getStringExtra("result");
                    }
                }
            }
        }
    }
}
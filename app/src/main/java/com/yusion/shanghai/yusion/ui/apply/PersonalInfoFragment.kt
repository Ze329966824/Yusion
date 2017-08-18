package com.yusion.shanghai.yusion.ui.apply

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.utils.ContactsUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.personal_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by ice on 17/7/5.
 */
class PersonalInfoFragment : DoubleCheckFragment() {

    companion object {
        var CURRENT_CLICKED_VIEW_FOR_CONTACT: Int = -1
        var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1

        var _GENDER_INDEX: Int = 0
        var _WORK_POSITION_INDEX: Int = 0
        var _EDUCATION_INDEX: Int = 0
        var _HOUSE_TYPE_INDEX: Int = 0
        var _HOUSE_OWNER_RELATION_INDEX: Int = 0
        var _URG_RELATION_INDEX1: Int = 0
        var _URG_RELATION_INDEX2: Int = 0
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.personal_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDoubleCheckChangeBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
        }
        mDoubleCheckSubmitBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
//            var applyActivity = activity as ApplyActivity
//            if (personal_info_reg_tv.text.isNotEmpty()) {
//                applyActivity.mUserInfoBean.reg_addr.province = personal_info_reg_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
//                applyActivity.mUserInfoBean.reg_addr.city = personal_info_reg_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
//                applyActivity.mUserInfoBean.reg_addr.district = personal_info_reg_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
//            }
//            applyActivity.mUserInfoBean.gender = personal_info_gender_tv.text.toString()
//            applyActivity.mUserInfoBean.mobile = personal_info_mobile_edt.text.toString()
//            applyActivity.mUserInfoBean.edu = personal_info_education_tv.text.toString()
//            if (personal_info_current_address_tv.text.isNotEmpty()) {
//                applyActivity.mUserInfoBean.current_addr.province = personal_info_current_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
//                applyActivity.mUserInfoBean.current_addr.city = personal_info_current_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
//                applyActivity.mUserInfoBean.current_addr.district = personal_info_current_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
//            }
//            applyActivity.mUserInfoBean.current_addr.address1 = personal_info_current_address1_tv.text.toString()
//            applyActivity.mUserInfoBean.current_addr.address2 = personal_info_current_address2_tv.text.toString()
//
//            applyActivity.mUserInfoBean.company_name = personal_info_company_name_edt.text.toString()
//            if (personal_info_company_address_tv.text.isNotEmpty()) {
//                applyActivity.mUserInfoBean.company_addr.province = personal_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
//                applyActivity.mUserInfoBean.company_addr.city = personal_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
//                applyActivity.mUserInfoBean.company_addr.district = personal_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
//            }
//            applyActivity.mUserInfoBean.company_addr.address1 = personal_info_company_address1_tv.text.toString()
//            applyActivity.mUserInfoBean.company_addr.address2 = personal_info_company_address2_tv.text.toString()
//
//            applyActivity.mUserInfoBean.work_position = personal_info_work_position_tv.text.toString()
//            applyActivity.mUserInfoBean.monthly_income = personal_info_monthly_income_edt.text.toString().toInt()
//            applyActivity.mUserInfoBean.work_phone_num = personal_info_work_phone_num_edt.text.toString()
//            applyActivity.mUserInfoBean.house_owner_name = personal_info_house_owner_name_edt.text.toString()
//            applyActivity.mUserInfoBean.house_owner_relation = personal_info_house_owner_relation_tv.text.toString()
//            applyActivity.mUserInfoBean.house_type = personal_info_house_type_tv.text.toString()
//            applyActivity.mUserInfoBean.house_area = personal_info_house_area_edt.text.toString().toInt()
//            applyActivity.mUserInfoBean.urg_contact1 = personal_info_urg_contact1_edt.text.toString()
//            applyActivity.mUserInfoBean.urg_mobile1 = personal_info_urg_mobile1_edt.text.toString()
//            applyActivity.mUserInfoBean.urg_relation1 = personal_info_urg_relation1_tv.text.toString()
//            applyActivity.mUserInfoBean.urg_contact2 = personal_info_urg_contact2_edt.text.toString()
//            applyActivity.mUserInfoBean.urg_mobile2 = personal_info_urg_mobile2_edt.text.toString()
//            applyActivity.mUserInfoBean.urg_relation2 = personal_info_urg_relation2_tv.text.toString()
            nextStep()
        }
        personal_info_next_btn.setOnClickListener {
            if (checkCanNextStep()) {
                clearDoubleCheckItems()
                addDoubleCheckItem("姓名", personal_info_clt_nm_edt.text.toString())
                addDoubleCheckItem("身份证号", personal_info_id_no_edt.text.toString())
                addDoubleCheckItem("手机号", personal_info_mobile_edt.text.toString())
                mDoubleCheckDialog.show()
            }
        }
        personal_info_mobile_edt.setText(YusionApp.MOBILE)
        personal_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, _GENDER_INDEX, personal_info_gender_lin, personal_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
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
            WheelViewUtil.showCityWheelView(javaClass.simpleName, personal_info_current_address_lin, personal_info_current_address_tv, "请选择所在地区") { _, _ -> personal_info_current_address1_tv.text = "" }
        }
        personal_info_current_address1_tv.setOnClickListener {
            if (personal_info_current_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_current_address1_lin.id
                requestPOI(personal_info_current_address_tv.text.toString())
            }
        }
        personal_info_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, personal_info_company_address_lin, personal_info_company_address_tv, "请选择所在地区") { _, _ -> personal_info_company_address1_tv.text = "" }
        }
        personal_info_company_address1_lin.setOnClickListener {
            if (personal_info_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = personal_info_company_address1_lin.id
                requestPOI(personal_info_company_address_tv.text.toString())
            }
        }
        personal_info_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _WORK_POSITION_INDEX, personal_info_work_position_lin, personal_info_work_position_tv, "请选择", { _, index ->
                _WORK_POSITION_INDEX = index
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
//        if (hidden.not()) {
//            var userInfoBean = (activity as ApplyActivity).mUserInfoBean
//            personal_info_clt_nm_edt.text = userInfoBean.clt_nm
//            personal_info_id_no_edt.text = userInfoBean.id_no
//            personal_info_gender_tv.text = userInfoBean.gender
//            if (userInfoBean.reg_addr.province.isNotEmpty() && userInfoBean.reg_addr.city.isNotEmpty() && userInfoBean.reg_addr.district.isNotEmpty()) {
//                personal_info_reg_tv.text = userInfoBean.reg_addr.province + "/" + userInfoBean.reg_addr.city + "/" + userInfoBean.reg_addr.district
//            }
//        }
    }

    fun checkCanNextStep(): Boolean {
        return true
//        if (personal_info_reg_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "户籍地不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_gender_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_mobile_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_education_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "学历不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_current_address_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "现住地址不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_current_address1_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_current_address2_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_company_name_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_company_address_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_company_address1_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_company_address2_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_work_position_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_monthly_income_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "月收入不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_house_type_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "房屋性质不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_house_area_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "房屋面积不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_house_owner_name_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_house_owner_relation_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_urg_contact1_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "亲属联系人姓名不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_urg_mobile1_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "亲属联系人手机号不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_urg_relation1_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "亲属联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_urg_contact2_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "其他联系人姓名不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_urg_mobile2_edt.text.isEmpty()) {
//            Toast.makeText(mContext, "其他联系人手机号不能为空", Toast.LENGTH_SHORT).show()
//        } else if (personal_info_urg_relation2_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "其他联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show()
//        } else {
//            return true
//        }
//        return false
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

    private var drivingLicenseImgUrl: String = ""
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
                        personal_info_urg_contact1_edt.setText(result[0])
                        personal_info_urg_mobile1_edt.setText(result[1])
                    }
                    personal_info_urg_mobile2_img.id -> {
                        personal_info_urg_contact2_edt.setText(result[0])
                        personal_info_urg_mobile2_edt.setText(result[1])
                    }
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                when (CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    personal_info_current_address1_lin.id -> {
                        personal_info_current_address1_tv.text = data.getStringExtra("result");
                    }
                    personal_info_company_address1_lin.id -> {
                        personal_info_company_address1_tv.text = data.getStringExtra("result");
                    }
                }
            }
        }
    }
}
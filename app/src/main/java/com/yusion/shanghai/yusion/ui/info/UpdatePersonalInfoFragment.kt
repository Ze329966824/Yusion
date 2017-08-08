package com.yusion.shanghai.yusion.ui.info


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.BaseFragment
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq
import com.yusion.shanghai.yusion.bean.user.UserInfoBean
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity
import com.yusion.shanghai.yusion.ui.apply.SingleImgUploadActivity
import com.yusion.shanghai.yusion.utils.ContactsUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.fragment_personal_info.*

class UpdatePersonalInfoFragment : BaseFragment() {

    companion object {
        var CURRENT_CLICKED_VIEW_FOR_CONTACT: Int = -1
        var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1

        var START_FPR_DRIVING_LICENSE_IMG_ACTIVITY: Int = 100

        var _GENDER_INDEX: Int = 0
        var _WORK_POSITION_INDEX: Int = 0
        var _EDUCATION_INDEX: Int = 0
        var _HOUSE_TYPE_INDEX: Int = 0
        var _HOUSE_OWNER_RELATION_INDEX: Int = 0
        var _URG_RELATION_INDEX1: Int = 0
        var _URG_RELATION_INDEX2: Int = 0
    }

    private var mData: UserInfoBean = UserInfoBean()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        update_personal_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, UpdatePersonalInfoFragment._GENDER_INDEX, update_personal_info_gender_lin, update_personal_info_gender_tv, "请选择", { _, index ->
                UpdatePersonalInfoFragment._GENDER_INDEX = index
            })
        }
        update_personal_info_reg_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, update_personal_info_reg_lin, update_personal_info_reg_tv, "请选择") { _, _ -> }
        }
        update_personal_info_education_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.education_list_key, UpdatePersonalInfoFragment._EDUCATION_INDEX, update_personal_info_education_lin, update_personal_info_education_tv, "请选择", { _, index ->
                UpdatePersonalInfoFragment._EDUCATION_INDEX = index
            })
        }

        update_personal_info_current_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, update_personal_info_current_address_lin, update_personal_info_current_address_tv, "请选择") { _, _ -> update_personal_info_current_address1_tv.text = "" }
        }
        update_personal_info_current_address1_lin.setOnClickListener {
            if (update_personal_info_current_address_tv.text.isNotEmpty()) {
                UpdatePersonalInfoFragment.CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_current_address1_lin.id
                requestPOI(update_personal_info_current_address_tv.text.toString())
            }
        }


        update_personal_info_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, update_personal_info_company_address_lin, update_personal_info_company_address_tv, "请选择") { _, _ -> update_personal_info_company_address1_tv.text = "" }
        }
        update_personal_info_company_address1_lin.setOnClickListener {
            if (update_personal_info_company_address_tv.text.isNotEmpty()) {
                UpdatePersonalInfoFragment.CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_company_address1_lin.id
                requestPOI(update_personal_info_company_address_tv.text.toString())
            }
        }

        update_personal_info_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, UpdatePersonalInfoFragment._WORK_POSITION_INDEX, update_personal_info_work_position_lin, update_personal_info_work_position_tv, "请选择", { _, index ->
                UpdatePersonalInfoFragment._WORK_POSITION_INDEX = index
            })
        }

        update_personal_info_driving_license_lin.setOnClickListener {
            val intent = Intent(mContext, SingleImgUploadActivity::class.java)
            intent.putExtra("type", "driving_lic")
            intent.putExtra("role", "lender")
            intent.putExtra("clt_id", mData.clt_id)
            intent.putExtra("imgUrl", drivingLicenseImgUrl)
            startActivityForResult(intent, START_FPR_DRIVING_LICENSE_IMG_ACTIVITY)
        }
        update_personal_info_house_type_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.house_type_list_key, UpdatePersonalInfoFragment._HOUSE_TYPE_INDEX, update_personal_info_house_type_lin, update_personal_info_house_type_tv, "请选择", { _, index ->
                UpdatePersonalInfoFragment._HOUSE_TYPE_INDEX = index
            })
        }
        update_personal_info_house_owner_relation_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.house_relationship_list_key, UpdatePersonalInfoFragment._HOUSE_OWNER_RELATION_INDEX, update_personal_info_house_owner_relation_lin, update_personal_info_house_owner_relation_tv, "请选择", { _, index ->
                UpdatePersonalInfoFragment._HOUSE_OWNER_RELATION_INDEX = index
            })
        }
        update_personal_info_urg_relation1_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.urg_rela_relationship_list_key, UpdatePersonalInfoFragment._URG_RELATION_INDEX1, update_personal_info_urg_relation1_lin, update_personal_info_urg_relation1_tv, "请选择", { _, index ->
                UpdatePersonalInfoFragment._URG_RELATION_INDEX1 = index
            })
        }
        update_personal_info_urg_relation2_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.urg_other_relationship_list_key, UpdatePersonalInfoFragment._URG_RELATION_INDEX2, update_personal_info_urg_relation2_lin, update_personal_info_urg_relation2_tv, "请选择", { _, index ->
                UpdatePersonalInfoFragment._URG_RELATION_INDEX2 = index
            })
        }

        update_personal_info_urg_mobile1_img.setOnClickListener {
            UpdatePersonalInfoFragment.CURRENT_CLICKED_VIEW_FOR_CONTACT = it.id
            selectContact()
        }
        update_personal_info_urg_mobile2_img.setOnClickListener {
            UpdatePersonalInfoFragment.CURRENT_CLICKED_VIEW_FOR_CONTACT = it.id
            selectContact()
        }
    }

    fun onRefresh(data: UserInfoBean) {
        mData = data
        update_personal_info_clt_nm_edt.setText(mData.clt_nm)
        update_personal_info_id_no_edt.setText(mData.id_no)
        update_personal_info_mobile_edt.setText(mData.mobile)
        update_personal_info_gender_tv.text = mData.gender
        update_personal_info_reg_tv.text = if (mData.reg_addr.province.isNotEmpty()) "${mData.reg_addr.province}/${mData.reg_addr.city}/${mData.reg_addr.district}" else null
        update_personal_info_education_tv.text = mData.edu
        update_personal_info_current_address_tv.text = if (mData.current_addr.province.isNotEmpty()) "${mData.current_addr.province}/${mData.current_addr.city}/${mData.current_addr.district}" else null
        update_personal_info_current_address1_tv.text = mData.current_addr.address1
        update_personal_info_current_address2_tv.setText(mData.current_addr.address2)
        update_personal_info_company_name_edt.setText(mData.company_name)
        update_personal_info_company_address_tv.text = if (mData.company_addr.province.isNotEmpty()) "${mData.company_addr.province}/${mData.company_addr.city}/${mData.company_addr.district}" else null
        update_personal_info_company_address1_tv.text = mData.company_addr.address1
        update_personal_info_company_address2_tv.setText(mData.company_addr.address2)
        update_personal_info_work_position_tv.text = mData.work_position
        update_personal_info_monthly_income_edt.setText("${mData.monthly_income}")
        update_personal_info_work_phone_num_edt.setText(mData.work_phone_num)
        update_personal_info_house_owner_name_edt.setText(mData.house_owner_name)
        update_personal_info_house_owner_relation_tv.text = mData.house_owner_relation
        update_personal_info_house_type_tv.text = mData.house_type
        update_personal_info_house_area_edt.setText("${mData.house_area}")
        update_personal_info_urg_contact1_edt.setText(mData.urg_contact1)
        update_personal_info_urg_mobile1_edt.setText(mData.urg_mobile1)
        update_personal_info_urg_relation1_tv.text = mData.urg_relation1
        update_personal_info_urg_contact2_edt.setText(mData.urg_contact2)
        update_personal_info_urg_mobile2_edt.setText(mData.urg_mobile2)
        update_personal_info_urg_relation2_tv.text = mData.urg_relation2
        val req = ListImgsReq()
        req.role = "lender"
        req.label = "driving_lic"
        req.clt_id = mData.clt_id
        UploadApi.listImgs(mContext, req, { resp ->
            if (resp.list.size != 0) {
                update_personal_info_driving_license_tv.text = "已上传"
                drivingLicenseImgUrl = resp.list[0].s_url
            }
        })
    }

    fun requestUpdateUserInfoBean(onFinishCallBack: OnVoidCallBack) {
        mData.clt_nm = update_personal_info_clt_nm_edt.text.toString()
        mData.id_no = update_personal_info_id_no_edt.text.toString()
        mData.mobile = update_personal_info_mobile_edt.text.toString()
        mData.gender = update_personal_info_gender_tv.text.toString()
        if (update_personal_info_reg_tv.text.isNotEmpty()) {
            mData.reg_addr.province = update_personal_info_reg_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
            mData.reg_addr.city = update_personal_info_reg_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
            mData.reg_addr.district = update_personal_info_reg_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
        }
        mData.edu = update_personal_info_education_tv.text.toString()
        if (update_personal_info_current_address_tv.text.isNotEmpty()) {
            mData.current_addr.province = update_personal_info_current_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
            mData.current_addr.city = update_personal_info_current_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
            mData.current_addr.district = update_personal_info_current_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
        }
        mData.current_addr.address1 = update_personal_info_current_address1_tv.text.toString()
        mData.current_addr.address2 = update_personal_info_current_address2_tv.text.toString()
        mData.company_name = update_personal_info_company_name_edt.text.toString()
        if (update_personal_info_company_address_tv.text.isNotEmpty()) {
            mData.company_addr.province = update_personal_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
            mData.company_addr.city = update_personal_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
            mData.company_addr.district = update_personal_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
        }
        mData.company_addr.address1 = update_personal_info_company_address1_tv.text.toString()
        mData.company_addr.address2 = update_personal_info_company_address2_tv.text.toString()
        mData.work_position = update_personal_info_work_position_tv.text.toString()
        mData.monthly_income = update_personal_info_monthly_income_edt.text.toString().toInt()
        mData.work_phone_num = update_personal_info_work_phone_num_edt.text.toString()
        mData.house_owner_name = update_personal_info_house_owner_name_edt.text.toString()
        mData.house_owner_relation = update_personal_info_house_owner_relation_tv.text.toString()
        mData.house_type = update_personal_info_house_type_tv.text.toString()
        mData.house_area = update_personal_info_house_area_edt.text.toString().toInt()
        mData.urg_contact1 = update_personal_info_urg_contact1_edt.text.toString()
        mData.urg_mobile1 = update_personal_info_urg_mobile1_edt.text.toString()
        mData.urg_relation1 = update_personal_info_urg_relation1_tv.text.toString()
        mData.urg_contact2 = update_personal_info_urg_contact2_edt.text.toString()
        mData.urg_mobile2 = update_personal_info_urg_mobile2_edt.text.toString()
        mData.urg_relation2 = update_personal_info_urg_relation2_tv.text.toString()
        onFinishCallBack.callBack()
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
                when (UpdatePersonalInfoFragment.CURRENT_CLICKED_VIEW_FOR_CONTACT) {
                    update_personal_info_urg_mobile1_img.id -> {
                        update_personal_info_urg_contact1_edt.setText(result[0])
                        update_personal_info_urg_mobile1_edt.setText(result[1])
                    }
                    update_personal_info_urg_mobile2_img.id -> {
                        update_personal_info_urg_contact2_edt.setText(result[0])
                        update_personal_info_urg_mobile2_edt.setText(result[1])
                    }
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                when (UpdatePersonalInfoFragment.CURRENT_CLICKED_VIEW_FOR_ADDRESS) {
                    update_personal_info_current_address1_lin.id -> {
                        update_personal_info_current_address1_tv.text = data.getStringExtra("result"); }
                    update_personal_info_company_address1_lin.id -> {
                        update_personal_info_company_address1_tv.text = data.getStringExtra("result"); }
                }
            } else if (requestCode == START_FPR_DRIVING_LICENSE_IMG_ACTIVITY) {
                drivingLicenseImgUrl = data.getStringExtra("imgUrl")
                update_personal_info_driving_license_tv.text = if (drivingLicenseImgUrl.isNotEmpty()) "已上传" else "请上传"
            }
        }
    }
}

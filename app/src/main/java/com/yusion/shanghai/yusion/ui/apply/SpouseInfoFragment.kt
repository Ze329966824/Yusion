package com.yusion.shanghai.yusion.ui.apply

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.BaseFragment
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.utils.ContactsUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.spouse_info.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by ice on 17/7/5.
 */
class SpouseInfoFragment : BaseFragment() {

    companion object {
        var START_FOR_DRIVING_SINGLE_IMG_ACTIVITY = 1000
        var START_FOR_SPOUSE_ID_CARD_ACTIVITY = 1001
        var _GENDER_INDEX: Int = 0
        var _WORK_POSITION_INDEX: Int = 0
        var _MARRIAGE_INDEX: Int = 0
        var idBackImgUrl = ""
        var idFrontImgUrl = ""
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.spouse_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spouse_info_id_back_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_back")
            intent.putExtra("role", "lender_sp")
            intent.putExtra("imgUrl", idBackImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        spouse_info_id_front_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_front")
            intent.putExtra("role", "lender_sp")
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
        spouse_info_divorced_lin.setOnClickListener {
            var intent = Intent(mContext, SingleImgUploadActivity::class.java)
            intent.putExtra("type", "divorce_proof")
            intent.putExtra("role", "lender")
            intent.putExtra("clt_id", (activity as ApplyActivity).mUserInfoBean.clt_id)
            intent.putExtra("imgUrl", divorceImgUrl)
            startActivityForResult(intent, START_FOR_DRIVING_SINGLE_IMG_ACTIVITY)
        }
        spouse_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, _GENDER_INDEX, spouse_info_gender_lin, spouse_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
            })
        }
        spouse_info_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, spouse_info_company_address_lin, spouse_info_company_address_tv, "请选择所在地区") { _, _ -> spouse_info_company_address1_tv.text = "" }
        }
        spouse_info_company_address1_lin.setOnClickListener {
            if (spouse_info_company_address_tv.text.isNotEmpty())
                requestPOI(spouse_info_company_address_tv.text.toString())
        }
        spouse_info_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _WORK_POSITION_INDEX, spouse_info_work_position_lin, spouse_info_work_position_tv, "请选择", { _, index ->
                _WORK_POSITION_INDEX = index
            })
        }
        spouse_info_mobile_img.setOnClickListener { selectContact() }
        spouse_info_submit_btn.setOnClickListener {
            //            if (checkCanNextStep()) {
//                var applyActivity = activity as ApplyActivity
//                applyActivity.mUserInfoBean.marriage = if (spouse_info_marriage_tv.text == "已婚") "已婚" else spouse_info_marriage_tv.text.toString()
//                if (applyActivity.mUserInfoBean.marriage == "已婚") {
//                    applyActivity.mUserInfoBean.spouse.marriage = "已婚"
//                    applyActivity.mUserInfoBean.spouse.clt_nm = spouse_info_clt_nm_edt.text.toString()
//                    applyActivity.mUserInfoBean.spouse.id_no = spouse_info_id_no_edt.text.toString()
//                    applyActivity.mUserInfoBean.spouse.gender = spouse_info_gender_tv.text.toString()
//                    applyActivity.mUserInfoBean.spouse.mobile = spouse_info_mobile_edt.text.toString()
//                    applyActivity.mUserInfoBean.spouse.company_name = spouse_info_company_name_edt.text.toString()
//                    if (spouse_info_company_address_tv.text.isNotEmpty()) {
//                        applyActivity.mUserInfoBean.spouse.company_addr.province = spouse_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
//                        applyActivity.mUserInfoBean.spouse.company_addr.city = spouse_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
//                        applyActivity.mUserInfoBean.spouse.company_addr.district = spouse_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
//                    }
//                    applyActivity.mUserInfoBean.spouse.company_addr.address1 = spouse_info_company_address1_tv.text.toString()
//                    applyActivity.mUserInfoBean.spouse.company_addr.address2 = spouse_info_company_address2_edt.text.toString()
//                    applyActivity.mUserInfoBean.spouse.work_phone_num = spouse_info_work_phone_num_edt.text.toString()
//                    applyActivity.mUserInfoBean.spouse.work_position = spouse_info_work_position_tv.text.toString()
//                    applyActivity.mUserInfoBean.spouse.monthly_income = spouse_info_monthly_income_edt.text.toString().toInt()
//                    applyActivity.mUserInfoBean.spouse.reg_addr_details = if (regDetailAddress.isEmpty()) "" else regDetailAddress
//                }
            nextStep()
//            }
        }

        step1.setOnClickListener { EventBus.getDefault().post(ApplyActivityEvent.showAutonymCertifyFragment) }
        step2.setOnClickListener { EventBus.getDefault().post(ApplyActivityEvent.showPersonalInfoFragment) }

        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
    }

    fun checkCanNextStep(): Boolean {
        if (spouse_info_marriage_tv.text == "已婚") {
            if (spouse_info_clt_nm_edt.text.isEmpty()) {
                Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_id_no_edt.text.isEmpty()) {
                Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_gender_tv.text.isEmpty()) {
                Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_mobile_edt.text.isEmpty()) {
                Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_company_name_edt.text.isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_company_address_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_company_address1_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_company_address2_edt.text.isEmpty()) {
                Toast.makeText(mContext, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_work_position_tv.text.isEmpty()) {
                Toast.makeText(mContext, "单职务不能为空", Toast.LENGTH_SHORT).show()
            } else if (spouse_info_monthly_income_edt.text.isEmpty()) {
                Toast.makeText(mContext, "月收入不能为空", Toast.LENGTH_SHORT).show()
            } else {
                return true
            }
        } else if (spouse_info_marriage_tv.text == "离异") {
            if (spouse_info_divorced_tv.text == "请上传") {
                Toast.makeText(mContext, "请上传 离婚证(法院判决书)", Toast.LENGTH_SHORT).show()
            } else {
                return true
            }
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

    private var divorceImgUrl = ""
    var regDetailAddress = ""
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
                spouse_info_mobile_edt.setText(result[1])
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                spouse_info_company_address1_tv.text = data.getStringExtra("result");
            } else if (requestCode == Constants.REQUEST_DOCUMENT) {
                when (data.getStringExtra("type")) {
                    "id_card_back" -> {
                        if (!TextUtils.isEmpty(data.getStringExtra("objectKey"))) {
                            spouse_info_id_back_tv.text = "已上传"
                            spouse_info_id_back_tv.setTextColor(resources.getColor(R.color.system_color))
                            spouse_info_id_no_edt.setText(data.getStringExtra("idNo"))
                            regDetailAddress = data.getStringExtra("addr")
                            spouse_info_clt_nm_edt.setText(data.getStringExtra("name"))
                            idBackImgUrl = data.getStringExtra("imgUrl")
                        }
                    }
                    "id_card_front" -> {
                        if (!TextUtils.isEmpty(data.getStringExtra("objectKey"))) {
                            idFrontImgUrl = data.getStringExtra("imgUrl")
                            spouse_info_id_front_tv.text = "已上传"
                            spouse_info_id_front_tv.setTextColor(resources.getColor(R.color.system_color))
                        }
                    }
                }
            }
        }
    }
}
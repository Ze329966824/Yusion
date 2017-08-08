package com.yusion.shanghai.yusion.ui.info

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.BaseFragment
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq
import com.yusion.shanghai.yusion.bean.user.UserInfoBean
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity
import com.yusion.shanghai.yusion.ui.apply.SingleImgUploadActivity
import com.yusion.shanghai.yusion.ui.apply.SpouseIdCardActivity
import com.yusion.shanghai.yusion.ui.apply.SpouseInfoFragment
import com.yusion.shanghai.yusion.utils.ContactsUtil
import com.yusion.shanghai.yusion.utils.LoadingUtils
import com.yusion.shanghai.yusion.utils.OcrUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.fragment_spouse_info.*
import java.io.File

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

class UpdateSpouseInfoFragment : BaseFragment() {

    companion object {
        var CURRENT_CLICKED_VIEW_FOR_CONTACT: Int = -1
        var CURRENT_CLICKED_VIEW_FOR_ADDRESS: Int = -1
        var CURRENT_CLICKED_VIEW_FOR_PIC: Int = -1

        var START_FOR_DRIVING_SINGLE_IMG_ACTIVITY = 1000
        var START_FOR_SPOUSE_ID_CARD_ACTIVITY = 1001

        var _GENDER_INDEX: Int = 0
        var _WORK_POSITION_INDEX: Int = 0
        var _EDUCATION_INDEX: Int = 0
        var _MARRIAGE_INDEX: Int = 0
        var _HOUSE_TYPE_INDEX: Int = 0
        var _HOUSE_OWNER_RELATION_INDEX: Int = 0
        var _URG_RELATION_INDEX1: Int = 0
        var _URG_RELATION_INDEX2: Int = 0
    }

    private var mData: UserInfoBean = UserInfoBean()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_spouse_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update_spouse_info_marriage_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.marriage_key, _MARRIAGE_INDEX, update_spouse_info_marriage_lin, update_spouse_info_marriage_tv, "请选择", { _, index ->
                _MARRIAGE_INDEX = index
                update_spouse_info_marriage_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "已婚") View.VISIBLE else View.GONE
                update_spouse_info_divorced_group_lin.visibility = if (YusionApp.CONFIG_RESP.marriage_value[_MARRIAGE_INDEX] == "离异") View.VISIBLE else View.GONE
            })
        }
        update_spouse_info_divorced_lin.setOnClickListener {
            var intent = Intent(mContext, SingleImgUploadActivity::class.java)
            intent.putExtra("type", "divorce_proof")
            intent.putExtra("role", "lender")
            intent.putExtra("clt_id", mData.clt_id)
            intent.putExtra("imgUrl", divorceImgUrl)
            startActivityForResult(intent, SpouseInfoFragment.START_FOR_DRIVING_SINGLE_IMG_ACTIVITY)
        }
        update_spouse_info_id_no_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_PIC = update_spouse_info_id_no_img.id
            takePhoto()
        }
        update_spouse_info_gender_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.gender_list_key, _GENDER_INDEX, update_spouse_info_gender_lin, update_spouse_info_gender_tv, "请选择", { _, index ->
                _GENDER_INDEX = index
            })
        }
        update_spouse_info_mobile_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = it.id
            selectContact()
        }

        update_spouse_info_company_address_lin.setOnClickListener {
            WheelViewUtil.showCityWheelView(javaClass.simpleName, update_spouse_info_company_address_lin, update_spouse_info_company_address_tv, "请选择") { _, _ -> update_spouse_info_company_address1_tv.text = "" }
        }
        update_spouse_info_company_address1_lin.setOnClickListener {
            if (update_spouse_info_company_address_tv.text.isNotEmpty()) {
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_company_address1_lin.id
                requestPOI(update_spouse_info_company_address_tv.text.toString())
            }
        }
        update_spouse_info_work_position_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.work_position_key, _WORK_POSITION_INDEX, update_spouse_info_work_position_lin, update_spouse_info_work_position_tv, "请选择", { _, index ->
                _WORK_POSITION_INDEX = index
            })
        }
    }

    fun onRefresh(data: UserInfoBean) {
        mData = data
        update_spouse_info_marriage_tv.text = mData.marriage
        update_spouse_info_marriage_group_lin.visibility = if (mData.marriage == "已婚") View.VISIBLE else View.GONE
        update_spouse_info_divorced_group_lin.visibility = if (mData.marriage == "离异") View.VISIBLE else View.GONE
        if (mData.marriage == "已婚") {
            update_spouse_info_clt_nm_edt.setText(mData.spouse.clt_nm)
            update_spouse_info_id_no_edt.setText(mData.spouse.id_no)
            update_spouse_info_gender_tv.text = mData.spouse.gender
            update_spouse_info_mobile_edt.setText(mData.spouse.mobile)
            update_spouse_info_company_name_edt.setText(mData.spouse.company_name)
            update_spouse_info_company_address_tv.text = if (mData.spouse.company_addr.province.isNotEmpty()) "${mData.spouse.company_addr.province}/${mData.spouse.company_addr.city}/${mData.spouse.company_addr.district}" else null
            update_spouse_info_company_address1_tv.text = mData.spouse.company_addr.address1
            update_spouse_info_company_address2_edt.setText(mData.spouse.company_addr.address2)
            update_spouse_info_work_position_tv.text = mData.spouse.work_position
            update_spouse_info_monthly_income_edt.setText("${mData.spouse.monthly_income}")
            update_spouse_info_work_phone_num_edt.setText(mData.spouse.work_phone_num)
        } else if (mData.marriage == "离异") {
            val req = ListImgsReq()
            req.role = "lender"
            req.label = "divorce_proof"
            req.clt_id = mData.clt_id
            UploadApi.listImgs(mContext, req, { resp ->
                if (resp.list.size != 0) {
                    update_spouse_info_divorced_tv.text = "已上传"
                    divorceImgUrl = resp.list[0].s_url
                }
            })
        }
    }

    fun requestUpdateUserInfoBean(onFinishCallBack: OnVoidCallBack) {
        mData.marriage = if (update_spouse_info_marriage_tv.text == "已婚") "已婚" else update_spouse_info_marriage_tv.text.toString()
        if (mData.marriage == "已婚") {
            mData.spouse.marriage = "已婚"
            mData.spouse.clt_nm = update_spouse_info_clt_nm_edt.text.toString()
            mData.spouse.id_no = update_spouse_info_id_no_edt.text.toString()
            mData.spouse.gender = update_spouse_info_gender_tv.text.toString()
            mData.spouse.mobile = update_spouse_info_mobile_edt.text.toString()
            mData.spouse.company_name = update_spouse_info_company_name_edt.text.toString()
            if (update_spouse_info_company_address_tv.text.isNotEmpty()) {
                mData.spouse.company_addr.province = update_spouse_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0]
                mData.spouse.company_addr.city = update_spouse_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                mData.spouse.company_addr.district = update_spouse_info_company_address_tv.text.toString().split("/".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[2]
            }
            mData.spouse.company_addr.address1 = update_spouse_info_company_address1_tv.text.toString()
            mData.spouse.company_addr.address2 = update_spouse_info_company_address2_edt.text.toString()
            mData.spouse.work_phone_num = update_spouse_info_work_phone_num_edt.text.toString()
            mData.spouse.work_position = update_spouse_info_work_position_tv.text.toString()
            mData.spouse.monthly_income = update_spouse_info_monthly_income_edt.text.toString().toInt()
        }
        onFinishCallBack.callBack()
    }

    private var idBackFile = File("");
    fun takePhoto() {
        idBackFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + ".jpg")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(idBackFile))
        startActivityForResult(intent, Constants.REQUEST_IDCARD_1_CAPTURE)
    }

    private var divorceImgUrl = ""
    var regDetailAddress = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_CONTACTS -> {
                    data?.let {
                        val uri = data.data
                        val contacts = ContactsUtil.getPhoneContacts(mContext, uri)
                        val result = arrayOf("", "")
                        if (contacts != null) {
                            System.arraycopy(contacts, 0, result, 0, contacts.size)
                        }
                        update_spouse_info_clt_nm_edt.setText(result[0])
                        update_spouse_info_mobile_edt.setText(result[1])
                    }
                }
                Constants.REQUEST_ADDRESS -> {
                    data?.let {
                        update_spouse_info_company_address1_tv.text = data.getStringExtra("result")
                    }
                }
                UpdateSpouseInfoFragment.START_FOR_DRIVING_SINGLE_IMG_ACTIVITY -> {
                    data?.let {
                        divorceImgUrl = data.getStringExtra("imgUrl")
                        update_spouse_info_divorced_tv.text = if (divorceImgUrl.isNotEmpty()) "已上传" else "请上传"
                    }
                }
                Constants.REQUEST_IDCARD_1_CAPTURE -> {
                    var dialog = LoadingUtils.createLoadingDialog(mContext)
                    dialog.show()

                    OcrUtil.requestOcr(mContext, idBackFile.absolutePath, OSSObjectKeyBean("lender_sp", "id_card_back", ".png"), "id_card", OcrUtil.OnOcrSuccessCallBack { ocrResp, objectKey ->
                        if (ocrResp == null) {
                            dialog.dismiss()
                            Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
                            return@OnOcrSuccessCallBack
                        }
                        SpouseIdCardActivity.ID_BACK_FID = objectKey
                        if (ocrResp.showapi_res_code != 0 && ocrResp.showapi_res_body.idNo.isNullOrEmpty() || ocrResp.showapi_res_body.name.isNullOrEmpty()) {
                            Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
                        } else {
                            update_spouse_info_clt_nm_edt.setText(ocrResp.showapi_res_body.name)
                            update_spouse_info_id_no_edt.setText(ocrResp.showapi_res_body.idNo)
                            update_spouse_info_gender_tv.text = ocrResp.showapi_res_body.sex
                        }
                        dialog.dismiss()
                    }, OnItemDataCallBack<Throwable> {
                        Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })
                }
            }
        }
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


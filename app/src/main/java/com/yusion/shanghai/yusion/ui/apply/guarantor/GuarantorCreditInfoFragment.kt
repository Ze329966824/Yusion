package com.yusion.shanghai.yusion.ui.apply.guarantor

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.bean.ocr.OcrResp
import com.yusion.shanghai.yusion.bean.user.GetGuarantorInfoReq
import com.yusion.shanghai.yusion.event.AddGuarantorActivityEvent
import com.yusion.shanghai.yusion.retrofit.service.ProductApi
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.ui.apply.DocumentActivity
import com.yusion.shanghai.yusion.utils.ContactsUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.guarantor_credit_info.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by ice on 2017/8/21.
 */
class GuarantorCreditInfoFragment : DoubleCheckFragment() {
    var _GUARANTOR_REL_INDEX: Int = 0
    var idBackImgUrl = ""
    var idFrontImgUrl = ""
    var ocrResp = OcrResp.ShowapiResBodyBean()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.guarantor_credit_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDoubleCheckChangeBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
        }
        mDoubleCheckSubmitBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
            ProductApi.getGuarantorInfo(mContext, GetGuarantorInfoReq(guarantor_credit_info_id_number_tv.text.toString(), guarantor_credit_info_name_tv.text.toString()
                    , guarantor_credit_info_rel_tv.text.toString(), guarantor_credit_info_mobile_edt.text.toString())) {
                if (it == null) {
                    return@getGuarantorInfo
                }
                var addGuarantorActivity = activity as AddGuarantorActivity
                addGuarantorActivity.mGuarantorInfo = it
                ocrResp?.let {
                    addGuarantorActivity.mGuarantorInfo.gender = ocrResp.sex
                    addGuarantorActivity.mGuarantorInfo.reg_addr_details = if (TextUtils.isEmpty(ocrResp.addr)) "" else ocrResp.addr
                    addGuarantorActivity.mGuarantorInfo.reg_addr.province = ocrResp.province
                    addGuarantorActivity.mGuarantorInfo.reg_addr.city = ocrResp.city
                    addGuarantorActivity.mGuarantorInfo.reg_addr.district = ocrResp.town
                }
                addGuarantorActivity.mGuarantorInfo.social_ship = YusionApp.CONFIG_RESP.guarantor_relationship_list_key[_GUARANTOR_REL_INDEX]
                nextStep()
//                uploadUrl(it.clt_id)
            }
        }
        guarantor_credit_info_next_btn.setOnClickListener {
            if (checkCanNextStep()) {
                clearDoubleCheckItems()
                addDoubleCheckItem("姓名", guarantor_credit_info_name_tv.text.toString())
                addDoubleCheckItem("身份证号", guarantor_credit_info_id_number_tv.text.toString())
                mDoubleCheckDialog.show()
            }
        }
        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf")
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf")
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf")

        guarantor_credit_info_rel_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.guarantor_relationship_list_key, _GUARANTOR_REL_INDEX, guarantor_credit_info_rel_lin, guarantor_credit_info_rel_tv, "请选择", { _, index ->
                _GUARANTOR_REL_INDEX = index
            })
        }
        guarantor_credit_info_mobile_img.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, Constants.REQUEST_CONTACTS)
        }
        guarantor_credit_info_id_back_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_back")
            intent.putExtra("role", "lender")
            intent.putExtra("imgUrl", idBackImgUrl)
            intent.putExtra("ocrResp", ocrResp)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        guarantor_credit_info_id_front_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_front")
            intent.putExtra("role", "lender")
            intent.putExtra("imgUrl", idFrontImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }

        guarantor_credit_info_name_tv.setText("just Test")
        guarantor_credit_info_id_number_tv.setText("${Date().time}")
    }

    fun checkCanNextStep(): Boolean {
        return true
//        if (!hasIdBackImg) {
//            Toast.makeText(mContext, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show()
//        } else if (!hasIdFrontImg) {
//            Toast.makeText(mContext, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show()
//        } else if (autonym_certify_name_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show()
//        } else if (autonym_certify_id_number_tv.text.isEmpty()) {
//            Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show()
//        } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(autonym_certify_id_number_tv.text.toString())) {
//            Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show()
//        } else {
//            return true
//        }
//        return false
    }

    fun nextStep() {
        EventBus.getDefault().post(AddGuarantorActivityEvent.showGuarantorInfoFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                Constants.REQUEST_DOCUMENT -> {
                    data?.let {
                        when (data.getStringExtra("type")) {
                            "id_card_back" -> {
                                if (!TextUtils.isEmpty(data.getStringExtra("objectKey"))) {
                                    guarantor_credit_info_id_back_tv.text = "已上传"
                                    guarantor_credit_info_id_back_tv.setTextColor(resources.getColor(R.color.system_color))
                                    ocrResp = data.getSerializableExtra("ocrResp") as OcrResp.ShowapiResBodyBean
                                } else {
                                    guarantor_credit_info_id_back_tv.text = "请上传"
                                    guarantor_credit_info_id_back_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                                }
                                idBackImgUrl = data.getStringExtra("imgUrl")
                                guarantor_credit_info_id_number_tv.setText(ocrResp.idNo)
                                guarantor_credit_info_name_tv.setText(ocrResp.name)
                            }
                            "id_card_front" -> {
                                if (!TextUtils.isEmpty(data.getStringExtra("objectKey"))) {
                                    guarantor_credit_info_id_front_tv.text = "已上传"
                                    guarantor_credit_info_id_front_tv.setTextColor(resources.getColor(R.color.system_color))
                                } else {
                                    guarantor_credit_info_id_front_tv.text = "请上传"
                                    guarantor_credit_info_id_front_tv.setTextColor(resources.getColor(R.color.please_upload_color))
                                }
                                idFrontImgUrl = data.getStringExtra("imgUrl")
                            }
                        }
                    }
                }

                Constants.REQUEST_CONTACTS -> {
                    val uri = data.data
                    val contacts = ContactsUtil.getPhoneContacts(mContext, uri)
                    val result = arrayOf("", "")
                    if (contacts != null) {
                        System.arraycopy(contacts, 0, result, 0, contacts.size)
                    }
                    guarantor_credit_info_name_tv.setText(result[0])
                    guarantor_credit_info_mobile_edt.setText(result[1])
                }
            }
        }
    }
}

package com.yusion.shanghai.yusion.ui.apply

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.bean.auth.GetUserInfoReq
import com.yusion.shanghai.yusion.bean.ocr.OcrResp
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.retrofit.service.ProductApi
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.settings.Settings
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.autonym_certify.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by ice on 17/7/5.
 */
class AutonymCertifyFragment : DoubleCheckFragment() {

    companion object {
        var CURRENT_CLICKED_VIEW_FOR_PIC: Int = -1
        var ID_BACK_FID = ""
        var ID_FRONT_FID = ""
        var _DIR_REL_INDEX: Int = 0
    }

    var hasIdFrontImg = false
    var hasIdBackImg = false
    var addr = ""
    var idBackImgUrl = ""
    var idFrontImgUrl = ""
    var drivingLicImgUrl = ""
    var ocrResp = OcrResp.ShowapiResBodyBean()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.autonym_certify, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDoubleCheckChangeBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
        }
        mDoubleCheckSubmitBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
            ProductApi.getClientInfo(mContext, GetUserInfoReq(autonym_certify_id_number_tv.text.toString(), autonym_certify_name_tv.text.toString())) {
                if (it == null) {
                    return@getClientInfo
                }
                var applyActivity = activity as ApplyActivity
                applyActivity.mClientInfo = it
                ocrResp?.let {
                    applyActivity.mClientInfo.gender = ocrResp.sex
                    applyActivity.mClientInfo.reg_addr_details = if (TextUtils.isEmpty(ocrResp.addr)) "" else ocrResp.addr
                    applyActivity.mClientInfo.reg_addr.province = ocrResp.province
                    applyActivity.mClientInfo.reg_addr.city = ocrResp.city
                    applyActivity.mClientInfo.reg_addr.district = ocrResp.town
                }
                nextStep()
//                uploadUrl(it.clt_id)
            }
        }
        autonym_certify_next_btn.setOnClickListener {
            if (checkCanNextStep()) {
                clearDoubleCheckItems()
                addDoubleCheckItem("姓名", autonym_certify_name_tv.text.toString())
                addDoubleCheckItem("身份证号", autonym_certify_id_number_tv.text.toString())
                mDoubleCheckDialog.show()
            }
        }
        autonym_certify_driving_license_rel_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.drv_lic_relationship_list_key, _DIR_REL_INDEX, autonym_certify_driving_license_rel_lin, autonym_certify_driving_license_rel_tv, "请选择", { _, index ->
                _DIR_REL_INDEX = index
            })
        }
        autonym_certify_id_back_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_back")
            intent.putExtra("role", "lender")
            intent.putExtra("imgUrl", idBackImgUrl)
            intent.putExtra("ocrResp", ocrResp)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        autonym_certify_id_front_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "id_card_front")
            intent.putExtra("role", "lender")
            intent.putExtra("imgUrl", idFrontImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        autonym_certify_driving_license_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", "driving_lic")
            intent.putExtra("role", "lender")
            intent.putExtra("imgUrl", drivingLicImgUrl)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");


        autonym_certify_name_tv.setText("just Test")
        autonym_certify_id_number_tv.setText("${Date().time}")
        if (Settings.isShameData) {
            autonym_certify_id_number_tv.setText("513001198707080231")
            hasIdBackImg = true
            hasIdFrontImg = true
            ID_BACK_FID = "test"
            ID_FRONT_FID = "test"
        }

    }

    fun uploadUrl(cltId: String) {
        //上传url到服务器
        val uploadFilesUrlReq = UploadFilesUrlReq()
        uploadFilesUrlReq.clt_id = cltId
        val files = ArrayList<UploadFilesUrlReq.FileUrlBean>()
        val idBackBean = UploadFilesUrlReq.FileUrlBean()
        idBackBean.file_id = ID_BACK_FID
        idBackBean.label = "id_card_back"
        idBackBean.role = "lender"
        val idFrontBean = UploadFilesUrlReq.FileUrlBean()
        idFrontBean.file_id = ID_FRONT_FID
        idFrontBean.label = "id_card_front"
        idFrontBean.role = "lender"
        files.add(idBackBean)
        files.add(idFrontBean)
        uploadFilesUrlReq.files = files
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "")
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "")

        UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq) { code, _ ->
            if (code >= 0) {
                nextStep()
            }
        }
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
        EventBus.getDefault().post(ApplyActivityEvent.showPersonalInfoFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_DOCUMENT -> {
                    data?.let {
                        when (data.getStringExtra("type")) {
                            "id_card_back" -> {
                                if (!TextUtils.isEmpty(data.getStringExtra("objectKey"))) {
                                    autonym_certify_id_back_tv.text = "已上传"
                                    autonym_certify_id_back_tv.setTextColor(resources.getColor(R.color.system_color))
                                    ocrResp = data.getSerializableExtra("ocrResp") as OcrResp.ShowapiResBodyBean
                                    autonym_certify_id_number_tv.setText(ocrResp.idNo)
                                    autonym_certify_name_tv.setText(ocrResp.name)
                                    idBackImgUrl = data.getStringExtra("imgUrl")
                                }
                            }
                            "id_card_front" -> {
                                if (!TextUtils.isEmpty(data.getStringExtra("objectKey"))) {
                                    idFrontImgUrl = data.getStringExtra("imgUrl")
                                    autonym_certify_id_front_tv.text = "已上传"
                                    autonym_certify_id_front_tv.setTextColor(resources.getColor(R.color.system_color))
                                }
                            }
                            "driving_lic" -> {
                                if (!TextUtils.isEmpty(data.getStringExtra("objectKey"))) {
                                    drivingLicImgUrl = data.getStringExtra("imgUrl")
                                    autonym_certify_driving_license_tv.text = "已上传"
                                    autonym_certify_driving_license_tv.setTextColor(resources.getColor(R.color.system_color))
                                }
                            }
                        }
                    }
                }
//                Constants.REQUEST_IDCARD_1_CAPTURE -> {
//                    hasIdBackImg = true
//                    Glide.with(mContext).load(idBackFile).into(autonym_certify_id_back_img)
//
//                    var dialog = LoadingUtils.createLoadingDialog(mContext)
//                    dialog.show()
//                    OcrUtil.requestOcr(mContext, idBackFile.absolutePath, OSSObjectKeyBean("lender", "id_card_back", ".png"), "id_card", OcrUtil.OnOcrSuccessCallBack { ocrResp, objectKey ->
//                        ID_BACK_FID = objectKey
//                        if (ocrResp == null) {
//                            dialog.dismiss()
//                            Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
//                            return@OnOcrSuccessCallBack
//                        }
//                        if (ocrResp.showapi_res_code != 0 && ocrResp.showapi_res_body.idNo.isNullOrEmpty() || ocrResp.showapi_res_body.name.isNullOrEmpty()) {
//                            Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(mContext, "识别成功", Toast.LENGTH_SHORT).show()
//                            autonym_certify_id_number_tv.setText(ocrResp.showapi_res_body.idNo)
//                            autonym_certify_name_tv.setText(ocrResp.showapi_res_body.name)
//                            (activity as ApplyActivity).mOcrRespByAutonymCertify = ocrResp
//                        }
//                        dialog.dismiss()
//                    }, OnItemDataCallBack<Throwable> {
//                        Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
//                        dialog.dismiss()
//                    })
//
//                }
//                Constants.REQUEST_IDCARD_2_CAPTURE -> {
//                    hasIdFrontImg = true
//                    Glide.with(mContext).load(idFrontFile).into(autonym_certify_id_front_img)
//
//                    OssUtil.uploadOss(mContext, true, idFrontFile.absolutePath, OSSObjectKeyBean("lender", "id_card_front", ".png"), OnItemDataCallBack<String> {
//                        ID_FRONT_FID = it
//                    }, null)
//                }
            }
        }
    }
}
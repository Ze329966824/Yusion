package com.yusion.shanghai.yusion.ui.apply

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.bean.auth.GetUserInfoReq
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack
import com.yusion.shanghai.yusion.retrofit.service.ProductApi
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.settings.Settings
import com.yusion.shanghai.yusion.utils.*
import kotlinx.android.synthetic.main.autonym_certify.*
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.util.*

/**
 * Created by ice on 17/7/5.
 */
class AutonymCertifyFragment : DoubleCheckFragment() {

    companion object {
        var CURRENT_CLICKED_VIEW_FOR_PIC: Int = -1
        var ID_BACK_FID = ""
        var ID_FRONT_FID = ""
    }

    var hasIdFrontImg = false
    var hasIdBackImg = false

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
            ProductApi.getUserInfo(mContext, GetUserInfoReq(autonym_certify_id_number_tv.text.toString(), autonym_certify_name_tv.text.toString())) {
                var applyActivity = activity as ApplyActivity
                applyActivity.mUserInfoBean = it
                var body = applyActivity.mOcrRespByAutonymCertify.showapi_res_body
                body?.let {
                    applyActivity.mUserInfoBean.gender = body.sex
                    applyActivity.mUserInfoBean.reg_addr_details = if (body.addr.isEmpty()) "" else body.addr
                    applyActivity.mUserInfoBean.reg_addr.province = body.province
                    applyActivity.mUserInfoBean.reg_addr.city = body.city
                    applyActivity.mUserInfoBean.reg_addr.district = body.town
                }
                uploadUrl(it.clt_id)
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
        autonym_certify_id_back_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_PIC = autonym_certify_id_back_img.id;
            takePhoto()
        }
        autonym_certify_id_front_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_PIC = autonym_certify_id_front_img.id;
            takePhoto()
        }

        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");


        if (Settings.isShameData) {
            autonym_certify_name_tv.setText("${Date().time}")
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
        if (!hasIdBackImg) {
            Toast.makeText(mContext, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show()
        } else if (!hasIdFrontImg) {
            Toast.makeText(mContext, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show()
        } else if (autonym_certify_name_tv.text.isEmpty()) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show()
        } else if (autonym_certify_id_number_tv.text.isEmpty()) {
            Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show()
        } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(autonym_certify_id_number_tv.text.toString())) {
            Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    fun nextStep() {
        EventBus.getDefault().post(ApplyActivityEvent.showPersonalInfoFragment)
    }

    private var idBackFile = File("");
    private var idFrontFile = File("");

    fun takePhoto() {
        when (CURRENT_CLICKED_VIEW_FOR_PIC) {
            autonym_certify_id_back_img.id -> {
                idBackFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + ".jpg")
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(idBackFile))
                startActivityForResult(intent, Constants.REQUEST_IDCARD_1_CAPTURE)
            }
            autonym_certify_id_front_img.id -> {
                idFrontFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + ".jpg")
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(idFrontFile))
                startActivityForResult(intent, Constants.REQUEST_IDCARD_2_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_IDCARD_1_CAPTURE -> {
                    hasIdBackImg = true
                    Glide.with(mContext).load(idBackFile).into(autonym_certify_id_back_img)

                    var dialog = LoadingUtils.createLoadingDialog(mContext)
                    dialog.show()
                    OcrUtil.requestOcr(mContext, idBackFile.absolutePath, OSSObjectKeyBean("lender", "id_card_back", ".png"), "id_card", OcrUtil.OnOcrSuccessCallBack { ocrResp, objectKey ->
                        if (ocrResp == null) {
                            dialog.dismiss()
                            Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
                            return@OnOcrSuccessCallBack
                        }
                        ID_BACK_FID = objectKey
                        if (ocrResp.showapi_res_code != 0 && ocrResp.showapi_res_body.idNo.isNullOrEmpty() || ocrResp.showapi_res_body.name.isNullOrEmpty()) {
                            Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(mContext, "识别成功", Toast.LENGTH_SHORT).show()
                            autonym_certify_id_number_tv.setText(ocrResp.showapi_res_body.idNo)
                            autonym_certify_name_tv.setText(ocrResp.showapi_res_body.name)
                            (activity as ApplyActivity).mOcrRespByAutonymCertify = ocrResp
                        }
                        dialog.dismiss()
                    }, OnItemDataCallBack<Throwable> {
                        Toast.makeText(mContext, "识别失败", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })

                }
                Constants.REQUEST_IDCARD_2_CAPTURE -> {
                    hasIdFrontImg = true
                    Glide.with(mContext).load(idFrontFile).into(autonym_certify_id_front_img)

                    OssUtil.uploadOss(mContext, true, idFrontFile.absolutePath, OSSObjectKeyBean("lender", "id_card_front", ".png"), OnItemDataCallBack<String> {
                        ID_FRONT_FID = it
                    }, null)
                }
            }
        }
    }
}
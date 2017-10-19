package com.yusion.shanghai.yusion.ui.apply

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.YusionApp
import com.yusion.shanghai.yusion.base.DoubleCheckFragment
import com.yusion.shanghai.yusion.bean.ocr.OcrResp
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq
import com.yusion.shanghai.yusion.bean.user.GetClientInfoReq
import com.yusion.shanghai.yusion.event.ApplyActivityEvent
import com.yusion.shanghai.yusion.retrofit.api.ProductApi
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.settings.Settings
import com.yusion.shanghai.yusion.ubt.UBT
import com.yusion.shanghai.yusion.ubt.annotate.BindView
import com.yusion.shanghai.yusion.ui.upload.img.DocumentActivity
import com.yusion.shanghai.yusion.utils.CheckIdCardValidUtil
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil
import kotlinx.android.synthetic.main.autonym_certify.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by ice on 17/7/5.
 */
/**
 * 通过ID_BACK_FID ID_FRONT_FID DRI_FID 判断是否已上传图片至oss
 */
class AutonymCertifyFragment : DoubleCheckFragment() {

    var _DIR_REL_INDEX: Int = 0

    var ID_BACK_FID = ""
    var ID_FRONT_FID = ""
    var DRI_FID = ""

    var drivingLicImgUrl = ""
    var idBackImgUrl = ""
    var idFrontImgUrl = ""

    var ocrResp = OcrResp.ShowapiResBodyBean()


    @BindView(id = R.id.autonym_certify_id_back_tv, widgetName = "autonym_certify_id_back_tv")
    var autonym_certify_id_back_tv: TextView? = null

    @BindView(id = R.id.autonym_certify_id_front_tv, widgetName = "autonym_certify_id_front_tv")
    var autonym_certify_id_front_tv: TextView? = null

    @BindView(id = R.id.autonym_certify_name_tv, widgetName = "autonym_certify_name_tv")
    var autonym_certify_name_tv: EditText? = null

    @BindView(id = R.id.autonym_certify_id_number_tv, widgetName = "autonym_certify_id_number_tv")
    var autonym_certify_id_number_tv: EditText? = null

    @BindView(id = R.id.autonym_certify_driving_license_tv, widgetName = "autonym_certify_driving_license_tv")
    var autonym_certify_driving_license_tv: TextView? = null

    @BindView(id = R.id.autonym_certify_driving_license_rel_tv, widgetName = "autonym_certify_driving_license_rel_tv")
    var autonym_certify_driving_license_rel_tv: TextView? = null

    @BindView(id = R.id.autonym_certify_next_btn, widgetName = "autonym_certify_next_btn", onClick = "submitAutonymCertify")
    var autonym_certify_next_btn: Button? = null


    fun submitAutonymCertify(view: View?) {
        (autonym_certify_next_btn as Button).setFocusable(true)
        (autonym_certify_next_btn as Button).setFocusableInTouchMode(true)
        (autonym_certify_next_btn as Button).requestFocus()
        (autonym_certify_next_btn as Button).requestFocusFromTouch()
    }

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    ViewCompat.animate(autonym_certify_warnning_lin).translationY(-autonym_certify_warnning_lin.height * 1.0f).setDuration(1000).start()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.autonym_certify, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UBT.bind(this, view, ApplyActivity::class.java.getSimpleName())
        mDoubleCheckChangeBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
        }
        mDoubleCheckSubmitBtn.setOnClickListener {
            mDoubleCheckDialog.dismiss()
            ProductApi.getClientInfo(mContext, GetClientInfoReq(autonym_certify_id_number_tv?.text.toString(), autonym_certify_name_tv?.text.toString(), "1")) {
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
                applyActivity.mClientInfo.drv_lic_relationship = YusionApp.CONFIG_RESP.drv_lic_relationship_list_value[_DIR_REL_INDEX]
                uploadUrl(it.clt_id)
//                nextStep()
            }
        }

        (autonym_certify_next_btn as Button).setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                (autonym_certify_next_btn as Button).clearFocus();
                if (checkCanNextStep()) {
                    clearDoubleCheckItems()
                    addDoubleCheckItem("姓名", autonym_certify_name_tv?.text.toString())
                    addDoubleCheckItem("身份证号", autonym_certify_id_number_tv?.text.toString())
                    mDoubleCheckDialog.show()
                }
            }
        }
        autonym_certify_driving_license_rel_lin.setOnClickListener {
            WheelViewUtil.showWheelView<String>(YusionApp.CONFIG_RESP.drv_lic_relationship_list_key, _DIR_REL_INDEX, autonym_certify_driving_license_rel_lin, autonym_certify_driving_license_rel_tv, "请选择", { _, index ->
                _DIR_REL_INDEX = index
            })
        }
        autonym_certify_id_back_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.ID_BACK)
            intent.putExtra("role", Constants.PersonType.LENDER)
            intent.putExtra("imgUrl", idBackImgUrl)
            intent.putExtra("needUploadFidToServer", false)
            intent.putExtra("objectKey", ID_BACK_FID)
            intent.putExtra("ocrResp", ocrResp)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        autonym_certify_id_front_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.ID_FRONT)
            intent.putExtra("role", Constants.PersonType.LENDER)
            intent.putExtra("imgUrl", idFrontImgUrl)
            intent.putExtra("needUploadFidToServer", false)
            intent.putExtra("objectKey", ID_FRONT_FID)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        autonym_certify_driving_license_lin.setOnClickListener {
            var intent = Intent(mContext, DocumentActivity::class.java)
            intent.putExtra("type", Constants.FileLabelType.DRI_LIC)
            intent.putExtra("role", Constants.PersonType.LENDER)
            intent.putExtra("imgUrl", drivingLicImgUrl)
            intent.putExtra("needUploadFidToServer", false)
            intent.putExtra("objectKey", DRI_FID)
            startActivityForResult(intent, Constants.REQUEST_DOCUMENT)
        }
        step1.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step2.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");
        step3.typeface = Typeface.createFromAsset(mContext.assets, "yj.ttf");

        if (Settings.isShameData) {
            autonym_certify_name_tv?.setText("just Test")
            autonym_certify_id_number_tv?.setText("${Date().time}")
            autonym_certify_id_number_tv?.setText("513001198707080231")
            ID_BACK_FID = "test"
            ID_FRONT_FID = "test"
        }

        autonym_certify_warnning_lin.post {
            handler.sendEmptyMessageDelayed(0, 2000)
        }

    }

    fun uploadUrl(cltId: String) {
        val idBackBean = UploadFilesUrlReq.FileUrlBean()
        idBackBean.file_id = ID_BACK_FID
        idBackBean.label = Constants.FileLabelType.ID_BACK
        idBackBean.clt_id = cltId

        val idFrontBean = UploadFilesUrlReq.FileUrlBean()
        idFrontBean.file_id = ID_FRONT_FID
        idFrontBean.label = Constants.FileLabelType.ID_FRONT
        idFrontBean.clt_id = cltId

        val driBean = UploadFilesUrlReq.FileUrlBean()
        driBean.file_id = DRI_FID
        driBean.label = Constants.FileLabelType.DRI_LIC
        driBean.clt_id = cltId

        val files = ArrayList<UploadFilesUrlReq.FileUrlBean>()
        files.add(idBackBean)
        files.add(idFrontBean)
        files.add(driBean)

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

    fun checkCanNextStep(): Boolean {
//        return true
        if (ID_BACK_FID.isEmpty()) {
            Toast.makeText(mContext, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show()
        } else if (ID_FRONT_FID.isEmpty()) {
            Toast.makeText(mContext, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show()
        } else if (DRI_FID.isEmpty()) {
            Toast.makeText(mContext, "请拍摄驾照影像件", Toast.LENGTH_SHORT).show()
        } else if ((autonym_certify_name_tv as EditText).text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show()
        } else if ((autonym_certify_id_number_tv as EditText).text.toString().isEmpty()) {
            Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show()
        } else if (!CheckIdCardValidUtil.isValidatedAllIdcard((autonym_certify_id_number_tv as EditText).text.toString())) {
            Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show()
        } else if ((autonym_certify_driving_license_rel_tv as TextView).text.toString().isEmpty()) {
            Toast.makeText(mContext, "请选择驾照证持有人与本人关系", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
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
                            Constants.FileLabelType.ID_BACK -> {
                                ID_BACK_FID = data.getStringExtra("objectKey")
                                idBackImgUrl = data.getStringExtra("imgUrl")
                                if (ID_BACK_FID.isNotEmpty()) {
                                    autonym_certify_id_back_tv?.text = "已上传"
                                    autonym_certify_id_back_tv?.setTextColor(resources.getColor(R.color.system_color))
                                    ocrResp = data.getSerializableExtra("ocrResp") as OcrResp.ShowapiResBodyBean
                                } else {
                                    autonym_certify_id_back_tv?.text = "请上传"
                                    autonym_certify_id_back_tv?.setTextColor(resources.getColor(R.color.please_upload_color))
                                }
                                if (ocrResp.idNo.isNotEmpty()) {
                                    autonym_certify_id_number_tv?.setText(ocrResp.idNo)
                                }
                                if (ocrResp.name.isNotEmpty()) {
                                    autonym_certify_name_tv?.setText(ocrResp.name)
                                } else {

                                }
                            }
                            Constants.FileLabelType.ID_FRONT -> {
                                ID_FRONT_FID = data.getStringExtra("objectKey")
                                idFrontImgUrl = data.getStringExtra("imgUrl")
                                if (ID_FRONT_FID.isNotEmpty()) {
                                    autonym_certify_id_front_tv?.text = "已上传"
                                    autonym_certify_id_front_tv?.setTextColor(resources.getColor(R.color.system_color))
                                } else {
                                    autonym_certify_id_front_tv?.text = "请上传"
                                    autonym_certify_id_front_tv?.setTextColor(resources.getColor(R.color.please_upload_color))
                                }
                            }
                            Constants.FileLabelType.DRI_LIC -> {
                                DRI_FID = data.getStringExtra("objectKey")
                                drivingLicImgUrl = data.getStringExtra("imgUrl")
                                if (DRI_FID.isNotEmpty()) {
                                    autonym_certify_driving_license_tv?.text = "已上传"
                                    autonym_certify_driving_license_tv?.setTextColor(resources.getColor(R.color.system_color))
                                } else {
                                    autonym_certify_driving_license_tv?.text = "请上传"
                                    autonym_certify_driving_license_tv?.setTextColor(resources.getColor(R.color.please_upload_color))
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }
}
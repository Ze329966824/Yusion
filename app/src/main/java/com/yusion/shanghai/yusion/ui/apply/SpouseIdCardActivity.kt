package com.yusion.shanghai.yusion.ui.apply

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.bumptech.glide.Glide
import com.yusion.shanghai.yusion.R
import com.yusion.shanghai.yusion.base.BaseActivity
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq
import com.yusion.shanghai.yusion.retrofit.api.UploadApi
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack
import com.yusion.shanghai.yusion.settings.Constants
import com.yusion.shanghai.yusion.utils.OcrUtil
import com.yusion.shanghai.yusion.utils.OssUtil
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil
import kotlinx.android.synthetic.main.activity_spouse_id_card.*
import java.io.File

class SpouseIdCardActivity : BaseActivity() {

    companion object {
        var CURRENT_CLICKED_VIEW_FOR_PIC: Int = -1
        var ID_BACK_FID = ""
        var ID_FRONT_FID = ""
    }

    private var idNo = ""
    private var name = ""
    private var addr = ""
    private var idBackImgUrl = ""
    private var idFrontImgUrl = ""
    private var hasUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spouse_id_card)

        initTitleBar(this, "配偶身份证").setLeftClickListener { onBack() }
        idBackImgUrl = intent.getStringExtra("idBackImgUrl")
        idFrontImgUrl = intent.getStringExtra("idFrontImgUrl")
        if (idBackImgUrl.isNotEmpty()) {
            Glide.with(this).load(File(idBackImgUrl)).into(spouse_id_card_id_back_img)
        }
        if (idFrontImgUrl.isNotEmpty()) {
            Glide.with(this).load(File(idFrontImgUrl)).into(spouse_id_card_id_front_img)
        }
        spouse_id_card_id_back_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_PIC = spouse_id_card_id_back_img.id;
            takePhoto()
        }
        spouse_id_card_id_front_img.setOnClickListener {
            CURRENT_CLICKED_VIEW_FOR_PIC = spouse_id_card_id_front_img.id;
            takePhoto()
        }
    }

    override fun onBackPressed() {
        onBack()
    }

    fun onBack() {
        if (idBackImgUrl.isEmpty()) {
            Toast.makeText(this, "请拍摄身份证人像面", Toast.LENGTH_SHORT).show()
            return
        } else if (idFrontImgUrl.isEmpty()) {
            Toast.makeText(this, "请拍摄身份证国徽面", Toast.LENGTH_SHORT).show()
            return
        }

        val uploadFilesUrlReq = UploadFilesUrlReq()
        uploadFilesUrlReq.clt_id = intent.getStringExtra("clt_id")
        val files = ArrayList<UploadFilesUrlReq.FileUrlBean>()
        val idBackBean = UploadFilesUrlReq.FileUrlBean()
        idBackBean.file_id = ID_BACK_FID
        idBackBean.label = "id_card_back"
        idBackBean.role = "lender_sp"
        val idFrontBean = UploadFilesUrlReq.FileUrlBean()
        idFrontBean.file_id = ID_FRONT_FID
        idFrontBean.label = "id_card_front"
        idFrontBean.role = "lender_sp"
        files.add(idBackBean)
        files.add(idFrontBean)
        uploadFilesUrlReq.files = files
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(this).getValue("region", "")
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(this).getValue("bucket", "")

        var dialog = AlertDialog.Builder(this).setMessage("请稍等...").create()
        dialog.show()
        UploadApi.uploadFileUrl(this, uploadFilesUrlReq) { code, _ ->
            if (code == 0) {
                dialog.dismiss()

                var intent = Intent()
                intent.putExtra("idNo", idNo)
                intent.putExtra("name", name)
                intent.putExtra("addr", addr)
                intent.putExtra("idBackImgUrl", idBackImgUrl)
                intent.putExtra("idFrontImgUrl", idFrontImgUrl)
                intent.putExtra("hasUpdate", hasUpdate)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

    }

    private var idBackFile = File("");
    private var idFrontFile = File("");
    fun takePhoto() {
        when (CURRENT_CLICKED_VIEW_FOR_PIC) {
            spouse_id_card_id_back_img.id -> {
                idBackFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis().toString() + ".jpg")
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(idBackFile))
                startActivityForResult(intent, Constants.REQUEST_IDCARD_1_CAPTURE)
            }
            spouse_id_card_id_front_img.id -> {
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
                    idBackImgUrl = idBackFile.absolutePath
                    Glide.with(this).load(idBackFile).into(spouse_id_card_id_back_img)

                    val dialog = ProgressDialog(this)
                    dialog.setMessage("正在识别身份证...")
                    dialog.setCancelable(false)
                    dialog.show()

                    OcrUtil.requestOcr(this, idBackFile.absolutePath, OSSObjectKeyBean("lender_sp", "id_card_back", ".png"), "id_card", OcrUtil.OnOcrSuccessCallBack { ocrResp, objectKey ->
                        if (ocrResp == null) {
                            dialog.dismiss()
                            Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show()
                            return@OnOcrSuccessCallBack
                        }
                        ID_BACK_FID = objectKey
                        if (ocrResp.showapi_res_code != 0 && ocrResp.showapi_res_body.idNo.isNullOrEmpty() || ocrResp.showapi_res_body.name.isNullOrEmpty()) {
                            Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show()
                        } else {
                            idNo = ocrResp.showapi_res_body.idNo
                            name = ocrResp.showapi_res_body.name
                            addr = ocrResp.showapi_res_body.addr
                            hasUpdate = true
                        }
                        dialog.dismiss()
                    }, OnItemDataCallBack<Throwable> {
                        Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })
                }
                Constants.REQUEST_IDCARD_2_CAPTURE -> {
                    idFrontImgUrl = idFrontFile.absolutePath
                    Glide.with(this).load(idFrontFile).into(spouse_id_card_id_front_img)

                    val dialog = ProgressDialog(this)
                    dialog.setCancelable(false)
                    dialog.show()
                    OssUtil.uploadOss(this, false, idFrontFile.absolutePath, OSSObjectKeyBean("lender_sp", "id_card_front", ".png"), OnItemDataCallBack<String> {
                        dialog.dismiss()
                        ID_FRONT_FID = it
                    }, OnItemDataCallBack<Throwable> {
                        dialog.dismiss()
                    })
                }
            }
        }
    }
}


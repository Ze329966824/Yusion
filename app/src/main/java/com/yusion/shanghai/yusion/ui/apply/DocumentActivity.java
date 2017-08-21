package com.yusion.shanghai.yusion.ui.apply;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OcrUtil;
import com.yusion.shanghai.yusion.utils.OssUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import java.io.File;
import java.util.ArrayList;

import static com.yusion.shanghai.yusion.R.layout.activity_document;

public class DocumentActivity extends BaseActivity {
    ImageView takePhoto;
    private File imageFile;

    private String title;
    private String mImgUrl;
    private String mType;
    private String mCltId;
    private String mRole;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mGetIntent = getIntent();

        mType = mGetIntent.getStringExtra("type");
        mCltId = mGetIntent.getStringExtra("clt_id");
        mRole = mGetIntent.getStringExtra("role");

        mImgUrl = mGetIntent.getStringExtra("imgUrl");//重点看驾照的时候会判断是否为空

        LayoutInflater inFlater = getLayoutInflater();

        //判断传过来的type 如果是 身份证，则直接拍照，不能调用本地文件，如果是驾驶证和授权书，
//        initTitleBar(this, title).setLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBack();
//            }
//        });
        if (mType.equals("auth_credit")) {//征信授权书
            view = inFlater.inflate(R.layout.activity_authorize, null);
        } else if (mType.equals("id_card_front")) {//身份证正面
            view = inFlater.inflate(R.layout.activity_shengfz, null);
        } else if (mType.equals("id_card_back")) {//身份证反面
            view = inFlater.inflate(R.layout.activity_document, null);
        } else if (mType.equals("driving_lic")) {//驾驶证
            view = inFlater.inflate(R.layout.activity_drive, null);
        }

        setContentView(view);

        getTitleInfo();

        takePhoto = (ImageView) findViewById(R.id.camera_document);
        doEvent();
    }

    private void doEvent() {
        //判断类型 如果是身份证则 直接拍照，如果不是，则打开本地的数据
        if (mType.equals("id_card_front")) {//身份证正面
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    startActivityForResult(intent, 3000);//正面3000，反面3001，授权书3002
                }
            });
        } else if (mType.equals("id_card_back")) {//身份证反面
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                    startActivityForResult(intent, 3001);
                }
            });

        } else if (mType.equals("auth_credit")) {//授权书
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3002);

        } else if (mType.equals("driving_lic")) {//驾照
            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(DocumentActivity.this, PhotoMediaActivity.class);
                    i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());//加载类型
                    startActivityForResult(i, 100);
                }
            });
        }
    }


//        if (!TextUtils.isEmpty(mImgUrl)) { //第二次点进来的时候
//            Dialog dialog = LoadingUtils.createLoadingDialog(this);
//            dialog.show();
//            Glide.with(this).load(mImgUrl).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    dialog.dismiss();
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    dialog.dismiss();
//                    return false;
//                }
//            }).into(mImg);
//        }
    //  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {//驾驶证
                ArrayList<String> files = data.getStringArrayListExtra("files");
                String localUrl = files.get(0);
                Glide.with(this).load(localUrl).into(takePhoto);
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                OssUtil.uploadOss(this, false, localUrl, new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String objectKey) {
                        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                        uploadFilesUrlReq.clt_id = mCltId;
                        ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
                        UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                        fileUrlBean.file_id = objectKey;
                        fileUrlBean.label = mType;
                        fileUrlBean.role = mRole;
                        files.add(fileUrlBean);

                        uploadFilesUrlReq.files = files;
                        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("region", "");
                        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("bucket", "");
                        UploadApi.uploadFileUrl(DocumentActivity.this, uploadFilesUrlReq, (code, msg) -> {
                            dialog.dismiss();
                            if (code >= 0) {
                                mImgUrl = localUrl;
                            }
                        });
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        dialog.dismiss();
                    }
                });
            } else if (requestCode == 3000) {//正面识别 上传
                Glide.with(this).load(imageFile).into(takePhoto);
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                OcrUtil.requestOcr(this, imageFile.getAbsolutePath(), new OSSObjectKeyBean("lender", "id_card_back", ".png"), "id_card", new OcrUtil.OnOcrSuccessCallBack() {
                    @Override
                    public void OnOcrSuccess(OcrResp ocrResp, String objectKey) {
                        if (ocrResp == null) {
                            dialog.dismiss();
                            Toast.makeText(DocumentActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (ocrResp.showapi_res_code != 0 && TextUtils.isEmpty(ocrResp.showapi_res_body.idNo) || TextUtils.isEmpty(ocrResp.showapi_res_body.name)) {
                            Toast.makeText(DocumentActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(DocumentActivity.this, "识别成功", Toast.LENGTH_LONG).show();
                            //调用上传图片的方法
                            UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                            uploadFilesUrlReq.clt_id = mCltId;
                            ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList();
                            //UploadFilesUrlReq.FileUrlBean
                            UploadFilesUrlReq.FileUrlBean idFrontBean = new UploadFilesUrlReq.FileUrlBean();
                            idFrontBean.label = "id_card_front";
                            idFrontBean.role = "lender";
                            files.add(idFrontBean);
                            uploadFilesUrlReq.files = files;

                            uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("region", "");
                            uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("bucket", "");
                            UploadApi.uploadFileUrl(DocumentActivity.this, uploadFilesUrlReq, new OnCodeAndMsgCallBack() {
                                @Override
                                public void callBack(int code, String msg) {
                                    if (code >= 0) {
                                        mImgUrl = imageFile.getAbsolutePath();
                                        dialog.dismiss();
                                    }
                                }
                            });

                        }
                        dialog.dismiss();
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        Toast.makeText(DocumentActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

            } else if (requestCode == 3001) {//背面识别上传
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentActivity.this).load(imageFile).into(takePhoto);
                OssUtil.uploadOss(DocumentActivity.this, false, imageFile.getAbsolutePath(), new OSSObjectKeyBean("lender", "id_card_front", ".png"), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String data) {
                        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                        uploadFilesUrlReq.clt_id = mCltId;
                        ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList();
                        //UploadFilesUrlReq.FileUrlBean
                        UploadFilesUrlReq.FileUrlBean idBackBean = new UploadFilesUrlReq.FileUrlBean();
                        idBackBean.label = "id_card_back";
                        idBackBean.role = "lender";
                        files.add(idBackBean);
                        uploadFilesUrlReq.files = files;

                        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("region", "");
                        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("bucket", "");
                        UploadApi.uploadFileUrl(DocumentActivity.this, uploadFilesUrlReq, new OnCodeAndMsgCallBack() {
                            @Override
                            public void callBack(int code, String msg) {
                                if (code >= 0) {

                                    mImgUrl = imageFile.getAbsolutePath();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        dialog.dismiss();
                    }
                });
            } else if (requestCode == 3002) {//授权书 直接拍摄上传
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentActivity.this).load(imageFile).into(takePhoto);
                OssUtil.uploadOss(DocumentActivity.this, false, imageFile.getAbsolutePath(), new OSSObjectKeyBean("lender", "auth_credit", ".png"), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String data) {
                        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                        uploadFilesUrlReq.clt_id = mCltId;
                        ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
                        UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                        // fileUrlBean.file_id = sqs1Url;
                        fileUrlBean.label = "auth_credit";
                        fileUrlBean.role = "lender";
                        files.add(fileUrlBean);

                        uploadFilesUrlReq.files = files;
                        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("region", "");
                        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(DocumentActivity.this).getValue("bucket", "");
                        UploadApi.uploadFileUrl(DocumentActivity.this, uploadFilesUrlReq, new OnCodeAndMsgCallBack() {
                            @Override
                            public void callBack(int code, String msg) {
                                if (code >= 0) {
                                    //
                                    mImgUrl = imageFile.getAbsolutePath();
                                    dialog.dismiss();
                                }
                            }
                        });
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {

                    }
                });
            }

            // Glide.with(this).load(imageFile).into(takePhoto);
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        Intent intent = new Intent();
        intent.putExtra("imgUrl", mImgUrl);//驾照用回传的url判断了是否显示已经上传
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getTitleInfo() {
        if (mType.equals("auth_credit")) {//征信授权书
            initTitleBar(this, "征信授权书");
        } else if (mType.equals("id_card_front")) {//身份证正面
            initTitleBar(this, "身份证正面");
        } else if (mType.equals("id_card_back")) {//身份证反面
            initTitleBar(this, "身份证反面");
        } else if (mType.equals("driving_lic")) {//驾驶证
            initTitleBar(this, "驾驶证");
        }
    }
}

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
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OcrUtil;
import com.yusion.shanghai.yusion.utils.OssUtil;

import java.io.File;
import java.util.ArrayList;

public class DocumentActivity extends BaseActivity {
    ImageView takePhoto;
    private File imageFile;
    private String mType;//id_card_front  id_card_back  driving_lic  auth_credit
    private String mRole;//lender lender_sp guarantor guarantor_sp
    private String mImgObjectKey;
    private View view;
    private String idNo;
    private String name;
    private String addr;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mGetIntent = getIntent();
        mType = mGetIntent.getStringExtra("type");
        mRole = mGetIntent.getStringExtra("role");
        LayoutInflater inFlater = getLayoutInflater();
        if (mType.equals("auth_credit")) {//征信授权书
            view = inFlater.inflate(R.layout.activity_authorize, null);
        } else if (mType.equals("id_card_front")) {//身份证正面
            view = inFlater.inflate(R.layout.activity_document, null);
        } else if (mType.equals("id_card_back")) {//身份证反面
            view = inFlater.inflate(R.layout.activity_shengfz, null);
        } else if (mType.equals("driving_lic")) {//驾驶证
            view = inFlater.inflate(R.layout.activity_drive, null);
        }
        setContentView(view);
        getTitleInfo();
        takePhoto = (ImageView) findViewById(R.id.camera_document);
        if (!mGetIntent.getStringExtra("imgUrl").isEmpty()) {
            Glide.with(this).load(mGetIntent.getStringExtra("imgUrl")).into(takePhoto);
        }
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
                        imgUrl = localUrl;
                        mImgObjectKey = objectKey;
                        dialog.dismiss();
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        Toast.makeText(DocumentActivity.this, "上传图片异常", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            } else if (requestCode == 3001) {//id_back
                Glide.with(this).load(imageFile).into(takePhoto);
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                OcrUtil.requestOcr(this, imageFile.getAbsolutePath(), new OSSObjectKeyBean(mRole, mType, ".png"), "id_card", new OcrUtil.OnOcrSuccessCallBack() {
                    @Override
                    public void OnOcrSuccess(OcrResp ocrResp, String objectKey) {
                        if (ocrResp == null) {
                            Toast.makeText(DocumentActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            return;
                        } else if (ocrResp.showapi_res_code != 0 && TextUtils.isEmpty(ocrResp.showapi_res_body.idNo) || TextUtils.isEmpty(ocrResp.showapi_res_body.name)) {
                            Toast.makeText(DocumentActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            return;
                        } else {
                            Toast.makeText(DocumentActivity.this, "识别成功", Toast.LENGTH_LONG).show();
                            imgUrl = imageFile.getAbsolutePath();
                            mImgObjectKey = objectKey;
                            addr = ocrResp.showapi_res_body.addr;
                            name = ocrResp.showapi_res_body.name;
                            idNo = ocrResp.showapi_res_body.idNo;
                            dialog.dismiss();
                        }
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        Toast.makeText(DocumentActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

            } else if (requestCode == 3000) {//id_front
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentActivity.this).load(imageFile).into(takePhoto);
                OssUtil.uploadOss(DocumentActivity.this, false, imageFile.getAbsolutePath(), new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String objectKey) {
                        imgUrl = imageFile.getAbsolutePath();
                        mImgObjectKey = objectKey;
                        dialog.dismiss();
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        Toast.makeText(DocumentActivity.this, "上传图片异常", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            } else if (requestCode == 3002) {//授权书 直接拍摄上传
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentActivity.this).load(imageFile).into(takePhoto);
                OssUtil.uploadOss(DocumentActivity.this, false, imageFile.getAbsolutePath(), new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String objectKey) {
                        imgUrl = imageFile.getAbsolutePath();
                        mImgObjectKey = objectKey;
                        dialog.dismiss();
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        Toast.makeText(DocumentActivity.this, "上传图片异常", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        Intent intent = new Intent();
        intent.putExtra("objectKey", mImgObjectKey);
        intent.putExtra("type", mType);
        intent.putExtra("name", name);
        intent.putExtra("idNo", idNo);
        intent.putExtra("addr", addr);
        intent.putExtra("imgUrl", imgUrl);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getTitleInfo() {
        if (mType.equals("auth_credit")) {//征信授权书
            initTitleBar(this, "征信授权书").setLeftClickListener(v -> onBack());
        } else if (mType.equals("id_card_front")) {//身份证正面
            initTitleBar(this, "身份证国徽面").setLeftClickListener(v -> onBack());
            ;
        } else if (mType.equals("id_card_back")) {//身份证反面
            initTitleBar(this, "身份证人像面").setLeftClickListener(v -> onBack());
            ;
        } else if (mType.equals("driving_lic")) {//驾驶证
            initTitleBar(this, "驾驶证影像件").setLeftClickListener(v -> onBack());
            ;
        }
    }
}

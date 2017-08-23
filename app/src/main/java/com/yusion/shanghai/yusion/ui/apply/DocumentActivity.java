package com.yusion.shanghai.yusion.ui.apply;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;

public class DocumentActivity extends BaseActivity {
    ImageView choose_icon;
    ImageView true_choose_icon;
    ImageView takePhoto;
    Button delete_image_btn;
    private File imageFile;
    private String mType;//id_card_front  id_card_back  driving_lic  auth_credit
    private String mRole;//lender lender_sp guarantor guarantor_sp
    private String mImgObjectKey;
    private View view;
    private OcrResp.ShowapiResBodyBean mOcrResp;
    private String imgUrl = "";
    TitleBar titleBar;
    Intent mGetIntent;

    private boolean isHasImage = false;
    private boolean isEdit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetIntent = getIntent();
        mType = mGetIntent.getStringExtra("type");
        mRole = mGetIntent.getStringExtra("role");
        LayoutInflater inFlater = getLayoutInflater();
        if (mType.equals("auth_credit")) {//征信授权书
            view = inFlater.inflate(R.layout.activity_authorize, null);
        } else if (mType.equals("id_card_front")) {//身份证国徽面
            view = inFlater.inflate(R.layout.activity_document, null);
        } else if (mType.equals("id_card_back")) {//身份证人像面
            view = inFlater.inflate(R.layout.activity_shengfz, null);
            mOcrResp = ((OcrResp.ShowapiResBodyBean) mGetIntent.getSerializableExtra("ocrResp"));
        } else if (mType.equals("driving_lic")) {//驾驶证
            view = inFlater.inflate(R.layout.activity_drive, null);
        }
        setContentView(view);

        getTitleInfo();

        delete_image_btn = (Button) findViewById(R.id.image_update_btn);
        choose_icon = (ImageView) findViewById(R.id.choose_icon);
        true_choose_icon = (ImageView) findViewById(R.id.true_choose_icon);
        takePhoto = (ImageView) findViewById(R.id.camera_document);
        if (!TextUtils.isEmpty(mGetIntent.getStringExtra("imgUrl"))) {
            Glide.with(this).load(mGetIntent.getStringExtra("imgUrl")).into(takePhoto);
            isHasImage = true;
        } else {
            isHasImage = false;
        }
        doEvent();

        deleteImage();

    }

    private void doEvent() {

        //判断类型 如果是身份证则 直接拍照，如果不是，则打开本地的数据
        if (mType.equals("id_card_front")) {//身份证正面
            // deleteImage();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {//驾驶证
                ArrayList<String> files = data.getStringArrayListExtra("files");
                String localUrl = files.get(0);
                Glide.with(this).load(localUrl).into(takePhoto);
                isHasImage = true;
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
                isHasImage = true;
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
                            mOcrResp = ocrResp.showapi_res_body;
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
                isHasImage = true;
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
                isHasImage = true;
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
        intent.putExtra("ocrResp", mOcrResp);
        intent.putExtra("imgUrl", imgUrl);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getTitleInfo() {
        if (mType.equals("auth_credit")) {//征信授权书
            titleBar = initTitleBar(this, "征信授权书").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);
        } else if (mType.equals("id_card_front")) {//身份证国徽面
            titleBar = initTitleBar(this, "身份证国徽面").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);

        } else if (mType.equals("id_card_back")) {//身份证人像面
            titleBar = initTitleBar(this, "身份证人像面").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);

        } else if (mType.equals("driving_lic")) {//驾驶证
            titleBar = initTitleBar(this, "驾驶证影像件").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);
        }
    }

    private void deleteImage() {
//        if (mType.equals("id_card_back")) {
//            titleBar = initTitleBar(DocumentActivity.this, "身份证人像面");
//        }
        titleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasImage) {//有图
                    if (isEdit) {
                        isEdit = false;
                        takePhoto.setEnabled(false);
                        choose_icon.setVisibility(View.VISIBLE);
                        true_choose_icon.setVisibility(View.GONE);
                        delete_image_btn.setVisibility(View.VISIBLE);
                        titleBar.setRightText("取消");
                        choose_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                choose_icon.setVisibility(View.GONE);
                                true_choose_icon.setVisibility(View.VISIBLE);
                                delete_image_btn.setTextColor(Color.parseColor("#222A36"));
                                delete_image_btn.setEnabled(true);
                                delete_image_btn.setVisibility(View.VISIBLE);
                            }
                        });
                        true_choose_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                choose_icon.setVisibility(View.VISIBLE);
                                true_choose_icon.setVisibility(View.GONE);
                                delete_image_btn.setEnabled(false);
                                delete_image_btn.setTextColor(Color.parseColor("#ffffff"));
                                delete_image_btn.setVisibility(View.GONE);
                            }
                        });
                        delete_image_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete_image_btn.setVisibility(View.GONE);
                                choose_icon.setVisibility(View.GONE);
                                true_choose_icon.setVisibility(View.GONE);
                                titleBar.setRightText("编辑");
                                takePhoto.setEnabled(true);
                                Glide.with(DocumentActivity.this).load(R.mipmap.camera_document).into(takePhoto);

                                imgUrl = "";
                                mImgObjectKey = "";

                                isHasImage = false;
                            }
                        });
                    } else {
                        isEdit = true;
                        choose_icon.setVisibility(View.GONE);
                        true_choose_icon.setVisibility(View.GONE);
                        titleBar.setRightText("编辑");
                        takePhoto.setEnabled(true);
                        delete_image_btn.setVisibility(View.GONE);
                    }
                } else {
                    return;
                }
            }
        });
    }
}


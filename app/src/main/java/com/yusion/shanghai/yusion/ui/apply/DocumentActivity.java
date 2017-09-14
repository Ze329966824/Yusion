package com.yusion.shanghai.yusion.ui.apply;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnMultiDataCallBack;
import com.yusion.shanghai.yusion.ui.upload.PreviewActivity;
import com.yusion.shanghai.yusion.utils.DensityUtil;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OcrUtil;
import com.yusion.shanghai.yusion.utils.OssUtil;
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentActivity extends BaseActivity {
    //private StatusImageRel statusImageRel;
    Button btn;
    ImageView choose_icon;
    ImageView true_choose_icon;
    ImageView takePhoto;
    Button delete_image_btn;
    private File imageFile;
    private String mType;//id_card_front  id_card_back  driving_lic  auth_credit
    private String mRole;//lender lender_sp guarantor guarantor_sp
    private String mImgObjectKey = "";
    private View view;
    private OcrResp.ShowapiResBodyBean mOcrResp;
    private String imgUrl = "";
    TitleBar titleBar;
    Intent mGetIntent;

    private boolean isHasImage = false;
    private boolean isEdit = true;
    private boolean isFlag = true;
    private boolean isClick = false;
    private UploadLabelItemBean mTopItem;
    private TextView errorTv;
    private LinearLayout errorLin;
    private List<UploadImgItemBean> imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetIntent = getIntent();
        mTopItem = (UploadLabelItemBean) mGetIntent.getSerializableExtra("topItem");
        if (mTopItem != null) {
            //从影像件列表点击进来
            mType = mTopItem.value;
            imgList = mTopItem.img_list;

        } else {
            mType = mGetIntent.getStringExtra("type");
        }
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


        createBottomDialog();
        // statusImageRel = (StatusImageRel) findViewById(R.id.statusImageRel);
        //statusImageRel.sourceImg.setImageResource(R.mipmap.camera_document);
        btn = (Button) findViewById(R.id.btn);
        delete_image_btn = (Button) findViewById(R.id.image_update_btn);
        choose_icon = (ImageView) findViewById(R.id.choose_icon);
        true_choose_icon = (ImageView) findViewById(R.id.true_choose_icon);
        takePhoto = (ImageView) findViewById(R.id.camera_document);

        if (mTopItem != null) {
            if (imgList.size() > 0) {
                isHasImage = true;
                UploadImgItemBean itemBean = imgList.get(0);
                if (!TextUtils.isEmpty(itemBean.local_path)) {
                    imgUrl = itemBean.local_path;
                    Glide.with(this).load(itemBean.local_path).into(takePhoto);
                    //GlideUtil.loadImg(this, statusImageRel, new File(itemBean.local_path));
                } else {
                    imgUrl = itemBean.s_url;
                    Glide.with(this).load(itemBean.s_url).into(takePhoto);
                    //GlideUtil.loadImg(this, statusImageRel, itemBean.s_url);
                }
            }
            errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
            errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
            if (!mTopItem.hasGetImgsFromServer) {
                //第一次进入
                ListImgsReq req = new ListImgsReq();
                req.label = mTopItem.value;
                req.clt_id = mGetIntent.getStringExtra("clt_id");
                UploadApi.listImgs(this, req, resp -> {
                    mTopItem.hasGetImgsFromServer = true;
                    if (resp.has_err) {
                        errorLin.setVisibility(View.VISIBLE);
                        errorTv.setText("您提交的资料有误：" + resp.error);
                        mTopItem.errorInfo = "您提交的资料有误：" + resp.error;
                    } else {
                        errorLin.setVisibility(View.GONE);
                        mTopItem.errorInfo = "";
                    }
                    if (resp.list.size() != 0) {
                        imgUrl = resp.list.get(0).s_url;
                        Glide.with(this).load(resp.list.get(0).s_url).into(takePhoto);
                        //GlideUtil.loadImg(this, statusImageRel, resp.list.get(0).s_url);
                        titleBar.setRightTextColor(Color.parseColor("#ffffff"));
                        isHasImage = true;
                        imgList.addAll(resp.list);
                    } else {
                        isHasImage = false;
                    }
                });
            } else if (mTopItem.hasError) {
                errorLin.setVisibility(View.VISIBLE);
                errorTv.setText(mTopItem.errorInfo);
            }
            getTitleInfo();
        } else {
            getTitleInfo();
            mImgObjectKey = mGetIntent.getStringExtra("objectKey");
            if (mImgObjectKey == null) {
                mImgObjectKey = "";
            }
            if (!TextUtils.isEmpty(mGetIntent.getStringExtra("imgUrl"))) {
                imgUrl = mGetIntent.getStringExtra("imgUrl");
                Glide.with(this).load(mGetIntent.getStringExtra("imgUrl")).into(takePhoto);
                //GlideUtil.loadImg(this, statusImageRel, new File(mGetIntent.getStringExtra("imgUrl")));
                titleBar.setRightTextColor(Color.parseColor("#ffffff"));
                isHasImage = true;
            } else {
                isHasImage = false;
            }
        }
        createBottomDialog();
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasImage && !isClick) {//如果有图，且不在编辑模式
                    if (!mBottomDialog.isShowing()) {
                        mBottomDialog.show();
                    }
                } else {
                    takePhoto();
                }
            }
        });

        titleBar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if (isHasImage) {//有图的情况下
                        titleBar.setRightTextColor(Color.parseColor("#ffffff"));
                        if (isEdit) { //编辑状态
                            isClick = true;
                            choose_icon.setImageResource(R.mipmap.choose_icon);
                            choose_icon.setVisibility(View.VISIBLE);
                            delete_image_btn.setVisibility(View.VISIBLE);
                            titleBar.setRightText("取消");
                            delete_image_btn.setEnabled(false);
                            delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));//删除按钮是虚化的
                            isEdit = false;

                        } else {//在点击取消的时候 回归原来的状态
                            choose_icon.setVisibility(View.GONE);
                            true_choose_icon.setVisibility(View.GONE);
                            titleBar.setRightText("编辑");
                            takePhoto.setEnabled(true);
                            //statusImageRel.sourceImg.setEnabled(true);
                            delete_image_btn.setVisibility(View.GONE);
                            isEdit = true;
                            isClick = false;
                        }
                    } else {
                        isClick = false;
                        return;
                    }
                }
            }
        });


        delete_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delete_image_btn.setVisibility(View.GONE);
//                choose_icon.setVisibility(View.GONE);
//                true_choose_icon.setVisibility(View.GONE);
//                choose_icon.setImageResource(R.mipmap.choose_icon);
//                titleBar.setRightText("编辑").setRightTextColor(Color.parseColor("#80ffffff"));
//
//                Glide.with(DocumentActivity.this).load(R.mipmap.camera_document).into(takePhoto);
                imgUrl = "";
                mImgObjectKey = "";

                if (mTopItem != null) {
                    if (!TextUtils.isEmpty(imgList.get(0).id)) {
                        DelImgsReq req = new DelImgsReq();
                        req.clt_id = mGetIntent.getStringExtra("clt_id");
                        req.id.add(imgList.get(0).id);
                        UploadApi.delImgs(DocumentActivity.this, req, new OnCodeAndMsgCallBack() {
                            @Override
                            public void callBack(int code, String msg) {
                                if (code == 0) {
                                    Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                                    mTopItem.hasImg = false;
                                    imgList.clear();
                                    //statusImageRel.progressPro.setVisibility(View.GONE);

                                    delete_image_btn.setVisibility(View.GONE);

                                    choose_icon.setVisibility(View.GONE);
                                    true_choose_icon.setVisibility(View.GONE);
                                    choose_icon.setImageResource(R.mipmap.choose_icon);
                                    titleBar.setRightText("编辑").setRightTextColor(Color.parseColor("#80ffffff"));

                                    Glide.with(DocumentActivity.this).load(R.mipmap.camera_document).into(takePhoto);
                                    //Glide.with(DocumentActivity.this).load(R.mipmap.camera_document).into(statusImageRel.sourceImg);
                                }
                            }
                        });
                    } else {
                        imgList.clear();
                    }
                } else {//13946466464
                    if (!TextUtils.isEmpty(getIntent().getStringExtra("imgUrlId"))) {
                        DelImgsReq req = new DelImgsReq();
                        req.clt_id = mGetIntent.getStringExtra("clt_id");
                        req.id.add(imgList.get(0).id);
                        UploadApi.delImgs(DocumentActivity.this, req, new OnCodeAndMsgCallBack() {
                            @Override
                            public void callBack(int code, String msg) {
                                if (code == 0) {
                                    Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                                    mTopItem.hasImg = false;
                                    imgList.clear();
                                    //statusImageRel.progressPro.setVisibility(View.GONE);

                                    delete_image_btn.setVisibility(View.GONE);

                                    choose_icon.setVisibility(View.GONE);
                                    true_choose_icon.setVisibility(View.GONE);
                                    choose_icon.setImageResource(R.mipmap.choose_icon);
                                    titleBar.setRightText("编辑").setRightTextColor(Color.parseColor("#80ffffff"));

                                    Glide.with(DocumentActivity.this).load(R.mipmap.camera_document).into(takePhoto);
                                    //Glide.with(DocumentActivity.this).load(R.mipmap.camera_document).into(statusImageRel.sourceImg);

                                }
                            }
                        });
                    }
                }

                isHasImage = false;
                takePhoto.setEnabled(true);
                //statusImageRel.sourceImg.setEnabled(true);
                isClick = false;
            }
        });


    }

    private Dialog mBottomDialog;

    private void createBottomDialog() {
        View bottomLayout = LayoutInflater.from(this).inflate(R.layout.document_bottom_dialog, null);
        TextView tv1 = ((TextView) bottomLayout.findViewById(R.id.tv1));
        TextView tv2 = ((TextView) bottomLayout.findViewById(R.id.tv2));
        TextView tv3 = ((TextView) bottomLayout.findViewById(R.id.tv3));
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DocumentActivity.this, PreviewActivity.class);
                intent.putExtra("PreviewImg", imgUrl);

                ActivityOptionsCompat compat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(DocumentActivity.this,
                                btn, "shareNames");
                ActivityCompat.startActivity(DocumentActivity.this, intent, compat.toBundle());

                // startActivity(intent);

                // ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(btn, btn.getWidth() / 2, btn.getHeight() / 2, 0, 0);
                //ActivityCompat.startActivity(DocumentActivity.this, new Intent(DocumentActivity.this, PreviewActivity.class), compat.toBundle());

                //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(DocumentActivity.this, btn, "shareNames").toBundle());
                if (mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                if (mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
            }
        });
        if (mBottomDialog == null) {
            mBottomDialog = new Dialog(this, R.style.MyDialogStyle);
            mBottomDialog.setContentView(bottomLayout);
            mBottomDialog.setCanceledOnTouchOutside(false);
            mBottomDialog.getWindow().setWindowAnimations(R.style.dialogAnimationStyle);
            mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            mBottomDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Window dialogWindow = mBottomDialog.getWindow();
            dialogWindow.getDecorView().setBackgroundResource(android.R.color.transparent);
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.setGravity(Gravity.BOTTOM);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (metrics.widthPixels - DensityUtil.dip2px(this, 15) * 2);
            dialogWindow.setAttributes(lp);
        }
    }

    private void takePhoto() {
        if (mType.equals("id_card_front") && !isClick) {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3000);//正面3000，反面3001，授权书3002
        } else if (mType.equals("id_card_back") && !isClick) {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3001);
        } else if (mType.equals("auth_credit") && !isClick) {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3002);

        } else if (mType.equals("driving_lic") && !isClick) {
            Intent i = new Intent(DocumentActivity.this, PhotoMediaActivity.class);
            i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());//加载类型
            i.putExtra("maxCount", 1);//加载类型
            startActivityForResult(i, 100);
        } else if (isClick) {
            if (isFlag) {
                choose_icon.setImageResource(R.mipmap.surechoose_icon);
                delete_image_btn.setEnabled(true);
                delete_image_btn.setTextColor(Color.parseColor("#ff3f00"));
                isFlag = false;
            } else {
                choose_icon.setImageResource(R.mipmap.choose_icon);
                delete_image_btn.setEnabled(false);
                delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));
                isFlag = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {//驾驶证
                ArrayList<String> files = data.getStringArrayListExtra("files");
                String localUrl = files.get(0);

                if (mTopItem != null) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = localUrl;
                    item.role = mRole;
                    item.type = mType;
                    imgList.clear();
                    imgList.add(item);
                    mTopItem.hasImg = true;
                }

                Glide.with(this).load(localUrl).into(takePhoto);
                //GlideUtil.loadImg(this, statusImageRel, new File(localUrl));
                isHasImage = true;
                titleBar.setRightTextColor(Color.parseColor("#ffffff"));
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

                if (mTopItem != null) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = imageFile.getAbsolutePath();
                    item.role = mRole;
                    item.type = mType;
                    imgList.clear();
                    imgList.add(item);
                }

                //Glide.with(this).load(imageFile).into(takePhoto);


                isHasImage = true;
                titleBar.setRightTextColor(Color.parseColor("#ffffff"));
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                OcrUtil.requestOcr(this, imageFile.getAbsolutePath(), new OSSObjectKeyBean(mRole, mType, ".png"), "id_card", new OcrUtil.OnOcrSuccessCallBack() {
                    @Override
                    public void OnOcrSuccess(OcrResp ocrResp, String objectKey) {
                        imgUrl = imageFile.getAbsolutePath();
                        mImgObjectKey = objectKey;
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
                            mOcrResp = ocrResp.showapi_res_body;
                            dialog.dismiss();
                        }
                    }
                }, new OnMultiDataCallBack<Throwable, String>() {
                    @Override
                    public void onMultiDataCallBack(Throwable throwable, String objectKey) {
                        if (!TextUtils.isEmpty(objectKey)) {
                            mImgObjectKey = objectKey;
                        }
                        Toast.makeText(DocumentActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            } else if (requestCode == 3000) {//id_front

                if (mTopItem != null) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = imageFile.getAbsolutePath();
                    item.role = mRole;
                    item.type = mType;
                    imgList.clear();
                    imgList.add(item);
                }

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentActivity.this).load(imageFile).into(takePhoto);
                //GlideUtil.loadImg(this, statusImageRel, imageFile);
                isHasImage = true;
                titleBar.setRightTextColor(Color.parseColor("#ffffff"));
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

                if (mTopItem != null) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = imageFile.getAbsolutePath();
                    item.role = mRole;
                    item.type = mType;
                    imgList.clear();
                    imgList.add(item);
                }

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentActivity.this).load(imageFile).into(takePhoto);
                //GlideUtil.loadImg(this, statusImageRel, imageFile);
                isHasImage = true;
                titleBar.setRightTextColor(Color.parseColor("#ffffff"));
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
//        Intent intent = new Intent();
        if (mTopItem != null && mTopItem.img_list.size() > 0) {
            mTopItem.img_list.get(0).objectKey = mImgObjectKey;
        }
        mGetIntent.putExtra("objectKey", mImgObjectKey);
        mGetIntent.putExtra("type", mType);
        mGetIntent.putExtra("ocrResp", mOcrResp);
        mGetIntent.putExtra("imgUrl", imgUrl);
        setResult(RESULT_OK, mGetIntent);
//        setResult(RESULT_OK, intent);
        finish();
    }

    private void getTitleInfo() {
        if (mType.equals("auth_credit")) {//征信授权书
            titleBar = initTitleBar(this, "征信授权书").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextSize(16);
            if (!isHasImage) {
                titleBar.setRightTextColor(Color.parseColor("#80ffffff"));
            }

        } else if (mType.equals("id_card_front")) {//身份证国徽面
            titleBar = initTitleBar(this, "身份证国徽面").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);
            if (!isHasImage) {
                titleBar.setRightTextColor(Color.parseColor("#80ffffff"));
            }

        } else if (mType.equals("id_card_back")) {//身份证人像面
            titleBar = initTitleBar(this, "身份证人像面").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);
            if (!isHasImage) {
                titleBar.setRightTextColor(Color.parseColor("#80ffffff"));
            }

        } else if (mType.equals("driving_lic")) {//驾驶证
            titleBar = initTitleBar(this, "驾驶证影像件").setLeftClickListener(v -> onBack()).setRightText("编辑")
                    .setRightTextColor(Color.parseColor("#ffffff")).setRightTextSize(16);
            if (!isHasImage) {
                titleBar.setRightTextColor(Color.parseColor("#80ffffff"));
            }
        }
    }
}


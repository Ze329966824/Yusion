package com.yusion.shanghai.yusion.ui.apply;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OssUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;

import java.util.ArrayList;

public class SingleImgUploadActivity extends BaseActivity {

    private ImageView mImg;
    private String mImgUrl;
    private String mType;
    private String mCltId;
    private String mRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_img_upload);
        Intent mGetIntent = getIntent();
        mType = mGetIntent.getStringExtra("type");
        mCltId = mGetIntent.getStringExtra("clt_id");
        mRole = mGetIntent.getStringExtra("role");
        String title = "";
        if (mType.equals("driving_lic")) {
            title = "驾驶证影像件";
        } else if (mType.equals("divorce_proof")) {
            title = "离婚证(法院判决书)";
        }
        initTitleBar(this, title).setLeftClickListener(v -> onBack());

        mImg = (ImageView) findViewById(R.id.single_img_upload_camera_img);
        mImg.setOnClickListener(v -> {
            Intent i = new Intent(SingleImgUploadActivity.this, PhotoMediaActivity.class);
            i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
            i.putExtra("maxCount", 1);
            startActivityForResult(i, 100);
        });

        mImgUrl = mGetIntent.getStringExtra("imgUrl");
        if (!TextUtils.isEmpty(mImgUrl)) {
            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            Glide.with(this).load(mImgUrl).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    dialog.dismiss();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    dialog.dismiss();
                    return false;
                }
            }).into(mImg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                String localUrl = files.get(0);
                Glide.with(this).load(localUrl).into(mImg);

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                OssUtil.uploadOss(this, false, localUrl, new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String objectKey) {
                        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
//                        uploadFilesUrlReq.clt_id = mCltId;
                        ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
                        UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                        fileUrlBean.file_id = objectKey;
                        fileUrlBean.label = mType;
//                        fileUrlBean.role = mRole;
                        files.add(fileUrlBean);
                        uploadFilesUrlReq.files = files;
                        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(SingleImgUploadActivity.this).getValue("region", "");
                        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(SingleImgUploadActivity.this).getValue("bucket", "");
                        UploadApi.uploadFileUrl(SingleImgUploadActivity.this, uploadFilesUrlReq, (code, msg) -> {
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
            }
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        Intent intent = new Intent();
        intent.putExtra("imgUrl", mImgUrl);
        setResult(RESULT_OK, intent);
        finish();
    }
}

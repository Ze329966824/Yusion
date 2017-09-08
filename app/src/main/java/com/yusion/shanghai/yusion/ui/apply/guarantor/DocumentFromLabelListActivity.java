package com.yusion.shanghai.yusion.ui.apply.guarantor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.ui.upload.PreviewActivity;
import com.yusion.shanghai.yusion.utils.DensityUtil;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OssUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentFromLabelListActivity extends BaseActivity {
    private Button btn;
    private boolean isEditing = false;
    private String title;
    private ImageView choose_icon;
    private ImageView takePhoto;
    private Button delete_image_btn;
    private File imageFile;
    private String mType;//id_card_front  id_card_back  driving_lic  auth_credit
    private String mRole;//lender lender_sp guarantor guarantor_sp
    private String imgUrl = "";
    private TitleBar titleBar;
    private Intent mGetIntent;
    private TextView mEditTv;
    private boolean isHasImage = false;
    private TextView errorTv;
    private LinearLayout errorLin;
    private UploadImgItemBean imageBean;
    private Dialog mBottomDialog;
    private String clt_id;
    private boolean hasImg = false;
    private String imgId;
    private boolean hasChoose = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetIntent = getIntent();

        title = mGetIntent.getStringExtra("title");
        clt_id = mGetIntent.getStringExtra("clt_id");
        mRole = mGetIntent.getStringExtra("role");
        mType = mGetIntent.getStringExtra("type");

        if (mType.equals("id_card_front")) {//身份证国徽面
            setContentView(R.layout.activity_document);
        } else if (mType.equals("id_card_back")) {//身份证人像面
            setContentView(R.layout.activity_shengfz);
        } else if (mType.equals("driving_lic")) {//驾驶证
            setContentView(R.layout.activity_drive);
        }

        initView();
        initData();

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHasImage) {
                    if (isEditing) {//如果是编辑状态
                        setIconAnddelBtn(hasChoose);
                    } else {
                        if (!mBottomDialog.isShowing()) {
                            mBottomDialog.show();
                        }
                    }
                } else {
                    takePhoto();
                }
            }
        });
        delete_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<String> relDelImgIdList = new ArrayList<>();
                relDelImgIdList.add(imgId);
                DelImgsReq req = new DelImgsReq();
                req.id = relDelImgIdList;
                req.clt_id = clt_id;
                UploadApi.delImgs(DocumentFromLabelListActivity.this, req, new OnCodeAndMsgCallBack() {
                    @Override
                    public void callBack(int code, String msg) {
                        if (code == 0) {
                            Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
//                            imageBean = null;

                            hasImg = false;
                            onImgCountChange(hasImg);

                            setImageResourse(null);

                            delete_image_btn.setVisibility(View.GONE);
                            choose_icon.setImageResource(R.mipmap.choose_icon);
                            choose_icon.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }

    private void initView() {
        createBottomDialog();
        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
        btn = (Button) findViewById(R.id.btn);
        delete_image_btn = (Button) findViewById(R.id.image_update_btn);
        titleBar = initTitleBar(this, title).setLeftClickListener(v -> onBack());
        takePhoto = (ImageView) findViewById(R.id.camera_document);
        choose_icon = (ImageView) findViewById(R.id.choose_icon);
        mEditTv = titleBar.getRightTextTv();
        titleBar.setRightText("编辑").setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    isEditing = false;
//                    mEditTv.setText("编辑");
//                    delete_image_btn.setVisibility(View.GONE);
//                    choose_icon.setVisibility(View.GONE);
//                    choose_icon.setImageResource(R.mipmap.choose_icon);
                } else {
                    isEditing = true;
//                    mEditTv.setText("取消");
//                    delete_image_btn.setVisibility(View.VISIBLE);
//                    delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));
//                    setIsEditing(isEditing);
                }
                //如果是编辑状态，设置图片 haschoose 为false
                setIsEditing(isEditing);
            }
        });
    }

    private void initData() {
        ListImgsReq req = new ListImgsReq();
        req.clt_id = clt_id;
        req.label = mType;
        UploadApi.listImgs(this, req, resp -> {
//            imageBean = new UploadImgItemBean();
            if (resp != null) {
                if (resp.has_err) {
                    errorLin.setVisibility(View.VISIBLE);
                    errorTv.setText("您提交的资料有误：" + resp.error);
                } else {
                    errorLin.setVisibility(View.GONE);
                }
                if (resp.list.size() > 0) {
                    hasImg = true;
//                    imageBean = resp.list.get(0);
                    setImageResourse(resp.list.get(0));

                    imgId = resp.list.get(0).id;
                }
            }
            onImgCountChange(hasImg);
        });

    }

    private void onImgCountChange(boolean hasImg) {
        if (hasImg) {
            mEditTv.setEnabled(true);
            mEditTv.setTextColor(Color.parseColor("#ffffff"));
        } else {
            mEditTv.setEnabled(false);
            mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
            mEditTv.setText("编辑");
        }
    }


    private void createBottomDialog() {
        View bottomLayout = LayoutInflater.from(this).inflate(R.layout.document_bottom_dialog, null);
        TextView tv1 = ((TextView) bottomLayout.findViewById(R.id.tv1));
        TextView tv2 = ((TextView) bottomLayout.findViewById(R.id.tv2));
        TextView tv3 = ((TextView) bottomLayout.findViewById(R.id.tv3));
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(myApp, "预览", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DocumentFromLabelListActivity.this, PreviewActivity.class);
                intent.putExtra("PreviewImg", imgUrl);

                ActivityOptionsCompat compat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(DocumentFromLabelListActivity.this,
                                btn, "shareNamess");
                ActivityCompat.startActivity(DocumentFromLabelListActivity.this, intent, compat.toBundle());

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

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {

        finish();
    }

    public void setIsEditing(boolean isEditing) {
        if (isEditing) {
            //显示icon布局
            choose_icon.setVisibility(View.VISIBLE);
            hasChoose = false;
            mEditTv.setText("取消");
            delete_image_btn.setVisibility(View.VISIBLE);
            delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));
        } else {
            mEditTv.setText("编辑");
            delete_image_btn.setVisibility(View.GONE);
            choose_icon.setVisibility(View.GONE);
            choose_icon.setImageResource(R.mipmap.choose_icon);
        }
    }

    public void setIconAnddelBtn(boolean isHasChoose) {
        if (isHasChoose) {
            //显示虚勾
            choose_icon.setImageResource(R.mipmap.choose_icon);
            //imgList.get(0).hasChoose = false;
            hasChoose = false;
            delete_image_btn.setEnabled(false);
            delete_image_btn.setTextColor(Color.parseColor("#d1d1d1"));

        } else {
            choose_icon.setImageResource(R.mipmap.surechoose_icon);
            delete_image_btn.setEnabled(true);
            //imgList.get(0).hasChoose = true;
            hasChoose = true;
            delete_image_btn.setEnabled(true);
            delete_image_btn.setTextColor(Color.parseColor("#ff3f00"));
            //显示实勾
        }
    }

    public void setImageResourse(UploadImgItemBean imageBean) {
        Dialog dialog = LoadingUtils.createLoadingDialog(this);
        dialog.show();
        if (imageBean == null) {
            Glide.with(this).load(R.mipmap.camera_document).into(takePhoto);
        } else {
            if (TextUtils.isEmpty(imageBean.local_path)) {
                //Glide.with(mContext).load(new File(item.local_path)).listener(new UploadListActivity.RvAdapter.GlideRequestListener(dialog)).into(holder.img);
                Glide.with(this).load(imageBean.s_url).listener(new GlideRequestListener(dialog)).into(takePhoto);
                imgUrl = imageBean.s_url;
            } else if (TextUtils.isEmpty(imageBean.s_url)) {
                //Glide.with(this).load(imageBean.local_path).into(takePhoto);
                Glide.with(this).load(imageBean.local_path).listener(new GlideRequestListener(dialog)).into(takePhoto);
                imgUrl = imageBean.local_path;
            }

        }
    }

    private void takePhoto() {
        if (mType.equals("id_card_front")) {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3000);//正面3000，反面3001，授权书3002
        } else if (mType.equals("id_card_back")) {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3001);
        } else if (mType.equals("driving_lic")) {
            Intent i = new Intent(DocumentFromLabelListActivity.this, PhotoMediaActivity.class);
            i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());//加载类型
            i.putExtra("maxCount", 1);//加载类型
            startActivityForResult(i, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 100) {//驾驶证
                List<String> files = new ArrayList<>();
                files.clear();
                files = data.getStringArrayListExtra("files");
                imgUrl = files.get(0);
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(this).load(imgUrl).into(takePhoto);
                imageBean = new UploadImgItemBean();
                imageBean.local_path = imgUrl;
                imageBean.type = mType;
                imageBean.role = mRole;

                hasImg = true;
                onImgCountChange(hasImg);

                upLoadImg(dialog, imageBean.local_path);

            } else if (requestCode == 3001) {//id_back

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentFromLabelListActivity.this).load(imageFile).into(takePhoto);
                imageBean = new UploadImgItemBean();
                imageBean.type = mType;
                imageBean.role = mRole;
                imageBean.local_path = imageFile.getAbsolutePath();

                hasImg = true;
                onImgCountChange(hasImg);

                upLoadImg(dialog, imageBean.local_path);

            } else if (requestCode == 3000) {//id_front
                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                Glide.with(DocumentFromLabelListActivity.this).load(imageFile).into(takePhoto);

                imageBean = new UploadImgItemBean();
                imageBean.type = mType;
                imageBean.role = mRole;
                imageBean.local_path = imageFile.getAbsolutePath();

                hasImg = true;
                onImgCountChange(hasImg);

                upLoadImg(dialog, imageBean.local_path);
            }
        }

    }

    private void upLoadImg(final Dialog dialog, String imagePath) {
        OssUtil.uploadOss(DocumentFromLabelListActivity.this, false, imagePath, new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
            @Override
            public void onItemDataCallBack(String objectKey) {

                imageBean.objectKey = objectKey;

                List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
                UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                fileUrlBean.clt_id = clt_id;
                fileUrlBean.label = mType;
                fileUrlBean.file_id = imageBean.objectKey;
                uploadFileUrlBeanList.add(fileUrlBean);
                UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
                uploadFilesUrlReq.files = uploadFileUrlBeanList;
                uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(DocumentFromLabelListActivity.this).getValue("region", "");
                uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(DocumentFromLabelListActivity.this).getValue("bucket", "");
                UploadApi.uploadFileUrl(DocumentFromLabelListActivity.this, uploadFilesUrlReq, new OnItemDataCallBack<List<String>>() {
                    @Override
                    public void onItemDataCallBack(List<String> data) {
                        imgId = data.get(0);
                        dialog.dismiss();
                    }
                });
            }
        }, new OnItemDataCallBack<Throwable>() {
            @Override
            public void onItemDataCallBack(Throwable data) {
                Toast.makeText(DocumentFromLabelListActivity.this, "上传图片异常", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private class GlideRequestListener implements RequestListener<Drawable> {
        private Dialog dialog;

        public GlideRequestListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            Toast.makeText(DocumentFromLabelListActivity.this, "图片加载失败", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            dialog.dismiss();
            return false;
        }
    }
}

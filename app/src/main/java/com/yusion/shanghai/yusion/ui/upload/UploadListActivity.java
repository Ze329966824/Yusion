package com.yusion.shanghai.yusion.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.DelImgsReq;
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.glide.ProgressModelLoader;
import com.yusion.shanghai.yusion.glide.StatusImageView;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OssUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ice on 2017/9/8.
 */

public class UploadListActivity extends BaseActivity {
    private RvAdapter adapter;
    private Intent mGetIntent;
    private TextView errorTv;
    private LinearLayout errorLin;
    private boolean isEditing = false;
    private TextView mEditTv;
    private LinearLayout uploadBottomLin;
    private TextView uploadTv2;
    private TextView uploadTv1;
    private List<UploadImgItemBean> lists = new ArrayList<>();
    private String role;
    private String type;
    private String clt_id;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);

        mGetIntent = getIntent();
        role = mGetIntent.getStringExtra("role");
        type = mGetIntent.getStringExtra("type");
        clt_id = mGetIntent.getStringExtra("clt_id");
        title = mGetIntent.getStringExtra("title");

        initView();
        initData();
    }

    private void initView() {
        TitleBar titleBar = initTitleBar(this, title).setLeftClickListener(v -> onBack());
        mEditTv = titleBar.getRightTextTv();
        titleBar.setRightText("编辑").setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    isEditing = false;
                    mEditTv.setText("编辑");
                    uploadBottomLin.setVisibility(View.GONE);
                } else {
                    isEditing = true;
                    mEditTv.setText("取消");
                    uploadBottomLin.setVisibility(View.VISIBLE);

                    uploadTv2.setText("删除");
                    uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                }
                adapter.setIsEditing(isEditing);
            }
        });

        uploadBottomLin = (LinearLayout) findViewById(R.id.upload_bottom_lin);
        uploadTv1 = (TextView) findViewById(R.id.upload_bottom_tv1);
        uploadTv2 = (TextView) findViewById(R.id.upload_bottom_tv2);
        uploadTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTv1.getText().toString().equals("全选")) {
                    for (UploadImgItemBean itemBean : lists) {
                        itemBean.hasChoose = true;
                    }
                    uploadTv1.setText("取消全选");
                    uploadTv2.setText(String.format("删除(%d)", getCurrentChooseItemCount()));
                    uploadTv2.setTextColor(Color.RED);
                    adapter.notifyDataSetChanged();
                } else if (uploadTv1.getText().toString().equals("取消全选")) {
                    for (UploadImgItemBean itemBean : lists) {
                        itemBean.hasChoose = false;
                    }
                    uploadTv1.setText("全选");
                    uploadTv2.setText("删除");
                    uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                    adapter.notifyDataSetChanged();
                }
            }
        });
        uploadTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //要删除的图片的id集合
                List<String> delImgIdList = new ArrayList<>();

                //要删除的索引集合
                List<Integer> indexList = new ArrayList<>();
                for (int i = 0; i < lists.size(); i++) {
                    if (lists.get(i).hasChoose) indexList.add(i);
                }
                Collections.sort(indexList);

                //没删除一个对象就该偏移+1
                int offset = 0;
                for (int i = 0; i < indexList.size(); i++) {
                    int delIndex = indexList.get(i) - offset;
                    delImgIdList.add(lists.get(delIndex).id);
                    lists.remove(delIndex);
                    offset++;
                }

                uploadTv2.setText("删除");
                uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                adapter.notifyDataSetChanged();

                DelImgsReq req = new DelImgsReq();
                req.clt_id = clt_id;
                req.id.addAll(delImgIdList);
                if (delImgIdList.size() > 0) {
                    UploadApi.delImgs(UploadListActivity.this, req, new OnCodeAndMsgCallBack() {
                        @Override
                        public void callBack(int code, String msg) {
                            if (code == 0) {
                                Toast.makeText(myApp, "删除成功", Toast.LENGTH_SHORT).show();
                                onImgCountChange(lists.size() > 0);
                            }
                        }
                    });
                }
            }
        });

        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);

        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new RvAdapter(this, lists);
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item, int index) {
                if (isEditing) {
                    if (item.hasChoose) {
                        item.hasChoose = false;
                    } else {
                        item.hasChoose = true;
                    }
                    adapter.notifyDataSetChanged();
                    if (getCurrentChooseItemCount() != 0) {
                        uploadTv2.setText(String.format("删除(%d)", getCurrentChooseItemCount()));
                        uploadTv2.setTextColor(Color.RED);
                    } else {
                        uploadTv2.setText("删除");
                        uploadTv2.setTextColor(Color.parseColor("#d1d1d1"));
                    }
                } else {
                    String imgUrl;
                    if (!TextUtils.isEmpty(item.local_path)) {
                        imgUrl = item.local_path;
                    } else {
                        imgUrl = item.s_url;
                    }
                    previewImg(findViewById(R.id.preview_anchor), imgUrl);

                }
            }

            @Override
            public void onFooterClick(View v) {
                Intent i = new Intent(UploadListActivity.this, PhotoMediaActivity.class);
                i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                startActivityForResult(i, 100);
            }
        });
    }

    private void previewImg(View previewAnchor, String imgUrl) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("PreviewImg", imgUrl);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, previewAnchor, "shareNames");
        ActivityCompat.startActivity(this, intent, compat.toBundle());
    }

    private void initData() {
        ListImgsReq req = new ListImgsReq();
        req.label = type;
        req.clt_id = clt_id;
        UploadApi.listImgs(this, req, resp -> {
            if (resp.has_err) {
                errorLin.setVisibility(View.VISIBLE);
                errorTv.setText("您提交的资料有误：" + resp.error);
            } else {
                errorLin.setVisibility(View.GONE);
            }

            onImgCountChange(resp.list.size() > 0);

            if (resp.list.size() != 0) {
                lists.addAll(resp.list);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private int getCurrentChooseItemCount() {
        int totalCount = 0;
        for (UploadImgItemBean itemBean : lists) {
            if (itemBean.hasChoose) {
                totalCount++;
            }
        }
        return totalCount;
    }

    private void onImgCountChange(boolean hasImg) {
        if (hasImg) {
            mEditTv.setEnabled(true);
            mEditTv.setTextColor(Color.parseColor("#ffffff"));
        } else {
            mEditTv.setEnabled(false);
            mEditTv.setTextColor(Color.parseColor("#d1d1d1"));
            mEditTv.setText("编辑");
            uploadBottomLin.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> files = data.getStringArrayListExtra("files");

            List<UploadImgItemBean> toAddList = new ArrayList<>();
            for (String file : files) {
                UploadImgItemBean item = new UploadImgItemBean();
                item.local_path = file;
                item.role = role;
                item.type = type;
                toAddList.add(item);
            }

            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            int account = 0;
            for (UploadImgItemBean imgItemBean : toAddList) {
                account++;
                int finalAccount = account;
                String suffix = ".png";
                OssUtil.uploadOss(this, false, imgItemBean.local_path, new OSSObjectKeyBean(Constants.PersonType.LENDER, type, suffix), new OnItemDataCallBack<String>() {
                    @Override
                    public void onItemDataCallBack(String objectKey) {
                        imgItemBean.objectKey = objectKey;
                        onUploadOssFinish(finalAccount, files, dialog, toAddList);
                    }
                }, new OnItemDataCallBack<Throwable>() {
                    @Override
                    public void onItemDataCallBack(Throwable data) {
                        onUploadOssFinish(finalAccount, files, dialog, toAddList);
                    }
                });
            }
        }
    }

    private void onUploadOssFinish(int finalAccount, ArrayList<String> files, Dialog dialog, final List<UploadImgItemBean> toAddList) {
        if (finalAccount == files.size()) {
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    calculateRelToAddList(toAddList, new OnItemDataCallBack<List<UploadImgItemBean>>() {
                        @Override
                        public void onItemDataCallBack(List<UploadImgItemBean> relToAddList) {
                            uploadImgs(clt_id, relToAddList, new OnVoidCallBack() {
                                @Override
                                public void callBack() {
                                    lists.addAll(relToAddList);
                                    adapter.notifyItemRangeInserted(adapter.getItemCount(), relToAddList.size());
                                    onImgCountChange(lists.size() > 0);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void calculateRelToAddList(List<UploadImgItemBean> toAddList, OnItemDataCallBack<List<UploadImgItemBean>> onRelToAddListCallBack) {
        List<UploadImgItemBean> relToAddList = new ArrayList<>();
        for (UploadImgItemBean itemBean : toAddList) {
            if (!TextUtils.isEmpty(itemBean.objectKey)) {
                relToAddList.add(itemBean);
            }
        }
        onRelToAddListCallBack.onItemDataCallBack(relToAddList);
    }

    private Dialog mUploadFileDialog;

    public void uploadImgs(String clt_id, List<UploadImgItemBean> lists, OnVoidCallBack onSuccessCallBack) {
        List<UploadFilesUrlReq.FileUrlBean> uploadFileUrlBeanList = new ArrayList<>();
        for (UploadImgItemBean imgItemBean : lists) {
            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
            fileUrlBean.clt_id = clt_id;
            fileUrlBean.file_id = imgItemBean.objectKey;
            fileUrlBean.label = imgItemBean.type;
            uploadFileUrlBeanList.add(fileUrlBean);
        }

        if (mUploadFileDialog == null) {
            mUploadFileDialog = LoadingUtils.createLoadingDialog(this);
            mUploadFileDialog.setCancelable(false);
        }
        mUploadFileDialog.show();
        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
        uploadFilesUrlReq.files = uploadFileUrlBeanList;
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(this).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(this).getValue("bucket", "");
        UploadApi.uploadFileUrl(this, uploadFilesUrlReq, new OnItemDataCallBack<List<String>>() {
            @Override
            public void onItemDataCallBack(List<String> data) {
                for (int i = 0; i < lists.size(); i++) {
                    lists.get(i).id = data.get(i);
                }
                mUploadFileDialog.dismiss();
                onSuccessCallBack.callBack();
                Toast.makeText(UploadListActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        finish();
    }

    public static class RvAdapter extends RecyclerView.Adapter<RvAdapter.VH> {

        private LayoutInflater mLayoutInflater;
        private List<UploadImgItemBean> mItems;
        private Context mContext;
        private OnItemClick mOnItemClick;
        public static final int TYPE_ADD_IMG = 100;
        public static final int TYPE_IMG = 101;
        private boolean isEditing = false;

        public RvAdapter(Context context, List<UploadImgItemBean> items) {
            mItems = items;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == TYPE_ADD_IMG) {
                view = mLayoutInflater.inflate(R.layout.upload_list_add_img_item, parent, false);
            } else if (viewType == TYPE_IMG) {
//                view = mLayoutInflater.inflate(R.layout.upload_list_img_item_new, parent, false);
                view = new StatusImageView(mContext);
            }
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            if (position == mItems.size()) {
                holder.itemView.setOnClickListener(v -> mOnItemClick.onFooterClick(v));
            } else {
                UploadImgItemBean item = mItems.get(position);
//                Dialog dialog = LoadingUtils.createLoadingDialog(mContext);
//                dialog.show();

//                if (!TextUtils.isEmpty(item.local_path)) {
//                    Glide.with(mContext).load(new File(item.local_path)).listener(new GlideRequestListener(dialog)).into(holder.img);
//                } else {
//                    Glide.with(mContext).load(item.s_url).listener(new GlideRequestListener(dialog)).into(holder.img);
//                }

                if (!TextUtils.isEmpty(item.local_path)) {
                    Glide.with(mContext).load(new File(item.local_path)).into(((StatusImageView) holder.itemView).getSourceImg());
                } else {
//                    Glide.with(mContext).using(new ProgressModelLoader(new ProgressListener() {
//                        @Override
//                        public void progress(long bytesRead, long contentLength, boolean done) {
//                            StatusImageView statusImageView = (StatusImageView) holder.itemView;
//
//                            statusImageView.getProgressTv().post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Log.e("TAG", "progress() called with: bytesRead = [" + bytesRead + "], contentLength = [" + contentLength + "], done = [" + done + "]");
//                                    statusImageView.getProgressTv().setText(String.format(Locale.CHINA, "%.2f%%", bytesRead * 100f / contentLength));
//                                }
//                            });
//                        }
//                    })).load(item.s_url).into(((StatusImageView) holder.itemView).getSourceImg());

//                    String s_url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505125096416&di=77af33c77f8119e97f12348aa64f901d&imgtype=0&src=http%3A%2F%2Fwww.dyedz.gov.cn%2Fd%2Ffile%2Fguihuagongshi%2F2017-07-25%2Fcbb2a42956a7826108d8395d6c76f827.jpg";
//                    String url = "http://192.168.199.215:8000/IMG20170911154016.jpg";
                    Glide.with(mContext).using(new ProgressModelLoader(((StatusImageView) holder.itemView))).load(item.s_url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((StatusImageView) holder.itemView).getSourceImg());
                }
//
//                holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, position));
//                if (isEditing) {
//                    holder.cbImg.setVisibility(View.VISIBLE);
//                    if (item.hasChoose) {
//                        holder.cbImg.setImageResource(R.mipmap.surechoose_icon);
//                    } else {
//                        holder.cbImg.setImageResource(R.mipmap.choose_icon);
//                    }
//                } else {
//                    holder.cbImg.setVisibility(View.GONE);
//                }
            }
        }

        public void setIsEditing(boolean isEditing) {
            if (isEditing) {
                for (UploadImgItemBean item : mItems) {
                    item.hasChoose = false;
                }
            }
            this.isEditing = isEditing;
            notifyDataSetChanged();
        }

//        private class GlideRequestListener implements RequestListener<Drawable> {
//            private Dialog dialog;
//
//            public GlideRequestListener(Dialog dialog) {
//                this.dialog = dialog;
//            }
//
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                Toast.makeText(mContext, "图片加载失败", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                dialog.dismiss();
//                return false;
//            }
//        }


        @Override
        public int getItemViewType(int position) {
            return position == mItems.size() ? TYPE_ADD_IMG : TYPE_IMG;
        }

        //size+1是因为有 添加图片的item
        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size() + 1;
        }

        protected class VH extends RecyclerView.ViewHolder {
            public ImageView img;
            public ImageView cbImg;

            public VH(View itemView) {
                super(itemView);
                img = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_img));
                cbImg = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_cb_img));
            }
        }

        public interface OnItemClick {
            void onItemClick(View v, UploadImgItemBean item, int index);

            void onFooterClick(View v);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }
    }
}

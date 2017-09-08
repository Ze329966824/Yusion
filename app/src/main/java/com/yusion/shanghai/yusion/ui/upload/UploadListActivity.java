package com.yusion.shanghai.yusion.ui.upload;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OssUtil;
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
    private UploadLabelItemBean mTopItem;//上级页面传过来的bean
    private TextView errorTv;
    private LinearLayout errorLin;
    private List<UploadImgItemBean> imgList;
    private int currentChooseCount = 0;
    private boolean hasImg = false;
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
        TitleBar titleBar = initTitleBar(this, mGetIntent.getStringExtra("title")).setLeftClickListener(v -> onBack());
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
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                if (files.size() > 0) {
                    hasImg = true;
                    onImgCountChange(hasImg);
                }
                List<UploadImgItemBean> toAddList = new ArrayList<>();
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = role;
                    item.type = type;
                    toAddList.add(item);
                }

                imgList.addAll(toAddList);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), files.size());
                if (mTopItem != null) {
                    mTopItem.hasImg = imgList.size() > 0;
                }

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (UploadImgItemBean imgItemBean : toAddList) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, imgItemBean.local_path, new OSSObjectKeyBean(role, type, ".png"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            imgItemBean.objectKey = objectKey;
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                            }
                        }
                    }, new OnItemDataCallBack<Throwable>() {
                        @Override
                        public void onItemDataCallBack(Throwable data) {
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        }
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
                view = mLayoutInflater.inflate(R.layout.upload_list_img_item, parent, false);
            }
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            if (position == mItems.size()) {
                holder.itemView.setOnClickListener(v -> mOnItemClick.onFooterClick(v));
            } else {
                UploadImgItemBean item = mItems.get(position);
                Dialog dialog = LoadingUtils.createLoadingDialog(mContext);
                dialog.show();
                if (!TextUtils.isEmpty(item.local_path)) {
                    Glide.with(mContext).load(new File(item.local_path)).listener(new GlideRequestListener(dialog)).into(holder.img);
                } else {
                    Glide.with(mContext).load(item.s_url).listener(new GlideRequestListener(dialog)).into(holder.img);
                }
                holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, position));
                if (isEditing) {
                    holder.cbImg.setVisibility(View.VISIBLE);
                    if (item.hasChoose) {
                        holder.cbImg.setImageResource(R.mipmap.surechoose_icon);
                    } else {
                        holder.cbImg.setImageResource(R.mipmap.choose_icon);
                    }
                } else {
                    holder.cbImg.setVisibility(View.GONE);
                }
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

        private class GlideRequestListener implements RequestListener<Drawable> {
            private Dialog dialog;

            public GlideRequestListener(Dialog dialog) {
                this.dialog = dialog;
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Toast.makeText(mContext, "图片加载失败", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                dialog.dismiss();
                return false;
            }
        }


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

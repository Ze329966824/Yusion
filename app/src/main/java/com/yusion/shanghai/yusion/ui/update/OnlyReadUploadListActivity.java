package com.yusion.shanghai.yusion.ui.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.ui.apply.PreviewActivity;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OssUtil;
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ice on 2017/8/30.
 */

public class OnlyReadUploadListActivity extends BaseActivity {

    private OnlyReadUploadImgListAdapter adapter;
    private Intent mGetIntent;
    private UploadLabelItemBean mTopItem;//上级页面传过来的bean
    private TextView errorTv;
    private LinearLayout errorLin;
    private List<UploadImgItemBean> imgList;
    private String mRole;
    private String mType;
    private TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);

        mGetIntent = getIntent();
        mRole = mGetIntent.getStringExtra("role");
        mTopItem = (UploadLabelItemBean) mGetIntent.getSerializableExtra("topItem");
        if (mTopItem != null) {
            imgList = mTopItem.img_list;
            mType = mTopItem.value;
            titleBar = initTitleBar(this, mTopItem.name).setLeftClickListener(v -> onBack());
        }
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new OnlyReadUploadImgListAdapter(this, imgList);
        adapter.setOnItemClick(new OnlyReadUploadImgListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item) {
                String imgUrl;
                if (!TextUtils.isEmpty(item.local_path)) {
                    imgUrl = item.local_path;
                } else {
                    imgUrl = item.s_url;
                }
                previewImg(findViewById(R.id.preview_anchor), imgUrl);
            }
        });
        rv.setAdapter(adapter);
        initData();
    }

    private void previewImg(View previewAnchor, String imgUrl) {
        Intent intent = new Intent(this, PreviewActivity.class);
        intent.putExtra("PreviewImg", imgUrl);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, previewAnchor, "shareNames");
        ActivityCompat.startActivity(this, intent, compat.toBundle());
    }

    private void initData() {
        if (mTopItem == null) {

        } else {
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
                        imgList.addAll(resp.list);
                        adapter.notifyDataSetChanged();
                    }
                });
            } else if (mTopItem.hasError) {
                errorLin.setVisibility(View.VISIBLE);
                errorTv.setText(mTopItem.errorInfo);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                List<UploadImgItemBean> toAddList = new ArrayList<>();
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = mRole;
                    item.type = mType;
                    toAddList.add(item);
                }

                imgList.addAll(toAddList);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), files.size());

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (UploadImgItemBean imgItemBean : toAddList) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, imgItemBean.local_path, new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
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
        imgList = ((ArrayList<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList"));
        setResult(RESULT_OK, mGetIntent);
        finish();
    }

    public static class OnlyReadUploadImgListAdapter extends RecyclerView.Adapter<OnlyReadUploadImgListAdapter.VH> {

        private LayoutInflater mLayoutInflater;
        private List<UploadImgItemBean> mItems;
        private Context mContext;
        private OnItemClick mOnItemClick;

        public OnlyReadUploadImgListAdapter(Context context, List<UploadImgItemBean> items) {
            mItems = items;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = mLayoutInflater.inflate(R.layout.upload_list_img_item, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            UploadImgItemBean item = mItems.get(position);
            Dialog dialog = LoadingUtils.createLoadingDialog(mContext);
            dialog.show();
            if (!TextUtils.isEmpty(item.local_path)) {
                Glide.with(mContext).load(new File(item.local_path)).listener(new GlideRequestListener(dialog)).into(holder.img);
            } else {
                Glide.with(mContext).load(item.s_url).listener(new GlideRequestListener(dialog)).into(holder.img);
            }
            holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item));
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

        //size+1是因为有 添加图片的item
        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        protected class VH extends RecyclerView.ViewHolder {
            public ImageView img;

            public VH(View itemView) {
                super(itemView);
                img = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_img));
            }
        }

        public interface OnItemClick {
            void onItemClick(View v, UploadImgItemBean item);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }

    }

}
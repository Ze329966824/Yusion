package com.yusion.shanghai.yusion.ui.upload;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.utils.LoadingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ice on 2017/8/30.
 */

public class OnlyReadUploadListActivity extends BaseActivity {

    private OnlyReadUploadImgListAdapter adapter;
    private Intent mGetIntent;
    private String type;
    private String title;
    private String clt_id;
    private List<UploadImgItemBean> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);

        mGetIntent = getIntent();
        type = mGetIntent.getStringExtra("type");
        title = mGetIntent.getStringExtra("title");
        clt_id = mGetIntent.getStringExtra("clt_id");
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(this, title).setLeftClickListener(v -> onBack());
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
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
            if (resp.list.size() > 0) {
                imgList.addAll(resp.list);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(OnlyReadUploadListActivity.this, "暂无图片", Toast.LENGTH_SHORT).show();
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

    static class OnlyReadUploadImgListAdapter extends RecyclerView.Adapter<OnlyReadUploadImgListAdapter.VH> {

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
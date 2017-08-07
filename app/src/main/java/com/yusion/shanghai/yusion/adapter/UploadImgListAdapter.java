package com.yusion.shanghai.yusion.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;

import java.io.File;
import java.util.List;

/**
 * Created by ice on 2017/7/17.
 */

public class UploadImgListAdapter extends RecyclerView.Adapter<UploadImgListAdapter.VH> {

    private LayoutInflater mLayoutInflater;
    private List<UploadImgItemBean> mItems;
    private Context mContext;
    private OnItemClick mOnItemClick;
    public static final int TYPE_ADD_IMG = 100;
    public static final int TYPE_IMG = 101;

    public UploadImgListAdapter(Context context, List<UploadImgItemBean> items) {
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
            ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.show();
            if (!TextUtils.isEmpty(item.local_path)) {
                Glide.with(mContext).load(new File(item.local_path)).listener(new GlideRequestListener(dialog)).into(holder.img);
            } else {
                Glide.with(mContext).load(item.s_url).listener(new GlideRequestListener(dialog)).into(holder.img);
            }
            holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item));
        }
    }

    private class GlideRequestListener implements RequestListener<Drawable> {
        private ProgressDialog dialog;

        public GlideRequestListener(ProgressDialog dialog) {
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

        public VH(View itemView) {
            super(itemView);
            img = ((ImageView) itemView.findViewById(R.id.upload_list_img_item_img));
        }
    }

    public interface OnItemClick {
        void onItemClick(View v, UploadImgItemBean item);

        void onFooterClick(View v);
    }

    public void setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

}

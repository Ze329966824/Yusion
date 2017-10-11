package com.yusion.shanghai.yusion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;

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
    private boolean isEditing = false;

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
//            Dialog dialog = LoadingUtils.createLoadingDialog(mContext);
//            dialog.show();
//            if (!TextUtils.isEmpty(item.local_path)) {
//                Glide.with(mContext).load(new File(item.local_path)).listener(new GlideRequestListener(dialog)).into(holder.img);
//            } else {
//                Glide.with(mContext).load(item.s_url).listener(new GlideRequestListener(dialog)).into(holder.img);
//            }
            holder.itemView.setOnClickListener(mOnItemClick == null ? null : (View.OnClickListener) v -> mOnItemClick.onItemClick(v, item, holder.cbImg));
            if (isEditing) {
                holder.cbImg.setVisibility(View.VISIBLE);
                if (item.hasChoose) {
                    holder.cbImg.setImageResource(R.mipmap.surechoose_icon);
                    holder.cbImg.setTag(R.id.hasChoose, true);
                } else {
                    holder.cbImg.setImageResource(R.mipmap.choose_icon);
                    holder.cbImg.setTag(R.id.hasChoose, false);
                }

            } else {
                holder.cbImg.setVisibility(View.GONE);
            }
        }
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
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
        void onItemClick(View v, UploadImgItemBean item, ImageView cbImg);

        void onFooterClick(View v);
    }

    public void setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

}

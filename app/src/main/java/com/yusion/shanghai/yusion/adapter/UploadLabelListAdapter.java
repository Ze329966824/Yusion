package com.yusion.shanghai.yusion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;

import java.util.List;

/**
 * Created by ice on 2017/7/17.
 */

public class UploadLabelListAdapter extends RecyclerView.Adapter<UploadLabelListAdapter.VH> {

    private LayoutInflater mLayoutInflater;
    private List<UploadLabelItemBean> mItems;
    private Context mContext;
    private OnItemClick mOnItemClick;

    public UploadLabelListAdapter(Context context, List<UploadLabelItemBean> items) {
        mItems = items;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mLayoutInflater.inflate(R.layout.upload_label_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        UploadLabelItemBean item = mItems.get(position);
        holder.name.setText(item.name);
        if (item.hasError) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.status.setVisibility(View.GONE);
        } else {
            holder.icon.setVisibility(View.GONE);
            holder.status.setVisibility(View.VISIBLE);
            if (!hasImg(item)) {
                holder.status.setText("请上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.please_upload_color));
            } else {
                holder.status.setText("已上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.system_color));
            }
        }
        holder.itemView.setOnClickListener(mOnItemClick == null ? null : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.onItemClick(v, item, position);
            }
        });
    }

    private boolean hasImg(UploadLabelItemBean itemBean) {
        if (itemBean.label_list.size() == 0) {
            return itemBean.img_list.size() != 0;
        } else {
            for (UploadLabelItemBean labelItemBean : itemBean.label_list) {
                if (hasImg(labelItemBean)) return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public interface OnItemClick {
        //需要返回index以便在集合中替换元素
        void onItemClick(View v, UploadLabelItemBean item, int index);
    }

    public void setOnItemClick(OnItemClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    protected class VH extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView status;
        public TextView mark;
        public ImageView icon;

        public VH(View itemView) {
            super(itemView);
            name = ((TextView) itemView.findViewById(R.id.upload_label_list_item_name_tv));
            status = ((TextView) itemView.findViewById(R.id.upload_label_list_item_status_tv));
            icon = ((ImageView) itemView.findViewById(R.id.upload_label_list_icon_img));
            mark = ((TextView) itemView.findViewById(R.id.upload_label_list_item_mark_tv));
        }
    }
}

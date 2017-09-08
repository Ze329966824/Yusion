package com.yusion.shanghai.yusion.ui.upload;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.upload.ListLabelsErrorReq;
import com.yusion.shanghai.yusion.bean.upload.ListLabelsErrorResp;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.ui.apply.guarantor.DocumentFromLabelListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ice on 2017/9/8.
 */

public class UploadLabelListActivity extends BaseActivity {
    private RvAdapter adapter;
    private List<UploadLabelItemBean> mItems = new ArrayList<>();
    private Intent mGetIntent;
    private String clt_id;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_label_list);
        mGetIntent = getIntent();
        initTitleBar(this, "个人影像件").setRightTextColor(Color.WHITE).setLeftClickListener(v -> onBack());
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UploadLabelListActivity.RvAdapter(this, mItems);
        rv.setAdapter(adapter);
        adapter.setOnItemClick(new RvAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadLabelItemBean item, int index) {
                Intent intent = new Intent();
                intent.setClass(UploadLabelListActivity.this, UploadListActivity.class);
                if (item.name.contains("授权书")) {
                    intent.setClass(UploadLabelListActivity.this, OnlyReadUploadListActivity.class);
                } else if (item.name.contains("人像面")) {
                    intent.setClass(UploadLabelListActivity.this, DocumentFromLabelListActivity.class);
                }
                intent.putExtra("type", item.value);//id_card_back
                intent.putExtra("title", item.name);//身份证人像面
                intent.putExtra("role", role);
                intent.putExtra("clt_id", clt_id);
                startActivity(intent);
            }
        });
        initData();
    }

    private void initData() {
        clt_id = mGetIntent.getStringExtra("clt_id");
        role = mGetIntent.getStringExtra("role");

        clt_id = "2ce3b4ee7be711e7a7560242ac110003";
        role = "lender";

        try {
            JSONArray jsonArray = new JSONArray(YusionApp.CONFIG_RESP.client_material);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UploadLabelItemBean uploadLabelItemBean = new Gson().fromJson(jsonObject.toString(), UploadLabelItemBean.class);
                mItems.add(uploadLabelItemBean);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        ListLabelsErrorReq req = new ListLabelsErrorReq();
        req.clt_id = clt_id;
        ArrayList<String> labelsList = new ArrayList<>();
        for (UploadLabelItemBean itemBean : mItems) {
            labelsList.add(itemBean.value);
        }
        req.label_list = labelsList;

        UploadApi.listLabelsError(this, req, new OnItemDataCallBack<ListLabelsErrorResp>() {
            @Override
            public void onItemDataCallBack(ListLabelsErrorResp resp) {
                loop:
                for (UploadLabelItemBean uploadLabelItemBean : mItems) {
                    for (String hasImgLabel : resp.has_img_labels) {
                        if (uploadLabelItemBean.value.equals(hasImgLabel)) {
                            uploadLabelItemBean.hasImg = true;
                            continue loop;
                        } else {
                            uploadLabelItemBean.hasImg = false;
                        }
                    }
                }

                loop:
                for (UploadLabelItemBean uploadLabelItemBean : mItems) {
                    for (String hasErrorLabel : resp.err_labels) {
                        if (uploadLabelItemBean.value.equals(hasErrorLabel)) {
                            uploadLabelItemBean.hasError = true;
                            continue loop;
                        } else {
                            uploadLabelItemBean.hasError = false;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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
        private List<UploadLabelItemBean> mItems;
        private Context mContext;
        private OnItemClick mOnItemClick;

        public RvAdapter(Context context, List<UploadLabelItemBean> items) {
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

            if (item.hasImg) {
                holder.status.setText("已上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.system_color));
            } else {
                holder.status.setText("请上传");
                holder.status.setTextColor(mContext.getResources().getColor(R.color.please_upload_color));
            }
            holder.icon.setVisibility(View.GONE);
            if (item.hasError) {
                holder.status.setVisibility(View.GONE);
                holder.icon.setVisibility(View.VISIBLE);
            }

            //用户端授权书只允许查看且不显示错误
            if (item.name.contains("授权书")) {
                holder.status.setVisibility(View.VISIBLE);
                holder.icon.setVisibility(View.GONE);
                holder.status.setText("查看");
                if (item.hasImg) {
                    holder.status.setTextColor(mContext.getResources().getColor(R.color.system_color));
                } else {
                    holder.status.setTextColor(mContext.getResources().getColor(R.color.please_upload_color));
                }
            }

            holder.itemView.setOnClickListener(mOnItemClick == null ? null : new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClick.onItemClick(v, item, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }

        public interface OnItemClick {
            void onItemClick(View v, UploadLabelItemBean item, int index);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }

        public class VH extends RecyclerView.ViewHolder {

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
}

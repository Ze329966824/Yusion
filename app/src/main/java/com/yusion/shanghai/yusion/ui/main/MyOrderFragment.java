package com.yusion.shanghai.yusion.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.order.GetAppListResp;
import com.yusion.shanghai.yusion.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.ui.order.FinancePlanActivity;
import com.yusion.shanghai.yusion.ui.order.OrderDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/8/4.
 */

public class MyOrderFragment extends BaseFragment {
    private RecyclerAdapterWithHF adapter;
    private List<GetAppListResp> items;
    private PtrClassicFrameLayout ptr;
    private RecyclerView rv;
    private LinearLayout llyt;

    public static MyOrderFragment newInstance() {
        Bundle args = new Bundle();
        MyOrderFragment fragment = new MyOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleBar(view, "我的申请");

        rv = (RecyclerView) view.findViewById(R.id.my_order_rv);
        llyt = (LinearLayout) view.findViewById(R.id.my_order_llyt);
        rv.setLayoutManager(new LinearLayoutManager(mContext));
        items = new ArrayList<>();
        MyOrderListAdapter myOrderListAdapter = new MyOrderListAdapter(mContext, items);
        adapter = new RecyclerAdapterWithHF(myOrderListAdapter);
        rv.setAdapter(adapter);

        ptr = (PtrClassicFrameLayout) view.findViewById(R.id.my_order_ptr);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                items.clear();
                refreshData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, rv, header);
            }
        });

        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    //getArguments().getString("st")
    private void refreshData() {
        //getArguments().getString("st");
        OrderApi.getAppList(mContext, "0", new OnItemDataCallBack<List<GetAppListResp>>() {
            @Override
            public void onItemDataCallBack(List<GetAppListResp> resp) {
                if (resp != null && resp.size() > 0) {
                    ptr.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                    llyt.setVisibility(View.GONE);
                    items.clear();
                    items.addAll(resp);
                    adapter.notifyDataSetChanged();
                    ptr.refreshComplete();
                } else {
                    ptr.refreshComplete();
                    rv.setVisibility(View.GONE);
                    llyt.setVisibility(View.VISIBLE);
                    ptr.setVisibility(View.VISIBLE);
                }
//                items.clear();
//                items.addAll(resp);
//                adapter.notifyDataSetChanged();
//                ptr.refreshComplete();
            }
        });
    }

    static class MyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater mLayoutInflater;
        private Context mContext;
        private List<GetAppListResp> mItems;
        private OnItemClick mOnItemClick;

        public MyOrderListAdapter(Context context, List<GetAppListResp> items) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            mItems = items;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(mLayoutInflater.inflate(R.layout.order_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VH vh = (VH) holder;
            GetAppListResp item = mItems.get(position);
            if (item.status_st == 4) {
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FinancePlanActivity.class);
                        intent.putExtra("app_id", item.app_id);
                        mContext.startActivity(intent);
                    }
                });
            } else {
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
                        intent.putExtra("app_id", item.app_id);
                        mContext.startActivity(intent);
                    }
                });
            }

//            if (item.uw && item.uw_confirmed == 1) {
//                vh.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
//                        intent.putExtra("app_id", item.app_id);
//                        mContext.startActivity(intent);
//                    }
//                });
//            } else if (item.uw && item.uw_confirmed != 1) {
//                vh.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, FinancePlanActivity.class);
//                        intent.putExtra("app_id", item.app_id);
//                        mContext.startActivity(intent);
//                    }
//                });
//            } else if (!item.uw) {
//                vh.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
//                        intent.putExtra("app_id", item.app_id);
//                        mContext.startActivity(intent);
//                    }
//                });
//            }
            vh.salesName.setText(item.dlr_sales_nm);
            vh.name.setText(item.clt_nm);
            vh.door.setText(item.dlr_nm);
            vh.brand.setText(item.brand);
            vh.model.setText(item.model_name);
            vh.trix.setText(item.trix);
            vh.time.setText(item.app_ts);
            vh.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.dlr_sales_mobile));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
            if (item.status_st == 2) {//待审核
                vh.st.setTextColor(Color.parseColor("#FFA400"));
                vh.line_21.setBackgroundColor(Color.parseColor("#06b7a3"));
                vh.point_image_1.setBackgroundResource(R.drawable.poi_green);
                vh.point_image_2.setBackgroundResource(R.drawable.poi_green);
            } else if (item.status_st == 3) {//审核失败
                vh.st.setTextColor(Color.parseColor("#FF3F00"));
                vh.line_21.setBackgroundColor(Color.parseColor("#FF3F00"));
                //vh.point_image_2.setBackgroundResource(R.drawable.poi_red);
                vh.point_image_2.setBackgroundResource(R.drawable.poi_red);
            } else if (item.status_st == 4) {//待确认金融方案
                vh.st.setTextColor(Color.parseColor("#FFA400"));
            } else if (item.status_st == 6) {//放款中
                vh.line_41.setBackgroundColor(Color.parseColor("#06b7a3"));
                vh.line_21.setBackgroundColor(Color.parseColor("#06b7a3"));
                vh.line_22.setBackgroundColor(Color.parseColor("#06b7a3"));
                vh.point_image_2.setBackgroundResource(R.drawable.poi_green);
                vh.st.setTextColor(Color.parseColor("#06B7A3"));
            } else if (item.status_st == 9) {//已取消
                vh.st.setTextColor(Color.parseColor("#666666"));
                vh.point_image_1.setBackgroundResource(R.drawable.poi_grey);
                vh.line_12.setBackgroundColor(Color.parseColor("#dddddd"));
            }
            vh.st.setText(item.status_code);//待审核  status_code status_st: 2
            vh.periods.setText(item.nper);
            vh.loan.setText(item.loan_amt);
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }


        protected class VH extends RecyclerView.ViewHolder {

            public TextView name;
            public TextView st;
            public TextView door;
            public TextView time;
            public TextView model;
            public TextView brand;
            public TextView trix;
            public TextView loan;
            public TextView periods;
            public ImageView phone;
            public TextView salesName;
            public View line_21;
            public View line_22;
            public View line_41;
            public View line_12;

            public ImageView point_image_2;
            public ImageView point_image_1;
            public ImageView point_image_4;

            public VH(View itemView) {
                super(itemView);
                name = ((TextView) itemView.findViewById(R.id.order_list_item_name_tv));
                salesName = ((TextView) itemView.findViewById(R.id.order_list_item_sales_name_tv));
                st = ((TextView) itemView.findViewById(R.id.order_list_item_st_tv));
                door = ((TextView) itemView.findViewById(R.id.order_list_item_door_tv));
                time = ((TextView) itemView.findViewById(R.id.order_list_item_time_tv));
                model = ((TextView) itemView.findViewById(R.id.order_list_item_model_tv));
                brand = ((TextView) itemView.findViewById(R.id.order_item_brand));
                trix = ((TextView) itemView.findViewById(R.id.order_list_item_trix_tv));
                loan = ((TextView) itemView.findViewById(R.id.order_list_item_total_loan_tv));
                periods = ((TextView) itemView.findViewById(R.id.order_list_item_periods_tv));
                phone = ((ImageView) itemView.findViewById(R.id.order_list_item_sales_phone_img));

                line_21 = itemView.findViewById(R.id.point_line_21);
                line_22 = itemView.findViewById(R.id.point_line_22);
                line_41 = itemView.findViewById(R.id.point_line_41);
                line_12 = itemView.findViewById(R.id.point_line_12);

                point_image_2 = (ImageView) itemView.findViewById(R.id.point_image_2);
                point_image_1 = (ImageView) itemView.findViewById(R.id.point_image_1);
                point_image_4 = (ImageView) itemView.findViewById(R.id.point_image_4);

            }
        }


        public interface OnItemClick {
            void OnItemClick(View v);
        }

        public void setOnItemClick(OnItemClick mOnItemClick) {
            this.mOnItemClick = mOnItemClick;
        }
    }
}

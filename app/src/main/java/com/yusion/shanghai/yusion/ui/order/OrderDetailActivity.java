package com.yusion.shanghai.yusion.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.order.GetAppDetailResp;
import com.yusion.shanghai.yusion.bean.order.GetFinancePlanDetailResp;
import com.yusion.shanghai.yusion.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;


public class OrderDetailActivity extends BaseActivity {

    private RelativeLayout waitRel;
    private RelativeLayout cancelRel;
    private RelativeLayout passRel;
    private RelativeLayout rejectRel;
    private LinearLayout replyLin;
    private LinearLayout applyLin;
    private TextView applyBillPriceTv;
    private TextView applyFirstPriceTv;
    private TextView applyLoanPriceTv;
    private TextView applyManagementPriceTv;
    private TextView applyOtherPriceTv;
    private TextView applyTotalLoanPriceTv;
    private TextView applyLoanBankTv;
    private TextView applyProductTypeTv;
    private TextView applyPeriodsTv;
    private TextView replayBillPriceTv;
    private TextView replayFirstPriceTv;
    private TextView replayLoanPriceTv;
    private TextView replayManagementPriceTv;
    private TextView replayOtherPriceTv;
    private TextView replayTotalLoanPriceTv;
    private TextView replayLoanBankTv;
    private TextView replayProductTypeTv;
    private TextView replayPeriodsTv;
    private TextView dlrNameTv;
    private TextView salesNameTv;
    private TextView customerNameTv;
    private TextView brandTv;
    private TextView trixTv;
    private TextView modelTv;
    private TextView guidePriceTv;
    private TextView waitReason;
    private TextView cancelReason;
    private TextView passReason;
    private TextView rejectReason;

    //申请和批复的金融方案
    private TextView applyFirstPercentTv2;
    private TextView replyFirstPercentTv2;
    private TextView applyBillPriceTv2;
    private TextView replyBillPriceTv2;

    private TextView applyFirstPriceTv2;
    private TextView replyFirstPriceTv2;

    private TextView applyLoanPriceTv2;
    private TextView replyLoanPriceTv2;

    private TextView applyManagementPriceTv2;
    private TextView replyManagementPriceTv2;

    private TextView applyOtherPriceTv2;
    private TextView replyOtherPriceTv2;

    private TextView applyTotalPriceTv2;
    private TextView replyTotalPriceTv2;

    private TextView applyBankTv2;
    private TextView replyBankTv2;

    private TextView applyReplyDateTv2;
    private TextView ReplyRepayDateTv2;

    private FloatingActionButton fab;
    private NestedScrollView mScrollView;

    private TextView applyMonthPrice;
    private TextView replyMonthPrice;

    private TextView monthPrice;

    private LinearLayout orderDetailFianceLin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initTitleBar(this, "申请详情");
        initView();
        initData();

//        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mScrollView.smoothScrollTo(0,0);
//            }
//        });
    }

    private void initView() {
        mScrollView = (NestedScrollView) findViewById(R.id.scrollview_fiance);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.smoothScrollTo(0, 0);
            }
        });
        waitRel = (RelativeLayout) findViewById(R.id.order_detail_status_wait_layout);
        cancelRel = (RelativeLayout) findViewById(R.id.order_detail_status_cancel_layout);
        passRel = (RelativeLayout) findViewById(R.id.order_detail_status_pass_layout);
        rejectRel = (RelativeLayout) findViewById(R.id.order_detail_status_reject_layout);
        replyLin = (LinearLayout) findViewById(R.id.order_detail_reply_lin);
        applyLin = (LinearLayout) findViewById(R.id.order_detail_apply_lin);
        waitReason = (TextView) findViewById(R.id.order_detail_status_wait_reason);
        cancelReason = (TextView) findViewById(R.id.order_detail_status_cancel_reason);
        passReason = (TextView) findViewById(R.id.order_detail_status_pass_reason);
        rejectReason = (TextView) findViewById(R.id.order_detail_status_reject_reason);

        applyBillPriceTv = (TextView) findViewById(R.id.order_detail_apply_bill_price_tv);
        applyFirstPriceTv = (TextView) findViewById(R.id.order_detail_apply_first_price_tv);
        applyLoanPriceTv = (TextView) findViewById(R.id.order_detail_apply_loan_price_tv);
        applyManagementPriceTv = (TextView) findViewById(R.id.order_detail_apply_management_price_tv);
        applyOtherPriceTv = (TextView) findViewById(R.id.order_detail_apply_other_price_tv);
        applyTotalLoanPriceTv = (TextView) findViewById(R.id.order_detail_apply_total_loan_price_tv);
        applyLoanBankTv = (TextView) findViewById(R.id.order_detail_apply_loan_bank_price_tv);
        applyProductTypeTv = (TextView) findViewById(R.id.order_detail_apply_product_type_tv);
        applyPeriodsTv = (TextView) findViewById(R.id.order_detail_apply_periods_tv);

        replayBillPriceTv = (TextView) findViewById(R.id.order_detail_replay_bill_price_tv);
        replayFirstPriceTv = (TextView) findViewById(R.id.order_detail_replay_first_price_tv);
        replayLoanPriceTv = (TextView) findViewById(R.id.order_detail_replay_loan_price_tv);
        replayManagementPriceTv = (TextView) findViewById(R.id.order_detail_replay_management_price_tv);
        replayOtherPriceTv = (TextView) findViewById(R.id.order_detail_replay_other_price_tv);
        replayTotalLoanPriceTv = (TextView) findViewById(R.id.order_detail_replay_total_loan_price_tv);
        replayLoanBankTv = (TextView) findViewById(R.id.order_detail_replay_loan_bank_price_tv);
        replayProductTypeTv = (TextView) findViewById(R.id.order_detail_replay_product_type_tv);
        replayPeriodsTv = (TextView) findViewById(R.id.order_detail_replay_periods_tv);

        brandTv = (TextView) findViewById(R.id.order_detail_brand_tv);
        trixTv = (TextView) findViewById(R.id.order_detail_trix_tv);
        modelTv = (TextView) findViewById(R.id.order_detail_model_tv);
        guidePriceTv = (TextView) findViewById(R.id.order_detail_guide_price_tv);


        dlrNameTv = (TextView) findViewById(R.id.order_detail_dlr_name_tv);
        salesNameTv = (TextView) findViewById(R.id.order_detail_sales_name_tv);
        customerNameTv = (TextView) findViewById(R.id.order_detail_customer_name_tv);

        monthPrice = (TextView) findViewById(R.id.order_detail_month_price_tv);

        //批复和申请的金融方案
        applyFirstPercentTv2 = (TextView) findViewById(R.id.apply_first_percent_tv);
        replyFirstPercentTv2 = (TextView) findViewById(R.id.reply_first_percent_tv);
        applyBillPriceTv2 = (TextView) findViewById(R.id.apply_bill_price_tv2);
        replyBillPriceTv2 = (TextView) findViewById(R.id.reply_bill_price_tv2);

        applyFirstPriceTv2 = (TextView) findViewById(R.id.apply_first_price_tv2);
        replyFirstPriceTv2 = (TextView) findViewById(R.id.reply_first_price_tv2);

        applyLoanPriceTv2 = (TextView) findViewById(R.id.apply_loan_price_tv2);
        replyLoanPriceTv2 = (TextView) findViewById(R.id.reply_loan_price_tv2);

        applyManagementPriceTv2 = (TextView) findViewById(R.id.apply_management_price_tv2);
        replyManagementPriceTv2 = (TextView) findViewById(R.id.reply_management_price_tv2);

        applyOtherPriceTv2 = (TextView) findViewById(R.id.apply_other_price_tv2);
        replyOtherPriceTv2 = (TextView) findViewById(R.id.reply_other_price_tv2);

        applyBankTv2 = (TextView) findViewById(R.id.apply_bank_tv2);
        replyBankTv2 = (TextView) findViewById(R.id.reply_bank_tv2);

        applyReplyDateTv2 = (TextView) findViewById(R.id.apply_repay_date_tv2);
        ReplyRepayDateTv2 = (TextView) findViewById(R.id.reply_repay_data_tv2);

        applyTotalPriceTv2 = (TextView) findViewById(R.id.apply_total_price_tv2);
        replyTotalPriceTv2 = (TextView) findViewById(R.id.reply_total_price_tv2);

        applyMonthPrice = (TextView) findViewById(R.id.apply_month_price_tv2);
        replyMonthPrice = (TextView) findViewById(R.id.reply_month_price_tv2);

        orderDetailFianceLin = (LinearLayout) findViewById(R.id.order_detail_fiance_lin);

    }

    private void initData() {
        String app_id = getIntent().getStringExtra("app_id");
        OrderApi.getAppDetails(this, app_id, new OnItemDataCallBack<OrderDetailBean>() {
            @Override
            public void onItemDataCallBack(OrderDetailBean resp) {

                if (resp.status_st == 2) {//2是待审核
                    waitRel.setVisibility(View.VISIBLE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.VISIBLE);
                    replyLin.setVisibility(View.GONE);
                    waitReason.setText(resp.uw_detail.comments);
                    orderDetailFianceLin.setVisibility(View.GONE);
                } else if (resp.status_st == 4) {
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.VISIBLE);
                    replyLin.setVisibility(View.VISIBLE);
                    passReason.setText(resp.uw_detail.comments);
                    orderDetailFianceLin.setVisibility(View.VISIBLE);
                } else if (resp.status_st == 6) {
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    applyLin.setVisibility(View.VISIBLE);
                    replyLin.setVisibility(View.VISIBLE);
                    passReason.setText(resp.uw_detail.comments);
                    orderDetailFianceLin.setVisibility(View.VISIBLE);
                } else if (resp.status_st == 3) {
                    waitRel.setVisibility(View.GONE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.VISIBLE);
                    rejectReason.setText(resp.uw_detail.comments);
                    orderDetailFianceLin.setVisibility(View.GONE);
                }

                applyBillPriceTv.setText(resp.vehicle_price);
                applyFirstPriceTv.setText(resp.vehicle_down_payment);
                applyLoanPriceTv.setText(resp.vehicle_loan_amt);
                applyManagementPriceTv.setText(resp.management_fee);
                applyOtherPriceTv.setText(resp.other_fee);
                applyTotalLoanPriceTv.setText(resp.loan_amt);
                applyLoanBankTv.setText(resp.loan_bank);
                applyProductTypeTv.setText(resp.product_type);
                applyPeriodsTv.setText(resp.nper);


                replayBillPriceTv.setText(resp.uw_detail.vehicle_price);
                replayFirstPriceTv.setText(resp.uw_detail.vehicle_down_payment);
                replayLoanPriceTv.setText(resp.uw_detail.vehicle_loan_amt);
                replayManagementPriceTv.setText(resp.uw_detail.management_fee);
                replayOtherPriceTv.setText(resp.uw_detail.other_fee);
                replayTotalLoanPriceTv.setText(resp.uw_detail.loan_amt);
                replayLoanBankTv.setText(resp.loan_bank);
                replayProductTypeTv.setText(resp.product_type);
                replayPeriodsTv.setText(resp.uw_detail.nper);

                brandTv.setText(resp.brand);
                trixTv.setText(resp.trix);
                modelTv.setText(resp.model_name);
                guidePriceTv.setText(resp.msrp);

                dlrNameTv.setText(resp.dlr_nm);
                salesNameTv.setText(resp.dlr_sales_name);
                // customerNameTv.setText(resp.dlr_dfim_name);

                monthPrice.setText(resp.monthly_payment);

                customerNameTv.setText(resp.dlr_dfim_name + "");
                findViewById(R.id.order_detail_customer_mobile_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.mobile));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                findViewById(R.id.order_detail_sales_mobile_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.dlr_sales_mobile));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

            }

        });
        OrderApi.getFinancePlanDetail(this, app_id, new OnItemDataCallBack<GetFinancePlanDetailResp>() {
            @Override
            public void onItemDataCallBack(GetFinancePlanDetailResp resp) {
                if (resp != null) {
                    applyBillPriceTv2.setText(resp.getApp().getVehicle_price());
                    replyBillPriceTv2.setText(resp.getUw().getVehicle_price());
                    //compare(resp.getApp().getVehicle_price(),resp.getUw().getVehicle_price(),applyBillPriceTv,replyBillPriceTv);

                    applyFirstPriceTv2.setText(resp.getApp().getVehicle_down_payment());
                    replyFirstPriceTv2.setText(resp.getUw().getVehicle_down_payment());

                    applyLoanPriceTv2.setText(resp.getApp().getVehicle_loan_amt());
                    replyLoanPriceTv2.setText(resp.getUw().getVehicle_down_payment());

                    applyManagementPriceTv2.setText(resp.getApp().getManagement_fee());
                    replyManagementPriceTv2.setText(resp.getUw().getManagement_fee());

                    applyOtherPriceTv2.setText(resp.getApp().getOther_fee());
                    replyOtherPriceTv2.setText(resp.getUw().getOther_fee());

                    applyTotalPriceTv2.setText(resp.getApp().getLoan_amt());
                    replyTotalPriceTv2.setText(resp.getUw().getLoan_amt());

                    applyBankTv2.setText(resp.getApp().getLoan_bank());
                    replyBankTv2.setText(resp.getUw().getLoan_bank());

                    applyReplyDateTv2.setText(resp.getApp().getNper() + "期");
                    ReplyRepayDateTv2.setText(resp.getUw().getNper() + "期");

                    applyFirstPercentTv2.setText(resp.getApp().getVehicle_down_payment_percent() * 100 + "%");
                    replyFirstPercentTv2.setText(resp.getUw().getVehicle_down_payment_percent() * 100 + "%");

                    applyMonthPrice.setText(resp.getApp().getMonthly_payment());
                    replyMonthPrice.setText(resp.getUw().getMonthly_payment());
                }
            }
        });
        compare(applyMonthPrice, replyMonthPrice);
        compare(applyFirstPercentTv2, replyFirstPercentTv2);
        compare(applyBillPriceTv2, replyBillPriceTv2);
        compare(applyFirstPriceTv2, replyFirstPriceTv2);
        compare(applyLoanPriceTv2, replyLoanPriceTv2);
        compare(applyManagementPriceTv2, replyManagementPriceTv2);
        compare(applyOtherPriceTv2, replyOtherPriceTv2);
        compare(applyTotalPriceTv2, replyTotalPriceTv2);
        compare(applyBankTv2, replyBankTv2);
        compare(applyReplyDateTv2, ReplyRepayDateTv2);
    }

    private void compare(TextView tv1, TextView tv2) {
        if (tv1.getText().toString().compareTo(tv2.getText().toString()) == 0) {
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#222a36"));
        } else {
            tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#CBA053"));
        }
    }

}


package com.yusion.shanghai.yusion.ui.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.order.OrderDetailBean;
import com.yusion.shanghai.yusion.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;


public class OrderDetailActivity extends BaseActivity {

    private RelativeLayout waitRel;
    private RelativeLayout cancelRel;
    private RelativeLayout passRel;
    private RelativeLayout rejectRel;
    //    private LinearLayout replyLin;
    private LinearLayout nore_financeLin;         //金融方案
    private TextView applyBillPriceTv;        //金融方案-车辆开票价
    private TextView applyFirstPriceTv;       //金融方案-车辆首付款
    private TextView applyLoanPriceTv;        //金融方案-车辆贷款金额
    private TextView applyManagementPriceTv;  //金融方案-档案管理费
    private TextView applyOtherPriceTv;       //金融方案-其他费用
    private TextView applyTotalLoanPriceTv;   //金融方案-总贷款额
    private TextView applyLoanBankTv;         //金融方案-贷款银行
    private TextView applyProductTypeTv;      //金融方案-产品类型
    private TextView applyPeriodsTv;          //金融方案-还款期限
    private TextView applyMonthPriceTv;       //金融方案-月供


    private LinearLayout havere_financeLin;                //金融方案(有批复)
    private TextView havere_applyFirstPercentTv;     //金融方案(有批复)-首付比例(申请信息)
    private TextView havere_replyFirstPercentTv;     //金融方案(有批复)-首付比例(批复信息)
    private TextView havere_applyBillPriceTv;        //金融方案(有批复)-车辆开票价(申请信息)
    private TextView havere_replyBillPriceTv;        //金融方案(有批复)-车辆开票价(批复信息)
    private TextView havere_applyFirstPriceTv;       //金融方案(有批复)-车辆首付款(申请信息)
    private TextView havere_replyFirstPriceTv;       //金融方案(有批复)-车辆首付款(批复信息)
    private TextView havere_applyLoanPriceTv;        //金融方案(有批复)-车辆贷款金额(申请信息)
    private TextView havere_replyLoanPriceTv;        //金融方案(有批复)-车辆贷款金额(批复信息)
    private TextView havere_applyManagementPriceTv;  //金融方案(有批复)-档案管理费(申请信息)
    private TextView havere_replyManagementPriceTv;  //金融方案(有批复)-档案管理费(批复信息)
    private TextView havere_applyOtherPriceTv;       //金融方案(有批复)-其他费用(申请信息)
    private TextView havere_replyOtherPriceTv;       //金融方案(有批复)-其他费用(批复信息)
    private TextView havere_applyTotalPriceTv;       //金融方案(有批复)-总贷款额(申请信息)
    private TextView havere_replyTotalPriceTv;       //金融方案(有批复)-总贷款额(批复信息)
    private TextView havere_applyBankTv;             //金融方案(有批复)-贷款银行(申请信息)
    private TextView havere_replyBankTv;             //金融方案(有批复)-贷款银行(批复信息)
    private TextView havere_applyRepayDateTv;        //金融方案(有批复)-还款期限(申请信息)
    private TextView havere_replyRepayDateTv;        //金融方案(有批复)-还款期限(批复信息)
    private TextView havere_applyProductTypeTv;      //金融方案(有批复)-产品类型(申请信息)
    private TextView havere_replyProductTypeTv;      //金融方案(有批复)-产品类型(批复信息)
    private TextView havere_applyMonthPriceTv;       //金融方案(有批复)-月供(批复信息)
    private TextView havere_replyMonthPriceTv;       //金融方案(有批复)-月供(批复信息)


    private FloatingActionButton fab;

    private NestedScrollView mScrollView;


    private TextView dlrNameTv;                 //订单-门店
    private TextView brandTv;                   //订单-品牌
    private TextView trixTv;                    //订单-车系
    private TextView modelTv;                   //订单-车型
    private TextView guidePriceTv;              //订单-厂商指导价

    private TextView salesNameTv;                 //报单人员姓名
    private TextView customerNameTv;              //客户身份证号
    private LinearLayout order_detail_sale_info_layout;         //报单人员
    private LinearLayout order_detail_customer_info_layout;     //金融专员
    private LinearLayout order_detail_car_info_layout;          //订单信息


    //车辆信息修改
    private LinearLayout alter_carInfo_lin;      //订单(修改前后)
    private TextView alter_before_dlr;           //订单-门店(修改前)
    private TextView alter_after_dlr;            //订单-门店(修改后)
    private TextView alter_before_brand;         //订单-品牌(修改前)
    private TextView alter_after_brand;          //订单-品牌(修改后)
    private TextView alter_before_trix;          //订单-车系(修改前)
    private TextView alter_after_trix;           //订单-车系(修改后)
    private TextView alter_before_model;         //订单-车型(修改前)
    private TextView alter_after_model;          //订单-车型(修改后)
    private TextView alter_before_guiderpice;    //订单-厂商指导价(修改前)
    private TextView alter_after_guideprice;     //订单-厂商指导价(修改后)

    private TextView alter_before_totalpice;     //订单-总贷款额(修改前)
    private TextView alter_after_totalprice;     //订单-总贷款额(修改后)
    private TextView alter_before_monthpice;     //订单-月供(修改前)
    private TextView alter_after_monthprice;     //订单-月供(修改后)
    private TextView alter_before_date;          //订单-还款日期(修改前)
    private TextView alter_after_date;           //订单-还款日期(修改前)
    private TextView waitReason;        //审核中备注
    private TextView wait_title;        //审核中标题
    private TextView passReason;        //通过备注
    private TextView pass_title;        //通过标题
    private TextView reject_title;      //拒绝标题
    private TextView rejectReason;      //拒绝备注
    private TextView cancelReason;      //取消备注
    private TextView cancel_title;      //取消标题

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initTitleBar(this, "申请详情");
        initView();
        initData();
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
        order_detail_sale_info_layout = (LinearLayout) findViewById(R.id.order_detail_sale_info_layout);
        order_detail_customer_info_layout = (LinearLayout) findViewById(R.id.order_detail_customer_info_layout);
        waitRel = (RelativeLayout) findViewById(R.id.order_detail_status_wait_layout);
        cancelRel = (RelativeLayout) findViewById(R.id.order_detail_status_cancel_layout);
        passRel = (RelativeLayout) findViewById(R.id.order_detail_status_pass_layout);
        rejectRel = (RelativeLayout) findViewById(R.id.order_detail_status_reject_layout);
//        replyLin = (LinearLayout) findViewById(R.id.order_detail_reply_lin);
        nore_financeLin = (LinearLayout) findViewById(R.id.order_detail_finance_lin);
        waitReason = (TextView) findViewById(R.id.order_detail_status_wait_reason);
        cancelReason = (TextView) findViewById(R.id.order_detail_status_cancel_reason);
        passReason = (TextView) findViewById(R.id.order_detail_status_pass_reason);
        rejectReason = (TextView) findViewById(R.id.order_detail_status_reject_reason);

        applyBillPriceTv = (TextView) findViewById(R.id.order_detail_finance_bill_price_tv);
        applyFirstPriceTv = (TextView) findViewById(R.id.order_detail_finance_first_price_tv);
        applyLoanPriceTv = (TextView) findViewById(R.id.order_detail_finance_loan_price_tv);
        applyManagementPriceTv = (TextView) findViewById(R.id.order_detail_finance_management_price_tv);
        applyOtherPriceTv = (TextView) findViewById(R.id.order_detail_finance_other_price_tv);
        applyTotalLoanPriceTv = (TextView) findViewById(R.id.order_detail_finance_total_loan_price_tv);
        applyLoanBankTv = (TextView) findViewById(R.id.order_detail_finance_loan_bank_tv);
        applyProductTypeTv = (TextView) findViewById(R.id.order_detail_finance_product_type_tv);
        applyPeriodsTv = (TextView) findViewById(R.id.order_detail_finance_periods_tv);


        brandTv = (TextView) findViewById(R.id.order_detail_brand_tv);
        trixTv = (TextView) findViewById(R.id.order_detail_trix_tv);
        modelTv = (TextView) findViewById(R.id.order_detail_model_tv);
        guidePriceTv = (TextView) findViewById(R.id.order_detail_guide_price_tv);


        dlrNameTv = (TextView) findViewById(R.id.order_detail_dlr_name_tv);
        salesNameTv = (TextView) findViewById(R.id.order_detail_sales_name_tv);
        customerNameTv = (TextView) findViewById(R.id.order_detail_customer_name_tv);

        applyMonthPriceTv = (TextView) findViewById(R.id.order_detail_finance_month_price_tv);

        //批复和申请的金融方案
        havere_applyFirstPercentTv = (TextView) findViewById(R.id.order_detail_havere_apply_first_percent_tv);
        havere_replyFirstPercentTv = (TextView) findViewById(R.id.order_detail_havere_reply_first_percent_tv);
        havere_applyBillPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_bill_price_tv);
        havere_replyBillPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_bill_price_tv);

        havere_applyFirstPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_first_price_tv);
        havere_replyFirstPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_first_price_tv);

        havere_applyLoanPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_loan_price_tv);
        havere_replyLoanPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_loan_price_tv);

        havere_applyManagementPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_management_price_tv);
        havere_replyManagementPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_management_price_tv);

        havere_applyOtherPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_other_price_tv);
        havere_replyOtherPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_other_price_tv);

        havere_applyBankTv = (TextView) findViewById(R.id.order_detail_havere_apply_bank_tv);
        havere_replyBankTv = (TextView) findViewById(R.id.order_detail_havere_reply_bank_tv);

        havere_applyRepayDateTv = (TextView) findViewById(R.id.order_detail_havere_apply_repay_date_tv);
        havere_replyRepayDateTv = (TextView) findViewById(R.id.order_detail_havere_reply_repay_data_tv);

        havere_applyTotalPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_total_price_tv);
        havere_replyTotalPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_total_price_tv);

        havere_applyMonthPriceTv = (TextView) findViewById(R.id.order_detail_havere_apply_month_price_tv);
        havere_replyMonthPriceTv = (TextView) findViewById(R.id.order_detail_havere_reply_month_price_tv);

        havere_financeLin = (LinearLayout) findViewById(R.id.order_detail_havere_lin);

        //车辆信息对比修改
        order_detail_car_info_layout = (LinearLayout) findViewById(R.id.order_detail_car_info_layout);
        alter_carInfo_lin = (LinearLayout) findViewById(R.id.alter_carInfo_lin);
        //carinfoRel.setVisibility(View.GONE);
        alter_before_dlr = (TextView) findViewById(R.id.order_detail_before_dlr_name_tv);
        alter_after_dlr = (TextView) findViewById(R.id.order_detail_dlr_name_tv);
        alter_before_brand = (TextView) findViewById(R.id.order_detail_before_brand_tv);
        alter_after_brand = (TextView) findViewById(R.id.order_detail_brand_tv);
        alter_before_trix = (TextView) findViewById(R.id.order_detail_before_trix_tv);
        alter_after_trix = (TextView) findViewById(R.id.order_detail_trix_tv);
        alter_before_model = (TextView) findViewById(R.id.order_detail_before_model_tv);
        alter_after_model = (TextView) findViewById(R.id.order_detail_model_tv);
        alter_before_guiderpice = (TextView) findViewById(R.id.order_detail_before_guide_price_tv);
        alter_after_guideprice = (TextView) findViewById(R.id.order_detail_guide_price_tv);

        alter_before_totalpice = (TextView) findViewById(R.id.order_detail_before_loan_amt_tv);
        alter_after_totalprice = (TextView) findViewById(R.id.order_detail_loan_amt_tv);
        alter_before_monthpice = (TextView) findViewById(R.id.order_detail_before_monthpice_tv);
        alter_after_monthprice = (TextView) findViewById(R.id.order_detail__monthpice_tv);
        alter_before_date = (TextView) findViewById(R.id.order_detail_before_repaydate_tv);
        alter_after_date = (TextView) findViewById(R.id.order_detail_before_repaydate_tv);
        wait_title = (TextView) findViewById(R.id.order_detail_status_wait);
        pass_title = (TextView) findViewById(R.id.order_detail_status_pass);
        reject_title = (TextView) findViewById(R.id.order_detail_status_reject);
        cancel_title = (TextView) findViewById(R.id.order_detail_status_cancel);


    }

    private void initData() {
        String app_id = getIntent().getStringExtra("app_id");
        OrderApi.getAppDetails(this, app_id, new OnItemDataCallBack<OrderDetailBean>() {
            @Override
            public void onItemDataCallBack(OrderDetailBean resp) {
                if (resp == null) {
                    return;
                }
                if (resp.status_st == 2) {//2是待审核
                    waitRel.setVisibility(View.VISIBLE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    nore_financeLin.setVisibility(View.VISIBLE);//visiable
                    if (resp.uw) {
                        waitReason.setText(resp.uw_detail.comments);
                    }
                    wait_title.setText(resp.client_status_code);
                } else if (resp.status_st == 4) {//待确认金融方案 //有批复的
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    nore_financeLin.setVisibility(View.GONE);
                    pass_title.setText(resp.client_status_code);

                } else if (resp.status_st == 6) {//放款中      //有批复的
                    passRel.setVisibility(View.VISIBLE);
                    waitRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.GONE);
                    nore_financeLin.setVisibility(View.GONE);
                    if (resp.uw) {
                        passReason.setText(resp.uw_detail.comments);
                    }
                    pass_title.setText(resp.client_status_code);
                } else if (resp.status_st == 3) {
                    waitRel.setVisibility(View.GONE);
                    passRel.setVisibility(View.GONE);
                    rejectRel.setVisibility(View.VISIBLE);
                    if (resp.uw) {
                        rejectReason.setText(resp.uw_detail.comments);
                    }
                }
                if (resp.uw && resp.uw_detail != null) {
                    nore_financeLin.setVisibility(View.GONE);
                    havere_financeLin.setVisibility(View.VISIBLE);

                    havere_applyTotalPriceTv.setText(resp.loan_amt);
                    havere_replyTotalPriceTv.setText(resp.uw_detail.loan_amt);
                    havere_applyMonthPriceTv.setText(resp.monthly_payment);
                    havere_replyMonthPriceTv.setText(resp.uw_detail.monthly_payment);
                    havere_applyRepayDateTv.setText(resp.nper);
                    havere_replyRepayDateTv.setText(resp.uw_detail.nper);

                    compare(havere_applyMonthPriceTv, havere_replyMonthPriceTv);

                    compare(havere_applyTotalPriceTv, havere_replyTotalPriceTv);
                    compare(havere_applyRepayDateTv, havere_replyRepayDateTv);

                } else {
                    nore_financeLin.setVisibility(View.VISIBLE);
                    havere_financeLin.setVisibility(View.GONE);

                    applyTotalLoanPriceTv.setText(resp.loan_amt);

                    applyPeriodsTv.setText(resp.nper);
                    applyMonthPriceTv.setText(resp.monthly_payment);
                }



                if (resp.is_modify && resp.old_app != null) {
                    alter_carInfo_lin.setVisibility(View.VISIBLE);

                    order_detail_car_info_layout.setVisibility(View.GONE);
                    //车辆原信息和修改信息

                    alter_before_dlr.setText(resp.old_app.dlr_nm);
                    alter_after_dlr.setText(resp.new_app.dlr_nm);
                    alter_before_brand.setText(resp.old_app.brand);
                    alter_after_brand.setText(resp.new_app.brand);
                    alter_before_trix.setText(resp.old_app.trix);
                    alter_after_trix.setText(resp.new_app.trix);
                    alter_before_model.setText(resp.old_app.model_name);
                    alter_after_model.setText(resp.new_app.model_name);
                    alter_before_guiderpice.setText(resp.old_app.msrp);
                    alter_after_guideprice.setText(resp.new_app.msrp);

                    alter_before_totalpice.setText(resp.old_app.loan_amt);
                    alter_after_totalprice.setText(resp.new_app.loan_amt);
                    alter_before_monthpice.setText(resp.old_app.monthly_payment);
                    alter_after_monthprice.setText(resp.new_app.monthly_payment);
                    alter_before_date.setText(resp.old_app.nper);
                    alter_after_date.setText(resp.new_app.nper);

                    compare(alter_before_dlr, alter_after_dlr);
                    compare(alter_before_brand, alter_after_brand);
                    compare(alter_before_trix, alter_after_trix);
                    compare(alter_before_model, alter_after_model);
                    compare(alter_before_guiderpice, alter_after_guideprice);
                    compare(alter_before_totalpice, alter_after_totalprice);
                    compare(alter_before_monthpice, alter_after_monthprice);
                    compare(alter_before_date, alter_after_date);

                } else {
                    order_detail_car_info_layout.setVisibility(View.VISIBLE);
                    alter_carInfo_lin.setVisibility(View.GONE);

                    brandTv.setText(resp.brand);
                    trixTv.setText(resp.trix);
                    modelTv.setText(resp.model_name);
                    guidePriceTv.setText(resp.msrp);
                    dlrNameTv.setText(resp.dlr_nm);
                }



                if (TextUtils.isEmpty(resp.dlr_sales_name)) {
                    order_detail_sale_info_layout.setVisibility(View.GONE);
                } else {
                    order_detail_sale_info_layout.setVisibility(View.VISIBLE);
                    salesNameTv.setText(resp.dlr_sales_name);
                }


                if (TextUtils.isEmpty(resp.dlr_dfim_name)) {//金融专员
                    order_detail_customer_info_layout.setVisibility(View.GONE);
                } else {
                    order_detail_customer_info_layout.setVisibility(View.VISIBLE);
                    customerNameTv.setText(resp.dlr_dfim_name);
                }



                //金融专员信息
                findViewById(R.id.order_detail_customer_mobile_img).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + resp.dlr_dfim_mobile));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                //报单人员信息
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


    }

    private void compare(TextView tv1, TextView tv2) {
        if (tv1.getText().toString().equals(tv2.getText().toString())) {
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#222a36"));
        } else {
            tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#CBA053"));
        }
    }

}


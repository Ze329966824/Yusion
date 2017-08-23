package com.yusion.shanghai.yusion.ui.order;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.order.ConfirmFinancePlanReq;
import com.yusion.shanghai.yusion.bean.order.GetFinancePlanDetailResp;
import com.yusion.shanghai.yusion.retrofit.api.OrderApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class FinancePlanActivity extends BaseActivity {

    private int b1 = 90000;
    private int b2 = 90000;

    private TextView applyBillPriceTv;
    private TextView replyBillPriceTv;

    private int f1 = 70000;
    private int f2 = 60000;
    private TextView applyFirstPriceTv;
    private TextView replyFirstPriceTv;

    private int L1 = 80000;
    private int L2 = 90000;
    private TextView applyLoanPriceTv;
    private TextView replyLoanPriceTv;

    private int M1 = 300;
    private int M2 = 3000;
    private TextView applyManagementPriceTv;
    private TextView replyManagementPriceTv;

    private int O1 = 4000;
    private int O2 = 5000;

    private TextView applyOtherPriceTv;
    private TextView replyOtherPriceTv;

    private int T1 = 78000;
    private int T2 = 89000;
    private TextView applyTotalPriceTv;
    private TextView replyTotalPriceTv;

    private String bank1 = "工商银行";
    private String bank2 = "工商银行";
    private TextView applyBankTv;
    private TextView replyBankTv;

    private String date1 = "24期";
    private String date2 = "23期";

    private TextView applyReplyDateTv;
    private TextView ReplyRepayDateTv;


    //    private TextView carLoanPriceTv;
//    private TextView managementPriceTv;
//    private TextView otherPriceTv;
//    private TextView loanPriceTv;
//    private TextView loanBankTv;
//    private TextView periodsTv;
    private TextView percentTv;

    private Button confirmBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_plan);
        initTitleBar(this, "我的金融方案");

        //Integer integer = Integer.valueOf(applyBillPriceTv.setText("2000"));


        applyBillPriceTv = (TextView) findViewById(R.id.apply_bill_price_tv);
        replyBillPriceTv = (TextView) findViewById(R.id.reply_bill_price_tv);

        applyFirstPriceTv = (TextView) findViewById(R.id.apply_first_price_tv);
        replyFirstPriceTv = (TextView) findViewById(R.id.reply_first_price_tv);

        applyLoanPriceTv = (TextView) findViewById(R.id.apply_loan_price_tv);
        replyLoanPriceTv = (TextView) findViewById(R.id.reply_loan_price_tv);

        applyManagementPriceTv = (TextView) findViewById(R.id.apply_management_price_tv);
        replyManagementPriceTv = (TextView) findViewById(R.id.reply_management_price_tv);

        applyOtherPriceTv = (TextView) findViewById(R.id.apply_other_price_tv);
        replyOtherPriceTv = (TextView) findViewById(R.id.reply_other_price_tv);

        applyBankTv = (TextView) findViewById(R.id.apply_bank_tv);
        replyBankTv = (TextView) findViewById(R.id.reply_bank_tv);

        applyReplyDateTv = (TextView) findViewById(R.id.apply_repay_date_tv);
        ReplyRepayDateTv = (TextView) findViewById(R.id.reply_repay_data_tv);

        applyTotalPriceTv = (TextView) findViewById(R.id.apply_total_price_tv);
        replyTotalPriceTv = (TextView) findViewById(R.id.reply_total_price_tv);


//        billPriceTv = (TextView) findViewById(R.id.bill_price_tv);
//        firstPriceTv = (TextView) findViewById(R.id.finance_plan_first_price_tv);
//        carLoanPriceTv = (TextView) findViewById(R.id.finance_plan_car_loan_price_tv);
//        managementPriceTv = (TextView) findViewById(R.id.finance_plan_management_price_tv);
//        otherPriceTv = (TextView) findViewById(R.id.finance_plan_other_price_tv);
//        loanPriceTv = (TextView) findViewById(R.id.finance_plan_loan_price_tv);
//        loanBankTv = (TextView) findViewById(R.id.finance_plan_loan_bank_tv);
        //periodsTv = (TextView) findViewById(R.id.finance_plan_periods_tv);
        percentTv = (TextView) findViewById(R.id.finance_plan_percent_tv);
        confirmBtn = (Button) findViewById(R.id.finance_plan_confirm_btn);


//        applyBillPriceTv.setText(b1 + "");
//        replyBillPriceTv.setText(b2 + "");
        compare(b1, b2, applyBillPriceTv, replyBillPriceTv);
        compare(f1, f2, applyFirstPriceTv, replyFirstPriceTv);
        compare(L1, L2, applyLoanPriceTv, replyLoanPriceTv);
        compare(M1, M2, applyManagementPriceTv, replyManagementPriceTv);
        compare(O1, O2, applyOtherPriceTv, replyOtherPriceTv);
        compare(T1, T2, applyTotalPriceTv, replyTotalPriceTv);

        compare(bank1, bank2, applyBankTv, replyBankTv);
        compare(date1, date2, applyReplyDateTv, ReplyRepayDateTv);


//        int a = Integer.valueOf(applyBillPriceTv.getText() + "");
//        int b = Integer.valueOf(replyBillPriceTv.getText() + "");
//
//        if (a != b) {
//            applyBillPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            applyBillPriceTv.setTextColor(Color.parseColor("#999999"));
//            replyBillPriceTv.setTextColor(Color.parseColor("#CBA053"));
//        } else {
//            applyBillPriceTv.setTextColor(Color.parseColor("#999999"));
//            replyBillPriceTv.setTextColor(Color.parseColor("#222a36"));
//        }


        //int b  = Integer.valueOf(applyBillPriceTv.setText(2000+""));


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmFinancePlanReq req = new ConfirmFinancePlanReq();
                req.app_id = getIntent().getStringExtra("app_id");
                OrderApi.confirmFinancePlan(FinancePlanActivity.this, req, new OnCodeAndMsgCallBack() {
                    @Override
                    public void callBack(int code, String msg) {
                        if (code == 0) {
                            Toast.makeText(myApp, "确认成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(myApp, "确认失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


//        OrderApi.getFinancePlanDetail(this, getIntent().getStringExtra("app_id"), new OnItemDataCallBack<GetFinancePlanDetailResp>() {
//            @Override
//            public void onItemDataCallBack(GetFinancePlanDetailResp resp) {
//                replyBillPriceTv.setText(resp.vehicle_price);
//                replyFirstPriceTv.setText(resp.vehicle_down_payment);
//                replyLoanPriceTv.setText(resp.vehicle_loan_amt);
//                replyManagementPriceTv.setText(resp.management_fee);
//                replyOtherPriceTv.setText(resp.other_fee);
//                replyLoanPriceTv.setText(resp.loan_amt);
//                replyBankTv.setText(resp.loan_bank);
//                //periodsTv.setText(resp.nper);
//                percentTv.setText(resp.vehicle_down_payment_percent * 100 + "%");
//            }
//        });
    }

    private void compare(String str1, String str2, TextView tv1, TextView tv2) {
        tv1.setText(str1);
        tv2.setText(str2);
        if (tv1.getText().toString().compareTo(tv2.getText().toString()) == 0) {
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#222a36"));
        } else {
            tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#CBA053"));
        }
    }

    private void compare(int apply, int reply, TextView tv1, TextView tv2) {
        tv1.setText(apply + "");
        tv2.setText(reply + "");
        int a = Integer.valueOf(tv1.getText() + "");
        int b = Integer.valueOf(tv2.getText() + "");
        if (tv1.getText().toString().compareTo(tv2.getText().toString()) == 0) {//相等
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#222a36"));
        } else {
            tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv1.setTextColor(Color.parseColor("#999999"));
            tv2.setTextColor(Color.parseColor("#CBA053"));
        }

//        if (a != b) {
//            tv1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            tv1.setTextColor(Color.parseColor("#999999"));
//            tv2.setTextColor(Color.parseColor("#CBA053"));
//        } else {
//            tv1.setTextColor(Color.parseColor("#999999"));
//            tv2.setTextColor(Color.parseColor("#222a36"));
//        }

    }
}


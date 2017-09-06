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

public class FinancePlanActivity extends BaseActivity {


    private TextView applyBillPriceTv;
    private TextView replyBillPriceTv;

    private TextView applyFirstPriceTv;
    private TextView replyFirstPriceTv;

    private TextView applyLoanPriceTv;
    private TextView replyLoanPriceTv;

    private TextView applyManagementPriceTv;
    private TextView replyManagementPriceTv;

    private TextView applyOtherPriceTv;
    private TextView replyOtherPriceTv;

    private TextView applyTotalPriceTv;
    private TextView replyTotalPriceTv;

    private TextView applyBankTv;
    private TextView replyBankTv;

    private TextView applyReplyDateTv;
    private TextView ReplyRepayDateTv;

    private TextView percentTv;

    private Button confirmBtn;

    private TextView applyMonthPriceTv;
    private TextView replyMonthPriceTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_plan);
        initTitleBar(this, "我的金融方案");

        initView();
        initData();

    }

    private void initData() {
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
        //getIntent().getStringExtra("app_id")  1100002
        String app_id = "11000002";
        OrderApi.getFinancePlanDetail(this, getIntent().getStringExtra("app_id"), new OnItemDataCallBack<GetFinancePlanDetailResp>() {
            @Override
            public void onItemDataCallBack(GetFinancePlanDetailResp resp) {
                applyBillPriceTv.setText(resp.getApp().getVehicle_price());
                replyBillPriceTv.setText(resp.getUw().getVehicle_price());
                //compare(resp.getApp().getVehicle_price(),resp.getUw().getVehicle_price(),applyBillPriceTv,replyBillPriceTv);

                applyFirstPriceTv.setText(resp.getApp().getVehicle_down_payment());
                replyFirstPriceTv.setText(resp.getUw().getVehicle_down_payment());

                applyLoanPriceTv.setText(resp.getApp().getVehicle_loan_amt());
                replyLoanPriceTv.setText(resp.getUw().getVehicle_down_payment());

                applyManagementPriceTv.setText(resp.getApp().getManagement_fee());
                replyManagementPriceTv.setText(resp.getUw().getManagement_fee());

                applyOtherPriceTv.setText(resp.getApp().getOther_fee());
                replyOtherPriceTv.setText(resp.getUw().getOther_fee());

                applyTotalPriceTv.setText(resp.getApp().getLoan_amt());
                replyTotalPriceTv.setText(resp.getUw().getLoan_amt());

                applyBankTv.setText(resp.getApp().getLoan_bank());
                replyBankTv.setText(resp.getUw().getLoan_bank());

                applyReplyDateTv.setText(resp.getApp().getNper() + "期");
                ReplyRepayDateTv.setText(resp.getUw().getNper() + "期");

                applyMonthPriceTv.setText(resp.getApp().getMonthly_payment());
                replyMonthPriceTv.setText(resp.getUw().getMonthly_payment());

                //percentTv.setText(resp.getUw().getVehicle_down_payment_percent() * 100 + "%");
                compare(applyMonthPriceTv, replyMonthPriceTv);
                compare(applyBillPriceTv, replyBillPriceTv);
                compare(applyFirstPriceTv, replyFirstPriceTv);
                compare(applyLoanPriceTv, replyLoanPriceTv);
                compare(applyManagementPriceTv, replyManagementPriceTv);
                compare(applyOtherPriceTv, replyOtherPriceTv);
                compare(applyTotalPriceTv, replyTotalPriceTv);
                compare(applyBankTv, replyBankTv);
                compare(applyReplyDateTv, ReplyRepayDateTv);
            }
        });

    }

    private void initView() {
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

        applyMonthPriceTv = (TextView) findViewById(R.id.apply_month_price_tv);
        replyMonthPriceTv = (TextView) findViewById(R.id.reply_month_price_tv);

        confirmBtn = (Button) findViewById(R.id.finance_plan_confirm_btn);
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


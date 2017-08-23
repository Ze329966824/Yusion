package com.yusion.shanghai.yusion.ui.order;

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

    private TextView billPriceTv;
    private TextView applyBillPriceTv;

    private TextView firstPriceTv;
    private TextView applyFirstPriceTv;

    private TextView carLoanPriceTv;
    private TextView managementPriceTv;
    private TextView otherPriceTv;
    private TextView loanPriceTv;
    private TextView loanBankTv;
    private TextView periodsTv;
    private TextView percentTv;
    private Button confirmBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_plan);
        initTitleBar(this, "我的金融方案");

        billPriceTv = (TextView) findViewById(R.id.finance_plan_bill_price_tv);
        firstPriceTv = (TextView) findViewById(R.id.finance_plan_first_price_tv);
        carLoanPriceTv = (TextView) findViewById(R.id.finance_plan_car_loan_price_tv);
        managementPriceTv = (TextView) findViewById(R.id.finance_plan_management_price_tv);
        otherPriceTv = (TextView) findViewById(R.id.finance_plan_other_price_tv);
        loanPriceTv = (TextView) findViewById(R.id.finance_plan_loan_price_tv);
        loanBankTv = (TextView) findViewById(R.id.finance_plan_loan_bank_tv);
        periodsTv = (TextView) findViewById(R.id.finance_plan_periods_tv);
        percentTv = (TextView) findViewById(R.id.finance_plan_percent_tv);
        confirmBtn = (Button) findViewById(R.id.finance_plan_confirm_btn);
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


        OrderApi.getFinancePlanDetail(this, getIntent().getStringExtra("app_id"), new OnItemDataCallBack<GetFinancePlanDetailResp>() {
            @Override
            public void onItemDataCallBack(GetFinancePlanDetailResp resp) {
                billPriceTv.setText(resp.vehicle_price);
                firstPriceTv.setText(resp.vehicle_down_payment);
                carLoanPriceTv.setText(resp.vehicle_loan_amt);
                managementPriceTv.setText(resp.management_fee);
                otherPriceTv.setText(resp.other_fee);
                loanPriceTv.setText(resp.loan_amt);
                loanBankTv.setText(resp.loan_bank);
                periodsTv.setText(resp.nper);
                percentTv.setText(resp.vehicle_down_payment_percent * 100 + "%");
            }
        });
    }
}


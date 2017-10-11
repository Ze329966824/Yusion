package com.yusion.shanghai.yusion.bean.order;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/7/28.
 */

public class GetFinancePlanDetailResp {
    /**
     * plan_id : 1
     * uw : {"vehicle_price":"232342.00","loan_amt":"23423.00","vehicle_loan_amt":"23423.00","vehicle_down_payment":"234234.00","vehicle_down_payment_percent":0.34,"monthly_payment":"4444.00","nper":12,"management_fee":"23422.00","other_fee":"444.00","loan_bank":"测试"}
     * app : {"vehicle_price":"80000.00","loan_amt":"13300.00","vehicle_loan_amt":"10000.00","vehicle_down_payment":"70000.00","vehicle_down_payment_percent":0.875,"monthly_payment":"557.00","nper":24,"management_fee":"300.00","other_fee":"3000.00","loan_bank":"测试"}
     */

    public int plan_id;
    public UwBean uw;
    public AppBean app;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class UwBean {
        /**
         * vehicle_price : 232342.00
         * loan_amt : 23423.00
         * vehicle_loan_amt : 23423.00
         * vehicle_down_payment : 234234.00
         * vehicle_down_payment_percent : 0.34
         * monthly_payment : 4444.00
         * nper : 12
         * management_fee : 23422.00
         * other_fee : 444.00
         * loan_bank : 测试
         */

        public String vehicle_price;//开票价
        public String loan_amt;//车辆总贷款额
        public String vehicle_loan_amt;//贷款额
        public String vehicle_down_payment;//车辆首付款
        public double vehicle_down_payment_percent; //首付比例
        public String monthly_payment;
        public int nper;//还款期限
        public String management_fee;//管理费
        public String other_fee;//其他费用
        public String loan_bank; //贷款银行
        public String product_type;//产品类型

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class AppBean {
        /**
         * vehicle_price : 80000.00
         * loan_amt : 13300.00
         * vehicle_loan_amt : 10000.00
         * vehicle_down_payment : 70000.00
         * vehicle_down_payment_percent : 0.875
         * monthly_payment : 557.00
         * nper : 24
         * management_fee : 300.00
         * other_fee : 3000.00
         * loan_bank : 测试
         */

        public String vehicle_price;
        public String loan_amt;
        public String vehicle_loan_amt;
        public String vehicle_down_payment;
        public double vehicle_down_payment_percent;
        public String monthly_payment;
        public int nper;
        public String management_fee;
        public String other_fee;
        public String loan_bank;
        public String product_type;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }
}

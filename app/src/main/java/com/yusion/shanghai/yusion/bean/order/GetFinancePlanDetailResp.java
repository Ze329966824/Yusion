package com.yusion.shanghai.yusion.bean.order;

/**
 * Created by ice on 2017/7/28.
 */

public class GetFinancePlanDetailResp {

    /**
     * vehicle_price : 88888.00
     * loan_amt : 8888.00
     * vehicle_loan_amt : 888.00
     * vehicle_down_payment : 88.00
     * vehicle_down_payment_percent : 8
     * monthly_payment : null
     * nper : null
     * management_fee : 222.00
     * other_fee : 333.00
     * plan_id : 10000300
     * loan_bank : 台州工行
     */

    public String vehicle_price;
    public String loan_amt;
    public String vehicle_loan_amt;
    public String vehicle_down_payment;
    public float vehicle_down_payment_percent;
    public Object monthly_payment;
    public String nper;
    public String management_fee;
    public String other_fee;
    public int plan_id;
    public String loan_bank;
}

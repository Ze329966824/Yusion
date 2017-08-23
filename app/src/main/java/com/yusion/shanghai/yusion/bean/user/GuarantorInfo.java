package com.yusion.shanghai.yusion.bean.user;

import com.google.gson.Gson;

/**
 * Created by ice on 2017/8/23.
 */

public class GuarantorInfo {

    /**
     * major_income_type : null
     * major_income : 0
     * major_busi_type : null
     * major_company_name : null
     * major_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
     * major_work_position : null
     * major_work_phone_num : null
     * major_remark : null
     * extra_income_type : null
     * extra_income : 0
     * extra_busi_type : null
     * extra_company_name : null
     * extra_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
     * extra_work_position : null
     * extra_work_phone_num : null
     * extra_remark : null
     * drv_lic_relationship : null
     * clt_id : 0b9d71d487e611e78bbf02f1f38b2f4a
     * clt_nm : 担保人
     * id_no : 1234562278
     * gender : null
     * birthday : null
     * age : null
     * edu : null
     * marriage : 未婚
     * mobile : 13844545767
     * reg_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
     * reg_addr_details : null
     * current_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
     * house_owner_name : null
     * house_type : null
     * house_area : 0
     * house_owner_relation : null
     * urg_contact1 : null
     * urg_mobile1 : null
     * urg_relation1 : null
     * urg_contact2 : null
     * urg_mobile2 : null
     * urg_relation2 : null
     * commited : 0
     * status : N
     * created_time : 2017-08-23 17:32:56
     * update_time : 2017-08-23 17:32:56
     * spouse : {"major_income_type":null,"major_income":0,"major_busi_type":null,"major_company_name":null,"major_company_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null},"major_work_position":null,"major_work_phone_num":null,"major_remark":null,"extra_income_type":null,"extra_income":0,"extra_busi_type":null,"extra_company_name":null,"extra_company_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null},"extra_work_position":null,"extra_work_phone_num":null,"extra_remark":null,"drv_lic_relationship":null,"clt_id":"0ba2637487e611e78bbf02f1f38b2f4a","clt_nm":"","id_no":"","gender":null,"birthday":null,"age":null,"edu":null,"marriage":null,"mobile":"","reg_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null},"reg_addr_details":null,"current_addr":{"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null},"house_owner_name":null,"house_type":null,"house_area":0,"house_owner_relation":null,"urg_contact1":null,"urg_mobile1":null,"urg_relation1":null,"urg_contact2":null,"urg_mobile2":null,"urg_relation2":null,"commited":0,"status":"N","created_time":null,"update_time":null}
     */

    public String social_ship;
    public String major_income_type;
    public String major_income;
    public String major_busi_type;
    public String major_company_name;
    public MajorCompanyAddrBean major_company_addr;
    public String major_work_position;
    public String major_work_phone_num;
    public String major_remark;
    public String extra_income_type;
    public String extra_income;
    public String extra_busi_type;
    public String extra_company_name;
    public ExtraCompanyAddrBean extra_company_addr;
    public String extra_work_position;
    public String extra_work_phone_num;
    public String extra_remark;
    public String drv_lic_relationship;
    public String clt_id;
    public String clt_nm;
    public String id_no;
    public String gender;
    public String birthday;
    public String age;
    public String edu;
    public String marriage;
    public String mobile;
    public RegAddrBean reg_addr;
    public String reg_addr_details;
    public CurrentAddrBean current_addr;
    public String house_owner_name;
    public String house_type;
    public String house_area;
    public String house_owner_relation;
    public String urg_contact1;
    public String urg_mobile1;
    public String urg_relation1;
    public String urg_contact2;
    public String urg_mobile2;
    public String urg_relation2;
    public String commited;
    public String status;
    public String created_time;
    public String update_time;
    public SpouseBean spouse;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class MajorCompanyAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : null
         * update_time : null
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class ExtraCompanyAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : null
         * update_time : null
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class RegAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : null
         * update_time : null
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class CurrentAddrBean {
        /**
         * province :
         * city :
         * district : null
         * zip_code : null
         * address1 : null
         * address2 : null
         * longitude : null
         * latitude : null
         * len_of_residence : null
         * created_time : null
         * update_time : null
         */

        public String province;
        public String city;
        public String district;
        public String zip_code;
        public String address1;
        public String address2;
        public String longitude;
        public String latitude;
        public String len_of_residence;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static class SpouseBean {
        /**
         * major_income_type : null
         * major_income : 0
         * major_busi_type : null
         * major_company_name : null
         * major_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
         * major_work_position : null
         * major_work_phone_num : null
         * major_remark : null
         * extra_income_type : null
         * extra_income : 0
         * extra_busi_type : null
         * extra_company_name : null
         * extra_company_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
         * extra_work_position : null
         * extra_work_phone_num : null
         * extra_remark : null
         * drv_lic_relationship : null
         * clt_id : 0ba2637487e611e78bbf02f1f38b2f4a
         * clt_nm :
         * id_no :
         * gender : null
         * birthday : null
         * age : null
         * edu : null
         * marriage : null
         * mobile :
         * reg_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
         * reg_addr_details : null
         * current_addr : {"province":"","city":"","district":null,"zip_code":null,"address1":null,"address2":null,"longitude":null,"latitude":null,"len_of_residence":null,"created_time":null,"update_time":null}
         * house_owner_name : null
         * house_type : null
         * house_area : 0
         * house_owner_relation : null
         * urg_contact1 : null
         * urg_mobile1 : null
         * urg_relation1 : null
         * urg_contact2 : null
         * urg_mobile2 : null
         * urg_relation2 : null
         * commited : 0
         * status : N
         * created_time : null
         * update_time : null
         */

        public String major_income_type;
        public String major_income;
        public String major_busi_type;
        public String major_company_name;
        public MajorCompanyAddrBeanX major_company_addr;
        public String major_work_position;
        public String major_work_phone_num;
        public String major_remark;
        public String extra_income_type;
        public String extra_income;
        public String extra_busi_type;
        public String extra_company_name;
        public ExtraCompanyAddrBeanX extra_company_addr;
        public String extra_work_position;
        public String extra_work_phone_num;
        public String extra_remark;
        public String drv_lic_relationship;
        public String clt_id;
        public String clt_nm;
        public String id_no;
        public String gender;
        public String birthday;
        public String age;
        public String edu;
        public String marriage;
        public String mobile;
        public RegAddrBeanX reg_addr;
        public String reg_addr_details;
        public CurrentAddrBeanX current_addr;
        public String house_owner_name;
        public String house_type;
        public String house_area;
        public String house_owner_relation;
        public String urg_contact1;
        public String urg_mobile1;
        public String urg_relation1;
        public String urg_contact2;
        public String urg_mobile2;
        public String urg_relation2;
        public String commited;
        public String status;
        public String created_time;
        public String update_time;

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        public static class MajorCompanyAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : null
             * update_time : null
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }

        public static class ExtraCompanyAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : null
             * update_time : null
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }

        public static class RegAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : null
             * update_time : null
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }

        public static class CurrentAddrBeanX {
            /**
             * province :
             * city :
             * district : null
             * zip_code : null
             * address1 : null
             * address2 : null
             * longitude : null
             * latitude : null
             * len_of_residence : null
             * created_time : null
             * update_time : null
             */

            public String province;
            public String city;
            public String district;
            public String zip_code;
            public String address1;
            public String address2;
            public String longitude;
            public String latitude;
            public String len_of_residence;
            public String created_time;
            public String update_time;

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }
        }
    }
}

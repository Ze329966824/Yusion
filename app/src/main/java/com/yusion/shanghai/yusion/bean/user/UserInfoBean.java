package com.yusion.shanghai.yusion.bean.user;

/**
 * 类描述：
 * 伟大的创建人：ice
 * 不可相信的创建时间：17/4/19 下午7:07
 */

public class UserInfoBean {

    /**
     * marriage : 已婚
     * spouse : {"marriage":null,"urg_mobile1":null,"clt_nm":"","house_type":null,"work_phone_num":null,"urg_mobile2":null,"house_area":0,"mobile":"","company_addr":{"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null},"clt_id":"ffaab2706dc711e7b9c220c9d087acb5","id_no":"","urg_contact2":null,"edu":null,"urg_contact1":null,"work_position":null,"house_owner_name":null,"home_addr":{"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null},"current_addr":{"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null},"gender":null,"company_name":null,"urg_relation1":null,"monthly_income":0,"reg_addr":{"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null},"urg_relation2":null}
     * urg_mobile1 : null
     * clt_nm : lllfff
     * house_type : null
     * work_phone_num : null
     * urg_mobile2 : null
     * house_area : 0
     * mobile : 18662513062
     * company_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
     * clt_id : ff695e886dc711e7a04920c9d087acb5
     * id_no : 411722197508214014
     * urg_contact2 : null
     * edu : null
     * urg_contact1 : null
     * work_position : null
     * house_owner_name : null
     * house_owner_relation : null
     * home_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
     * current_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
     * gender : null
     * company_name : null
     * urg_relation1 : null
     * monthly_income : 0
     * reg_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
     * urg_relation2 : null
     */

    public String marriage;
    public SpouseBean spouse;
    public String urg_mobile1;
    public String clt_nm;
    public String house_type;
    public String work_phone_num;
    public String urg_mobile2;
    public int house_area;
    public String mobile;
    public CompanyAddrBeanX company_addr;
    public String clt_id;
    public String id_no;
    public String urg_contact2;
    public String edu;
    public String urg_contact1;
    public String work_position;
    public String house_owner_name;
    public String house_owner_relation;
    public CurrentAddrBeanX current_addr;
    public String gender;
    public String company_name;
    public String urg_relation1;
    public int monthly_income;
    public RegAddrBeanX reg_addr;
    public String urg_relation2;
    public String reg_addr_details;

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "marriage='" + marriage + '\'' +
                ", spouse=" + spouse +
                ", urg_mobile1='" + urg_mobile1 + '\'' +
                ", clt_nm='" + clt_nm + '\'' +
                ", house_type='" + house_type + '\'' +
                ", work_phone_num='" + work_phone_num + '\'' +
                ", urg_mobile2='" + urg_mobile2 + '\'' +
                ", house_area=" + house_area +
                ", mobile='" + mobile + '\'' +
                ", company_addr=" + company_addr +
                ", clt_id='" + clt_id + '\'' +
                ", id_no='" + id_no + '\'' +
                ", urg_contact2='" + urg_contact2 + '\'' +
                ", reg_addr_details='" + reg_addr_details + '\'' +
                ", edu='" + edu + '\'' +
                ", urg_contact1='" + urg_contact1 + '\'' +
                ", work_position='" + work_position + '\'' +
                ", house_owner_name='" + house_owner_name + '\'' +
                ", house_owner_relation='" + house_owner_relation + '\'' +
                ", current_addr=" + current_addr +
                ", gender='" + gender + '\'' +
                ", company_name='" + company_name + '\'' +
                ", urg_relation1='" + urg_relation1 + '\'' +
                ", monthly_income=" + monthly_income +
                ", reg_addr=" + reg_addr +
                ", urg_relation2='" + urg_relation2 + '\'' +
                '}';
    }

    public static class SpouseBean {
        /**
         * marriage : null
         * urg_mobile1 : null
         * clt_nm :
         * house_type : null
         * work_phone_num : null
         * urg_mobile2 : null
         * house_area : 0
         * mobile :
         * company_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
         * clt_id : ffaab2706dc711e7b9c220c9d087acb5
         * id_no :
         * urg_contact2 : null
         * edu : null
         * urg_contact1 : null
         * work_position : null
         * house_owner_name : null
         * home_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
         * current_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
         * gender : null
         * company_name : null
         * urg_relation1 : null
         * monthly_income : 0
         * reg_addr : {"province":"","latitude":null,"address1":null,"longitude":null,"county":null,"zip_code":null,"len_of_residence":null,"district":null,"city":"","address2":null}
         * urg_relation2 : null
         */

        public String marriage;
        public String urg_mobile1;
        public String clt_nm;
        public String house_type;
        public String work_phone_num;
        public String urg_mobile2;
        public int house_area;
        public String mobile;
        public CompanyAddrBeanX company_addr;
        public String clt_id;
        public String id_no;
        public String urg_contact2;
        public String edu;
        public String urg_contact1;
        public String work_position;
        public String house_owner_name;
        public CurrentAddrBeanX current_addr;
        public String gender;
        public String company_name;
        public String urg_relation1;
        public int monthly_income;
        public RegAddrBeanX reg_addr;
        public String urg_relation2;
        public String reg_addr_details;

        @Override
        public String toString() {
            return "SpouseBean{" +
                    "marriage='" + marriage + '\'' +
                    ", urg_mobile1='" + urg_mobile1 + '\'' +
                    ", clt_nm='" + clt_nm + '\'' +
                    ", house_type='" + house_type + '\'' +
                    ", work_phone_num='" + work_phone_num + '\'' +
                    ", urg_mobile2='" + urg_mobile2 + '\'' +
                    ", house_area=" + house_area +
                    ", mobile='" + mobile + '\'' +
                    ", company_addr=" + company_addr +
                    ", reg_addr_details=" + reg_addr_details +
                    ", clt_id='" + clt_id + '\'' +
                    ", id_no='" + id_no + '\'' +
                    ", urg_contact2='" + urg_contact2 + '\'' +
                    ", edu='" + edu + '\'' +
                    ", urg_contact1='" + urg_contact1 + '\'' +
                    ", work_position='" + work_position + '\'' +
                    ", house_owner_name='" + house_owner_name + '\'' +
                    ", current_addr=" + current_addr +
                    ", gender='" + gender + '\'' +
                    ", company_name='" + company_name + '\'' +
                    ", urg_relation1='" + urg_relation1 + '\'' +
                    ", monthly_income=" + monthly_income +
                    ", reg_addr=" + reg_addr +
                    ", urg_relation2='" + urg_relation2 + '\'' +
                    '}';
        }
    }

    public static class CompanyAddrBeanX {
        /**
         * province :
         * latitude : null
         * address1 : null
         * longitude : null
         * county : null
         * zip_code : null
         * len_of_residence : null
         * district : null
         * city :
         * address2 : null
         */

        public String province;
        public String latitude;
        public String address1;
        public String longitude;
        public String county;
        public String zip_code;
        public String len_of_residence;
        public String district;
        public String city;
        public String address2;

        @Override
        public String toString() {
            return "CompanyAddrBeanX{" +
                    "province='" + province + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", address1='" + address1 + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", county='" + county + '\'' +
                    ", zip_code='" + zip_code + '\'' +
                    ", len_of_residence='" + len_of_residence + '\'' +
                    ", district='" + district + '\'' +
                    ", city='" + city + '\'' +
                    ", address2='" + address2 + '\'' +
                    '}';
        }
    }

    public static class CurrentAddrBeanX {
        /**
         * province :
         * latitude : null
         * address1 : null
         * longitude : null
         * county : null
         * zip_code : null
         * len_of_residence : null
         * district : null
         * city :
         * address2 : null
         */

        public String province;
        public String latitude;
        public String address1;
        public String longitude;
        public String county;
        public String zip_code;
        public String len_of_residence;
        public String district;
        public String city;
        public String address2;

        @Override
        public String toString() {
            return "CurrentAddrBeanX{" +
                    "province='" + province + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", address1='" + address1 + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", county='" + county + '\'' +
                    ", zip_code='" + zip_code + '\'' +
                    ", len_of_residence='" + len_of_residence + '\'' +
                    ", district='" + district + '\'' +
                    ", city='" + city + '\'' +
                    ", address2='" + address2 + '\'' +
                    '}';
        }
    }

    public static class RegAddrBeanX {
        /**
         * province :
         * latitude : null
         * address1 : null
         * longitude : null
         * county : null
         * zip_code : null
         * len_of_residence : null
         * district : null
         * city :
         * address2 : null
         */

        public String province;
        public String latitude;
        public String address1;
        public String longitude;
        public String county;
        public String zip_code;
        public String len_of_residence;
        public String district;
        public String city;
        public String address2;

        @Override
        public String toString() {
            return "RegAddrBeanX{" +
                    "province='" + province + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", address1='" + address1 + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", county='" + county + '\'' +
                    ", zip_code='" + zip_code + '\'' +
                    ", len_of_residence='" + len_of_residence + '\'' +
                    ", district='" + district + '\'' +
                    ", city='" + city + '\'' +
                    ", address2='" + address2 + '\'' +
                    '}';
        }
    }
}
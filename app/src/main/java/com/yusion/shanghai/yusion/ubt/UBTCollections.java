package com.yusion.shanghai.yusion.ubt;

import android.util.Pair;

import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.ui.apply.ApplyActivity;
import com.yusion.shanghai.yusion.ui.apply.AutonymCertifyFragment;
import com.yusion.shanghai.yusion.ui.apply.PersonalInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.SpouseInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.guarantor.AddGuarantorActivity;
import com.yusion.shanghai.yusion.ui.apply.guarantor.DocmtActivity;
import com.yusion.shanghai.yusion.ui.apply.guarantor.DocumentFromLabelListActivity;
import com.yusion.shanghai.yusion.ui.apply.guarantor.GuarantorCreditInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.guarantor.GuarantorInfoFragment;
import com.yusion.shanghai.yusion.ui.apply.guarantor.GuarantorSpouseInfoFragment;
import com.yusion.shanghai.yusion.ui.entrance.AgreeMentActivity;
import com.yusion.shanghai.yusion.ui.entrance.LaunchActivity;
import com.yusion.shanghai.yusion.ui.entrance.LoginActivity;
import com.yusion.shanghai.yusion.ui.entrance.MainActivity;
import com.yusion.shanghai.yusion.ui.entrance.WebViewActivity;
import com.yusion.shanghai.yusion.ui.main.HomeFragment;
import com.yusion.shanghai.yusion.ui.main.MineFragment;
import com.yusion.shanghai.yusion.ui.main.MyOrderFragment;
import com.yusion.shanghai.yusion.ui.main.mine.SettingsActivity;
import com.yusion.shanghai.yusion.ui.order.FinancePlanActivity;
import com.yusion.shanghai.yusion.ui.order.OrderDetailActivity;
import com.yusion.shanghai.yusion.ui.update.CommitActivity;
import com.yusion.shanghai.yusion.ui.update.ImgsListFragment;
import com.yusion.shanghai.yusion.ui.update.InfoListActivity;
import com.yusion.shanghai.yusion.ui.update.InfoListFragment;
import com.yusion.shanghai.yusion.ui.update.UpdateGuarantorInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdateGuarantorSpouseInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdatePersonalInfoActivity;
import com.yusion.shanghai.yusion.ui.update.UpdateSpouseInfoActivity;
import com.yusion.shanghai.yusion.ui.upload.OnlyReadUploadListActivity;
import com.yusion.shanghai.yusion.ui.upload.PreviewActivity;
import com.yusion.shanghai.yusion.ui.upload.UploadLabelListActivity;
import com.yusion.shanghai.yusion.ui.upload.UploadListActivity;

import java.util.HashMap;

/**
 * Created by ice on 2017/9/26.
 */

public class UBTCollections {
    public static HashMap<String, Pair<String, String>> pageNameMaps = new HashMap<>();
    public static HashMap<String, String> widgetNameMaps = new HashMap<>();

    static {
        pageNameMaps.put(LaunchActivity.class.getSimpleName(), new Pair<>("launch", "启动页面"));
        pageNameMaps.put(LoginActivity.class.getSimpleName(), new Pair<>("login", "登陆页面"));
        pageNameMaps.put(WebViewActivity.class.getSimpleName(), new Pair<>("webview", "H5页面"));
        pageNameMaps.put(AgreeMentActivity.class.getSimpleName(), new Pair<>("agreement", "协议页面"));
        pageNameMaps.put(MainActivity.class.getSimpleName(), new Pair<>("home_order_mine", "首页-订单-我的页面"));

        pageNameMaps.put(HomeFragment.class.getSimpleName(), new Pair<>("home", "首页"));
        pageNameMaps.put(MyOrderFragment.class.getSimpleName(), new Pair<>("order_list", "订单列表页面"));
        pageNameMaps.put(MineFragment.class.getSimpleName(), new Pair<>("mine", "个人信息页面"));

        pageNameMaps.put(SettingsActivity.class.getSimpleName(), new Pair<>("settings", "设置页面"));

        pageNameMaps.put(FinancePlanActivity.class.getSimpleName(), new Pair<>("finance_plan", "金融方案页面"));
        pageNameMaps.put(OrderDetailActivity.class.getSimpleName(), new Pair<>("order_detail", "订单详情页面"));

        pageNameMaps.put(CommitActivity.class.getSimpleName(), new Pair<>("commit_apply_success", "提交申请成功页面"));
        pageNameMaps.put(ImgsListFragment.class.getSimpleName(), new Pair<>("info_img", "资料列表-影像件页面"));
        pageNameMaps.put(InfoListActivity.class.getSimpleName(), new Pair<>("info", "资料列表页面"));
        pageNameMaps.put(InfoListFragment.class.getSimpleName(), new Pair<>("info_info", "资料列表-资料页面"));

        pageNameMaps.put(UpdateGuarantorInfoActivity.class.getSimpleName(), new Pair<>("update_guarantor", "修改担保人资料页面"));
        pageNameMaps.put(UpdateGuarantorSpouseInfoActivity.class.getSimpleName(), new Pair<>("update_guarantor_spouse", "修改担保人配偶资料页面"));
        pageNameMaps.put(UpdatePersonalInfoActivity.class.getSimpleName(), new Pair<>("update_lender", "修改个人资料页面"));
        pageNameMaps.put(UpdateSpouseInfoActivity.class.getSimpleName(), new Pair<>("update_lender_spouse", "修改个人配偶资料页面"));

        pageNameMaps.put(ApplyActivity.class.getSimpleName(), new Pair<>("lender_apply", "主贷人申请页面"));
        pageNameMaps.put(AutonymCertifyFragment.class.getSimpleName(), new Pair<>("lender_apply_credit", "主贷人-征信信息页面"));
        pageNameMaps.put(PersonalInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_personal", "主贷人-个人信息页面"));
        pageNameMaps.put(SpouseInfoFragment.class.getSimpleName(), new Pair<>("lender_apply_spouse", "主贷人-配偶页面"));
        pageNameMaps.put(AddGuarantorActivity.class.getSimpleName(), new Pair<>("guarantor_apply", "担保人申请页面"));
        pageNameMaps.put(GuarantorCreditInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-征信信息页面"));
        pageNameMaps.put(GuarantorInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-个人信息页面"));
        pageNameMaps.put(GuarantorSpouseInfoFragment.class.getSimpleName(), new Pair<>("guarantor_apply_personal", "担保人-配偶页面"));
        pageNameMaps.put(AMapPoiListActivity.class.getSimpleName(), new Pair<>("poi_address", "高德地图POI检索界面"));

        pageNameMaps.put(DocmtActivity.class.getSimpleName(), new Pair<>("single_img_upload", "单张图片上传界面(身份证正反面,驾照)"));
        pageNameMaps.put(DocumentFromLabelListActivity.class.getSimpleName(), new Pair<>("multi_img_upload", "多张图片上传界面"));

        pageNameMaps.put(PreviewActivity.class.getSimpleName(), new Pair<>("img_preview", "图片预览界面"));
        pageNameMaps.put(OnlyReadUploadListActivity.class.getSimpleName(), new Pair<>("img_authorization_book", "授权书查看页面"));
        pageNameMaps.put(UploadLabelListActivity.class.getSimpleName(), new Pair<>("upload_label_list", "上传影像件标签列表页面"));
        pageNameMaps.put(UploadListActivity.class.getSimpleName(), new Pair<>("upload_img_list", "上传影像件列表页面"));


        //widgetNameMaps.put("personal_info_income_from_tv", "选择主要收入来源");

    }

    static {
        widgetNameMaps.put("autonym_certify_id_back_tv", "上传身份证人像面");
        widgetNameMaps.put("autonym_certify_id_front_tv", "上传身份证国徽面");
        widgetNameMaps.put("autonym_certify_name_tv", "输入姓名");
        widgetNameMaps.put("autonym_certify_id_number_tv", "输入身份证号");
        widgetNameMaps.put("autonym_certify_driving_license_tv", "上传驾驶证");
        widgetNameMaps.put("autonym_certify_driving_license_rel_tv", "选择驾驶证与本人关系");


        widgetNameMaps.put("personal_info_gender_tv", "选择性别");
        widgetNameMaps.put("personal_info_reg_tv", "选择户籍地");
        widgetNameMaps.put("personal_info_education_tv", "选择学历");
        widgetNameMaps.put("personal_info_current_address_tv", "选择现住地址");
        widgetNameMaps.put("personal_info_current_address1_tv", "选择详细地址");
        widgetNameMaps.put("personal_info_current_address2_tv", "输入门牌号");
        widgetNameMaps.put("personal_info_live_with_parent_tv", "选择是否与父母同住");

        widgetNameMaps.put("personal_info_income_from_tv", "选择主要收入来源");

        widgetNameMaps.put("personal_info_from_income_year_edt", "输入年收入(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_name_edt", "输入单位名称(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_address_tv", "选择单位地址(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_address1_tv", "选择详细地址(主要工资)");
        widgetNameMaps.put("personal_info_from_income_company_address2_tv", "输入门牌号(主要工资)");
        widgetNameMaps.put("personal_info_from_income_work_position_tv", "选择职务(主要工资)");
        widgetNameMaps.put("personal_info_from_income_work_phone_num_edt", "输入单位座机(主要工资)");

        widgetNameMaps.put("personal_info_from_self_year_edt", "输入年收入(主要自营)");
        widgetNameMaps.put("personal_info_from_self_type_tv", "选择业务类型(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_name_edt", "输入店铺名称(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_address_tv", "选择经营项目地址(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_address1_tv", "选择详细地址(主要自营)");
        widgetNameMaps.put("personal_info_from_self_company_address2_tv", "输入门牌号(主要自营)");


        widgetNameMaps.put("personal_info_extra_income_from_tv", "选择额外收入来源");
        widgetNameMaps.put("personal_info_extra_from_income_year_edt", "输入年收入(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_name_edt", "输入单位名称(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_address_tv", "选择单位名称(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_address1_tv", "选择详细地址(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_company_address2_tv", "输入门牌号(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_work_position_tv", "选择职务(额外工资)");
        widgetNameMaps.put("personal_info_extra_from_income_work_phone_num_edt", "输入单位座机(额外工资)");


        widgetNameMaps.put("personal_info_house_type_tv", "选择房屋性质");
        widgetNameMaps.put("personal_info_house_area_edt", "输入房屋面积");
        widgetNameMaps.put("personal_info_house_owner_name_edt", "输入房屋所有人");
        widgetNameMaps.put("personal_info_house_owner_relation_tv", "选择与申请人关系(房屋所有人)");

        widgetNameMaps.put("personal_info_urg_relation1_tv", "与申请人关系(紧急联系人1)");
        widgetNameMaps.put("personal_info_urg_mobile1_edt", "手机号码(紧急联系人1)");
        widgetNameMaps.put("personal_info_urg_contact1_edt", "联系人姓名(紧急联系人1)");
        widgetNameMaps.put("personal_info_urg_relation2_tv", "与申请人关系(紧急联系人2)");
        widgetNameMaps.put("personal_info_urg_mobile2_edt", "手机号码(紧急联系人2)");
        widgetNameMaps.put("personal_info_urg_contact2_edt", "联系人姓名(紧急联系人2)");

//个人资料
        widgetNameMaps.put("update_personal_info_clt_nm_edt", "输入姓名");
        widgetNameMaps.put("update_personal_info_id_no_edt", "身份证号");
        widgetNameMaps.put("update_personal_info_gender_tv", "性别");
        widgetNameMaps.put("update_personal_info_reg_tv", "户籍地");
        widgetNameMaps.put("update_personal_info_mobile_edt", "手机号");
        widgetNameMaps.put("update_personal_info_education_tv", "学历");
        widgetNameMaps.put("update_personal_info_current_address_tv", "现住地址");
        widgetNameMaps.put("update_personal_info_current_address1_tv", "详细地址");
        widgetNameMaps.put("update_personal_info_current_address2_tv", "门牌号");
        widgetNameMaps.put("update_personal_info_live_with_parent_tv", "是否与父母同住");
        widgetNameMaps.put("update_personal_info_income_from_tv", "主要收入来源");
        widgetNameMaps.put("update_personal_info_from_income_year_edt", "主要-工资-年收入");
        widgetNameMaps.put("update_personal_info_from_income_company_name_edt", "主要-工资-单位名称");
        widgetNameMaps.put("update_personal_info_from_income_company_address_tv", "主要-工资-单位地址");
        widgetNameMaps.put("update_personal_info_from_income_company_address1_tv", "主要-工资-详细地址");
        widgetNameMaps.put("update_personal_info_from_income_company_address2_tv", "主要-工资-门牌号");
        widgetNameMaps.put("update_personal_info_work_position_tv", "主要-工资-职务");
        widgetNameMaps.put("update_personal_info_from_income_work_phone_num_edt", "主要-工资-单位座机");
        widgetNameMaps.put("update_personal_info_from_self_year_edt", "主要-自营-年收入");
        widgetNameMaps.put("update_personal_info_from_self_type_tv", "主要-自营-业务类型");
        widgetNameMaps.put("update_personal_info_from_self_company_name_edt", "主要-自营-店铺名称");
        widgetNameMaps.put("update_personal_info_from_self_company_address_tv", "主要-自营-单位地址");
        widgetNameMaps.put("update_personal_info_from_self_company_address1_tv", "主要-自营-详细地址");
        widgetNameMaps.put("update_personal_info_from_self_company_address2_tv", "主要-自营-门牌号");
        widgetNameMaps.put("update_personal_info_from_other_year_edt", "主要-其他-年收入");
        widgetNameMaps.put("update_personal_info_from_other_remark_tv", "主要-其他-备注");
        widgetNameMaps.put("update_personal_info_extra_income_from_tv", "额外收入来源");
        widgetNameMaps.put("update_personal_info_extra_from_income_year_edt", "额外-工资-年收入");
        widgetNameMaps.put("update_personal_info_extra_from_income_company_name_edt", "额外-工资-单位名称");
        widgetNameMaps.put("update_personal_info_extra_from_income_company_address_tv", "额外-工资-公司地址");
        widgetNameMaps.put("update_personal_info_extra_from_income_company_address1_tv", "额外-工资-详细地址");
        widgetNameMaps.put("update_personal_info_extra_from_income_company_address2_tv", "额外-工资-门牌号");
        widgetNameMaps.put("update_personal_extra_info_work_position_tv", "额外-工资-职务");
        widgetNameMaps.put("update_personal_info_extra_from_income_work_phone_num_edt", "额外-工资-单位座机");
        widgetNameMaps.put("update_personal_info_house_type_tv", "房屋性质");
        widgetNameMaps.put("update_personal_info_house_area_edt", "房屋面积");
        widgetNameMaps.put("update_personal_info_house_owner_name_edt", "房屋所有人");
        widgetNameMaps.put("update_personal_info_house_owner_relation_tv", "与申请人关系");
        widgetNameMaps.put("update_personal_info_urg_relation1_tv", "紧急联系人-与申请人关系1");
        widgetNameMaps.put("update_personal_info_urg_mobile1_edt", "紧急联系人-手机号1");
        widgetNameMaps.put("update_personal_info_urg_contact1_edt", "紧急联系人-姓名1");
        widgetNameMaps.put("update_personal_info_urg_relation2_tv", "紧急联系人-与申请人关系2");
        widgetNameMaps.put("update_personal_info_urg_mobile2_edt", "紧急联系人-手机号2");
        widgetNameMaps.put("update_personal_info_urg_contact2_edt", "紧急联系人-姓名2");
        widgetNameMaps.put("submit_img", "提交个人资料");

        //配偶资料
        widgetNameMaps.put("update_spouse_info_marriage_tv", "婚否");
        widgetNameMaps.put("update_spouse_info_clt_nm_edt", "输入姓名");
        widgetNameMaps.put("update_spouse_info_id_no_edt", "身份证号");
        widgetNameMaps.put("update_spouse_info_gender_tv", "性别");
        widgetNameMaps.put("update_spouse_info_mobile_edt", "手机号");
        widgetNameMaps.put("update_spouse_info_income_from_tv", "主要收入来源");
        widgetNameMaps.put("update_spouse_info_from_income_year_edt", "主要-工资-年收入");
        widgetNameMaps.put("update_spouse_info_from_income_company_name_edt", "主要-工资-单位名称");
        widgetNameMaps.put("update_spouse_info_from_income_company_address_tv", "主要-工资-单位地址");
        widgetNameMaps.put("update_spouse_info_from_income_company_address1_tv", "主要-工资-详细地址");
        widgetNameMaps.put("update_spouse_info_from_income_company_address2_tv", "主要-工资-门牌号");
        widgetNameMaps.put("update_spouse_info_work_position_tv", "主要-工资-职务");
        widgetNameMaps.put("update_spouse_info_from_income_work_phone_num_edt", "主要-工资-单位座机");
        widgetNameMaps.put("update_spouse_info_from_self_year_edt", "主要-自营-年收入");
        widgetNameMaps.put("update_spouse_info_from_self_type_tv", "主要-自营-业务类型");
        widgetNameMaps.put("update_spouse_info_from_self_company_name_edt", "主要-自营-店铺名称");
        widgetNameMaps.put("update_spouse_info_from_self_company_address_tv", "主要-自营-单位地址");
        widgetNameMaps.put("update_spouse_info_from_self_company_address1_tv", "主要-自营-详细地址");
        widgetNameMaps.put("update_spouse_info_from_self_company_address2_tv", "主要-自营-门牌号");
        widgetNameMaps.put("update_spouse_info_from_other_year_edt", "主要-其他-年收入");
        widgetNameMaps.put("update_spouse_info_from_other_remark_tv", "主要-其他-备注");
        widgetNameMaps.put("update_spouse_info_extra_income_from_tv", "额外收入来源");
        widgetNameMaps.put("update_spouse_info_extra_from_income_year_edt", "额外-工资-年收入");
        widgetNameMaps.put("update_spouse_info_extra_from_income_company_name_edt", "额外-工资-单位名称");
        widgetNameMaps.put("update_spouse_info_extra_from_income_company_address_tv", "额外-工资-公司地址");
        widgetNameMaps.put("update_spouse_info_extra_from_income_company_address1_tv", "额外-工资-详细地址");
        widgetNameMaps.put("update_spouse_info_extra_from_income_company_address2_tv", "额外-工资-门牌号");
        widgetNameMaps.put("update_spouse_info_extra_from_income_work_phone_num_edt", "额外-工资-单位座机");
        widgetNameMaps.put("submit_img", "提交配偶资料");

        //担保人资料
        widgetNameMaps.put("update_guarantor_info_clt_nm_edt", "输入姓名");
        widgetNameMaps.put("update_guarantor_info_id_no_edt", "身份证号");
        widgetNameMaps.put("update_guarantor_info_gender_tv", "性别");
        widgetNameMaps.put("update_guarantor_info_reg_tv", "户籍地");
        widgetNameMaps.put("update_guarantor_info_mobile_edt", "手机号");
        widgetNameMaps.put("update_guarantor_info_education_tv", "学历");
        widgetNameMaps.put("update_guarantor_info_current_address_tv", "现住地址");
        widgetNameMaps.put("update_guarantor_info_current_address1_tv", "详细地址");
        widgetNameMaps.put("update_guarantor_info_current_address2_tv", "门牌号");
        widgetNameMaps.put("update_guarantor_info_income_from_tv", "主要收入来源");
        widgetNameMaps.put("update_guarantor_info_from_income_year_edt", "主要-工资-年收入");
        widgetNameMaps.put("update_guarantor_info_from_income_company_name_edt", "主要-工资-单位名称");
        widgetNameMaps.put("update_guarantor_info_from_income_company_address_tv", "主要-工资-单位地址");
        widgetNameMaps.put("update_guarantor_info_from_income_company_address1_tv", "主要-工资-详细地址");
        widgetNameMaps.put("update_guarantor_info_from_income_company_address2_tv", "主要-工资-门牌号");
        widgetNameMaps.put("update_guarantor_info_work_position_tv", "主要-工资-职务");
        widgetNameMaps.put("update_guarantor_info_from_income_work_phone_num_edt", "主要-工资-单位座机");
        widgetNameMaps.put("update_guarantor_info_from_self_year_edt", "主要-自营-年收入");
        widgetNameMaps.put("update_guarantor_info_from_self_type_tv", "主要-自营-业务类型");
        widgetNameMaps.put("update_guarantor_info_from_self_company_name_edt", "主要-自营-店铺名称");
        widgetNameMaps.put("update_guarantor_info_from_self_company_address_tv", "主要-自营-单位地址");
        widgetNameMaps.put("update_guarantor_info_from_self_company_address1_tv", "主要-自营-详细地址");
        widgetNameMaps.put("update_guarantor_info_from_self_company_address2_tv", "主要-自营-门牌号");
        widgetNameMaps.put("update_guarantor_info_from_other_year_edt", "主要-其他-年收入");
        widgetNameMaps.put("update_guarantor_info_from_other_remark_tv", "主要-其他-备注");
        widgetNameMaps.put("update_guarantor_info_extra_income_from_tv", "额外收入来源");
        widgetNameMaps.put("update_guarantor_info_extra_from_income_year_edt", "额外-工资-年收入");
        widgetNameMaps.put("update_guarantor_info_extra_from_income_company_name_edt", "额外-工资-单位名称");
        widgetNameMaps.put("update_guarantor_info_extra_from_income_company_address_tv", "额外-工资-公司地址");
        widgetNameMaps.put("update_guarantor_info_extra_from_income_company_address1_tv", "额外-工资-详细地址");
        widgetNameMaps.put("update_guarantor_info_extra_from_income_company_address2_tv", "额外-工资-门牌号");
        widgetNameMaps.put("update_guarantor_extra_info_work_position_tv", "额外-工资-职务");
        widgetNameMaps.put("update_guarantor_info_extra_from_income_work_phone_num_edt", "额外-工资-单位座机");
        widgetNameMaps.put("update_guarantor_info_house_type_tv", "房屋性质");
        widgetNameMaps.put("update_guarantor_info_house_address_tv", "房屋地址");
        widgetNameMaps.put("update_guarantor_info_house_address1_tv", "房屋详细地址");
        widgetNameMaps.put("update_guarantor_info_house_address2_tv", "门牌号");
        widgetNameMaps.put("update_guarantor_info_house_owner_name_edt", "房屋所有人");
        widgetNameMaps.put("update_guarantor_info_house_owner_relation_tv", "与申请人关系");

        widgetNameMaps.put("submit_img", "提交担保人资料");


        //担保人配偶资料
        widgetNameMaps.put("update_guarantor_spouse_info_clt_nm_edt", "输入姓名");
        widgetNameMaps.put("update_guarantor_spouse_info_id_no_edt", "身份证号");
        widgetNameMaps.put("update_guarantor_spouse_info_gender_tv", "性别");
        widgetNameMaps.put("update_guarantor_spouse_info_mobile_edt", "手机号");
        widgetNameMaps.put("update_guarantor_spouse_info_income_from_tv", "主要收入来源");
        widgetNameMaps.put("update_guarantor_spouse_info_from_income_year_edt", "主要-工资-年收入");
        widgetNameMaps.put("update_guarantor_spouse_info_from_income_company_name_edt", "主要-工资-单位名称");
        widgetNameMaps.put("update_guarantor_spouse_info_from_income_company_address_tv", "主要-工资-单位地址");
        widgetNameMaps.put("update_guarantor_spouse_info_from_income_company_address1_tv", "主要-工资-详细地址");
        widgetNameMaps.put("update_guarantor_spouse_info_from_income_company_address2_tv", "主要-工资-门牌号");
        widgetNameMaps.put("update_guarantor_spouse_info_work_position_tv", "主要-工资-职务");
        widgetNameMaps.put("update_guarantor_spouse_info_from_income_work_phone_num_edt", "主要-工资-单位座机");
        widgetNameMaps.put("update_guarantor_spouse_info_from_self_year_edt", "主要-自营-年收入");
        widgetNameMaps.put("update_guarantor_spouse_info_from_self_type_tv", "主要-自营-业务类型");
        widgetNameMaps.put("update_guarantor_spouse_info_from_self_company_name_edt", "主要-自营-店铺名称");
        widgetNameMaps.put("update_guarantor_spouse_info_from_self_company_address_tv", "主要-自营-单位地址");
        widgetNameMaps.put("update_guarantor_spouse_info_from_self_company_address1_tv", "主要-自营-详细地址");
        widgetNameMaps.put("update_guarantor_spouse_info_from_self_company_address2_tv", "主要-自营-门牌号");
        widgetNameMaps.put("update_guarantor_spouse_info_from_other_year_edt", "主要-其他-年收入");
        widgetNameMaps.put("update_guarantor_spouse_info_from_other_remark_tv", "主要-其他-备注");
        widgetNameMaps.put("update_guarantor_spouse_info_extra_income_from_tv", "额外收入来源");
        widgetNameMaps.put("update_guarantor_spouse_info_extra_from_income_year_edt", "额外-工资-年收入");
        widgetNameMaps.put("update_guarantor_spouse_info_extra_from_income_company_name_edt", "额外-工资-单位名称");
        widgetNameMaps.put("update_guarantor_spouse_info_extra_from_income_company_address_tv", "额外-工资-公司地址");
        widgetNameMaps.put("update_guarantor_spouse_info_extra_from_income_company_address1_tv", "额外-工资-详细地址");
        widgetNameMaps.put("update_guarantor_spouse_info_extra_from_income_company_address2_tv", "额外-工资-门牌号");
        widgetNameMaps.put("update_guarantor_spouse_info_extra_from_income_work_phone_num_edt", "额外-工资-单位座机");
        widgetNameMaps.put("update_guarantor_spouse_info_marriage_tv", "婚否");
        widgetNameMaps.put("submit_img", "提交担保人配偶资料");

    }

    public static String getPageNm(String key) {
        Pair<String, String> pair = pageNameMaps.get(key);
        if (pair == null) {
            return "";
        } else {
            return pair.first;
        }
    }

    public static String getPageNmCn(String key) {
        Pair<String, String> pair = pageNameMaps.get(key);
        if (pair == null) {
            return "";
        } else {
            return pair.second;
        }
    }

    public static String getWidgetNmCn(String widget) {
        return widgetNameMaps.get(widget);
    }
}

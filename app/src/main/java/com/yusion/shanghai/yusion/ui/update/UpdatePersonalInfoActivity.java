package com.yusion.shanghai.yusion.ui.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SearchViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;
import com.yusion.shanghai.yusion.bean.user.GetClientInfoReq;
import com.yusion.shanghai.yusion.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.ubt.annotate.BindView;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.utils.CheckIdCardValidUtil;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.ContactsUtil;
import com.yusion.shanghai.yusion.utils.InputMethodUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion.widget.NoEmptyEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdatePersonalInfoActivity extends UpdateInfoActivity {

//    private UpdateImgsLabelFragment mUpdateImgsLabelFragment;
//    private String[] mTabTitle = {"个人资料", "影像件"};

    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
//        add("其他");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
        add("无");
    }};
    private List<String> ifwithparentlist = new ArrayList<String>() {{
        add("是");
        add("否");
    }};
    public static int UPDATE_INCOME_FROME_INDEX;
    public static int UPDATE_LIVE_WITH_PARENT_INDEX;
    public static int UPDATE_EXTRA_INCOME_FROME_INDEX;
    public static int UPDATE_SEX_INDEX;
    public static int UPDATE_EDUCATION_INDEX;
    public static int UPDATE_HOUSE_TYPE_INDEX;
    public static int UPDATE_HOUSE_OWNER_RELATION_INDEX;
    public static int UPDATE_URG_RELATION_INDEX1;
    public static int UPDATE_URG_RELATION_INDEX2;
    public static int UPDATE_FROM_INCOME_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_EXTRA_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_SELF_TYPE_INDEX;
    public static int CURRENT_CLICKED_VIEW_FOR_ADDRESS = -1;
    public static int CURRENT_CLICKED_VIEW_FOR_CONTACT = -1;
    private LinearLayout income_from_lin;
    private LinearLayout income_extra_from_lin;
    private TextView income_from_tv;
    private TextView income_extra_from_tv;
    private LinearLayout update_personal_info_gender_lin;
    private LinearLayout update_personal_info_reg_lin;
    private LinearLayout update_personal_info_education_lin;
    private LinearLayout update_personal_info_current_address_lin;
    private LinearLayout update_personal_info_current_address1_lin;
    private LinearLayout update_personal_info_house_type_lin;
    private LinearLayout update_personal_info_house_owner_relation_lin;
    private LinearLayout update_personal_info_urg_relation1_lin;
    private LinearLayout update_personal_info_urg_relation2_lin;
    private LinearLayout update_personal_info_from_income_company_address_lin;
    private LinearLayout update_personal_info_from_income_company_address1_lin;
    private LinearLayout update_personal_info_from_income_work_position_lin;
    private LinearLayout update_personal_info_extra_from_income_company_address1_lin;
    private LinearLayout update_personal_info_extra_from_income_work_position_lin;
    private TextView update_personal_info_extra_from_income_work_position_tv;
    private LinearLayout update_personal_info_extra_from_income_company_address_lin;
    private LinearLayout update_personal_info_from_self_company_address_lin;
    private LinearLayout update_personal_info_from_self_company_address1_lin;
    private ImageView update_personal_info_urg_mobile1_img;
    private ImageView update_personal_info_urg_mobile2_img;
    private LinearLayout update_personal_info_from_self_type_lin;
    private NestedScrollView mScrollView;
    private LinearLayout update_personal_info_from_income_group_lin;
    private LinearLayout update_personal_info_from_self_group_lin;
    private LinearLayout update_personal_info_from_other_group_lin;
    private LinearLayout update_personal_info_extra_from_income_group_lin;

    @BindView(id = R.id.update_personal_info_clt_nm_edt, widgetName = "update_personal_info_clt_nm_edt")
    private NoEmptyEditText update_personal_info_clt_nm_edt;                       //姓名

    @BindView(id = R.id.update_personal_info_id_no_edt, widgetName = "update_personal_info_id_no_edt")
    private EditText update_personal_info_id_no_edt;                        //身份证号

    @BindView(id = R.id.update_personal_info_gender_tv, widgetName = "update_personal_info_gender_tv")
    private TextView update_personal_info_gender_tv;                        //性别

    @BindView(id = R.id.update_personal_info_reg_tv, widgetName = "update_personal_info_reg_tv")
    private TextView update_personal_info_reg_tv;//户籍

    @BindView(id = R.id.update_personal_info_mobile_edt, widgetName = "update_personal_info_mobile_edt")
    private EditText update_personal_info_mobile_edt;                       //手机号

    @BindView(id = R.id.update_personal_info_education_tv, widgetName = "update_personal_info_education_tv")
    private TextView update_personal_info_education_tv;                     //学历

    @BindView(id = R.id.update_personal_info_current_address_tv, widgetName = "update_personal_info_current_address_tv")
    private TextView update_personal_info_current_address_tv;               //现住地址

    @BindView(id = R.id.update_personal_info_current_address1_tv, widgetName = "update_personal_info_current_address1_tv")
    private TextView update_personal_info_current_address1_tv;              //详细地址

    @BindView(id = R.id.update_personal_info_current_address2_tv, widgetName = "update_personal_info_current_address2_tv")
    private NoEmptyEditText update_personal_info_current_address2_tv;              //门牌号

    @BindView(id = R.id.update_personal_info_income_from_tv, widgetName = "update_personal_info_income_from_tv")
    private TextView update_personal_info_income_from_tv;                   //主要收入来源

    @BindView(id = R.id.update_personal_info_from_income_year_edt, widgetName = "update_personal_info_from_income_year_edt")
    private EditText update_personal_info_from_income_year_edt;             //主要-工资-年收入

    @BindView(id = R.id.update_personal_info_from_income_company_name_edt, widgetName = "update_personal_info_from_income_company_name_edt")
    private NoEmptyEditText update_personal_info_from_income_company_name_edt;     //主要-工资-单位名称

    @BindView(id = R.id.update_personal_info_from_income_company_address_tv, widgetName = "update_personal_info_from_income_company_address_tv")
    private TextView update_personal_info_from_income_company_address_tv;   //主要-工资-单位地址

    @BindView(id = R.id.update_personal_info_from_income_company_address1_tv, widgetName = "update_personal_info_from_income_company_address1_tv")
    private TextView update_personal_info_from_income_company_address1_tv;  //主要-工资-详细地址

    @BindView(id = R.id.update_personal_info_from_income_company_address2_tv, widgetName = "update_personal_info_from_income_company_address2_tv")
    private NoEmptyEditText update_personal_info_from_income_company_address2_tv;  //主要-工资-门牌号

    @BindView(id = R.id.update_personal_info_work_position_tv, widgetName = "update_personal_info_work_position_tv")
    private TextView update_personal_info_work_position_tv;                 //主要-工资-职务

    @BindView(id = R.id.update_personal_info_from_income_work_phone_num_edt, widgetName = "update_personal_info_from_income_work_phone_num_edt")
    private NoEmptyEditText update_personal_info_from_income_work_phone_num_edt;   //主要-工资-单位座机

    @BindView(id = R.id.update_personal_info_from_self_year_edt, widgetName = "update_personal_info_from_self_year_edt")
    private EditText update_personal_info_from_self_year_edt;               //主要-自营-年收入

    @BindView(id = R.id.update_personal_info_from_self_type_tv, widgetName = "update_personal_info_from_self_type_tv")
    private TextView update_personal_info_from_self_type_tv;                //主要-自营-业务类型

    @BindView(id = R.id.update_personal_info_from_self_company_name_edt, widgetName = "update_personal_info_from_self_company_name_edt")
    private NoEmptyEditText update_personal_info_from_self_company_name_edt;       //主要-自营-店铺名称

    @BindView(id = R.id.update_personal_info_from_self_company_address_tv, widgetName = "update_personal_info_from_self_company_address_tv")
    private TextView update_personal_info_from_self_company_address_tv;     //主要-自营-单位地址

    @BindView(id = R.id.update_personal_info_from_self_company_address1_tv, widgetName = "update_personal_info_from_self_company_address1_tv")
    private TextView update_personal_info_from_self_company_address1_tv;    //主要-自营-详细地址

    @BindView(id = R.id.update_personal_info_from_self_company_address2_tv, widgetName = "update_personal_info_from_self_company_address2_tv")
    private NoEmptyEditText update_personal_info_from_self_company_address2_tv;    //主要-自营-门牌号

    @BindView(id = R.id.update_personal_info_from_other_year_edt, widgetName = "update_personal_info_from_other_year_edt")
    private EditText update_personal_info_from_other_year_edt;              //主要-其他-年收入

    @BindView(id = R.id.update_personal_info_from_other_remark_tv, widgetName = "update_personal_info_from_other_remark_tv")
    private NoEmptyEditText update_personal_info_from_other_remark_tv;             //主要-其他-备注

    @BindView(id = R.id.update_personal_info_extra_income_from_tv, widgetName = "update_personal_info_extra_income_from_tv")
    private TextView update_personal_info_extra_income_from_tv;             //额外收入来源

    @BindView(id = R.id.update_personal_info_extra_from_income_year_edt, widgetName = "update_personal_info_extra_from_income_year_edt")
    private EditText update_personal_info_extra_from_income_year_edt;            //额外-工资-年收入

    @BindView(id = R.id.update_personal_info_extra_from_income_company_name_edt, widgetName = "update_personal_info_extra_from_income_company_name_edt")
    private NoEmptyEditText update_personal_info_extra_from_income_company_name_edt;    //额外-工资-单位名称

    @BindView(id = R.id.update_personal_info_extra_from_income_company_address_tv, widgetName = "update_personal_info_extra_from_income_company_address_tv")
    private TextView update_personal_info_extra_from_income_company_address_tv;  //额外-工资-公司地址

    @BindView(id = R.id.update_personal_info_extra_from_income_company_address1_tv, widgetName = "update_personal_info_extra_from_income_company_address1_tv")
    private TextView update_personal_info_extra_from_income_company_address1_tv; //额外-工资-详细地址

    @BindView(id = R.id.update_personal_info_extra_from_income_company_address2_tv, widgetName = "update_personal_info_extra_from_income_company_address2_tv")
    private NoEmptyEditText update_personal_info_extra_from_income_company_address2_tv; //额外-工资-门牌号

    @BindView(id = R.id.update_personal_extra_info_work_position_tv, widgetName = "update_personal_extra_info_work_position_tv")
    private TextView update_personal_extra_info_work_position_tv;                //额外-工资-职务

    @BindView(id = R.id.update_personal_info_extra_from_income_work_phone_num_edt, widgetName = "update_personal_info_extra_from_income_work_phone_num_edt")
    private NoEmptyEditText update_personal_info_extra_from_income_work_phone_num_edt;  //额外-工资-单位座机

    @BindView(id = R.id.update_personal_info_house_type_tv, widgetName = "update_personal_info_house_type_tv")
    private TextView update_personal_info_house_type_tv;                     //房屋性质

    @BindView(id = R.id.update_personal_info_house_area_edt, widgetName = "update_personal_info_house_area_edt")
    private NoEmptyEditText update_personal_info_house_area_edt;                    //房屋面积

    @BindView(id = R.id.update_personal_info_house_owner_name_edt, widgetName = "update_personal_info_house_owner_name_edt")
    private NoEmptyEditText update_personal_info_house_owner_name_edt;              //房屋所有人

    @BindView(id = R.id.update_personal_info_house_owner_relation_tv, widgetName = "update_personal_info_house_owner_relation_tv")
    private TextView update_personal_info_house_owner_relation_tv;           //与申请人关系

    @BindView(id = R.id.update_personal_info_urg_relation1_tv, widgetName = "update_personal_info_urg_relation1_tv")
    private TextView update_personal_info_urg_relation1_tv;           //紧急联系人-与申请人关系1

    @BindView(id = R.id.update_personal_info_urg_mobile1_edt, widgetName = "update_personal_info_urg_mobile1_edt")
    private EditText update_personal_info_urg_mobile1_edt;            //紧急联系人-手机号1

    @BindView(id = R.id.update_personal_info_urg_contact1_edt, widgetName = "update_personal_info_urg_contact1_edt")
    private NoEmptyEditText update_personal_info_urg_contact1_edt;           //紧急联系人-姓名1

    @BindView(id = R.id.update_personal_info_urg_relation2_tv, widgetName = "update_personal_info_urg_relation2_tv")
    private TextView update_personal_info_urg_relation2_tv;           //紧急联系人-与申请人关系2

    @BindView(id = R.id.update_personal_info_urg_mobile2_edt, widgetName = "update_personal_info_urg_mobile2_edt")
    private EditText update_personal_info_urg_mobile2_edt;            //紧急联系人-手机号2

    @BindView(id = R.id.update_personal_info_urg_contact2_edt, widgetName = "update_personal_info_urg_contact2_edt")
    private NoEmptyEditText update_personal_info_urg_contact2_edt;           //紧急联系人-姓名2

    private LinearLayout update_personal_info_live_with_parent_lin;       //是否与父母同住

    @BindView(id = R.id.update_personal_info_live_with_parent_tv, widgetName = "update_personal_info_live_with_parent_tv")
    private TextView update_personal_info_live_with_parent_tv;//是否与父母同住

    private File imageFile;
    private OcrResp.ShowapiResBodyBean mOcrResp;
    private ClientInfo clientInfo;

    @BindView(id = R.id.submit_img, widgetName = "submit_img", onClick = "submitMaterial")
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personal_info);
        UBT.bind(this);
        initTitleBar(this, "个人资料").setLeftClickListener(v -> showDoubleCheckForExit());

        initView();

        getInfo();  //获取用户信息

//        findViewById(R.id.submit_img).setOnClickListener(v -> {
//            submit();   //更新信息
//        });
        submitBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.clearFocus();
                    submit();
                }
            }
        });
    }

    private void submitMaterial(View view) {
        submitBtn.setFocusable(true);
        submitBtn.setFocusableInTouchMode(true);
        submitBtn.requestFocus();
        submitBtn.requestFocusFromTouch();
    }

    private void initView() {
        clientInfo = new ClientInfo();
        //初始化
        update_personal_info_clt_nm_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_clt_nm_edt);
        update_personal_info_id_no_edt = (EditText) findViewById(R.id.update_personal_info_id_no_edt);
        update_personal_info_gender_tv = (TextView) findViewById(R.id.update_personal_info_gender_tv);
        update_personal_info_reg_tv = (TextView) findViewById(R.id.update_personal_info_reg_tv);
        update_personal_info_mobile_edt = (EditText) findViewById(R.id.update_personal_info_mobile_edt);
        update_personal_info_education_tv = (TextView) findViewById(R.id.update_personal_info_education_tv);
        update_personal_info_current_address_tv = (TextView) findViewById(R.id.update_personal_info_current_address_tv);
        update_personal_info_current_address1_tv = (TextView) findViewById(R.id.update_personal_info_current_address1_tv);
        update_personal_info_current_address2_tv = (NoEmptyEditText) findViewById(R.id.update_personal_info_current_address2_tv);
        update_personal_info_income_from_tv = (TextView) findViewById(R.id.update_personal_info_income_from_tv);
        update_personal_info_from_income_year_edt = (EditText) findViewById(R.id.update_personal_info_from_income_year_edt);
        update_personal_info_from_income_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_from_income_company_name_edt);
        update_personal_info_from_income_company_address_tv = (TextView) findViewById(R.id.update_personal_info_from_income_company_address_tv);
        update_personal_info_from_income_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_from_income_company_address1_tv);
        update_personal_info_from_income_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_personal_info_from_income_company_address2_tv);
        update_personal_info_work_position_tv = (TextView) findViewById(R.id.update_personal_info_work_position_tv);
        update_personal_info_from_income_work_phone_num_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_from_income_work_phone_num_edt);
        update_personal_info_from_self_year_edt = (EditText) findViewById(R.id.update_personal_info_from_self_year_edt);
        update_personal_info_from_self_type_tv = (TextView) findViewById(R.id.update_personal_info_from_self_type_tv);
        update_personal_info_from_self_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_from_self_company_name_edt);
        update_personal_info_from_self_company_address_tv = (TextView) findViewById(R.id.update_personal_info_from_self_company_address_tv);
        update_personal_info_from_self_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_from_self_company_address1_tv);
        update_personal_info_from_self_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_personal_info_from_self_company_address2_tv);
        update_personal_info_from_other_year_edt = (EditText) findViewById(R.id.update_personal_info_from_other_year_edt);
        update_personal_info_from_other_remark_tv = (NoEmptyEditText) findViewById(R.id.update_personal_info_from_other_remark_tv);
        update_personal_info_extra_income_from_tv = (TextView) findViewById(R.id.update_personal_info_extra_income_from_tv);
        update_personal_info_extra_from_income_year_edt = (EditText) findViewById(R.id.update_personal_info_extra_from_income_year_edt);
        update_personal_info_extra_from_income_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_extra_from_income_company_name_edt);
        update_personal_info_extra_from_income_company_address_tv = (TextView) findViewById(R.id.update_personal_info_extra_from_income_company_address_tv);
        update_personal_info_extra_from_income_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_extra_from_income_company_address1_tv);
        update_personal_info_extra_from_income_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_personal_info_extra_from_income_company_address2_tv);
        update_personal_extra_info_work_position_tv = (TextView) findViewById(R.id.update_personal_extra_info_work_position_tv);
        update_personal_info_extra_from_income_work_phone_num_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_extra_from_income_work_phone_num_edt);
        update_personal_info_house_type_tv = (TextView) findViewById(R.id.update_personal_info_house_type_tv);
        update_personal_info_house_area_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_house_area_edt);
        update_personal_info_house_owner_name_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_house_owner_name_edt);
        update_personal_info_house_owner_relation_tv = (TextView) findViewById(R.id.update_personal_info_house_owner_relation_tv);
        update_personal_info_urg_relation1_tv = (TextView) findViewById(R.id.update_personal_info_urg_relation1_tv);
        update_personal_info_urg_mobile1_edt = (EditText) findViewById(R.id.update_personal_info_urg_mobile1_edt);
        update_personal_info_urg_contact1_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_urg_contact1_edt);
        update_personal_info_urg_relation2_tv = (TextView) findViewById(R.id.update_personal_info_urg_relation2_tv);
        update_personal_info_urg_mobile2_edt = (EditText) findViewById(R.id.update_personal_info_urg_mobile2_edt);
        update_personal_info_urg_contact2_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_urg_contact2_edt);

        update_personal_info_live_with_parent_lin = (LinearLayout) findViewById(R.id.update_personal_info_live_with_parent_lin);
        update_personal_info_live_with_parent_tv = (TextView) findViewById(R.id.update_personal_info_live_with_parent_tv);
        mScrollView = ((NestedScrollView) findViewById(R.id.scrollView));


        //回到顶部按钮
        findViewById(R.id.fab).setOnClickListener(v -> mScrollView.smoothScrollTo(0, 0));
        update_personal_info_from_income_group_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_income_group_lin);
        update_personal_info_from_self_group_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_self_group_lin);
        update_personal_info_from_other_group_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_other_group_lin);


        //选择收入来源
        income_from_lin = (LinearLayout) findViewById(R.id.update_personal_info_income_from_lin);
        income_from_tv = (TextView) findViewById(R.id.update_personal_info_income_from_tv);
        income_from_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(incomelist, UPDATE_INCOME_FROME_INDEX,
                income_from_lin,
                income_from_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_INCOME_FROME_INDEX = selectedIndex;
                    if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("工资")) {
                        findViewById(R.id.update_personal_info_from_income_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_personal_info_from_income_group_lin).setVisibility(View.GONE);
                    }
                    if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("自营")) {
                        findViewById(R.id.update_personal_info_from_self_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_personal_info_from_self_group_lin).setVisibility(View.GONE);
                    }
                    if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("其他")) {
                        findViewById(R.id.update_personal_info_from_other_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_personal_info_from_other_group_lin).setVisibility(View.GONE);
                    }
                }));

        //工资 公司地址
        update_personal_info_from_income_company_address_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_income_company_address_lin);
        update_personal_info_from_income_company_address_tv = (TextView) findViewById(R.id.update_personal_info_from_income_company_address_tv);
        update_personal_info_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_from_income_company_address_lin,
                        update_personal_info_from_income_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_personal_info_from_income_company_address1_tv.setText("")
                );
            }
        });

        //工资 详细地址
        update_personal_info_from_income_company_address1_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_income_company_address1_lin);
        update_personal_info_from_income_company_address1_lin.setOnClickListener(v -> {
            if (update_personal_info_from_income_company_address_tv != null) {
                update_personal_info_from_income_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_from_income_company_address1_lin.getId();
                requestPOI(update_personal_info_from_income_company_address_tv.getText().toString());
            } else {
                update_personal_info_from_income_company_address1_tv.setEnabled(false);
            }
        });

        // 工资 选择职务
        update_personal_info_from_income_work_position_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_income_work_position_lin);
        update_personal_info_work_position_tv = (TextView) findViewById(R.id.update_personal_info_work_position_tv);
        update_personal_info_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                update_personal_info_from_income_work_position_lin,
                update_personal_info_work_position_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_FROM_INCOME_WORK_POSITION_INDEX = selectedIndex));

        //自营 业务类型
        update_personal_info_from_self_type_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_self_type_lin);
        update_personal_info_from_self_type_tv = (TextView) findViewById(R.id.update_personal_info_from_self_type_tv);
        update_personal_info_from_self_type_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.busi_type_list_key,
                UPDATE_FROM_SELF_TYPE_INDEX,
                update_personal_info_from_self_type_lin,
                update_personal_info_from_self_type_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_FROM_SELF_TYPE_INDEX = selectedIndex;
                    if (YusionApp.CONFIG_RESP.busi_type_list_value.get(UPDATE_FROM_SELF_TYPE_INDEX).equals("其他")) {
                        EditText editText = new EditText(this);
                        new AlertDialog.Builder(this)
                                .setTitle("请输入业务类型")
                                .setView(editText)
                                .setCancelable(false)
                                .setPositiveButton("确定", (dialog, which) -> {
                                    update_personal_info_from_self_type_tv.setText(editText.getText().toString());
                                    UPDATE_FROM_SELF_TYPE_INDEX = 0;
                                    InputMethodUtil.hideInputMethod(this);
                                    dialog.dismiss();
                                })
                                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
                    }
                }
        ));

        //自营 单位地址
        update_personal_info_from_self_company_address_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_self_company_address_lin);
        update_personal_info_from_self_company_address_tv = (TextView) findViewById(R.id.update_personal_info_from_self_company_address_tv);
        update_personal_info_from_self_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_from_self_company_address_lin,
                        update_personal_info_from_self_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_personal_info_from_self_company_address1_tv.setText("")
                );
            }
        });

        //自营 详细地址
        update_personal_info_from_self_company_address1_lin = (LinearLayout) findViewById(R.id.update_personal_info_from_self_company_address1_lin);
        update_personal_info_from_self_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_from_self_company_address1_tv);
        update_personal_info_from_self_company_address1_lin.setOnClickListener(v -> {
            if (update_personal_info_from_self_company_address_tv != null) {
                update_personal_info_from_self_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_from_self_company_address1_lin.getId();
                requestPOI(update_personal_info_from_self_company_address_tv.getText().toString());
            } else {
                update_personal_info_from_self_company_address1_tv.setEnabled(false);
            }
        });

        //额外 公司地址
        update_personal_info_extra_from_income_company_address_lin = (LinearLayout) findViewById(R.id.update_personal_info_extra_from_income_company_address_lin);
        update_personal_info_extra_from_income_company_address_tv = (TextView) findViewById(R.id.update_personal_info_extra_from_income_company_address_tv);
        update_personal_info_extra_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_extra_from_income_company_address_lin,
                        update_personal_info_extra_from_income_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_personal_info_extra_from_income_company_address1_tv.setText("")
                );
            }
        });

        //选择额外收入来源
        update_personal_info_extra_from_income_group_lin = (LinearLayout) findViewById(R.id.update_personal_info_extra_from_income_group_lin);
        //额外工资
        income_extra_from_lin = (LinearLayout) findViewById(R.id.update_personal_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) findViewById(R.id.update_personal_info_extra_income_from_tv);
        income_extra_from_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(incomeextarlist,
                UPDATE_EXTRA_INCOME_FROME_INDEX,
                income_extra_from_lin,
                income_extra_from_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_EXTRA_INCOME_FROME_INDEX = selectedIndex;
                    if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("工资")) {
                        findViewById(R.id.update_personal_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_personal_info_extra_from_income_group_lin).setVisibility(View.GONE);
                    }
                    if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("无")) {
                        findViewById(R.id.update_personal_info_extra_from_income_group_lin).setVisibility(View.GONE);

                    }
                }
        ));

        //额外 详细地址
        update_personal_info_extra_from_income_company_address1_lin = (LinearLayout) findViewById(R.id.update_personal_info_extra_from_income_company_address1_lin);
        update_personal_info_extra_from_income_company_address1_tv = (TextView) findViewById(R.id.update_personal_info_extra_from_income_company_address1_tv);
        update_personal_info_extra_from_income_company_address1_lin.setOnClickListener(v -> {
            if (update_personal_info_extra_from_income_company_address_tv != null) {
                update_personal_info_extra_from_income_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_extra_from_income_company_address1_lin.getId();
                requestPOI(update_personal_info_extra_from_income_company_address_tv.getText().toString());
            } else {
                update_personal_info_extra_from_income_company_address1_tv.setEnabled(false);
            }
        });

        // 额外 选择职务
        update_personal_info_extra_from_income_work_position_lin = (LinearLayout) findViewById(R.id.update_personal_info_extra_from_income_work_position_lin);
        update_personal_info_extra_from_income_work_position_tv = (TextView) findViewById(R.id.update_personal_extra_info_work_position_tv);
        update_personal_info_extra_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                UPDATE_FROM_EXTRA_WORK_POSITION_INDEX,
                update_personal_info_extra_from_income_work_position_lin,
                update_personal_info_extra_from_income_work_position_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex));

        //选择性别
        update_personal_info_gender_lin = (LinearLayout) findViewById(R.id.update_personal_info_gender_lin);
        update_personal_info_gender_tv = (TextView) findViewById(R.id.update_personal_info_gender_tv);
        update_personal_info_gender_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.gender_list_key,
                UPDATE_SEX_INDEX,
                update_personal_info_gender_lin,
                update_personal_info_gender_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_SEX_INDEX = selectedIndex
        ));

        //选择户籍地
        update_personal_info_reg_lin = (LinearLayout) findViewById(R.id.update_personal_info_reg_lin);
        update_personal_info_reg_tv = (TextView) findViewById(R.id.update_personal_info_reg_tv);
        update_personal_info_reg_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_reg_lin,
                        update_personal_info_reg_tv,
                        "请选择所在地区",
                        (clickedView, city) -> {

                        }
                );
            }
        });

        //选择学历
        update_personal_info_education_lin = (LinearLayout) findViewById(R.id.update_personal_info_education_lin);
        update_personal_info_education_tv = (TextView) findViewById(R.id.update_personal_info_education_tv);
        update_personal_info_education_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.education_list_key,
                UPDATE_EDUCATION_INDEX,
                update_personal_info_education_lin,
                update_personal_info_education_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_EDUCATION_INDEX = selectedIndex));

        //现在地址
        update_personal_info_current_address_lin = (LinearLayout) findViewById(R.id.update_personal_info_current_address_lin);
        update_personal_info_current_address_tv = (TextView) findViewById(R.id.update_personal_info_current_address_tv);
        update_personal_info_current_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_current_address_lin,
                        update_personal_info_current_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_personal_info_current_address1_tv.setText("")
                );
            }
        });

        //详细地址
        update_personal_info_current_address1_lin = (LinearLayout) findViewById(R.id.update_personal_info_current_address1_lin);
        update_personal_info_current_address1_tv = (TextView) findViewById(R.id.update_personal_info_current_address1_tv);
        update_personal_info_current_address1_lin.setOnClickListener(v -> {
            if (!update_personal_info_current_address_tv.getText().toString().isEmpty()) {
                update_personal_info_current_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_current_address1_lin.getId();
                requestPOI(update_personal_info_current_address_tv.getText().toString());
            } else {
                update_personal_info_current_address1_tv.setEnabled(false);
            }
        });

        //房屋类型
        update_personal_info_house_type_lin = (LinearLayout) findViewById(R.id.update_personal_info_house_type_lin);
        update_personal_info_house_type_tv = (TextView) findViewById(R.id.update_personal_info_house_type_tv);
        update_personal_info_house_type_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_type_list_key,
                UPDATE_HOUSE_TYPE_INDEX,
                update_personal_info_house_type_lin,
                update_personal_info_house_type_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_HOUSE_TYPE_INDEX = selectedIndex));

        //与申请人关系
        update_personal_info_house_owner_relation_lin = (LinearLayout) findViewById(R.id.update_personal_info_house_owner_relation_lin);
        update_personal_info_house_owner_relation_tv = (TextView) findViewById(R.id.update_personal_info_house_owner_relation_tv);
        update_personal_info_house_owner_relation_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_relationship_list_key,
                UPDATE_HOUSE_OWNER_RELATION_INDEX,
                update_personal_info_house_owner_relation_lin,
                update_personal_info_house_owner_relation_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_HOUSE_OWNER_RELATION_INDEX = selectedIndex));
        update_personal_info_urg_relation2_lin = (LinearLayout) findViewById(R.id.update_personal_info_urg_relation2_lin);
        update_personal_info_urg_relation2_tv = (TextView) findViewById(R.id.update_personal_info_urg_relation2_tv);
        update_personal_info_urg_relation2_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.urg_other_relationship_list_key,
                UPDATE_URG_RELATION_INDEX2,
                update_personal_info_urg_relation2_lin,
                update_personal_info_urg_relation2_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_URG_RELATION_INDEX2 = selectedIndex));
        update_personal_info_urg_relation1_lin = (LinearLayout) findViewById(R.id.update_personal_info_urg_relation1_lin);
        update_personal_info_urg_relation1_tv = (TextView) findViewById(R.id.update_personal_info_urg_relation1_tv);
        update_personal_info_urg_relation1_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.urg_rela_relationship_list_key,
                UPDATE_URG_RELATION_INDEX1,
                update_personal_info_urg_relation1_lin,
                update_personal_info_urg_relation1_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_URG_RELATION_INDEX1 = selectedIndex));
        //紧急联系人
        update_personal_info_urg_mobile1_img = (ImageView) findViewById(R.id.update_personal_info_urg_mobile1_img);
        update_personal_info_urg_mobile1_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = update_personal_info_urg_mobile1_img.getId();
            selectContact();
        });
        update_personal_info_urg_mobile2_img = (ImageView) findViewById(R.id.update_personal_info_urg_mobile2_img);
        update_personal_info_urg_mobile2_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = update_personal_info_urg_mobile2_img.getId();
            selectContact();
        });
        update_personal_info_urg_mobile1_edt = (EditText) findViewById(R.id.update_personal_info_urg_mobile1_edt);
        update_personal_info_urg_mobile2_edt = (EditText) findViewById(R.id.update_personal_info_urg_mobile2_edt);
        update_personal_info_urg_contact1_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_urg_contact1_edt);
        update_personal_info_urg_contact2_edt = (NoEmptyEditText) findViewById(R.id.update_personal_info_urg_contact2_edt);


        //是否与父母同住
        update_personal_info_live_with_parent_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(ifwithparentlist,
                UPDATE_LIVE_WITH_PARENT_INDEX,
                update_personal_info_live_with_parent_lin,
                update_personal_info_live_with_parent_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_LIVE_WITH_PARENT_INDEX = selectedIndex));


    }

    private void requestPOI(String city) {
        if (city != null) {
            String[] citys = city.split("/");
            if (citys.length == 3) {
                String city1 = citys[1];
                String city2 = citys[2];
                Intent intent = new Intent(this, AMapPoiListActivity.class);
                intent.putExtra("city", city1);
                intent.putExtra("keywords", city2);
                startActivityForResult(intent, Constants.REQUEST_ADDRESS);
            }
        }
    }

    //获取用户信息
    public void getClientinfo(ClientInfo data) {
        if (data != null) {
            clientInfo = data;
            //填充
            update_personal_info_clt_nm_edt.setText(clientInfo.clt_nm);
            update_personal_info_id_no_edt.setText(clientInfo.id_no);
            update_personal_info_gender_tv.setText(clientInfo.gender);
            if (clientInfo.reg_addr.province.equals("") && clientInfo.reg_addr.city.equals("") && clientInfo.reg_addr.district.equals("")) {
                update_personal_info_reg_tv.setText(null);
            } else {
                update_personal_info_reg_tv.setText(clientInfo.reg_addr.province + "/" + clientInfo.reg_addr.city + "/" + clientInfo.reg_addr.district);
            }
            update_personal_info_mobile_edt.setText(clientInfo.mobile);
            update_personal_info_education_tv.setText(clientInfo.edu);
            if (clientInfo.current_addr.province.equals("") && clientInfo.current_addr.city.equals("") && clientInfo.current_addr.district.equals("")) {
                update_personal_info_current_address_tv.setText(null);
            } else {
                update_personal_info_current_address_tv.setText(clientInfo.current_addr.province + "/" + clientInfo.current_addr.city + "/" + clientInfo.current_addr.district);
            }
            update_personal_info_current_address1_tv.setText(clientInfo.current_addr.address1);
            update_personal_info_current_address2_tv.setText(clientInfo.current_addr.address2);
            update_personal_info_income_from_tv.setText(clientInfo.major_income_type);
            //判断主要收入类型
            switch (clientInfo.major_income_type) {
                case "工资":
                    update_personal_info_from_income_group_lin.setVisibility(View.VISIBLE);
                    update_personal_info_from_income_year_edt.setText(clientInfo.major_income);
                    update_personal_info_from_income_company_name_edt.setText(clientInfo.major_company_name);
                    if (clientInfo.major_company_addr.province.equals("") && clientInfo.major_company_addr.city.equals("") && clientInfo.major_company_addr.district.equals("")) {
                        update_personal_info_from_income_company_address_tv.setText(null);
                    } else {
                        update_personal_info_from_income_company_address_tv.setText(clientInfo.major_company_addr.province + "/" + clientInfo.major_company_addr.city + "/" + clientInfo.major_company_addr.district);
                    }
                    update_personal_info_from_income_company_address1_tv.setText(clientInfo.major_company_addr.address1);
                    update_personal_info_from_income_company_address2_tv.setText(clientInfo.major_company_addr.address2);
                    update_personal_info_work_position_tv.setText(clientInfo.major_work_position);
                    update_personal_info_from_income_work_phone_num_edt.setText(clientInfo.major_work_phone_num);
                    break;
                case "自营":
                    update_personal_info_from_self_group_lin.setVisibility(View.VISIBLE);
                    update_personal_info_from_self_year_edt.setText(clientInfo.major_income);
                    update_personal_info_from_self_type_tv.setText(clientInfo.major_busi_type);
                    update_personal_info_from_self_company_name_edt.setText(clientInfo.major_company_name);
                    if (clientInfo.major_company_addr.province.equals("") && clientInfo.major_company_addr.city.equals("") && clientInfo.major_company_addr.district.equals("")) {
                        update_personal_info_from_self_company_address_tv.setText(null);
                    } else {
                        update_personal_info_from_self_company_address_tv.setText(clientInfo.major_company_addr.province + "/" + clientInfo.major_company_addr.city + "/" + clientInfo.major_company_addr.district);
                    }
                    update_personal_info_from_self_company_address1_tv.setText(clientInfo.major_company_addr.address1);
                    update_personal_info_from_self_company_address2_tv.setText(clientInfo.major_company_addr.address2);
                    break;
                case "其他":
                    update_personal_info_from_other_group_lin.setVisibility(View.VISIBLE);
                    update_personal_info_from_other_year_edt.setText(clientInfo.major_income);
                    update_personal_info_from_other_remark_tv.setText(clientInfo.major_remark);
                    break;
            }
            update_personal_info_extra_income_from_tv.setText(clientInfo.extra_income_type);
            //判断额外收入类型
            switch (clientInfo.extra_income_type) {
                case "工资":
                    update_personal_info_extra_from_income_group_lin.setVisibility(View.VISIBLE);
                    update_personal_info_extra_from_income_year_edt.setText(clientInfo.extra_income);
                    update_personal_info_extra_from_income_company_name_edt.setText(clientInfo.extra_company_name);
                    if (clientInfo.extra_company_addr.province.equals("") && clientInfo.extra_company_addr.city.equals("") && clientInfo.extra_company_addr.district.equals("")) {
                        update_personal_info_extra_from_income_company_address_tv.setText(null);
                    } else {
                        update_personal_info_extra_from_income_company_address_tv.setText(clientInfo.extra_company_addr.province + "/" + clientInfo.extra_company_addr.city + "/" + clientInfo.extra_company_addr.district);
                    }
                    update_personal_info_extra_from_income_company_address1_tv.setText(clientInfo.extra_company_addr.address1);
                    update_personal_info_extra_from_income_company_address2_tv.setText(clientInfo.extra_company_addr.address2);
                    update_personal_extra_info_work_position_tv.setText(clientInfo.extra_work_position);
                    update_personal_info_extra_from_income_work_phone_num_edt.setText(clientInfo.extra_work_phone_num);
                    break;
                case "无":
                    update_personal_info_extra_from_income_group_lin.setVisibility(View.GONE);
            }
            update_personal_info_house_type_tv.setText(clientInfo.house_type);
            update_personal_info_house_area_edt.setText(clientInfo.house_area);
            update_personal_info_house_owner_name_edt.setText(clientInfo.house_owner_name);
            update_personal_info_house_owner_relation_tv.setText(clientInfo.house_owner_relation);
            update_personal_info_urg_relation1_tv.setText(clientInfo.urg_relation1);
            update_personal_info_urg_mobile1_edt.setText(clientInfo.urg_mobile1);
            update_personal_info_urg_contact1_edt.setText(clientInfo.urg_contact1);
            update_personal_info_urg_relation2_tv.setText(clientInfo.urg_relation2);
            update_personal_info_urg_mobile2_edt.setText(clientInfo.urg_mobile2);
            update_personal_info_urg_contact2_edt.setText(clientInfo.urg_contact2);
            update_personal_info_live_with_parent_tv.setText(clientInfo.is_live_with_parent);
        }

    }

    //提交用户信息
    public void updateClientinfo(OnVoidCallBack callBack) {
        //校验
        if (checkUserInfo()) {
            //提交
            clientInfo.clt_nm = update_personal_info_clt_nm_edt.getText().toString().trim();
            clientInfo.id_no = update_personal_info_id_no_edt.getText().toString().trim();
            clientInfo.gender = update_personal_info_gender_tv.getText().toString().trim();
            clientInfo.reg_addr.province = update_personal_info_reg_tv.getText().toString().trim().split("/")[0];
            clientInfo.reg_addr.city = update_personal_info_reg_tv.getText().toString().trim().split("/")[1];
            clientInfo.reg_addr.district = update_personal_info_reg_tv.getText().toString().trim().split("/")[2];
            clientInfo.mobile = update_personal_info_mobile_edt.getText().toString().trim();
            clientInfo.edu = update_personal_info_education_tv.getText().toString().trim();
            clientInfo.current_addr.province = update_personal_info_current_address_tv.getText().toString().trim().split("/")[0];
            clientInfo.current_addr.city = update_personal_info_current_address_tv.getText().toString().trim().split("/")[1];
            clientInfo.current_addr.district = update_personal_info_current_address_tv.getText().toString().trim().split("/")[2];
            clientInfo.current_addr.address1 = update_personal_info_current_address1_tv.getText().toString().trim();
            clientInfo.current_addr.address2 = update_personal_info_current_address2_tv.getText().toString().trim();
            clientInfo.major_income_type = update_personal_info_income_from_tv.getText().toString().trim();
            clientInfo.is_live_with_parent = update_personal_info_live_with_parent_tv.getText().toString().trim();

            //判断主要收入类型
            Log.e("主要收入类型", "" + update_personal_info_income_from_tv.getText().toString().trim());
            switch (update_personal_info_income_from_tv.getText().toString().trim()) {
                case "工资":
                    clientInfo.major_income = update_personal_info_from_income_year_edt.getText().toString().trim();
                    clientInfo.major_company_name = update_personal_info_from_income_company_name_edt.getText().toString().trim();
                    if (TextUtils.isEmpty(update_personal_info_from_income_company_address_tv.getText().toString())) {
                        clientInfo.major_company_addr.province = "";
                        clientInfo.major_company_addr.city = "";
                        clientInfo.major_company_addr.district = "";
                    } else {
                        clientInfo.major_company_addr.province = update_personal_info_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        clientInfo.major_company_addr.city = update_personal_info_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        clientInfo.major_company_addr.district = update_personal_info_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                    }
                    clientInfo.major_company_addr.address1 = update_personal_info_from_income_company_address1_tv.getText().toString().trim();
                    clientInfo.major_company_addr.address2 = update_personal_info_from_income_company_address2_tv.getText().toString().trim();
                    clientInfo.major_work_position = update_personal_info_work_position_tv.getText().toString().trim();
                    clientInfo.major_work_phone_num = update_personal_info_from_income_work_phone_num_edt.getText().toString().trim();
                    break;
                case "自营":
                    clientInfo.major_income = update_personal_info_from_self_year_edt.getText().toString().trim();
                    clientInfo.major_busi_type = update_personal_info_from_self_type_tv.getText().toString().trim();
                    clientInfo.major_company_name = update_personal_info_from_self_company_name_edt.getText().toString().trim();
                    if (TextUtils.isEmpty(update_personal_info_from_self_company_address_tv.getText())) {
                        clientInfo.major_company_addr.province = "";
                        clientInfo.major_company_addr.city = "";
                        clientInfo.major_company_addr.district = "";
                    } else {
                        clientInfo.major_company_addr.province = update_personal_info_from_self_company_address_tv.getText().toString().trim().split("/")[0];
                        clientInfo.major_company_addr.city = update_personal_info_from_self_company_address_tv.getText().toString().trim().split("/")[1];
                        clientInfo.major_company_addr.district = update_personal_info_from_self_company_address_tv.getText().toString().trim().split("/")[2];
                    }
                    clientInfo.major_company_addr.address1 = update_personal_info_from_self_company_address1_tv.getText().toString().trim();
                    clientInfo.major_company_addr.address2 = update_personal_info_from_self_company_address2_tv.getText().toString().trim();
                    break;
                case "其他":
                    clientInfo.major_income = update_personal_info_from_other_year_edt.getText().toString().trim();
                    clientInfo.major_remark = update_personal_info_from_other_remark_tv.getText().toString().trim();
                    break;
            }
            clientInfo.extra_income_type = update_personal_info_extra_income_from_tv.getText().toString().trim();
            //判断额外收入类型
            switch (update_personal_info_extra_income_from_tv.getText().toString().trim()) {
                case "工资":
                    clientInfo.extra_income = update_personal_info_extra_from_income_year_edt.getText().toString().trim();
                    clientInfo.extra_company_name = update_personal_info_extra_from_income_company_name_edt.getText().toString().trim();
                    if (TextUtils.isEmpty(update_personal_info_extra_from_income_company_address_tv.getText().toString())) {
                        clientInfo.extra_company_addr.province = "";
                        clientInfo.extra_company_addr.city = "";
                        clientInfo.extra_company_addr.district = "";
                    } else {
                        clientInfo.extra_company_addr.province = update_personal_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        clientInfo.extra_company_addr.city = update_personal_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        clientInfo.extra_company_addr.district = update_personal_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                    }
                    clientInfo.extra_company_addr.address1 = update_personal_info_extra_from_income_company_address1_tv.getText().toString().trim();
                    clientInfo.extra_company_addr.address2 = update_personal_info_extra_from_income_company_address2_tv.getText().toString().trim();
                    clientInfo.extra_work_position = update_personal_extra_info_work_position_tv.getText().toString().trim();
                    clientInfo.extra_work_phone_num = update_personal_info_extra_from_income_work_phone_num_edt.getText().toString().trim();
                    break;
                case "无":
                    break;
            }
            clientInfo.house_type = update_personal_info_house_type_tv.getText().toString().trim();
            clientInfo.house_area = update_personal_info_house_area_edt.getText().toString().trim();
            clientInfo.house_owner_name = update_personal_info_house_owner_name_edt.getText().toString().trim();
            clientInfo.house_owner_relation = update_personal_info_house_owner_relation_tv.getText().toString().trim();
            clientInfo.urg_relation1 = update_personal_info_urg_relation1_tv.getText().toString().trim();
            clientInfo.urg_mobile1 = update_personal_info_urg_mobile1_edt.getText().toString().trim();
            clientInfo.urg_contact1 = update_personal_info_urg_contact1_edt.getText().toString().trim();
            clientInfo.urg_relation2 = update_personal_info_urg_relation2_tv.getText().toString().trim();
            clientInfo.urg_mobile2 = update_personal_info_urg_mobile2_edt.getText().toString().trim();
            clientInfo.urg_contact2 = update_personal_info_urg_contact2_edt.getText().toString().trim();
            callBack.callBack();
        }
    }

    private boolean checkUserInfo() {
        if (update_personal_info_clt_nm_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_id_no_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "身份证号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(update_personal_info_id_no_edt.getText().toString())) {
            Toast.makeText(this, "身份证号有误", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_gender_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "性别不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_reg_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "户籍地不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_mobile_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(update_personal_info_mobile_edt.getText().toString())) {
            Toast.makeText(this, "手机号码有误", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_education_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "学历不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_current_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "现住地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_current_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_current_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_live_with_parent_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "是否与父母同住不能为空", Toast.LENGTH_SHORT).show();
        }
        //主要工资
        else if (update_personal_info_income_from_tv.getText().toString().equals("")) {
            Toast.makeText(this, "收入来源不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("工资") && update_personal_info_from_income_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "工资年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("工资") && update_personal_info_from_income_company_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位名称不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("工资") && update_personal_info_from_income_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("工资") && update_personal_info_from_income_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("工资") && update_personal_info_from_income_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("工资") && update_personal_info_work_position_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "职务不能为空", Toast.LENGTH_SHORT).show();
        }  //主要自营
        else if (update_personal_info_income_from_tv.getText().toString().equals("自营") && update_personal_info_from_self_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "自营年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("自营") && update_personal_info_from_self_type_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "业务类型不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("自营") && update_personal_info_from_self_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "项目经营地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("自营") && update_personal_info_from_self_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "自营的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("自营") && update_personal_info_from_self_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "自营的门牌号不能为空", Toast.LENGTH_SHORT).show();
        }

        //主要其他
        else if (update_personal_info_income_from_tv.getText().toString().equals("其他") && update_personal_info_from_other_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "其他年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_income_from_tv.getText().toString().equals("其他") && update_personal_info_from_other_remark_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "备注不能为空", Toast.LENGTH_SHORT).show();
        }

        //额外工资
        else if (update_personal_info_extra_income_from_tv.getText().toString().equals("工资") && update_personal_info_extra_from_income_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_extra_income_from_tv.getText().toString().equals("工资") && update_personal_info_extra_from_income_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位名称不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_extra_income_from_tv.getText().toString().equals("工资") && update_personal_info_extra_from_income_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_extra_income_from_tv.getText().toString().equals("工资") && update_personal_info_extra_from_income_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_extra_income_from_tv.getText().toString().equals("工资") && update_personal_info_extra_from_income_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_extra_income_from_tv.getText().toString().equals("工资") && update_personal_extra_info_work_position_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "职务不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_type_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "房屋性质不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_area_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "房屋面积不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_owner_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_owner_relation_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_relation1_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(update_personal_info_urg_mobile1_edt.getText().toString())) {
            Toast.makeText(this, "紧急联系人手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(update_personal_info_urg_mobile1_edt.getText().toString())) {
            Toast.makeText(this, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_contact1_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "紧急联系人人姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_relation2_tv.getText().toString().isEmpty()) {
            Toast.makeText(this, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(update_personal_info_urg_mobile2_edt.getText().toString())) {
            Toast.makeText(this, "紧急联系人手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(update_personal_info_urg_mobile2_edt.getText().toString())) {
            Toast.makeText(this, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_contact2_edt.getText().toString().isEmpty()) {
            Toast.makeText(this, "紧急联系人人姓名不能为空", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_CONTACTS);
    }

    private void getInfo() {
        ProductApi.getClientInfo(this, new GetClientInfoReq(), data -> {
            if (data != null) {
                clientInfo = data;
                getClientinfo(clientInfo);
//                mUpdateImgsLabelFragment.setCltIdAndRole(clientInfo.clt_id, Constants.PersonType.LENDER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_CONTACTS) {
                Uri uri = data.getData();
                String[] contacts = ContactsUtil.getPhoneContacts(this, uri);
                String[] result = new String[]{"", ""};
                if (contacts != null) {
                    System.arraycopy(contacts, 0, result, 0, contacts.length);
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_personal_info_urg_mobile1_img.getId()) {
                    update_personal_info_urg_contact1_edt.setText(result[0]);
                    update_personal_info_urg_mobile1_edt.setText(result[1].replaceAll(" ", ""));
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_personal_info_urg_mobile2_img.getId()) {
                    update_personal_info_urg_contact2_edt.setText(result[0]);
                    update_personal_info_urg_mobile2_edt.setText(result[1].replaceAll(" ", ""));
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_personal_info_current_address1_lin.getId()) {
                    update_personal_info_current_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_personal_info_from_income_company_address1_lin.getId()) {
                    update_personal_info_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_personal_info_from_self_company_address1_lin.getId()) {
                    update_personal_info_from_self_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_personal_info_extra_from_income_company_address1_lin.getId()) {
                    update_personal_info_extra_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
            }
        }
    }

    public void submit() {
        //提交用户资料
        updateClientinfo(() -> ProductApi.updateClientInfo(UpdatePersonalInfoActivity.this, clientInfo, data -> {
            if (data == null) {
                {
                    return;
                }
            }
            clientInfo = data;
            UBT.sendAllUBTEvents(this);
            //上传影像件
            toCommitActivity(clientInfo.clt_id, "lender", "个人影像件资料", "continue");


//            mUpdateImgsLabelFragment.requestUpload(clientInfo.clt_id, () -> {
//                Intent intent = new Intent(UpdatePersonalInfoActivity.this, CommitActivity.class);
//                startActivity(intent);
//                finish();
//            });
        }));

//        //上传影像件
//        mUpdateImgsLabelFragment.requestUpload(clientInfo.clt_id, new OnVoidCallBack() {
//            @Override
//            public void callBack() {
//                //上传用户资料
//                mUpdatePersonalInfoFragment.updateClientinfo(new OnVoidCallBack() {
//                    @Override
//                    public void callBack() {
//                        ProductApi.updateClientInfo(UpdatePersonalInfoActivity.this, clientInfo, new OnItemDataCallBack<ClientInfo>() {
//                            @Override
//                            public void onItemDataCallBack(ClientInfo contact) {
//                                if (contact == null) return;
//                                Intent intent = new Intent(UpdatePersonalInfoActivity.this, CommitActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        });
//                    }
//                });
//            }
//        });
    }

    private void showDoubleCheckForExit() {
        new AlertDialog.Builder(this).setMessage("退出时未提交的更新将会被舍弃")
                .setPositiveButton("确定该操作", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("取消该操作", (dialog, which) -> dialog.dismiss()).show();
    }
//    private void initView() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        ArrayList<Fragment> mFragments = new ArrayList<>();
//        mUpdatePersonalInfoFragment = new UpdatePersonalInfoFragment();
//        mUpdateImgsLabelFragment = new UpdateImgsLabelFragment();
//        mFragments.add(mUpdatePersonalInfoFragment);
//        mFragments.add(mUpdateImgsLabelFragment);
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(new InfoViewPagerAdapter(getSupportFragmentManager(), mFragments));
//        MagicIndicator mMagicIndicator = (MagicIndicator) findViewById(R.id.tab_layout);
//        ImageView mSubmitImg = (ImageView) findViewById(R.id.submit_img);
//        mSubmitImg.setOnClickListener(v -> finish());
//        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setAdjustMode(true);
//        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
//            @Override
//            public int getCount() {
//                return mTabTitle == null ? 0 : mTabTitle.length;
//            }
//
//            @Override
//            public IPagerTitleView getTitleView(Context context, final int index) {
//                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
//                colorTransitionPagerTitleView.setNormalColor(0xFF999999);
//                colorTransitionPagerTitleView.setSelectedColor(0xFF06B7A3);
//                colorTransitionPagerTitleView.setText(mTabTitle[index]);
//                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(index));
//                return colorTransitionPagerTitleView;
//            }
//
//            @Override
//            public IPagerIndicator getIndicator(Context context) {
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setColors(getResources().getColor(R.color.system_color));
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
//                return indicator;
//            }
//        });
//        mMagicIndicator.setNavigator(commonNavigator);
//
//        ViewPagerHelper.bind(mMagicIndicator, viewPager);
//    }

//    private class InfoViewPagerAdapter extends FragmentPagerAdapter {
//
//        private final List<Fragment> mFragments;
//
//        InfoViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
//            super(fm);
//            mFragments = fragments;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragments == null ? 0 : mFragments.size();
//        }
//
//    }


}

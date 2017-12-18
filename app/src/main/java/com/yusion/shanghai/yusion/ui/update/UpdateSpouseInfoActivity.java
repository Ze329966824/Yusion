package com.yusion.shanghai.yusion.ui.update;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
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
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;
import com.yusion.shanghai.yusion.bean.user.GetClientInfoReq;
import com.yusion.shanghai.yusion.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ubt.UBT;
import com.yusion.shanghai.yusion.ubt.annotate.BindView;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.utils.CheckIdCardValidUtil;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.ContactsUtil;
import com.yusion.shanghai.yusion.utils.InputMethodUtil;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OcrUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion.widget.NoEmptyEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.yusion.shanghai.yusion.R.id.update_personal_info_current_address1_lin;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_current_address1_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_current_address2_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_current_address_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_education_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_house_area_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_house_owner_name_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_house_owner_relation_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_house_type_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_income_from_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_live_with_parent_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_mobile_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_reg_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_contact1_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_contact2_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_mobile1_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_mobile1_img;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_mobile2_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_mobile2_img;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_relation1_tv;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_relation2_tv;


public class UpdateSpouseInfoActivity extends UpdateInfoActivity {
//    private UpdateSpouseInfoFragment mUpdateSpouseInfoFragment;
//    private UpdateImgsLabelFragment mUpdateImgsLabelFragment;
//    private String[] mTabTitle = {"配偶信息", "影像件"};

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

    public String idBackImgUrl = "";
    public String idBackImgId = "";
    public String idFrontImgId = "";
    public String idFrontImgUrl = "";
    public String ID_BACK_FID = "";
    public String ID_FRONT_FID = "";
    public static int UPDATE_INCOME_FROME_INDEX;
    public static int UPDATE_EXTRA_INCOME_FROME_INDEX;
    public static int UPDATE_MARRIAGE_INDEX;
    public static int UPDATE_SEX_INDEX;
    public static int UPDATE_FROM_INCOME_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_EXTRA_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_SELF_TYPE_INDEX;
    public static int CURRENT_CLICKED_VIEW_FOR_ADDRESS = -1;
    public static int CURRENT_CLICKED_VIEW_FOR_CONTACT = -1;
    public static int UPDATE_EDUCATION_INDEX;
    public static int UPDATE_HOUSE_TYPE_INDEX;
    public static int UPDATE_HOUSE_OWNER_RELATION_INDEX;
    public static int UPDATE_URG_RELATION_INDEX1;
    public static int UPDATE_URG_RELATION_INDEX2;
    public static int UPDATE_LIVE_WITH_PARENT_INDEX;

    private LinearLayout income_from_lin;
    private LinearLayout income_extra_from_lin;

    @BindView(id = R.id.update_spouse_info_income_from_tv, widgetName = "update_spouse_info_income_from_tv")
    private TextView income_from_tv;
    @BindView(id = R.id.update_spouse_info_extra_income_from_tv, widgetName = "update_spouse_info_extra_income_from_tv")
    private TextView income_extra_from_tv;

    private LinearLayout update_spouse_info_marriage_lin;

    @BindView(id = R.id.update_spouse_info_marriage_tv, widgetName = "update_spouse_info_marriage_tv")
    private TextView update_spouse_info_marriage_tv;

    private LinearLayout update_spouse_info_gender_lin;
    private LinearLayout update_spouse_info_from_income_company_address_lin;
    private LinearLayout update_spouse_info_from_income_company_address1_lin;
    private LinearLayout update_spouse_info_from_income_work_position_lin;
    private LinearLayout update_spouse_info_from_self_company_address_lin;
    private LinearLayout update_spouse_info_from_self_company_address1_lin;
    private LinearLayout update_spouse_info_extra_from_income_company_address_lin;
    private LinearLayout update_spouse_info_extra_from_income_company_address1_lin;
    private LinearLayout update_spouse_info_extra_from_income_work_position_lin;


    private TextView update_spouse_info_divorced_tv;
    private LinearLayout update_spouse_info_register_addr_lin;
    private TextView update_spouse_info_register_addr_tv;
    private ImageView update_spouse_info_mobile_img;
    private LinearLayout update_spouse_info_from_self_type_lin;
    private NestedScrollView mScrollView;
    private LinearLayout update_spouse_info_marriage_group_lin;
    private LinearLayout update_spouse_info_divorced_group_lin;
    private LinearLayout update_spouse_info_die_group_lin;
    private LinearLayout update_spouse_info_from_income_group_lin;
    private LinearLayout update_spouse_info_from_self_group_lin;
    private LinearLayout update_spouse_info_from_other_group_lin;
    private LinearLayout update_spouse_info_extra_from_income_group_lin;
    private LinearLayout update_spouse_info_reg_lin;
    private LinearLayout update_spouse_info_education_lin;
    private LinearLayout update_spouse_info_current_address_lin;
    private LinearLayout update_spouse_info_current_address1_lin;
    private LinearLayout update_spouse_info_house_type_lin;
    private LinearLayout update_spouse_info_house_owner_relation_lin;
    private LinearLayout update_spouse_info_urg_relation1_lin;
    private LinearLayout update_spouse_info_urg_relation2_lin;
    private ImageView update_spouse_info_urg_mobile1_img;
    private ImageView update_spouse_info_urg_mobile2_img;
    //    private LinearLayout update_spouse_info_id_back_lin;
//    private LinearLayout update_spouse_info_id_front_lin;
    private TextView update_spouse_info_id_back_tv;
    private TextView update_spouse_info_id_front_tv;

    @BindView(id = R.id.update_spouse_info_clt_nm_edt, widgetName = "update_spouse_info_clt_nm_edt")
    private NoEmptyEditText update_spouse_info_clt_nm_edt;                       //姓名

    @BindView(id = R.id.update_spouse_info_id_no_edt, widgetName = "update_spouse_info_id_no_edt")
    private EditText update_spouse_info_id_no_edt;    //身份证号  

    @BindView(id = R.id.update_spouse_info_gender_tv, widgetName = "update_spouse_info_gender_tv")
    private TextView update_spouse_info_gender_tv;                       //性别

    @BindView(id = R.id.update_spouse_info_reg_tv, widgetName = "update_spouse_info_reg_tv")
    private TextView update_spouse_info_reg_tv;                           //户籍

    @BindView(id = R.id.update_spouse_info_mobile_edt, widgetName = "update_spouse_info_mobile_edt")
    private EditText update_spouse_info_mobile_edt;                       //手机号

    @BindView(id = R.id.update_spouse_info_education_tv, widgetName = "update_spouse_info_education_tv")
    private TextView update_spouse_info_education_tv;                       //学历


    @BindView(id = R.id.update_spouse_info_current_address_tv, widgetName = "update_spouse_info_current_address_tv")
    private TextView update_spouse_info_current_address_tv;               //现住地址

    @BindView(id = R.id.update_spouse_info_current_address1_tv, widgetName = "update_spouse_info_current_address1_tv")
    private TextView update_spouse_info_current_address1_tv;              //详细地址

    @BindView(id = R.id.update_spouse_info_current_address2_tv, widgetName = "update_spouse_info_current_address2_tv")
    private NoEmptyEditText update_spouse_info_current_address2_tv;              //门牌号

    @BindView(id = R.id.update_spouse_info_income_from_tv, widgetName = "update_spouse_info_income_from_tv")
    private TextView update_spouse_info_income_from_tv;                   //主要收入来源

    @BindView(id = R.id.update_spouse_info_from_income_year_edt, widgetName = "update_spouse_info_from_income_year_edt")
    private EditText update_spouse_info_from_income_year_edt;             //主要-工资-年收入

    @BindView(id = R.id.update_spouse_info_from_income_company_name_edt, widgetName = "update_spouse_info_from_income_company_name_edt")
    private NoEmptyEditText update_spouse_info_from_income_company_name_edt;     //主要-工资-单位名称

    @BindView(id = R.id.update_spouse_info_from_income_company_address_tv, widgetName = "update_spouse_info_from_income_company_address_tv")
    private TextView update_spouse_info_from_income_company_address_tv;   //主要-工资-单位地址

    @BindView(id = R.id.update_spouse_info_from_income_company_address1_tv, widgetName = "update_spouse_info_from_income_company_address1_tv")
    private TextView update_spouse_info_from_income_company_address1_tv;  //主要-工资-详细地址

    @BindView(id = R.id.update_spouse_info_from_income_company_address2_tv, widgetName = "update_spouse_info_from_income_company_address2_tv")
    private NoEmptyEditText update_spouse_info_from_income_company_address2_tv;  //主要-工资-门牌号

    @BindView(id = R.id.update_spouse_info_work_position_tv, widgetName = "update_spouse_info_work_position_tv")
    private TextView update_spouse_info_work_position_tv;                 //主要-工资-职务

    @BindView(id = R.id.update_spouse_info_from_income_work_phone_num_edt, widgetName = "update_spouse_info_from_income_work_phone_num_edt")
    private NoEmptyEditText update_spouse_info_from_income_work_phone_num_edt;   //主要-工资-单位座机

    @BindView(id = R.id.update_spouse_info_from_self_year_edt, widgetName = "update_spouse_info_from_self_year_edt")
    private EditText update_spouse_info_from_self_year_edt;               //主要-自营-年收入

    @BindView(id = R.id.update_spouse_info_from_self_type_tv, widgetName = "update_spouse_info_from_self_type_tv")
    private TextView update_spouse_info_from_self_type_tv;                //主要-自营-业务类型

    @BindView(id = R.id.update_spouse_info_from_self_company_name_edt, widgetName = "update_spouse_info_from_self_company_name_edt")
    private NoEmptyEditText update_spouse_info_from_self_company_name_edt;       //主要-自营-店铺名称

    @BindView(id = R.id.update_spouse_info_from_self_company_address_tv, widgetName = "update_spouse_info_from_self_company_address_tv")
    private TextView update_spouse_info_from_self_company_address_tv;     //主要-自营-单位地址

    @BindView(id = R.id.update_spouse_info_from_self_company_address1_tv, widgetName = "update_spouse_info_from_self_company_address1_tv")
    private TextView update_spouse_info_from_self_company_address1_tv;    //主要-自营-详细地址

    @BindView(id = R.id.update_spouse_info_from_self_company_address2_tv, widgetName = "update_spouse_info_from_self_company_address2_tv")
    private NoEmptyEditText update_spouse_info_from_self_company_address2_tv;    //主要-自营-门牌号

    @BindView(id = R.id.update_spouse_info_from_other_year_edt, widgetName = "update_spouse_info_from_other_year_edt")
    private EditText update_spouse_info_from_other_year_edt;              //主要-其他-年收入

    @BindView(id = R.id.update_spouse_info_from_other_remark_tv, widgetName = "update_spouse_info_from_other_remark_tv")
    private NoEmptyEditText update_spouse_info_from_other_remark_tv;             //主要-其他-备注

    @BindView(id = R.id.update_spouse_info_extra_income_from_tv, widgetName = "update_spouse_info_extra_income_from_tv")
    private TextView update_spouse_info_extra_income_from_tv;             //额外收入来源

    @BindView(id = R.id.update_spouse_info_extra_from_income_year_edt, widgetName = "update_spouse_info_extra_from_income_year_edt")
    private EditText update_spouse_info_extra_from_income_year_edt;            //额外-工资-年收入

    @BindView(id = R.id.update_spouse_info_extra_from_income_company_name_edt, widgetName = "update_spouse_info_extra_from_income_company_name_edt")
    private NoEmptyEditText update_spouse_info_extra_from_income_company_name_edt;    //额外-工资-单位名称

    @BindView(id = R.id.update_spouse_info_extra_from_income_company_address_tv, widgetName = "update_spouse_info_extra_from_income_company_address_tv")
    private TextView update_spouse_info_extra_from_income_company_address_tv;  //额外-工资-公司地址

    @BindView(id = R.id.update_spouse_info_extra_from_income_company_address1_tv, widgetName = "update_spouse_info_extra_from_income_company_address1_tv")
    private TextView update_spouse_info_extra_from_income_company_address1_tv; //额外-工资-详细地址

    @BindView(id = R.id.update_spouse_info_extra_from_income_company_address2_tv, widgetName = "update_spouse_info_extra_from_income_company_address2_tv")
    private NoEmptyEditText update_spouse_info_extra_from_income_company_address2_tv; //额外-工资-门牌号

    @BindView(id = R.id.spouse_extra_info_work_position_tv, widgetName = "update_spouse_info_extra_from_income_work_position_tv")
    private TextView update_spouse_info_extra_from_income_work_position_tv;

    @BindView(id = R.id.update_spouse_info_extra_from_income_work_phone_num_edt, widgetName = "update_spousespouse_info_extra_from_income_work_phone_num_edt")
    private NoEmptyEditText update_spouse_info_extra_from_income_work_phone_num_edt;  //额外-工资-单位座机

    @BindView(id = R.id.update_spouse_info_house_type_tv, widgetName = "update_spouse_info_house_type_tv")
    private TextView update_spouse_info_house_type_tv;                     //房屋性质

    @BindView(id = R.id.update_spouse_info_house_area_edt, widgetName = "update_spouse_info_house_area_edt")
    private NoEmptyEditText update_spouse_info_house_area_edt;                    //房屋面积

    @BindView(id = R.id.update_spouse_info_house_owner_name_edt, widgetName = "update_spouse_info_house_owner_name_edt")
    private NoEmptyEditText update_spouse_info_house_owner_name_edt;              //房屋所有人

    @BindView(id = R.id.update_spouse_info_house_owner_relation_tv, widgetName = "update_spouse_info_house_owner_relation_tv")
    private TextView update_spouse_info_house_owner_relation_tv;           //与申请人关系

    @BindView(id = R.id.update_spouse_info_urg_relation1_tv, widgetName = "update_spouse_info_urg_relation1_tv")
    private TextView update_spouse_info_urg_relation1_tv;           //紧急联系人-与申请人关系1

    @BindView(id = R.id.update_spouse_info_urg_mobile1_edt, widgetName = "update_spouse_info_urg_mobile1_edt")
    private EditText update_spouse_info_urg_mobile1_edt;            //紧急联系人-手机号1

    @BindView(id = R.id.update_spouse_info_urg_contact1_edt, widgetName = "update_spouse_info_urg_contact1_edt")
    private NoEmptyEditText update_spouse_info_urg_contact1_edt;           //紧急联系人-姓名1

    @BindView(id = R.id.update_spouse_info_urg_relation2_tv, widgetName = "update_spouse_info_urg_relation2_tv")
    private TextView update_spouse_info_urg_relation2_tv;           //紧急联系人-与申请人关系2

    @BindView(id = R.id.update_spouse_info_urg_mobile2_edt, widgetName = "update_spouse_info_urg_mobile2_edt")
    private EditText update_spouse_info_urg_mobile2_edt;            //紧急联系人-手机号2

    @BindView(id = R.id.update_spouse_info_urg_contact2_edt, widgetName = "update_spouse_info_urg_contact2_edt")
    private NoEmptyEditText update_spouse_info_urg_contact2_edt;           //紧急联系人-姓名2

    private LinearLayout update_spouse_info_live_with_parent_lin;       //是否与父母同住

    @BindView(id = R.id.update_spouse_info_live_with_parent_tv, widgetName = "update_spouse_info_live_with_parent_tv")
    private TextView update_spouse_info_live_with_parent_tv;//是否与父母同住

    private ImageView update_spouse_info_id_no_img;                       //身份证号旁边的一个按钮（——>DocumentActivity）


    private OcrResp.ShowapiResBodyBean ocrResp = new OcrResp.ShowapiResBodyBean();
    private ArrayList<UploadImgItemBean> divorceImgsList = new ArrayList<UploadImgItemBean>();
    private ArrayList<UploadImgItemBean> resBookList = new ArrayList<UploadImgItemBean>();
    private UploadImgItemBean backImg = new UploadImgItemBean();
    private UploadImgItemBean frontImg = new UploadImgItemBean();

    @BindView(id = R.id.update_spouse_info_child_count_edt, widgetName = "update_spouse_info_child_count_edt")
    private EditText update_spouse_info_child_count_edt;                   //子女数量

    @BindView(id = R.id.update_spouse_info_child_count1_edt, widgetName = "update_spouse_info_child_count1_edt")
    private EditText update_spouse_info_child_count1_edt;                   //子女数量

    @BindView(id = R.id.update_spouse_info_child_count2_edt, widgetName = "update_spouse_info_child_count2_edt")
    private EditText update_spouse_info_child_count2_edt;                   //子女数量

    public ClientInfo clientInfo;
    private File imageFile;
    private OcrResp.ShowapiResBodyBean mOcrResp;

    @BindView(id = R.id.submit_img, widgetName = "submit_img", onClick = "submitMaterial")
    private Button submitBtn;

    private String old_marriage;
    private String now_marriage;
    private String imgsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spouse_info);
        UBT.bind(this);
        initTitleBar(this, "配偶资料").setLeftClickListener(v -> showDoubleCheckForExit());
        initView();

        getInfo();  //获取配偶信息

//        submitBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                    Log.e("testtttttttt","aaaa");
//                    v.clearFocus();
//
//                    submit();
//
//            }
//        });
//        findViewById(R.id.submit_img).setOnClickListener(v -> {
//            submit();   //更新配偶信息
//        });
    }

    private void submitMaterial(View view) {
        submit();
//        submitBtn.setFocusable(true);
//        submitBtn.setFocusableInTouchMode(true);
//        submitBtn.requestFocus();
//        submitBtn.requestFocusFromTouch();
    }

    private void initView() {


        //初始化
        update_spouse_info_clt_nm_edt = (NoEmptyEditText) findViewById(R.id.update_spouse_info_clt_nm_edt);
        update_spouse_info_id_no_edt = (EditText) findViewById(R.id.update_spouse_info_id_no_edt);
        update_spouse_info_id_no_img = (ImageView) findViewById(R.id.update_spouse_info_id_no_img);

        update_spouse_info_gender_tv = (TextView) findViewById(R.id.update_spouse_info_gender_tv);
        update_spouse_info_mobile_edt = (EditText) findViewById(R.id.update_spouse_info_mobile_edt);
        update_spouse_info_income_from_tv = (TextView) findViewById(R.id.update_spouse_info_income_from_tv);
        update_spouse_info_from_income_year_edt = (EditText) findViewById(R.id.update_spouse_info_from_income_year_edt);
        update_spouse_info_from_income_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_spouse_info_from_income_company_name_edt);
        update_spouse_info_from_income_company_address_tv = (TextView) findViewById(R.id.update_spouse_info_from_income_company_address_tv);
        update_spouse_info_from_income_company_address1_tv = (TextView) findViewById(R.id.update_spouse_info_from_income_company_address1_tv);
        update_spouse_info_from_income_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_spouse_info_from_income_company_address2_tv);
        update_spouse_info_work_position_tv = (TextView) findViewById(R.id.update_spouse_info_work_position_tv);
        update_spouse_info_from_income_work_phone_num_edt = (NoEmptyEditText) findViewById(R.id.update_spouse_info_from_income_work_phone_num_edt);
        update_spouse_info_from_self_year_edt = (EditText) findViewById(R.id.update_spouse_info_from_self_year_edt);
        update_spouse_info_from_self_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_spouse_info_from_self_company_name_edt);
        update_spouse_info_from_self_company_address_tv = (TextView) findViewById(R.id.update_spouse_info_from_self_company_address_tv);
        update_spouse_info_from_self_company_address1_tv = (TextView) findViewById(R.id.update_spouse_info_from_self_company_address1_tv);
        update_spouse_info_from_self_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_spouse_info_from_self_company_address2_tv);
        update_spouse_info_from_other_year_edt = (EditText) findViewById(R.id.update_spouse_info_from_other_year_edt);
        update_spouse_info_from_other_remark_tv = (NoEmptyEditText) findViewById(R.id.update_spouse_info_from_other_remark_tv);
        update_spouse_info_extra_income_from_tv = (TextView) findViewById(R.id.update_spouse_info_extra_income_from_tv);
        update_spouse_info_extra_from_income_year_edt = (EditText) findViewById(R.id.update_spouse_info_extra_from_income_year_edt);
        update_spouse_info_extra_from_income_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_spouse_info_extra_from_income_company_name_edt);
        update_spouse_info_extra_from_income_company_address_tv = (TextView) findViewById(R.id.update_spouse_info_extra_from_income_company_address_tv);
        update_spouse_info_extra_from_income_company_address1_tv = (TextView) findViewById(R.id.update_spouse_info_extra_from_income_company_address1_tv);
        update_spouse_info_extra_from_income_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_spouse_info_extra_from_income_company_address2_tv);
        update_spouse_info_extra_from_income_work_phone_num_edt = (NoEmptyEditText) findViewById(R.id.update_spouse_info_extra_from_income_work_phone_num_edt);
        update_spouse_info_id_front_tv = (TextView) findViewById(R.id.update_spouse_info_id_front_tv);
        update_spouse_info_id_back_tv = (TextView) findViewById(R.id.update_spouse_info_id_back_tv);
//        update_spouse_info_id_front_lin = (LinearLayout) findViewById(R.id.update_spouse_info_id_front_lin);
//        update_spouse_info_id_back_lin = (LinearLayout) findViewById(update_spouse_info_id_back_lin);
        update_spouse_info_child_count_edt = (EditText) findViewById(R.id.update_spouse_info_child_count_edt);
        update_spouse_info_child_count1_edt = (EditText) findViewById(R.id.update_spouse_info_child_count1_edt);
        update_spouse_info_child_count2_edt = (EditText) findViewById(R.id.update_spouse_info_child_count2_edt);

        mScrollView = ((NestedScrollView) findViewById(R.id.scrollView));

        //回到顶部按钮
        findViewById(R.id.fab).setOnClickListener(v -> mScrollView.smoothScrollTo(0, 0));

        update_spouse_info_id_no_img.setOnClickListener(v -> {
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3001);
        });

//        backImg.type = Constants.FileLabelType.ID_BACK;
//        backImg.role = Constants.PersonType.LENDER_SP;
//        String backTitle = "身份证人像面";
        //身份证人像面
//        update_spouse_info_id_back_lin.setOnClickListener(v -> {
//            Intent intent = new Intent(this, DocumentActivity.class);
//            intent.putExtra("type", Constants.FileLabelType.ID_BACK);
//            intent.putExtra("role", Constants.PersonType.LENDER_SP);
//            intent.putExtra("imgUrl", idBackImgUrl);
//            intent.putExtra("imgUrlId", idBackImgId);
////            intent.putExtra("title",backTitle);
////            intent.putExtra("imgBean",backImg);  // s_url,id,type,role
//            intent.putExtra("ocrResp", ocrResp);
//            intent.putExtra("clt_id", clientInfo.spouse.clt_id);
//            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
//        });


//        String frontTitle = "身份证国徽面";
        //身份证国徽面
//        update_spouse_info_id_front_lin.setOnClickListener(v -> {
//            Intent intent = new Intent(this, DocumentActivity.class);
//            intent.putExtra("type", Constants.FileLabelType.ID_FRONT);
//            intent.putExtra("role", Constants.PersonType.LENDER_SP);
//            intent.putExtra("imgUrl", idFrontImgUrl);
//            intent.putExtra("imgUrld", idFrontImgId);
////            intent.putExtra("title",frontTitle);
////            intent.putExtra("imgBean",frontImg);
//            intent.putExtra("clt_id", clientInfo.spouse.clt_id);
//            startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
//        });

//        //法院判判决书
//        update_spouse_info_divorced_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_divorced_lin);
//        update_spouse_info_divorced_lin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, UploadListActivity.class);
//                intent.putExtra("type", Constants.FileLabelType.MARRIAGE_PROOF);
//                intent.putExtra("role", Constants.PersonType.LENDER);
//                intent.putExtra("imgList", divorceImgsList);
//                intent.putExtra("title", "离婚证");
//                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
//            }
//        });

        //选择收入来源
        update_spouse_info_from_income_group_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_income_group_lin);
        update_spouse_info_from_self_group_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_self_group_lin);
        update_spouse_info_from_other_group_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_other_group_lin);
        income_from_lin = (LinearLayout) findViewById(R.id.update_spouse_info_income_from_lin);
        income_from_tv = (TextView) findViewById(R.id.update_spouse_info_income_from_tv);
        income_from_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(incomelist, UPDATE_INCOME_FROME_INDEX,
                income_from_lin,
                income_from_tv,
                "请选择",
                new WheelViewUtil.OnSubmitCallBack() {
                    @Override
                    public void onSubmitCallBack(View clickedView, int selectedIndex) {
                        UPDATE_INCOME_FROME_INDEX = selectedIndex;

                        if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("工资")) {
                            findViewById(R.id.update_spouse_info_from_income_group_lin).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.update_spouse_info_from_income_group_lin).setVisibility(View.GONE);
                        }

                        if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("自营")) {
                            findViewById(R.id.update_spouse_info_from_self_group_lin).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.update_spouse_info_from_self_group_lin).setVisibility(View.GONE);
                        }

                        if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("其他")) {
                            findViewById(R.id.update_spouse_info_from_other_group_lin).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.update_spouse_info_from_other_group_lin).setVisibility(View.GONE);
                        }
                    }
                }));
        update_spouse_info_marriage_group_lin = (LinearLayout) findViewById(R.id.update_spouse_info_marriage_group_lin);
        update_spouse_info_divorced_group_lin = (LinearLayout) findViewById(R.id.update_spouse_info_divorced_group_lin);
        update_spouse_info_die_group_lin = (LinearLayout) findViewById(R.id.update_spouse_info_die_group_lin);
        //选择个人婚姻状态
        update_spouse_info_marriage_lin = (LinearLayout) findViewById(R.id.update_spouse_info_marriage_lin);
        update_spouse_info_marriage_tv = (TextView) findViewById(R.id.update_spouse_info_marriage_tv);
        update_spouse_info_marriage_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().marriage_key,
                UPDATE_MARRIAGE_INDEX,
                update_spouse_info_marriage_lin,
                update_spouse_info_marriage_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_MARRIAGE_INDEX = selectedIndex;
                    clientInfo.marriage = ((YusionApp) getApplication()).getConfigResp().marriage_key.get(UPDATE_MARRIAGE_INDEX);
                    if (((YusionApp) getApplication()).getConfigResp().marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("已婚")) {
                        findViewById(R.id.fab).setVisibility(View.VISIBLE);
                        update_spouse_info_marriage_group_lin.setVisibility(View.VISIBLE);
                    } else {
                        update_spouse_info_marriage_group_lin.setVisibility(View.GONE);
                    }
                    if (((YusionApp) getApplication()).getConfigResp().marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("离异")) {
                        update_spouse_info_divorced_group_lin.setVisibility(View.VISIBLE);
                        findViewById(R.id.fab).setVisibility(View.GONE);
                    } else {
                        update_spouse_info_divorced_group_lin.setVisibility(View.GONE);
                    }
                    if (((YusionApp) getApplication()).getConfigResp().marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("丧偶")) {
                        update_spouse_info_die_group_lin.setVisibility(View.VISIBLE);
                        findViewById(R.id.fab).setVisibility(View.GONE);
                    } else {
                        update_spouse_info_die_group_lin.setVisibility(View.GONE);
                    }
                }));

        //选择性别
        update_spouse_info_gender_lin = (LinearLayout) findViewById(R.id.update_spouse_info_gender_lin);
        update_spouse_info_gender_tv = (TextView) findViewById(R.id.update_spouse_info_gender_tv);
        update_spouse_info_gender_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().gender_list_key,
                UPDATE_SEX_INDEX,
                update_spouse_info_gender_lin,
                update_spouse_info_gender_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_SEX_INDEX = selectedIndex
        ));

        //工资 公司地址
        update_spouse_info_from_income_company_address_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_income_company_address_lin);
        update_spouse_info_from_income_company_address_tv = (TextView) findViewById(R.id.update_spouse_info_from_income_company_address_tv);
        update_spouse_info_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_from_income_company_address_lin,
                        update_spouse_info_from_income_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_spouse_info_from_income_company_address1_tv.setText("")
                );
            }
        });

        //工资 详细地址 ????
        update_spouse_info_from_income_company_address1_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_income_company_address1_lin);
        update_spouse_info_from_income_company_address1_tv = (TextView) findViewById(R.id.update_spouse_info_from_income_company_address1_tv);
        update_spouse_info_from_income_company_address1_lin.setOnClickListener(v -> {
            if (update_spouse_info_from_income_company_address_tv != null) {
                update_spouse_info_from_income_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_from_income_company_address1_lin.getId();
                requestPOI(update_spouse_info_from_income_company_address_tv.getText().toString());
            } else {
                update_spouse_info_from_income_company_address1_tv.setEnabled(false);
            }
        });

        // 工资 选择职务
        update_spouse_info_from_income_work_position_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_income_work_position_lin);
        update_spouse_info_work_position_tv = (TextView) findViewById(R.id.update_spouse_info_work_position_tv);
        update_spouse_info_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().work_position_key,
                UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                update_spouse_info_from_income_work_position_lin,
                update_spouse_info_work_position_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_FROM_INCOME_WORK_POSITION_INDEX = selectedIndex));

        //自营 业务类型
        update_spouse_info_from_self_type_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_self_type_lin);
        update_spouse_info_from_self_type_tv = (TextView) findViewById(R.id.update_spouse_info_from_self_type_tv);
        update_spouse_info_from_self_type_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().busi_type_list_key,
                UPDATE_FROM_SELF_TYPE_INDEX,
                update_spouse_info_from_self_type_lin,
                update_spouse_info_from_self_type_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_FROM_SELF_TYPE_INDEX = selectedIndex;
                    if (((YusionApp) getApplication()).getConfigResp().busi_type_list_value.get(UPDATE_FROM_SELF_TYPE_INDEX).equals("其他")) {
                        EditText editText = new EditText(this);
                        new AlertDialog.Builder(this)
                                .setTitle("请输入业务类型")
                                .setView(editText)
                                .setCancelable(false)
                                .setPositiveButton("确定", (dialog, which) -> {
                                    update_spouse_info_from_self_type_tv.setText(editText.getText());
                                    UPDATE_FROM_SELF_TYPE_INDEX = 0;
                                    InputMethodUtil.hideInputMethod(this);
                                    dialog.dismiss();
                                })
                                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
                    }
                }
        ));

        //自营 单位地址
        update_spouse_info_from_self_company_address_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_self_company_address_lin);
        update_spouse_info_from_self_company_address_tv = (TextView) findViewById(R.id.update_spouse_info_from_self_company_address_tv);
        update_spouse_info_from_self_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_from_self_company_address_lin,
                        update_spouse_info_from_self_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_spouse_info_from_self_company_address1_tv.setText("")
                );
            }
        });

        //自营 详细地址 ????
        update_spouse_info_from_self_company_address1_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_self_company_address1_lin);
        update_spouse_info_from_self_company_address1_tv = (TextView) findViewById(R.id.update_spouse_info_from_self_company_address1_tv);
        update_spouse_info_from_self_company_address1_lin.setOnClickListener(v -> {
            if (update_spouse_info_from_self_company_address_tv != null) {
                update_spouse_info_from_self_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_from_self_company_address1_lin.getId();
                requestPOI(update_spouse_info_from_self_company_address_tv.getText().toString());
            } else {
                update_spouse_info_from_self_company_address1_tv.setEnabled(false);

            }
        });


        //额外 公司地址
        update_spouse_info_extra_from_income_company_address_lin = (LinearLayout) findViewById(R.id.update_spouse_info_extra_from_income_company_address_lin);
        update_spouse_info_extra_from_income_company_address_tv = (TextView) findViewById(R.id.update_spouse_info_extra_from_income_company_address_tv);
        update_spouse_info_extra_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_extra_from_income_company_address_lin,
                        update_spouse_info_extra_from_income_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_spouse_info_extra_from_income_company_address1_tv.setText("")
                );
            }
        });

        //选择额外收入来源
        update_spouse_info_extra_from_income_group_lin = (LinearLayout) findViewById(R.id.update_spouse_info_extra_from_income_group_lin);
        income_extra_from_lin = (LinearLayout) findViewById(R.id.update_spouse_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) findViewById(R.id.update_spouse_info_extra_income_from_tv);
        income_extra_from_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(incomeextarlist,
                UPDATE_EXTRA_INCOME_FROME_INDEX,
                income_extra_from_lin,
                income_extra_from_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_EXTRA_INCOME_FROME_INDEX = selectedIndex;
                    if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("工资")) {
                        findViewById(R.id.update_spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
                    }
                    if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("无")) {
                        findViewById(R.id.update_spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
                    }
                }
        ));

        //法院判决书
//        LinearLayout update_spouse_info_divorced_lin = (LinearLayout) findViewById(R.id.update_spouse_info_divorced_lin);
//        update_spouse_info_divorced_tv = (TextView) findViewById(R.id.update_spouse_info_divorced_tv);
//        update_spouse_info_divorced_lin.setOnClickListener(v -> {
//            Intent intent = new Intent(this, UploadListActivity.class);
//            intent.putExtra("type", Constants.FileLabelType.MARRIAGE_PROOF);
//            intent.putExtra("role", Constants.PersonType.LENDER);
//            intent.putExtra("imgList", divorceImgsList);
//            intent.putExtra("title", "离婚证");
//            intent.putExtra("clt_id", clientInfo.clt_id);
//            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
//        });

        //户口本
//        update_spouse_info_register_addr_lin = (LinearLayout) findViewById(R.id.update_spouse_info_register_addr_lin);
//        update_spouse_info_register_addr_tv = (TextView) findViewById(R.id.update_spouse_info_register_addr_tv);
//        update_spouse_info_register_addr_lin.setOnClickListener(v -> {
//            Intent intent = new Intent(this, UploadListActivity.class);
//            intent.putExtra("type", Constants.FileLabelType.RES_BOOKLET);
//            intent.putExtra("role", Constants.PersonType.LENDER);
//            intent.putExtra("imgList", resBookList);
//            intent.putExtra("clt_id", clientInfo.clt_id);
//            intent.putExtra("title", "户口本");
//            intent.putExtra("clt_id", clientInfo.clt_id);
//            startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
//        });
        //额外 详细地址 ????
        update_spouse_info_extra_from_income_company_address1_lin = (LinearLayout) findViewById(R.id.update_spouse_info_extra_from_income_company_address1_lin);
        update_spouse_info_extra_from_income_company_address1_tv = (TextView) findViewById(R.id.update_spouse_info_extra_from_income_company_address1_tv);
        update_spouse_info_extra_from_income_company_address1_lin.setOnClickListener(v -> {
            if (update_spouse_info_extra_from_income_company_address_tv != null) {
                update_spouse_info_extra_from_income_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_extra_from_income_company_address1_lin.getId();
                requestPOI(update_spouse_info_extra_from_income_company_address_tv.getText().toString());
            } else {
                update_spouse_info_extra_from_income_company_address1_tv.setEnabled(false);
            }

        });
        // 额外 选择职务
        update_spouse_info_extra_from_income_work_position_lin = (LinearLayout) findViewById(R.id.update_spouse_info_extra_from_income_work_position_lin);
        update_spouse_info_extra_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().work_position_key,
                UPDATE_FROM_EXTRA_WORK_POSITION_INDEX,
                update_spouse_info_extra_from_income_work_position_lin,
                update_spouse_info_extra_from_income_work_position_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex));

        update_spouse_info_mobile_img = (ImageView) findViewById(R.id.update_spouse_info_mobile_img);
        update_spouse_info_mobile_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = update_spouse_info_mobile_img.getId();
            selectContact();
        });

        update_spouse_info_reg_lin = (LinearLayout) findViewById(R.id.update_spouse_info_reg_lin);
        update_spouse_info_education_lin = (LinearLayout) findViewById(R.id.update_spouse_info_education_lin);
        update_spouse_info_current_address_lin = (LinearLayout) findViewById(R.id.update_spouse_info_current_address_lin);
        update_spouse_info_current_address1_lin = (LinearLayout) findViewById(R.id.update_spouse_info_current_address1_lin);
        update_spouse_info_house_type_lin = (LinearLayout) findViewById(R.id.update_spouse_info_house_type_lin);
        update_spouse_info_house_owner_relation_lin = (LinearLayout) findViewById(R.id.update_spouse_info_house_owner_relation_lin);
        update_spouse_info_urg_relation1_lin = (LinearLayout) findViewById(R.id.update_spouse_info_urg_relation1_lin);
        update_spouse_info_urg_relation2_lin = (LinearLayout) findViewById(R.id.update_spouse_info_urg_relation2_lin);
        update_spouse_info_urg_mobile1_img = (ImageView) findViewById(R.id.update_spouse_info_urg_mobile1_img);
        update_spouse_info_urg_mobile2_img = (ImageView) findViewById(R.id.update_spouse_info_urg_mobile2_img);
        update_spouse_info_live_with_parent_lin = (LinearLayout) findViewById(R.id.update_spouse_info_live_with_parent_lin);

        //选择户籍地
        update_spouse_info_reg_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_reg_lin,
                        update_spouse_info_reg_tv,
                        "请选择所在地区",
                        (clickedView, city) -> {

                        }
                );
            }
        });
        //选择学历
        update_spouse_info_education_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().education_list_key,
                UPDATE_EDUCATION_INDEX,
                update_spouse_info_education_lin,
                update_spouse_info_education_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_EDUCATION_INDEX = selectedIndex));

        //现在地址
        update_spouse_info_current_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_current_address_lin,
                        update_spouse_info_current_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_spouse_info_current_address1_tv.setText("")
                );
            }
        });

        //详细地址
        update_spouse_info_current_address1_lin.setOnClickListener(v -> {
            if (!update_spouse_info_current_address_tv.getText().toString().isEmpty()) {
                update_spouse_info_current_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_current_address1_lin.getId();
                requestPOI(update_spouse_info_current_address_tv.getText().toString());
            } else {
                update_spouse_info_current_address1_tv.setEnabled(false);
            }
        });

        //房屋类型
        update_spouse_info_house_type_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().house_type_list_key,
                UPDATE_HOUSE_TYPE_INDEX,
                update_spouse_info_house_type_lin,
                update_spouse_info_house_type_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_HOUSE_TYPE_INDEX = selectedIndex));

        //与申请人关系
        update_spouse_info_house_owner_relation_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().house_relationship_list_key,
                UPDATE_HOUSE_OWNER_RELATION_INDEX,
                update_spouse_info_house_owner_relation_lin,
                update_spouse_info_house_owner_relation_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_HOUSE_OWNER_RELATION_INDEX = selectedIndex));
        update_spouse_info_urg_relation2_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().urg_other_relationship_list_key,
                UPDATE_URG_RELATION_INDEX2,
                update_spouse_info_urg_relation2_lin,
                update_spouse_info_urg_relation2_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_URG_RELATION_INDEX2 = selectedIndex));
        update_spouse_info_urg_relation1_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(((YusionApp) getApplication()).getConfigResp().urg_rela_relationship_list_key,
                UPDATE_URG_RELATION_INDEX1,
                update_spouse_info_urg_relation1_lin,
                update_spouse_info_urg_relation1_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_URG_RELATION_INDEX1 = selectedIndex));
        //紧急联系人
        update_spouse_info_urg_mobile1_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = update_spouse_info_urg_mobile1_img.getId();
            selectContact();
        });
        update_spouse_info_urg_mobile2_img.setOnClickListener(v -> {
            CURRENT_CLICKED_VIEW_FOR_CONTACT = update_spouse_info_urg_mobile2_img.getId();
            selectContact();
        });

        //是否与父母同住
        update_spouse_info_live_with_parent_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(ifwithparentlist,
                UPDATE_LIVE_WITH_PARENT_INDEX,
                update_spouse_info_live_with_parent_lin,
                update_spouse_info_live_with_parent_tv,
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
                if (update_spouse_info_mobile_img.getId() == CURRENT_CLICKED_VIEW_FOR_CONTACT) {
                    update_spouse_info_mobile_edt.setText(result[1].replaceAll(" ", ""));
                    UBT.addEvent(this, "text_change", "edit_text", "update_spouse_info_mobile_edt", UpdateSpouseInfoActivity.class.getSimpleName(), "手机号");
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_spouse_info_urg_mobile1_img.getId()) {
                    update_spouse_info_urg_contact1_edt.setText(result[0]);
                    update_spouse_info_urg_mobile1_edt.setText(result[1].replaceAll(" ", ""));
                    UBT.addEvent(this, "text_change", "edit_text", "update_spouse_info_urg_mobile1_edt", UpdateSpouseInfoActivity.class.getSimpleName(), "手机号");
                    UBT.addEvent(this, "text_change", "edit_text", "update_spouse_info_urg_contact1_edt", UpdateSpouseInfoActivity.class.getSimpleName(), "联系人姓名");
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_spouse_info_urg_mobile2_img.getId()) {
                    update_spouse_info_urg_contact2_edt.setText(result[0]);
                    update_spouse_info_urg_mobile2_edt.setText(result[1].replaceAll(" ", ""));
                    UBT.addEvent(this, "text_change", "edit_text", "update_spouse_info_urg_mobile2_edt", UpdateSpouseInfoActivity.class.getSimpleName(), "手机号");
                    UBT.addEvent(this, "text_change", "edit_text", "update_spouse_info_urg_contact2_edt", UpdateSpouseInfoActivity.class.getSimpleName(), "联系人姓名");
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_spouse_info_current_address1_lin.getId()) {
                    update_spouse_info_current_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_spouse_info_from_income_company_address1_lin.getId()) {
                    update_spouse_info_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_spouse_info_from_self_company_address1_lin.getId()) {
                    update_spouse_info_from_self_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_spouse_info_extra_from_income_company_address1_lin.getId()) {
                    update_spouse_info_extra_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == 3001) {
            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            OcrUtil.requestOcr(this, imageFile.getAbsolutePath(), new OSSObjectKeyBean("lender_sp", "id_card_back", ".png"), "id_card", (ocrResp1, objectKey) -> {
                if (ocrResp1 == null) {
                    Toast.makeText(UpdateSpouseInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else if (ocrResp1.showapi_res_code != 0 && TextUtils.isEmpty(ocrResp1.showapi_res_body.idNo) || TextUtils.isEmpty(ocrResp1.showapi_res_body.name)) {
                    Toast.makeText(UpdateSpouseInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(UpdateSpouseInfoActivity.this, "识别成功", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    mOcrResp = ocrResp1.showapi_res_body;
                    if (mOcrResp != null) {
                        if (!TextUtils.isEmpty(mOcrResp.idNo)) {
                            update_spouse_info_id_no_edt.setText(mOcrResp.idNo);
                        }
                        if (!TextUtils.isEmpty(mOcrResp.name)) {
                            update_spouse_info_clt_nm_edt.setText(mOcrResp.name);
                        }
                        if (!TextUtils.isEmpty(mOcrResp.sex)) {
                            update_spouse_info_gender_tv.setText(mOcrResp.sex);
                        }
                    }
                }
            }, (throwable, s) ->
                    Toast.makeText(UpdateSpouseInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show());
        }

//            else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
//                switch (contact.getStringExtra("type")) {
//                    case Constants.FileLabelType.RES_BOOKLET:
//                        resBookList = (ArrayList<UploadImgItemBean>) contact.getSerializableExtra("imgList");
//                        if (resBookList.size() > 0) {
//                            update_spouse_info_register_addr_tv.setText("已上传");
//                            update_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.system_color));
//                        } else {
//                            update_spouse_info_register_addr_tv.setText("请上传");
//                            update_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
//                        }
//                        break;
//                    case Constants.FileLabelType.MARRIAGE_PROOF:
//                        divorceImgsList = (ArrayList<UploadImgItemBean>) contact.getSerializableExtra("imgList");
//                        if (divorceImgsList.size() > 0) {
//                            update_spouse_info_divorced_tv.setText("已上传");
//                            update_spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.system_color));
//                        } else {
//                            update_spouse_info_divorced_tv.setText("请上传");
//                            update_spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
//                        }
//                }
//            }
//


//            else if (requestCode == Constants.REQUEST_DOCUMENT) {
                /*

                backImg = (UploadImgItemBean) contact.getSerializableExtra("backImg");
                switch (backImg.type){
                    case Constants.FileLabelType.ID_BACK:
                        if (contact.getBooleanExtra("ishaveImg",false)){
                            update_spouse_info_id_back_tv.setText("已上传");
                            update_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
                            ocrResp = (OcrResp.ShowapiResBodyBean) contact.getSerializableExtra("ocrResp");
                        }else{
                             update_spouse_info_id_back_tv.setText("请上传");
                            update_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        if(ocResp != null){
                            if (!TextUtils.isEmpty(ocrResp.idNo)) {
                                update_spouse_info_id_no_edt.setText(ocrResp.idNo);
                            }
                            if (!TextUtils.isEmpty(ocrResp.name)) {
                                update_spouse_info_clt_nm_edt.setText(ocrResp.name);
                            }
                            if (!TextUtils.isEmpty(ocrResp.sex)) {
                                update_spouse_info_gender_tv.setText(ocrResp.sex);
                            }
                        }
                        break;

                   case Constants.FileLabelType.ID_FRONT:
                         if (contact.getBooleanExtra("ishaveImg",false)){
                            update_spouse_info_id_front_tv.setText("已上传");
                            update_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            update_spouse_info_id_front_tv.setText("请上传");
                            update_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        break;
                }
*/

//                switch (contact.getStringExtra("type")) {
//                    case Constants.FileLabelType.ID_BACK:
//                        ID_BACK_FID = contact.getStringExtra("objectKey");
//                        idBackImgUrl = contact.getStringExtra("imgUrl");
////                        if (!idBackImgUrl.isEmpty()) {
////                            update_spouse_info_id_back_tv.setText("已上传");
////                            update_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
////                            ocrResp = (OcrResp.ShowapiResBodyBean) contact.getSerializableExtra("ocrResp");
////                        } else {
////                            update_spouse_info_id_back_tv.setText("请上传");
////                            update_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
////                        }
//                        if (ocrResp != null) {
//                            if (!TextUtils.isEmpty(ocrResp.idNo)) {
//                                update_spouse_info_id_no_edt.setText(ocrResp.idNo);
//                            }
//                            if (!TextUtils.isEmpty(ocrResp.name)) {
//                                update_spouse_info_clt_nm_edt.setText(ocrResp.name);
//                            }
//                            if (!TextUtils.isEmpty(ocrResp.sex)) {
//                                update_spouse_info_gender_tv.setText(ocrResp.sex);
//                            }
//                        }
//
//                        break;
////                    case Constants.FileLabelType.ID_FRONT:
////                        ID_FRONT_FID = contact.getStringExtra("objectKey");
////                        idFrontImgUrl = contact.getStringExtra("imgUrl");
////                        if (!idFrontImgUrl.isEmpty()) {
////                            update_spouse_info_id_front_tv.setText("已上传");
////                            update_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
////                        } else {
////                            update_spouse_info_id_front_tv.setText("请上传");
////                            update_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
////                        }
////                        break;
//                }
//            }


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
//                if (clientInfo.marriage.equals("已婚")) {
//                    mUpdateImgsLabelFragment.setVisibility(View.VISIBLE);
//                } else {
//                    mUpdateImgsLabelFragment.setVisibility(View.GONE);
//                }
//                mUpdateImgsLabelFragment.setCltIdAndRole(clientInfo.spouse.clt_id, Constants.PersonType.LENDER_SP);
            }
        });
    }


    private void submit() {
        submitBtn.setFocusable(false);
//        mUpdateSpouseInfoFragment.requestUpdate();
        //上传用户资料

        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        clientInfo.spouse.imei = telephonyManager.getDeviceId();
        updateClientinfo(() -> ProductApi.updateClientInfo(UpdateSpouseInfoActivity.this, clientInfo, data -> {
            if (data == null) {
                return;
            }
            UBT.sendAllUBTEvents(this);
            clientInfo = data;
            //已婚状态：上传配偶cltid
//            if (contact != null) {
            if (clientInfo.marriage.equals("已婚")) {
//                requestUpload(clientInfo.spouse.clt_id, () -> {
                //上传影像件
//                    requestUpload(clientInfo.spouse.clt_id, () -> {
//                toCommitActivity(clientInfo.spouse.clt_id, "lender_sp", "个人配偶影像件资料", "continue");
                Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
                intent.putExtra("clt_id", clientInfo.clt_id);
                intent.putExtra("role", "lender_sp");
                intent.putExtra("title", "个人配偶影像件资料");
                intent.putExtra("commit_state", "continue");
                startActivity(intent);
                finish();
//                        Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
//                        startActivity(intent);
//                        finish();
//                    });
//                });
            } else {
                //其他状态：上传主贷人cltid，不上传右侧影像件
//                requestUpload(clientInfo.clt_id, () -> {

                if (!TextUtils.equals(now_marriage, "未婚")) {
                    //状态没变：一个按钮
                    if (TextUtils.equals(old_marriage, now_marriage)) {
//                        toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "return");
                        Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
                        intent.putExtra("clt_id", clientInfo.clt_id);
                        intent.putExtra("role", "lender_sp");
                        intent.putExtra("title", "个人影像件资料");
                        intent.putExtra("commit_state", "return");
                        startActivity(intent);
                        finish();
                    }//状态改变：两个按钮
                    else {
                        if (TextUtils.equals(now_marriage, "丧偶")) {
//                            toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "continue");
                            Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
                            intent.putExtra("clt_id", clientInfo.clt_id);
                            intent.putExtra("role", "lender_sp");
                            intent.putExtra("title", "个人影像件资料");
                            intent.putExtra("commit_state", "continue");
                            startActivity(intent);
                            finish();
                        } else if (TextUtils.equals(now_marriage, "离异")) {
//                            toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "continue");
                            Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
                            intent.putExtra("clt_id", clientInfo.clt_id);
                            intent.putExtra("role", "lender_sp");
                            intent.putExtra("title", "个人影像件资料");
                            intent.putExtra("commit_state", "continue");
                            startActivity(intent);
                            finish();
                        }
                    }
                }//未婚：一个按钮
                else {
//                    toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "return");
                    Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
                    intent.putExtra("clt_id", clientInfo.clt_id);
                    intent.putExtra("role", "lender_sp");
                    intent.putExtra("title", "个人影像件资料");
                    intent.putExtra("commit_state", "return");
                    startActivity(intent);
                    finish();
                }
//                    new AlertDialog.Builder(this)
//                            .setMessage("资料上传成功，请前往影像件界面上传影像件")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();


//                    Toast.makeText(UpdateSpouseInfoActivity.this, "提交成功，离婚证（户口本）请在主贷人的影像件里查看", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(UpdateSpouseInfoActivity.this, CommitActivity.class);
//                    startActivity(intent);
//                    finish();

//                });
            }


        }));
    }


    public void updateClientinfo(OnVoidCallBack callBack) {
        if (checkUserInfo()) {
            //提交
            clientInfo.spouse.marriage = update_spouse_info_marriage_tv.getText().toString();
            clientInfo.marriage = update_spouse_info_marriage_tv.getText().toString();
            now_marriage = clientInfo.marriage;
            switch (update_spouse_info_marriage_tv.getText().toString()) {
                case "未婚":
                    break;
                case "已婚":
                    clientInfo.spouse.clt_nm = update_spouse_info_clt_nm_edt.getText().toString().trim();
                    clientInfo.spouse.id_no = update_spouse_info_id_no_edt.getText().toString().trim();
                    clientInfo.spouse.gender = update_spouse_info_gender_tv.getText().toString().trim();
//                    if (update_spouse_info_reg_tv.getText().toString().trim().split("/").length == 3) {
//                        clientInfo.spouse.reg_addr.province = update_spouse_info_reg_tv.getText().toString().trim().split("/").[0];
//                        clientInfo.spouse.reg_addr.city = update_spouse_info_reg_tv.getText().toString().trim().split("/")[1];
//                        clientInfo.spouse.reg_addr.district = update_spouse_info_reg_tv.getText().toString().trim().split("/")[2];
//                    }
//                    clientInfo.spouse.edu = update_spouse_info_education_tv.getText().toString().trim();
//                    if (update_spouse_info_current_address_tv.getText().toString().trim().split("/").length == 3) {
//                        clientInfo.spouse.current_addr.province = update_spouse_info_current_address_tv.getText().toString().trim().split("/")[0];
//                        clientInfo.spouse.current_addr.city = update_spouse_info_current_address_tv.getText().toString().trim().split("/")[1];
//                        clientInfo.spouse.current_addr.district = update_spouse_info_current_address_tv.getText().toString().trim().split("/")[2];
//                        clientInfo.spouse.current_addr.address1 = update_spouse_info_current_address1_tv.getText().toString().trim();
//                        clientInfo.spouse.current_addr.address2 = update_spouse_info_current_address2_tv.getText().toString().trim();
//                    }
//                        clientInfo.spouse.is_live_with_parent = update_spouse_info_live_with_parent_tv.getText().toString().trim();
                    clientInfo.child_num = update_spouse_info_child_count_edt.getText().toString().trim();
                    clientInfo.spouse.mobile = update_spouse_info_mobile_edt.getText().toString().trim();
                    clientInfo.spouse.major_income_type = update_spouse_info_income_from_tv.getText().toString().trim();
                    //判断主要收入类型
                    switch (update_spouse_info_income_from_tv.getText().toString().trim()) {
                        case "工资":
                            clientInfo.spouse.major_income = update_spouse_info_from_income_year_edt.getText().toString().trim();
                            clientInfo.spouse.major_company_name = update_spouse_info_from_income_company_name_edt.getText().toString().trim();
                            if (update_spouse_info_from_income_company_address_tv.getText().toString().split("/").length == 3) {
                                clientInfo.spouse.major_company_addr.province = update_spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                                clientInfo.spouse.major_company_addr.city = update_spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                                clientInfo.spouse.major_company_addr.district = update_spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                                clientInfo.spouse.major_company_addr.address1 = update_spouse_info_from_income_company_address1_tv.getText().toString().trim();
                                clientInfo.spouse.major_company_addr.address2 = update_spouse_info_from_income_company_address2_tv.getText().toString().trim();
                            }
                            clientInfo.spouse.major_work_position = update_spouse_info_work_position_tv.getText().toString().trim();
                            clientInfo.spouse.major_work_phone_num = update_spouse_info_from_income_work_phone_num_edt.getText().toString().trim();
                            break;
                        case "自营":
                            clientInfo.spouse.major_income = update_spouse_info_from_self_year_edt.getText().toString().trim();
                            clientInfo.spouse.major_busi_type = update_spouse_info_from_self_type_tv.getText().toString().trim();
                            clientInfo.spouse.major_company_name = update_spouse_info_from_self_company_name_edt.getText().toString().trim();
                            if (TextUtils.isEmpty(update_spouse_info_from_self_company_address_tv.getText())) {
                                clientInfo.spouse.major_company_addr.province = "";
                                clientInfo.spouse.major_company_addr.city = "";
                                clientInfo.spouse.major_company_addr.district = "";
                            } else {
                                clientInfo.spouse.major_company_addr.province = update_spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[0];
                                clientInfo.spouse.major_company_addr.city = update_spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[1];
                                clientInfo.spouse.major_company_addr.district = update_spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[2];
                            }
                            clientInfo.spouse.major_company_addr.address1 = update_spouse_info_from_self_company_address1_tv.getText().toString().trim();
                            clientInfo.spouse.major_company_addr.address2 = update_spouse_info_from_self_company_address2_tv.getText().toString().trim();
                            break;
                        case "其他":
                            clientInfo.spouse.major_income = update_spouse_info_from_other_year_edt.getText().toString().trim();
                            clientInfo.spouse.major_remark = update_spouse_info_from_other_remark_tv.getText().toString().trim();
                            break;
                        default:
                            break;
                    }
                    clientInfo.spouse.extra_income_type = update_spouse_info_extra_income_from_tv.getText().toString().trim();
                    //判断额外收入类型
                    switch (update_spouse_info_extra_income_from_tv.getText().toString().trim()) {
                        case "工资":
                            clientInfo.spouse.extra_income = update_spouse_info_extra_from_income_year_edt.getText().toString().trim();
                            clientInfo.spouse.extra_company_name = update_spouse_info_extra_from_income_company_name_edt.getText().toString().trim();
                            if (update_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/").length == 3) {
                                clientInfo.spouse.extra_company_addr.province = update_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                                clientInfo.spouse.extra_company_addr.city = update_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                                clientInfo.spouse.extra_company_addr.district = update_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                                clientInfo.spouse.extra_company_addr.address1 = update_spouse_info_extra_from_income_company_address1_tv.getText().toString().trim();
                                clientInfo.spouse.extra_company_addr.address2 = update_spouse_info_extra_from_income_company_address2_tv.getText().toString().trim();
                            }
                            clientInfo.spouse.extra_work_position = update_spouse_info_extra_from_income_work_position_tv.getText().toString().trim();
                            clientInfo.spouse.extra_work_phone_num = update_spouse_info_extra_from_income_work_phone_num_edt.getText().toString().trim();
                            break;
                        case "无":
                            break;
                        default:
                            break;
                    }
//                    clientInfo.spouse.house_type = update_spouse_info_house_type_tv.getText().toString().trim();
//                    clientInfo.spouse.house_area = update_spouse_info_house_area_edt.getText().toString().trim();
//                    clientInfo.spouse.house_owner_name = update_spouse_info_house_owner_name_edt.getText().toString().trim();
//                    clientInfo.spouse.house_owner_relation = update_spouse_info_house_owner_relation_tv.getText().toString().trim();
//                    clientInfo.spouse.urg_relation1 = update_spouse_info_urg_relation1_tv.getText().toString().trim();
//                    clientInfo.spouse.urg_mobile1 = update_spouse_info_urg_mobile1_edt.getText().toString().trim();
//                    clientInfo.spouse.urg_contact1 = update_spouse_info_urg_contact1_edt.getText().toString().trim();
//                    clientInfo.spouse.urg_relation2 = update_spouse_info_urg_relation2_tv.getText().toString().trim();
//                    clientInfo.spouse.urg_mobile2 = update_spouse_info_urg_mobile2_edt.getText().toString().trim();
//                    clientInfo.spouse.urg_contact2 = update_spouse_info_urg_contact2_edt.getText().toString().trim();
                    
                    break;
                case "离异":
                    clientInfo.child_num = update_spouse_info_child_count1_edt.getText().toString().trim();
                    break;
                case "丧偶":
                    clientInfo.child_num = update_spouse_info_child_count2_edt.getText().toString().trim();
                    break;
                default:
                    break;
            }

            callBack.callBack();
//            updateimgUrl(callBack);
        }
    }

    private boolean checkUserInfo() {
        if (update_spouse_info_marriage_tv.getText().toString().equals("已婚")) {
            if (TextUtils.isEmpty(update_spouse_info_clt_nm_edt.getText().toString())) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(update_spouse_info_id_no_edt.getText().toString())) {
                Toast.makeText(this, "身份证号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(update_spouse_info_id_no_edt.getText().toString())) {
                Toast.makeText(this, "身份证号有误", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(update_spouse_info_gender_tv.getText().toString())) {
                Toast.makeText(this, "性别不能为空", Toast.LENGTH_SHORT).show();
            }
//            else if (update_spouse_info_reg_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "户籍地不能为空", Toast.LENGTH_SHORT).show();
//            }
            else if (TextUtils.isEmpty(update_spouse_info_mobile_edt.getText().toString())) {
                Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(update_spouse_info_mobile_edt.getText().toString())) {
                Toast.makeText(this, "手机号码有误", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(update_spouse_info_child_count_edt.getText().toString())) {
                Toast.makeText(this, "子女数量不能为空", Toast.LENGTH_SHORT).show();
            }
//            else if (update_spouse_info_education_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "学历不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_current_address_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "现住地址不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_current_address1_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_current_address2_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_live_with_parent_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "是否与父母同住不能为空", Toast.LENGTH_SHORT).show();
//            }
            else if (update_spouse_info_income_from_tv.getText().toString().equals("")) {
                Toast.makeText(this, "收入来源不能为空", Toast.LENGTH_SHORT).show();
            }//主要工资
            else if (update_spouse_info_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_from_income_year_edt.getText().toString())) {
                Toast.makeText(this, "工资年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_from_income_company_name_edt.getText().toString())) {
                Toast.makeText(this, "单位名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_from_income_company_address_tv.getText().toString())) {
                Toast.makeText(this, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_from_income_company_address1_tv.getText().toString())) {
                Toast.makeText(this, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_from_income_company_address2_tv.getText().toString())) {
                Toast.makeText(this, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_work_position_tv.getText().toString())) {
                Toast.makeText(this, "职务不能为空", Toast.LENGTH_SHORT).show();
            } //主要自营
            else if (update_spouse_info_income_from_tv.getText().toString().equals("自营") && TextUtils.isEmpty(update_spouse_info_from_self_year_edt.getText().toString())) {
                Toast.makeText(this, "自营年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("自营") && TextUtils.isEmpty(update_spouse_info_from_self_type_tv.getText().toString())) {
                Toast.makeText(this, "业务类型不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("自营") && TextUtils.isEmpty(update_spouse_info_from_self_company_address_tv.getText().toString())) {
                Toast.makeText(this, "项目经营地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("自营") && TextUtils.isEmpty(update_spouse_info_from_self_company_address1_tv.getText().toString())) {
                Toast.makeText(this, "自营的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("自营") && TextUtils.isEmpty(update_spouse_info_from_self_company_address2_tv.getText().toString())) {
                Toast.makeText(this, "自营的门牌号不能为空", Toast.LENGTH_SHORT).show();
            }//主要其他
            else if (update_spouse_info_income_from_tv.getText().toString().equals("其他") && TextUtils.isEmpty(update_spouse_info_from_other_year_edt.getText().toString())) {
                Toast.makeText(this, "其他年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("其他") && TextUtils.isEmpty(update_spouse_info_from_other_remark_tv.getText().toString())) {
                Toast.makeText(this, "备注不能为空", Toast.LENGTH_SHORT).show();
            }


            //额外工资
            else if (update_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_extra_from_income_year_edt.getText().toString())) {
                Toast.makeText(this, "年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_extra_from_income_company_name_edt.getText().toString())) {
                Toast.makeText(this, "单位名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_extra_from_income_company_address_tv.getText().toString())) {
                Toast.makeText(this, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_extra_from_income_company_address1_tv.getText().toString())) {
                Toast.makeText(this, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_extra_from_income_company_address2_tv.getText().toString())) {
                Toast.makeText(this, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && TextUtils.isEmpty(update_spouse_info_extra_from_income_work_position_tv.getText().toString())) {
                Toast.makeText(this, "职务不能为空", Toast.LENGTH_SHORT).show();
            }
//            else if (update_spouse_info_house_type_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "房屋性质不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_house_area_edt.getText().toString().isEmpty()) {
//                Toast.makeText(this, "房屋面积不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_house_owner_name_edt.getText().toString().isEmpty()) {
//                Toast.makeText(this, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_house_owner_relation_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_urg_relation1_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
//            } else if (TextUtils.isEmpty(update_spouse_info_urg_mobile1_edt.getText().toString())) {
//                Toast.makeText(this, "紧急联系人手机号不能为空", Toast.LENGTH_SHORT).show();
//            } else if (!CheckMobileUtil.checkMobile(update_spouse_info_urg_mobile1_edt.getText().toString())) {
//                Toast.makeText(this, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_urg_contact1_edt.getText().toString().isEmpty()) {
//                Toast.makeText(this, "紧急联系人人姓名不能为空", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_urg_relation2_tv.getText().toString().isEmpty()) {
//                Toast.makeText(this, "紧急联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
//            } else if (TextUtils.isEmpty(update_spouse_info_urg_mobile2_edt.getText().toString())) {
//                Toast.makeText(this, "紧急联系人手机号不能为空", Toast.LENGTH_SHORT).show();
//            } else if (!CheckMobileUtil.checkMobile(update_spouse_info_urg_mobile2_edt.getText().toString())) {
//                Toast.makeText(this, "紧急联系人手机号格式错误", Toast.LENGTH_SHORT).show();
//            } else if (update_spouse_info_urg_contact2_edt.getText().toString().isEmpty()) {
//                Toast.makeText(this, "紧急联系人人姓名不能为空", Toast.LENGTH_SHORT).show();
//            }
            else {
                return true;
            }
        }
        if (update_spouse_info_marriage_tv.getText().toString().equals("未婚")) {
            return true;
        }
        if (update_spouse_info_marriage_tv.getText().toString().equals("离异")) {
            if (TextUtils.isEmpty(update_spouse_info_child_count1_edt.getText().toString())) {
                Toast.makeText(this, "子女数量不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        if (update_spouse_info_marriage_tv.getText().toString().equals("丧偶")) {
            if (TextUtils.isEmpty(update_spouse_info_child_count2_edt.getText().toString())) {
                Toast.makeText(this, "子女数量不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        return false;
    }

    private void uploadUrl(String clt_id, OnVoidCallBack callBack) {
        ArrayList<UploadFilesUrlReq.FileUrlBean> files = new ArrayList<>();
        String marriage = update_spouse_info_marriage_tv.getText().toString();
        switch (marriage) {
            case "离异":
                for (int i = 0; i < divorceImgsList.size(); i++) {
                    UploadImgItemBean divo = divorceImgsList.get(i);
                    //如果没有拍照，就不添加该照片
                    if (TextUtils.isEmpty(divo.objectKey)) {
                        continue;
                    }
                    UploadFilesUrlReq.FileUrlBean divorceFileItem = new UploadFilesUrlReq.FileUrlBean();
                    divorceFileItem.file_id = divo.objectKey;
                    divorceFileItem.label = Constants.FileLabelType.MARRIAGE_PROOF;
                    divorceFileItem.clt_id = clt_id;
                    files.add(divorceFileItem);
                }
                break;
            case "丧偶":
                for (int i = 0; i < resBookList.size(); i++) {
                    UploadImgItemBean resb = resBookList.get(i);
                    if (TextUtils.isEmpty(resb.objectKey)) {
                        continue;
                    }
                    UploadFilesUrlReq.FileUrlBean resBookFileItem = new UploadFilesUrlReq.FileUrlBean();
                    resBookFileItem.file_id = resb.objectKey;
                    resBookFileItem.label = Constants.FileLabelType.RES_BOOKLET;
                    resBookFileItem.clt_id = clt_id;
                    files.add(resBookFileItem);
                }
                break;

            case "已婚":
                if (!TextUtils.isEmpty(ID_BACK_FID)) {
                    UploadFilesUrlReq.FileUrlBean idBackBean = new UploadFilesUrlReq.FileUrlBean();
                    idBackBean.file_id = ID_BACK_FID;
                    idBackBean.label = Constants.FileLabelType.ID_BACK;
                    idBackBean.clt_id = clt_id;
                    files.add(idBackBean);
                }
                if (!TextUtils.isEmpty(ID_FRONT_FID)) {
                    UploadFilesUrlReq.FileUrlBean idFrontBean = new UploadFilesUrlReq.FileUrlBean();
                    idFrontBean.file_id = ID_FRONT_FID;
                    idFrontBean.label = Constants.FileLabelType.ID_FRONT;
                    idFrontBean.clt_id = clt_id;
                    files.add(idFrontBean);
                }
                break;
        }
        UploadFilesUrlReq uploadFilesUrlReq = new UploadFilesUrlReq();
        uploadFilesUrlReq.files = files;
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(this).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(this).getValue("bucket", "");
        if (files.size() > 0) {
            UploadApi.uploadFileUrl(this, uploadFilesUrlReq, (code, msg) -> {
                if (code < 0) {
                    return;
                }
                callBack.callBack();
            });
        } else {
            //如果没有拍照，则不调用上传图片接口，直接跳转到CommitActivity
            callBack.callBack();
        }
    }

    //获取用户信息
    public void getClientinfo(ClientInfo data) {
        if (data != null) {
            clientInfo = data;
            //填充
            update_spouse_info_marriage_tv.setText(clientInfo.marriage);
            old_marriage = clientInfo.marriage;
            switch (clientInfo.marriage) {
                case "未婚":
                    findViewById(R.id.fab).setVisibility(View.GONE);
                    break;
                case "已婚":

//                    ListImgsReq req1 = new ListImgsReq();
//                    req1.label = Constants.FileLabelType.ID_BACK;
//                    req1.clt_id = contact.spouse.clt_id;
//                    UploadApi.listImgs(this, req1, resp -> {
//                        if (resp.list.size() != 0) {
//                            update_spouse_info_id_back_tv.setText("已上传");
//                            update_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
//                            idBackImgUrl = resp.list.get(0).s_url;
//                            idBackImgId = resp.list.get(0).id;
////                            backImg = resp.list.get(0);
//
//                        }
//                    });
//                    ListImgsReq req2 = new ListImgsReq();
//                    req2.label = Constants.FileLabelType.ID_FRONT;
//                    req2.clt_id = contact.spouse.clt_id;
//                    UploadApi.listImgs(this, req2, resp -> {
//                        if (resp.list.size() != 0) {
//                            update_spouse_info_id_front_tv.setText("已上传");
//                            update_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
//                            idFrontImgUrl = resp.list.get(0).s_url;
//                            idFrontImgId = resp.list.get(0).id;
////                            frontImg = resp.list.get(0);
////                            Log.e("TAG", "getClientinfo: "+frontImg);
//                        }
//                    });
                    findViewById(R.id.fab).setVisibility(View.VISIBLE);
                    update_spouse_info_marriage_group_lin.setVisibility(View.VISIBLE);
                    update_spouse_info_clt_nm_edt.setText(clientInfo.spouse.clt_nm);
                    update_spouse_info_id_no_edt.setText(clientInfo.spouse.id_no);
                    update_spouse_info_gender_tv.setText(clientInfo.spouse.gender);
                    if (clientInfo.spouse.reg_addr.province.equals("") && clientInfo.spouse.reg_addr.city.equals("") && clientInfo.spouse.reg_addr.district.equals("")) {
                        update_spouse_info_reg_tv.setText(null);
                    } else {
                        update_spouse_info_reg_tv.setText(clientInfo.spouse.reg_addr.province + "/" + clientInfo.spouse.reg_addr.city + "/" + clientInfo.spouse.reg_addr.district);
                    }
                    update_spouse_info_mobile_edt.setText(clientInfo.spouse.mobile);
                    update_spouse_info_education_tv.setText(clientInfo.spouse.edu);
                    if (clientInfo.spouse.current_addr.province.equals("") && clientInfo.spouse.current_addr.city.equals("") && clientInfo.spouse.current_addr.district.equals("")) {
                        update_spouse_info_current_address_tv.setText(null);
                    } else {
                        update_spouse_info_current_address_tv.setText(clientInfo.spouse.current_addr.province + "/" + clientInfo.spouse.current_addr.city + "/" + clientInfo.spouse.current_addr.district);
                    }
                    update_spouse_info_current_address1_tv.setText(clientInfo.spouse.current_addr.address1);
                    update_spouse_info_current_address2_tv.setText(clientInfo.spouse.current_addr.address2);
                    update_spouse_info_income_from_tv.setText(clientInfo.spouse.major_income_type);
                    update_spouse_info_child_count_edt.setText(clientInfo.child_num);
                    //判断主要收入类型
                    switch (clientInfo.spouse.major_income_type) {
                        case "工资":
                            update_spouse_info_from_income_group_lin.setVisibility(View.VISIBLE);
                            update_spouse_info_from_income_year_edt.setText(clientInfo.spouse.major_income);
                            update_spouse_info_from_income_company_name_edt.setText(clientInfo.spouse.major_company_name);
                            if (!clientInfo.spouse.major_company_addr.province.equals("")) {
                                update_spouse_info_from_income_company_address_tv.setText(clientInfo.spouse.major_company_addr.province + "/" + clientInfo.spouse.major_company_addr.city + "/" + clientInfo.spouse.major_company_addr.district);
                            } else {
                                update_spouse_info_from_income_company_address_tv.setText(null);
                            }
                            update_spouse_info_from_income_company_address1_tv.setText(clientInfo.spouse.major_company_addr.address1);
                            update_spouse_info_from_income_company_address2_tv.setText(clientInfo.spouse.major_company_addr.address2);
                            update_spouse_info_work_position_tv.setText(clientInfo.spouse.major_work_position);
                            update_spouse_info_from_income_work_phone_num_edt.setText(clientInfo.spouse.major_work_phone_num);
                            break;
                        case "自营":
                            update_spouse_info_from_self_group_lin.setVisibility(View.VISIBLE);
                            update_spouse_info_from_self_type_tv.setText(clientInfo.spouse.major_busi_type);
                            update_spouse_info_from_self_year_edt.setText(clientInfo.spouse.major_income);
                            update_spouse_info_from_self_type_tv.setText(clientInfo.spouse.major_busi_type);
                            update_spouse_info_from_self_company_name_edt.setText(clientInfo.spouse.major_company_name);
                            if (!clientInfo.spouse.major_company_addr.province.equals("")) {
                                update_spouse_info_from_self_company_address_tv.setText(clientInfo.spouse.major_company_addr.province + "/" + clientInfo.spouse.major_company_addr.city + "/" + clientInfo.spouse.major_company_addr.district);
                            } else {
                                update_spouse_info_from_self_company_address_tv.setText(null);
                            }
                            update_spouse_info_from_self_company_address1_tv.setText(clientInfo.spouse.major_company_addr.address1);
                            update_spouse_info_from_self_company_address2_tv.setText(clientInfo.spouse.major_company_addr.address2);
                            break;
                        case "其他":
                            update_spouse_info_from_other_group_lin.setVisibility(View.VISIBLE);
                            update_spouse_info_from_other_year_edt.setText(clientInfo.spouse.major_income);
                            update_spouse_info_from_other_remark_tv.setText(clientInfo.spouse.major_remark);
                            break;
                        default:
                            break;
                    }
                    update_spouse_info_extra_income_from_tv.setText(clientInfo.spouse.extra_income_type);
                    //判断额外收入类型
                    switch (clientInfo.spouse.extra_income_type) {
                        case "工资":
                            update_spouse_info_extra_from_income_group_lin.setVisibility(View.VISIBLE);
                            update_spouse_info_extra_from_income_work_position_tv.setText(clientInfo.spouse.extra_work_position);
                            update_spouse_info_extra_from_income_year_edt.setText(clientInfo.spouse.extra_income);
                            update_spouse_info_extra_from_income_company_name_edt.setText(clientInfo.spouse.extra_company_name);
                            if (!clientInfo.spouse.extra_company_addr.province.equals("")) {
                                update_spouse_info_extra_from_income_company_address_tv.setText(clientInfo.spouse.extra_company_addr.province + "/" + clientInfo.spouse.extra_company_addr.city + "/" + clientInfo.spouse.extra_company_addr.district);
                            } else {
                                update_spouse_info_extra_from_income_company_address_tv.setText(null);
                            }
                            update_spouse_info_extra_from_income_company_address1_tv.setText(clientInfo.spouse.extra_company_addr.address1);
                            update_spouse_info_extra_from_income_company_address2_tv.setText(clientInfo.spouse.extra_company_addr.address2);
                            update_spouse_info_extra_from_income_work_phone_num_edt.setText(clientInfo.spouse.extra_work_phone_num);
                            break;
                        case "无":
                            update_spouse_info_extra_from_income_group_lin.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }

                    update_spouse_info_house_type_tv.setText(clientInfo.spouse.house_type);
                    update_spouse_info_house_area_edt.setText(clientInfo.spouse.house_area);
                    update_spouse_info_house_owner_name_edt.setText(clientInfo.spouse.house_owner_name);
                    update_spouse_info_house_owner_relation_tv.setText(clientInfo.spouse.house_owner_relation);
                    update_spouse_info_urg_relation1_tv.setText(clientInfo.spouse.urg_relation1);
                    update_spouse_info_urg_mobile1_edt.setText(clientInfo.spouse.urg_mobile1);
                    update_spouse_info_urg_contact1_edt.setText(clientInfo.spouse.urg_contact1);
                    update_spouse_info_urg_relation2_tv.setText(clientInfo.spouse.urg_relation2);
                    update_spouse_info_urg_mobile2_edt.setText(clientInfo.spouse.urg_mobile2);
                    update_spouse_info_urg_contact2_edt.setText(clientInfo.spouse.urg_contact2);
                    update_spouse_info_live_with_parent_tv.setText(clientInfo.spouse.is_live_with_parent);


                    break;
                case "离异":

//                    ListImgsReq req3 = new ListImgsReq();
//                    req3.label = Constants.FileLabelType.MARRIAGE_PROOF;
//                    req3.clt_id = contact.clt_id;
//                    UploadApi.listImgs(this, req3, resp -> {
//                        if (resp.list.size() != 0) {
//                            update_spouse_info_divorced_tv.setText("已上传");
//                            update_spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.system_color));
//                            divorceImgsList = (ArrayList<UploadImgItemBean>) resp.list;
//                        }
//                    });
                    findViewById(R.id.fab).setVisibility(View.GONE);
                    update_spouse_info_marriage_group_lin.setVisibility(View.GONE);
                    update_spouse_info_die_group_lin.setVisibility(View.GONE);
                    update_spouse_info_divorced_group_lin.setVisibility(View.VISIBLE);
                    update_spouse_info_child_count1_edt.setText(clientInfo.child_num);
                    break;
                case "丧偶":
//                    ListImgsReq req4 = new ListImgsReq();
//                    req4.label = Constants.FileLabelType.RES_BOOKLET;
//                    req4.clt_id = contact.clt_id;
//                    UploadApi.listImgs(this, req4, resp -> {
//                        if (resp.list.size() != 0) {
//                            update_spouse_info_register_addr_tv.setText("已上传");
//                            update_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.system_color));
//                            resBookList = (ArrayList<UploadImgItemBean>) resp.list;
//                        }
//                    });
                    findViewById(R.id.fab).setVisibility(View.GONE);
                    update_spouse_info_divorced_group_lin.setVisibility(View.GONE);
                    update_spouse_info_die_group_lin.setVisibility(View.VISIBLE);
                    update_spouse_info_child_count2_edt.setText(clientInfo.child_num);
                    break;

                default:
                    break;
            }
        }
    }

    public void requestUpload(String clt_id, OnVoidCallBack onVoidCallBack) {
        uploadUrl(clt_id, onVoidCallBack);
    }


//    private void initView() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        ArrayList<Fragment> mFragments = new ArrayList<>();
//        mUpdateSpouseInfoFragment = new UpdateSpouseInfoFragment();
//        mUpdateImgsLabelFragment = new UpdateImgsLabelFragment();
//        mFragments.add(mUpdateSpouseInfoFragment);
//        mFragments.add(mUpdateImgsLabelFragment);
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 1) {
//                    if (!clientInfo.marriage.equals("已婚")) {
//                        mUpdateImgsLabelFragment.setVisibility(View.GONE);
//                    } else {
//                        mUpdateImgsLabelFragment.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
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

    private class InfoViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments;

        InfoViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

    }

    private void showDoubleCheckForExit() {
        new AlertDialog.Builder(this).setMessage("退出时未提交的更新将会被舍弃")
                .setPositiveButton("确定该操作", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("取消该操作", (dialog, which) -> dialog.dismiss()).show();
    }
}

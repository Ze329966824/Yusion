package com.yusion.shanghai.yusion.ui.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.user.GetGuarantorInfoReq;
import com.yusion.shanghai.yusion.bean.user.GuarantorInfo;
import com.yusion.shanghai.yusion.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.utils.CheckIdCardValidUtil;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.InputMethodUtil;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OcrUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion.widget.NoEmptyEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateGuarantorInfoActivity extends BaseActivity {
//    private UpdateGuarantorInfoFragment mUpdateGuarantorInfoFragment;
//    private UpdateImgsLabelFragment mUpdateImgsLabelFragment;
//    private String[] mTabTitle = {"担保人资料", "影像件"};\

    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
        add("其他");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
        add("无");
    }};
    public static int UPDATE_INCOME_FROME_INDEX;
    public static int UPDATE_EXTRA_INCOME_FROME_INDEX;
    public static int UPDATE_SEX_INDEX;
    public static int UPDATE_EDUCATION_INDEX;
    public static int UPDATE_HOUSE_TYPE_INDEX;
    public static int UPDATE_HOUSE_OWNER_RELATION_INDEX;
    public static int UPDATE_FROM_INCOME_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_EXTRA_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_SELF_TYPE_INDEX;
    public static int CURRENT_CLICKED_VIEW_FOR_ADDRESS = -1;
    private LinearLayout income_from_lin;
    private LinearLayout income_extra_from_lin;
    private TextView income_from_tv;
    private TextView income_extra_from_tv;
    private LinearLayout update_guarantor_info_gender_lin;
    private LinearLayout update_guarantor_info_reg_lin;
    private LinearLayout update_guarantor_info_education_lin;
    private LinearLayout update_guarantor_info_current_address_lin;
    private LinearLayout update_guarantor_info_current_address1_lin;
    private LinearLayout update_guarantor_info_house_type_lin;
    private TextView update_guarantor_info_house_owner_relation_tv;
    private LinearLayout update_guarantor_info_from_income_company_address_lin;
    private LinearLayout update_guarantor_info_from_income_company_address1_lin;
    private LinearLayout update_guarantor_info_from_income_work_position_lin;
    private LinearLayout update_guarantor_info_extra_from_income_company_address1_lin;
    private LinearLayout update_guarantor_info_extra_from_income_work_position_lin;
    private TextView update_guarantor_info_extra_from_income_work_position_tv;
    private LinearLayout update_guarantor_info_extra_from_income_company_address_lin;
    private LinearLayout update_guarantor_info_from_self_company_address_lin;
    private LinearLayout update_guarantor_info_from_self_company_address1_lin;
    private LinearLayout update_guarantor_info_from_self_type_lin;
    private NestedScrollView mScrollView;
    private LinearLayout update_guarantor_info_from_income_group_lin;
    private LinearLayout update_guarantor_info_from_self_group_lin;
    private LinearLayout update_guarantor_info_from_other_group_lin;
    private LinearLayout update_guarantor_info_extra_from_income_group_lin;
    private NoEmptyEditText update_guarantor_info_clt_nm_edt;                       //姓名
    private EditText update_guarantor_info_id_no_edt;                        //身份证号
    private TextView update_guarantor_info_gender_tv;                        //性别
    private TextView update_guarantor_info_reg_tv;                           //户籍
    private EditText update_guarantor_info_mobile_edt;                       //手机号
    private ImageView update_guarantor_info_id_no_img;                       //手机号
    private TextView update_guarantor_info_education_tv;                     //学历
    private TextView update_guarantor_info_current_address_tv;               //现住地址
    private TextView update_guarantor_info_current_address1_tv;              //详细地址
    private NoEmptyEditText update_guarantor_info_current_address2_tv;              //门牌号
    private TextView update_guarantor_info_income_from_tv;                   //主要收入来源
    private EditText update_guarantor_info_from_income_year_edt;             //主要-工资-年收入
    private NoEmptyEditText update_guarantor_info_from_income_company_name_edt;     //主要-工资-单位名称
    private TextView update_guarantor_info_from_income_company_address_tv;   //主要-工资-单位地址
    private TextView update_guarantor_info_from_income_company_address1_tv;  //主要-工资-详细地址
    private NoEmptyEditText update_guarantor_info_from_income_company_address2_tv;  //主要-工资-门牌号
    private TextView update_guarantor_info_work_position_tv;                 //主要-工资-职务
    private NoEmptyEditText update_guarantor_info_from_income_work_phone_num_edt;   //主要-工资-单位座机(选填)
    private EditText update_guarantor_info_from_self_year_edt;               //主要-自营-年收入
    private TextView update_guarantor_info_from_self_type_tv;                //主要-自营-业务类型
    private NoEmptyEditText update_guarantor_info_from_self_company_name_edt;       //主要-自营-店铺名称
    private TextView update_guarantor_info_from_self_company_address_tv;     //主要-自营-单位地址
    private TextView update_guarantor_info_from_self_company_address1_tv;    //主要-自营-详细地址
    private NoEmptyEditText update_guarantor_info_from_self_company_address2_tv;    //主要-自营-门牌号
    private EditText update_guarantor_info_from_other_year_edt;              //主要-其他-年收入
    private NoEmptyEditText update_guarantor_info_from_other_remark_tv;             //主要-其他-备注
    private TextView update_guarantor_info_extra_income_from_tv;             //额外收入来源
    private EditText update_guarantor_info_extra_from_income_year_edt;            //额外-工资-年收入
    private NoEmptyEditText update_guarantor_info_extra_from_income_company_name_edt;    //额外-工资-单位名称
    private TextView update_guarantor_info_extra_from_income_company_address_tv;  //额外-工资-公司地址
    private TextView update_guarantor_info_extra_from_income_company_address1_tv; //额外-工资-详细地址
    private NoEmptyEditText update_guarantor_info_extra_from_income_company_address2_tv; //额外-工资-门牌号
    private TextView update_guarantor_extra_info_work_position_tv;                //额外-工资-职务
    private NoEmptyEditText update_guarantor_info_extra_from_income_work_phone_num_edt;  //额外-工资-单位座机
    private TextView update_guarantor_info_house_type_tv;                     //房屋性质
    private LinearLayout update_guarantor_info_house_address_lin;         //房屋地址
    private TextView update_guarantor_info_house_address_tv;
    private LinearLayout update_guarantor_info_house_address1_lin;        //房屋详细地址
    private TextView update_guarantor_info_house_address1_tv;
    private TextView update_guarantor_info_house_address2_tv;               //门牌号


    private NoEmptyEditText update_guarantor_info_house_owner_name_edt;              //房屋所有人
    private LinearLayout update_guarantor_info_house_owner_relation_lin;    //与担保人关系

    private GuarantorInfo guarantorInfo;
    private File imageFile;
    private OcrResp.ShowapiResBodyBean mOcrResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_guarantor_info);
        initTitleBar(this, "担保人资料").setLeftClickListener(v -> showDoubleCheckForExit());
        initView();

        getInfo();  //获取担保人信息
        findViewById(R.id.submit_img).setOnClickListener(v -> {
            submit();   //更新用户信息
        });


    }

    private void initView() {
        mScrollView = ((NestedScrollView) findViewById(R.id.scrollView));
        //回到顶部按钮
        findViewById(R.id.fab).setOnClickListener(v -> mScrollView.smoothScrollTo(0, 0));

        //初始化
        update_guarantor_info_clt_nm_edt = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_clt_nm_edt);
        update_guarantor_info_id_no_edt = (EditText) findViewById(R.id.update_guarantor_info_id_no_edt);
        update_guarantor_info_gender_tv = (TextView) findViewById(R.id.update_guarantor_info_gender_tv);
        update_guarantor_info_reg_tv = (TextView) findViewById(R.id.update_guarantor_info_reg_tv);
        update_guarantor_info_mobile_edt = (EditText) findViewById(R.id.update_guarantor_info_mobile_edt);
        update_guarantor_info_education_tv = (TextView) findViewById(R.id.update_guarantor_info_education_tv);
        update_guarantor_info_current_address_tv = (TextView) findViewById(R.id.update_guarantor_info_current_address_tv);
        update_guarantor_info_current_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_current_address1_tv);
        update_guarantor_info_current_address2_tv = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_current_address2_tv);
        update_guarantor_info_income_from_tv = (TextView) findViewById(R.id.update_guarantor_info_income_from_tv);
        update_guarantor_info_from_income_year_edt = (EditText) findViewById(R.id.update_guarantor_info_from_income_year_edt);
        update_guarantor_info_from_income_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_from_income_company_name_edt);
        update_guarantor_info_from_income_company_address_tv = (TextView) findViewById(R.id.update_guarantor_info_from_income_company_address_tv);
        update_guarantor_info_from_income_company_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_from_income_company_address1_tv);
        update_guarantor_info_from_income_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_from_income_company_address2_tv);
        update_guarantor_info_work_position_tv = (TextView) findViewById(R.id.update_guarantor_info_work_position_tv);
        update_guarantor_info_from_income_work_phone_num_edt = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_from_income_work_phone_num_edt);
        update_guarantor_info_from_self_year_edt = (EditText) findViewById(R.id.update_guarantor_info_from_self_year_edt);
        update_guarantor_info_from_self_type_tv = (TextView) findViewById(R.id.update_guarantor_info_from_self_type_tv);
        update_guarantor_info_from_self_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_from_self_company_name_edt);
        update_guarantor_info_from_self_company_address_tv = (TextView) findViewById(R.id.update_guarantor_info_from_self_company_address_tv);
        update_guarantor_info_from_self_company_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_from_self_company_address1_tv);
        update_guarantor_info_from_self_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_from_self_company_address2_tv);
        update_guarantor_info_from_other_year_edt = (EditText) findViewById(R.id.update_guarantor_info_from_other_year_edt);
        update_guarantor_info_from_other_remark_tv = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_from_other_remark_tv);
        update_guarantor_info_extra_income_from_tv = (TextView) findViewById(R.id.update_guarantor_info_extra_income_from_tv);
        update_guarantor_info_extra_from_income_year_edt = (EditText) findViewById(R.id.update_guarantor_info_extra_from_income_year_edt);
        update_guarantor_info_extra_from_income_company_name_edt = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_extra_from_income_company_name_edt);
        update_guarantor_info_extra_from_income_company_address_tv = (TextView) findViewById(R.id.update_guarantor_info_extra_from_income_company_address_tv);
        update_guarantor_info_extra_from_income_company_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_extra_from_income_company_address1_tv);
        update_guarantor_info_extra_from_income_company_address2_tv = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_extra_from_income_company_address2_tv);
        update_guarantor_extra_info_work_position_tv = (TextView) findViewById(R.id.update_guarantor_extra_info_work_position_tv);
        update_guarantor_info_extra_from_income_work_phone_num_edt = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_extra_from_income_work_phone_num_edt);
        update_guarantor_info_house_type_tv = (TextView) findViewById(R.id.update_guarantor_info_house_type_tv);
        update_guarantor_info_house_owner_name_edt = (NoEmptyEditText) findViewById(R.id.update_guarantor_info_house_owner_name_edt);
        update_guarantor_info_from_income_group_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_income_group_lin);
        update_guarantor_info_from_self_group_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_self_group_lin);
        update_guarantor_info_from_other_group_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_other_group_lin);
        update_guarantor_info_house_address_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_house_address_lin);
        update_guarantor_info_house_address1_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_house_address1_lin);
        update_guarantor_info_house_address_tv = (TextView) findViewById(R.id.update_guarantor_info_house_address_tv);
        update_guarantor_info_house_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_house_address1_tv);
        update_guarantor_info_house_address2_tv = (TextView) findViewById(R.id.update_guarantor_info_house_address2_tv);
        update_guarantor_info_id_no_img = (ImageView) findViewById(R.id.update_guarantor_info_id_no_img);

        //拍摄身份证
        update_guarantor_info_id_no_img.setOnClickListener(v ->{
            imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(intent, 3001);
        });
        //选择收入来源
        income_from_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_income_from_lin);
        income_from_tv = (TextView) findViewById(R.id.update_guarantor_info_income_from_tv);
        income_from_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(incomelist, UPDATE_INCOME_FROME_INDEX,
                income_from_lin,
                income_from_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_INCOME_FROME_INDEX = selectedIndex;
                    if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("工资")) {
                        findViewById(R.id.update_guarantor_info_from_income_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_guarantor_info_from_income_group_lin).setVisibility(View.GONE);
                    }
                    if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("自营")) {
                        findViewById(R.id.update_guarantor_info_from_self_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_guarantor_info_from_self_group_lin).setVisibility(View.GONE);
                    }
                    if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("其他")) {
                        findViewById(R.id.update_guarantor_info_from_other_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_guarantor_info_from_other_group_lin).setVisibility(View.GONE);
                    }
                }));

        //工资 公司地址
        update_guarantor_info_from_income_company_address_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_income_company_address_lin);
        update_guarantor_info_from_income_company_address_tv = (TextView) findViewById(R.id.update_guarantor_info_from_income_company_address_tv);
        update_guarantor_info_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_from_income_company_address_lin,
                        update_guarantor_info_from_income_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_guarantor_info_from_income_company_address1_tv.setText("")
                );
            }
        });

        //工资 详细地址
        update_guarantor_info_from_income_company_address1_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_income_company_address1_lin);
        update_guarantor_info_from_income_company_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_from_income_company_address1_tv);
        update_guarantor_info_from_income_company_address1_lin.setOnClickListener(v -> {
            if (update_guarantor_info_from_income_company_address_tv != null) {
                update_guarantor_info_from_income_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_from_income_company_address1_lin.getId();
                requestPOI(update_guarantor_info_from_income_company_address_tv.getText().toString());
            } else {
                update_guarantor_info_from_income_company_address1_tv.setEnabled(false);
            }
        });

        // 工资 选择职务
        update_guarantor_info_from_income_work_position_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_income_work_position_lin);
        update_guarantor_info_work_position_tv = (TextView) findViewById(R.id.update_guarantor_info_work_position_tv);
        update_guarantor_info_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                update_guarantor_info_from_income_work_position_lin,
                update_guarantor_info_work_position_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_FROM_INCOME_WORK_POSITION_INDEX = selectedIndex));

        //自营 业务类型
        update_guarantor_info_from_self_type_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_self_type_lin);
        update_guarantor_info_from_self_type_tv = (TextView) findViewById(R.id.update_guarantor_info_from_self_type_tv);
        update_guarantor_info_from_self_type_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.busi_type_list_key,
                UPDATE_FROM_SELF_TYPE_INDEX,
                update_guarantor_info_from_self_type_lin,
                update_guarantor_info_from_self_type_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_FROM_SELF_TYPE_INDEX = selectedIndex;
                    if (YusionApp.CONFIG_RESP.busi_type_list_value.get(UPDATE_FROM_SELF_TYPE_INDEX).equals("其他")) {
                        EditText editText = new EditText(UpdateGuarantorInfoActivity.this);
                        new AlertDialog.Builder(UpdateGuarantorInfoActivity.this)
                                .setTitle("请输入业务类型")
                                .setView(editText)
                                .setCancelable(false)
                                .setPositiveButton("确定", (dialog, which) -> {
                                    update_guarantor_info_from_self_type_tv.setText(editText.getText());
                                    UPDATE_FROM_SELF_TYPE_INDEX = 0;
                                    InputMethodUtil.hideInputMethod(this);
                                    dialog.dismiss();
                                })
                                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
                    }
                }
        ));

        //自营 单位地址
        update_guarantor_info_from_self_company_address_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_self_company_address_lin);
        update_guarantor_info_from_self_company_address_tv = (TextView) findViewById(R.id.update_guarantor_info_from_self_company_address_tv);
        update_guarantor_info_from_self_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_from_self_company_address_lin,
                        update_guarantor_info_from_self_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_guarantor_info_from_self_company_address1_tv.setText("")
                );
            }
        });


        //自营 详细地址
        update_guarantor_info_from_self_company_address1_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_from_self_company_address1_lin);
        update_guarantor_info_from_self_company_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_from_self_company_address1_tv);
        update_guarantor_info_from_self_company_address1_lin.setOnClickListener(v -> {
            if (update_guarantor_info_from_self_company_address_tv != null) {
                update_guarantor_info_from_self_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_from_self_company_address1_lin.getId();
                requestPOI(update_guarantor_info_from_self_company_address_tv.getText().toString());
            } else {
                update_guarantor_info_from_self_company_address1_tv.setEnabled(false);
            }
        });

        //额外 公司地址
        update_guarantor_info_extra_from_income_company_address_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_extra_from_income_company_address_lin);
        update_guarantor_info_extra_from_income_company_address_tv = (TextView) findViewById(R.id.update_guarantor_info_extra_from_income_company_address_tv);
        update_guarantor_info_extra_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_extra_from_income_company_address_lin,
                        update_guarantor_info_extra_from_income_company_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_guarantor_info_extra_from_income_company_address1_tv.setText("")
                );
            }
        });
        update_guarantor_info_extra_from_income_group_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_extra_from_income_group_lin);
        //选择额外收入来源
        income_extra_from_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) findViewById(R.id.update_guarantor_info_extra_income_from_tv);
        income_extra_from_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(incomeextarlist,
                UPDATE_EXTRA_INCOME_FROME_INDEX,
                income_extra_from_lin,
                income_extra_from_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_EXTRA_INCOME_FROME_INDEX = selectedIndex;
                    if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("工资")) {
                        findViewById(R.id.update_guarantor_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.update_guarantor_info_extra_from_income_group_lin).setVisibility(View.GONE);
                    }
                    if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("无")) {
                        findViewById(R.id.update_guarantor_info_extra_from_income_group_lin).setVisibility(View.GONE);

                    }
                }
        ));

        //额外 详细地址
        update_guarantor_info_extra_from_income_company_address1_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_extra_from_income_company_address1_lin);
        update_guarantor_info_extra_from_income_company_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_extra_from_income_company_address1_tv);
        update_guarantor_info_extra_from_income_company_address1_lin.setOnClickListener(v -> {
            if (update_guarantor_info_extra_from_income_company_address_tv != null) {
                update_guarantor_info_extra_from_income_company_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_extra_from_income_company_address1_lin.getId();
                requestPOI(update_guarantor_info_extra_from_income_company_address_tv.getText().toString());
            } else {
                update_guarantor_info_extra_from_income_company_address1_tv.setEnabled(false);
            }
        });

        // 额外 选择职务
        update_guarantor_info_extra_from_income_work_position_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_extra_from_income_work_position_lin);
        update_guarantor_info_extra_from_income_work_position_tv = (TextView) findViewById(R.id.update_guarantor_extra_info_work_position_tv);
        update_guarantor_info_extra_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                UPDATE_FROM_EXTRA_WORK_POSITION_INDEX,
                update_guarantor_info_extra_from_income_work_position_lin,
                update_guarantor_info_extra_from_income_work_position_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex));

        //选择性别
        update_guarantor_info_gender_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_gender_lin);
        update_guarantor_info_gender_tv = (TextView) findViewById(R.id.update_guarantor_info_gender_tv);
        update_guarantor_info_gender_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.gender_list_key,
                UPDATE_SEX_INDEX,
                update_guarantor_info_gender_lin,
                update_guarantor_info_gender_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_SEX_INDEX = selectedIndex
        ));

        //选择户籍地
        update_guarantor_info_reg_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_reg_lin);
        update_guarantor_info_reg_tv = (TextView) findViewById(R.id.update_guarantor_info_reg_tv);
        update_guarantor_info_reg_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_reg_lin,
                        update_guarantor_info_reg_tv,
                        "请选择所在地区",
                        (clickedView, city) -> {

                        }
                );
            }
        });

        //选择学历
        update_guarantor_info_education_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_education_lin);
        update_guarantor_info_education_tv = (TextView) findViewById(R.id.update_guarantor_info_education_tv);
        update_guarantor_info_education_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.education_list_key,
                UPDATE_EDUCATION_INDEX,
                update_guarantor_info_education_lin,
                update_guarantor_info_education_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_EDUCATION_INDEX = selectedIndex));

        //现在地址
        update_guarantor_info_current_address_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_current_address_lin);
        update_guarantor_info_current_address_tv = (TextView) findViewById(R.id.update_guarantor_info_current_address_tv);
        update_guarantor_info_current_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_current_address_lin,
                        update_guarantor_info_current_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_guarantor_info_current_address1_tv.setText("")
                );
            }
        });

        //详细地址
        update_guarantor_info_current_address1_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_current_address1_lin);
        update_guarantor_info_current_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_current_address1_tv);
        update_guarantor_info_current_address1_lin.setOnClickListener(v -> {
            if (!update_guarantor_info_current_address_tv.getText().toString().isEmpty()) {
                update_guarantor_info_current_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_current_address1_lin.getId();
                requestPOI(update_guarantor_info_current_address_tv.getText().toString());
            } else {
                update_guarantor_info_current_address1_tv.setEnabled(false);
            }
        });

        //房屋类型
        update_guarantor_info_house_type_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_house_type_lin);
        update_guarantor_info_house_type_tv = (TextView) findViewById(R.id.update_guarantor_info_house_type_tv);
        update_guarantor_info_house_type_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_type_list_key,
                UPDATE_HOUSE_TYPE_INDEX,
                update_guarantor_info_house_type_lin,
                update_guarantor_info_house_type_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_HOUSE_TYPE_INDEX = selectedIndex));


        //与申请人关系
        update_guarantor_info_house_owner_relation_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_house_owner_relation_lin);
        update_guarantor_info_house_owner_relation_tv = (TextView) findViewById(R.id.update_guarantor_info_house_owner_relation_tv);
        update_guarantor_info_house_owner_relation_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_relationship_list_key,
                UPDATE_HOUSE_OWNER_RELATION_INDEX,
                update_guarantor_info_house_owner_relation_lin,
                update_guarantor_info_house_owner_relation_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_HOUSE_OWNER_RELATION_INDEX = selectedIndex));

        //房屋地址
        update_guarantor_info_house_address_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_house_address_lin);
        update_guarantor_info_house_address_tv = (TextView) findViewById(R.id.update_guarantor_info_house_address_tv);
        update_guarantor_info_house_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_house_address_lin,
                        update_guarantor_info_house_address_tv,
                        "请选择所在地区",
                        (clickedView, city) -> update_guarantor_info_house_address1_tv.setText("")
                );
            }
        });

        //详细地址
        update_guarantor_info_house_address1_lin = (LinearLayout) findViewById(R.id.update_guarantor_info_house_address1_lin);
        update_guarantor_info_house_address1_tv = (TextView) findViewById(R.id.update_guarantor_info_house_address1_tv);
        update_guarantor_info_house_address1_lin.setOnClickListener(v -> {
            if (!update_guarantor_info_house_address_tv.getText().toString().isEmpty()) {
                update_guarantor_info_house_address1_tv.setEnabled(true);
                CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_house_address1_lin.getId();
                requestPOI(update_guarantor_info_house_address_tv.getText().toString());
            } else {
                update_guarantor_info_house_address1_tv.setEnabled(false);
            }
        });


    }

    private void requestPOI(String city) {
        if (city != null) {
            String[] citys = city.split("/");
            if (citys.length == 3) {
                String city1 = citys[1];
                String city2 = citys[2];
                Intent intent = new Intent(UpdateGuarantorInfoActivity.this, AMapPoiListActivity.class);
                intent.putExtra("city", city1);
                intent.putExtra("keywords", city2);
                startActivityForResult(intent, Constants.REQUEST_ADDRESS);
            }
        }
    }


    private void getInfo() {
        ProductApi.getGuarantorInfo(this, new GetGuarantorInfoReq(), data -> {
            if (data != null) {
                guarantorInfo = data;
                getGuarantorInfo(guarantorInfo);
            }
        });
    }


    private void submit() {
        //提交用户资料
        updateGuarantorinfo(() -> ProductApi.updateGuarantorInfo(UpdateGuarantorInfoActivity.this, guarantorInfo, data -> {
            if (data == null) return;
            guarantorInfo = data;
            //上传影像件
//            mUpdateImgsLabelFragment.requestUpload(guarantorInfo.clt_id, () -> {

            toCommitActivity(guarantorInfo.clt_id, "guarantor", "担保人影像件资料","continue");

//            });
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_ADDRESS) {

                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_info_current_address1_lin.getId()) {
                    update_guarantor_info_current_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_info_house_address1_lin.getId()) {
                    update_guarantor_info_house_address1_tv.setText(data.getStringExtra("result"));
                }

                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_info_from_income_company_address1_lin.getId()) {
                    update_guarantor_info_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_info_from_self_company_address1_lin.getId()) {
                    update_guarantor_info_from_self_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_info_extra_from_income_company_address1_lin.getId()) {
                    update_guarantor_info_extra_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }

            }
        }else if (resultCode == Activity.RESULT_OK &&requestCode == 3001) {
            Dialog dialog = LoadingUtils.createLoadingDialog(this);
            dialog.show();
            OcrUtil.requestOcr(this, imageFile.getAbsolutePath(), new OSSObjectKeyBean("lender_sp", "id_card_back", ".png"), "id_card", (ocrResp1, objectKey) -> {
                if (ocrResp1 == null) {
                    Toast.makeText(UpdateGuarantorInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else if (ocrResp1.showapi_res_code != 0 && TextUtils.isEmpty(ocrResp1.showapi_res_body.idNo) || TextUtils.isEmpty(ocrResp1.showapi_res_body.name)) {
                    Toast.makeText(UpdateGuarantorInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(UpdateGuarantorInfoActivity.this, "识别成功", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    mOcrResp = ocrResp1.showapi_res_body;
                    if (mOcrResp != null) {
                        if (!TextUtils.isEmpty(mOcrResp.idNo)) {
                            update_guarantor_info_id_no_edt.setText(mOcrResp.idNo);
                        }
//                        if (!TextUtils.isEmpty(mOcrResp.name)) {
//                            update_guarantor_info_clt_nm_edt.setText(mOcrResp.name);
//                        }
//                        if (!TextUtils.isEmpty(mOcrResp.sex)) {
//                            update_guarantor_info_gender_tv.setText(mOcrResp.sex);
//                        }
                    }
                }
            }, (throwable, s) ->
                    Toast.makeText(UpdateGuarantorInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show());
        }
    }

    public void getGuarantorInfo(GuarantorInfo data) {
        if (data != null) {
            guarantorInfo = data;
            //填充
            update_guarantor_info_clt_nm_edt.setText(guarantorInfo.clt_nm);
            update_guarantor_info_id_no_edt.setText(guarantorInfo.id_no);
            update_guarantor_info_gender_tv.setText(guarantorInfo.gender);
            if (guarantorInfo.reg_addr.province.equals("") && guarantorInfo.reg_addr.city.equals("") && guarantorInfo.reg_addr.district.equals("")) {
                update_guarantor_info_reg_tv.setText(null);
            } else {
                update_guarantor_info_reg_tv.setText(guarantorInfo.reg_addr.province + "/" + guarantorInfo.reg_addr.city + "/" + guarantorInfo.reg_addr.district);
            }
            update_guarantor_info_mobile_edt.setText(guarantorInfo.mobile);
            update_guarantor_info_education_tv.setText(guarantorInfo.edu);
            if (!guarantorInfo.current_addr.province.equals("")) {
                update_guarantor_info_current_address_tv.setText(guarantorInfo.current_addr.province + "/" + guarantorInfo.current_addr.city + "/" + guarantorInfo.current_addr.district);
            } else {
                update_guarantor_info_current_address_tv.setText(null);
            }
            update_guarantor_info_current_address_tv.setText(guarantorInfo.current_addr.province + "/" + guarantorInfo.current_addr.city + "/" + guarantorInfo.current_addr.district);
            update_guarantor_info_current_address1_tv.setText(guarantorInfo.current_addr.address1);
            update_guarantor_info_current_address2_tv.setText(guarantorInfo.current_addr.address2);
            update_guarantor_info_income_from_tv.setText(guarantorInfo.major_income_type);
            //判断主要收入类型
            switch (guarantorInfo.major_income_type) {
                case "工资":
                    update_guarantor_info_from_income_group_lin.setVisibility(View.VISIBLE);
                    update_guarantor_info_from_income_year_edt.setText(guarantorInfo.major_income);
                    update_guarantor_info_from_income_company_name_edt.setText(guarantorInfo.major_company_name);
                    if (!guarantorInfo.major_company_addr.province.equals("")) {
                        update_guarantor_info_from_income_company_address_tv.setText(guarantorInfo.major_company_addr.province + "/" + guarantorInfo.major_company_addr.city + "/" + guarantorInfo.major_company_addr.district);
                    } else {
                        update_guarantor_info_from_income_company_address_tv.setText(null);
                    }
                    update_guarantor_info_from_income_company_address1_tv.setText(guarantorInfo.major_company_addr.address1);
                    update_guarantor_info_from_income_company_address2_tv.setText(guarantorInfo.major_company_addr.address2);
                    update_guarantor_info_work_position_tv.setText(guarantorInfo.major_work_position);
                    update_guarantor_info_from_income_work_phone_num_edt.setText(guarantorInfo.major_work_phone_num);
                    break;
                case "自营":
                    update_guarantor_info_from_self_group_lin.setVisibility(View.VISIBLE);
                    update_guarantor_info_from_self_year_edt.setText(guarantorInfo.major_income);
                    update_guarantor_info_from_self_type_tv.setText(guarantorInfo.major_busi_type);
                    update_guarantor_info_from_self_company_name_edt.setText(guarantorInfo.major_company_name);
                    if (!guarantorInfo.major_company_addr.province.equals("")) {
                        update_guarantor_info_from_self_company_address_tv.setText(guarantorInfo.major_company_addr.province + "/" + guarantorInfo.major_company_addr.city + "/" + guarantorInfo.major_company_addr.district);
                    } else {
                        update_guarantor_info_from_self_company_address_tv.setText(null);
                    }
                    update_guarantor_info_from_self_company_address1_tv.setText(guarantorInfo.major_company_addr.address1);
                    update_guarantor_info_from_self_company_address2_tv.setText(guarantorInfo.major_company_addr.address2);
                    break;
                case "其他":
                    update_guarantor_info_from_other_group_lin.setVisibility(View.VISIBLE);
                    update_guarantor_info_from_other_year_edt.setText(guarantorInfo.major_income);
                    update_guarantor_info_from_other_remark_tv.setText(guarantorInfo.major_remark);
                    break;
            }

            update_guarantor_info_extra_income_from_tv.setText(guarantorInfo.extra_income_type);
            //判断额外收入类型
            switch (guarantorInfo.extra_income_type) {
                case "工资":
                    update_guarantor_info_extra_from_income_group_lin.setVisibility(View.VISIBLE);
                    update_guarantor_info_extra_from_income_year_edt.setText(guarantorInfo.extra_income);
                    update_guarantor_info_extra_from_income_company_name_edt.setText(guarantorInfo.extra_company_name);
                    if (!guarantorInfo.extra_company_addr.province.equals("")) {
                        update_guarantor_info_extra_from_income_company_address_tv.setText(guarantorInfo.extra_company_addr.province + "/" + guarantorInfo.extra_company_addr.city + "/" + guarantorInfo.extra_company_addr.district);
                    } else {
                        update_guarantor_info_extra_from_income_company_address_tv.setText(null);
                    }
                    update_guarantor_info_extra_from_income_company_address1_tv.setText(guarantorInfo.extra_company_addr.address1);
                    update_guarantor_info_extra_from_income_company_address2_tv.setText(guarantorInfo.extra_company_addr.address2);
                    update_guarantor_extra_info_work_position_tv.setText(guarantorInfo.extra_work_position);
                    update_guarantor_info_extra_from_income_work_phone_num_edt.setText(guarantorInfo.extra_work_phone_num);
                    break;
                case "无":
                    update_guarantor_info_extra_from_income_group_lin.setVisibility(View.GONE);
            }
            update_guarantor_info_house_type_tv.setText(guarantorInfo.house_type);
            update_guarantor_info_house_owner_name_edt.setText(guarantorInfo.house_owner_name);
            update_guarantor_info_house_owner_relation_tv.setText(guarantorInfo.house_owner_relation);
            update_guarantor_info_house_address2_tv.setText(guarantorInfo.house_addr.address2);
            update_guarantor_info_house_address1_tv.setText(guarantorInfo.house_addr.address1);
            if (!guarantorInfo.house_addr.province.equals("")) {
                update_guarantor_info_house_address_tv.setText(guarantorInfo.house_addr.province + "/" + guarantorInfo.house_addr.city + "/" + guarantorInfo.house_addr.district);
            } else {
                update_guarantor_info_house_address_tv.setText(null);
            }
        }


    }

    public void updateGuarantorinfo(OnVoidCallBack callBack) {
        //校验
        if (checkUserInfo()) {

            //提交
            guarantorInfo.clt_nm = update_guarantor_info_clt_nm_edt.getText().toString().trim();
            guarantorInfo.id_no = update_guarantor_info_id_no_edt.getText().toString().trim();
            guarantorInfo.gender = update_guarantor_info_gender_tv.getText().toString().trim();
            if (update_guarantor_info_reg_tv.getText().toString().trim().split("/").length == 3) {
                guarantorInfo.reg_addr.province = update_guarantor_info_reg_tv.getText().toString().trim().split("/")[0];
                guarantorInfo.reg_addr.city = update_guarantor_info_reg_tv.getText().toString().trim().split("/")[1];
                guarantorInfo.reg_addr.district = update_guarantor_info_reg_tv.getText().toString().trim().split("/")[2];
                guarantorInfo.mobile = update_guarantor_info_mobile_edt.getText().toString().trim();
                guarantorInfo.edu = update_guarantor_info_education_tv.getText().toString().trim();
            }
            if (update_guarantor_info_current_address_tv.getText().toString().trim().split("/").length == 3) {
                guarantorInfo.current_addr.province = update_guarantor_info_current_address_tv.getText().toString().trim().split("/")[0];
                guarantorInfo.current_addr.city = update_guarantor_info_current_address_tv.getText().toString().trim().split("/")[1];
                guarantorInfo.current_addr.district = update_guarantor_info_current_address_tv.getText().toString().trim().split("/")[2];
                guarantorInfo.current_addr.address1 = update_guarantor_info_current_address1_tv.getText().toString().trim();
                guarantorInfo.current_addr.address2 = update_guarantor_info_current_address2_tv.getText().toString().trim();
            }
            guarantorInfo.major_income_type = update_guarantor_info_income_from_tv.getText().toString().trim();
            //判断主要收入类型
            switch (update_guarantor_info_income_from_tv.getText().toString().trim()) {
                case "工资":
                    guarantorInfo.major_income = update_guarantor_info_from_income_year_edt.getText().toString().trim();
                    guarantorInfo.major_company_name = update_guarantor_info_from_income_company_name_edt.getText().toString().trim();
                    if (update_guarantor_info_from_income_company_address_tv.getText().toString().trim().split("/").length == 3) {
                        guarantorInfo.major_company_addr.province = update_guarantor_info_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        guarantorInfo.major_company_addr.city = update_guarantor_info_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        guarantorInfo.major_company_addr.district = update_guarantor_info_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                        guarantorInfo.major_company_addr.address1 = update_guarantor_info_from_income_company_address1_tv.getText().toString().trim();
                        guarantorInfo.major_company_addr.address2 = update_guarantor_info_from_income_company_address2_tv.getText().toString().trim();
                    }
                    guarantorInfo.major_work_position = update_guarantor_info_work_position_tv.getText().toString().trim();
                    guarantorInfo.major_work_phone_num = update_guarantor_info_from_income_work_phone_num_edt.getText().toString().trim();
                    break;
                case "自营":
                    guarantorInfo.major_income = update_guarantor_info_from_self_year_edt.getText().toString().trim();
                    guarantorInfo.major_busi_type = update_guarantor_info_from_self_type_tv.getText().toString().trim();
                    guarantorInfo.major_company_name = update_guarantor_info_from_self_company_name_edt.getText().toString().trim();
                    if (TextUtils.isEmpty(update_guarantor_info_from_self_company_address_tv.getText())) {
                        guarantorInfo.major_company_addr.province = "";
                        guarantorInfo.major_company_addr.city = "";
                        guarantorInfo.major_company_addr.district = "";
                    } else {
                        guarantorInfo.major_company_addr.province = update_guarantor_info_from_self_company_address_tv.getText().toString().trim().split("/")[0];
                        guarantorInfo.major_company_addr.city = update_guarantor_info_from_self_company_address_tv.getText().toString().trim().split("/")[1];
                        guarantorInfo.major_company_addr.district = update_guarantor_info_from_self_company_address_tv.getText().toString().trim().split("/")[2];
                    }
                    guarantorInfo.major_company_addr.address1 = update_guarantor_info_from_self_company_address1_tv.getText().toString().trim();
                    guarantorInfo.major_company_addr.address2 = update_guarantor_info_from_self_company_address2_tv.getText().toString().trim();
                    break;
                case "其他":
                    guarantorInfo.major_income = update_guarantor_info_from_other_year_edt.getText().toString().trim();
                    guarantorInfo.major_remark = update_guarantor_info_from_other_remark_tv.getText().toString().trim();
                    break;
            }
            guarantorInfo.extra_income_type = update_guarantor_info_extra_income_from_tv.getText().toString().trim();
            //判断额外收入类型
            switch (update_guarantor_info_extra_income_from_tv.getText().toString().trim()) {
                case "工资":
                    guarantorInfo.extra_income = update_guarantor_info_extra_from_income_year_edt.getText().toString().trim();
                    guarantorInfo.extra_company_name = update_guarantor_info_extra_from_income_company_name_edt.getText().toString().trim();
                    if (update_guarantor_info_extra_from_income_company_address_tv.getText().toString().trim().split("/").length == 3) {
                        guarantorInfo.extra_company_addr.province = update_guarantor_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        guarantorInfo.extra_company_addr.city = update_guarantor_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        guarantorInfo.extra_company_addr.district = update_guarantor_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                        guarantorInfo.extra_company_addr.address1 = update_guarantor_info_extra_from_income_company_address1_tv.getText().toString().trim();
                        guarantorInfo.extra_company_addr.address2 = update_guarantor_info_extra_from_income_company_address2_tv.getText().toString().trim();
                    }
                    guarantorInfo.extra_work_position = update_guarantor_extra_info_work_position_tv.getText().toString().trim();
                    guarantorInfo.extra_work_phone_num = update_guarantor_info_extra_from_income_work_phone_num_edt.getText().toString().trim();
                    break;
                case "无":
                    break;
            }
            guarantorInfo.house_type = update_guarantor_info_house_type_tv.getText().toString().trim();
            guarantorInfo.house_owner_name = update_guarantor_info_house_owner_name_edt.getText().toString().trim();
            guarantorInfo.house_owner_relation = update_guarantor_info_house_owner_relation_tv.getText().toString().trim();
            if (update_guarantor_info_house_address_tv.getText().toString().trim().split("/").length == 3) {
                guarantorInfo.house_addr.province = update_guarantor_info_house_address_tv.getText().toString().trim().split("/")[0];
                guarantorInfo.house_addr.city = update_guarantor_info_house_address_tv.getText().toString().trim().split("/")[1];
                guarantorInfo.house_addr.district = update_guarantor_info_house_address_tv.getText().toString().trim().split("/")[2];
                guarantorInfo.house_addr.address1 = update_guarantor_info_house_address1_tv.getText().toString().trim();
                guarantorInfo.house_addr.address2 = update_guarantor_info_house_address2_tv.getText().toString().trim();
            }
            callBack.callBack();
        }
    }


    private boolean checkUserInfo() {
        if (update_guarantor_info_clt_nm_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_id_no_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "身份证号不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(update_guarantor_info_id_no_edt.getText().toString())) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "身份证号有误", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_gender_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "性别不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_reg_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "户籍地不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_mobile_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
        } else if (!CheckMobileUtil.checkMobile(update_guarantor_info_mobile_edt.getText().toString())) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "手机号码有误", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_education_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "学历不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_current_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "现住地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_current_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_current_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("")) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "收入来源不能为空", Toast.LENGTH_SHORT).show();
        }
        //主要工资
        else if (update_guarantor_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_from_income_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "工资年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_from_income_company_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位名称不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_from_income_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_from_income_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_from_income_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_work_position_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "职务不能为空", Toast.LENGTH_SHORT).show();
        }//主要自营
        else if (update_guarantor_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_info_from_self_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "自营年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_info_from_self_type_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "业务类型不能为空", Toast.LENGTH_SHORT).show();
        }//主要其他
        else if (update_guarantor_info_income_from_tv.getText().toString().equals("其他") && update_guarantor_info_from_other_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "其他年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_income_from_tv.getText().toString().equals("其他") && update_guarantor_info_from_other_remark_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "备注不能为空", Toast.LENGTH_SHORT).show();
        }


        //额外工资
        else if (update_guarantor_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_extra_from_income_year_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "年收入不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_extra_from_income_company_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位名称不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_extra_from_income_company_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_extra_from_income_company_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_info_extra_from_income_company_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_extra_info_work_position_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "职务不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_house_type_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "房屋性质不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_house_address_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "房屋地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_house_address1_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "房屋地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_house_address2_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "房屋地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_house_owner_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_guarantor_info_house_owner_relation_tv.getText().toString().isEmpty()) {
            Toast.makeText(UpdateGuarantorInfoActivity.this, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void showDoubleCheckForExit() {
        new AlertDialog.Builder(UpdateGuarantorInfoActivity.this).setMessage("退出时未提交的更新将会被舍弃")
                .setPositiveButton("确定该操作", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("取消该操作", (dialog, which) -> dialog.dismiss()).show();
    }

//        mUpdateImgsLabelFragment.requestUpload(guarantorInfo.clt_id, new OnVoidCallBack() {
//            @Override
//            public void callBack() {
//                if (mUpdateGuarantorInfoFragment.updateGuarantorinfo()) {
//                    ProductApi.updateGuarantorInfo(UpdateGuarantorInfoActivity.this, guarantorInfo, new OnItemDataCallBack<GuarantorInfo>() {
//                        @Override
//                        public void onItemDataCallBack(GuarantorInfo data) {
//                            if (data == null) return;
//                            Intent intent = new Intent(UpdateGuarantorInfoActivity.this, CommitActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                }
//            }
//        });
//}

//    private void initView() {
//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        ArrayList<Fragment> mFragments = new ArrayList<>();
//        mUpdateGuarantorInfoFragment = new UpdateGuarantorInfoFragment();
//        mUpdateImgsLabelFragment = new UpdateImgsLabelFragment();
//        mFragments.add(mUpdateGuarantorInfoFragment);
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
//            public IPagerTitleView getTitleView(Context context, int i) {
//                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
//                colorTransitionPagerTitleView.setNormalColor(0xFF999999);
//                colorTransitionPagerTitleView.setSelectedColor(0xFF06B7A3);
//                colorTransitionPagerTitleView.setText(mTabTitle[i]);
//                colorTransitionPagerTitleView.setOnClickListener(view -> viewPager.setCurrentItem(i));
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
//        ViewPagerHelper.bind(mMagicIndicator, viewPager);
//    }

//private class InfoViewPagerAdapter extends FragmentPagerAdapter {
//    private final List<Fragment> mFragments;
//
//
//    InfoViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
//        super(fm);
//        mFragments = fragments;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mFragments.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return mFragments == null ? 0 : mFragments.size();
//    }
//
//}


}

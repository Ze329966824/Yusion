package com.yusion.shanghai.yusion.ui.update;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;

import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.utils.ContactsUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.yusion.shanghai.yusion.R.id.update_personal_info_company_name_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_contact1_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_contact2_edt;
import static com.yusion.shanghai.yusion.R.id.update_personal_info_urg_mobile1_edt;

/**
 * Created by ice on 2017/8/21.
 */

public class UpdatePersonalInfoFragment extends BaseFragment {
    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
        add("其他");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
    }};
    private List<String> sexlist = new ArrayList<String>() {{
        add("男");
        add("女?");
    }};
    private List<String> educationlist = new ArrayList<String>() {{
        add("大专");
        add("本科");
        add("硕士");
        add("博士");
    }};
    private List<String> housetypelist = new ArrayList<String>() {{
        add("商品房");
        add("自建房");
    }};
    private List<String> houseownerlist = new ArrayList<String>() {{
        add("父母");
        add("恋人");
    }};

    public static int UPDATE_INCOME_FROME_INDEX;
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
    private ClientInfo data;
    private LinearLayout update_personal_info_from_income_group_lin;
    private LinearLayout update_personal_info_from_self_group_lin;
    private LinearLayout update_personal_info_from_other_group_lin;
    private LinearLayout update_personal_info_extra_from_income_group_lin;


    private EditText update_personal_info_clt_nm_edt;                       //姓名
    private EditText update_personal_info_id_no_edt;                        //身份证号
    private TextView update_personal_info_gender_tv;                        //性别
    private TextView update_personal_info_reg_tv;                           //户籍
    private EditText update_personal_info_mobile_edt;                       //手机号
    private TextView update_personal_info_education_tv;                     //学历
    private TextView update_personal_info_current_address_tv;               //现住地址
    private TextView update_personal_info_current_address1_tv;              //详细地址
    private EditText update_personal_info_current_address2_tv;              //门牌号
    private TextView update_personal_info_income_from_tv;                   //主要收入来源
    private EditText update_personal_info_from_income_year_edt;             //主要-工资-年收入
    private EditText update_personal_info_from_income_company_name_edt;     //主要-工资-单位名称
    private TextView update_personal_info_from_income_company_address_tv;   //主要-工资-单位地址
    private TextView update_personal_info_from_income_company_address1_tv;  //主要-工资-详细地址
    private EditText update_personal_info_from_income_company_address2_tv;  //主要-工资-门牌号
    private TextView update_personal_info_work_position_tv;                 //主要-工资-职务
    private EditText update_personal_info_from_income_work_phone_num_edt;   //主要-工资-单位座机
    private EditText update_personal_info_from_self_year_edt;               //主要-自营-年收入
    private TextView update_personal_info_from_self_type_tv;                //主要-自营-业务类型
    private EditText update_personal_info_from_self_company_name_edt;       //主要-自营-店铺名称
    private TextView update_personal_info_from_self_company_address_tv;     //主要-自营-单位地址
    private TextView update_personal_info_from_self_company_address1_tv;    //主要-自营-详细地址
    private EditText update_personal_info_from_self_company_address2_tv;    //主要-自营-门牌号
    private EditText update_personal_info_from_other_year_edt;              //主要-其他-年收入
    private EditText update_personal_info_from_other_remark_tv;             //主要-其他-备注
    private TextView update_personal_info_extra_income_from_tv;             //额外收入来源
    private EditText update_personal_info_extra_from_income_year_edt;            //额外-工资-年收入
    private EditText update_personal_info_extra_from_income_company_name_edt;    //额外-工资-单位名称
    private TextView update_personal_info_extra_from_income_company_address_tv;  //额外-工资-公司地址
    private TextView update_personal_info_extra_from_income_company_address1_tv; //额外-工资-详细地址
    private EditText update_personal_info_extra_from_income_company_address2_tv; //额外-工资-门牌号
    private TextView update_personal_extra_info_work_position_tv;                //额外-工资-职务
    private EditText update_personal_info_extra_from_income_work_phone_num_edt;  //额外-工资-单位座机
    private TextView update_personal_info_house_type_tv;                     //房屋性质
    private EditText update_personal_info_house_area_edt;                    //房屋面积
    private EditText update_personal_info_house_owner_name_edt;              //房屋所有人
    private TextView update_personal_info_house_owner_relation_tv;           //与申请人关系
    private TextView update_personal_info_urg_relation1_tv;           //紧急联系人-与申请人关系1
    private EditText update_personal_info_urg_mobile1_edt;            //紧急联系人-手机号1
    private EditText update_personal_info_urg_contact1_edt;           //紧急联系人-姓名1
    private TextView update_personal_info_urg_relation2_tv;           //紧急联系人-与申请人关系2
    private EditText update_personal_info_urg_mobile2_edt;            //紧急联系人-手机号2
    private EditText update_personal_info_urg_contact2_edt;           //紧急联系人-姓名2
    private ClientInfo clientInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_personal_info, container, false);

        //初始化
        update_personal_info_clt_nm_edt = (EditText) view.findViewById(R.id.update_personal_info_clt_nm_edt);
        update_personal_info_id_no_edt = (EditText) view.findViewById(R.id.update_personal_info_id_no_edt);
        update_personal_info_gender_tv = (TextView) view.findViewById(R.id.update_personal_info_gender_tv);
        update_personal_info_reg_tv = (TextView) view.findViewById(R.id.update_personal_info_reg_tv);
        update_personal_info_mobile_edt = (EditText) view.findViewById(R.id.update_personal_info_mobile_edt);
        update_personal_info_education_tv = (TextView) view.findViewById(R.id.update_personal_info_education_tv);
        update_personal_info_current_address_tv = (TextView) view.findViewById(R.id.update_personal_info_current_address_tv);
        update_personal_info_current_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_current_address1_tv);
        update_personal_info_current_address2_tv = (EditText) view.findViewById(R.id.update_personal_info_current_address2_tv);
        update_personal_info_income_from_tv = (TextView) view.findViewById(R.id.update_personal_info_income_from_tv);
        update_personal_info_from_income_year_edt = (EditText) view.findViewById(R.id.update_personal_info_from_income_year_edt);
        update_personal_info_from_income_company_name_edt = (EditText) view.findViewById(R.id.update_personal_info_from_income_company_name_edt);
        update_personal_info_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_personal_info_from_income_company_address_tv);
        update_personal_info_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_from_income_company_address1_tv);
        update_personal_info_from_income_company_address2_tv = (EditText) view.findViewById(R.id.update_personal_info_from_income_company_address2_tv);
        update_personal_info_work_position_tv = (TextView) view.findViewById(R.id.update_personal_info_work_position_tv);
        update_personal_info_from_income_work_phone_num_edt = (EditText) view.findViewById(R.id.update_personal_info_from_income_work_phone_num_edt);
        update_personal_info_from_self_year_edt = (EditText) view.findViewById(R.id.update_personal_info_from_self_year_edt);
        update_personal_info_from_self_type_tv = (TextView) view.findViewById(R.id.update_personal_info_from_self_type_tv);
        update_personal_info_from_self_company_name_edt = (EditText) view.findViewById(R.id.update_personal_info_from_self_company_name_edt);
        update_personal_info_from_self_company_address_tv = (TextView) view.findViewById(R.id.update_personal_info_from_self_company_address_tv);
        update_personal_info_from_self_company_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_from_self_company_address1_tv);
        update_personal_info_from_self_company_address2_tv = (EditText) view.findViewById(R.id.update_personal_info_from_self_company_address2_tv);
        update_personal_info_from_other_year_edt = (EditText) view.findViewById(R.id.update_personal_info_from_other_year_edt);
        update_personal_info_from_other_remark_tv = (EditText) view.findViewById(R.id.update_personal_info_from_other_remark_tv);
        update_personal_info_extra_income_from_tv = (TextView) view.findViewById(R.id.update_personal_info_extra_income_from_tv);
        update_personal_info_extra_from_income_year_edt = (EditText) view.findViewById(R.id.update_personal_info_extra_from_income_year_edt);
        update_personal_info_extra_from_income_company_name_edt = (EditText) view.findViewById(R.id.update_personal_info_extra_from_income_company_name_edt);
        update_personal_info_extra_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_personal_info_extra_from_income_company_address_tv);
        update_personal_info_extra_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_extra_from_income_company_address1_tv);
        update_personal_info_extra_from_income_company_address2_tv = (EditText) view.findViewById(R.id.update_personal_info_extra_from_income_company_address2_tv);
        update_personal_extra_info_work_position_tv = (TextView) view.findViewById(R.id.update_personal_extra_info_work_position_tv);
        update_personal_info_extra_from_income_work_phone_num_edt = (EditText) view.findViewById(R.id.update_personal_info_extra_from_income_work_phone_num_edt);
        update_personal_info_house_type_tv = (TextView) view.findViewById(R.id.update_personal_info_house_type_tv);
        update_personal_info_house_area_edt = (EditText) view.findViewById(R.id.update_personal_info_house_area_edt);
        update_personal_info_house_owner_name_edt = (EditText) view.findViewById(R.id.update_personal_info_house_owner_name_edt);
        update_personal_info_house_owner_relation_tv = (TextView) view.findViewById(R.id.update_personal_info_house_owner_relation_tv);
        update_personal_info_urg_relation1_tv = (TextView) view.findViewById(R.id.update_personal_info_urg_relation1_tv);
        update_personal_info_urg_mobile1_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_mobile1_edt);
        update_personal_info_urg_contact1_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_contact1_edt);
        update_personal_info_urg_relation2_tv = (TextView) view.findViewById(R.id.update_personal_info_urg_relation2_tv);
        update_personal_info_urg_mobile2_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_mobile2_edt);
        update_personal_info_urg_contact2_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_contact2_edt);

//        initCommit();

        mScrollView = ((NestedScrollView) view.findViewById(R.id.scrollView));

        //回到顶部按钮
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.smoothScrollTo(0, 0);
            }
        });

        update_personal_info_from_income_group_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_income_group_lin);
        update_personal_info_from_self_group_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_self_group_lin);
        update_personal_info_from_other_group_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_other_group_lin);

        //选择收入来源
        income_from_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_income_from_lin);
        income_from_tv = (TextView) view.findViewById(R.id.update_personal_info_income_from_tv);
        income_from_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(incomelist, UPDATE_INCOME_FROME_INDEX,
                        income_from_lin,
                        income_from_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_INCOME_FROME_INDEX = selectedIndex;

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "工资") {
                                    view.findViewById(R.id.update_personal_info_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_personal_info_from_income_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "自营") {
                                    view.findViewById(R.id.update_personal_info_from_self_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_personal_info_from_self_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "其他") {
                                    view.findViewById(R.id.update_personal_info_from_other_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_personal_info_from_other_group_lin).setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        //工资 公司地址
        update_personal_info_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_income_company_address_lin);
        update_personal_info_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_personal_info_from_income_company_address_tv);
        update_personal_info_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_from_income_company_address_lin,
                        update_personal_info_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_personal_info_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //工资 详细地址 ????

        update_personal_info_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_income_company_address1_lin);
        update_personal_info_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_from_income_company_address1_tv);
        update_personal_info_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_personal_info_from_income_company_address_tv != null) {
                    update_personal_info_from_income_company_address1_tv.setEnabled(true);
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_from_income_company_address1_lin.getId();
                    requestPOI(update_personal_info_from_income_company_address_tv.getText().toString());
                } else {
                    update_personal_info_from_income_company_address1_tv.setEnabled(false);
                }
            }
        });

        // 工资 选择职务
        update_personal_info_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_income_work_position_lin);
        update_personal_info_work_position_tv = (TextView) view.findViewById(R.id.update_personal_info_work_position_tv);
        update_personal_info_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                        update_personal_info_from_income_work_position_lin,
                        update_personal_info_work_position_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_FROM_INCOME_WORK_POSITION_INDEX = selectedIndex;
                            }
                        });
            }
        });

        //自营 业务类型
        update_personal_info_from_self_type_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_self_type_lin);
        update_personal_info_from_self_type_tv = (TextView) view.findViewById(R.id.update_personal_info_from_self_type_tv);
        update_personal_info_from_self_type_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.busi_type_list_key,
                        UPDATE_FROM_SELF_TYPE_INDEX,
                        update_personal_info_from_self_type_lin,
                        update_personal_info_from_self_type_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_FROM_SELF_TYPE_INDEX = selectedIndex;
                                if (YusionApp.CONFIG_RESP.busi_type_list_value.get(UPDATE_FROM_SELF_TYPE_INDEX).equals("其他")) {
                                    EditText editText = new EditText(mContext);
                                    new AlertDialog.Builder(mContext)
                                            .setTitle("请输入业务类型")
                                            .setView(editText)
                                            .setCancelable(false)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    update_personal_info_from_self_type_tv.setText(editText.getText().toString());
                                                    UPDATE_FROM_SELF_TYPE_INDEX = 0;
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
                                }

                            }
                        }
                );
            }
        });


        //自营 单位地址
        update_personal_info_from_self_company_address_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_self_company_address_lin);
        update_personal_info_from_self_company_address_tv = (TextView) view.findViewById(R.id.update_personal_info_from_self_company_address_tv);
        update_personal_info_from_self_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_from_self_company_address_lin,
                        update_personal_info_from_self_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_personal_info_from_self_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });


        //自营 详细地址 ????

        update_personal_info_from_self_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_from_self_company_address1_lin);
        update_personal_info_from_self_company_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_from_self_company_address1_tv);
        update_personal_info_from_self_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_personal_info_from_self_company_address_tv != null) {
                    update_personal_info_from_self_company_address1_tv.setEnabled(true);
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_from_self_company_address1_lin.getId();
                    requestPOI(update_personal_info_from_self_company_address_tv.getText().toString());
                } else {
                    update_personal_info_from_self_company_address1_tv.setEnabled(false);
                }
            }
        });

        //其他 备注


        //额外 公司地址
        update_personal_info_extra_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_extra_from_income_company_address_lin);
        update_personal_info_extra_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_personal_info_extra_from_income_company_address_tv);
        update_personal_info_extra_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_extra_from_income_company_address_lin,
                        update_personal_info_extra_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_personal_info_extra_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });
        update_personal_info_extra_from_income_group_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_extra_from_income_group_lin);
        //选择额外收入来源
        income_extra_from_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) view.findViewById(R.id.update_personal_info_extra_income_from_tv);
        income_extra_from_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(incomeextarlist,
                        UPDATE_EXTRA_INCOME_FROME_INDEX,
                        income_extra_from_lin,
                        income_extra_from_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_EXTRA_INCOME_FROME_INDEX = selectedIndex;
                                if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX) == "工资") {
                                    view.findViewById(R.id.update_personal_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_personal_info_extra_from_income_group_lin).setVisibility(View.GONE);
                                }
                            }
                        }
                );
            }
        });


        //额外 详细地址 ????

        update_personal_info_extra_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_extra_from_income_company_address1_lin);
        update_personal_info_extra_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_extra_from_income_company_address1_tv);
        update_personal_info_extra_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_personal_info_extra_from_income_company_address_tv != null) {
                    update_personal_info_extra_from_income_company_address1_tv.setEnabled(true);
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_extra_from_income_company_address1_lin.getId();
                    requestPOI(update_personal_info_extra_from_income_company_address_tv.getText().toString());
                } else {
                    update_personal_info_extra_from_income_company_address1_tv.setEnabled(false);
                }
            }
        });

        // 额外 选择职务
        update_personal_info_extra_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_extra_from_income_work_position_lin);
        update_personal_info_extra_from_income_work_position_tv = (TextView) view.findViewById(R.id.update_personal_extra_info_work_position_tv);
        update_personal_info_extra_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_EXTRA_WORK_POSITION_INDEX,
                        update_personal_info_extra_from_income_work_position_lin,
                        update_personal_info_extra_from_income_work_position_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex;
                            }
                        });
            }
        });


        //选择性别
        update_personal_info_gender_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_gender_lin);
        update_personal_info_gender_tv = (TextView) view.findViewById(R.id.update_personal_info_gender_tv);
        update_personal_info_gender_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.gender_list_key,
                        UPDATE_SEX_INDEX,
                        update_personal_info_gender_lin,
                        update_personal_info_gender_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_SEX_INDEX = selectedIndex;
                            }
                        }
                );
            }
        });

        //选择户籍地
        update_personal_info_reg_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_reg_lin);
        update_personal_info_reg_tv = (TextView) view.findViewById(R.id.update_personal_info_reg_tv);
        update_personal_info_reg_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_reg_lin,
                        update_personal_info_reg_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {

                            }
                        }
                );
            }
        });


        //选择学历
        update_personal_info_education_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_education_lin);
        update_personal_info_education_tv = (TextView) view.findViewById(R.id.update_personal_info_education_tv);
        update_personal_info_education_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.education_list_key,
                        UPDATE_EDUCATION_INDEX,
                        update_personal_info_education_lin,
                        update_personal_info_education_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_EDUCATION_INDEX = selectedIndex;
                            }
                        });
            }
        });

        //现在地址
        update_personal_info_current_address_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_current_address_lin);
        update_personal_info_current_address_tv = (TextView) view.findViewById(R.id.update_personal_info_current_address_tv);
        update_personal_info_current_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_personal_info_current_address_lin,
                        update_personal_info_current_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_personal_info_current_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //详细地址  ？？？？？？？？？？？
        update_personal_info_current_address1_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_current_address1_lin);
        update_personal_info_current_address1_tv = (TextView) view.findViewById(R.id.update_personal_info_current_address1_tv);
        update_personal_info_current_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!update_personal_info_current_address_tv.getText().toString().isEmpty()) {
                    update_personal_info_current_address1_tv.setEnabled(true);
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_personal_info_current_address1_lin.getId();
                    requestPOI(update_personal_info_current_address_tv.getText().toString());
                } else {
                    update_personal_info_current_address1_tv.setEnabled(false);
                }
            }
        });

        //房屋类型
        update_personal_info_house_type_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_house_type_lin);
        update_personal_info_house_type_tv = (TextView) view.findViewById(R.id.update_personal_info_house_type_tv);
        update_personal_info_house_type_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_type_list_key,
                        UPDATE_HOUSE_TYPE_INDEX,
                        update_personal_info_house_type_lin,
                        update_personal_info_house_type_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_HOUSE_TYPE_INDEX = selectedIndex;
                            }
                        });
            }
        });

        //与申请人关系
        update_personal_info_house_owner_relation_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_house_owner_relation_lin);
        update_personal_info_house_owner_relation_tv = (TextView) view.findViewById(R.id.update_personal_info_house_owner_relation_tv);
        update_personal_info_house_owner_relation_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_relationship_list_key,
                        UPDATE_HOUSE_OWNER_RELATION_INDEX,
                        update_personal_info_house_owner_relation_lin,
                        update_personal_info_house_owner_relation_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_HOUSE_OWNER_RELATION_INDEX = selectedIndex;
                            }
                        });
            }
        });

        update_personal_info_urg_relation2_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_urg_relation2_lin);
        update_personal_info_urg_relation2_tv = (TextView) view.findViewById(R.id.update_personal_info_urg_relation2_tv);
        update_personal_info_urg_relation2_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.urg_other_relationship_list_key,
                        UPDATE_URG_RELATION_INDEX2,
                        update_personal_info_urg_relation2_lin,
                        update_personal_info_urg_relation2_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_URG_RELATION_INDEX2 = selectedIndex;
                            }
                        });
            }
        });

        update_personal_info_urg_relation1_lin = (LinearLayout) view.findViewById(R.id.update_personal_info_urg_relation1_lin);
        update_personal_info_urg_relation1_tv = (TextView) view.findViewById(R.id.update_personal_info_urg_relation1_tv);
        update_personal_info_urg_relation1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.urg_rela_relationship_list_key,
                        UPDATE_URG_RELATION_INDEX1,
                        update_personal_info_urg_relation1_lin,
                        update_personal_info_urg_relation1_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_URG_RELATION_INDEX1 = selectedIndex;
                            }
                        });
            }
        });


        update_personal_info_urg_mobile1_img = (ImageView) view.findViewById(R.id.update_personal_info_urg_mobile1_img);
        update_personal_info_urg_mobile1_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_CLICKED_VIEW_FOR_CONTACT = update_personal_info_urg_mobile1_img.getId();
                selectContact();
            }
        });


        update_personal_info_urg_mobile2_img = (ImageView) view.findViewById(R.id.update_personal_info_urg_mobile2_img);
        update_personal_info_urg_mobile2_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_CLICKED_VIEW_FOR_CONTACT = update_personal_info_urg_mobile2_img.getId();
                selectContact();
            }
        });

        update_personal_info_urg_mobile1_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_mobile1_edt);
        update_personal_info_urg_mobile2_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_mobile2_edt);
        update_personal_info_urg_contact1_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_contact1_edt);
        update_personal_info_urg_contact2_edt = (EditText) view.findViewById(R.id.update_personal_info_urg_contact2_edt);

        return view;

    }

//    private void initCommit() {
//        UpdatePersonalInfoActivity updatePersonalInfoActivity = (UpdatePersonalInfoActivity) getActivity();
//        updateClientinfo(data);
//        updatePersonalInfoActivity.commit();
//    }


    //获取用户信息
    public void getClientinfo(ClientInfo data) {
        if (data != null) {
            clientInfo = data;
            //填充
            update_personal_info_clt_nm_edt.setText(clientInfo.clt_nm);
            update_personal_info_id_no_edt.setText(clientInfo.id_no);
            update_personal_info_gender_tv.setText(clientInfo.gender);
            update_personal_info_reg_tv.setText(clientInfo.reg_addr.province + "/" + clientInfo.reg_addr.city + "/" + clientInfo.reg_addr.district);
            update_personal_info_mobile_edt.setText(clientInfo.mobile);
            update_personal_info_education_tv.setText(clientInfo.edu);
            update_personal_info_current_address_tv.setText(clientInfo.current_addr.province + "/" + clientInfo.current_addr.city + "/" + clientInfo.current_addr.district);
            update_personal_info_current_address1_tv.setText(clientInfo.current_addr.address1);
            update_personal_info_current_address2_tv.setText(clientInfo.current_addr.address2);
            update_personal_info_income_from_tv.setText(clientInfo.major_income_type);
            //判断主要收入类型
            switch (clientInfo.major_income_type) {
                case "工资":
                    update_personal_info_from_income_group_lin.setVisibility(View.VISIBLE);
                    update_personal_info_from_income_year_edt.setText(clientInfo.major_income);
                    update_personal_info_from_income_company_name_edt.setText(clientInfo.major_company_name);
                    update_personal_info_from_income_company_address_tv.setText(clientInfo.major_company_addr.province + "/" + clientInfo.major_company_addr.city + "/" + clientInfo.major_company_addr.district);
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
                    update_personal_info_from_self_company_address_tv.setText(clientInfo.major_company_addr.province + "/" + clientInfo.major_company_addr.city + "/" + clientInfo.major_company_addr.district);
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
                    update_personal_info_extra_from_income_company_address_tv.setText(clientInfo.extra_company_addr.province + "/" + clientInfo.extra_company_addr.city + "/" + clientInfo.extra_company_addr.district);
                    update_personal_info_extra_from_income_company_address1_tv.setText(clientInfo.extra_company_addr.address1);
                    update_personal_info_extra_from_income_company_address2_tv.setText(clientInfo.extra_company_addr.address2);
                    update_personal_extra_info_work_position_tv.setText(clientInfo.extra_work_position);
                    update_personal_info_extra_from_income_work_phone_num_edt.setText(clientInfo.extra_work_phone_num);
                    break;
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
        }

    }


    //提交用户信息
    public boolean updateClientinfo(OnVoidCallBack callBack) {
        //校验
        if (checkUserInfo()) {
            if (checkIncome()) {

                //提交
                clientInfo.clt_nm = update_personal_info_clt_nm_edt.getText().toString();
                clientInfo.id_no = update_personal_info_id_no_edt.getText().toString();
                clientInfo.gender = update_personal_info_gender_tv.getText().toString();


                clientInfo.reg_addr.province = update_personal_info_reg_tv.getText().toString().split("/")[0];
                clientInfo.reg_addr.city = update_personal_info_reg_tv.getText().toString().split("/")[1];
                clientInfo.reg_addr.district = update_personal_info_reg_tv.getText().toString().split("/")[2];

                clientInfo.mobile = update_personal_info_mobile_edt.getText().toString();
                clientInfo.edu = update_personal_info_education_tv.getText().toString();


                clientInfo.current_addr.province = update_personal_info_current_address_tv.getText().toString().split("/")[0];
                clientInfo.current_addr.city = update_personal_info_current_address_tv.getText().toString().split("/")[1];
                clientInfo.current_addr.district = update_personal_info_current_address_tv.getText().toString().split("/")[2];
                clientInfo.current_addr.address1 = update_personal_info_current_address1_tv.getText().toString();
                clientInfo.current_addr.address2 = update_personal_info_current_address2_tv.getText().toString();
                clientInfo.major_income_type = update_personal_info_income_from_tv.getText().toString();


                //判断主要收入类型
                Log.e("主要收入类型", "" + update_personal_info_income_from_tv.getText().toString());
                switch (update_personal_info_income_from_tv.getText().toString()) {
                    case "工资":
                        clientInfo.major_income = update_personal_info_from_income_year_edt.getText().toString();
                        clientInfo.major_company_name = update_personal_info_from_income_company_name_edt.getText().toString();
                        clientInfo.major_company_addr.province = update_personal_info_from_income_company_address_tv.getText().toString().split("/")[0];
                        clientInfo.major_company_addr.city = update_personal_info_from_income_company_address_tv.getText().toString().split("/")[1];
                        clientInfo.major_company_addr.district = update_personal_info_from_income_company_address_tv.getText().toString().split("/")[2];
                        clientInfo.major_company_addr.address1 = update_personal_info_from_income_company_address1_tv.getText().toString();
                        clientInfo.major_company_addr.address2 = update_personal_info_from_income_company_address2_tv.getText().toString();
                        clientInfo.major_work_position = update_personal_info_work_position_tv.getText().toString();
                        clientInfo.major_work_phone_num = update_personal_info_from_income_work_phone_num_edt.getText().toString();
                        break;
                    case "自营":

                        clientInfo.major_income = update_personal_info_from_self_year_edt.getText().toString();
                        clientInfo.major_busi_type = update_personal_info_from_self_type_tv.getText().toString();
                        clientInfo.major_company_name = update_personal_info_from_self_company_name_edt.getText().toString();
                        clientInfo.major_company_addr.province = update_personal_info_from_self_company_address_tv.getText().toString().split("/")[0];
                        clientInfo.major_company_addr.city = update_personal_info_from_self_company_address_tv.getText().toString().split("/")[1];
                        clientInfo.major_company_addr.district = update_personal_info_from_self_company_address_tv.getText().toString().split("/")[2];
                        clientInfo.major_company_addr.address1 = update_personal_info_from_self_company_address1_tv.getText().toString();
                        clientInfo.major_company_addr.address2 = update_personal_info_from_self_company_address2_tv.getText().toString();
                        break;
                    case "其他":
                        clientInfo.major_income = update_personal_info_from_other_year_edt.getText().toString();
                        clientInfo.major_remark = update_personal_info_from_other_remark_tv.getText().toString();
                        break;
                }
                clientInfo.extra_income_type = update_personal_info_extra_income_from_tv.getText().toString();
                //判断额外收入类型
                switch (update_personal_info_extra_income_from_tv.getText().toString()) {
                    case "工资":
                        clientInfo.extra_income = update_personal_info_extra_from_income_year_edt.getText().toString();
                        clientInfo.extra_company_name = update_personal_info_extra_from_income_company_name_edt.getText().toString();
                        clientInfo.extra_company_addr.province = update_personal_info_extra_from_income_company_address_tv.getText().toString().split("/")[0];
                        clientInfo.extra_company_addr.city = update_personal_info_extra_from_income_company_address_tv.getText().toString().split("/")[1];
                        clientInfo.extra_company_addr.district = update_personal_info_extra_from_income_company_address_tv.getText().toString().split("/")[2];
                        clientInfo.extra_company_addr.address1 = update_personal_info_extra_from_income_company_address1_tv.getText().toString();
                        clientInfo.extra_company_addr.address2 = update_personal_info_extra_from_income_company_address2_tv.getText().toString();
                        clientInfo.extra_work_position = update_personal_extra_info_work_position_tv.getText().toString();
                        clientInfo.extra_work_phone_num = update_personal_info_extra_from_income_work_phone_num_edt.getText().toString();
                        break;
                }
                clientInfo.house_type = update_personal_info_house_type_tv.getText().toString();
                clientInfo.house_area = update_personal_info_house_area_edt.getText().toString();
                clientInfo.house_owner_name = update_personal_info_house_owner_name_edt.getText().toString();
                clientInfo.house_owner_relation = update_personal_info_house_owner_relation_tv.getText().toString();
                clientInfo.urg_relation1 = update_personal_info_urg_relation1_tv.getText().toString();
                clientInfo.urg_mobile1 = update_personal_info_urg_mobile1_edt.getText().toString();
                clientInfo.urg_contact1 = update_personal_info_urg_contact1_edt.getText().toString();
                clientInfo.urg_relation2 = update_personal_info_urg_relation2_tv.getText().toString();
                clientInfo.urg_mobile2 = update_personal_info_urg_mobile2_edt.getText().toString();
                clientInfo.urg_contact2 = update_personal_info_urg_contact2_edt.getText().toString();

                callBack.callBack();
                return true;
            }
        }
        return false;
    }

    private boolean checkIncome() {
        //主要工资
        if (update_personal_info_income_from_tv.getText().toString().equals("工资")) {
            if (update_personal_info_from_income_company_name_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_income_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_income_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_income_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_work_position_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_income_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "自营年收入不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        //主要自营
        else if (update_personal_info_income_from_tv.getText().toString().equals("自营")) {
            if (update_personal_info_from_self_company_name_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "店铺名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_self_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_self_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_self_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_self_type_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_self_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "自营年收入不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        //主要其他
        else if (update_personal_info_income_from_tv.getText().toString().equals("其他")) {
            if (update_personal_info_from_other_remark_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "备注不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_personal_info_from_other_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "其他年收入不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        return false;

    }

    private boolean checkUserInfo() {
        if (update_personal_info_reg_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "户籍地不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_gender_tv.toString().isEmpty()) {
            Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_mobile_edt.toString().isEmpty()) {
            Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_education_tv.toString().isEmpty()) {
            Toast.makeText(mContext, "学历不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_current_address_tv.toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_current_address1_tv.toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_current_address2_tv.toString().isEmpty()) {
            Toast.makeText(mContext, "现住地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_type_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋性质不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_area_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋面积不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_owner_name_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_house_owner_relation_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "房屋所有权人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_contact1_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "亲属联系人姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_mobile1_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "亲属联系人手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_relation1_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "亲属联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_contact2_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "其他联系人姓名不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_mobile2_edt.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "其他联系人手机号不能为空", Toast.LENGTH_SHORT).show();
        } else if (update_personal_info_urg_relation2_tv.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "其他联系人与申请人关系不能为空", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }


    private void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_CONTACTS);
    }

    private void requestPOI(String city) {
        if (city != null) {
            String[] citys = city.split("/");
            if (citys.length == 3) {
                String city1 = citys[1];
                String city2 = citys[2];
                Intent intent = new Intent(mContext, AMapPoiListActivity.class);
                intent.putExtra("city", city1);
                intent.putExtra("keywords", city2);
                startActivityForResult(intent, Constants.REQUEST_ADDRESS);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_CONTACTS) {
                Uri uri = data.getData();
                String[] contacts = ContactsUtil.getPhoneContacts(mContext, uri);
                String[] result = new String[]{"", ""};
                if (contacts != null) {
                    System.arraycopy(contacts, 0, result, 0, contacts.length);
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_personal_info_urg_mobile1_img.getId()) {

                    update_personal_info_urg_contact1_edt.setText(result[0]);
                    update_personal_info_urg_mobile1_edt.setText(result[1]);
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_personal_info_urg_mobile2_img.getId()) {

                    update_personal_info_urg_contact2_edt.setText(result[0]);
                    update_personal_info_urg_mobile2_edt.setText(result[1]);
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

}

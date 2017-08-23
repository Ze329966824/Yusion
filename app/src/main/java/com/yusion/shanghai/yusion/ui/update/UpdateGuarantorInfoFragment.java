package com.yusion.shanghai.yusion.ui.update;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.utils.ContactsUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;

import java.util.ArrayList;
import java.util.List;


public class UpdateGuarantorInfoFragment extends BaseFragment {

    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
        add("其他");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
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

    private LinearLayout update_guarantor_info_gender_lin;
    private TextView update_guarantor_info_gender_tv;

    private LinearLayout update_guarantor_info_reg_lin;
    private TextView update_guarantor_info_reg_tv;

    private LinearLayout update_guarantor_info_education_lin;
    private TextView update_guarantor_info_education_tv;

    private LinearLayout update_guarantor_info_current_address_lin;
    private TextView update_guarantor_info_current_address_tv;

    private LinearLayout update_guarantor_info_current_address1_lin;
    private TextView update_guarantor_info_current_address1_tv;

    private LinearLayout update_guarantor_info_house_type_lin;
    private TextView update_guarantor_info_house_type_tv;

    private LinearLayout update_guarantor_info_house_owner_relation_lin;
    private TextView update_guarantor_info_house_owner_relation_tv;

    private LinearLayout update_guarantor_info_urg_relation1_lin;
    private TextView update_guarantor_info_urg_relation1_tv;

    private LinearLayout update_guarantor_info_urg_relation2_lin;
    private TextView update_guarantor_info_urg_relation2_tv;

    private LinearLayout update_guarantor_info_from_income_company_address_lin;
    private TextView update_guarantor_info_from_income_company_address_tv;

    private LinearLayout update_guarantor_info_from_income_company_address1_lin;
    private TextView update_guarantor_info_from_income_company_address1_tv;

    private LinearLayout update_guarantor_info_from_income_work_position_lin;
    private TextView update_guarantor_info_work_position_tv;

    private LinearLayout update_guarantor_info_extra_from_income_company_address1_lin;
    private TextView update_guarantor_info_extra_from_income_company_address1_tv;

    private LinearLayout update_guarantor_info_extra_from_income_work_position_lin;
    private TextView update_guarantor_info_extra_from_income_work_position_tv;

    private LinearLayout update_guarantor_info_extra_from_income_company_address_lin;
    private TextView update_guarantor_info_extra_from_income_company_address_tv;

    private LinearLayout update_guarantor_info_from_self_company_address_lin;
    private TextView update_guarantor_info_from_self_company_address_tv;

    private LinearLayout update_guarantor_info_from_self_company_address1_lin;
    private TextView update_guarantor_info_from_self_company_address1_tv;

    private ImageView update_guarantor_info_urg_mobile1_img;
    private ImageView update_guarantor_info_urg_mobile2_img;

    private EditText update_guarantor_info_urg_mobile1_edt;
    private EditText update_guarantor_info_urg_contact1_edt;
    private EditText update_guarantor_info_urg_mobile2_edt;
    private EditText update_guarantor_info_urg_contact2_edt;

    private LinearLayout update_guarantor_info_from_self_type_lin;
    private TextView update_guarantor_info_from_self_type_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_guarantor_info, container, false);
        //选择收入来源
        income_from_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_income_from_lin);
        income_from_tv = (TextView) view.findViewById(R.id.update_guarantor_info_income_from_tv);
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
                                    view.findViewById(R.id.update_guarantor_info_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_info_from_income_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "自营") {
                                    view.findViewById(R.id.update_guarantor_info_from_self_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_info_from_self_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "其他") {
                                    view.findViewById(R.id.update_guarantor_info_from_other_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_info_from_other_group_lin).setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        //工资 公司地址
        update_guarantor_info_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_from_income_company_address_lin);
        update_guarantor_info_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_info_from_income_company_address_tv);
        update_guarantor_info_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_from_income_company_address_lin,
                        update_guarantor_info_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_guarantor_info_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //工资 详细地址 ????

        update_guarantor_info_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_from_income_company_address1_lin);
        update_guarantor_info_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_info_from_income_company_address1_tv);
        update_guarantor_info_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_guarantor_info_from_income_company_address_tv != null) {
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_from_income_company_address1_lin.getId();
                    requestPOI(update_guarantor_info_from_income_company_address_tv.getText().toString());
                }
            }
        });

        // 工资 选择职务
        update_guarantor_info_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_from_income_work_position_lin);
        update_guarantor_info_work_position_tv = (TextView) view.findViewById(R.id.update_guarantor_info_work_position_tv);
        update_guarantor_info_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                        update_guarantor_info_from_income_work_position_lin,
                        update_guarantor_info_work_position_tv,
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
        update_guarantor_info_from_self_type_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_from_self_type_lin);
        update_guarantor_info_from_self_type_tv = (TextView) view.findViewById(R.id.update_guarantor_info_from_self_type_tv);
        update_guarantor_info_from_self_type_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.busi_type_list_key,
                        UPDATE_FROM_SELF_TYPE_INDEX,
                        update_guarantor_info_from_self_type_lin,
                        update_guarantor_info_from_self_type_tv,
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
                                                    update_guarantor_info_from_self_type_tv.setText(editText.getText());
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
        update_guarantor_info_from_self_company_address_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_from_self_company_address_lin);
        update_guarantor_info_from_self_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_info_from_self_company_address_tv);
        update_guarantor_info_from_self_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_from_self_company_address_lin,
                        update_guarantor_info_from_self_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_guarantor_info_from_self_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });


        //自营 详细地址 ????

        update_guarantor_info_from_self_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_from_self_company_address1_lin);
        update_guarantor_info_from_self_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_info_from_self_company_address1_tv);
        update_guarantor_info_from_self_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_guarantor_info_from_self_company_address_tv != null) {
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_from_self_company_address1_lin.getId();
                    requestPOI(update_guarantor_info_from_self_company_address_tv.getText().toString());
                }
            }
        });

        //其他 备注


        //额外 公司地址
        update_guarantor_info_extra_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_extra_from_income_company_address_lin);
        update_guarantor_info_extra_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_info_extra_from_income_company_address_tv);
        update_guarantor_info_extra_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_extra_from_income_company_address_lin,
                        update_guarantor_info_extra_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_guarantor_info_extra_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //选择额外收入来源
        income_extra_from_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) view.findViewById(R.id.update_guarantor_info_extra_income_from_tv);
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
                                    view.findViewById(R.id.update_guarantor_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_info_extra_from_income_group_lin).setVisibility(View.GONE);
                                }
                            }
                        }
                );
            }
        });


        //额外 详细地址 ????

        update_guarantor_info_extra_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_extra_from_income_company_address1_lin);
        update_guarantor_info_extra_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_info_extra_from_income_company_address1_tv);
        update_guarantor_info_extra_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_guarantor_info_extra_from_income_company_address_tv != null) {
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_extra_from_income_company_address1_lin.getId();
                }
                requestPOI(update_guarantor_info_extra_from_income_company_address_tv.getText().toString());
            }
        });

        // 额外 选择职务
        update_guarantor_info_extra_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_extra_from_income_work_position_lin);
        update_guarantor_info_extra_from_income_work_position_tv = (TextView) view.findViewById(R.id.update_guarantor_extra_info_work_position_tv);
        update_guarantor_info_extra_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_EXTRA_WORK_POSITION_INDEX,
                        update_guarantor_info_extra_from_income_work_position_lin,
                        update_guarantor_info_extra_from_income_work_position_tv,
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
        update_guarantor_info_gender_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_gender_lin);
        update_guarantor_info_gender_tv = (TextView) view.findViewById(R.id.update_guarantor_info_gender_tv);
        update_guarantor_info_gender_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.gender_list_key,
                        UPDATE_SEX_INDEX,
                        update_guarantor_info_gender_lin,
                        update_guarantor_info_gender_tv,
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
        update_guarantor_info_reg_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_reg_lin);
        update_guarantor_info_reg_tv = (TextView) view.findViewById(R.id.update_guarantor_info_reg_tv);
        update_guarantor_info_reg_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_reg_lin,
                        update_guarantor_info_reg_tv,
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
        update_guarantor_info_education_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_education_lin);
        update_guarantor_info_education_tv = (TextView) view.findViewById(R.id.update_guarantor_info_education_tv);
        update_guarantor_info_education_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.education_list_key,
                        UPDATE_EDUCATION_INDEX,
                        update_guarantor_info_education_lin,
                        update_guarantor_info_education_tv,
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
        update_guarantor_info_current_address_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_current_address_lin);
        update_guarantor_info_current_address_tv = (TextView) view.findViewById(R.id.update_guarantor_info_current_address_tv);
        update_guarantor_info_current_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_info_current_address_lin,
                        update_guarantor_info_current_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_guarantor_info_current_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //详细地址  ？？？？？？？？？？？
        update_guarantor_info_current_address1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_current_address1_lin);

        update_guarantor_info_current_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_info_current_address1_tv);
        update_guarantor_info_current_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!update_guarantor_info_current_address_tv.getText().toString().isEmpty()) {
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_info_current_address1_lin.getId();
                    requestPOI(update_guarantor_info_current_address_tv.getText().toString());
                }
            }
        });

        //房屋类型
        update_guarantor_info_house_type_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_house_type_lin);
        update_guarantor_info_house_type_tv = (TextView) view.findViewById(R.id.update_guarantor_info_house_type_tv);
        update_guarantor_info_house_type_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_type_list_key,
                        UPDATE_HOUSE_TYPE_INDEX,
                        update_guarantor_info_house_type_lin,
                        update_guarantor_info_house_type_tv,
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
        update_guarantor_info_house_owner_relation_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_house_owner_relation_lin);
        update_guarantor_info_house_owner_relation_tv = (TextView) view.findViewById(R.id.update_guarantor_info_house_owner_relation_tv);
        update_guarantor_info_house_owner_relation_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.house_relationship_list_key,
                        UPDATE_HOUSE_OWNER_RELATION_INDEX,
                        update_guarantor_info_house_owner_relation_lin,
                        update_guarantor_info_house_owner_relation_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_HOUSE_OWNER_RELATION_INDEX = selectedIndex;
                            }
                        });
            }
        });

        update_guarantor_info_urg_relation2_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_urg_relation2_lin);
        update_guarantor_info_urg_relation2_tv = (TextView) view.findViewById(R.id.update_guarantor_info_urg_relation2_tv);
        update_guarantor_info_urg_relation2_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.urg_other_relationship_list_key,
                        UPDATE_URG_RELATION_INDEX2,
                        update_guarantor_info_urg_relation2_lin,
                        update_guarantor_info_urg_relation2_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_URG_RELATION_INDEX2 = selectedIndex;
                            }
                        });
            }
        });

        update_guarantor_info_urg_relation1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_info_urg_relation1_lin);
        update_guarantor_info_urg_relation1_tv = (TextView) view.findViewById(R.id.update_guarantor_info_urg_relation1_tv);
        update_guarantor_info_urg_relation1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.urg_rela_relationship_list_key,
                        UPDATE_URG_RELATION_INDEX1,
                        update_guarantor_info_urg_relation1_lin,
                        update_guarantor_info_urg_relation1_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_URG_RELATION_INDEX1 = selectedIndex;
                            }
                        });
            }
        });


        update_guarantor_info_urg_mobile1_img = (ImageView) view.findViewById(R.id.update_guarantor_info_urg_mobile1_img);
        update_guarantor_info_urg_mobile1_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_CLICKED_VIEW_FOR_CONTACT = update_guarantor_info_urg_mobile1_img.getId();
                selectContact();
            }
        });


        update_guarantor_info_urg_mobile2_img = (ImageView) view.findViewById(R.id.update_guarantor_info_urg_mobile2_img);
        update_guarantor_info_urg_mobile2_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_CLICKED_VIEW_FOR_CONTACT = update_guarantor_info_urg_mobile2_img.getId();
                selectContact();
            }
        });

        update_guarantor_info_urg_mobile1_edt = (EditText) view.findViewById(R.id.update_guarantor_info_urg_mobile1_edt);
        update_guarantor_info_urg_mobile2_edt = (EditText) view.findViewById(R.id.update_guarantor_info_urg_mobile2_edt);
        update_guarantor_info_urg_contact1_edt = (EditText) view.findViewById(R.id.update_guarantor_info_urg_contact1_edt);
        update_guarantor_info_urg_contact2_edt = (EditText) view.findViewById(R.id.update_guarantor_info_urg_contact2_edt);

        return view;

    }


    private void selectContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_CONTACTS);
    }

    private void requestPOI(String city) {
        String[] citys = city.split("/");
        String city1 = citys[1];
        String city2 = citys[2];

        Intent intent = new Intent(mContext, AMapPoiListActivity.class);
        intent.putExtra("city", city1);
        intent.putExtra("keywords", city2);

        startActivityForResult(intent, Constants.REQUEST_ADDRESS);
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
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_guarantor_info_urg_mobile1_img.getId()) {

                    update_guarantor_info_urg_contact1_edt.setText(result[0]);
                    update_guarantor_info_urg_mobile1_edt.setText(result[1]);
                }
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_guarantor_info_urg_mobile2_img.getId()) {

                    update_guarantor_info_urg_contact2_edt.setText(result[0]);
                    update_guarantor_info_urg_mobile2_edt.setText(result[1]);
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {

                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_info_current_address1_lin.getId()) {
                    update_guarantor_info_current_address1_tv.setText(data.getStringExtra("result"));
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
        }
    }
}

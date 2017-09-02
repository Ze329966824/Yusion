package com.yusion.shanghai.yusion.ui.update;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseFragment;
import com.yusion.shanghai.yusion.bean.ocr.OcrResp;
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.ListImgsResp;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.user.GuarantorInfo;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.ui.apply.DocumentActivity;
import com.yusion.shanghai.yusion.ui.info.UploadListActivity;
import com.yusion.shanghai.yusion.utils.CheckIdCardValidUtil;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.ContactsUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion.widget.NoEmptyEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/8/21.
 */

public class UpdateGuarantorSpouseInfoFragment extends BaseFragment {
    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
        add("其他");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
        add("无");
    }};
    public static int START_FOR_DRIVING_SINGLE_IMG_ACTIVITY = 1000;
    public static int UPDATE_INCOME_FROME_INDEX;
    public static int UPDATE_EXTRA_INCOME_FROME_INDEX;
    public static int UPDATE_MARRIAGE_INDEX;
    public static int UPDATE_SEX_INDEX;
    public static int UPDATE_FROM_INCOME_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_EXTRA_WORK_POSITION_INDEX;
    public static int UPDATE_FROM_SELF_TYPE_INDEX;
    public static int CURRENT_CLICKED_VIEW_FOR_ADDRESS = -1;
    public static int CURRENT_CLICKED_VIEW_FOR_CONTACT = -1;
    private LinearLayout income_from_lin;
    private LinearLayout income_extra_from_lin;
    private TextView income_from_tv;
    private TextView income_extra_from_tv;
    private LinearLayout update_guarantor_spouse_info_marriage_lin;
    private TextView update_guarantor_spouse_info_marriage_tv;
    private LinearLayout update_guarantor_spouse_info_gender_lin;
    private LinearLayout update_guarantor_spouse_info_from_income_company_address_lin;
    private LinearLayout update_guarantor_spouse_info_from_income_company_address1_lin;
    private LinearLayout update_guarantor_spouse_info_from_income_work_position_lin;
    private LinearLayout update_guarantor_spouse_info_from_self_company_address_lin;
    private LinearLayout update_guarantor_spouse_info_from_self_company_address1_lin;
    private LinearLayout update_guarantor_spouse_info_extra_from_income_company_address_lin;
    private LinearLayout update_guarantor_spouse_info_extra_from_income_company_address1_lin;
    private LinearLayout update_guarantor_spouse_info_extra_from_income_work_position_lin;
    private TextView update_guarantor_spouse_info_extra_from_income_work_position_tv;
    private ImageView update_guarantor_spouse_info_mobile_img;
    private LinearLayout update_guarantor_spouse_info_divorced_lin;
    private TextView update_guarantor_spouse_info_divorced_tv;
    private LinearLayout update_guarantor_spouse_info_register_addr_lin;
    private TextView update_guarantor_spouse_info_register_addr_tv;
    private LinearLayout update_guarantor_spouse_info_from_self_type_lin;
    private NestedScrollView mScrollView;
    private GuarantorInfo guarantorInfo;
    private LinearLayout update_guarantor_spouse_info_extra_from_income_group_lin;
    private LinearLayout update_guarantor_spouse_info_marriage_group_lin;
    private LinearLayout update_guarantor_spouse_info_divorced_group_lin;
    private LinearLayout update_guarantor_spouse_info_die_group_lin;
    private LinearLayout update_guarantor_spouse_info_from_income_group_lin;
    private LinearLayout update_guarantor_spouse_info_from_self_group_lin;
    private LinearLayout update_guarantor_spouse_info_from_other_group_lin;
    private LinearLayout update_guarantor_spouse_info_extra_income_from_lin;
    private TextView update_guarantor_spouse_info_id_back_tv;
    private TextView update_guarantor_spouse_info_id_front_tv;
    private LinearLayout update_guarantor_spouse_info_id_back_lin;
    private LinearLayout update_guarantor_spouse_info_id_front_lin;


    private NoEmptyEditText update_guarantor_spouse_info_clt_nm_edt;                       //姓名
    private EditText update_guarantor_spouse_info_id_no_edt;                        //身份证号
    private TextView update_guarantor_spouse_info_gender_tv;                        //性别
    private EditText update_guarantor_spouse_info_mobile_edt;                       //手机号
    private TextView update_guarantor_spouse_info_income_from_tv;                   //主要收入来源
    private EditText update_guarantor_spouse_info_from_income_year_edt;             //主要-工资-年收入
    private NoEmptyEditText update_guarantor_spouse_info_from_income_company_name_edt;     //主要-工资-单位名称
    private TextView update_guarantor_spouse_info_from_income_company_address_tv;   //主要-工资-单位地址
    private TextView update_guarantor_spouse_info_from_income_company_address1_tv;  //主要-工资-详细地址
    private NoEmptyEditText update_guarantor_spouse_info_from_income_company_address2_tv;  //主要-工资-门牌号
    private TextView update_guarantor_spouse_info_work_position_tv;                 //主要-工资-职务
    private NoEmptyEditText update_guarantor_spouse_info_from_income_work_phone_num_edt;   //主要-工资-单位座机
    private EditText update_guarantor_spouse_info_from_self_year_edt;               //主要-自营-年收入
    private TextView update_guarantor_spouse_info_from_self_type_tv;                //主要-自营-业务类型
    private NoEmptyEditText update_guarantor_spouse_info_from_self_company_name_edt;       //主要-自营-店铺名称
    private TextView update_guarantor_spouse_info_from_self_company_address_tv;     //主要-自营-单位地址
    private TextView update_guarantor_spouse_info_from_self_company_address1_tv;    //主要-自营-详细地址
    private NoEmptyEditText update_guarantor_spouse_info_from_self_company_address2_tv;    //主要-自营-门牌号
    private EditText update_guarantor_spouse_info_from_other_year_edt;              //主要-其他-年收入
    private NoEmptyEditText update_guarantor_spouse_info_from_other_remark_tv;             //主要-其他-备注
    private TextView update_guarantor_spouse_info_extra_income_from_tv;             //额外收入来源
    private EditText update_guarantor_spouse_info_extra_from_income_year_edt;            //额外-工资-年收入
    private NoEmptyEditText update_guarantor_spouse_info_extra_from_income_company_name_edt;    //额外-工资-单位名称
    private TextView update_guarantor_spouse_info_extra_from_income_company_address_tv;  //额外-工资-公司地址
    private TextView update_guarantor_spouse_info_extra_from_income_company_address1_tv; //额外-工资-详细地址
    private NoEmptyEditText update_guarantor_spouse_info_extra_from_income_company_address2_tv; //额外-工资-门牌号
    private NoEmptyEditText update_guarantor_spouse_info_extra_from_income_work_phone_num_edt;  //额外-工资-单位座机
    private OcrResp.ShowapiResBodyBean ocrResp = new OcrResp.ShowapiResBodyBean();
    private ArrayList divorceImgsList = new ArrayList<UploadImgItemBean>();
    private ArrayList resBookList = new ArrayList<UploadImgItemBean>();
    public static String idBackImgUrl = "";
    public static String idFrontImgUrl = "";
    public static String ID_BACK_FID = "";
    public static String ID_FRONT_FID = "";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_guarantor_spouse_info, container, false);

        //初始化
        update_guarantor_spouse_info_clt_nm_edt = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_clt_nm_edt);
        update_guarantor_spouse_info_id_no_edt = (EditText) view.findViewById(R.id.update_guarantor_spouse_info_id_no_edt);
        update_guarantor_spouse_info_gender_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_gender_tv);
        update_guarantor_spouse_info_mobile_edt = (EditText) view.findViewById(R.id.update_guarantor_spouse_info_mobile_edt);
        update_guarantor_spouse_info_income_from_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_income_from_tv);
        update_guarantor_spouse_info_from_income_year_edt = (EditText) view.findViewById(R.id.update_guarantor_spouse_info_from_income_year_edt);
        update_guarantor_spouse_info_from_income_company_name_edt = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_name_edt);
        update_guarantor_spouse_info_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_address_tv);
        update_guarantor_spouse_info_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_address1_tv);
        update_guarantor_spouse_info_from_income_company_address2_tv = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_address2_tv);
        update_guarantor_spouse_info_work_position_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_work_position_tv);
        update_guarantor_spouse_info_from_income_work_phone_num_edt = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_from_income_work_phone_num_edt);
        update_guarantor_spouse_info_from_self_year_edt = (EditText) view.findViewById(R.id.update_guarantor_spouse_info_from_self_year_edt);
        update_guarantor_spouse_info_from_self_type_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_self_type_tv);
        update_guarantor_spouse_info_from_self_company_name_edt = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_name_edt);
        update_guarantor_spouse_info_from_self_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_address_tv);
        update_guarantor_spouse_info_from_self_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_address1_tv);
        update_guarantor_spouse_info_from_self_company_address2_tv = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_address2_tv);
        update_guarantor_spouse_info_from_other_year_edt = (EditText) view.findViewById(R.id.update_guarantor_spouse_info_from_other_year_edt);
        update_guarantor_spouse_info_from_other_remark_tv = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_from_other_remark_tv);
        update_guarantor_spouse_info_extra_income_from_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_extra_income_from_tv);
        update_guarantor_spouse_info_extra_from_income_year_edt = (EditText) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_year_edt);
        update_guarantor_spouse_info_extra_from_income_company_name_edt = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_name_edt);
        update_guarantor_spouse_info_extra_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_address_tv);
        update_guarantor_spouse_info_extra_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_address1_tv);
        update_guarantor_spouse_info_extra_from_income_company_address2_tv = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_address2_tv);
        update_guarantor_spouse_info_extra_from_income_work_phone_num_edt = (NoEmptyEditText) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_work_phone_num_edt);
        update_guarantor_spouse_info_from_income_group_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_income_group_lin);
        update_guarantor_spouse_info_from_self_group_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_self_group_lin);
        update_guarantor_spouse_info_from_other_group_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_other_group_lin);
        update_guarantor_spouse_info_extra_income_from_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_extra_income_from_lin);
        update_guarantor_spouse_info_id_back_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_id_back_tv);
        update_guarantor_spouse_info_id_front_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_id_front_tv);
        update_guarantor_spouse_info_id_front_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_id_front_lin);
        update_guarantor_spouse_info_id_back_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_id_back_lin);


        mScrollView = ((NestedScrollView) view.findViewById(R.id.scrollView));

        //回到顶部按钮
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.smoothScrollTo(0, 0);
            }
        });
        //选择收入来源
        income_from_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_income_from_lin);
        income_from_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_income_from_tv);
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

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("工资")) {
                                    view.findViewById(R.id.update_guarantor_spouse_info_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_spouse_info_from_income_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("自营")) {
                                    view.findViewById(R.id.update_guarantor_spouse_info_from_self_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_spouse_info_from_self_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX).equals("其他")) {
                                    view.findViewById(R.id.update_guarantor_spouse_info_from_other_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_spouse_info_from_other_group_lin).setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        //选择额外收入来源
        update_guarantor_spouse_info_extra_from_income_group_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_group_lin);
        income_extra_from_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_extra_income_from_tv);
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
                                if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("工资")) {
                                    view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
                                }
                                if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX).equals("无")) {
                                    view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);

                                }
                            }
                        }
                );
            }
        });
        update_guarantor_spouse_info_marriage_group_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_marriage_group_lin);
        update_guarantor_spouse_info_divorced_group_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_divorced_group_lin);
        update_guarantor_spouse_info_die_group_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_die_group_lin);
        //选择个人婚姻状态
        update_guarantor_spouse_info_marriage_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_marriage_lin);
        update_guarantor_spouse_info_marriage_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_marriage_tv);
        update_guarantor_spouse_info_marriage_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.marriage_key,
                        UPDATE_MARRIAGE_INDEX,
                        update_guarantor_spouse_info_marriage_lin,
                        update_guarantor_spouse_info_marriage_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_MARRIAGE_INDEX = selectedIndex;

                                if (YusionApp.CONFIG_RESP.marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("已婚")) {
                                    update_guarantor_spouse_info_marriage_group_lin.setVisibility(View.VISIBLE);
                                } else {
                                    update_guarantor_spouse_info_marriage_group_lin.setVisibility(View.GONE);
                                }

                                if (YusionApp.CONFIG_RESP.marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("离异")) {
                                    update_guarantor_spouse_info_divorced_group_lin.setVisibility(View.VISIBLE);
                                } else {
                                    update_guarantor_spouse_info_divorced_group_lin.setVisibility(View.GONE);
                                }

                                if (YusionApp.CONFIG_RESP.marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("丧偶")) {
                                    update_guarantor_spouse_info_die_group_lin.setVisibility(View.VISIBLE);
                                } else {
                                    update_guarantor_spouse_info_die_group_lin.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        //配偶身份证人像面
        update_guarantor_spouse_info_id_back_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_id_back_lin);
        update_guarantor_spouse_info_id_back_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_id_back_tv);
        update_guarantor_spouse_info_id_back_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DocumentActivity.class);
                intent.putExtra("type", Constants.FileLabelType.ID_BACK);
                intent.putExtra("role", Constants.PersonType.LENDER_SP);
                intent.putExtra("ocrResp", ocrResp);
                intent.putExtra("imgUrl", idBackImgUrl);
                startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
            }
        });
        //配偶身份证国徽面
        update_guarantor_spouse_info_id_front_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_id_front_lin);
        update_guarantor_spouse_info_id_front_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_id_front_tv);
        update_guarantor_spouse_info_id_front_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DocumentActivity.class);
                intent.putExtra("type", Constants.FileLabelType.ID_FRONT);
                intent.putExtra("role", Constants.PersonType.LENDER_SP);
                intent.putExtra("imgUrl", idFrontImgUrl);
                startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
            }
        });

        //选择性别
        update_guarantor_spouse_info_gender_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_gender_lin);
        update_guarantor_spouse_info_gender_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_gender_tv);
        update_guarantor_spouse_info_gender_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.gender_list_key,
                        UPDATE_SEX_INDEX,
                        update_guarantor_spouse_info_gender_lin,
                        update_guarantor_spouse_info_gender_tv,
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

        //工资 公司地址
        update_guarantor_spouse_info_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_address_lin);
        update_guarantor_spouse_info_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_address_tv);
        update_guarantor_spouse_info_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_spouse_info_from_income_company_address_lin,
                        update_guarantor_spouse_info_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_guarantor_spouse_info_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //工资 详细地址
        update_guarantor_spouse_info_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_address1_lin);
        update_guarantor_spouse_info_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_income_company_address1_tv);
        update_guarantor_spouse_info_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_guarantor_spouse_info_from_income_company_address_tv != null) {
                    update_guarantor_spouse_info_from_income_company_address1_tv.setEnabled(true);
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_spouse_info_from_income_company_address1_lin.getId();
                    requestPOI(update_guarantor_spouse_info_from_income_company_address_tv.getText().toString());
                } else {
                    update_guarantor_spouse_info_from_income_company_address1_tv.setEnabled(false);
                }
            }
        });

        // 工资 选择职务
        update_guarantor_spouse_info_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_income_work_position_lin);
        update_guarantor_spouse_info_work_position_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_work_position_tv);
        update_guarantor_spouse_info_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                        update_guarantor_spouse_info_from_income_work_position_lin,
                        update_guarantor_spouse_info_work_position_tv,
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
        update_guarantor_spouse_info_from_self_type_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_self_type_lin);
        update_guarantor_spouse_info_from_self_type_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_self_type_tv);
        update_guarantor_spouse_info_from_self_type_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.busi_type_list_key,
                        UPDATE_FROM_SELF_TYPE_INDEX,
                        update_guarantor_spouse_info_from_self_type_lin,
                        update_guarantor_spouse_info_from_self_type_tv,
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
                                                    update_guarantor_spouse_info_from_self_type_tv.setText(editText.getText());
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
        update_guarantor_spouse_info_from_self_company_address_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_address_lin);
        update_guarantor_spouse_info_from_self_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_address_tv);
        update_guarantor_spouse_info_from_self_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_spouse_info_from_self_company_address_lin,
                        update_guarantor_spouse_info_from_self_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_guarantor_spouse_info_from_self_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //自营 详细地址
        update_guarantor_spouse_info_from_self_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_address1_lin);
        update_guarantor_spouse_info_from_self_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_from_self_company_address1_tv);
        update_guarantor_spouse_info_from_self_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_guarantor_spouse_info_from_self_company_address_tv != null) {
                    update_guarantor_spouse_info_from_self_company_address1_tv.setEnabled(true);
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_spouse_info_from_self_company_address1_lin.getId();
                    requestPOI(update_guarantor_spouse_info_from_self_company_address_tv.getText().toString());
                } else {
                    update_guarantor_spouse_info_from_self_company_address1_tv.setEnabled(false);
                }
            }
        });

        //额外 公司地址
        update_guarantor_spouse_info_extra_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_address_lin);
        update_guarantor_spouse_info_extra_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_address_tv);
        update_guarantor_spouse_info_extra_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_guarantor_spouse_info_extra_from_income_company_address_lin,
                        update_guarantor_spouse_info_extra_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_guarantor_spouse_info_extra_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

//        //选择额外收入来源
//        income_extra_from_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_extra_income_from_lin);
//        income_extra_from_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_extra_income_from_tv);
//        income_extra_from_lin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WheelViewUtil.showWheelView(incomeextarlist,
//                        UPDATE_EXTRA_INCOME_FROME_INDEX,
//                        income_extra_from_lin,
//                        income_extra_from_tv,
//                        "请选择",
//                        new WheelViewUtil.OnSubmitCallBack() {
//                            @Override
//                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
//                                UPDATE_EXTRA_INCOME_FROME_INDEX = selectedIndex;
//                                if (incomeextarlist.get(UPDATE_EXTRA_INCOME_FROME_INDEX) == "工资") {
//                                    view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
//                                } else {
//                                    view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                );
//            }
//        });

        //额外 详细地址
        update_guarantor_spouse_info_extra_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_address1_lin);
        update_guarantor_spouse_info_extra_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_company_address1_tv);
        update_guarantor_spouse_info_extra_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_guarantor_spouse_info_extra_from_income_company_address_tv != null) {
                    update_guarantor_spouse_info_extra_from_income_company_address1_tv.setEnabled(true);
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_guarantor_spouse_info_extra_from_income_company_address1_lin.getId();
                    requestPOI(update_guarantor_spouse_info_extra_from_income_company_address_tv.getText().toString());
                } else {
                    update_guarantor_spouse_info_extra_from_income_company_address1_tv.setEnabled(false);
                }
            }
        });

        // 额外 选择职务
        update_guarantor_spouse_info_extra_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_extra_from_income_work_position_lin);
        update_guarantor_spouse_info_extra_from_income_work_position_tv = (TextView) view.findViewById(R.id.personal_extra_info_work_position_tv);
        update_guarantor_spouse_info_extra_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_EXTRA_WORK_POSITION_INDEX,
                        update_guarantor_spouse_info_extra_from_income_work_position_lin,
                        update_guarantor_spouse_info_extra_from_income_work_position_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex;
                            }
                        });
            }
        });

        //法院判决书
        update_guarantor_spouse_info_divorced_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_divorced_lin);
        update_guarantor_spouse_info_divorced_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_divorced_tv);
        update_guarantor_spouse_info_divorced_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadListActivity.class);
                intent.putExtra("type", Constants.FileLabelType.MARRIAGE_PROOF);
                intent.putExtra("role", Constants.PersonType.LENDER);
                intent.putExtra("imgList", divorceImgsList);
                intent.putExtra("title", "离婚证");
                intent.putExtra("clt_id", guarantorInfo.clt_id);
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
            }
        });


        //户口本
        update_guarantor_spouse_info_register_addr_lin = (LinearLayout) view.findViewById(R.id.update_guarantor_spouse_info_register_addr_lin);
        update_guarantor_spouse_info_register_addr_tv = (TextView) view.findViewById(R.id.update_guarantor_spouse_info_register_addr_tv);
        update_guarantor_spouse_info_register_addr_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UploadListActivity.class);
                intent.putExtra("type", Constants.FileLabelType.RES_BOOKLET);
                intent.putExtra("role", Constants.PersonType.LENDER);
                intent.putExtra("imgList", resBookList);
                intent.putExtra("title", "户口本");
                intent.putExtra("clt_id", guarantorInfo.clt_id);
                startActivityForResult(intent, Constants.REQUEST_MULTI_DOCUMENT);
            }
        });
        update_guarantor_spouse_info_mobile_img = (ImageView) view.findViewById(R.id.update_guarantor_spouse_info_mobile_img);
        update_guarantor_spouse_info_mobile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_CLICKED_VIEW_FOR_CONTACT = update_guarantor_spouse_info_mobile_img.getId();
                selectContact();
            }
        });
        update_guarantor_spouse_info_mobile_edt = (EditText) view.findViewById(R.id.update_guarantor_spouse_info_mobile_edt);
        return view;
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
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_guarantor_spouse_info_mobile_img.getId()) {
                    update_guarantor_spouse_info_mobile_edt.setText(result[1].replaceAll(" ", ""));
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_spouse_info_from_income_company_address1_lin.getId()) {
                    update_guarantor_spouse_info_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_spouse_info_from_self_company_address1_lin.getId()) {
                    update_guarantor_spouse_info_from_self_company_address1_tv.setText(data.getStringExtra("result"));
                }
                if (CURRENT_CLICKED_VIEW_FOR_ADDRESS == update_guarantor_spouse_info_extra_from_income_company_address1_lin.getId()) {
                    update_guarantor_spouse_info_extra_from_income_company_address1_tv.setText(data.getStringExtra("result"));
                }
            } else if (requestCode == Constants.REQUEST_DOCUMENT) {
                switch (data.getStringExtra("type")) {
                    case Constants.FileLabelType.ID_BACK:
                        ID_BACK_FID = data.getStringExtra("objectKey");
                        idBackImgUrl = data.getStringExtra("imgUrl");
                        if (!idBackImgUrl.isEmpty()) {
                            update_guarantor_spouse_info_id_back_tv.setText("已上传");
                            update_guarantor_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
                            ocrResp = (OcrResp.ShowapiResBodyBean) data.getSerializableExtra("ocrResp");
                        } else {
                            update_guarantor_spouse_info_id_back_tv.setText("请上传");
                            update_guarantor_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        if (ocrResp != null) {
                            update_guarantor_spouse_info_id_no_edt.setText(ocrResp.idNo);
                            update_guarantor_spouse_info_clt_nm_edt.setText(ocrResp.name);
                        }
                        break;
                    case Constants.FileLabelType.ID_FRONT:
                        ID_FRONT_FID = data.getStringExtra("objectKey");
                        idFrontImgUrl = data.getStringExtra("imgUrl");
                        if (!idFrontImgUrl.isEmpty()) {
                            update_guarantor_spouse_info_id_front_tv.setText("已上传");
                            update_guarantor_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            update_guarantor_spouse_info_id_front_tv.setText("请上传");
                            update_guarantor_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        break;
                }
            } else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
                switch (data.getStringExtra("type")) {
                    case Constants.FileLabelType.RES_BOOKLET:
                        resBookList = (ArrayList) data.getSerializableExtra("imgList");
                        if (resBookList.size() > 0) {
                            update_guarantor_spouse_info_register_addr_tv.setText("已上传");
                            update_guarantor_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            update_guarantor_spouse_info_register_addr_tv.setText("请上传");
                            update_guarantor_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        break;
                    case Constants.FileLabelType.MARRIAGE_PROOF:
                        divorceImgsList = (ArrayList) data.getSerializableExtra("imgList");
                        if (divorceImgsList.size() > 0) {
                            update_guarantor_spouse_info_divorced_tv.setText("已上传");
                            update_guarantor_spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            update_guarantor_spouse_info_divorced_tv.setText("请上传");
                            update_guarantor_spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                }
            }
        }
    }

    public void getGuarantorinfo(GuarantorInfo data) {
        if (data != null) {
            guarantorInfo = data;
            //填充
            update_guarantor_spouse_info_marriage_tv.setText(guarantorInfo.marriage);
            switch (guarantorInfo.marriage) {
                case "未婚":
                    break;
                case "已婚":
                    ListImgsReq req1 = new ListImgsReq();
                    req1.label = Constants.FileLabelType.ID_BACK;
                    req1.clt_id = data.spouse.clt_id;
                    UploadApi.listImgs(mContext, req1, resp -> {
                        if (resp.list.size() != 0) {
                            update_guarantor_spouse_info_id_back_tv.setText("已上传");
                            update_guarantor_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
                            idBackImgUrl = resp.list.get(0).s_url;
                        }
                    });
                    ListImgsReq req2 = new ListImgsReq();
                    req2.label = Constants.FileLabelType.ID_FRONT;
                    req2.clt_id = data.spouse.clt_id;
                    UploadApi.listImgs(mContext, req2, resp -> {
                        if (resp.list.size() != 0) {
                            update_guarantor_spouse_info_id_front_tv.setText("已上传");
                            update_guarantor_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
                            idFrontImgUrl = resp.list.get(0).s_url;
                        }
                    });
                    update_guarantor_spouse_info_marriage_group_lin.setVisibility(View.VISIBLE);
                    update_guarantor_spouse_info_clt_nm_edt.setText(guarantorInfo.spouse.clt_nm);
                    update_guarantor_spouse_info_id_no_edt.setText(guarantorInfo.spouse.id_no);
                    update_guarantor_spouse_info_gender_tv.setText(guarantorInfo.spouse.gender);
                    update_guarantor_spouse_info_mobile_edt.setText(guarantorInfo.spouse.mobile);

                    update_guarantor_spouse_info_income_from_tv.setText(guarantorInfo.spouse.major_income_type);
                    //判断主要收入类型
                    switch (guarantorInfo.spouse.major_income_type) {
                        case "工资":
                            update_guarantor_spouse_info_from_income_group_lin.setVisibility(View.VISIBLE);
                            update_guarantor_spouse_info_from_income_year_edt.setText(guarantorInfo.spouse.major_income);
                            update_guarantor_spouse_info_from_income_company_name_edt.setText(guarantorInfo.spouse.major_company_name);
                            if (!guarantorInfo.spouse.major_company_addr.province.equals("")) {
                                update_guarantor_spouse_info_from_income_company_address_tv.setText(guarantorInfo.spouse.major_company_addr.province + "/" + guarantorInfo.spouse.major_company_addr.city + "/" + guarantorInfo.spouse.major_company_addr.district);
                            } else {
                                update_guarantor_spouse_info_from_income_company_address_tv.setText(null);
                            }
                            update_guarantor_spouse_info_from_income_company_address1_tv.setText(guarantorInfo.spouse.major_company_addr.address1);
                            update_guarantor_spouse_info_from_income_company_address2_tv.setText(guarantorInfo.spouse.major_company_addr.address2);
                            update_guarantor_spouse_info_work_position_tv.setText(guarantorInfo.spouse.major_work_position);
                            update_guarantor_spouse_info_from_income_work_phone_num_edt.setText(guarantorInfo.spouse.major_work_phone_num);
                            break;
                        case "自营":
                            update_guarantor_spouse_info_from_self_group_lin.setVisibility(View.VISIBLE);
                            update_guarantor_spouse_info_from_self_year_edt.setText(guarantorInfo.spouse.major_income);
                            update_guarantor_spouse_info_from_self_type_tv.setText(guarantorInfo.spouse.major_busi_type);
                            update_guarantor_spouse_info_from_self_company_name_edt.setText(guarantorInfo.spouse.major_company_name);
                            if (!guarantorInfo.spouse.major_company_addr.province.equals("")) {
                                update_guarantor_spouse_info_from_self_company_address_tv.setText(guarantorInfo.spouse.major_company_addr.province + "/" + guarantorInfo.spouse.major_company_addr.city + "/" + guarantorInfo.spouse.major_company_addr.district);
                            } else {
                                update_guarantor_spouse_info_from_self_company_address_tv.setText(null);
                            }
                            update_guarantor_spouse_info_from_self_company_address1_tv.setText(guarantorInfo.spouse.major_company_addr.address1);
                            update_guarantor_spouse_info_from_self_company_address2_tv.setText(guarantorInfo.spouse.major_company_addr.address2);
                            break;
                        case "其他":
                            update_guarantor_spouse_info_from_other_group_lin.setVisibility(View.VISIBLE);
                            update_guarantor_spouse_info_from_other_year_edt.setText(guarantorInfo.spouse.major_income);
                            update_guarantor_spouse_info_from_other_remark_tv.setText(guarantorInfo.spouse.major_remark);
                            break;
                    }

                    update_guarantor_spouse_info_extra_income_from_tv.setText(guarantorInfo.spouse.extra_income_type);
                    //判断额外收入类型
                    switch (guarantorInfo.spouse.extra_income_type) {
                        case "工资":
                            update_guarantor_spouse_info_extra_from_income_group_lin.setVisibility(View.VISIBLE);
                            update_guarantor_spouse_info_extra_from_income_year_edt.setText(guarantorInfo.spouse.extra_income);
                            update_guarantor_spouse_info_extra_from_income_company_name_edt.setText(guarantorInfo.spouse.extra_company_name);
                            if (!guarantorInfo.spouse.extra_company_addr.province.equals("")) {
                                update_guarantor_spouse_info_extra_from_income_company_address_tv.setText(guarantorInfo.spouse.extra_company_addr.province + "/" + guarantorInfo.spouse.extra_company_addr.city + "/" + guarantorInfo.spouse.extra_company_addr.district);
                            } else {
                                update_guarantor_spouse_info_extra_from_income_company_address_tv.setText(null);
                            }
                            update_guarantor_spouse_info_extra_from_income_company_address1_tv.setText(guarantorInfo.spouse.extra_company_addr.address1);
                            update_guarantor_spouse_info_extra_from_income_company_address2_tv.setText(guarantorInfo.spouse.extra_company_addr.address2);
                            update_guarantor_spouse_info_extra_from_income_work_position_tv.setText(guarantorInfo.spouse.extra_work_position);
                            update_guarantor_spouse_info_extra_from_income_work_phone_num_edt.setText(guarantorInfo.spouse.extra_work_phone_num);
                            break;
                        case "无":
                            update_guarantor_spouse_info_extra_from_income_group_lin.setVisibility(View.GONE);
                    }
                    break;
                case "离异":
                    ListImgsReq req3 = new ListImgsReq();
                    req3.label = Constants.FileLabelType.MARRIAGE_PROOF;
                    req3.clt_id = data.clt_id;
                    UploadApi.listImgs(mContext, req3, new OnItemDataCallBack<ListImgsResp>() {
                        @Override
                        public void onItemDataCallBack(ListImgsResp resp) {
                            if (resp.list.size() != 0) {
                                update_guarantor_spouse_info_divorced_tv.setText("已上传");
                                update_guarantor_spouse_info_divorced_tv.setTextColor(getResources().getColor(R.color.system_color));
                                divorceImgsList = (ArrayList) resp.list;
                            }
                        }
                    });
                    update_guarantor_spouse_info_divorced_group_lin.setVisibility(View.VISIBLE);
                    break;
                case "丧偶":
                    ListImgsReq req4 = new ListImgsReq();
                    req4.label = Constants.FileLabelType.RES_BOOKLET;
                    req4.clt_id = data.clt_id;
                    UploadApi.listImgs(mContext, req4, new OnItemDataCallBack<ListImgsResp>() {
                        @Override
                        public void onItemDataCallBack(ListImgsResp resp) {
                            if (resp.list.size() != 0) {
                                update_guarantor_spouse_info_register_addr_tv.setText("已上传");
                                update_guarantor_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.system_color));
                                resBookList = (ArrayList) resp.list;
                            }
                        }
                    });
                    update_guarantor_spouse_info_die_group_lin.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public void updateGuarantorinfo(OnVoidCallBack callBack) {
        if (checkUserInfo()) {

            //提交
            guarantorInfo.marriage = update_guarantor_spouse_info_marriage_tv.getText().toString().trim();
            switch (update_guarantor_spouse_info_marriage_tv.getText().toString().trim()) {
                case "未婚":
                    break;
                case "已婚":
                    guarantorInfo.spouse.clt_nm = update_guarantor_spouse_info_clt_nm_edt.getText().toString().trim();
                    guarantorInfo.spouse.id_no = update_guarantor_spouse_info_id_no_edt.getText().toString().trim();
                    guarantorInfo.spouse.gender = update_guarantor_spouse_info_gender_tv.getText().toString().trim();
                    guarantorInfo.spouse.mobile = update_guarantor_spouse_info_mobile_edt.getText().toString().trim();
                    break;
                case "离异":
                    break;
                case "丧偶":
                    break;
            }
            guarantorInfo.spouse.major_income_type = update_guarantor_spouse_info_income_from_tv.getText().toString().trim();
            //判断主要收入类型
            switch (update_guarantor_spouse_info_income_from_tv.getText().toString().trim()) {
                case "工资":
                    guarantorInfo.spouse.major_income = update_guarantor_spouse_info_from_income_year_edt.getText().toString().trim();
                    guarantorInfo.spouse.major_company_name = update_guarantor_spouse_info_from_income_company_name_edt.getText().toString().trim();
                    if (update_guarantor_spouse_info_from_income_company_address_tv.getText().toString().trim().split("/").length == 3) {
                        guarantorInfo.spouse.major_company_addr.province = update_guarantor_spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        guarantorInfo.spouse.major_company_addr.city = update_guarantor_spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        guarantorInfo.spouse.major_company_addr.district = update_guarantor_spouse_info_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                        guarantorInfo.spouse.major_company_addr.address1 = update_guarantor_spouse_info_from_income_company_address1_tv.getText().toString().trim();
                        guarantorInfo.spouse.major_company_addr.address2 = update_guarantor_spouse_info_from_income_company_address2_tv.getText().toString().trim();
                    }
                    guarantorInfo.spouse.major_work_position = update_guarantor_spouse_info_work_position_tv.getText().toString().trim();
                    guarantorInfo.spouse.major_work_phone_num = update_guarantor_spouse_info_from_income_work_phone_num_edt.getText().toString().trim();
                    break;
                case "自营":
                    guarantorInfo.spouse.major_income = update_guarantor_spouse_info_from_self_year_edt.getText().toString().trim();
                    guarantorInfo.spouse.major_busi_type = update_guarantor_spouse_info_from_self_type_tv.getText().toString().trim();
                    guarantorInfo.spouse.major_company_name = update_guarantor_spouse_info_from_self_company_name_edt.getText().toString().trim();
                    if (update_guarantor_spouse_info_from_self_company_address_tv.getText().toString().trim().split("/").length == 3) {
                        guarantorInfo.spouse.major_company_addr.province = update_guarantor_spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[0];
                        guarantorInfo.spouse.major_company_addr.city = update_guarantor_spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[1];
                        guarantorInfo.spouse.major_company_addr.district = update_guarantor_spouse_info_from_self_company_address_tv.getText().toString().trim().split("/")[2];
                        guarantorInfo.spouse.major_company_addr.address1 = update_guarantor_spouse_info_from_self_company_address1_tv.getText().toString().trim();
                        guarantorInfo.spouse.major_company_addr.address2 = update_guarantor_spouse_info_from_self_company_address2_tv.getText().toString().trim();
                    }
                    break;
                case "其他":
                    guarantorInfo.spouse.major_income = update_guarantor_spouse_info_from_other_year_edt.getText().toString().trim();
                    guarantorInfo.spouse.major_remark = update_guarantor_spouse_info_from_other_remark_tv.getText().toString().trim();
                    break;
            }
            guarantorInfo.spouse.extra_income_type = update_guarantor_spouse_info_extra_income_from_tv.getText().toString().trim();
            //判断额外收入类型
            switch (update_guarantor_spouse_info_extra_income_from_tv.getText().toString().trim()) {
                case "工资":
                    guarantorInfo.spouse.extra_income = update_guarantor_spouse_info_extra_from_income_year_edt.getText().toString().trim();
                    guarantorInfo.spouse.extra_company_name = update_guarantor_spouse_info_extra_from_income_company_name_edt.getText().toString().trim();
                    if (update_guarantor_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/").length == 3) {
                        guarantorInfo.spouse.extra_company_addr.province = update_guarantor_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[0];
                        guarantorInfo.spouse.extra_company_addr.city = update_guarantor_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[1];
                        guarantorInfo.spouse.extra_company_addr.district = update_guarantor_spouse_info_extra_from_income_company_address_tv.getText().toString().trim().split("/")[2];
                        guarantorInfo.spouse.extra_company_addr.address1 = update_guarantor_spouse_info_extra_from_income_company_address1_tv.getText().toString().trim();
                        guarantorInfo.spouse.extra_company_addr.address2 = update_guarantor_spouse_info_extra_from_income_company_address2_tv.getText().toString().trim();
                    }
                    guarantorInfo.spouse.extra_work_position = update_guarantor_spouse_info_extra_from_income_work_position_tv.getText().toString().trim();
                    guarantorInfo.spouse.extra_work_phone_num = update_guarantor_spouse_info_extra_from_income_work_phone_num_edt.getText().toString().trim();
                    break;
                case "无":
                    break;
            }
            callBack.callBack();
        }


    }


    private boolean checkUserInfo() {
        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("已婚")) {
            if (update_guarantor_spouse_info_clt_nm_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_gender_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_mobile_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(update_guarantor_spouse_info_mobile_edt.getText().toString())) {
                Toast.makeText(mContext, "手机号码有误", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_id_no_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "身份证号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckIdCardValidUtil.isValidatedAllIdcard(update_guarantor_spouse_info_id_no_edt.getText().toString())) {
                Toast.makeText(mContext, "身份证号有误", Toast.LENGTH_SHORT).show();
            }//主要工资
            else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_from_income_company_name_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_from_income_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_from_income_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_from_income_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_work_position_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_from_income_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "工资年收入不能为空", Toast.LENGTH_SHORT).show();
            } //主要自营
            else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_spouse_info_from_self_company_name_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "店铺名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_spouse_info_from_self_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_spouse_info_from_self_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_spouse_info_from_self_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_spouse_info_from_self_type_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "业务类型不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("自营") && update_guarantor_spouse_info_from_self_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "自营年收入不能为空", Toast.LENGTH_SHORT).show();
            }//主要其他
            else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("其他") && update_guarantor_spouse_info_from_other_remark_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "备注不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("其他") && update_guarantor_spouse_info_from_other_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "其他年收入不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_income_from_tv.getText().toString().equals("")) {
                Toast.makeText(mContext, "收入来源不能为空", Toast.LENGTH_SHORT).show();
            }
            //主要工资
            else if (update_guarantor_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_extra_from_income_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位名称不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_extra_from_income_company_address_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_extra_from_income_company_address1_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的详细地址不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_extra_from_income_company_address2_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "单位地址的门牌号不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_extra_from_income_work_position_tv.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "职务不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_guarantor_spouse_info_extra_income_from_tv.getText().toString().equals("工资") && update_guarantor_spouse_info_extra_from_income_year_edt.getText().toString().isEmpty()) {
                Toast.makeText(mContext, "自营年收入不能为空", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("未婚")) {
            return true;
        }
        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("离异")) {
            return true;
        }
        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("丧偶")) {
            return true;
        }
        return false;
    }

    public void updateimgUrl(OnVoidCallBack callBack) {
        //更新图片
        if (guarantorInfo.spouse.clt_id == null) {
            return;
        }

        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("未婚")) {

            uploadUrl(guarantorInfo.clt_id, callBack);

        }
        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("已婚")) {

            uploadUrl(guarantorInfo.spouse.clt_id, callBack);

        }
        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("离异")) {

            uploadUrl(guarantorInfo.clt_id, callBack);

        }
        if (update_guarantor_spouse_info_marriage_tv.getText().toString().equals("丧偶")) {

            uploadUrl(guarantorInfo.clt_id, callBack);

        }
    }

    private void uploadUrl(String clt_id, OnVoidCallBack callBack) {
        ArrayList files = new ArrayList<UploadFilesUrlReq.FileUrlBean>();
        String marriage = update_guarantor_spouse_info_marriage_tv.getText().toString();
        switch (marriage) {
            case "离异":
                for (int i = 0; i < divorceImgsList.size(); i++) {
                    UploadFilesUrlReq.FileUrlBean divorceFileItem = new UploadFilesUrlReq.FileUrlBean();
                    UploadImgItemBean divo = (UploadImgItemBean) divorceImgsList.get(i);
                    divorceFileItem.file_id = divo.objectKey;
                    divorceFileItem.label = Constants.FileLabelType.MARRIAGE_PROOF;
                    divorceFileItem.clt_id = clt_id;
                    files.add(divorceFileItem);
                }
                break;
            case "丧偶":
                for (int i = 0; i < resBookList.size(); i++) {
                    UploadFilesUrlReq.FileUrlBean resBookFileItem = new UploadFilesUrlReq.FileUrlBean();
                    UploadImgItemBean divo = (UploadImgItemBean) resBookList.get(i);
                    resBookFileItem.file_id = divo.objectKey;
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
        uploadFilesUrlReq.region = SharedPrefsUtil.getInstance(mContext).getValue("region", "");
        uploadFilesUrlReq.bucket = SharedPrefsUtil.getInstance(mContext).getValue("bucket", "");
        if (files.size() > 0) {
            UploadApi.uploadFileUrl(mContext, uploadFilesUrlReq, new OnCodeAndMsgCallBack() {
                @Override
                public void callBack(int code, String msg) {
                    if (code < 0) {
                        return;
                    }
                }
            });
            callBack.callBack();
        } else {
            callBack.callBack();
        }
    }

    public void requestUpload(String clt_id, OnVoidCallBack onVoidCallBack) {
        uploadUrl(clt_id, onVoidCallBack);
    }
}

    
package com.yusion.shanghai.yusion.ui.update;


import android.app.Activity;
import android.app.AlertDialog;
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
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.user.ClientInfo;
import com.yusion.shanghai.yusion.bean.user.GetClientInfoReq;
import com.yusion.shanghai.yusion.retrofit.api.ProductApi;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnVoidCallBack;
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.utils.CheckIdCardValidUtil;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.ContactsUtil;
import com.yusion.shanghai.yusion.utils.OcrUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;
import com.yusion.shanghai.yusion.widget.NoEmptyEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateSpouseInfoActivity extends BaseActivity {
//    private UpdateSpouseInfoFragment mUpdateSpouseInfoFragment;
//    private UpdateImgsLabelFragment mUpdateImgsLabelFragment;
//    private String[] mTabTitle = {"配偶信息", "影像件"};

    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
        add("其他");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
        add("无");
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
    private LinearLayout income_from_lin;
    private LinearLayout income_extra_from_lin;
    private TextView income_from_tv;
    private TextView income_extra_from_tv;
    private LinearLayout update_spouse_info_marriage_lin;
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
    private TextView update_spouse_info_extra_from_income_work_position_tv;
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
    //    private LinearLayout update_spouse_info_id_back_lin;
//    private LinearLayout update_spouse_info_id_front_lin;
    private TextView update_spouse_info_id_back_tv;
    private TextView update_spouse_info_id_front_tv;


    private NoEmptyEditText update_spouse_info_clt_nm_edt;                       //姓名
    private EditText update_spouse_info_id_no_edt;                        //身份证号
    private ImageView update_spouse_info_id_no_img;                       //身份证号旁边的一个按钮（——>DocumentActivity）
    private TextView update_spouse_info_gender_tv;                        //性别
    private EditText update_spouse_info_mobile_edt;                       //手机号
    private TextView update_spouse_info_income_from_tv;                   //主要收入来源
    private EditText update_spouse_info_from_income_year_edt;             //主要-工资-年收入
    private NoEmptyEditText update_spouse_info_from_income_company_name_edt;     //主要-工资-单位名称
    private TextView update_spouse_info_from_income_company_address_tv;   //主要-工资-单位地址
    private TextView update_spouse_info_from_income_company_address1_tv;  //主要-工资-详细地址
    private NoEmptyEditText update_spouse_info_from_income_company_address2_tv;  //主要-工资-门牌号
    private TextView update_spouse_info_work_position_tv;                 //主要-工资-职务
    private NoEmptyEditText update_spouse_info_from_income_work_phone_num_edt;   //主要-工资-单位座机
    private EditText update_spouse_info_from_self_year_edt;               //主要-自营-年收入
    private TextView update_spouse_info_from_self_type_tv;                //主要-自营-业务类型
    private NoEmptyEditText update_spouse_info_from_self_company_name_edt;       //主要-自营-店铺名称
    private TextView update_spouse_info_from_self_company_address_tv;     //主要-自营-单位地址
    private TextView update_spouse_info_from_self_company_address1_tv;    //主要-自营-详细地址
    private NoEmptyEditText update_spouse_info_from_self_company_address2_tv;    //主要-自营-门牌号
    private EditText update_spouse_info_from_other_year_edt;              //主要-其他-年收入
    private NoEmptyEditText update_spouse_info_from_other_remark_tv;             //主要-其他-备注
    private TextView update_spouse_info_extra_income_from_tv;             //额外收入来源
    private EditText update_spouse_info_extra_from_income_year_edt;            //额外-工资-年收入
    private NoEmptyEditText update_spouse_info_extra_from_income_company_name_edt;    //额外-工资-单位名称
    private TextView update_spouse_info_extra_from_income_company_address_tv;  //额外-工资-公司地址
    private TextView update_spouse_info_extra_from_income_company_address1_tv; //额外-工资-详细地址
    private NoEmptyEditText update_spouse_info_extra_from_income_company_address2_tv; //额外-工资-门牌号
    private NoEmptyEditText update_spouse_info_extra_from_income_work_phone_num_edt;  //额外-工资-单位座机
    private OcrResp.ShowapiResBodyBean ocrResp = new OcrResp.ShowapiResBodyBean();
    private ArrayList<UploadImgItemBean> divorceImgsList = new ArrayList<UploadImgItemBean>();
    private ArrayList<UploadImgItemBean> resBookList = new ArrayList<UploadImgItemBean>();
    private UploadImgItemBean backImg = new UploadImgItemBean();
    private UploadImgItemBean frontImg = new UploadImgItemBean();
    private EditText update_spouse_info_child_count_edt;                   //子女数量
    private EditText update_spouse_info_child_count1_edt;                   //子女数量
    private EditText update_spouse_info_child_count2_edt;                   //子女数量

    public ClientInfo clientInfo;
    private File imageFile;
    private OcrResp.ShowapiResBodyBean mOcrResp;

    private String old_marriage;
    private String now_marriage;
    private String imgsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spouse_info);
        initTitleBar(this, "配偶资料").setLeftClickListener(v -> showDoubleCheckForExit());
        initView();

        getInfo();  //获取配偶信息


        findViewById(R.id.submit_img).setOnClickListener(v -> {
            submit();   //更新配偶信息
        });
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
        update_spouse_info_marriage_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.marriage_key,
                UPDATE_MARRIAGE_INDEX,
                update_spouse_info_marriage_lin,
                update_spouse_info_marriage_tv,
                "请选择",
                (clickedView, selectedIndex) -> {
                    UPDATE_MARRIAGE_INDEX = selectedIndex;
                    clientInfo.marriage = YusionApp.CONFIG_RESP.marriage_key.get(UPDATE_MARRIAGE_INDEX);
                    if (YusionApp.CONFIG_RESP.marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("已婚")) {
                        update_spouse_info_marriage_group_lin.setVisibility(View.VISIBLE);
                    } else {
                        update_spouse_info_marriage_group_lin.setVisibility(View.GONE);
                    }
                    if (YusionApp.CONFIG_RESP.marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("离异")) {
                        update_spouse_info_divorced_group_lin.setVisibility(View.VISIBLE);
                        findViewById(R.id.fab).setVisibility(View.GONE);
                    } else {
                        update_spouse_info_divorced_group_lin.setVisibility(View.GONE);
                    }
                    if (YusionApp.CONFIG_RESP.marriage_key.get(UPDATE_MARRIAGE_INDEX).equals("丧偶")) {
                        update_spouse_info_die_group_lin.setVisibility(View.VISIBLE);
                        findViewById(R.id.fab).setVisibility(View.GONE);
                    } else {
                        update_spouse_info_die_group_lin.setVisibility(View.GONE);
                        findViewById(R.id.fab).setVisibility(View.GONE);
                    }
                }));

        //选择性别
        update_spouse_info_gender_lin = (LinearLayout) findViewById(R.id.update_spouse_info_gender_lin);
        update_spouse_info_gender_tv = (TextView) findViewById(R.id.update_spouse_info_gender_tv);
        update_spouse_info_gender_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.gender_list_key,
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
        update_spouse_info_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                update_spouse_info_from_income_work_position_lin,
                update_spouse_info_work_position_tv,
                "请选择",
                (clickedView, selectedIndex) -> UPDATE_FROM_INCOME_WORK_POSITION_INDEX = selectedIndex));

        //自营 业务类型
        update_spouse_info_from_self_type_lin = (LinearLayout) findViewById(R.id.update_spouse_info_from_self_type_lin);
        update_spouse_info_from_self_type_tv = (TextView) findViewById(R.id.update_spouse_info_from_self_type_tv);
        update_spouse_info_from_self_type_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.busi_type_list_key,
                UPDATE_FROM_SELF_TYPE_INDEX,
                update_spouse_info_from_self_type_lin,
                update_spouse_info_from_self_type_tv,
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
                                    update_spouse_info_from_self_type_tv.setText(editText.getText());
                                    UPDATE_FROM_SELF_TYPE_INDEX = 0;
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
        update_spouse_info_extra_from_income_work_position_tv = (TextView) findViewById(R.id.personal_extra_info_work_position_tv);
        update_spouse_info_extra_from_income_work_position_lin.setOnClickListener(v -> WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
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
                }
            } else if (requestCode == Constants.REQUEST_ADDRESS) {
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
        } else if (requestCode == 3001) {
            OcrUtil.requestOcr(this, imageFile.getAbsolutePath(), new OSSObjectKeyBean("lender_sp", "id_card_back", ".png"), "id_card", (ocrResp1, objectKey) -> {
                if (ocrResp1 == null) {
                    Toast.makeText(UpdateSpouseInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                } else if (ocrResp1.showapi_res_code != 0 && TextUtils.isEmpty(ocrResp1.showapi_res_body.idNo) || TextUtils.isEmpty(ocrResp1.showapi_res_body.name)) {
                    Toast.makeText(UpdateSpouseInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UpdateSpouseInfoActivity.this, "识别成功", Toast.LENGTH_LONG).show();
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
            }, (throwable, s) -> Toast.makeText(UpdateSpouseInfoActivity.this, "识别失败", Toast.LENGTH_LONG).show());
        }

//            else if (requestCode == Constants.REQUEST_MULTI_DOCUMENT) {
//                switch (data.getStringExtra("type")) {
//                    case Constants.FileLabelType.RES_BOOKLET:
//                        resBookList = (ArrayList<UploadImgItemBean>) data.getSerializableExtra("imgList");
//                        if (resBookList.size() > 0) {
//                            update_spouse_info_register_addr_tv.setText("已上传");
//                            update_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.system_color));
//                        } else {
//                            update_spouse_info_register_addr_tv.setText("请上传");
//                            update_spouse_info_register_addr_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
//                        }
//                        break;
//                    case Constants.FileLabelType.MARRIAGE_PROOF:
//                        divorceImgsList = (ArrayList<UploadImgItemBean>) data.getSerializableExtra("imgList");
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

                backImg = (UploadImgItemBean) data.getSerializableExtra("backImg");
                switch (backImg.type){
                    case Constants.FileLabelType.ID_BACK:
                        if (data.getBooleanExtra("ishaveImg",false)){
                            update_spouse_info_id_back_tv.setText("已上传");
                            update_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
                            ocrResp = (OcrResp.ShowapiResBodyBean) data.getSerializableExtra("ocrResp");
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
                         if (data.getBooleanExtra("ishaveImg",false)){
                            update_spouse_info_id_front_tv.setText("已上传");
                            update_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.system_color));
                        } else {
                            update_spouse_info_id_front_tv.setText("请上传");
                            update_spouse_info_id_front_tv.setTextColor(getResources().getColor(R.color.please_upload_color));
                        }
                        break;
                }
*/

//                switch (data.getStringExtra("type")) {
//                    case Constants.FileLabelType.ID_BACK:
//                        ID_BACK_FID = data.getStringExtra("objectKey");
//                        idBackImgUrl = data.getStringExtra("imgUrl");
////                        if (!idBackImgUrl.isEmpty()) {
////                            update_spouse_info_id_back_tv.setText("已上传");
////                            update_spouse_info_id_back_tv.setTextColor(getResources().getColor(R.color.system_color));
////                            ocrResp = (OcrResp.ShowapiResBodyBean) data.getSerializableExtra("ocrResp");
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
////                        ID_FRONT_FID = data.getStringExtra("objectKey");
////                        idFrontImgUrl = data.getStringExtra("imgUrl");
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
//        mUpdateSpouseInfoFragment.requestUpdate();
        //上传用户资料
        updateClientinfo(() -> ProductApi.updateClientInfo(UpdateSpouseInfoActivity.this, clientInfo, data -> {
            if (data == null) {
                return;
            }
            clientInfo = data;
            //已婚状态：上传配偶cltid
//            if (data != null) {
            if (clientInfo.marriage.equals("已婚")) {
//                requestUpload(clientInfo.spouse.clt_id, () -> {
                //上传影像件
//                    requestUpload(clientInfo.spouse.clt_id, () -> {
                toCommitActivity(clientInfo.spouse.clt_id, "lender_sp", "个人配偶影像件资料", "continue");

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
                        toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "return");
                    }//状态改变：两个按钮
                    else {
                        if (TextUtils.equals(now_marriage, "丧偶")) {
                            toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "continue");
                        } else if (TextUtils.equals(now_marriage, "离异")) {
                            toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "continue");
                        }
                    }
                }//未婚：一个按钮
                else {
                    toCommitActivity(clientInfo.clt_id, "lender_sp", "个人影像件资料", "return");
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
            clientInfo.marriage = update_spouse_info_marriage_tv.getText().toString();
            now_marriage = clientInfo.marriage;
            switch (update_spouse_info_marriage_tv.getText().toString()) {
                case "未婚":
                    break;
                case "已婚":
                    clientInfo.spouse.clt_nm = update_spouse_info_clt_nm_edt.getText().toString().trim();
                    clientInfo.spouse.id_no = update_spouse_info_id_no_edt.getText().toString().trim();
                    clientInfo.spouse.gender = update_spouse_info_gender_tv.getText().toString().trim();
                    clientInfo.spouse.mobile = update_spouse_info_mobile_edt.getText().toString().trim();
                    clientInfo.child_num = update_spouse_info_child_count_edt.getText().toString().trim();
                    break;
                case "离异":
                    clientInfo.child_num = update_spouse_info_child_count1_edt.getText().toString().trim();
                    break;
                case "丧偶":
                    clientInfo.child_num = update_spouse_info_child_count2_edt.getText().toString().trim();
                    break;
            }
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
            } else if (TextUtils.isEmpty(update_spouse_info_mobile_edt.getText().toString())) {
                Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            } else if (!CheckMobileUtil.checkMobile(update_spouse_info_mobile_edt.getText().toString())) {
                Toast.makeText(this, "手机号码有误", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(update_spouse_info_child_count_edt.getText().toString())) {
                Toast.makeText(this, "子女数量不能为空", Toast.LENGTH_SHORT).show();
            } else if (update_spouse_info_income_from_tv.getText().toString().equals("")) {
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
            } else {
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
//                    req1.clt_id = data.spouse.clt_id;
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
//                    req2.clt_id = data.spouse.clt_id;
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
                    update_spouse_info_mobile_edt.setText(clientInfo.spouse.mobile);
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

                    }
                    break;
                case "离异":

//                    ListImgsReq req3 = new ListImgsReq();
//                    req3.label = Constants.FileLabelType.MARRIAGE_PROOF;
//                    req3.clt_id = data.clt_id;
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
//                    req4.clt_id = data.clt_id;
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

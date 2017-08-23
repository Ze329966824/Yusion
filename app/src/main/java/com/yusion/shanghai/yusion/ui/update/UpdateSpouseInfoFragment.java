package com.yusion.shanghai.yusion.ui.update;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.tv.TvView;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.yusion.shanghai.yusion.settings.Constants;
import com.yusion.shanghai.yusion.ui.apply.AMapPoiListActivity;
import com.yusion.shanghai.yusion.ui.apply.DocumentActivity;
import com.yusion.shanghai.yusion.utils.ContactsUtil;
import com.yusion.shanghai.yusion.utils.wheel.WheelViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aa on 2017/8/21.
 */

public class UpdateSpouseInfoFragment extends BaseFragment {
    private List<String> incomelist = new ArrayList<String>() {{
        add("工资");
        add("自营");
        add("其他");
    }};
    private List<String> incomeextarlist = new ArrayList<String>() {{
        add("工资");
    }};
    private List<String> marriagelist = new ArrayList<String>() {{
        add("已婚");
        add("离异");
        add("丧偶");
    }};


    public static String idBackImgUrl = "";
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
    private LinearLayout update_spouse_info_marriage_lin;
    private TextView update_spouse_info_marriage_tv;
    private LinearLayout update_spouse_info_id_back_lin;
    private TextView update_spouse_info_id_back_tv;
    private LinearLayout update_spouse_info_id_front_lin;
    private TextView update_spouse_info_id_front_tv;
    private LinearLayout update_spouse_info_gender_lin;
    private TextView update_spouse_info_gender_tv;
    private LinearLayout update_spouse_info_from_income_company_address_lin;
    private TextView update_spouse_info_from_income_company_address_tv;

    private LinearLayout update_spouse_info_from_income_company_address1_lin;
    private TextView update_spouse_info_from_income_company_address1_tv;

    private LinearLayout update_spouse_info_from_income_work_position_lin;
    private TextView update_spouse_info_work_position_tv;

    private LinearLayout update_spouse_info_from_self_company_address_lin;
    private TextView update_spouse_info_from_self_company_address_tv;

    private LinearLayout update_spouse_info_from_self_company_address1_lin;
    private TextView update_spouse_info_from_self_company_address1_tv;


    private LinearLayout update_spouse_info_extra_from_income_company_address_lin;
    private TextView update_spouse_info_extra_from_income_company_address_tv;

    private LinearLayout update_spouse_info_extra_from_income_company_address1_lin;
    private TextView update_spouse_info_extra_from_income_company_address1_tv;

    private LinearLayout update_spouse_info_extra_from_income_work_position_lin;
    private TextView update_spouse_info_extra_from_income_work_position_tv;

    private LinearLayout update_spouse_info_divorced_lin;
    private TextView update_spouse_info_divorced_tv;

    private LinearLayout update_spouse_info_die_proof_lin;
    private TextView update_spouse_info_die_proof_tv;

    private LinearLayout update_spouse_info_register_addr_lin;
    private TextView update_spouse_info_register_addr_tv;

    private ImageView update_spouse_info_mobile_img;
    private EditText update_spouse_info_mobile_edt;

    private LinearLayout update_spouse_info_from_self_type_lin;
    private TextView update_spouse_info_from_self_type_tv;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_spouse_info, container, false);

        //选择收入来源
        income_from_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_income_from_lin);
        income_from_tv = (TextView) view.findViewById(R.id.update_spouse_info_income_from_tv);
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
                                    view.findViewById(R.id.update_spouse_info_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_spouse_info_from_income_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "自营") {
                                    view.findViewById(R.id.update_spouse_info_from_self_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_spouse_info_from_self_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "其他") {
                                    view.findViewById(R.id.update_spouse_info_from_other_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_spouse_info_from_other_group_lin).setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

//        //选择额外收入来源
//        income_extra_from_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_extra_income_from_lin);
//        income_extra_from_tv = (TextView) view.findViewById(R.id.update_spouse_info_extra_income_from_tv);
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
//                                    view.findViewById(R.id.update_spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
//                                } else {
//                                    view.findViewById(R.id.update_spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
//                                }
//                            }
//                        }
//                );
//            }
//        });

        //选择个人婚姻状态
        update_spouse_info_marriage_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_marriage_lin);
        update_spouse_info_marriage_tv = (TextView) view.findViewById(R.id.update_spouse_info_marriage_tv);
        update_spouse_info_marriage_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(marriagelist, //YusionApp.CONFIG_RESP.marriage_key
                        UPDATE_MARRIAGE_INDEX,
                        update_spouse_info_marriage_lin,
                        update_spouse_info_marriage_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_MARRIAGE_INDEX = selectedIndex;

                                if (marriagelist.get(UPDATE_MARRIAGE_INDEX) == "已婚") {
                                    view.findViewById(R.id.update_spouse_info_marriage_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_spouse_info_marriage_group_lin).setVisibility(View.GONE);
                                }

                                if (marriagelist.get(UPDATE_MARRIAGE_INDEX) == "离异") {
                                    view.findViewById(R.id.update_spouse_info_divorced_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_spouse_info_divorced_group_lin).setVisibility(View.GONE);
                                }

                                if (marriagelist.get(UPDATE_MARRIAGE_INDEX) == "丧偶") {
                                    view.findViewById(R.id.update_spouse_info_die_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_spouse_info_die_group_lin).setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        //配偶身份证人像面
        update_spouse_info_id_back_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_id_back_lin);
        update_spouse_info_id_back_tv = (TextView) view.findViewById(R.id.update_spouse_info_id_back_tv);
        update_spouse_info_id_back_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DocumentActivity.class);
                intent.putExtra("type", "id_card_back");
                intent.putExtra("role", "lender_sp");
                intent.putExtra("imgUrl", idBackImgUrl);
                startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
            }
        });
        //配偶身份证国徽面
        update_spouse_info_id_front_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_id_front_lin);
        update_spouse_info_id_front_tv = (TextView) view.findViewById(R.id.update_spouse_info_id_front_tv);
        update_spouse_info_id_front_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DocumentActivity.class);
                intent.putExtra("type", "id_card_back");
                intent.putExtra("role", "lender_sp");
                intent.putExtra("imgUrl", idBackImgUrl);
                startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
            }
        });


        //选择性别
        update_spouse_info_gender_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_gender_lin);
        update_spouse_info_gender_tv = (TextView) view.findViewById(R.id.update_spouse_info_gender_tv);
        update_spouse_info_gender_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.gender_list_key,
                        UPDATE_SEX_INDEX,
                        update_spouse_info_gender_lin,
                        update_spouse_info_gender_tv,
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
        update_spouse_info_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_from_income_company_address_lin);
        update_spouse_info_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_spouse_info_from_income_company_address_tv);
        update_spouse_info_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_from_income_company_address_lin,
                        update_spouse_info_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_spouse_info_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //工资 详细地址 ????

        update_spouse_info_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_from_income_company_address1_lin);
        update_spouse_info_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_spouse_info_from_income_company_address1_tv);
        update_spouse_info_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_spouse_info_from_income_company_address_tv != null) {
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_from_income_company_address1_lin.getId();
                    requestPOI(update_spouse_info_from_income_company_address_tv.getText().toString());
                }
            }
        });

        // 工资 选择职务
        update_spouse_info_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_from_income_work_position_lin);
        update_spouse_info_work_position_tv = (TextView) view.findViewById(R.id.update_spouse_info_work_position_tv);
        update_spouse_info_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_INCOME_WORK_POSITION_INDEX,
                        update_spouse_info_from_income_work_position_lin,
                        update_spouse_info_work_position_tv,
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
        update_spouse_info_from_self_type_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_from_self_type_lin);
        update_spouse_info_from_self_type_tv = (TextView) view.findViewById(R.id.update_spouse_info_from_self_type_tv);
        update_spouse_info_from_self_type_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.busi_type_list_key,
                        UPDATE_FROM_SELF_TYPE_INDEX,
                        update_spouse_info_from_self_type_lin,
                        update_spouse_info_from_self_type_tv,
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
                                                    update_spouse_info_from_self_type_tv.setText(editText.getText());
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
        update_spouse_info_from_self_company_address_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_from_self_company_address_lin);
        update_spouse_info_from_self_company_address_tv = (TextView) view.findViewById(R.id.update_spouse_info_from_self_company_address_tv);
        update_spouse_info_from_self_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_from_self_company_address_lin,
                        update_spouse_info_from_self_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_spouse_info_from_self_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });


        //自营 详细地址 ????

        update_spouse_info_from_self_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_from_self_company_address1_lin);
        update_spouse_info_from_self_company_address1_tv = (TextView) view.findViewById(R.id.update_spouse_info_from_self_company_address1_tv);
        update_spouse_info_from_self_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_spouse_info_from_self_company_address_tv != null) {
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_from_self_company_address1_lin.getId();
                    requestPOI(update_spouse_info_from_self_company_address_tv.getText().toString());
                }
            }
        });

        //其他 备注


        //额外 公司地址
        update_spouse_info_extra_from_income_company_address_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_extra_from_income_company_address_lin);
        update_spouse_info_extra_from_income_company_address_tv = (TextView) view.findViewById(R.id.update_spouse_info_extra_from_income_company_address_tv);
        update_spouse_info_extra_from_income_company_address_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showCityWheelView(getClass().getSimpleName(),
                        update_spouse_info_extra_from_income_company_address_lin,
                        update_spouse_info_extra_from_income_company_address_tv,
                        "请选择所在地区",
                        new WheelViewUtil.OnCitySubmitCallBack() {
                            @Override
                            public void onCitySubmitCallBack(View clickedView, String city) {
                                update_spouse_info_extra_from_income_company_address1_tv.setText("");
                            }
                        }
                );
            }
        });

        //选择额外收入来源
        income_extra_from_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) view.findViewById(R.id.update_spouse_info_extra_income_from_tv);
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
                                    view.findViewById(R.id.update_spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.update_spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
                                }
                            }
                        }
                );
            }
        });




        //法院判决书
        update_spouse_info_divorced_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_divorced_lin);
        update_spouse_info_divorced_tv = (TextView) view.findViewById(R.id.update_spouse_info_divorced_tv);
        update_spouse_info_divorced_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), DivorcedActivity.class);
//                startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
                Toast.makeText(mContext, "还没有创建DivorcedActivity，快去创建.", Toast.LENGTH_SHORT).show();
            }
        });

        //死亡证明
        update_spouse_info_die_proof_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_die_proof_lin);
        update_spouse_info_die_proof_tv = (TextView) view.findViewById(R.id.update_spouse_info_die_proof_tv);
        update_spouse_info_die_proof_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), DiedActivity.class);
//                startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
                Toast.makeText(mContext, "还没有创建DiedActivity，快去创建.", Toast.LENGTH_SHORT).show();
            }
        });

        //户口本
        update_spouse_info_register_addr_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_register_addr_lin);
        update_spouse_info_register_addr_tv = (TextView) view.findViewById(R.id.update_spouse_info_register_addr_tv);
        update_spouse_info_register_addr_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), RegisteredActivity.class);
//                startActivityForResult(intent, Constants.REQUEST_DOCUMENT);
                Toast.makeText(mContext, "还没有创建RegisteredActivity，快去创建.", Toast.LENGTH_SHORT).show();
            }
        });
        
        //额外 详细地址 ????

        update_spouse_info_extra_from_income_company_address1_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_extra_from_income_company_address1_lin);
        update_spouse_info_extra_from_income_company_address1_tv = (TextView) view.findViewById(R.id.update_spouse_info_extra_from_income_company_address1_tv);
        update_spouse_info_extra_from_income_company_address1_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_spouse_info_extra_from_income_company_address_tv != null) {
                    CURRENT_CLICKED_VIEW_FOR_ADDRESS = update_spouse_info_extra_from_income_company_address1_lin.getId();
                    requestPOI(update_spouse_info_extra_from_income_company_address_tv.getText().toString());
                }

            }
        });

        // 额外 选择职务
        update_spouse_info_extra_from_income_work_position_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_extra_from_income_work_position_lin);
        update_spouse_info_extra_from_income_work_position_tv = (TextView) view.findViewById(R.id.personal_extra_info_work_position_tv);
        update_spouse_info_extra_from_income_work_position_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelViewUtil.showWheelView(YusionApp.CONFIG_RESP.work_position_key,
                        UPDATE_FROM_EXTRA_WORK_POSITION_INDEX,
                        update_spouse_info_extra_from_income_work_position_lin,
                        update_spouse_info_extra_from_income_work_position_tv,
                        "请选择",
                        new WheelViewUtil.OnSubmitCallBack() {
                            @Override
                            public void onSubmitCallBack(View clickedView, int selectedIndex) {
                                UPDATE_FROM_EXTRA_WORK_POSITION_INDEX = selectedIndex;
                            }
                        });
            }
        });


        update_spouse_info_divorced_lin = (LinearLayout) view.findViewById(R.id.update_spouse_info_divorced_lin);
        update_spouse_info_divorced_tv = (TextView) view.findViewById(R.id.update_spouse_info_divorced_tv);
        update_spouse_info_divorced_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        update_spouse_info_mobile_img = (ImageView) view.findViewById(R.id.update_spouse_info_mobile_img);
        update_spouse_info_mobile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_CLICKED_VIEW_FOR_CONTACT = update_spouse_info_mobile_img.getId();
                selectContact();
            }
        });
        update_spouse_info_mobile_edt = (EditText) view.findViewById(R.id.update_spouse_info_mobile_edt);


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
                if (CURRENT_CLICKED_VIEW_FOR_CONTACT == update_spouse_info_mobile_img.getId()) {
                    update_spouse_info_mobile_edt.setText(result[1]);
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
        }
    }
}

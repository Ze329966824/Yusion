package com.yusion.shanghai.yusion.ui.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseFragment;
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

    public static int UPDATE_INCOME_FROME_INDEX;
    public static int UPDATE_EXTRA_INCOME_FROME_INDEX;

    private LinearLayout income_from_lin;
    private LinearLayout income_extra_from_lin;
    private TextView income_from_tv;
    private TextView income_extra_from_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_spouse_info, container, false);

        income_from_lin = (LinearLayout) view.findViewById(R.id.spouse_info_income_from_lin);
        income_from_tv = (TextView) view.findViewById(R.id.spouse_info_income_from_tv);
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
                                    view.findViewById(R.id.spouse_info_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.spouse_info_from_income_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "自营") {
                                    view.findViewById(R.id.spouse_info_from_self_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.spouse_info_from_self_group_lin).setVisibility(View.GONE);
                                }

                                if (incomelist.get(UPDATE_INCOME_FROME_INDEX) == "其他") {
                                    view.findViewById(R.id.spouse_info_from_other_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.spouse_info_from_other_group_lin).setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });


        income_extra_from_lin = (LinearLayout) view.findViewById(R.id.spouse_info_extra_income_from_lin);
        income_extra_from_tv = (TextView) view.findViewById(R.id.spouse_info_extra_income_from_tv);
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
                                    view.findViewById(R.id.spouse_info_extra_from_income_group_lin).setVisibility(View.VISIBLE);
                                } else {
                                    view.findViewById(R.id.spouse_info_extra_from_income_group_lin).setVisibility(View.GONE);
                                }
                            }
                        }
                );
            }
        });


        return view;
    }
}

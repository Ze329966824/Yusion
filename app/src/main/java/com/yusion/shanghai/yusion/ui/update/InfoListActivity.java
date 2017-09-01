package com.yusion.shanghai.yusion.ui.update;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.ui.apply.guarantor.AddGuarantorActivity;
import com.yusion.shanghai.yusion.base.BaseActivity;

public class InfoListActivity extends BaseActivity {

    private boolean ishaveGuarantee = false;
    private LinearLayout guarantee_info;
    private LinearLayout add_guarantee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        initTitleBar(this, getResources().getString(R.string.list_info));
        ishaveguarantee();

    }


    private void ishaveguarantee() {
        guarantee_info = (LinearLayout) findViewById(R.id.guarantee_info);
        add_guarantee = (LinearLayout) findViewById(R.id.add_guarantee);
        ishaveGuarantee = getIntent().getBooleanExtra("ishaveGuarantee", true);
        if (ishaveGuarantee) {
            add_guarantee.setVisibility(View.GONE);
        } else {
            guarantee_info.setVisibility(View.GONE);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_personal_info_layout:
                startActivity(new Intent(InfoListActivity.this, UpdatePersonalInfoActivity.class));
                break;

            case R.id.list_personalspouse_info_layout:
                startActivity(new Intent(InfoListActivity.this, UpdateSpouseInfoActivity.class));

                break;

            case R.id.list_guarantor_info:
                startActivity(new Intent(InfoListActivity.this, UpdateGuarantorInfoActivity.class));
                break;

            case R.id.list_guarantorspouse_info:
                startActivity(new Intent(InfoListActivity.this, UpdateGuarantorSpouseInfoActivity.class));

                break;

            case R.id.icon_add_guarantee:
                startActivity(new Intent(InfoListActivity.this, AddGuarantorActivity.class));
                break;

        }
    }
}

package com.yusion.shanghai.yusion.ui.info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;

public class InfoListActivity extends BaseActivity {

    private boolean ishaveguarantee = false;
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

        if (ishaveguarantee != true) {
            guarantee_info.setVisibility(View.GONE);
        } else {
            add_guarantee.setVisibility(View.GONE);
        }
    }
}

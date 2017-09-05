package com.yusion.shanghai.yusion.ui.update;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.user.ListCurrentTpye;
import com.yusion.shanghai.yusion.retrofit.api.UserApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;

public class CommitActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);
        initTitleBar(this, getResources().getString(R.string.commit));
        findViewById(R.id.return_list_info).setOnClickListener(v -> UserApi.getListCurrentTpye(CommitActivity.this, data -> {
            if (data.guarantor_commited) {
                Intent intent = new Intent(CommitActivity.this, InfoListActivity.class);
                intent.putExtra("ishaveGuarantee", true);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(CommitActivity.this, InfoListActivity.class);
                intent.putExtra("ishaveGuarantee", false);
                startActivity(intent);
                finish();
            }
        }));
    }
}

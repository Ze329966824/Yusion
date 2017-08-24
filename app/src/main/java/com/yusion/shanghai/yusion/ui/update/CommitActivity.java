package com.yusion.shanghai.yusion.ui.update;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
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
        reutrnInfoList();
    }

    private void reutrnInfoList() {
        findViewById(R.id.return_list_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UserApi.getListCurrentTpye(CommitActivity.this, new OnItemDataCallBack<ListCurrentTpye>() {
                    @Override
                    public void onItemDataCallBack(ListCurrentTpye data) {
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
                    }
                });


            }
        });
    }


}

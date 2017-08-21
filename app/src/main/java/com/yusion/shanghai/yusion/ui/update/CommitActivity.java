package com.yusion.shanghai.yusion.ui.update;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;

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
                Intent intent = new Intent(CommitActivity.this,InfoListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}

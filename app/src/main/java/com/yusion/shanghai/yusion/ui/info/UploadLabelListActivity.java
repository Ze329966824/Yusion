package com.yusion.shanghai.yusion.ui.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.adapter.UploadLabelListAdapter;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import java.util.List;

public class UploadLabelListActivity extends BaseActivity {

    private Intent mGetIntent;
    private List<UploadLabelItemBean> mItems;
    private UploadLabelListAdapter mAdapter;
    private int mIndex;//item对象在上级页面中list的索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_label_list);
        mGetIntent = getIntent();
        mIndex = mGetIntent.getIntExtra("index", -1);
        initTitleBar(this, mGetIntent.getStringExtra("name")).setLeftClickListener(v -> onBack());
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_label_list_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        mItems = ((UploadLabelItemBean) mGetIntent.getSerializableExtra("item")).label_list;
        mAdapter = new UploadLabelListAdapter(this, mItems);
        mAdapter.setOnItemClick(new UploadLabelListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadLabelItemBean item, int index) {
                Intent intent = new Intent();
                if (item.label_list.size() == 0) {
                    //下级目录为图片页
                    intent.setClass(UploadLabelListActivity.this, UploadListActivity.class);
                } else {
                    //下级目录为标签页
                    intent.setClass(UploadLabelListActivity.this, UploadLabelListActivity.class);
                }
                intent.putExtra("item", item);
                intent.putExtra("name", item.name);
                intent.putExtra("index", index);
                intent.putExtra("clt_id", mGetIntent.getStringExtra("clt_id"));
                intent.putExtra("role", mGetIntent.getStringExtra("role"));
                startActivityForResult(intent, 100);
            }
        });
        rv.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        mGetIntent.putExtra("index", mIndex);
        setResult(RESULT_OK, mGetIntent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                UploadLabelItemBean item = (UploadLabelItemBean) data.getSerializableExtra("item");
                mItems.set(data.getIntExtra("index", -1), item);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

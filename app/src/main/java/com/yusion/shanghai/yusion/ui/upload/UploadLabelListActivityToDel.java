package com.yusion.shanghai.yusion.ui.upload;

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

public class UploadLabelListActivityToDel extends BaseActivity {

    private Intent mGetIntent;
    private List<UploadLabelItemBean> labelList;
    private UploadLabelListAdapter mAdapter;
    private int mIndex;//item对象在上级页面中list的索引
    private UploadLabelItemBean topItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_img);
        mGetIntent = getIntent();
        topItem = (UploadLabelItemBean) mGetIntent.getSerializableExtra("topItem");
        initTitleBar(this, topItem.name).setLeftClickListener(v -> onBack());
        RecyclerView rv = (RecyclerView) findViewById(R.id.update_img_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        labelList = topItem.label_list;
        mAdapter = new UploadLabelListAdapter(this, labelList);
        mAdapter.setOnItemClick(new UploadLabelListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadLabelItemBean item, int index) {
                Intent intent = new Intent();
                if (item.label_list.size() == 0) {
                    //下级目录为图片页
                    intent.setClass(UploadLabelListActivityToDel.this, UploadListNotFromLabelLIstActivity.class);
                } else {
                    //下级目录为标签页
                    intent.setClass(UploadLabelListActivityToDel.this, UploadLabelListActivityToDel.class);
                }
                intent.putExtra("topItem", item);
                //clt_id取图片
//                intent.putExtra("clt_id", ((UpdateUserInfoActivity) getActivity()).getUserInfoBean().clt_id);
                intent.putExtra("index", index);
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
        setResult(RESULT_OK, mGetIntent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                UploadLabelItemBean topItem = (UploadLabelItemBean) data.getSerializableExtra("topItem");
                labelList.set(data.getIntExtra("index", -1), topItem);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

package com.yusion.shanghai.yusion.ui.info;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbq.pickerlib.activity.PhotoMediaActivity;
import com.pbq.pickerlib.entity.PhotoVideoDir;
import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.adapter.UploadImgListAdapter;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.oss.OSSObjectKeyBean;
import com.yusion.shanghai.yusion.bean.upload.ListImgsReq;
import com.yusion.shanghai.yusion.bean.upload.UploadFilesUrlReq;
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.retrofit.api.UploadApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OssUtil;

import java.util.ArrayList;
import java.util.List;

public class UploadListActivity extends BaseActivity {

    private List<UploadImgItemBean> items;
    private UploadImgListAdapter adapter;
    private Intent mGetIntent;
    private int mIndex;//item对象在上级页面中list的索引
    private UploadLabelItemBean mTopItemBean;//上级页面传过来的bean
    private TextView errorTv;
    private LinearLayout errorLin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        mGetIntent = getIntent();
        mIndex = mGetIntent.getIntExtra("index", -1);
        mTopItemBean = (UploadLabelItemBean) mGetIntent.getSerializableExtra("item");
        initTitleBar(this, mGetIntent.getStringExtra("name")).setLeftClickListener(v -> onBack());
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        items = ((UploadLabelItemBean) mGetIntent.getSerializableExtra("item")).img_list;
        adapter = new UploadImgListAdapter(this, items);
        adapter.setOnItemClick(new UploadImgListAdapter.OnItemClick() {
            @Override
            public void onItemClick(View v, UploadImgItemBean item) {

            }

            @Override
            public void onFooterClick(View v) {
                Intent i = new Intent(UploadListActivity.this, PhotoMediaActivity.class);
                i.putExtra("loadType", PhotoVideoDir.Type.IMAGE.toString());
                startActivityForResult(i, 100);
            }
        });
        rv.setAdapter(adapter);
        initData();
    }

    private void initData() {
        if (!mTopItemBean.hasGetImgsFromServer) {
            //第一次进入
            ListImgsReq req = new ListImgsReq();
            req.role = mGetIntent.getStringExtra("role");
            req.label = mTopItemBean.value;
            req.clt_id = mGetIntent.getStringExtra("clt_id");
            UploadApi.listImgs(this, req, resp -> {
                mTopItemBean.hasGetImgsFromServer = true;
                if (resp.has_err) {
                    errorLin.setVisibility(View.VISIBLE);
                    errorTv.setText("您提交的资料有误：" + resp.error);
                    mTopItemBean.errorInfo = "您提交的资料有误：" + resp.error;
                } else {
                    errorLin.setVisibility(View.GONE);
                    mTopItemBean.errorInfo = "";
                }
                if (resp.list.size() != 0) {
                    items.addAll(resp.list);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (mTopItemBean.hasError) {
            errorLin.setVisibility(View.VISIBLE);
            errorTv.setText(mTopItemBean.errorInfo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = mGetIntent.getStringExtra("role");
                    item.type = mTopItemBean.value;
                    items.add(item);
                }
                adapter.notifyItemRangeInserted(adapter.getItemCount(), files.size());

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (String url : files) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, url, new OSSObjectKeyBean(mGetIntent.getStringExtra("role"), mTopItemBean.value, ".png"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            UploadFilesUrlReq.FileUrlBean fileUrlBean = new UploadFilesUrlReq.FileUrlBean();
                            fileUrlBean.file_id = objectKey;
                            fileUrlBean.label = mTopItemBean.value;
                            fileUrlBean.role = mGetIntent.getStringExtra("role");
                            UpdateUserInfoActivity.uploadFileUrlBeanList.add(fileUrlBean);
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                            }
                        }
                    }, new OnItemDataCallBack<Throwable>() {
                        @Override
                        public void onItemDataCallBack(Throwable data) {
                            if (finalAccount == files.size()) {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        }
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
}

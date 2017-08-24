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
import com.yusion.shanghai.yusion.bean.upload.UploadImgItemBean;
import com.yusion.shanghai.yusion.bean.upload.UploadLabelItemBean;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.utils.OssUtil;
import com.yusion.shanghai.yusion.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 如果mTopItem为null则该界面是上传户口本离婚证等界面
 */
public class UploadListActivity extends BaseActivity {

    private UploadImgListAdapter adapter;
    private Intent mGetIntent;
    private UploadLabelItemBean mTopItem;//上级页面传过来的bean
    private TextView errorTv;
    private LinearLayout errorLin;
    private List<UploadImgItemBean> imgList;
    private TitleBar titleBar;
    private String mRole;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);
        mGetIntent = getIntent();
        mRole = mGetIntent.getStringExtra("role");
        mTopItem = (UploadLabelItemBean) mGetIntent.getSerializableExtra("topItem");
        if (mTopItem != null) {
            imgList = mTopItem.img_list;
            mType = mTopItem.value;
            titleBar = initTitleBar(this, mTopItem.name).setLeftClickListener(v -> onBack());
        } else {
            //户口本等
            imgList = ((ArrayList<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList"));
            mType = mGetIntent.getStringExtra("type");
            titleBar = initTitleBar(this, mGetIntent.getStringExtra("title")).setLeftClickListener(v -> onBack());
        }
        titleBar.setRightText("编辑");
        RecyclerView rv = (RecyclerView) findViewById(R.id.upload_list_rv);
        errorTv = (TextView) findViewById(R.id.upload_list_error_tv);
        errorLin = (LinearLayout) findViewById(R.id.upload_list_error_lin);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new UploadImgListAdapter(this, imgList);
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
        if (mTopItem == null) {

        }else {
//        if (!mTopItem.hasGetImgsFromServer) {
//            //第一次进入
//            ListImgsReq req = new ListImgsReq();
//            req.role = mGetIntent.getStringExtra("role");
//            req.label = mTopItem.value;
//            req.clt_id = mGetIntent.getStringExtra("clt_id");
//            UploadApi.listImgs(this, req, resp -> {
//                mTopItem.hasGetImgsFromServer = true;
//                if (resp.has_err) {
//                    errorLin.setVisibility(View.VISIBLE);
//                    errorTv.setText("您提交的资料有误：" + resp.error);
//                    mTopItem.errorInfo = "您提交的资料有误：" + resp.error;
//                } else {
//                    errorLin.setVisibility(View.GONE);
//                    mTopItem.errorInfo = "";
//                }
//                if (resp.list.size() != 0) {
//                    items.addAll(resp.list);
//                    adapter.notifyDataSetChanged();
//                }
//            });
//        } else
            if (mTopItem.hasError) {
                errorLin.setVisibility(View.VISIBLE);
                errorTv.setText(mTopItem.errorInfo);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> files = data.getStringArrayListExtra("files");
                List<UploadImgItemBean> toAddList = new ArrayList<>();
                for (String file : files) {
                    UploadImgItemBean item = new UploadImgItemBean();
                    item.local_path = file;
                    item.role = mRole;
                    item.type = mType;
                    toAddList.add(item);
                }

                imgList.addAll(toAddList);
                adapter.notifyItemRangeInserted(adapter.getItemCount(), files.size());

                Dialog dialog = LoadingUtils.createLoadingDialog(this);
                dialog.show();
                int account = 0;
                for (UploadImgItemBean imgItemBean : toAddList) {
                    account++;
                    int finalAccount = account;
                    OssUtil.uploadOss(this, false, imgItemBean.local_path, new OSSObjectKeyBean(mRole, mType, ".png"), new OnItemDataCallBack<String>() {
                        @Override
                        public void onItemDataCallBack(String objectKey) {
                            imgItemBean.objectKey = objectKey;
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
        imgList = ((ArrayList<UploadImgItemBean>) mGetIntent.getSerializableExtra("imgList"));
        setResult(RESULT_OK, mGetIntent);
        finish();
    }
}

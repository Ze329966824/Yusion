package com.yusion.shanghai.yusion.ui.entrance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.BindingReq;
import com.yusion.shanghai.yusion.bean.auth.BindingResp;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnItemDataCallBack;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.utils.CheckMobileUtil;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.widget.CountDownButtonWrap;

public class BindingActivity extends BaseActivity {
    private EditText mBindingMobileTV;
    private EditText mBindingCodeTV;
    private Button mBindingCodeBtn;
    private Button mBindingSubmitBtn;
    private CountDownButtonWrap mCountDownBtnWrap;
    private BindingReq req;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        initTitleBar(this, "绑定手机").setLeftClickListener(v -> showDoubleCheckForExit());
        initView();

    }

    private void initView() {
        mBindingMobileTV = (EditText) findViewById(R.id.binding_mobile_edt);
        mBindingCodeTV = (EditText) findViewById(R.id.bindling_code_edt);
        mBindingCodeBtn = (Button) findViewById(R.id.bindling_code_btn);
        mBindingSubmitBtn = (Button) findViewById(R.id.binding_submit_btn);
        req = new BindingReq();
        req.reg_id = YusionApp.reg_id;
        req.source = getIntent().getStringExtra("source");
        req.open_id = getIntent().getStringExtra("open_id");
        req.unionid = getIntent().getStringExtra("unionid");
        Log.e("TAG", "Bindreq  unionid: "+req.unionid);

        if (Settings.isOnline) {
            mCountDownBtnWrap = new CountDownButtonWrap(mBindingCodeBtn, "重试", 60, 1);
        } else {
            mCountDownBtnWrap = new CountDownButtonWrap(mBindingCodeBtn, "重试", 5, 1);
        }
        mBindingCodeBtn.setOnClickListener(v -> {
            if (!CheckMobileUtil.checkMobile(mBindingMobileTV.getText().toString())) {
                Toast.makeText(BindingActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            } else {
                mCountDownBtnWrap.start();
                AuthApi.getVCode(BindingActivity.this, mBindingMobileTV.getText().toString(), data -> {
                    if (data != null) {
                        if (!Settings.isOnline) {
                            mBindingCodeTV.setText(data.verify_code);
                            req.verify_code = data.verify_code;
                            req.mobile = mBindingMobileTV.getText().toString();

                        }
                    }
                });
            }
        });


        mBindingSubmitBtn.setOnClickListener(v -> {
            if (!CheckMobileUtil.checkMobile(mBindingMobileTV.getText().toString())) {
                Toast.makeText(BindingActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mBindingCodeTV.getText())) {
                Toast.makeText(BindingActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            } else {


                AuthApi.checkOpenID(BindingActivity.this, mBindingMobileTV.getText().toString(), req.source, data -> {
                    if (data != null) {
                        if (data == 1) {
                            new AlertDialog.Builder(BindingActivity.this)
                                    .setMessage("检测到手机号：" + mBindingMobileTV.getText().toString() + "已经绑定过其他微信，是否替换？")
                                    .setPositiveButton("是", (dialog, which) -> {
                                        bind();
                                        dialog.dismiss();
                                    })
                                    .setNegativeButton("否", (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            bind();
                        }
                    }

                });
            }
        });
    }

    private void showDoubleCheckForExit() {
        new AlertDialog.Builder(this).setMessage("是否退出")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
    }

    private void getVCode() {
        mCountDownBtnWrap.start();
        AuthApi.getVCode(BindingActivity.this, mBindingMobileTV.getText().toString(), data -> {
            if (data != null) {
                if (!Settings.isOnline) {
                    mBindingCodeTV.setText(data.verify_code);
//                    req.verify_code = data.verify_code;
                    req.mobile = mBindingMobileTV.getText().toString();

                }
            }
        });
    }

    private void bind() {
        req.verify_code = mBindingCodeTV.getText().toString();
        req.mobile = mBindingMobileTV.getText().toString();
        AuthApi.binding(this, req, new OnItemDataCallBack<BindingResp>() {
            @Override
            public void onItemDataCallBack(BindingResp data) {
                if (data != null) {
                    YusionApp.TOKEN = data.token;
                    SharedPrefsUtil.getInstance(BindingActivity.this).putValue("token", YusionApp.TOKEN);
                    startActivity(new Intent(BindingActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}

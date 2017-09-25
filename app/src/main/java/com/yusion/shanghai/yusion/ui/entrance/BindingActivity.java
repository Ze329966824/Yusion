package com.yusion.shanghai.yusion.ui.entrance;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.settings.Settings;
import com.yusion.shanghai.yusion.widget.CountDownButtonWrap;

public class BindingActivity extends BaseActivity {
    private EditText mBindingMobileTV;
    private EditText mBindingCodeTV;
    private Button mBindingCodeBtn;
    private Button mBindingSubmitBtn;
    private CountDownButtonWrap mCountDownBtnWrap;

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
        if (Settings.isOnline) {
            mCountDownBtnWrap = new CountDownButtonWrap(mBindingCodeBtn, "重试", 30, 1);
        } else {
            mCountDownBtnWrap = new CountDownButtonWrap(mBindingCodeBtn, "重试", 5, 1);
        }
    }

    private void showDoubleCheckForExit() {
        new AlertDialog.Builder(this).setMessage("是否退出")
                .setPositiveButton("确定", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()).show();
    }
}

package com.yusion.shanghai.yusion.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.CheckIsAgreeReq;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.utils.SharedPrefsUtil;
import com.yusion.shanghai.yusion.widget.TitleBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgreeMentActivity extends BaseActivity {
    private TitleBar titleBar;
    private Button acceptBtn;
    private Button noAcceptBtn;
    private CheckIsAgreeReq req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_ment);
        titleBar = initTitleBar(this, "《客户信息获取、保密政策》").setLeftVisible(false);
        acceptBtn = (Button) findViewById(R.id.accept);
        noAcceptBtn = (Button) findViewById(R.id.no_accept);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req = new CheckIsAgreeReq();
                req.is_agree = true;

                AuthApi.isAgree(AgreeMentActivity.this, req, new OnCodeAndMsgCallBack() {
                    @Override
                    public void callBack(int code, String msg) {
                        if (code == 0) {
                            finish();
                        }
                    }
                });
            }
        });
        //一个是新启动的activity进入时的动画，另一个是当前activity消失时的动画
        noAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//不接受 调到mainac 显示对话框
                Intent intent = new Intent();
                intent.putExtra("noAccept", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_close);
    }
}

package com.yusion.shanghai.yusion.ui.entrance;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.yusion.shanghai.yusion.R;
import com.yusion.shanghai.yusion.YusionApp;
import com.yusion.shanghai.yusion.base.BaseActivity;
import com.yusion.shanghai.yusion.bean.auth.CheckIsAgreeReq;
import com.yusion.shanghai.yusion.retrofit.api.AuthApi;
import com.yusion.shanghai.yusion.retrofit.callback.OnCodeAndMsgCallBack;
import com.yusion.shanghai.yusion.utils.LoadingUtils;
import com.yusion.shanghai.yusion.widget.TitleBar;

public class AgreeMentActivity extends BaseActivity implements View.OnClickListener {
    private TitleBar titleBar;
    private Button acceptBtn;
    private Button noAcceptBtn;
    private CheckIsAgreeReq req;
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_ment);
        titleBar = initTitleBar(this, "《客户信息获取、保密政策》").setLeftVisible(false);
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        acceptBtn = (Button) findViewById(R.id.accept);
        noAcceptBtn = (Button) findViewById(R.id.no_accept);
        url = YusionApp.CONFIG_RESP.confident_policy_url;
        webView.loadUrl(url);

        initWebView();

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

    private void initWebView() {
        webView.setOnClickListener(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LoadingUtils.createLoadingDialog(AgreeMentActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

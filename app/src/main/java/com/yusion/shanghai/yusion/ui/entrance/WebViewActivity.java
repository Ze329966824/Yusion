package com.yusion.shanghai.yusion.ui.entrance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yusion.shanghai.yusion.R;

public class WebViewActivity extends AppCompatActivity {
    private String url;
    private String title;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webView);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        initWebView();

//        url = getIntent().getStringExtra("url");
//        title = getIntent().getStringExtra("title");
//        if (url != null && title != null) {
//            initTitleBar(this, title);
//            webView.loadUrl(url);
//        }
//        String type = getIntent().getStringExtra("type");
//
//        if (type == null)
//            return;
//        if (type.equals("Agreement")) {
//            initTitleBar(this, "用户协议");
//            webView.loadUrl("file:///android_asset/Agreement.html");
//        }

        webView.loadUrl("http://baidu.com");
    }

//    private void initWebView() {
//        webView.setOnLongClickListener(this);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//        });
//
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    LoadingUtils.dismiss();
//                } else {
//                    LoadingUtils.show(WebViewActivity.this);
//                }
//            }
//        });
//
//    }
}

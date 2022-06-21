package com.example.yin;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

public class WebViewActivity extends Activity {
    private WebView webView;
    private WebSettings webSettings;
    private String url;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_yin);
        webView=this.findViewById(R.id.web_view);
        url=getIntent().getStringExtra("url");


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });


        webView.setWebViewClient(new WebViewClient(){
            //设置是否在WebView中处理url请求，若不重新，默认会调用浏览器打开
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回true时，表示页面响应需自己处理，无法自动跳转，返回false则可以响应链接点击
                return false;
            }
        });

        webSettings =webView.getSettings();

        //如果访问的页面中要与JavaScript交互，则WebView设置为支持JavaScript
        webSettings.setJavaScriptEnabled(true);


        /**
         * 在Android5.0以下，默认采用 MIXED_ALWAYS_ALLOW 模式,        即总是允许WebView同时加载 Https 和 Http;
         * 从Android5.0开始，默认采用 MIXED_CONTENT_NEVER_ALLOW 模式，即总是不允许WebView同时加载 Https 和 Http
         */
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webView.loadUrl(url);

    }
}

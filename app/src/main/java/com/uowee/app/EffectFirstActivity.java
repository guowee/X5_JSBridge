package com.uowee.app;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by GuoWee on 2018/7/11.
 */

public class EffectFirstActivity extends AppCompatActivity {
    private ProgressBar mProgress;
    private BridgeWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_frist);
        init();
        //加载链接
        try {
            mWebView.loadUrl("http://www.baidu.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mProgress = (ProgressBar) findViewById(R.id.load_progress);
        mWebView = (BridgeWebView) findViewById(R.id.web_view);
        //设置滚动条不可用
        IX5WebViewExtension x5WebViewExtension = mWebView.getX5WebViewExtension();
        if (x5WebViewExtension != null) {
            x5WebViewExtension.setScrollBarFadingEnabled(false);
        }
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        // 允许js弹出窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置编码
        settings.setDefaultTextEncodingName("UTF-8");
        //设置支持DomStorage
        settings.setDomStorageEnabled(true);
        // 实现8倍缓存
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setAllowFileAccess(true);
        // 开启Application Cache功能
        settings.setAppCacheEnabled(true);
        //取得缓存路径
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//        String chejusPath = getFilesDir().getAbsolutePath()+ APP_CACHE_DIRNAME;
        //设置路径
        //API 19 deprecated
        settings.setDatabasePath(appCachePath);
        // 设置Application caches缓存目录
        settings.setAppCachePath(appCachePath);
        //是否启用数据库
        settings.setDatabaseEnabled(true);
        //设置存储模式 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置不支持字体缩放
        settings.setSupportZoom(false);
        //设置对应的cookie具体设置有子类重写该方法来实现
        //还有一种是加载https的URL时在5.0以上加载不了，5.0以下可以加载，这种情况可能是网页中存在非https得资源，在5.0以上是默认关闭，需要设置，
//		loadWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //加载进度改变的时候回调
                if (newProgress == 100) {
                    //隐藏掉进度条
                    mProgress.setVisibility(View.GONE);
                } else {
                    //显示进度条并加载进度
                    mProgress.setVisibility(View.VISIBLE);
                    mProgress.setProgress(newProgress);
                }
            }
        });
    }

    /**
     * 自定义的WebViewClient
     */
    class MyWebViewClient extends BridgeWebViewClient {

        public MyWebViewClient(BridgeWebView webView) {
            super(webView);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }
}

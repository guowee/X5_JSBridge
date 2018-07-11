package com.uowee.app.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.uowee.app.LoadInterface;
import com.uowee.app.R;

/**
 * webview加载进度效果
 */

public class LoadWebView extends LinearLayout {
    private ProgressBar mProgress;
    private BridgeWebView mWebView;
    private LoadInterface loadInfterface;

    public LoadWebView(Context context) {
        this(context, null);
    }

    public LoadWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        setOrientation(VERTICAL);

        initProgressBar(context);
        initWebView(context);
    }

    /**
     * 初始化进度条
     *
     * @param context
     */
    private void initProgressBar(Context context) {
        mProgress = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
        //设置最大进度
        mProgress.setMax(100);
        //设置当前进度
        mProgress.setProgress(0);
        //将progressbar添加到布局中
        addView(mProgress, LayoutParams.MATCH_PARENT, dip2px(3));
    }

    /**
     * 初始化WebView
     *
     * @param context
     */
    private void initWebView(Context context) {
        mWebView = new BridgeWebView(context);
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
        String appCachePath = context.getCacheDir().getAbsolutePath();
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
        //将webview添加到布局中
        LayoutParams lps = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
        addView(mWebView, lps);

        mWebView.setWebViewClient(new MyWebViewClient(mWebView));
        // set HadlerCallBack
        mWebView.setDefaultHandler(new myHadlerCallBack());
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
                //进行加载回调
                if (loadInfterface != null) {
                    loadInfterface.onProgressChanged(view, newProgress);
                }
            }
        });

    }

    public void goForward() {
        if (canGoForward()) {
            mWebView.goForward();
        }
    }

    public boolean canGoForward() {
        return mWebView != null && mWebView.canGoForward();
    }

    /**
     * 判断是否可以返回
     *
     * @return
     */
    public boolean canGoBack() {
        return mWebView != null && mWebView.canGoBack();
    }

    /**
     * 执行返回的动作
     */
    public void goBack() {
        if (canGoBack()) {
            mWebView.goBack();
        }
    }

    /**
     * 加载url链接
     *
     * @param url
     */
    public void loadUrl(String url) {
        if (mWebView != null) {
            //加载url链接
            try {
                mWebView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前的ProgressBar
     *
     * @return
     */
    public ProgressBar getProgressBar() {
        return mProgress;
    }

    /**
     * 获取当前的WebView
     *
     * @return
     */
    public BridgeWebView getWebView() {
        return mWebView;
    }

    /**
     * 设置监听回调
     *
     * @param listener
     */
    public void addOnWebViewLoadingListener(LoadInterface listener) {
        this.loadInfterface = listener;
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
            //加载完成回调
            if (loadInfterface != null) {
                loadInfterface.onPageFinished(view, url);
            }
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

    /**
     * 自定义回调
     */
    class myHadlerCallBack extends DefaultHandler {

        @Override
        public void handler(String data, CallBackFunction function) {

        }
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }
}

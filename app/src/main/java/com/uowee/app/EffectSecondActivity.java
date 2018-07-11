package com.uowee.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.tencent.smtt.sdk.WebView;
import com.uowee.app.widget.LoadWebView;

/**
 * Created by GuoWee on 2018/7/11.
 */

public class EffectSecondActivity extends AppCompatActivity {
    private String loadUrl = "file:///android_asset/demo.html";
    private BridgeWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_second);
        LoadWebView loadWebview = (LoadWebView) findViewById(R.id.load_webview);
        webView = loadWebview.getWebView();
        loadWebview.loadUrl(loadUrl);
        //设置加载监听
        loadWebview.addOnWebViewLoadingListener(new LoadInterface() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完成后进行js交互
                    //js调用Android方法  如果页面上面有多个的话，可以注册多个方法
                    //submitFromWeb 要和js那边定义的一样就可以了
                    webView.registerHandler("submitFromWeb", new BridgeHandler() {
                        @Override
                        public void handler(String data, CallBackFunction function) {
                            Toast.makeText(EffectSecondActivity.this, data, Toast.LENGTH_LONG).show();
                            //如果js那边调用后又 进行回调的话可以在这里进行回调的
                            function.onCallBack("submitFromWeb-----------------");
                        }
                    });

                    UserInfo user = new UserInfo();
                    user.name = "SDU";
                    user.pwd = "123456";
                    //Android发送消息给js，也可以注册多个
                    //functionInJs和js定义的要一致
                    webView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
                        @Override
                        public void onCallBack(String data) {
                            //这里也是可以进行js回传的
                        }
                    });

                    webView.send("hello");
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
    }
}

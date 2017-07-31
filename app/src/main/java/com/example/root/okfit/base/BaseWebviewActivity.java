package com.example.root.okfit.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.root.okfit.R;

import butterknife.BindView;

/**
 * Created by PengFeifei on 17-7-28.
 */

public class BaseWebviewActivity extends BaseActivity {

    private static final String LINK = "link";

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webView)
    WebView webView;

    public static void openLink(Context context, String link) {
        Intent intent = new Intent(context, BaseWebviewActivity.class);
        intent.putExtra(LINK, link);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings settings = webView.getSettings();
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(getWebClient());
        webView.setWebChromeClient(getChromeWebClient());
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        webView.loadUrl(intent.getStringExtra(LINK));
    }

    private WebViewClient getWebClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        };
    }

    private WebChromeClient getChromeWebClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                /*if (progressBar == null) {
                    return;
                }*/
                if (progress < 100) {
                    progressBar.setProgress(progress);
                    progressBar.setVisibility(View.VISIBLE);
                    return;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setToolBarTitle(title);
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        webView.setWebViewClient(null);
        webView.setWebChromeClient(null);
        super.onDestroy();
    }
}

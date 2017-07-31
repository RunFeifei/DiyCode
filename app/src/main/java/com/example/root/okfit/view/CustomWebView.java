package com.example.root.okfit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.text.TextUtils.isEmpty;

/**
 * Created by PengFeifei on 17-7-27.
 */

public class CustomWebView extends WebView {

    public CustomWebView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        setClickable(false);
        setFocusable(false);

        setHorizontalScrollBarEnabled(false);

        WebSettings settings = getSettings();
        settings.setDefaultFontSize(14);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        setWebViewClient(getWebClient());
        setWebChromeClient(getChromeWebClient());
    }

    public void loadData(String data) {
        super.loadDataWithBaseURL("", encodeContent(data), "text/html", "UTF-8", "");
    }

    //https://github.com/plusend/DiyCode/blob/master/app/src/main/java/com/plusend/diycode/view/widget/DWebView.java
    private String encodeContent(String content) {
        if (isEmpty(content) || isEmpty(content.trim())) return "";

        content = content.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
        content = content.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");

        content = content.replaceAll("<img[^>]+src=\"([^\"\'\\s]+)\"[^>]*>",
                "<img src=\"$1\" onClick=\"javascript:mWebViewImageListener.showImagePreview('$1')\"/>");
        content = content.replaceAll(
                "<a\\s+[^<>]*href=[\"\']([^\"\']+)[\"\'][^<>]*>\\s*<img\\s+src=\"([^\"\']+)\"[^<>]*/>\\s*</a>",
                "<a href=\"$1\"><img src=\"$2\"/></a>");

        content = content.replaceAll("(<table[^>]*?)\\s+border\\s*=\\s*\\S+", "$1");
        content = content.replaceAll("(<table[^>]*?)\\s+cellspacing\\s*=\\s*\\S+", "$1");
        content = content.replaceAll("(<table[^>]*?)\\s+cellpadding\\s*=\\s*\\S+", "$1");

        return String.format("<!DOCTYPE html>"
                + "<html><head>"
                + ("<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/html/css/front.css\">")
                + ("<script type=\"text/javascript\" src=\"file:///android_asset/html/js/d3.min.js\"></script>")
                + "%s"
                + "</head>"
                + "<body data-controller-name=\"topics\">"
                + "<div class=\"row\"><div class=\"col-md-9\"><div class=\"topic-detail panel panel-default\"><div class=\"panel-body markdown\">"
                + "<article>"
                + "%s"
                + "</article>"
                + "</div></div></div></div>"
                + "</body></html>", "", content);
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

        };
    }

}

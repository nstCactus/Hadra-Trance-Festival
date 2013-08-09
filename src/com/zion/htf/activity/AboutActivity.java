package com.zion.htf.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import com.zion.htf.R;

public class AboutActivity extends Activity {
    private final static String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WebView webView = (WebView)this.findViewById(R.id.webView);
        String str = this.getString(R.string.about_text);
        webView.loadData(str, "text/html", "UTF-8");
        webView.setBackgroundColor(0x00000000);

        if(Build.VERSION.SDK_INT >= 11) webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

}

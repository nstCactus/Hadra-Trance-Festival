package com.zion.htf.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.zion.htf.R;

import org.json.JSONException;

import java.math.BigDecimal;

public class AboutActivity extends Activity {
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

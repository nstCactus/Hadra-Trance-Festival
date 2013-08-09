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
    private final static String PAYPAL_CLIENT_ID = "AQKx2xDBRhC4J1VzGsLXe13IBBqX3ivEnKlSkOc07dEOyBXGcGUcecqACL3W";
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

        Intent intent = new Intent(this, PayPalService.class);

        // live: don't put any environment extra
        // sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, PAYPAL_CLIENT_ID);

        startService(intent);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void onDonatePressed(View pressed) {
        final Context context = this;

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Amount");
        alert.setMessage("How much are you willing to donate?");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                Log.v(TAG, value);

                PayPalPayment payment = new PayPalPayment(new BigDecimal(value), "USD", getString(R.string.donation_label));

                Intent intent = new Intent(context, PaymentActivity.class);

                // comment this line out for live or set to PaymentActivity.ENVIRONMENT_SANDBOX for sandbox
                intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);

                // it's important to repeat the clientId here so that the SDK has it if Android restarts your app midway through the payment UI flow.
                intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, PAYPAL_CLIENT_ID);
                intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "<sbooob@gmail.com>");
                intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "sbooob-facilitator@gmail.com");
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                startActivityForResult(intent, 0);

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        Log.e(TAG, requestCode + "");
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i(TAG, confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(TAG, "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i(TAG, "An invalid payment was submitted. Please see the docs.");
        }
    }
}

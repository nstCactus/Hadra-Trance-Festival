/*
    Copyright 2013 Yohann Bianchi

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
    or see <http://www.gnu.org/licenses/>.
 */

package com.zion.htf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.zion.htf.BuildConfig;
import com.zion.htf.R;

import org.json.JSONException;

import java.math.BigDecimal;

public class DonateActivity extends SherlockActivity implements SeekBar.OnSeekBarChangeListener{
	private final static String PAYPAL_CLIENT_ID      = "AQKx2xDBRhC4J1VzGsLXe13IBBqX3ivEnKlSkOc07dEOyBXGcGUcecqACL3W";
	private final static String PAYPAL_ENVIRONMENT    = PaymentActivity.ENVIRONMENT_SANDBOX;
	private final static String PAYPAL_RECEIVER_EMAIL = "sbooob-facilitator@gmail.com";
	private final static String PAYPAL_CURRENCY       = "USD";

	private final static String TAG = "AboutActivity";
	private SeekBar  seekBar;
	private TextView amountTextView;
	private String   amount;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_donate);

		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.seekBar = (SeekBar)this.findViewById(R.id.seekBar);
		this.seekBar.setOnSeekBarChangeListener(this);

		this.amountTextView = (TextView)this.findViewById(R.id.amount);

		Intent intent = new Intent(this, PayPalService.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PAYPAL_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, PAYPAL_CLIENT_ID);

		this.startService(intent);

		this.onProgressChanged(this.seekBar, this.seekBar.getProgress(), false);

		final ScrollView scrollView = (ScrollView)this.findViewById(R.id.donate_scrollView);
		ViewTreeObserver observer = scrollView.getViewTreeObserver();
		if(observer != null){
			observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
				@Override
				public void onGlobalLayout(){
					TextView donateJoke = (TextView)com.zion.htf.ui.DonateActivity.this.findViewById(R.id.donate_joke);
					int viewHeight = scrollView.getMeasuredHeight();
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(0, viewHeight, 0, 0);
					donateJoke.setLayoutParams(params);
				}
			});
		}
	}

	public void toggleDonateJokeImage(View button){
		ImageView image = (ImageView)this.findViewById(R.id.donate_joke_image);
		image.setVisibility(image.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onDestroy(){
		this.stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	public void onDonatePressed(View pressed){
		if(BuildConfig.DEBUG) Log.v(TAG, this.amount);

		PayPalPayment payment = new PayPalPayment(new BigDecimal(this.amount.replace(",", ".")), PAYPAL_CURRENCY, this.getString(R.string.donation_label));

		Intent intent = new Intent(this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PAYPAL_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, PAYPAL_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "<sbooob@gmail.com>");
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, PAYPAL_RECEIVER_EMAIL);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

		this.startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode == Activity.RESULT_OK){
			PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if(confirm != null){
				try{
					if(BuildConfig.DEBUG) Log.i(TAG, confirm.toJSONObject().toString(4));

					// TODO: send 'confirm' to your server for verification.
					// see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
					// for more details.
				}
				catch(JSONException e){
					Log.e(TAG, "an extremely unlikely failure occurred: ", e);
				}
			}
		}
		else if(resultCode == Activity.RESULT_CANCELED){
			if(BuildConfig.DEBUG) Log.i(TAG, "The user canceled.");
		}
		else if(resultCode == PaymentActivity.RESULT_PAYMENT_INVALID){
			Log.e(TAG, "An invalid payment was submitted. Please see the docs.");
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
		this.amount = String.format("%.2f", 0.2f + progress / 100f);
		this.amountTextView.setText("$" + this.amount);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar){

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar){

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		boolean ret = true;

		switch(item.getItemId()){
			case android.R.id.home:
				this.finish();
				break;

			default:
				ret = false;
		}

		return ret;
	}
}

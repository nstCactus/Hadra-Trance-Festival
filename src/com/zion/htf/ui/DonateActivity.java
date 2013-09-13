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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class DonateActivity extends SherlockActivity implements SeekBar.OnSeekBarChangeListener, TextWatcher{
	private final static String PAYPAL_CLIENT_ID        = BuildConfig.DEBUG ? "AQKx2xDBRhC4J1VzGsLXe13IBBqX3ivEnKlSkOc07dEOyBXGcGUcecqACL3W" : "Aa7K9hAB02q0IqcXFns7aa9C3xcwkgWGMWg0YKgcJhmq7PhPs-CRwOr7tlRg";
	private final static String PAYPAL_ENVIRONMENT      = BuildConfig.DEBUG ? PaymentActivity.ENVIRONMENT_SANDBOX : PaymentActivity.ENVIRONMENT_LIVE;
	private final static String PAYPAL_RECEIVER_EMAIL   = BuildConfig.DEBUG ? "sbooob-facilitator@gmail.com" : "sbooob@gmail.com";
	private final static String PAYPAL_DEFAULT_CURRENCY = "EUR";

    private final static int SEEKBAR_MIN = 20;
    private final static int SEEKBAR_MAX = 980;
    private final static int MIN_DONATION = DonateActivity.SEEKBAR_MIN;
    private final static int MAX_DONATION = DonateActivity.SEEKBAR_MAX + DonateActivity.SEEKBAR_MIN;

	private final static String TAG = "AboutActivity";

    private static CurrencySet currencySet = null;

	private SeekBar     seekBar;
    private EditText    amountEditText;
    private Spinner     currencySpinner;

    /** The sum to donate, in cents */
	private int amount;

    /** Timestamp (in milliseconds) of the latest controls update */
    private long lastUpdate = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_donate);

		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.seekBar = (SeekBar)this.findViewById(R.id.seekBar);
		this.seekBar.setOnSeekBarChangeListener(this);
		this.seekBar.setMax(DonateActivity.SEEKBAR_MAX);

        this.amountEditText = (EditText) this.findViewById(R.id.amount);
		this.amountEditText.addTextChangedListener(this);

		Intent intent = new Intent(this, PayPalService.class);

		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PAYPAL_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, PAYPAL_CLIENT_ID);

		this.startService(intent);

        // Initializes the amountEditText to reflects the internal value of amount
        this.amount = this.seekBar.getProgress() + SEEKBAR_MIN;
        this.updateControls();

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

		if(null == DonateActivity.currencySet){
			DonateActivity.currencySet = new CurrencySet();
			DonateActivity.currencySet.add(new Currency("US", "USD", "$"));
			DonateActivity.currencySet.add(new Currency("CA", "CAD", "$CA"));
			DonateActivity.currencySet.add(new Currency("GB", "GBP", "£"));
			DonateActivity.currencySet.add(new Currency("EU", "EUR", "€"), true);
		}

		this.currencySpinner = (Spinner)this.findViewById(R.id.currencySpinner);
		this.currencySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, DonateActivity.currencySet.getLabelArray()));
		this.currencySpinner.setSelection(DonateActivity.currencySet.findPositionByCountryCode(Locale.getDefault().getCountry()));
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
		if(BuildConfig.DEBUG) Log.v(TAG, String.valueOf(this.amount));

		int selectedIndex = this.currencySpinner.getSelectedItemPosition();
		String currencyCode = (selectedIndex >= 0 && selectedIndex < DonateActivity.currencySet.size()) ? DonateActivity.currencySet.get(selectedIndex).currencyCode : PAYPAL_DEFAULT_CURRENCY;

		PayPalPayment payment = new PayPalPayment(BigDecimal.valueOf(this.amount / 100f), currencyCode, this.getString(R.string.donation_label));

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

    private void updateControls(){
        this.seekBar.setProgress(this.amount - SEEKBAR_MIN);
        this.amountEditText.setText(String.format("%.2f", this.amount / 100f));
    }

    @Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
        if(fromUser){
		    this.amount = DonateActivity.SEEKBAR_MIN + progress;
            // Limits to roughly 30 updates per second to avoid IInputConnectionWrapper warning flood
            long now = new Date().getTime();
            if(now - this.lastUpdate > 0.3f){
                this.lastUpdate = now;
                this.updateControls();
            }
        }
	}

    @Override
	public void onStartTrackingTouch(SeekBar seekBar){

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar){
        // Forces update on seekbar release to ensure that displayed amount reflects internal value
        this.updateControls();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after){

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){

	}

	@Override
	public void afterTextChanged(Editable s){
        int previousAmount = this.amount;
        int value;
        try{
            value = (int)(Float.valueOf(s.toString().replace(',', '.')) * 100);
        }
        catch (NumberFormatException e){
            value = 0;
        }

        if(value < DonateActivity.MIN_DONATION){
            this.amount = DonateActivity.MIN_DONATION;
            previousAmount = -1;// Forces controls update
        }
        else if(value > DonateActivity.MAX_DONATION){
            this.amount = DonateActivity.MAX_DONATION;
            previousAmount = -1;// Forces controls update
        }
        else{
            this.amount = value;
        }

        int cursorPosition = this.amountEditText.getSelectionStart();
        if(previousAmount != this.amount ) this.updateControls();
        if(cursorPosition < this.amountEditText.length()) this.amountEditText.setSelection(cursorPosition);
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

	class Currency{
		public String countryCode;
		public String currencyCode;
		public String symbol;

		public Currency(String countryCode, String currencyCode, String symbol){
			this.countryCode = countryCode;
			this.currencyCode = currencyCode;
			this.symbol = symbol;
		}

		@Override
		public boolean equals(Object o){
			return o instanceof Currency
				   && ((Currency)o).symbol.equals(this.symbol)
				   && ((Currency)o).currencyCode.equals(this.currencyCode)
				   && ((Currency)o).countryCode.equals(this.countryCode);
		}
	}

	class CurrencySet extends ArrayList<Currency>{
		private int defaultCurrencyPosition = -1;

		public boolean add(Currency currency, boolean isDefault){
			boolean ret = false;

			if(!this.contains(currency)){
				ret = super.add(currency);
				if(isDefault) this.defaultCurrencyPosition = this.size() - 1;
			}
			return ret;
		}

		@Override
		public void add(int index, Currency object){
			throw new UnsupportedOperationException("Calling this method could potentially mess with the defaultCurrencyPosition. No time to write useless but safe implementation for this method.");
		}


		public int findPositionByCountryCode(String countryCode){
			return this.findPositionByCountryCode(countryCode, true);
		}

		public int findPositionByCountryCode(String countryCode, boolean returnDefault){
			boolean found = false;
			int position = -1;

			Currency item;
			Iterator<Currency> iterator = this.iterator();
			while(!found && iterator.hasNext()){
				item = iterator.next();
				position++;
				if(item.countryCode.equals(countryCode)){
					found = true;
				}
			}

			return position == -1 && returnDefault ? this.defaultCurrencyPosition : position;
		}

		public String[] getLabelArray(){
			String[] labels = new String[this.size()];

			for(int i = 0; i < this.size(); i++){
				Currency currentItem = this.get(i);
				labels[i] = currentItem.symbol;
			}

			return labels;
		}
	}
}

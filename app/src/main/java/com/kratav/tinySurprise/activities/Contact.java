package com.kratav.tinySurprise.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;

public class Contact extends BaseActivity implements OnClickListener {

	private TextView _whatsappTV, _emailTV, _phoneTV;
	private ImageView backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.CartActivityTheme);
		super.onCreate(savedInstanceState);
		actionbar();
		setContentView(R.layout.contact_us);
		
		backButton = (ImageView) findViewById(R.id.customBackButton);

		_whatsappTV = (TextView) findViewById(R.id.whatsappTV);
		_emailTV = (TextView) findViewById(R.id.emailTV);
		_phoneTV = (TextView) findViewById(R.id.callUsTV);

		_whatsappTV.setOnClickListener(this);
		_emailTV.setOnClickListener(this);
		_phoneTV.setOnClickListener(this);
		BakeryApplication application = (BakeryApplication) getApplication();
		mTracker = application.getDefaultTracker();
	}

	@Override
	protected void onResume() {
		super.onResume();
		BakeryApplication.getCart().setCouponApplied(false);
		BakeryApplication.getCart().setCostWithCoupon(BakeryApplication.getCart().getCost());
	}

	@Override
	public void onClick(View v) {
		if (v.equals(_whatsappTV)) {
			openWhatsapp();
		} else if (v.equals(_emailTV)) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("message/rfc822");
			intent.putExtra(Intent.EXTRA_SUBJECT, "");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"anjali@tinysurprise.com"});
			Intent mailer = Intent.createChooser(intent, null);
			startActivity(mailer);
		} else if (v.equals(_phoneTV)) {
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:91 9940180288"));
			startActivity(intent);
		}
	}

	public void openWhatsapp() {
		PackageManager pm = getPackageManager();
		try {
			Intent waIntent = new Intent(Intent.ACTION_SEND);
			waIntent.setType("text/plain");
			String text = "Hi tinySurprise";
			PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
			// Check if package exists or not. If not then code
			// in catch block will be called
			waIntent.setPackage("com.whatsapp");
			waIntent.putExtra(Intent.EXTRA_TEXT, text);
			startActivity(Intent.createChooser(waIntent, "Share with"));

		} catch (NameNotFoundException e) {
			Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
		}

	}
}

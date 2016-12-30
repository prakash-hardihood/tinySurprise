package com.kratav.tinySurprise.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.Toast;


import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.async.RestClient;
import com.kratav.tinySurprise.utils.GlobalApp;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class goodBuyActivity extends Activity {
	private TextView thanksTextView;
	private String senderName, senderEmail, orderID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodbye_screen);

		thanksTextView = (TextView) findViewById(R.id.thanksTextView);
		
		senderName = getIntent().getStringExtra("SENDER_NAME");
		senderEmail = getIntent().getStringExtra("SENDER_EMAIL");
		orderID = getIntent().getStringExtra(GlobalApp.ORDER_ID);
		
		StringBuilder thanksDrCooper = new StringBuilder();
		thanksDrCooper.append("Thanks ").append(senderName).append(", ")
				.append("We have sent a payment link to your email ")
				.append(senderEmail).append(".");

		int end = thanksDrCooper.indexOf(",");
		String subStart = thanksDrCooper.substring(0, end - 1);
		int start = subStart.indexOf(" ");
		Spannable wordtoSpan = new SpannableString(thanksDrCooper.toString());
		wordtoSpan.setSpan(new ForegroundColorSpan(R.color.green_blue), start,
				end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		thanksTextView.setText(wordtoSpan);
		
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				URL url = null;
				try {
					url = new URL("http://tinysurprise.com/paytiny.php?id="+params[0]);
					URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
					url = uri.toURL();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet(url.toString());
					HttpResponse response = client.execute(request);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							InputStream instream = entity.getContent();
							String result = RestClient.convertStreamToString(instream);
							JSONObject jo = new JSONObject(result);
							System.out.println(jo.getString("status"));
							return jo.getString("status");
						}
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "Err: something Went Wrong!";
			}
			protected void onPostExecute(String result) {
					Toast.makeText(goodBuyActivity.this, result, Toast.LENGTH_LONG).show();
			};
		}.execute(orderID);
	}

	
	
	
}

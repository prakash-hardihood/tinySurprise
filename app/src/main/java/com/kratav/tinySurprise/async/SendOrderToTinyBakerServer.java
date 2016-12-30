package com.kratav.tinySurprise.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;

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

public class SendOrderToTinyBakerServer extends AsyncTask<String, Void, String> {

	private Context context;
	private String change, email, orderID;

	public SendOrderToTinyBakerServer(Context context, String email, String orderId) {
		this.context = context;
		this.email = email;
		this.orderID = orderId;
	}

	@Override
	protected String doInBackground(String... params) {
		String urlx = "http://tinysurprise.com/test_mode/mobi-app/payment_status.php?txn=" + params[0] + "&txn_id=" + params[1] + "&card_type=" + params[2] + "&order_id="
				+ orderID;
		System.out.println(urlx);

		URL url = null;
		try {
			url = new URL(urlx);
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
					Log.e("SendOrder", "result: " + result);

					JSONObject jo = new JSONObject(result);
					System.out.println(jo.getString("status"));
					change = jo.getString("change");
					return change;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Err: something Went Wrong!";
	}
}

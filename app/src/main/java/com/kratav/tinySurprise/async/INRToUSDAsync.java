package com.kratav.tinySurprise.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kratav.tinySurprise.activities.CheckoutActivityForReciver;
import com.kratav.tinySurprise.activities.OrderReviewActivity;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.dialog.SweetAlertDialog;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Arun on 29-Oct-15.
 */
public class INRToUSDAsync extends AsyncTask<String, Void, String> {
    private Context context;
    private float amount;
    private String status;
    public float conAmount;

    public INRToUSDAsync(Context ctx, float amount) {
        this.context = ctx;
        this.amount = amount;
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            HttpClient client = new DefaultHttpClient();
            String urlx = "http://tinysurprise.com/test_mode/mobi-app/currencyconverter.php?amount=" + amount + "&from_currency=INR&to_currency=USD";

            URL url = new URL(urlx);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();

            System.out.println("url: " + url);

            HttpGet request = new HttpGet(url.toString());

            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String jsonResult = RestClient
                            .convertStreamToString(instream);

                    JSONObject jo = new JSONArray(jsonResult).getJSONArray(0).getJSONObject(0);
                    status = jo.getString("conversion");
                    if (status.equals("true")) {
                        conAmount = Float.parseFloat(jo.getString("con_amount"));
                    }
                    System.out.println("status: " + status);
                    return status;
                }
            }
        } catch (SocketTimeoutException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (UnknownHostException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (ConnectException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "null";
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
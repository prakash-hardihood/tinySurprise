package com.kratav.tinySurprise.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.kratav.tinySurprise.activities.CheckoutActivityForReciver;
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

public class CheckProductAvailability extends AsyncTask<String, Void, String> {
    private Context context;
    private SweetAlertDialog dialog;
    private String _daysRequired, _productName;
    public static Calendar delDate;

    public CheckProductAvailability(Context ctx, String productName) {
        this.context = ctx;
        this._productName = productName;
    }

    @Override
    protected void onPreExecute() {
        dialog = ((BaseActivity) context).showCustomDialog(context, AppConstant.CHECKING_AVAILABILITY);
        //if (!(context instanceof CheckoutActivityForReciver)) {
        dialog.show();
        // }
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            HttpClient client = new DefaultHttpClient();
            String urlx = "http://tinysurprise.com/test_mode/mobi-app/pincode.php?pincode="
                    + arg0[0] + "&sku[]=" + arg0[1];

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

                    JSONArray ja = new JSONArray(jsonResult).getJSONArray(0);
                    JSONObject jo = ja.getJSONObject(0);
                    jo = jo.getJSONObject("data");

                    Log.e("CheckProductAvailbility", "jsonResult: "
                            + jsonResult);
                    try {
                        _daysRequired = jo.getString("time taken to deliver");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("status: " + _daysRequired);
                    return _daysRequired;
                }
            }
        } catch (SocketTimeoutException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (UnknownHostException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (ConnectException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (org.json.JSONException je) {
            return "Err: Internal Server Exception.";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "null";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Err: Internal Server Exception.")) {
            dialog.setTitleText(AppConstant.OOPS)
                    .setContentText(AppConstant.INTERNAL_SERVER_ERROR)
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.WARNING_TYPE);
        } else if (result.contains("Err: Connection Timeout")) {
            dialog.setTitleText(AppConstant.OOPS)
                    .setContentText(AppConstant.CONN_TIMEOUT)
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.WARNING_TYPE);
        } else if (result.equals("null") || result.equals("false")) {
            dialog.setTitleText(AppConstant.OOPS)
                    .setContentText(AppConstant.NOT_AVAILABLE)
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.WARNING_TYPE);
        } else if (result.contains("Err:")) {
            dialog.setTitleText(AppConstant.OOPS)
                    .setContentText(result)
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.WARNING_TYPE);
        } else {
            Format formatter = new SimpleDateFormat("dd-MMM-yy", Locale.US);
            delDate = Calendar.getInstance();
            int dd = Integer.valueOf(_daysRequired);
            delDate.add(Calendar.DATE, dd);

            if (context instanceof CheckoutActivityForReciver) {
                dialog.cancel();
            } else {
                dialog.setTitleText(AppConstant.STATUS_AVAILBILITY)
                        .setContentText(_productName
                                        + " can be delivered at the requested pincode earliest by  "
                                        + formatter.format(delDate.getTime())
                                        + ". You can set the delivery date in the checkout page"
                        )
                        .setConfirmText(AppConstant.OK)
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

            }
        }
    }

    private Date addDays(Date d, int days) {
        d.setTime(d.getTime() + days * 1000 * 60 * 60 * 24);
        return d;
    }
}

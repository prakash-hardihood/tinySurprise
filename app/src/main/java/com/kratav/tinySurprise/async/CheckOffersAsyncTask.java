package com.kratav.tinySurprise.async;

import android.content.Context;
import android.os.AsyncTask;

import com.kratav.tinySurprise.bean.Marketing;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;

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

/**
 * Created by Arun on 29-Oct-15.
 */
public class CheckOffersAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;


    public CheckOffersAsyncTask(Context ctx) {
        this.context = ctx;

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            HttpClient client = new DefaultHttpClient();
            String urlx = "http://tinysurprise.com/test_mode/mobi-app/marketing.php";

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

                    Marketing marketing = BakeryApplication.getMarketing();
                    marketing.setStatus(jo.getString("is_marketing").equals("true") ? true : false);
                    marketing.setBgImage(jo.getString("bg_image"));
                    marketing.setPositiveBtnImage(jo.getString("button1"));
                    marketing.setNegativeBtnImage(jo.getString("button2"));
                    marketing.setCategory(jo.getString("category_name"));

                    System.out.println("status: " + marketing.isStatus());
                    return jo.getString("is_marketing");
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
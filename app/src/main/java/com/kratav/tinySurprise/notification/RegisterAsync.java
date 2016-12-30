package com.kratav.tinySurprise.notification;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jhon Snow on 12/8/2015.
 */
public class RegisterAsync extends AsyncTask<String, Void, String> {
    private Context context;
    private String name, email,regId;
    public RegisterAsync(Context context, String name, String email, String regId){
        this.context = context;
        this.name = name;
        this.regId = regId;
        this.email = email;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... arg0) {

        String response = "";
        try {
            URL url = new URL("http://tinysurprise.com/test_mode/mobi-app/gcm_register.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("name",name).appendQueryParameter("email",email ).appendQueryParameter("regId",regId );
            String query = builder.build().getEncodedQuery();
            System.out.println("Query is : " + query);
            writer.write(query);

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
                //throw new HttpException(responseCode + "");
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (ConnectException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Posted: " + response);
        return response;
    }
}

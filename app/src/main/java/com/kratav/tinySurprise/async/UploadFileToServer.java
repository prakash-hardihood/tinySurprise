package com.kratav.tinySurprise.async;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;


import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.dialog.SweetAlertDialog;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Arun on 23-Jul-15.
 */
public class UploadFileToServer extends AsyncTask<Void, Integer, String> {

    private Context context;
    long totalSize = 0;
    String filePath;
    public SweetAlertDialog sweetAlertDialog;
    public UploadFileToServer(Context context,String filePath){this.context = context;this.filePath = filePath;}
    @Override
    protected void onPreExecute() {
        // setting progress bar to zero
        sweetAlertDialog = ((BaseActivity)context).showCustomDialog(context, AppConstant.UPLOADING);
        sweetAlertDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Making progress bar visible
       // progressBar.setVisibility(View.VISIBLE);

        // updating progress bar value
        //progressBar.setProgress(progress[0]);

        // updating percentage value
        //txtPercentage.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://tinysurprise.com/test_mode/mobi-app/fileupload.php");

        //httppost.setHeader("Content-type","multipart/form-data;boundary="+System.currentTimeMillis());
        try {
           /* AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });*/
            MultipartEntity entity = new MultipartEntity();
            System.out.println("Path is: "+filePath);
            File sourceFile = new File(filePath);
            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));
            // Extra parameters if you want to pass to server
            entity.addPart("website",  new StringBody("www.tinySurprise.com"));
            entity.addPart("email",  new StringBody("ashu@gmail.com"));

            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }

        return responseString;

    }



}
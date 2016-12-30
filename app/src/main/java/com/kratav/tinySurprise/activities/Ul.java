package com.kratav.tinySurprise.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kratav.tinySurprise.R;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ashutosh on 7/27/2015.
 */
public class Ul extends Activity implements View.OnClickListener {
    private Button b, jB;
    private String s;
    private TextView tv;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        b = (Button) findViewById(R.id.ul);
        jB = (Button) findViewById(R.id.jsonBtn);
        tv = (TextView) findViewById(R.id.tv);

        b.setOnClickListener(this);
        jB.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(b)) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        } else if (view.equals(jB)) {
            text = "";
            try {
                postData().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                Uri selectedImageUri = data.getData();
                s = getRealPathFromURI(selectedImageUri);

                new UlAsync(s).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...

        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public AsyncTask<String, Void, String> postData() throws JSONException {
        return new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://tinysurprise.com/test_mode/mobi-app/json_test.php");
                JSONObject json = null;
                JSONObject j0 = new JSONObject();
                try {
                    j0.put("loginMode", "Ashu");
                    j0.put("reciver","priya");

                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < 5; i++) {
                        json = new JSONObject();
                        json.put("sku", "001" + i);
                        json.put("name", "Gadi.com");
                        json.put("driver", "Sunil");
                        json.put("price", "280");
                        json.put("option1", i);
                        json.put("option2", i);
                        jsonArray.put(json);
                    }
                    j0.put("products",jsonArray);
                    httppost.setHeader("json", j0.toString());
                    httppost.getParams().setParameter("jsonpost", jsonArray);
                    // Execute HTTP Post Request
                    System.out.print(jsonArray.toString());
                    HttpResponse response = httpclient.execute(httppost);

                    // for JSON:
                    if (response != null) {
                        InputStream is = response.getEntity().getContent();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        try {
                            while ((line = reader.readLine()) != null) {
                                sb.append(line + "\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Hell", sb.toString());
                        text = sb.toString();
                    }
                    return text;
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    return e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                    return e.getMessage();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                tv.setText(s);
            }
        };
    }
}


class UlAsync extends AsyncTask<String, Void, String> {
    String s;

    public UlAsync(String s) {
        this.s = s;
    }

    @Override
    protected String doInBackground(String... strings) {
        String responseString = null;
        long totalSize = 0;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://10.0.2.2/upload.php");

        try {
            MultipartEntity entity = new MultipartEntity();

            File sourceFile = new File(s);

            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));

            // Extra parameters if you want to pass to server
            entity.addPart("website", new StringBody("www.androidhive.info"));
            entity.addPart("email", new StringBody("abc@gmail.com"));

            //totalSize = entity.getContentLength();
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
        } catch (Exception e) {
            responseString = e.toString();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        System.out.println("result: " + s);
    }
}

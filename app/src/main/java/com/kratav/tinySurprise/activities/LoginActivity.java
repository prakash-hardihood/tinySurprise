package com.kratav.tinySurprise.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.async.RestClient;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static ProgressDialog mConnectionProgressDialog;
    private Button guestLogin;
    private LoginButton loginButton;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.kratav.tinySurprise", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        loginButton = (LoginButton) findViewById(R.id.login_button);
        guestLogin = (Button) findViewById(R.id.buttonLoginLogout1);

        loginButton.setReadPermissions("user_hometown", "user_location");
        //facebookLogin.setOnClickListener(this);
        guestLogin.setOnClickListener(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                Log.e("Facebook Token is: ", "" + AccessToken.getCurrentAccessToken().getToken());
                new RetriveUserDataFromFBAsync(LoginActivity.this).execute();
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("Facebook ", "Canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onClick(View v) {
        if (v.equals(guestLogin)) {
            startActivity(new Intent(LoginActivity.this, CheckoutActivityForSender.class).putExtra("LOGIN_MODE", "GUEST_LOGIN"));
        }
    }
}


class RetriveUserDataFromFBAsync extends AsyncTask<String, Void, String> {
    private static final String url = "https://graph.facebook.com/me?fields=id,name,email,location&access_token=" + AccessToken.getCurrentAccessToken().getToken();
    private Context context;


    public RetriveUserDataFromFBAsync(Context context) {
        this.context = context;
    }

    private ProgressDialog pDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(AppConstant.GETTING_USER_INFO_FB);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String jsonResult = RestClient.convertStreamToString(instream);

                    Log.e("Result from Facebook", "Result: " + jsonResult);
                    JSONObject jo = new JSONObject(jsonResult);
                    String id, name, email, location;
                    try {
                        id = jo.getString("id");
                    } catch (Exception e) {
                        id = "";
                        e.printStackTrace();
                    }
                    try {
                        name = jo.getString("name");
                    } catch (Exception e) {
                        name = "";
                        e.printStackTrace();
                    }
                    try {
                        email = jo.getString("email");
                    } catch (Exception e) {
                        email = "";
                        e.printStackTrace();
                    }
                    try {
                        jo = jo.getJSONObject("location");
                        location = jo.getString("name");
                    } catch (Exception e) {
                        location = "";
                        e.printStackTrace();
                    }
                    SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
                    Editor editor = pref.edit();
                    editor.putString("Username", name);
                    editor.putString("id", id);
                    editor.putString("userLocation", location);
                    editor.putString("userEmail", email);
                    editor.putString("loginType", "fb");
                    editor.commit();
                    return "success";
                }
            }
            return null;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (ConnectException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (org.json.JSONException je) {
            return "Err: Internal Server Exception.";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals(null) || result.equals("null")) {
            ((LoginActivity) context).showCustomDialog(context, AppConstant.OOPS, AppConstant.INTERNAL_SERVER_ERROR, true).show();
        } else if (result.equals("Err: Internal Server Exception.")) {
            ((LoginActivity) context).showCustomDialog(context, AppConstant.OOPS, AppConstant.INTERNAL_SERVER_ERROR, true).show();
        } else if (result.contains("Err: Connection Timeout")) {
            ((LoginActivity) context).showCustomDialog(context, AppConstant.OOPS, AppConstant.CONN_TIMEOUT, true).show();
        } else if (result.equals("success")) {
            //LOGOUT
            LoginManager.getInstance().logOut();
            context.startActivity(new Intent(context, CheckoutActivityForSender.class).putExtra("LOGIN_MODE", "fb_id"));
            ((Activity) context).finish();
        } else {
            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG).show();
        }
        if (pDialog.isShowing()) pDialog.dismiss();
    }
}


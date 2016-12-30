package com.kratav.tinySurprise.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.async.SendOrderToTinyBakerServer;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;
import com.kratav.tinySurprise.utils.ServiceHandler;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends BaseActivity {
    private ProgressDialog dialog;
    private Intent mainIntent;
    private String encVal;
    private TextView msg1;
    private Button msg2, contShopping;
    private LinearLayout statusLinearLayout;
    private WebView webview;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        webview = (WebView) findViewById(R.id.webview);
        msg1 = (TextView) findViewById(R.id.msg1TextView);
        msg2 = (Button) findViewById(R.id.msg2Btn);
        contShopping = (Button) findViewById(R.id.msg3Btn);
        msg2.setOnClickListener(this);
        contShopping.setOnClickListener(this);
        statusLinearLayout = (LinearLayout) findViewById(R.id.transStatusLL);

        mainIntent = getIntent();
        if (mainIntent.hasExtra("PaymentType") && mainIntent.getStringExtra("PaymentType").equals("PAYPAL")) {
            msg2.setVisibility(View.GONE);
            webview.setVisibility(View.GONE);
            statusLinearLayout.setVisibility(View.VISIBLE);
            try {
                JSONObject jo = new JSONObject(mainIntent.getStringExtra("paymentInfo")).getJSONObject("response");
                String s = jo.getString("id");
                msg1.setText("Congratulations, Your payment is successful. Please note your payment ID \n" + s);
            } catch (Exception e) {
                e.printStackTrace();
                msg1.setText("Your payment is successful. But we are unable to show the payment id because of some technical issue. It will be very helpful, if ou write about this us.");
            }
        } else {
            new RenderView().execute();
        }
        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.equals(msg2) && msg2.getVisibility() == View.VISIBLE && msg2.getText().toString().trim().equals("RETRY PAYMENT")) {
            if (mainIntent.hasExtra("PaymentType") && mainIntent.getStringExtra("PaymentType").equals("PAYPAL")) {
                finish();
            } else {
                new RenderView().execute();
            }
        } else if (v.equals(msg2)) {
            goToMainActivity();
        } else if (v.equals(contShopping)) {
            goToHomeActivity();
        }
    }

    private void goToMainActivity() {
        if (msg2.getVisibility() == View.VISIBLE && msg2.getText().toString().trim().equals("GO TO THE HOME SCREEN")) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    private void goToHomeActivity() {
        if (contShopping.getVisibility() == View.VISIBLE && contShopping.getText().toString().trim().equals("CONTINUE SHOPPING")) {
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            if (statusLinearLayout.getVisibility() == View.VISIBLE)
                statusLinearLayout.setVisibility(View.GONE);
            dialog = new ProgressDialog(WebViewActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(GlobalApp.ACCESS_CODE, mainIntent.getStringExtra(GlobalApp.ACCESS_CODE)));
            params.add(new BasicNameValuePair(GlobalApp.ORDER_ID, mainIntent.getStringExtra(GlobalApp.ORDER_ID)));
            String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(GlobalApp.RSA_KEY_URL), ServiceHandler.POST, params);

            if (!GlobalApp.chkNull(vResponse).equals("") && GlobalApp.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(GlobalApp.addToPostParams(GlobalApp.AMOUNT, mainIntent.getStringExtra(GlobalApp.AMOUNT)));
                vEncVal.append(GlobalApp.addToPostParams(GlobalApp.CURRENCY, mainIntent.getStringExtra(GlobalApp.CURRENCY)));
                encVal = GlobalApp.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    // process the html as needed by the app
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webview.setVisibility(View.GONE);
                            statusLinearLayout.setVisibility(View.VISIBLE);
                        }
                    });

                    if (html.indexOf("Failure") != -1) {
                        msg1.setText("Sorry your transaction was unsuccesful. Your order details are saved and you can retry payment here");
                        msg2.setText("RETRY PAYMENT");
                        findViewById(R.id.transStatusLL).setVisibility(View.VISIBLE);
                    } else if (html.indexOf("Success") != -1) {
                        new SendOrderToTinyBakerServer(WebViewActivity.this, mainIntent.getStringExtra(GlobalApp.BILLING_EMAIL), mainIntent.getStringExtra(GlobalApp.ORDER_ID)) {
                            @Override
                            protected void onPostExecute(String result) {
                                super.onPostExecute(result);

                                if (result.equals("success")) {
                                    msg1.setText("Your transaction is succesful. The order details have been emailed to you at " + mainIntent.getStringExtra(GlobalApp.BILLING_EMAIL) + " \nThis is your order id " + mainIntent.getStringExtra(GlobalApp.ORDER_ID));
                                    contShopping.setVisibility(View.GONE);
                                    BakeryApplication.getCart().setCartEmpty();
                                    msg2.setText("GO TO THE HOME SCREEN");
                                } else {
                                    msg1.setText("Please Contact to tinySurprise.com");
                                }
                            }
                        }
                                .execute("success", "", "ccavanue");
                    } else if (html.indexOf("Aborted") != -1) {
                        msg1.setText("Sorry your transaction was unsuccesful. Your order details are saved and you can retry payment here");
                        msg2.setText("RETRY PAYMENT");
                    } else {
                        msg1.setText("Sorry your transaction was unsuccesful. Your order details are saved and you can retry payment here");
                        msg2.setText("RETRY PAYMENT");
                    }

                }
            }


            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    if (dialog.isShowing())
                        dialog.dismiss();
                    webview.setVisibility(View.VISIBLE);
                    if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e("WebViewActivity", "OnReciveError");
                }
            });

			/*
             * An instance of this class will be registered as a JavaScript interface
			 */
            try {
                StringBuffer params = new StringBuffer();
                params.append(GlobalApp.addToPostParams(GlobalApp.ACCESS_CODE, mainIntent.getStringExtra(GlobalApp.ACCESS_CODE)));
                params.append(GlobalApp.addToPostParams(GlobalApp.MERCHANT_ID, mainIntent.getStringExtra(GlobalApp.MERCHANT_ID)));
                params.append(GlobalApp.addToPostParams(GlobalApp.ORDER_ID, mainIntent.getStringExtra(GlobalApp.ORDER_ID)));
                params.append(GlobalApp.addToPostParams(GlobalApp.REDIRECT_URL, mainIntent.getStringExtra(GlobalApp.REDIRECT_URL)));
                params.append(GlobalApp.addToPostParams(GlobalApp.CANCEL_URL, mainIntent.getStringExtra(GlobalApp.CANCEL_URL)));
                params.append(GlobalApp.addToPostParams(GlobalApp.LANGUAGE, "EN"));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_NAME, mainIntent.getStringExtra(GlobalApp.BILLING_NAME)));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_ADDRESS, mainIntent.getStringExtra(GlobalApp.BILLING_ADDRESS)));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_CITY, mainIntent.getStringExtra(GlobalApp.BILLING_CITY)));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_STATE, mainIntent.getStringExtra(GlobalApp.BILLING_STATE)));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_ZIP, mainIntent.getStringExtra(GlobalApp.BILLING_ZIP)));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_COUNTRY, mainIntent.getStringExtra(GlobalApp.BILLING_COUNTRY)));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_TEL, mainIntent.getStringExtra(GlobalApp.BILLING_TEL)));
                params.append(GlobalApp.addToPostParams(GlobalApp.BILLING_EMAIL, mainIntent.getStringExtra(GlobalApp.BILLING_EMAIL)));
                params.append(GlobalApp.addToPostParams(GlobalApp.DELIVERY_NAME, mainIntent.getStringExtra(GlobalApp.DELIVERY_NAME)));
                params.append(GlobalApp.addToPostParams(GlobalApp.DELIVERY_ADDRESS, mainIntent.getStringExtra(GlobalApp.DELIVERY_ADDRESS)));
                params.append(GlobalApp.addToPostParams(GlobalApp.DELIVERY_CITY, mainIntent.getStringExtra(GlobalApp.DELIVERY_CITY)));
                params.append(GlobalApp.addToPostParams(GlobalApp.DELIVERY_STATE, mainIntent.getStringExtra(GlobalApp.DELIVERY_STATE)));
                params.append(GlobalApp.addToPostParams(GlobalApp.DELIVERY_ZIP, mainIntent.getStringExtra(GlobalApp.DELIVERY_ZIP)));
                params.append(GlobalApp.addToPostParams(GlobalApp.DELIVERY_COUNTRY, mainIntent.getStringExtra(GlobalApp.DELIVERY_COUNTRY)));
                params.append(GlobalApp.addToPostParams(GlobalApp.DELIVERY_TEL, mainIntent.getStringExtra(GlobalApp.DELIVERY_TEL)));
                params.append(GlobalApp.addToPostParams(GlobalApp.ENC_VAL, URLEncoder.encode(encVal)));
                String vPostParams = params.substring(0, params.length() - 1);
                webview.postUrl(GlobalApp.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                finish();
                //findViewById(R.id.transStatusLL).setVisibility(View.VISIBLE);
                //Log.e("WebViewActivity","Exception occured while opening webview.");
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
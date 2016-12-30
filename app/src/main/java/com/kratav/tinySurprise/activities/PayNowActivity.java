package com.kratav.tinySurprise.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpException;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.OrderReviewListAdapter;
import com.kratav.tinySurprise.async.INRToUSDAsync;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by DELL on 12/28/2016.
 */
public class PayNowActivity extends BaseActivity {

    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String CONFIG_CLIENT_ID = "AYDt_KkBsVFJsgG0CVJs0_jGR2EsRO-Ucb2X5zOXl2Cf37mZQ8c4P6ylHO8qSJfT1A00mmojMTfzHBY4";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    public  static final int RequestPermissionCode  = 1 ;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName(AppConstant.TINYSURPRISE)
            .merchantPrivacyPolicyUri(Uri.parse("http://tiny" +
                    "surprise.com/privacy-policy"))
            .merchantUserAgreementUri(Uri.parse("http://tinysurprise.com/terms-and-conditions"));

    private String senderCountry = "India";
    private String deviceId;

    private ImageView backButton4;
    private TextView checkout_text, title1, title2, title3, title4, heading1, heading2, ccavenue_text, paypal_text;
    private RelativeLayout ccAvenue, PayPal;
    private RadioGroup radioGroup1;

    private Button payNow;
    String bc;
    private RadioButton radio_ccAvenue, radio_PayPal;

    private String senderFName, senderLName, senderEmail, senderAddress,
            senderPhone, senderCity, senderState, senderPin, reciverName,
            reciverZip, reciverPhone, reciverCity, reciverAddress,
            reciverState, senderMsg, deliveryDate,shippingType, amount;
    private String loginMode;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.HomeActivityTheme);
        super.onCreate(savedInstanceState);
        //  actionbar();
        setContentView(R.layout.checkout_4);
        paypal();

        backButton4 = (ImageView) findViewById(R.id.backButtonCheckout4);
        checkout_text = (TextView) findViewById(R.id.checkout_text4);
        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);
        title4 = (TextView) findViewById(R.id.title4);
        heading1 = (TextView) findViewById(R.id.heading1);
        heading2 = (TextView) findViewById(R.id.heading2);
        ccavenue_text = (TextView) findViewById(R.id.ccavenue_text);
        paypal_text = (TextView) findViewById(R.id.paypal_text);


        ccAvenue = (RelativeLayout) findViewById(R.id.option_ccavenue);
        PayPal = (RelativeLayout) findViewById(R.id.option_paypal);

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        payNow = (Button) findViewById(R.id.payNowButton1);
        radio_ccAvenue = (RadioButton) findViewById(R.id.radio0);
        radio_PayPal = (RadioButton) findViewById(R.id.radio1);

        senderFName = getIntent().getStringExtra("SENDER_NAME");
        senderLName = getIntent().getStringExtra("SLAST_NAME");
        senderEmail = getIntent().getStringExtra("SENDER_EMAIL");
        senderAddress = getIntent().getStringExtra("SENDER_ADDRESS");
        senderCity = getIntent().getStringExtra("SENDER_CITY");
        senderState = getIntent().getStringExtra("SENDER_STATE");
        senderPin = getIntent().getStringExtra("SENDER_ZIP");
        senderPhone = getIntent().getStringExtra("SENDER_PHONE");
        reciverName = getIntent().getStringExtra("RECIVER_NAME");
        reciverAddress = getIntent().getStringExtra("RECIVER_ADDRESS");
        reciverCity = getIntent().getStringExtra("RECIVER_CITY");
        reciverState = getIntent().getStringExtra("RECIVER_STATE");
        reciverPhone = getIntent().getStringExtra("RECIVER_PHONE");
        reciverZip = getIntent().getStringExtra("RECIVER_ZIP");

        senderMsg = getIntent().getStringExtra("SENDER_MSG");
        deliveryDate = getIntent().getStringExtra("DELIVERY_DATE");
        loginMode = getIntent().getStringExtra("LOGIN_MODE");
        shippingType = getIntent().getStringExtra("SHIPPING_TYPE");
        amount = getIntent().getStringExtra("AMOUNT");
        Log.e("OrderReviewActivirty", "DilevaryDate: " + deliveryDate
                + " sender city: " + senderCity + " : " + loginMode);


        try {
            checkout_text.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_HEAVY));
            title1.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_SEMI));
            title2.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_SEMI));
            title3.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_SEMI));
            title4.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_SEMI));
            heading1.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_HEAVY));
            heading2.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_SEMI));
            ccavenue_text.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_REG));
            paypal_text.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_REG));
            payNow.setTypeface(GlobalApp.getFont(
                    PayNowActivity.this, GlobalApp.LATO_HEAVY));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(radio_ccAvenue.isChecked()==true){
            radio_PayPal.setChecked(false);
        }

        if(radio_PayPal.isChecked()==true){
            radio_ccAvenue.setChecked(false);
        }
        ccAvenue.setOnClickListener(this);
        PayPal.setOnClickListener(this);
        backButton4.setOnClickListener(this);
        payNow.setOnClickListener(this);


    }

    public void onClick(final View arg0) {

        ccAvenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_ccAvenue.setChecked(true);
                radio_PayPal.setChecked(false);
            }
        });

        PayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_PayPal.setChecked(true);
                radio_ccAvenue.setChecked(false);
            }
        });

        backButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = radioGroup1.getCheckedRadioButtonId();

                if (radio_ccAvenue.isChecked() == true && radio_PayPal.isChecked() == false) {

                    // CCAVENUE
                    getOrderIDAsync();

                } else if (radio_PayPal.isChecked() == true && radio_ccAvenue.isChecked() == false) {

                    //PAYPAL
                    if (BakeryApplication.getCart().isCouponApplied())
                        bc = "" + BakeryApplication.getCart().getCostWithCoupon();
                    else
                        bc = "" + BakeryApplication.getCart().getCost();

                    new INRToUSDAsync(PayNowActivity.this, Float.parseFloat(bc)) {
                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            if (result.equals("Err: Internal Server Exception.")) {

                            } else if (result.contains("Err: Connection Timeout")) {

                            } else if (result.equals("null") || result.equals("false")) {

                            } else if (result.contains("Err:")) {

                            } else {
                                invokePaypal(conAmount);
                            }
                        }
                    }.execute();

                } else if (radio_ccAvenue.isChecked() == true && radio_PayPal.isChecked() == true) {

                    //Both items selected
                    Toast.makeText(getApplicationContext(),
                            "Please select one valid option", Toast.LENGTH_LONG)
                            .show();

                } else {

                    // No item selected
                    Toast.makeText(getApplicationContext(),
                            "Please select a payment option", Toast.LENGTH_LONG)
                            .show();

                }

            }
        });

        }

    private void paypal() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }


    private void getOrderIDAsync() {
        new GetOrderID() {
            protected void onPostExecute(String result) {
                if (result.equals("") || result.equals(null) || result.equals("null")) {
                    showCustomDialog(PayNowActivity.this, AppConstant.OOPS, AppConstant.INTERNAL_SERVER_ERROR, true).show();
                } else if (result.equals("Err: Internal Server Exception.")) {
                    showCustomDialog(PayNowActivity.this, AppConstant.OOPS, AppConstant.INTERNAL_SERVER_ERROR, true).show();
                } else if (result.contains("Err: Connection Timeout")) {
                    showCustomDialog(PayNowActivity.this, AppConstant.OOPS, AppConstant.CONN_TIMEOUT, true).show();
                } else {
                    try {
                        JSONObject ja = new JSONArray(result).getJSONObject(0);
                        JSONObject ja1 = ja.getJSONObject("request");
                        String msg = "", orderID = "", payment_status = "";
                        try {
                            msg = ja1.getString("message");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ja1 = ja.getJSONObject("data");
                        try {
                            orderID = ja1.getString("Order Id");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            payment_status = ja.getString("Payment_status");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(PayNowActivity.this,
                                WebViewActivity.class);
                        intent.putExtra(GlobalApp.ORDER_ID,
                                GlobalApp.chkNull(orderID).toString().trim());
                        intent.putExtra(GlobalApp.ACCESS_CODE,
                                GlobalApp.chkNull("AVQY03BK02AJ83YQJA").toString());
                        intent.putExtra(GlobalApp.MERCHANT_ID,
                                GlobalApp.chkNull("44756").toString());
                        intent.putExtra(GlobalApp.CURRENCY, GlobalApp
                                .chkNull("INR").toString());
                        intent.putExtra(GlobalApp.AMOUNT, GlobalApp.chkNull(Float.parseFloat(amount))
                                .toString().trim());
                        intent.putExtra(GlobalApp.BILLING_NAME,
                                GlobalApp.chkNull(senderFName + " " + senderLName)
                                        .toString().trim());
                        intent.putExtra(GlobalApp.BILLING_ADDRESS, GlobalApp
                                .chkNull(senderAddress).toString().trim());

                        intent.putExtra(GlobalApp.BILLING_COUNTRY, GlobalApp
                                .chkNull("India").toString().trim());
                        intent.putExtra(GlobalApp.BILLING_STATE,
                                GlobalApp.chkNull(senderState).toString().trim());
                        intent.putExtra(GlobalApp.BILLING_CITY,
                                GlobalApp.chkNull(senderCity).toString().trim());
                        intent.putExtra(GlobalApp.BILLING_ZIP,
                                GlobalApp.chkNull(senderPin).toString().trim());

                        intent.putExtra(GlobalApp.BILLING_TEL,
                                GlobalApp.chkNull(senderPhone).toString().trim());
                        intent.putExtra(GlobalApp.BILLING_EMAIL,
                                GlobalApp.chkNull(senderEmail).toString().trim());
                        intent.putExtra(GlobalApp.DELIVERY_NAME,
                                GlobalApp.chkNull(reciverName).toString().trim());
                        intent.putExtra(GlobalApp.DELIVERY_ADDRESS, GlobalApp
                                .chkNull(reciverAddress).toString().trim());

                        intent.putExtra(GlobalApp.DELIVERY_COUNTRY, GlobalApp
                                .chkNull("India").toString().trim());
                        intent.putExtra(GlobalApp.DELIVERY_STATE, GlobalApp
                                .chkNull(reciverState).toString().trim());

                        intent.putExtra(GlobalApp.DELIVERY_CITY,
                                GlobalApp.chkNull(reciverCity).toString().trim());
                        intent.putExtra(GlobalApp.DELIVERY_ZIP,
                                GlobalApp.chkNull(reciverZip).toString().trim());
                        intent.putExtra(GlobalApp.DELIVERY_TEL,
                                GlobalApp.chkNull(reciverPhone).toString().trim());

                        intent.putExtra(
                                GlobalApp.REDIRECT_URL,
                                GlobalApp
                                        .chkNull(
                                                "http://tinysurprise.com:8080/test_payment/ccavResponseHandler.jsp")
                                        .toString().trim());
                        intent.putExtra(
                                GlobalApp.CANCEL_URL,
                                GlobalApp
                                        .chkNull(
                                                "http://tinysurprise.com:8080/test_payment/ccavResponseHandler.jsp")
                                        .toString().trim());
                        intent.putExtra(
                                GlobalApp.RSA_KEY_URL,
                                GlobalApp
                                        .chkNull(
                                                "http://tinysurprise.com:8080/test_payment/GetRSA.jsp")
                                        .toString().trim());
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }


    class GetOrderID extends AsyncTask<String, Void, String> {
        String shippingType1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            shippingType1 = shippingType;
        }

        @Override
        protected String doInBackground(String... arg0) {
            JSONObject json = null;
            JSONObject j0 = new JSONObject();

            putJSON(j0, "sFname", senderFName);
            putJSON(j0, "sLname", senderLName);
            putJSON(j0, "sEmail", senderEmail);
            putJSON(j0, "sAddress", senderAddress);
            putJSON(j0, "sCity", senderCity);
            putJSON(j0, "sState", senderState);
            putJSON(j0, "senderCountry", senderCountry);
            putJSON(j0, "sZipcode", senderPin);
            putJSON(j0, "sTeliphone", senderPhone);
            putJSON(j0, "msg_on_card", senderMsg);
            if (loginMode.equals("GUEST_LOGIN")) putJSON(j0, "loginMode", "guest");
            else {
                putJSON(j0, "loginMode", loginMode);
                SharedPreferences prefs = PayNowActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String id = prefs.getString("id", null);
                putJSON(j0, "fb_id", id);
            }


            deviceId = getDeviceID();

            putJSON(j0, "rName", reciverName);
            putJSON(j0, "rAddress", reciverAddress);
            putJSON(j0, "rCity", reciverCity);
            putJSON(j0, "rState", reciverState);
            putJSON(j0, "rMobile", reciverPhone);
            putJSON(j0, "rZipCode", reciverZip);
            putJSON(j0, "deliveryDate", deliveryDate);
            putJSON(j0, "app_platform", GlobalApp.getDeviceName() + " : " + GlobalApp.getAndroidVersion() + " : " + deviceId);
            putJSON(j0, "shippping_price", BakeryApplication.getCart().getShippingCost());
            putJSON(j0, "cart_total", BakeryApplication.getCart().getCost());
            putJSON(j0, "cart_after_discount", BakeryApplication.getCart().getCostWithCoupon());
            putJSON(j0, "coupon_applied", BakeryApplication.getCart().isCouponApplied());
            putJSON(j0, "coupon_code", BakeryApplication.getCart().getAppliedCoupon());
            putJSON(j0, "discount_value", BakeryApplication.getCart().getAppliedCouponValue());
            putJSON(j0, "discount_type", BakeryApplication.getCart().getAppliedCouponType());
            putJSON(j0, "shipping_type", shippingType1);


            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < BakeryApplication.getCart().getCartItems()
                    .size(); i++) {
                Product p = BakeryApplication.getCart().getCartItems().get(i);
                json = new JSONObject();
                putJSON(json, "deldate", p.getTimeToDeliver());
                putJSON(json, "sku", p.getProductCode());

                putJSON(json, "location", "");
                putJSON(json, "qty", p.getQty());
                putJSON(j0, "uploadPicture", p.getPhotoPath());
                JSONObject jo = new JSONObject();
                for (int j = 0; j < p.getOptionSelected().size(); j++) {
                    putJSON(jo, p.getOptionSelected().get(j).getTitle(), p.getOptionSelected().get(j).getValue()); //TODO
                }
                putJSON(json, "Options", jo);
                jsonArray.put(json);
            }
            putJSON(j0, "products", jsonArray);
            String response = "";
            try {
                URL url = new URL("http://tinysurprise.com/test_mode/mobi-app/localtest.php");

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
                        .appendQueryParameter("jsonpost", j0.toString());
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
                    throw new HttpException(responseCode + "");
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

    private String getDeviceID() {
        String deviceId = "";
        String dump = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Device Id", dump);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(PayNowActivity.this,
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, RequestPermissionCode);
            deviceId = "";
        }
        else {
            //Type the function


            final TelephonyManager mTelephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

                deviceId = Settings.Secure.getString(getApplicationContext()
                        .getContentResolver(), Settings.Secure.ANDROID_ID);


        }

        return deviceId;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                    getDeviceID();
                }
                break;

            default:
                break;
        }
    }




    public void invokePaypal(float price) {
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE, price);
        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */
        Intent intent = new Intent(PayNowActivity.this, PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent, float price) {
        String s = "";
        for (int i = 0; i < BakeryApplication.getCart().getCartItems()
                .size(); i++) {
            Product p = BakeryApplication.getCart().getCartItems().get(i);
            s += p.getProductName() + "\n";
        }

        return new PayPalPayment(new BigDecimal(price), "USD", s, paymentIntent);
    }

}

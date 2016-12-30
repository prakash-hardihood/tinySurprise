package com.kratav.tinySurprise.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.OrderReviewListAdapter;
import com.kratav.tinySurprise.async.INRToUSDAsync;
import com.kratav.tinySurprise.async.RestClient;
import com.kratav.tinySurprise.async.SendOrderToTinyBakerServer;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import com.readystatesoftware.viewbadger.BadgeView;



public class OrderReviewActivity extends BaseActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = "paymentExample";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static final String CONFIG_CLIENT_ID = "AYDt_KkBsVFJsgG0CVJs0_jGR2EsRO-Ucb2X5zOXl2Cf37mZQ8c4P6ylHO8qSJfT1A00mmojMTfzHBY4";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName(AppConstant.TINYSURPRISE)
            .merchantPrivacyPolicyUri(Uri.parse("http://tiny" +
                    "surprise.com/privacy-policy"))
            .merchantUserAgreementUri(Uri.parse("http://tinysurprise.com/terms-and-conditions"));

    private TextView senderNameTextView, senderAddressTextView,
            senderCityTextView, senderStateTextView, senderPinTextView,
            senderPhoneNo, senderCardMsgTextView,
            reciverNameTextView, reciverAddressTextView, reciverCityTextView,
            reciverZipCodeTextView,
            reciverPhoneNoTextView, reciverDatePickTextView,
            tot, tot1, tot_text;
    public Button /* sendPaymentLinkButton */payNowButton, ok;
    public RadioGroup rPayment;
    private String senderCountry = "India";
    private ListView orderReviewListView;
    private static View orderReviewFooterView;
    private String senderFName, senderLName, senderEmail, senderAddress,
            senderPhone, senderCity, senderState, senderPin, reciverName,
            reciverZip, reciverPhone, reciverCity, reciverAddress,
            reciverState, senderMsg, deliveryDate, amount_payable;
    private OrderReviewListAdapter adapter;
    private ImageView backButton;
    private String loginMode;
    RadioGroup rg;
    private Effectstype effect;
    public int x,y;
    private TextView checkout_text, title1, title2, title3, title4, heading1, ttlOrderReviewTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.HomeActivityTheme);
        super.onCreate(savedInstanceState);
      //  actionbar();
        setContentView(R.layout.confirmpage);


        backButton = (ImageView) findViewById(R.id.backButtonCheckout3);
        checkout_text = (TextView) findViewById(R.id.checkout_text3);
        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);
        title4 = (TextView) findViewById(R.id.title4);
        heading1 = (TextView) findViewById(R.id.heading1);
        ttlOrderReviewTextView = (TextView) findViewById(R.id.ttlPriceOrderReview);


        checkout_text.setTypeface(GlobalApp.getFont(OrderReviewActivity.this, GlobalApp.LATO_HEAVY));
        title1.setTypeface(GlobalApp.getFont(OrderReviewActivity.this, GlobalApp.LATO_SEMI));
        title2.setTypeface(GlobalApp.getFont(OrderReviewActivity.this, GlobalApp.LATO_SEMI));
        title3.setTypeface(GlobalApp.getFont(OrderReviewActivity.this, GlobalApp.LATO_SEMI));
        title4.setTypeface(GlobalApp.getFont(OrderReviewActivity.this, GlobalApp.LATO_SEMI));


        orderReviewListView = (ListView) findViewById(R.id.orderReviewListView);
        adapter = new OrderReviewListAdapter(this, BakeryApplication.getCart().getCartItems());

        orderReviewFooterView = ((LayoutInflater) OrderReviewActivity.this
                .getSystemService(OrderReviewActivity.this.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.order_review_footer, null, false);

        orderReviewListView.addFooterView(orderReviewFooterView);


        senderNameTextView = (TextView) orderReviewFooterView.findViewById(R.id.senderNameConfirm);
        senderAddressTextView = (TextView) orderReviewFooterView.findViewById(R.id.senderAddressConfirm);
        tot = (TextView) orderReviewFooterView.findViewById(R.id.tot);
        tot1 = (TextView) orderReviewFooterView.findViewById(R.id.ttlPrice);
        tot_text = (TextView) orderReviewFooterView.findViewById(R.id.val);
        senderCityTextView = (TextView) orderReviewFooterView.findViewById(R.id.senderCityConfirm);
        senderPinTextView = (TextView) orderReviewFooterView.findViewById(R.id.senderZipConfirm);
        senderPhoneNo = (TextView) orderReviewFooterView.findViewById(R.id.senderPhoneConfirm);
        senderCardMsgTextView = (TextView) orderReviewFooterView.findViewById(R.id.senderMessgeConfirm);
        reciverNameTextView = (TextView) orderReviewFooterView.findViewById(R.id.RName1);
        reciverAddressTextView = (TextView) orderReviewFooterView.findViewById(R.id.rAddress1);
        reciverCityTextView = (TextView) orderReviewFooterView.findViewById(R.id.RCity1);
        reciverZipCodeTextView = (TextView) orderReviewFooterView.findViewById(R.id.rZipCodeCheckout1);
        reciverPhoneNoTextView = (TextView) orderReviewFooterView.findViewById(R.id.rPhoneNoCheckout1);
        reciverDatePickTextView = (TextView) orderReviewFooterView.findViewById(R.id.dateOfDelievery);

        // sendPaymentLinkButton = (Button) findViewById(R.id.sendPaymentLinkByEmailButton);
        payNowButton = (Button) findViewById(R.id.payNowButton);
        payNowButton.setTypeface(GlobalApp.getFont(OrderReviewActivity.this, GlobalApp.LATO_HEAVY));
        payNowButton.setBackgroundDrawable(getBackgroundStateListDrawable(this, R.color.button_orange, R.color.order_review_selector, 0));
        // sendPaymentLinkButton.setOnClickListener(this);
        payNowButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

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

        Log.e("OrderReviewActivirty", "DilevaryDate: " + deliveryDate
                + " sender city: " + senderCity + " : " + loginMode);


        if (BakeryApplication.getCart().isCouponApplied())
            ttlOrderReviewTextView.setText("" + BakeryApplication.getCart().getCostWithCoupon());

        else
            ttlOrderReviewTextView.setText("" + BakeryApplication.getCart().getCost());

        senderNameTextView.setText(senderFName + " " + senderLName);
        senderAddressTextView.setText(senderAddress);
        senderCityTextView.setText(senderCity);
        senderPinTextView.setText(senderPin);
        senderPhoneNo.setText(senderPhone);
        senderCardMsgTextView.setText(senderMsg);// TODO

        reciverNameTextView.setText(reciverName);
        reciverZipCodeTextView.setText(reciverZip);
        reciverPhoneNoTextView.setText(reciverPhone);
        reciverCityTextView.setText(reciverCity);
        reciverAddressTextView.setText(reciverAddress);
        reciverDatePickTextView.setText(deliveryDate);

        Log.e("OrderReviewActivirty", "sender city" + senderCityTextView);
        Log.e("OrderReviewActivirty", "sender state" + senderStateTextView);
        Log.e("OrderReviewActivirty", "sender zipcode" + senderPinTextView);

        try {
            senderNameTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            senderAddressTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            senderPhoneNo.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            senderCityTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            senderPinTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            senderCardMsgTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            reciverNameTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            reciverAddressTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            tot1.setTypeface(GlobalApp.getFont(OrderReviewActivity.this,
                    GlobalApp.LATO_REG));
            tot_text.setTypeface(GlobalApp.getFont(OrderReviewActivity.this,
                    GlobalApp.LATO_REG));
            reciverCityTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            reciverZipCodeTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            reciverPhoneNoTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            reciverDatePickTextView.setTypeface(GlobalApp.getFont(
                    OrderReviewActivity.this, GlobalApp.LATO_REG));
            tot.setTypeface(GlobalApp.getFont(OrderReviewActivity.this,
                    GlobalApp.LATO_REG));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderReviewListView.setAdapter(adapter);
        //todo
        new DeliveryTypos().execute("http://tinysurprise.com/test_mode/mobi-app/check_delivery.php?pincode="
                + reciverZip, "" + Math.round(BakeryApplication.getCart().getCost()));
    }

    RadioButton rbPayment, rbCCAvanue;

    @Override
    public void onClick(final View arg0) {

        payNowButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                //
                String name = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                Log.e("Del Type", name);
                if (name == "") {

                    Toast.makeText(getApplicationContext(),
                            "Please select one valid shipping option", Toast.LENGTH_LONG)
                            .show();
                } else {
                    startActivity(new Intent(OrderReviewActivity.this, PayNowActivity.class).putExtra("SENDER_NAME", getIntent().getStringExtra("SENDER_NAME"))
                            .putExtra("SLAST_NAME", getIntent().getStringExtra("SLAST_NAME")).putExtra("SENDER_EMAIL", getIntent().getStringExtra("SENDER_EMAIL"))
                            .putExtra("SENDER_PHONE", getIntent().getStringExtra("SENDER_PHONE")).putExtra("SENDER_ADDRESS", getIntent().getStringExtra("SENDER_ADDRESS"))
                            .putExtra("SENDER_CITY", getIntent().getStringExtra("SENDER_CITY")).putExtra("SENDER_STATE", getIntent().getStringExtra("SENDER_STATE"))
                            .putExtra("SENDER_ZIP", getIntent().getStringExtra("SENDER_ZIP")).putExtra("SENDER_MSG", getIntent().getStringExtra("SENDER_MSG"))
                            .putExtra("SENDER_MSG", getIntent().getStringExtra("SENDER_MSG")).putExtra("RECIVER_NAME", getIntent().getStringExtra("RECIVER_NAME"))
                            .putExtra("RECIVER_ZIP", getIntent().getStringExtra("RECIVER_ZIP")).putExtra("RECIVER_PHONE", getIntent().getStringExtra("RECIVER_PHONE"))
                            .putExtra("RECIVER_CITY", getIntent().getStringExtra("RECIVER_CITY")).putExtra("RECIVER_ADDRESS", getIntent().getStringExtra("RECIVER_ADDRESS"))
                            .putExtra("RECIVER_STATE", getIntent().getStringExtra("RECIVER_STATE")).putExtra("DELIVERY_DATE", getIntent().getStringExtra("DELIVERY_DATE"))
                            .putExtra("LOGIN_MODE", getIntent().getStringExtra("LOGIN_MODE")).putExtra("AMOUNT", ttlOrderReviewTextView.getText().toString().trim()).putExtra("SHIPPING_TYPE", ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString()));

                }
            }

        });


        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        System.out.println("Clicked:" + i);
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                final PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        Toast.makeText(getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();




                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

        /**
         * TODO: Send the authorization response to your server, where it can
         * exchange the authorization code for OAuth access and refresh tokens.
         *
         * Your server must then store these tokens, so that your server code
         * can execute payments for this user in the future.
         *
         * A more complete example that includes the required app-server to
         * PayPal-server integration is available from
         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
         */

    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    class Pincode {
        private String delType;
        private double conditionFromValue, conditionToValue, price;

        public String getDelType() {
            return delType;
        }

        public void setDelType(String delType) {
            this.delType = delType;
        }

        public double getConditionFromValue() {
            return conditionFromValue;
        }

        public void setConditionFromValue(double conditionFromValue) {
            this.conditionFromValue = conditionFromValue;
        }

        public double getConditionToValue() {
            return conditionToValue;
        }

        public void setConditionToValue(double conditionToValue) {
            this.conditionToValue = conditionToValue;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

    }

    class DeliveryTypos extends AsyncTask<String, String, String> {
        private List<Pincode> pincodeList = new ArrayList<Pincode>();
        private String url = "http://tinysurprise.com/test_mode/mobi-app/check_delivery.php?pincode=" + reciverZip;
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OrderReviewActivity.this);
            pDialog.setMessage("Give us a moment. We are checking available delivery options for the location");
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            try {
                URL urlx = new URL(url);
                URI uri = new URI(urlx.getProtocol(), urlx.getUserInfo(),
                        urlx.getHost(), urlx.getPort(), urlx.getPath(),
                        urlx.getQuery(), urlx.getRef());
                urlx = uri.toURL();

                Log.e("OrderReviewActivity", "Url is: " + urlx);

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(urlx.toString());
                HttpResponse response = client.execute(request);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                Log.e("Async", "statuscode: " + statusCode);
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String jsonResult = RestClient
                                .convertStreamToString(instream);

                        JSONArray ja = new JSONArray(jsonResult)
                                .getJSONArray(0);
                        Pincode pincode;
                        ja = ja.getJSONObject(0).getJSONArray("data")
                                .getJSONArray(0);
                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);
                            pincode = new Pincode();
                            pincode.setConditionFromValue(jo
                                    .getDouble("condition_from_value"));
                            pincode.setConditionToValue(jo
                                    .getDouble("condition_to_value"));
                            pincode.setDelType(jo.getString("delivery_type"));
                            pincode.setPrice(jo.getDouble("price"));
                            pincodeList.add(pincode);

                            Log.e("PinCodeAsync", "Del time is: "
                                    + pincodeList.get(i).getDelType());
                        }
                        return "success";
                    }
                }
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


        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(String result) {
            if (result.equals(null) || result.equals("null")) {
                showCustomDialog(OrderReviewActivity.this, AppConstant.OOPS, AppConstant.INTERNAL_SERVER_ERROR, true).show();
            } else if (result.equals("Err: Internal Server Exception.")) {
                showCustomDialog(OrderReviewActivity.this, AppConstant.OOPS, AppConstant.INTERNAL_SERVER_ERROR, true).show();
            } else if (result.contains("Err: Connection Timeout")) {
                showCustomDialog(OrderReviewActivity.this, AppConstant.OOPS, AppConstant.CONN_TIMEOUT, true).show();
            } else if (result.equals("success")) {
                if (pincodeList.size() <= 0) {
                    showCustomDialog(OrderReviewActivity.this, AppConstant.OOPS, AppConstant.YOUR_PINCODE_INCORRECT, true).show();
                }
                rg = new RadioGroup(OrderReviewActivity.this);
                rg.setOrientation(RadioGroup.VERTICAL);// or RadioGroup.VERTICAL
                RadioButton[] rb = new RadioButton[pincodeList.size()];
                System.out.println("I am in suceess rb length: " + rb.length);
                for (int j = 0; j < rb.length; j++) {
                    if (BakeryApplication.getCart().getCost() > pincodeList.get(j).getConditionFromValue()) {
                        System.out.println("Adding Radio Button: "
                                + pincodeList.get(j).getDelType());
                        rb[j] = new RadioButton(OrderReviewActivity.this);

                        if (pincodeList.get(j).getPrice() <= 0) {
                            rb[j].setText(pincodeList.get(j).getDelType());
                        } else {
                            int p = (int) pincodeList.get(j).getPrice();
                            rb[j].setText(pincodeList.get(j).getDelType()
                                    + "\t +" + p);
                        }
                        if(pincodeList.get(j).getDelType() == "Standard Delivery (9 AM - 7 PM)"){
                            rb[j].setChecked(true);
                        }
                        rb[j].setTextSize(11);
                        rb[j].setTextColor(getResources().getColor(R.color.text));
                        rb[j].setTypeface(GlobalApp.getFont(
                                OrderReviewActivity.this,
                                GlobalApp.LATO_SEMI));

                        rg.addView(rb[j]);
                    }
                }
                ((RelativeLayout) orderReviewFooterView
                        .findViewById(R.id.ship_type)).addView(rg);


                rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int x = rg.indexOfChild(rg.findViewById(checkedId));

                        BakeryApplication.getCart().setShippingCost((float) pincodeList.get(x).price);
                        float pri;
                        if (BakeryApplication.getCart().isCouponApplied()) {
                            pri = (float) (BakeryApplication.getCart().getCostWithCoupon() + BakeryApplication.getCart().getShippingCost());
                            ttlOrderReviewTextView.setText("" + pri);
                        } else {
                            pri = (float) (BakeryApplication.getCart().getCost() + BakeryApplication.getCart().getShippingCost());
                            ttlOrderReviewTextView.setText("" + pri);
                        }
                    }
                });
            }
            if (pDialog.isShowing()) pDialog.dismiss();
        }
    }


    class GetOrderID extends AsyncTask<String, Void, String> {
        String shippingType = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            shippingType = ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString();
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
                SharedPreferences prefs = OrderReviewActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String id = prefs.getString("id", null);
                putJSON(j0, "fb_id", id);
            }
            putJSON(j0, "rName", reciverName);
            putJSON(j0, "rAddress", reciverAddress);
            putJSON(j0, "rCity", reciverCity);
            putJSON(j0, "rState", reciverState);
            putJSON(j0, "rMobile", reciverPhone);
            putJSON(j0, "rZipCode", reciverZip);
            putJSON(j0, "deliveryDate", deliveryDate);
            putJSON(j0, "app_platform", GlobalApp.getDeviceName() + " : " + GlobalApp.getAndroidVersion() + " : " + GlobalApp.getDeviceId(OrderReviewActivity.this));
            putJSON(j0, "shippping_price", BakeryApplication.getCart().getShippingCost());
            putJSON(j0, "cart_total", BakeryApplication.getCart().getCost());
            putJSON(j0, "cart_after_discount", BakeryApplication.getCart().getCostWithCoupon());
            putJSON(j0, "coupon_applied", BakeryApplication.getCart().isCouponApplied());
            putJSON(j0, "coupon_code", BakeryApplication.getCart().getAppliedCoupon());
            putJSON(j0, "discount_value", BakeryApplication.getCart().getAppliedCouponValue());
            putJSON(j0, "discount_type", BakeryApplication.getCart().getAppliedCouponType());
            putJSON(j0, "shipping_type", shippingType);


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

}
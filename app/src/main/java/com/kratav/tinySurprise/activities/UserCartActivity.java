package com.kratav.tinySurprise.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.UserCartListViewAdapter;
import com.kratav.tinySurprise.async.RestClient;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.dialog.SweetAlertDialog;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;

@SuppressLint("NewApi")
public class UserCartActivity extends BaseActivity implements OnClickListener {
    private static ListView lsCartList;
    private UserCartListViewAdapter adapter;
    private Button continueShopButton, checkoutButton, applyCodeButon;
    private ImageView backButton;
    private TextView shoppingcart, priceWithCoupon;
    private static TextView cartdetails, totalcart, discount_cart, payable_cart, payable_cart_val,cartIsEmptyTextView, ttlBill, totalcart_val, discount_cart_val, total_val, ttb, statements, example,couponCode;
    private static View footerView;
    private EditText couponCodeET;
    private android.support.v7.app.ActionBar mActionBar;

    DecimalFormat df = new DecimalFormat("#.##");


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.CartActivityTheme);
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.actionbar_cart, null);
        ActionBar.LayoutParams l = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        mActionBar.setCustomView(customView, l);
        Toolbar parent = (Toolbar) customView.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);//
        backButton = (ImageView) customView.findViewById(R.id.customBackButton);

        setContentView(R.layout.cart_payment);
        adapter = new UserCartListViewAdapter(this);

        lsCartList = (ListView) findViewById(R.id.itemincart);
        checkoutButton = (Button) findViewById(R.id.SecureCheckOutButton);
        continueShopButton = (Button) findViewById(R.id.continueShopingButton);
        shoppingcart = (TextView) findViewById(R.id.checkoutTextView);
        cartIsEmptyTextView = (TextView) findViewById(R.id.cartIsEmptyTextView);
        total_val = (TextView) findViewById(R.id.items_val);
    //    priceWithCoupon = (TextView) findViewById(R.id.priceWithCoupon);
        ttlBill = (TextView) findViewById(R.id.ttlbillTextView);
        ttb= (TextView) findViewById(R.id.ttlbill);

        ttb.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
        total_val.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
        ttlBill.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));

        footerView = ((LayoutInflater) UserCartActivity.this.getSystemService(UserCartActivity.this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cart_footer, null, false);


      //  lineTextView = (TextView) footerView.findViewById(R.id.lineTextView);
        couponCodeET = (EditText) footerView.findViewById(R.id.couponcodeEditText);
        applyCodeButon = (Button) footerView.findViewById(R.id.applycoupon);
        statements = (TextView) footerView.findViewById(R.id.statements);
        example = (TextView) footerView.findViewById(R.id.example);
        couponCode = (TextView) footerView.findViewById(R.id.couponCode);
        cartdetails= (TextView) footerView.findViewById(R.id.cartdetails);
        totalcart = (TextView) footerView.findViewById(R.id.totalcart);
        totalcart_val = (TextView) footerView.findViewById(R.id.totalcart_val);
        discount_cart = (TextView) footerView.findViewById(R.id.discount_cart);
        discount_cart_val = (TextView) footerView.findViewById(R.id.discount_cart_val);
        payable_cart = (TextView) footerView.findViewById(R.id.payable_cart);
        payable_cart_val = (TextView) footerView.findViewById(R.id.payable_cart_val);

        statements.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_SEMI));
        example.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_SEMI));
        couponCode.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_SEMI));
        couponCodeET.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_SEMI));
        cartdetails.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
        totalcart.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
        totalcart_val.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
        discount_cart.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
        discount_cart_val.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
        payable_cart.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_HEAVY));
        payable_cart_val.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_HEAVY));

        lsCartList.addFooterView(footerView);
        checkoutButton.setBackgroundDrawable(getBackgroundStateListDrawable(this, R.color.button_bluish, R.color.button_bluish_selector, 0));
        continueShopButton.setBackgroundDrawable(getBackgroundStateListDrawable(this, R.color.button_grey, R.color.button_grey_selector, 0));
        try {
            continueShopButton.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_HEAVY));
            checkoutButton.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_HEAVY));
            // shoppingcart.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.ROBOTOLITE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkoutButton.setOnClickListener(this);
        continueShopButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        applyCodeButon.setOnClickListener(this);

        if (BakeryApplication.getCart().getLength() > 0) {
            lsCartList.setAdapter(adapter);
            BakeryApplication.getCart().evaluateCost();
        }

        if (BakeryApplication.getCart().getCost() <= 0 && BakeryApplication.getCart().getLength() <= 0) {
            makeCartEmptyView();
        } else {
            total_val.setText("ITEMS(" +BakeryApplication.getCart().getLength() + ")" );
            total_val.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
            ttlBill.setText("Rs: " + BakeryApplication.getCart().getCost());
            totalcart_val.setText("Rs: " + BakeryApplication.getCart().getCost());
            payable_cart_val.setText("Rs: " + BakeryApplication.getCart().getCost());
            ttlBill.setTypeface(GlobalApp.getFont(UserCartActivity.this, GlobalApp.LATO_REG));
            cartIsEmptyTextView.setVisibility(View.GONE);
            lsCartList.setVisibility(View.VISIBLE);
        }
        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onBackPressed() {
        //Coupon removing
        BakeryApplication.getCart().setCouponApplied(false);
        BakeryApplication.getCart().setCostWithCoupon(BakeryApplication.getCart().getCost());
        finishActivity("back");
    }

    public static void makeCartEmptyView() {
        cartIsEmptyTextView.setVisibility(View.VISIBLE);
        lsCartList.setVisibility(View.GONE);
        total_val.setVisibility(View.GONE);
        ttlBill.setVisibility(View.GONE);
       // lineTextView.setVisibility(View.GONE);
    }

    public static void refresh() {
        BakeryApplication.getCart().evaluateCost();
        if (BakeryApplication.getCart().getCost() <= 0 && BakeryApplication.getCart().getLength() <= 0) {
            makeCartEmptyView();
        } else {
            total_val.setText("ITEMS(" + BakeryApplication.getCart().getLength() + ")");
            ttlBill.setText("Rs. " + BakeryApplication.getCart().getCost());
            payable_cart_val.setText("Rs. " + BakeryApplication.getCart().getCost());
            totalcart_val.setText("Rs. " + BakeryApplication.getCart().getCost());
          //  discount_cart_val.
            cartIsEmptyTextView.setVisibility(View.GONE);
        }
    }


    public void finishActivity(String msg) {
        Intent i = getIntent();
        i.putExtra("MESSAGE", msg);
        setResult(2, i);
        finish();
    }


    @Override
    public void onClick(View v) {
        if (v.equals(continueShopButton)) {
            finishActivity("finishBack");
        } else if (v.equals(checkoutButton)) {
            if (BakeryApplication.getCart().getCartItems().size() > 0) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                if (pref.getString("Username", null) == null)
                    startActivity(new Intent(UserCartActivity.this, LoginActivity.class));
                else if (pref.getString("loginType", "GUEST_LOGIN").equals("fb"))
                    startActivity(new Intent(UserCartActivity.this, CheckoutActivityForSender.class).putExtra("LOGIN_MODE", "FACEBOOK_LOGIN"));
                else
                    startActivity(new Intent(UserCartActivity.this, CheckoutActivityForSender.class).putExtra("LOGIN_MODE", "GUEST_LOGIN"));
            } else {
                Toast.makeText(UserCartActivity.this, "Your cart is empty!", Toast.LENGTH_LONG).show();
            }

        } else if (v.equals(backButton)) {
            finishActivity("back");
        } else if (v.equals(applyCodeButon)) {
            if (couponCodeET.getText().toString().trim().length() < 1)
                Toast.makeText(getApplicationContext(), "Please enter a coupon code", Toast.LENGTH_LONG).show();
            else {
                new AsyncTask<Void, Void, Boolean>() {
                    boolean isValid, isPercentage;
                    int minqty, mincart, value;
                    SweetAlertDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        dialog = showCustomDialog(UserCartActivity.this, AppConstant.COUPON_APPLING);
                        dialog.show();
                    }

                    @Override
                    protected Boolean doInBackground(Void... arg0) {
                        String urlx = "http://tinysurprise.com/test_mode/mobi-app/coupon_code.php?coupon_code=" + couponCodeET.getText().toString().trim();
                        System.out.println(urlx);

                        URL url = null;
                        try {
                            url = new URL(urlx);
                            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                            url = uri.toURL();
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        try {
                            HttpClient client = new DefaultHttpClient();
                            HttpGet request = new HttpGet(url.toString());
                            HttpResponse response = client.execute(request);
                            StatusLine statusLine = response.getStatusLine();
                            int statusCode = statusLine.getStatusCode();
                            if (statusCode == 200) {
                                HttpEntity entity = response.getEntity();
                                if (entity != null) {
                                    InputStream instream = entity.getContent();
                                    String result = RestClient.convertStreamToString(instream);
                                    Log.e("SendOrder", "result: " + result);
                                    JSONObject jo = new JSONArray(result).getJSONArray(0).getJSONObject(0);
                                    isValid = jo.getInt("isvalid") == 1 ? true : false;
                                    if (isValid == false) return false;
                                    JSONObject jo1 = jo.getJSONObject("value");
                                    isPercentage = jo1.getInt("ispercentage") == 1 ? true : false;
                                    minqty = jo1.getInt("minqty");
                                    mincart = jo1.getInt("mincart");
                                    value = jo1.getInt("value");

                                    return isValid;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result == false) {
                            dialog.setTitleText(AppConstant.OOPS)
                                    .setContentText(AppConstant.COUPON_INVALID)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        } else {
                            if (BakeryApplication.getCart().isCouponApplied()) {
                                dialog.setTitleText(AppConstant.OOPS)
                                        .setContentText(AppConstant.COUPON_INVALID)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                            } else {
                                if (BakeryApplication.getCart().getCartItems().size() < minqty) {
                                    dialog.setTitleText(AppConstant.OOPS)
                                            .setContentText(AppConstant.COUPON_INVALID)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                    return;
                                } else if (BakeryApplication.getCart().getCost() < mincart) {
                                    dialog.setTitleText(AppConstant.OOPS)
                                            .setContentText(AppConstant.COUPON_INVALID)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                    return;
                                } else if (isPercentage) {
                                    BakeryApplication.getCart().setCouponApplied(true);
                                    BakeryApplication.getCart().setCostWithCoupon(BakeryApplication.getCart().getCost() - (BakeryApplication.getCart().getCost() * ((float) value / 100)));
                                    BakeryApplication.getCart().setAppliedCoupon(couponCodeET.getText().toString().trim());
                                    BakeryApplication.getCart().setAppliedCouponType("Percentage");
                                    BakeryApplication.getCart().setAppliedCouponValue("" + value);
                                    //priceWithCoupon.setText("After applying " + couponCodeET.getText().toString().trim() + " Coupon code:  " + BakeryApplication.getCart().getCostWithCoupon() + "RS");
                                    discount_cart_val.setText("- Rs. " + df.format(BakeryApplication.getCart().getCost() * ((float) value / 100)));
                                    payable_cart_val.setText("Rs. " + BakeryApplication.getCart().getCostWithCoupon());
                                    ttlBill.setText("Rs. " + BakeryApplication.getCart().getCostWithCoupon());
                                    BakeryApplication.getCart().setCouponApplied(false);
                                    BakeryApplication.getCart().setCostWithCoupon(BakeryApplication.getCart().getCost());
                                    dialog.setTitleText(AppConstant.WHOOH)
                                            .setContentText(AppConstant.COUPON_APPLIED_SUCCESSFULLY)
                                            .setConfirmText(AppConstant.OK)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                } else if (!isPercentage) {
                                    BakeryApplication.getCart().setCouponApplied(true);
                                    BakeryApplication.getCart().setCostWithCoupon(BakeryApplication.getCart().getCost() - value);
                                    BakeryApplication.getCart().setAppliedCoupon(couponCodeET.getText().toString().trim());
                                    BakeryApplication.getCart().setAppliedCouponType("Flat");
                                    BakeryApplication.getCart().setAppliedCouponValue("" + value);
                                    //priceWithCoupon.setText("After applying " + couponCodeET.getText().toString().trim() + " Coupon code:  " + BakeryApplication.getCart().getCostWithCoupon() + "RS");
                                    discount_cart_val.setText("- Rs. " + value);
                                    payable_cart_val.setText("Rs. "+ BakeryApplication.getCart().getCostWithCoupon());
                                    ttlBill.setText("Rs. "+ BakeryApplication.getCart().getCostWithCoupon());
                                    BakeryApplication.getCart().setCouponApplied(false);
                                    BakeryApplication.getCart().setCostWithCoupon(BakeryApplication.getCart().getCost());
                                    dialog.setTitleText(AppConstant.WHOOH)
                                            .setContentText(AppConstant.COUPON_APPLIED_SUCCESSFULLY)
                                            .setConfirmText(AppConstant.OK)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                } else {
                                    dialog.setTitleText(AppConstant.OOPS)
                                            .setContentText(AppConstant.COUPON_NOT_APPLIED)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                }
                            }
                        }
                    }
                }.execute();
            }
        }
    }
}
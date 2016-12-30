package com.kratav.tinySurprise.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class CheckoutActivityForSender extends BaseActivity {

    private EditText SName, LName, SEmail, SPhoneNo, SCity, SAddress, SState, SPin;
    private Button nextButton;
    private ImageView backButtonCheckOut;
    private TextView bd, ba,checkout_text,title1, title2, title3, title4, heading1, heading2;
    private TextView fname, lname, mail, phone, pin, add, city, state;
    private String cityName = null, stateName = null, zip = null, loginMode;
    private android.support.v7.app.ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.HomeActivityTheme);
        super.onCreate(savedInstanceState);
        /*actionbar
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.actionbar_cart, null);
        ActionBar.LayoutParams l = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        mActionBar.setCustomView(customView, l);
        Toolbar parent = (Toolbar) customView.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);//
        backButtonCheckOut = (ImageView) customView.findViewById(R.id.customBackButton);
*/

        setContentView(R.layout.checkout_sender_details);
        initControls();
        //fillLocation();
 //       backButtonCheckOut = (ImageView) customView.findViewById(R.id.customBackButton1);
 //       backButtonCheckOut.setOnClickListener(this);

        nextButton.setOnClickListener(this);
        loginMode = getIntent().getStringExtra("LOGIN_MODE");
        turnGPSOn();

        GPSTracker gpsTracker = new GPSTracker(this);
           if (gpsTracker.canGetLocation()) {
            double latitude = Double.parseDouble(String.valueOf(gpsTracker.latitude));
            double longitude = Double.parseDouble(String.valueOf(gpsTracker.longitude));

            Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getLocality();
                    stateName = addresses.get(0).getAdminArea();
                    zip = addresses.get(0).getPostalCode();
                    SCity.setText(cityName);
                    SState.setText(stateName);
                    SPin.setText(zip);
                }
                TelephonyManager tMgr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
                String mPhoneNumber = tMgr.getLine1Number();
                SPhoneNo.setText(mPhoneNumber);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (loginMode.equals("fb_id")) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            String[] user = pref.getString("Username", null).split(" ");
            SName.setText(user[0]);
            LName.setText(user[1]);
            if (pref.getString("userEmail", null) != null || !pref.getString("userEmail", null).equals(""))
                SEmail.setText(pref.getString("userEmail", null));
            else
                SEmail.setText(getEmail(this));
            //SAddress.setText(pref.getString("userLocation", null));
        } else {
            SEmail.setText(getEmail(this));
        }


        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    private void fillLocation() {
        SCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("pincode.csv")));
                    String line;
                    String v = SPin.getText().toString().trim();
                    while ((line = reader.readLine()) != null) {
                        String[] RowData = line.split(",");
                        if (RowData[0].equals(v)) {
                            SCity.setText(RowData[1]);
                            SState.setText(RowData[2]);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {
        }
    }

    // Method to turn off the GPS
    public void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { // if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }


    static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);
        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        turnGPSOff();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(backButtonCheckOut)) {
            finish();
        } else if (v.equals(nextButton)) {


            if (SName.getText().toString().trim().length() < 1) {
                SName.setError("Please enter your name");
            } else if (LName.getText().toString().trim().length() < 1) {
                LName.setError("Please enter your last name");
            } else if (SEmail.getText().toString().trim().length() < 1) {
                SEmail.setError("Please enter your email id");
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(SEmail.getText().toString().trim()).matches()) {
                SEmail.setError("Please enter correct email id");
            } else if (SPhoneNo.getText().toString().trim().length() < 1) {
                SPhoneNo.setError("Please enter your phone number");
            } else if (SPhoneNo.getText().toString().length() > 13 || !isValidPhoneNumber(SPhoneNo.getText().toString())) {
                SPhoneNo.setError("Please enter a valid phone number.");
            } else if (SAddress.getText().toString().trim().length() < 1) {
                SCity.setError("Please enter your address");
            } else if (SCity.getText().toString().trim().length() < 1) {
                SCity.setError("Please enter your city");
            } else if (SState.getText().toString().trim().length() < 1) {
                SCity.setError("Please enter your state");
            } else if (SPin.getText().toString().trim().length() < 1) {
                SCity.setError("Please enter your zipcode");
            } else {
                startActivity(new Intent(CheckoutActivityForSender.this, CheckoutActivityForReciver.class).putExtra("SENDER_NAME", SName.getText().toString().trim())
                        .putExtra("SLAST_NAME", LName.getText().toString().trim()).putExtra("SENDER_EMAIL", SEmail.getText().toString().trim())
                        .putExtra("SENDER_PHONE", SPhoneNo.getText().toString().trim()).putExtra("SENDER_ADDRESS", SAddress.getText().toString().trim())
                        .putExtra("SENDER_CITY", SCity.getText().toString().trim()).putExtra("SENDER_STATE", SState.getText().toString().trim())
                        .putExtra("SENDER_ZIP", SPin.getText().toString().trim()).putExtra("LOGIN_MODE", getIntent().getStringExtra("LOGIN_MODE")));
            }
        }
    }

    private void initControls() {
        SName = (EditText) findViewById(R.id.user_name);
        LName = (EditText) findViewById(R.id.last_name);
        SEmail = (EditText) findViewById(R.id.user_email);
        SPhoneNo = (EditText) findViewById(R.id.user_phone);
        SCity = (EditText) findViewById(R.id.user_city);
        SAddress = (EditText) findViewById(R.id.user_address);
        SState = (EditText) findViewById(R.id.user_state);
        SPin = (EditText) findViewById(R.id.user_pin);
        nextButton = (Button) findViewById(R.id.NextCheckOutSenderButton);
        backButtonCheckOut = (ImageView) findViewById(R.id.backButtonCheckout1);

        checkout_text = (TextView) findViewById(R.id.checkout_text1);
        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);
        title4 = (TextView) findViewById(R.id.title4);
        heading1 = (TextView) findViewById(R.id.heading1);
        heading2 = (TextView) findViewById(R.id.heading2);
        fname = (TextView) findViewById(R.id.first_name);
        lname = (TextView) findViewById(R.id.last_name);
        mail = (TextView) findViewById(R.id.email_id);
        phone = (TextView) findViewById(R.id.phone);
        pin = (TextView) findViewById(R.id.pin);
        add = (TextView) findViewById(R.id.address);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);

        nextButton.setBackgroundDrawable(getBackgroundStateListDrawable(this, R.color.button_green_sender, R.color.button_green_sender_selector, 0));
        //fillDummivalue();


        try {
            SName.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            LName.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            SEmail.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            SPhoneNo.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            SCity.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            SAddress.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            SState.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            SPin.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            //sd.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.ROBOTOMEDIUM));
            bd.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            ba.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            nextButton.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));

            checkout_text.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            title1.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            title2.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            title3.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            title4.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            heading1.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            heading2.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            fname.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            lname.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            mail.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            phone.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            pin.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            add.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            city.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            state.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillDummivalue() {
        SName.setText("Barney");
        LName.setText("Tiwari");
        SEmail.setText("as@gmail.com");
        SPhoneNo.setText("9876543210");
        SCity.setText("Deoria");
        SAddress.setText("Nehru Nagar");
        SState.setText("Uttar Praesh");
        SPin.setText("274001");
    }

}
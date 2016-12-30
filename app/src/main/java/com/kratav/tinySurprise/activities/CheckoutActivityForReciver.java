package com.kratav.tinySurprise.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.async.CheckProductAvailability;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("InlinedApi")
public class CheckoutActivityForReciver extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private EditText RZip, RCity, RAddress, RState, MessageOnCard;
    private AutoCompleteTextView RName, RPhone;
    private Button ConfirmOrderButton;
    private android.support.v7.app.ActionBar mActionBar;
    private ImageView backButtonCheckOut2;
    private TextView title1, title2, title3, title4;
    private TextView checkout, receiver_name, phone, delivery_address, personalization, pin, address, city, state, msg, datevalue, date;
    private RelativeLayout date_layout;
    private List<String> contactList;
    public String sku, ph, ph1;
    private TextView cd, da, p;

    public  static final int RequestPermissionCode  = 1 ;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setTheme(R.style.HomeActivityTheme);
        super.onCreate(savedInstanceState);
/*
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.actionbar_checkout_receiver, null);
        LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mActionBar.setCustomView(customView, l);
        */
        setContentView(R.layout.checkout_reciver_details);
/*        Toolbar parent = (Toolbar) customView.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);//
*/

    //    backButtonCheckOut = (ImageView) customView.findViewById(R.id.customBackButton);

        RName = (AutoCompleteTextView) findViewById(R.id.RName);
        RZip = (EditText) findViewById(R.id.rZipCodeCheckout);
        RPhone = (AutoCompleteTextView) findViewById(R.id.rPhoneNoCheckout);
        RCity = (EditText) findViewById(R.id.RCity);
        RAddress = (EditText) findViewById(R.id.rAddress);
        RState = (EditText) findViewById(R.id.rState);
        MessageOnCard = (EditText) findViewById(R.id.messageOnCardEditText);
  //      pickDate = (Button) findViewById(R.id.pickDateTextView);

        backButtonCheckOut2 = (ImageView) findViewById(R.id.backButtonCheckout2);
        date_layout = (RelativeLayout) findViewById(R.id.segment7);
        checkout = (TextView) findViewById(R.id.checkout_text2);
        title1 = (TextView) findViewById(R.id.title1);
        title2 = (TextView) findViewById(R.id.title2);
        title3 = (TextView) findViewById(R.id.title3);
        title4 = (TextView) findViewById(R.id.title4);
        receiver_name = (TextView) findViewById(R.id.receiver_name);
        phone =(TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        pin = (TextView) findViewById(R.id.pin);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        msg = (TextView) findViewById(R.id.msg);
        date = (TextView) findViewById(R.id.date);
        datevalue = (TextView) findViewById(R.id.date_value);//dateLL = (LinearLayout) findViewById(R.id.date);
        //initValue();

        cd = (TextView) findViewById(R.id.contactdetails);
        da = (TextView) findViewById(R.id.deilveryaddress);
        p = (TextView) findViewById(R.id.personalization);

        ConfirmOrderButton = (Button) findViewById(R.id.confrimOrderButton);
        ConfirmOrderButton.setBackgroundDrawable(getBackgroundStateListDrawable(this, R.color.button_green_sender, R.color.button_green_sender_selector, 0));
        //pickDate.setBackgroundDrawable(getBackgroundStateListDrawable(this, R.color.button_green_sender, R.color.button_green_sender_selector, 0));
        //receiver = (TextView) findViewById(R.id.CheckoutId);

        showContacts();

        try {
            //receiver.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.ROBOTOLITE));
            RName.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            RZip.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            RPhone.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            RCity.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            RAddress.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            MessageOnCard.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            RState.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_REG));
            cd.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            da.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            p.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            checkout.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            title1.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            title2.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            title3.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            title4.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            receiver_name.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            phone.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            address = (TextView) findViewById(R.id.address);
            pin.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            city.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            state.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            msg.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            date.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_SEMI));
            datevalue.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
            ConfirmOrderButton.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));


        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < BakeryApplication.getCart().getCartItems().size(); i++) {
            sku += BakeryApplication.getCart().getCartItems().get(i).getProductCode() + "&sku[]=";
        }
        //Log.e("SKUS", sku);

        //TODO  private ProgressDialog pDialog;
        // new CheckProductAvailability(CheckoutActivityForReciver.this, null).execute(RZip.getText().toString(), sku);


        ConfirmOrderButton.setOnClickListener(this);
        backButtonCheckOut2.setOnClickListener(this);
        date_layout.setOnClickListener(this);


/*      AutoCompleteAdapter
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items, readContactData());

        autoCompleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        RName.setAdapter(autoCompleteAdapter);


        RName.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ph1 = getPhoneNumber(RName.getText().toString());
                ph = ph1.replaceAll("\\s+", "");
                RPhone.setText(ph);
            }
        });

*/

        RCity.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("pincode.csv")));
                    String line;
                    String v = RZip.getText().toString().trim();
                    while ((line = reader.readLine()) != null) {
                        String[] RowData = line.split(",");
                        if (RowData[0].equals(v)) {
                            RCity.setText(RowData[1]);
                            RState.setText(RowData[2]);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }


    public void showContacts(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, RequestPermissionCode);
            }
            else {
                //Type the function
                ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items, readContactData());

                autoCompleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                RName.setAdapter(autoCompleteAdapter);


                RName.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        ph1 = getPhoneNumber(RName.getText().toString());
                        ph = ph1.replaceAll("\\s+", "");
                        RPhone.setText(ph);
                    }
                });
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        if (RC == RequestPermissionCode) {
            if (PResult[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initValue() {
        RName.setText("Ted");
        RZip.setText("600013");
        RPhone.setText("1234567890");
        RCity.setText("Chennai");
        RState.setText("TamilNadu");
        MessageOnCard.setText("Hello");
        //pickDate.setText("20/10/13");
    }

    public String getPhoneNumber(String name) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = '" + name + "'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = CheckoutActivityForReciver.this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if (ret == null)
            ret = "Unsaved";
        return ret;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(ConfirmOrderButton)) {
            if (RName.getText().toString().trim().equals("")) {
                RName.setError("Please fill receiver's name");
            } else if (RAddress.getText().toString().trim().equals("")) {
                RAddress.setError("Please fill receiver's Address");
            } else if (RCity.getText().toString().trim().equals("")) {
                RCity.setError("Please fill receiver's City");
            } else if (RZip.getText().toString().trim().equals("")) {
                RZip.setError("Please fill receiver's zip code");
            } else if (RState.getText().toString().trim().equals("")) {
                RState.setError("Please fill receiver's State");
            } else if (RPhone.getText().toString().trim().equals("")) {
                RPhone.setError("Please fill receiver's phone number");
            } else if (RPhone.getText().toString().length() > 13 || !isValidPhoneNumber(RPhone.getText().toString())) {
                RPhone.setError("Please enter a valid phone number.");
            } else if (datevalue.getText().toString().equals("Select Date Of Delivery")) {
                Toast.makeText(CheckoutActivityForReciver.this, "Please select a date", Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(CheckoutActivityForReciver.this, OrderReviewActivity.class).putExtra("SENDER_NAME", getIntent().getStringExtra("SENDER_NAME"))
                        .putExtra("SLAST_NAME", getIntent().getStringExtra("SLAST_NAME")).putExtra("SENDER_EMAIL", getIntent().getStringExtra("SENDER_EMAIL"))
                        .putExtra("SENDER_PHONE", getIntent().getStringExtra("SENDER_PHONE")).putExtra("SENDER_ADDRESS", getIntent().getStringExtra("SENDER_ADDRESS"))
                        .putExtra("SENDER_CITY", getIntent().getStringExtra("SENDER_CITY")).putExtra("SENDER_STATE", getIntent().getStringExtra("SENDER_STATE"))
                        .putExtra("SENDER_ZIP", getIntent().getStringExtra("SENDER_ZIP")).putExtra("SENDER_MSG", MessageOnCard.getText().toString().trim())
                        .putExtra("SENDER_MSG", MessageOnCard.getText().toString().trim()).putExtra("RECIVER_NAME", RName.getText().toString().trim())
                        .putExtra("RECIVER_ZIP", RZip.getText().toString().trim()).putExtra("RECIVER_PHONE", RPhone.getText().toString().trim())
                        .putExtra("RECIVER_CITY", RCity.getText().toString().trim()).putExtra("RECIVER_ADDRESS", RAddress.getText().toString().trim())
                        .putExtra("RECIVER_STATE", RState.getText().toString().trim()).putExtra("DELIVERY_DATE", datevalue.getText().toString().trim())
                        .putExtra("LOGIN_MODE", getIntent().getStringExtra("LOGIN_MODE")));
            }
        } else if (v.equals(backButtonCheckOut2)) {
            finish();
        } else if (v.equals(date_layout)) {
            if(RZip.getText().toString().equals("") || RZip.getText().toString() == null){
                Toast.makeText(CheckoutActivityForReciver.this,"Please fill the pincode",Toast.LENGTH_LONG).show();
            }else {
                new CheckProductAvailability(CheckoutActivityForReciver.this, null) {
                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        Calendar now = Calendar.getInstance();
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                CheckoutActivityForReciver.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                (now.get(Calendar.DAY_OF_MONTH) + Integer.parseInt(result))
                        );
                        //dpd.setThemeDark(true);
                        dpd.vibrate(true);
                        dpd.dismissOnPause(true);
                        dpd.setMinDate(CheckProductAvailability.delDate);
           /* if (true) {
                dpd.setAccentColor(R.color.button_green_sender);
            }*/
                        //dpd.showYearPickerFirst(true);
                        dpd.show(getFragmentManager(), "Datepickerdialog");
                    }
                }.execute(RZip.getText().toString(), sku);
            }
        }

    }

    private List<String> readContactData() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        contactList = new ArrayList<String>();
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactList.add(name);
        }
        phones.close();
        return contactList;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String[] monthNames = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        datevalue.setText((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" + monthNames[monthOfYear] + "-" + year);

    }
}

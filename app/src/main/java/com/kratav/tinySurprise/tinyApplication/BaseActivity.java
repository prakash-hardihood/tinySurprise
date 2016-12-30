package com.kratav.tinySurprise.tinyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.CheckoutActivityForReciver;
import com.kratav.tinySurprise.activities.CheckoutActivityForSender;
import com.kratav.tinySurprise.activities.Contact;
import com.kratav.tinySurprise.activities.FavActivity;
import com.kratav.tinySurprise.activities.OrderReviewActivity;
import com.kratav.tinySurprise.activities.UserCartActivity;
import com.kratav.tinySurprise.dialog.SweetAlertDialog;
import com.kratav.tinySurprise.utils.GlobalApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity implements OnClickListener {

    public static List<String> productNameList = new ArrayList<String>();
    private android.support.v7.app.ActionBar mActionBar;
    protected View customView = null;
    SweetAlertDialog sweetAlertDialog;
    public Tracker mTracker;
    private String rolPref = "rolPref";


    protected void actionbar() {
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);
        LayoutInflater li = LayoutInflater.from(this);
        customView = li.inflate(R.layout.actionbar_checkout_sender, null);
        TextView actionbarTittle = ((TextView) customView.findViewById(R.id.titleActionBar));
        ImageView backbutton = ((ImageView) customView.findViewById(R.id.customBackButton));
        if (this instanceof CheckoutActivityForSender)
            actionbarTittle.setText("SENDER DETAILS");
        else if (this instanceof CheckoutActivityForReciver)
            actionbarTittle.setText("RECEIVER DETAILS");
        else if (this instanceof OrderReviewActivity)
            actionbarTittle.setText("ORDER REVIEW");
        else if (this instanceof Contact)
            actionbarTittle.setText("CONTACT US");
        else if (this instanceof FavActivity)
            actionbarTittle.setText("FAVOURITES");
        backbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        actionbarTittle.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.ROBOTOLITE));
        LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mActionBar.setCustomView(customView, l);
        Toolbar parent = (Toolbar) customView.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);//
    }


    public boolean isValidPhoneNumber(String phoneNo) {
        Pattern mPattern = Pattern.compile("((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}");
        return mPattern.matcher(phoneNo).find();
    }

    @Override
    public void onClick(View arg0) {

    }

    public static LinkedHashMap cityHashMap() {
        LinkedHashMap hm = new LinkedHashMap();
        hm.put("Chennai", "CHN");
        hm.put("Bangalore", "BLR");
        hm.put("Mumbai", "MUM");
        hm.put("Delhi", "DEL");
        hm.put("Kolkata", "KOL");
        hm.put("Hyderabad", "HYD");
        hm.put("Chandigarh", "CHA");
        hm.put("Pune", "PUN");
        hm.put("Jaipur", "JPR");
        hm.put("Shimla", "SHM");
        return hm;
    }

    public static StateListDrawable setSelector(int normalColor, int pressedColor) {

        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        stateListDrawable.addState(new int[]{}, new ColorDrawable(pressedColor));
        return stateListDrawable;
    }


    public SweetAlertDialog showCustomDialog(final Context context, String title, String message, final boolean finishStatus) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        if (finishStatus == true)
                            ((Activity) context).finish();
                        ///else sweetAlertDialog.cancel();
                    }
                });
        return sweetAlertDialog;
    }

    public SweetAlertDialog showCustomDialog(final Context context, String title) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.bakers_blue));
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog.setCancelable(false);
        return sweetAlertDialog;
    }

    public void putJSON(JSONObject j0, String key, String value) {
        try {
            if (key != null)
                j0.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putJSON(JSONObject j0, String key, int value) {
        try {
            if (key != null)
                j0.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putJSON(JSONObject j0, String key, float value) {
        try {
            if (key != null)
                j0.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putJSON(JSONObject j0, String key, JSONObject value) {
        try {
            if (key != null)
                j0.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putJSON(JSONObject j0, String key, JSONArray value) {
        try {
            j0.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putJSON(JSONObject j0, String key, boolean value) {
        try {
            if (key != null)
                j0.put(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StateListDrawable getBackgroundStateListDrawable(Context context, int colorid, int stcolorid, int stwidthid) {
        try {
            Drawable normal = generateShape(context, getResources().getColor(colorid), stwidthid);
            Drawable selected = generateShape(context, getResources().getColor(stcolorid), stwidthid);
            Drawable pressed = generateShape(context, getResources().getColor(stcolorid), stwidthid);

            StateListDrawable std = null;
            if (selected != null && normal != null) {
                std = new StateListDrawable();
                std.addState(new int[]{android.R.attr.state_pressed}, pressed != null ? pressed : selected);
                std.addState(new int[]{android.R.attr.state_selected}, selected);
                std.addState(new int[]{android.R.attr.state_focused}, pressed != null ? pressed : selected);
                std.addState(new int[]{}, normal);
            }
            return std;
        } catch (Exception e) {
            return null;
        }
    }

    public GradientDrawable generateShape(Context context, int color, float stwidth) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        return gd;
    }

    private float[] getRadii(TypedArray arr) {
        float[] rada = new float[8];
        boolean sort = false;
        int length = arr.length();
        if (length == 1) {
            for (int i = 0; i < 8; i++)
                rada[i] = arr.getFloat(0, 0);
            return rada;
        }
        if (length == 4) sort = true;
        int index = 0;
        for (int i = 0; i < length; i++) {
            rada[index++] = arr.getFloat(i, 0);
            if (sort) {
                rada[index++] = arr.getFloat(i, 0);
            }
        }
        return rada;
    }

    protected void saveROL(String loc) {
        SharedPreferences.Editor editor = getSharedPreferences(rolPref, MODE_PRIVATE).edit();
        editor.putString("loc", loc);
        editor.commit();
    }

    SharedPreferences prefs = null;

    protected int getPrefRel() {
        if (prefs == null)
            prefs = getSharedPreferences(rolPref, MODE_PRIVATE);
        return prefs.getInt("relation", 0);
    }

    protected int getPrefOcc() {
        if (prefs == null)
            prefs = getSharedPreferences(rolPref, MODE_PRIVATE);
        return prefs.getInt("occ", 0);
    }

    protected String getPrefLoc() {
        if (prefs == null)
            prefs = getSharedPreferences(rolPref, MODE_PRIVATE);
        return prefs.getString("loc", null);
    }
}

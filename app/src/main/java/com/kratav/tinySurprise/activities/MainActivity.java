package com.kratav.tinySurprise.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.CategoryAdapters;
import com.kratav.tinySurprise.adapter.CitesAdapter;
import com.kratav.tinySurprise.adapter.ImageAdapter1;
import com.kratav.tinySurprise.adapter.NavAdapter;
import com.kratav.tinySurprise.async.RestClient;
import com.kratav.tinySurprise.bean.CustomOptions;
import com.kratav.tinySurprise.bean.Dynamic;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.database.DataAdapter;
import com.kratav.tinySurprise.dialog.SweetAlertDialog;
import com.kratav.tinySurprise.fragments.Categories1;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;
import com.kratav.tinySurprise.async.RetriveProductAccordingToCategory;

import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity  implements ExpandableListView.OnChildClickListener {

    private AlertDialog ad;
    private ImageButton positiveBtn, negativeButton;
    private ImageView dialogBackgroud;
    private SimpleCursorAdapter mAdapter;
    private MatrixCursor c;
    Toolbar toolbar;
    private Button home;

    private DrawerLayout drawer;
    private ExpandableListView drawerList;
    private ImageView shareButton;
    private TextView category_title;
    private android.support.v7.widget.SearchView searchView;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;



    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle bundle) {
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initDrawer();


        if (!BakeryApplication.getMarketing().isShown()) {
            checkOffers();
            BakeryApplication.getMarketing().setShown(true);
        }



        //setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ExpandableListView) findViewById(R.id.left_drawer);

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list,null, false);
        category_title = (TextView) listHeaderView.findViewById(R.id.category);
        category_title.setTypeface(GlobalApp.getFont(getBaseContext(), GlobalApp.LATO_HEAVY));
        drawerList.addHeaderView(listHeaderView);

        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.customsearchView);
        shareButton = (ImageView) findViewById(R.id.shareButton);
        home = (Button) findViewById(R.id.home_button);
        searchView.setMaxWidth(getResources().getDisplayMetrics().widthPixels - 100);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(shareButton);
            }
        });

        final String[] from = new String[]{"productName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                populateAdapter(query);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String arg0) {

                return false;
            }
        });

        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    public void initDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ExpandableListView) findViewById(R.id.left_drawer);
        prepareListData();
        drawerList.setAdapter(new NavAdapter(this, listDataHeader, listDataChild));

        drawerList.setOnChildClickListener(this);
        actionBarDrawerToggle = new
                ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                        super.onDrawerClosed(drawerView);
                    }
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank
                        super.onDrawerOpened(drawerView);
                    }
                };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }




    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.home, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.contactMenu:
                        startActivity(new Intent(MainActivity.this, Contact.class));
                        break;

                    case R.id.fav:
                        startActivity(new Intent(MainActivity.this, FavActivity.class));
                        break;

                    case R.id.cart:
                        if (BakeryApplication.getCart().getLength() > 0) {
                            startActivity(new Intent(MainActivity.this, UserCartActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), AppConstant.CART_IS_EMPTY, Toast.LENGTH_SHORT).show();
                        }
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    /*
     * Preparing the list data
     */

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding headers
        Resources res = getResources();
        String[] headers = res.getStringArray(R.array.nav_drawer_labels);
        listDataHeader = Arrays.asList(headers);

        // Static method
        List<String> rel, occ;
        String[] relation,occasion;

        relation = res.getStringArray(R.array.rel_array);
        rel = Arrays.asList(relation);
        occasion = res.getStringArray(R.array.occ_array);
        occ = Arrays.asList(occasion);
        // Add to hashMap
        listDataChild.put(listDataHeader.get(0), rel); // Header, Child data
        listDataChild.put(listDataHeader.get(1), occ);
    }




    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {

        String temp= null;
        int c_no;

        if(groupPosition == 0){
            c_no = GlobalApp.childnumber;
            temp = GlobalApp.relArray[c_no];
        }
        else if(groupPosition == 1){
            c_no = GlobalApp.childnumber;
            temp = GlobalApp.occArray[c_no];
        }
        Log.e("initial_category_name: ", temp );
        //    temp = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
            startActivity(new Intent(MainActivity.this, HomeActivity.class)
                    .putExtra(AppConstant.EXTRA_LOCATION, temp));

        return false;
    }



    @Override
    protected void onPause(){
        super.onPause();
    }

    protected void onResume(){
        super.onResume();
        mTracker.setScreenName("Image~" + getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataAdapter dataAdapter= new DataAdapter(MainActivity.this);
                dataAdapter.createDatabase();
                dataAdapter.open();
                dataAdapter.getProductNameList(productNameList);

            }
        }).start();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }


    private void populateAdapter(String query) {
        c = new MatrixCursor(new String[]{BaseColumns._ID, "productName"});
        for (int i = 0; i < productNameList.size(); i++) {
            if (productNameList.get(i).toLowerCase().contains(query.toLowerCase()))
                c.addRow(new Object[]{i, productNameList.get(i)});
        }
        mAdapter.changeCursor(c);
    }

    public void checkOffers() {
        if (BakeryApplication.getMarketing().isStatus()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            LayoutInflater inflator = MainActivity.this.getLayoutInflater();
            View v = inflator.inflate(R.layout.offer_dialog, null);

            dialogBackgroud = (ImageView) v.findViewById(R.id.dialogBackground);
            positiveBtn = (ImageButton) v.findViewById(R.id.btn_positive_offer_dialog);
            negativeButton = (ImageButton) v.findViewById(R.id.btn_negative_offer_button);

            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.cancel();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class).putExtra("categoryExtra", BakeryApplication.getMarketing().getCategory()));
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.cancel();
                }
            });

            Glide.with(this).load(BakeryApplication.getMarketing().getBgImage()).skipMemoryCache(true).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    e.printStackTrace();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    System.out.println("resource is downloaded!!");

                    return false;
                }
            }).into(dialogBackgroud);
            Glide.with(this).load(BakeryApplication.getMarketing().getPositiveBtnImage()).skipMemoryCache(true).into(positiveBtn);
            Glide.with(this).load(BakeryApplication.getMarketing().getNegativeBtnImage()).skipMemoryCache(true).into(negativeButton);
            builder.setView(v);
            builder.create();
            ad = builder.show();
            ad.setCanceledOnTouchOutside(true);
        }
    }

}

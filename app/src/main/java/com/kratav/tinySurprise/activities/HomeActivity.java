package com.kratav.tinySurprise.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.ProductGridAdapter;
import com.kratav.tinySurprise.async.GetProductThroughSKU;
import com.kratav.tinySurprise.async.RetrieveProductAsyncTask;
import com.kratav.tinySurprise.async.RetriveProductAccordingToCategory;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.database.DataAdapter;
import com.kratav.tinySurprise.grid.StaggeredGridView;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"DefaultLocale", "NewApi"})
public class HomeActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
    private int REQ_CODE = 1, i = 1;
    private List<Product> productList = new ArrayList<Product>();
    private ProductGridAdapter productGridAdapter;
    private TextView actionbarLocationTextView, productFromCategory, footerTV;
    public StaggeredGridView gridView;

    private LinearLayout actionBarCenterButton;
    private String relation, occassion, location;
    private ImageView actionbarRelationImageView, actionbarOccasionImageView, actionBarLeftButton, shareButton;
    private android.support.v7.widget.SearchView searchView;
    private SimpleCursorAdapter mAdapter;
    private MatrixCursor c;
    private RetrieveProductAsyncTask retriveProductAsync;
    public View footer;
    private LinearLayout errorLinearLayout;
    private TextView errorTextView;
    private String category = null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_view_home);
        init();
        searchView.setMaxWidth(getResources().getDisplayMetrics().widthPixels - 100);

        actionBarLeftButton.setOnClickListener(this);
        actionBarCenterButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);

        if (getIntent().getStringExtra(AppConstant.EXTRA_LOCATION) != null)
            saveROL(getIntent().getStringExtra(AppConstant.EXTRA_LOCATION));

        location = getPrefLoc();


       // BakeryApplication.setCurrentCity(location);

        LayoutInflater layoutInflater = getLayoutInflater();
        footer = layoutInflater.inflate(R.layout.grid_footer, null);

        footerTV = (TextView) footer.findViewById(R.id.footerTextView);
        gridView.addFooterView(footer);

        footer.setOnClickListener(this);

        productGridAdapter = new ProductGridAdapter(HomeActivity.this, productList);
        gridView.setAdapter(productGridAdapter);
        productGridAdapter.notifyDataSetChanged();

        if (getIntent().hasExtra("categoryExtra")) {
            this.category = getIntent().getStringExtra("categoryExtra");
            runCategoryAsync(getIntent().getStringExtra("categoryExtra"), getIntent().getStringExtra("categoryExtra").toUpperCase());
        } else {
            if (productList.size() < 1) {
                retriveProductAsync = new RetrieveProductAsyncTask(HomeActivity.this, productGridAdapter, productList);
                retriveProductAsync.execute(location, "" + i);
            }
        }

        final String[] from = new String[]{"productName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
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

        searchView.setOnSuggestionListener(new OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int arg0) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int arg0) {
                Cursor cursor = (Cursor) mAdapter.getItem(arg0);
                DataAdapter da = new DataAdapter(HomeActivity.this);
                boolean status = da.getCategoryList(cursor.getString(1));
                if (status == true) {
                    Toast.makeText(getApplicationContext(), "Item: " + da.getCategoryList(cursor.getString(1)), Toast.LENGTH_SHORT).show();
                    productList.clear();
                    RetrieveProductAsyncTask retriveProductAsync = new RetrieveProductAsyncTask(HomeActivity.this, productGridAdapter, productList);
                    retriveProductAsync.execute(relation, occassion, "" + i, location);
                } else {
                    GetProductThroughSKU gpts = new GetProductThroughSKU(HomeActivity.this);
                    DataAdapter daa = new DataAdapter(HomeActivity.this);
                    gpts.execute(daa.getSKU(cursor.getString(1)));
                }
                return false;
            }
        });
        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void showErrorLayout(String s) {
        if (!s.equals(""))
            errorTextView.setText(s);
        errorLinearLayout.setVisibility(View.VISIBLE);
    }

    public void toggleFooterViewVisibilty() {
        if (footer.getVisibility() == View.VISIBLE) footer.setVisibility(View.GONE);
        else footer.setVisibility(View.VISIBLE);
    }

    public void init() {
        actionBarCenterButton = (LinearLayout) findViewById(R.id.catLL);
        actionBarLeftButton = (ImageView) findViewById(R.id.actionBarLeftButton);
        gridView = (StaggeredGridView) findViewById(R.id.homeScreenGridView1);
        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.customsearchView);
        shareButton = (ImageView) findViewById(R.id.shareButton);
        productFromCategory = (TextView) findViewById(R.id.pfc);
        errorLinearLayout = (LinearLayout) findViewById(R.id.errorLinearLayout);
        errorTextView = (TextView) findViewById(R.id.errorTextView);

        int searchIconId = android.support.v7.appcompat.R.id.search_button;
        ImageView searchIcon = (ImageView) searchView.findViewById(searchIconId);
        searchIcon.setImageResource(R.drawable.search);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(actionBarLeftButton)) {
            Intent i = new Intent(HomeActivity.this, CategoryActivity.class);
            startActivityForResult(i, REQ_CODE);
        } else if (v.equals(actionBarCenterButton)) {
            productList.clear();
            System.gc();
            finish();
        } else if (v.equals(shareButton)) {
            showPopup(shareButton);
        } else if (v.equals(footer) && footerTV.getText().equals(AppConstant.LOAD_MORE)) {
            i += 1;
            if (category != null) {
                new RetriveProductAccordingToCategory() {
                    protected void onPreExecute() {
                        footerTV.setText("Loading...");
                    }

                    protected void onProgressUpdate(Product... values) {
                        super.onProgressUpdate(values);
                        Product productsDS = values[0];
                        productList.add(productsDS);
                        productGridAdapter.notifyDataSetChanged();
                        Log.e("Category Wise", "product added!!");
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (productList.size() < 1 && (result == null || result.equals(""))) {
                            showErrorLayout("No product available in this category.");
                        } else if (result != null && result.equals("Err: Internal Server Exception.")) {
                            showErrorLayout("Internal Server Exception.");
                        } else if (result != null && result.contains("Err: Connection Timeout")) {
                            showErrorLayout("Please check your internet connection.");
                        } else if (footer != null) {
                            if (productList.size() % 10 == 0) {
                                footerTV.setText(AppConstant.LOAD_MORE);
                            } else
                                footerTV.setText(AppConstant.NO_PRODUCT_AVAIL);
                        }
                    }
                }.execute(category, "" + i);
            } else {
                retriveProductAsync = new RetrieveProductAsyncTask(HomeActivity.this, productGridAdapter, productList);
                retriveProductAsync.execute(relation, occassion, "" + i, location);
            }
        } else if (v.equals(footer) && footerTV.getText().equals(AppConstant.NO_PRODUCT_AVAIL)) { //TODO
            Intent i = new Intent(HomeActivity.this, CategoryActivity.class);
            startActivityForResult(i, REQ_CODE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (retriveProductAsync != null && retriveProductAsync.getStatus() == Status.RUNNING) {
            retriveProductAsync.cancel(true);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.home, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem arg0) {
        if (arg0.getItemId() == R.id.contactMenu) {
            startActivity(new Intent(HomeActivity.this, Contact.class));
        } else if (arg0.getItemId() == R.id.fav) {
            startActivity(new Intent(HomeActivity.this, FavActivity.class));
        } else if (arg0.getItemId() == R.id.cart) {
            if (BakeryApplication.getCart().getLength() > 0) {
                startActivity(new Intent(HomeActivity.this, UserCartActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), AppConstant.CART_IS_EMPTY, Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private void populateAdapter(String query) {
        c = new MatrixCursor(new String[]{BaseColumns._ID, "productName"});
        for (int i = 0; i < productNameList.size(); i++) {
            if (productNameList.get(i).toLowerCase().contains(query.toLowerCase()))
                c.addRow(new Object[]{i, productNameList.get(i)});
        }
        mAdapter.changeCursor(c);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String categoryName = data.getStringExtra("categoryName");
                category = data.getStringExtra("result");
                // productList.clear();
                runCategoryAsync(category, categoryName);
            }
        }
    }

    public void runCategoryAsync(String category, String categoryName) {
        i = 1;
        if (footer.getVisibility() == View.VISIBLE) footer.setVisibility(View.GONE);
        if (errorLinearLayout.getVisibility() == View.VISIBLE)
            errorLinearLayout.setVisibility(View.GONE);
        new RetriveProductAccordingToCategory() {
            protected void onPreExecute() {
                errorLinearLayout.setVisibility(View.GONE);
                dialog = new ProgressDialog(HomeActivity.this, R.style.transparentDialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.setContentView(R.layout.progress);
                dialog.findViewById(R.id.loading_icon).startAnimation(AnimationUtils.loadAnimation(HomeActivity.this, R.anim.anim_rotate));
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                productList.clear();
                productGridAdapter.notifyDataSetChanged();

            }

            protected void onProgressUpdate(Product... values) {
                super.onProgressUpdate(values);
                Product productsDS = values[0];
                productList.add(productsDS);
                productGridAdapter.notifyDataSetChanged();
                Log.e("Category Wise", "product added!!");
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (productList.size() < 1 && (result == null || result.equals(""))) {
                    showErrorLayout("No product available in this category.");
                } else if (result.equals("Err: Internal Server Exception.")) {
                    showErrorLayout("Internal Server Exception.");
                } else if (result.contains("Err: Connection Timeout")) {
                    showErrorLayout("Please check your internet connection.");
                } else if (footer.getVisibility() == View.GONE) {
                    toggleFooterViewVisibilty();
                    if (productList.size() < 10)
                        footerTV.setText(AppConstant.NO_PRODUCT_AVAIL);
                    else if (productList.size() % 10 == 0) {
                        footerTV.setText(AppConstant.LOAD_MORE);
                    } else
                        footerTV.setText(AppConstant.NO_PRODUCT_AVAIL);
                } else if (footer != null) {
                    if (productList.size() % 10 == 0) {
                        footerTV.setText(AppConstant.LOAD_MORE);
                    } else
                        footerTV.setText(AppConstant.NO_PRODUCT_AVAIL);
                }
            }
        }.execute(category, "" + i);
        productFromCategory.setText(AppConstant.SHOWING_RESULTS_FROM + categoryName.toUpperCase()); //TODO Change the
        productFromCategory.setVisibility(View.VISIBLE);
    }
}

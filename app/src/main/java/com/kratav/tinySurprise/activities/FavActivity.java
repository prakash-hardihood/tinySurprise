package com.kratav.tinySurprise.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.FevListAdapter;
import com.kratav.tinySurprise.async.GetProductThroughSKU;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.database.FevDbOperations;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;


import java.util.List;

public class FavActivity extends BaseActivity implements OnItemClickListener {
    private ListView listView;
    private List<Product> fevProductList;
    private FevListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.CartActivityTheme);
        super.onCreate(savedInstanceState);
        actionbar();
        setContentView(R.layout.fev_product_layout);


        listView = (ListView) findViewById(R.id.fevListView);
        FevDbOperations fevDbOperations = new FevDbOperations(this);
        fevProductList = fevDbOperations.getAllFevProducts();
        adapter = new FevListAdapter(this, fevDbOperations.getAllFevProducts());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(this);
        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if(fevProductList.size() >0) {
            GetProductThroughSKU gpts = new GetProductThroughSKU(FavActivity.this);
            gpts.execute(fevProductList.get(arg2).getProductCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BakeryApplication.getCart().setCouponApplied(false);
        BakeryApplication.getCart().setCostWithCoupon(BakeryApplication.getCart().getCost());
        if (fevProductList.size() < 1) {
            TextView tv = new TextView(this);
            tv.setText("You have no favourites.");
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(null, Typeface.BOLD_ITALIC);
            tv.setBackgroundColor(getResources().getColor(R.color.grayBackground));
            listView.addHeaderView(tv);
            adapter.notifyDataSetChanged();
        }
    }
}

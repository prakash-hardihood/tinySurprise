package com.kratav.tinySurprise.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.ProductDetails;
import com.kratav.tinySurprise.bean.Dynamic;
import com.kratav.tinySurprise.database.FevDbOperations;
import com.kratav.tinySurprise.fragments.Categories1;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CategoryAdapters extends BaseAdapter implements ListAdapter {
    private Context context;
    private List<Dynamic> img;
    private ArrayList<Dynamic> mLblIcons = new ArrayList<Dynamic>();
    private LayoutInflater mLayoutInflater;
    private FevDbOperations db;


   /* int[] mImgIcons = {R.drawable.cupcakes, R.drawable.flowers, R.drawable.diwali,
            R.drawable.new_arrivals, R.drawable.experience, R.drawable.offers,
            R.drawable.chocolate_cookies, R.drawable.gift_combo, R.drawable.mens_cosmetics,
            R.drawable.mugs, R.drawable.personalized, R.drawable.portrait,
            R.drawable.teddy_bear, R.drawable.unique_gifts, R.drawable.vouchers, R.drawable.womens_apparels_accessories};
*/

    public CategoryAdapters(Context context, List<Dynamic> cat) {
        super();
        this.context = context;
        this.img = cat;
        this.mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        db = new FevDbOperations(context);

    }


    public int getCount() {

        return img.size();
    }

    public Object getItem(int position) {

        return mLblIcons.get(position);
    }

    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Dynamic dynamic = img.get(position);
        final ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.categories_grid, null);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.categoriesImageView);
            holder.lblIcom = (TextView) convertView.findViewById(R.id.categoryTextView);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        URL url = null;
        try {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int density = dm.densityDpi;

            switch(density){
                case DisplayMetrics.DENSITY_MEDIUM:
                    url = new URL(dynamic.getCateg_mimage());
                    //Ion.with(holder.imgIcon).placeholder(R.drawable.ph).load(url.toString());
                    System.out.println("Size is medium");
                    Log.e("imageurl", dynamic.getCateg_mimage());
                    Glide.with(context).load(url.toString()).placeholder(R.drawable.product_placeholder).into(holder.imgIcon);

                case DisplayMetrics.DENSITY_HIGH:
                    url = new URL(dynamic.getCateg_himage());
                    System.out.println("Size is high");
                    Log.e("imageurl", dynamic.getCateg_himage());
                    //Ion.with(holder.imgIcon).placeholder(R.drawable.ph).load(url.toString());
                    Glide.with(context).load(url.toString()).placeholder(R.drawable.product_placeholder).into(holder.imgIcon);

                case DisplayMetrics.DENSITY_XHIGH:
                    url = new URL(dynamic.getCateg_ximage());
                    System.out.println("Size is xhigh");
                    Log.e("imageurl", dynamic.getCateg_ximage());
                    //Ion.with(holder.imgIcon).placeholder(R.drawable.ph).load(url.toString());
                    Glide.with(context).load(url.toString()).placeholder(R.drawable.product_placeholder).into(holder.imgIcon);

                case DisplayMetrics.DENSITY_XXHIGH:
                    url = new URL(dynamic.getCateg_xximage());
                    System.out.println("Size is xxhigh");
                    Log.e("imageurl", dynamic.getCateg_xximage());
                    //Ion.with(holder.imgIcon).placeholder(R.drawable.ph).load(url.toString());
                    Glide.with(context).load(url.toString()).placeholder(R.drawable.product_placeholder).into(holder.imgIcon);

            }


            holder.lblIcom.setText(dynamic.getCategName());


        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }


        holder.layout.setBackgroundColor(0x30F1E7DD);
        /*
        if (position % 2 == 0)
            holder.layout.setBackgroundColor(0x30F1E7DD);
        else
            holder.layout.setBackgroundColor(Color.WHITE);
        */
        return convertView;
    }

    class ViewHolder {
        private ImageView imgIcon;
        private TextView lblIcom;
        private LinearLayout layout;
    }
}

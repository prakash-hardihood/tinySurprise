package com.kratav.tinySurprise.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.ProductDetails;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.grid.DynamicHeightImageView;
import com.kratav.tinySurprise.grid.DynamicHeightTextView;
import com.kratav.tinySurprise.grid.StaggeredGridView;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.utils.GlobalApp;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CategoriesGridSelectionResultAdapter extends BaseAdapter {
	private Context context;
	private int mWidth, mHight;
	private List<Product> productGalexy;

	public CategoriesGridSelectionResultAdapter(Context context,  List<Product> productListGalexy) {
		this.context = context;
		this.productGalexy = productListGalexy;
	}

	public int getCount() {
		return productGalexy.size();
	}

	public Object getItem(int position) {
		return productGalexy.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_item, null);
			convertView.setLayoutParams(new StaggeredGridView.GridLayoutParams(mWidth, mHight));
			holder = new ViewHolder();
			holder.imgIcon = (DynamicHeightImageView) convertView
					.findViewById(R.id.imageView1);
			holder.productName = (DynamicHeightTextView) convertView
					.findViewById(R.id.textView1);
			holder.productPrice = (TextView) convertView
					.findViewById(R.id.productPrice);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		URL url = null;
		holder.imgIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleClick(position);
			}
		});

		try {
			url = new URL(productGalexy.get(position).getProductImageUrl());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Glide.with(context).load(url.toString()).override(mWidth, mHight).into(holder.imgIcon);
		} catch (Exception e) {

		}

		try {
			holder.productName.setText(productGalexy.get(position)
					.getProductName());
			holder.productName.setTypeface(GlobalApp.getFont(context,
					GlobalApp.LIBERATION));
			holder.productPrice.setText(""
					+ context.getResources().getString(R.string.rs)
					+ productGalexy.get(position).getProductPrice());
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"Rupee_Foradian.ttf");
			holder.productPrice.setTypeface(face);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	public void handleClick(int position) {
		Product currentProduct = productGalexy.get(position);
		BakeryApplication.setCurrentProduct(currentProduct);
		Intent intent = new Intent(context, ProductDetails.class).putExtra(
				"RemainingActivity", "yes");
		context.startActivity(intent);
	}

	class ViewHolder {
		private ImageView imgIcon;
		private TextView productName, productPrice;
	}

}

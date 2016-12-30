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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.ProductDetails;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.database.FevDbOperations;
import com.kratav.tinySurprise.grid.DynamicHeightImageView;
import com.kratav.tinySurprise.grid.DynamicHeightTextView;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ProductGridAdapter extends BaseAdapter {

	private Context context;
	private List<Product> productGalexy;
	private FevDbOperations db;

	public ProductGridAdapter(Context context, List<Product> productListGalexy) {
		super();
		this.context = context;
		this.productGalexy = productListGalexy;
		db = new FevDbOperations(context);
	}

	public int getCount() {
		return productGalexy.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final Product product = productGalexy.get(position);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_item, null);
			holder = new ViewHolder();

			holder.imgIcon = (DynamicHeightImageView) convertView
					.findViewById(R.id.imageView1);
			holder.productName = (DynamicHeightTextView) convertView
					.findViewById(R.id.textView1);
			holder.productPrice = (TextView) convertView
					.findViewById(R.id.productPrice);
			holder.heartImageView = (ImageView) convertView
					.findViewById(R.id.heartImageView);
			holder.evValue  = (TextView) convertView.findViewById(R.id.eye_value);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		URL url = null;
		try {
			url = new URL(product.getProductImageUrl());

			//Ion.with(holder.imgIcon).placeholder(R.drawable.ph).load(url.toString());
			Glide.with(context).load(url.toString()).placeholder(R.drawable.product_placeholder).into(holder.imgIcon);

			holder.productName.setText(product.getProductName());
			holder.evValue.setText("" + product.getViewCount());
			holder.productPrice.setText(context.getResources().getString(
					R.string.rs)
					+ (int)productGalexy.get(position).getProductPrice());
			Typeface face = Typeface.createFromAsset(context.getAssets(),
					"Rupee_Foradian.ttf");
			holder.productPrice.setTypeface(face);

			if (db.isFav(product.getProductCode())) {
				System.out.println("ISfev: "
						+ db.isFav(product.getProductCode()));
				holder.heartImageView.setImageResource(R.drawable.fev_enable);
			} else
				holder.heartImageView.setImageResource(R.drawable.fav);

			holder.heartImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (db.favSKU(product)){
						holder.heartImageView
								.setImageResource(R.drawable.fev_enable);
						Toast.makeText(context, "Shortlisted", Toast.LENGTH_LONG).show();
					}
					else{
						holder.heartImageView.setImageResource(R.drawable.fav);
						Toast.makeText(context, "Favorite removed", Toast.LENGTH_LONG).show();
					}

				}
			});

			holder.imgIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					BakeryApplication.setCurrentProduct(product);
					Intent intent = new Intent(context, ProductDetails.class)
							.putExtra("RemainingActivity", "yes");
					context.startActivity(intent);

				}
			});
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		return convertView;

	}

	class ViewHolder {
		private ImageView imgIcon, heartImageView;
		private TextView productName, productPrice,evValue;
	}

}

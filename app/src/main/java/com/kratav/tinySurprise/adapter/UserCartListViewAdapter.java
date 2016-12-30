package com.kratav.tinySurprise.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.UserCartActivity;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.utils.GlobalApp;

import java.net.MalformedURLException;
import java.net.URL;

public class UserCartListViewAdapter extends BaseAdapter {
	private String qty1;
	private int qty;
	private Context context;

	public UserCartListViewAdapter(Activity context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return BakeryApplication.getCart().getCartItems().size();
	}

	@Override
	public Object getItem(int position) {
		return BakeryApplication.getCart().getCartItems().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ListViewHolder holder = null;
	Product product;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		URL url = null;
		product = BakeryApplication.getCart().getCartItems().get(position);
		View vi = convertView;

		if (vi == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.item_row_cart, null);

			// TextDetails
			holder = new ListViewHolder();
			holder.iThumb = (ImageView) vi.findViewById(R.id.imgThumbnailCart);
			holder.tTitle = (TextView) vi.findViewById(R.id.txtTitleCart);
			holder.tQty = (Spinner) vi.findViewById(R.id.txtQtyCart);
			holder.tCost = (TextView) vi.findViewById(R.id.tCostCart);
			holder.removeItem = (Button) vi.findViewById(R.id.removeButton);
			holder.tTitle.setTypeface(GlobalApp.getFont(context, GlobalApp.ROBOTOLITE));
			Typeface face = Typeface.createFromAsset(context.getAssets(), "Rupee_Foradian.ttf");
			holder.tCost.setTypeface(face);
			holder.optionTV = (TextView) vi.findViewById(R.id.cartOptionTextView);
			vi.setTag(holder);
		} else {
			holder = (ListViewHolder) vi.getTag();
		}

		holder.optionTV.setText("");

		for (int i = 0; i < product.getOptionSelected().size(); i++) {
			Log.e("Cart Adapter", "Option Size: " + product.getOptionSelected().size());
			String s = holder.optionTV.getText().toString();
			if(product.getOptionSelected().get(i).getOptionPrice() > 0){
				holder.optionTV.setText(s + "\n" + product.getOptionSelected().get(i).getValue() + " (" + product.getOptionSelected().get(i).getOptionPrice() + ")");
			}
			else{
				holder.optionTV.setText(s + "\n" + product.getOptionSelected().get(i).getValue() );
			}
			
		}

		holder.tTitle.setTextSize(13);
		holder.tTitle.setText(product.getProductName().toString());
		holder.tQty.setSelection(product.getQty() - 1);
		UserCartActivity.refresh();
		holder.tCost.setText("" + context.getResources().getString(R.string.rs) + (int) product.getProductCost());

		holder.tQty.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				UserCartActivity.refresh();
				qty1 = String.valueOf(holder.tQty.getSelectedItem());
				qty = Integer.parseInt(qty1);
				product.setQuantity(qty);
				holder.tCost.setText("" + context.getResources().getString(R.string.rs) + ((int) product.getProductCost()));
				Typeface face = Typeface.createFromAsset(context.getAssets(), "Rupee_Foradian.ttf");
				holder.tCost.setTypeface(face);
				UserCartActivity.refresh();
			}

			public void onNothingSelected(AdapterView<?> parentView) {
			}
		});

		// removing items from Cart
		holder.removeItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("Adapter", "Removed Item: " + product.getProductName() + " Position " + position);
				BakeryApplication.getCart().getCartItems().remove(position);
				product.setOptionsEmpty();
				UserCartActivity.refresh();
				notifyDataSetChanged();
			}
		});
		try {
			url = new URL(product.getProductImageUrl());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Glide.with(context).load(url.toString()).into(holder.iThumb);
		return vi;
	}

	class ListViewHolder {
		private ImageView iThumb;
		private TextView tTitle, tCost;
		private Button removeItem;
		private Spinner tQty;
		private TextView optionTV;
	}
}
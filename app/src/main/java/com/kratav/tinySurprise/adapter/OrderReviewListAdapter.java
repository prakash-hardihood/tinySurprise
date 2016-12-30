package com.kratav.tinySurprise.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.utils.GlobalApp;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class OrderReviewListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Product> items;
	private String quantity;
	private Context context;

	public OrderReviewListAdapter(Activity context, List<Product> items) {
		super();
		this.items = items;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		URL url = null;
		final Product product = items.get(position);
		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.item_row_order_review, null);

		ImageView iThumb = (ImageView) vi.findViewById(R.id.imgThumbnailOrderReview);
		TextView tTitle = (TextView) vi.findViewById(R.id.txtTitleOrderReview);
		TextView qty = (TextView) vi.findViewById(R.id.qty_txt);
		TextView number = (TextView) vi.findViewById(R.id.txtQtyCart);
		final TextView tCost = (TextView) vi.findViewById(R.id.txtShowCostOrderReview);
		TextView optionTV = (TextView) vi.findViewById(R.id.cartOptionOrderReview);

		tTitle.setTypeface(GlobalApp.getFont(context, GlobalApp.LATO_REG));
		optionTV.setTypeface(GlobalApp.getFont(context, GlobalApp.LATO_REG));
		qty.setTypeface(GlobalApp.getFont(context, GlobalApp.LATO_REG));
		number.setTypeface(GlobalApp.getFont(context, GlobalApp.LATO_REG));

		tTitle.setText(product.getProductName().toString());

		optionTV.setText("");
		for (int i = 0; i < product.getOptionSelected().size(); i++) {
			Log.e("Cart Adapter", "Option Size: " + product.getOptionSelected().size());
			String s = optionTV.getText().toString();
			if(product.getOptionSelected().get(i).getOptionPrice() > 0){
				optionTV.setText(s + "\n" + product.getOptionSelected().get(i).getValue() + " (+" + product.getOptionSelected().get(i).getOptionPrice() + ")");
			}
			else{
				optionTV.setText(s + "\n" + product.getOptionSelected().get(i).getValue() );
			}

		}


		Log.e("USERCARTFINALADAPTER", "Quantity : " + quantity);

	//	unit.setText(product.getQuantity()+" * " + (product.getCustomProductPrice()));
		tCost.setText("" + context.getResources().getString(R.string.rs) + product.getProductCost());
		Typeface face = Typeface.createFromAsset(context.getAssets(), "Rupee_Foradian.ttf");
		tCost.setTypeface(face);
		try {
			url = new URL(product.getProductImageUrl());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Glide.with(context).load(url.toString()).into(iThumb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vi;
	}
}
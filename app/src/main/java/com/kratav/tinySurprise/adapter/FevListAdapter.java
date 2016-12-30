package com.kratav.tinySurprise.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.bean.Product;

import java.util.List;

public class FevListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Product> fevProdctList;
	private Context context;

	public FevListAdapter(Activity context, List<Product> fevProducList) {
		super();
		this.fevProdctList = fevProducList;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public int getCount() {
		return fevProdctList.size();
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
		Product product = fevProdctList.get(position);
		View vi = convertView;
		ViewHolder holder;

		if (convertView == null){
			vi = inflater.inflate(R.layout.fev_item_row, null);
			holder = new ViewHolder();
			holder.fevImage = (ImageView) vi.findViewById(R.id.fevImageView);
			holder.fevTextView = (TextView) vi.findViewById(R.id.fevTextView);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
				
		holder.fevTextView.setText(product.getProductName());
		Glide.with(context).load(product.getProductImageUrl()).placeholder(R.drawable.ic_launcher).into(holder.fevImage);
		//ION.with(holder.fevImage).placeholder(R.drawable.ic_launcher).load();
		return vi;
	}
	
	class ViewHolder {
		private ImageView fevImage;
		private TextView fevTextView;
	}
}

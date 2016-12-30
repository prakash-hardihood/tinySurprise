package com.kratav.tinySurprise.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kratav.tinySurprise.R;

import java.util.List;

public class CitesAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<String> cities;

	public CitesAdapter(Activity context, List<String> cities) {
		super();
		this.cities = cities;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return cities.size();
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
		String city = cities.get(position);
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.city_listview_item, null);
		TextView tTitle = (TextView) vi.findViewById(R.id.txtTitleCity);
		tTitle.setText(city);
		return vi;
	}
}
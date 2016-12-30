package com.kratav.tinySurprise.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.ImageAdapter1;
import com.kratav.tinySurprise.utils.GlobalApp;


public class RelationFragment extends Fragment {
	private GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.relation, container, false);
		gridView = (GridView) rootView.findViewById(R.id.relationGridView);
		gridView.setAdapter(new ImageAdapter1(getActivity(),
				GlobalApp.relation, GlobalApp.reltext,null));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent i = getActivity().getIntent();
				i.putExtra("result", GlobalApp.reltext[position]);
				i.putExtra("categoryName", GlobalApp.reltext[position]);
				i.putExtra("categoryType", "rel");
				getActivity();
				getActivity().setResult(Activity.RESULT_OK, i);
				getActivity().finish();
			}
		});
		return rootView;
	}
}
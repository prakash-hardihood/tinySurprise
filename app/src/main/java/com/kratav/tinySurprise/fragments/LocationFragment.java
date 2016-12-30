package com.kratav.tinySurprise.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;


import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.adapter.CitesAdapter;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.GlobalApp;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationFragment extends Fragment {
    private ListView citiesListView;
    public EditText searchEditText;
    private ArrayList<String> cityList = new ArrayList<String>();
    private HashMap cityHashMap;
    CitesAdapter citiesAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.location, container, false);
        searchEditText = (EditText) rootView.findViewById(R.id.searchCitiesEditText);
        citiesListView = (ListView) rootView.findViewById(R.id.searchListView);

        cityHashMap = BaseActivity.cityHashMap();
        cityList.addAll(cityHashMap.keySet());

        citiesAdapter = new CitesAdapter(getActivity(), cityList);
        citiesListView.setAdapter(citiesAdapter);
        citiesAdapter.notifyDataSetChanged();
        TextWatcher searchCityTextWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @SuppressLint("DefaultLocale")
            public void afterTextChanged(Editable s) {
                cityList.clear();
                for (int i = 0; i < GlobalApp.cityArray.length; i++)
                    if ((GlobalApp.cityArray[i].toString().toLowerCase()).contains(searchEditText.getText().toString().toLowerCase()))
                        cityList.add(GlobalApp.cityArray[i]);
                citiesAdapter.notifyDataSetChanged();

            }
        };

        searchEditText.addTextChangedListener(searchCityTextWatcher);

        citiesListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = getActivity().getIntent();
                i.putExtra("result", cityHashMap.get(cityList.get(position)).toString());
                i.putExtra("categoryName",cityList.get(position).toString());
                i.putExtra("categoryType", "loc");
                getActivity();
                getActivity().setResult(Activity.RESULT_OK, i);
                getActivity().finish();
            }
        });
        return rootView;
    }



}

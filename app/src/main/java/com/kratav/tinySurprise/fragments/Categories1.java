package com.kratav.tinySurprise.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.kratav.tinySurprise.adapter.CategoryAdapters;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.lang.String;
import java.util.Arrays;
import java.util.List;

import com.kratav.tinySurprise.async.RestClient;
import com.kratav.tinySurprise.bean.Dynamic;



import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.bean.Dynamic;

import org.json.JSONArray;
import org.json.JSONObject;

public class Categories1 extends ListFragment  {
    public ListView categoriesListView;
    public CategoryAdapters categoryAdapter;
    private RetrieveDynamicCategory retriveDynamicCategory;
    /*   private String[] categoriesArray = {"Cakes", "Flowers", "Diwali", "New Arrivals", "Experiences", "Offers",
               "Chocolates", "Gift Combo", "Men's Accessories",
               "Mugs", "Personalized", "Portrait", "Teddy bear",
               "Unique Gifts", "Vouchers", "Women's Accessories"};
   */
    List<Dynamic> catList = new ArrayList<Dynamic>();
    ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View android = inflater.inflate(R.layout.categories_xml, container, false);


        categoryAdapter = new CategoryAdapters(getActivity(), catList);
        setListAdapter(categoryAdapter);

        retriveDynamicCategory = new RetrieveDynamicCategory(getActivity(), categoryAdapter, catList);
        retriveDynamicCategory.execute();

//        categoriesListView = (ListView)getView().findViewById(android.R.id.list);
//        categoriesListView.setOnItemClickListener(this);
        return android;
    }

    @Override
    public void onListItemClick(ListView arg0, View arg1, int position, long arg3) {

        System.gc();
        String temp = null;
        temp = catList.get(position).getCategURL();

        Intent i = getActivity().getIntent();
        i.putExtra("result", temp);
        i.putExtra("categoryName", catList.get(position).getCategName());
        i.putExtra("categoryType", "occ");
        getActivity().setResult(getActivity().RESULT_OK, i);
        getActivity().finish();


    }

    public class RetrieveDynamicCategory extends AsyncTask<String, Dynamic, String> {

        public Activity context;
        public ProgressDialog dialog;
        public CategoryAdapters pa;
        public List<Dynamic> catList;
        private static final String url_address = "http://tinysurprise.com/test_mode/mobi-app/dynamic_categories.php";


        public RetrieveDynamicCategory(Activity context, CategoryAdapters pa, List<Dynamic> catList) {
            this.context = context;
            this.pa = pa;
            this.catList = catList;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onProgressUpdate(Dynamic... values) {
            super.onProgressUpdate(values);
            Dynamic dyn = values[0];
            catList.add(dyn);
            pa.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... aloc) {

            String urlx;
            URL url = null;
            try {
                urlx = url_address ;
                url = new URL(urlx);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                        url.getHost(), url.getPort(), url.getPath(),
                        url.getQuery(), url.getRef());
                url = uri.toURL();
                Log.e("Dynamic Category", url.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url.toString());
                HttpResponse response = client.execute(request);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    int count;
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String jsonResult = RestClient
                                .convertStreamToString(instream);
                        Log.e("Retrive dynamic category", "" + jsonResult);

                        if (jsonResult == null || jsonResult.equalsIgnoreCase("null")) {
                            return null;
                        } else {

                            JSONArray ja = new JSONArray(jsonResult).getJSONArray(0);
                            System.out.println("By Category: " + ja.length() + " : " + ja);

                            JSONArray jx = ja.getJSONObject(0).getJSONArray("data");
                            for (count = 0; count <= jx.length() - 1; count++) {

                                Dynamic dyn = new Dynamic();
                                JSONObject x = jx.getJSONObject(count);
                                try {
                                    dyn.setCategName(x.getString("category_name"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    dyn.setCategURL(x.getString("category_url"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    dyn.setCateg_mimage(x
                                            .getString("category_image_medium"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    dyn.setCateg_himage(x
                                            .getString("category_image_large"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    dyn.setCateg_ximage(x
                                            .getString("category_image_XL"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    dyn.setCateg_xximage(x
                                            .getString("category_image_XXL"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                publishProgress(dyn);

                                if (dialog != null && dialog.isShowing() && count == 7) {
                                    dialog.dismiss();
                                }
                            }
                        }
                    }
                }
                return "Success";
            } catch (SocketTimeoutException e) {
                return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
            } catch (UnknownHostException e) {
                return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
            } catch (ConnectException e) {
                return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
            } catch (org.json.JSONException je) {
                return "Err: Internal Server Exception.";
            } catch (Exception e) {
                e.printStackTrace();
                return "Err: Some Prob. \n\n" + e.getMessage() + ".";
            } finally {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            categoryAdapter = new CategoryAdapters(getActivity(), catList);
            setListAdapter(categoryAdapter);

        }
    }
}



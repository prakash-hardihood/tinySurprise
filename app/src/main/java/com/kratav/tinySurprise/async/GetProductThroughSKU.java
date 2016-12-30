package com.kratav.tinySurprise.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;


import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.activities.FavActivity;
import com.kratav.tinySurprise.activities.ProductDetails;
import com.kratav.tinySurprise.bean.CustomOptions;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class GetProductThroughSKU extends AsyncTask<String, Void, String> {
    private Context context;
    private ProgressDialog dialog;
    private Product p;

    public GetProductThroughSKU(Context ctx) {
        this.context = ctx;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            URL url = new URL("http://tinysurprise.com/test_mode/mobi-app/product_detail.php?sku="
                    + arg0[0]);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();

            System.out.println(url);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url.toString());

            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String jsonResult = RestClient
                            .convertStreamToString(instream);

                    System.out.println(jsonResult);
                    JSONArray ja = new JSONArray(jsonResult);

                    JSONObject jo = ja.getJSONObject(0);
                    jo = jo.getJSONObject("data");
                    p = new Product();

                    try {
                        p.setProductName(jo.getString("Product name"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        p.setProductCode(jo.getString("Product code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        int isInStock = Integer.parseInt(jo.getString("is_in_stock"));
                        p.setIsInStock(isInStock == 1 ? true : false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        p.setViewCount(jo.getInt("Viewcount"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        p.setProductDescription(jo.getString("Description"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        p.setProductSDescription(jo.getString("Short_description"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String load = jo.getString("Price");

                        load = load.substring(0, load.indexOf("."));
                        String str = load.replaceAll("[^\\d.]", "");
                        int price = Integer.parseInt(str);
                        p.setProductPrice(price);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        p.setProductImageUrl(jo.getString("Thumbnail Image"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        p.setTimeToDeliver(jo.getString("Delivery Date"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<String> images = new ArrayList<String>();
                    try {
                        JSONObject galleryImages = jo
                                .getJSONObject("images");
                        JSONArray galleryImageArray = galleryImages
                                .getJSONArray("Base Image");

                        for (int i = 0; i < galleryImageArray.length(); i++) {
                            images.add(galleryImageArray.getString(i));
                            System.out.println(images);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        images.add("http://www.stevescheese.com/wp-content/plugins/woocommerce/assets/images/no_product_placeholder.png");
                    }
                    p.setGalleryImageUrlList(images);

                    try {

                        for (int i = 0; i < jo.getJSONArray("Custom Title").length(); i++) {
                            CustomOptions co = new CustomOptions();
                            co.setMsgTitle(jo.getJSONArray("Custom Title").getString(i).toString());
                            p.getCustomOptions().add(co);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < p.getCustomOptions().size(); i++)
                            if (jo.getJSONArray("Custom Require ").getInt(i) == 1)
                                p.getCustomOptions().get(i).setMendatory(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < p.getCustomOptions().size(); i++) {
                            p.getCustomOptions().get(i).setMsgType(jo.getJSONArray("Custom Type ").getString(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        System.out.println(jo.getString("Custom Values "));
                        String[] s = jo.getString("Custom Values ").split(",");
                        for (int i = 0; i < s.length; i++) {
                            p.getCustomOptions().get(i).setMsgValue(s[i]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        System.out.println(jo.getString("Custom Price"));
                        String[] s = jo.getString("Custom Price").split(",");
                        for (int i = 0; i < s.length; i++) {
                            p.getCustomOptions().get(i).setCustomPrice(s[i]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "Success";
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (ConnectException e) {
            return "Err: Connection Timeout. \n\nPlease make sure your internet connection is working.";
        } catch (org.json.JSONException je) {
            je.printStackTrace();
            return "Err: Internal Server Exception.";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dialog.isShowing())
                dialog.dismiss();
        }
        return "null";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Err: Internal Server Exception.")) {
            ((BaseActivity) context).showCustomDialog(context, AppConstant.OOPS, AppConstant.INTERNAL_SERVER_ERROR, false).show();
        } else if (result.contains("Err: Connection Timeout")) {
            ((BaseActivity) context).showCustomDialog(context, AppConstant.OOPS, AppConstant.CONN_TIMEOUT, false).show();
        } else if (result.equals("Success")) {
            BakeryApplication.setCurrentProduct(p);
            context.startActivity(new Intent(context, ProductDetails.class)
                    .putExtra("RemainingActivity", "yes"));
            if (context instanceof FavActivity) ((FavActivity) context).finish();
        }
    }
}

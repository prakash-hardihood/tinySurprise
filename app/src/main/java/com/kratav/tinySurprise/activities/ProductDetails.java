package com.kratav.tinySurprise.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.text.Html;
import android.text.Spanned;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.async.CheckProductAvailability;
import com.kratav.tinySurprise.async.UploadFileToServer;
import com.kratav.tinySurprise.bean.Additionals;
import com.kratav.tinySurprise.bean.Product;
import com.kratav.tinySurprise.bean.TempOptions;
import com.kratav.tinySurprise.contants.AppConstant;
import com.kratav.tinySurprise.dialog.SweetAlertDialog;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;
import com.kratav.tinySurprise.tinyApplication.BaseActivity;
import com.kratav.tinySurprise.utils.CirclePageIndicator;
import com.kratav.tinySurprise.utils.DynamicDetailsImageView;
import com.kratav.tinySurprise.utils.GlobalApp;
import com.kratav.tinySurprise.utils.OnSwipeTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ashutosh Tiwari ashutiwari4@gmail.com
 */
public class ProductDetails extends BaseActivity {

    private List<String> ImageUrlList;
    private android.support.v7.app.ActionBar mActionBar;
    private TextView /* productName, */ProductDetails, ProductDetails1, productPrice, customTitle, itemsIncartTextView;
    private Button addToCartButton, checkAvailability;
    private ImageView backButtonImageView, goShopImageView;
    private Product currentProductObject;
    private EditText pincodeEditText, et;
    private LinearLayout optionLinearLayout;
    private ArrayList<TempOptions> tempOptionArrayList = new ArrayList<TempOptions>();
    private LinearLayout imageLL;
    private int countFlag = 0;
    private CirclePageIndicator ci;
    private View optionDividerView;
    private DynamicDetailsImageView div;
    private int i = 0;
    Animation slide_in_left, slide_out_right;
    private Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws IndexOutOfBoundsException {
        setTheme(R.style.CheckoutActivityTheme);
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater li = LayoutInflater.from(this);
        View customView = li.inflate(R.layout.actionbar_custom, null);
        LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mActionBar.setCustomView(customView, l);
        Toolbar parent = (Toolbar) customView.getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);//
        backButtonImageView = (ImageView) customView.findViewById(R.id.customBackButton);
        goShopImageView = (ImageView) customView.findViewById(R.id.CustomGoShop);
        customTitle = (TextView) customView.findViewById(R.id.customProductTitle);
        setContentView(R.layout.product_details);

        itemsIncartTextView = (TextView) findViewById(R.id.itemsInCartTextView);
        // productName = (TextView) findViewById(R.id.productNameTextView);
        ProductDetails = (TextView) findViewById(R.id.productContentTextView);
        ProductDetails1 = (TextView) findViewById(R.id.productContentTextView1);
        productPrice = (TextView) findViewById(R.id.productPriceTextView);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);
        checkAvailability = (Button) findViewById(R.id.checkAvailability);
        pincodeEditText = (EditText) findViewById(R.id.pincodeEditText);
        optionLinearLayout = (LinearLayout) findViewById(R.id.optionLinearLayout);
        optionDividerView = (View) findViewById(R.id.pDetailsOptionDividerView);
        imageLL = (LinearLayout) findViewById(R.id.imageLL);
        ci = (CirclePageIndicator) findViewById(R.id.indicator);


        addToCartButton.setBackgroundDrawable(getBackgroundStateListDrawable(this,R.color.button_orange,R.color.button_orange_selector,0));

        currentProductObject = BakeryApplication.getCurrentProduct();

        if (!currentProductObject.isInStock()) addToCartButton.setText("Out of stock");
        else addToCartButton.setOnClickListener(addToCart);

        ImageUrlList = currentProductObject.getGalleryImageUrlList();
        div = new DynamicDetailsImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        div.setLayoutParams(layoutParams);
        div.setScaleType(ScaleType.FIT_XY);
        if (ImageUrlList.size() > i)
            Glide.with(this).load(ImageUrlList.get(i)).placeholder(R.drawable.product_placeholder).into(div);
        imageLL.addView(div);
        div.setOnTouchListener(swipe);

        if (ImageUrlList.size() <= 1)
            ci.setVisibility(View.GONE);
        else
            ci.setArray(ImageUrlList.size());

        checkAvailability.setOnClickListener(this);
        backButtonImageView.setOnClickListener(this);
        goShopImageView.setOnClickListener(this);

        try {
            customTitle.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
            productPrice.setTypeface(GlobalApp.getFont(ProductDetails.this, "EXO"));
            productPrice.setText("" + getResources().getString(R.string.rs) + currentProductObject.getProductPrice());
            Typeface face = Typeface.createFromAsset(getAssets(), "Rupee_Foradian.ttf");
            productPrice.setTypeface(face);
        } catch (Exception e) {
            e.printStackTrace();
        }
//      ProductDetails.setText(currentProductObject.getProductSDescription());
//      ProductDetails1.setText(currentProductObject.getProductDescription());
        ProductDetails.setText(Html.fromHtml(currentProductObject.getProductSDescription()));
        ProductDetails1.setText(Html.fromHtml(currentProductObject.getProductDescription()));
        customTitle.setText(currentProductObject.getProductName());

        if (currentProductObject.getCustomOptions().size() > 0) {
            optionLinearLayout.setVisibility(View.VISIBLE);
            optionDividerView.setVisibility(View.VISIBLE);
        }
        System.out.println("No of option is: " + currentProductObject.getCustomOptions().size());
        for (int x = 0; x < currentProductObject.getCustomOptions().size(); x++) {
            if (currentProductObject.getCustomOptions().get(x).getMsgType().equals("null"))
                continue;
            if (currentProductObject.getCustomOptions().get(x).getMsgType().equals("field")) {
                TextView tv = new TextView(this);
                tv.setTextColor(Color.parseColor("#551e17"));
                tv.setText(currentProductObject.getCustomOptions().get(x).getMsgTitle());
                tv.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));

                optionLinearLayout.addView(tv);
                et = new EditText(this);
                optionLinearLayout.addView(et);
                et.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                et.setTextColor(Color.parseColor("#7d7d7d"));

                TempOptions to = new TempOptions();
                to.setOptionTitle(tv.getText().toString());
                to.setObject(et);
                if (currentProductObject.getCustomOptions().get(x).isMendatory())
                    to.setMendetory(true);
                tempOptionArrayList.add(to);
            }
            if (currentProductObject.getCustomOptions().get(x).getMsgType().equals("radio")) {
                TextView tv = new TextView(this);
                tv.setTextColor(Color.parseColor("#551e17"));
                tv.setText(currentProductObject.getCustomOptions().get(x).getMsgTitle());
                tv.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                optionLinearLayout.addView(tv);

                RadioGroup rg = new RadioGroup(this); // create the RadioGroup
                rg.setOrientation(RadioGroup.VERTICAL);// or RadioGroup.VERTICAL
                System.out.println(currentProductObject.getCustomOptions().get(x).getMsgValue());
                String temp = currentProductObject.getCustomOptions().get(x)
                        .getMsgValue().charAt(0) == '#' ? currentProductObject
                        .getCustomOptions().get(x).getMsgValue().substring(1)
                        : currentProductObject.getCustomOptions().get(x).getMsgValue();
                String[] op = temp.split("#");
                String customPrice = currentProductObject.getCustomOptions().get(x).getCustomPrice().substring(1);
                String[] cp = customPrice.split("#");
                RadioButton[] rb = new RadioButton[op.length];
                for (int i = 0; i < op.length; i++) {
                    System.out.println("Adding Radio Button: " + op[i]);
                    rb[i] = new RadioButton(this);
                    rg.addView(rb[i]); // the RadioButtons are added to the
                    // radioGroup instead of the layout

                    if (cp[i].equals("0.0000"))
                        rb[i].setText(op[i]);
                    else
                        rb[i].setText(op[i].equals("null") ? "null" : op[i] + " \t +" + (cp[i].equals("null") ? "null" : (int) Double.parseDouble(cp[i])));
                    rb[i].setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                    rb[i].setTextColor(Color.parseColor("#7d7d7d"));
                }
                optionLinearLayout.addView(rg);
                TempOptions to = new TempOptions();
                to.setOptionTitle(tv.getText().toString());
                to.setObject(rg);
                if (currentProductObject.getCustomOptions().get(x).isMendatory())
                    to.setMendetory(true);
                tempOptionArrayList.add(to);
            }
            if (currentProductObject.getCustomOptions().get(x).getMsgType().equals("checkbox")) {
                TextView tv = new TextView(this);
                tv.setTextColor(Color.parseColor("#551e17"));
                tv.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                tv.setText(currentProductObject.getCustomOptions().get(x).getMsgTitle());
                optionLinearLayout.addView(tv);
                System.out.println("MSG_VALUE: " + currentProductObject.getCustomOptions().get(x).getMsgValue());
                if (currentProductObject.getCustomOptions().get(x).getMsgValue() == null || currentProductObject.getCustomOptions().get(x).getMsgValue().equals("null")) {
                    continue;
                } else if (currentProductObject.getCustomOptions().get(x).getMsgValue().length() > 1) {
                    String temp = currentProductObject.getCustomOptions()
                            .get(x).getMsgValue().charAt(0) == '#' ? currentProductObject
                            .getCustomOptions().get(x).getMsgValue().substring(1)
                            : currentProductObject.getCustomOptions().get(x).getMsgValue();
                    String[] op = temp.split("#");
                    String customPrice = currentProductObject.getCustomOptions().get(x).getCustomPrice()
                            .substring(1);
                    String[] cp = customPrice.split("#");

                    for (int i = 0; i < op.length; i++) {
                        CheckBox cb = new CheckBox(this);
                        try {
                            if (cp[i].equals("0.0000"))
                                cb.setText(op[i]);
                            else
                                cb.setText(op[i] + " \t +" + (int) Double.parseDouble(cp[i]));
                        } catch (Exception e) {
                            Toast.makeText(ProductDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();

                        }
                        cb.setTextColor(Color.parseColor("#7d7d7d"));
                        cb.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                        optionLinearLayout.addView(cb);

                        TempOptions to = new TempOptions();
                        to.setOptionTitle(tv.getText().toString());
                        to.setObject(cb);
                        if (currentProductObject.getCustomOptions().get(x).isMendatory()) {
                            to.setMendetory(true);
                        }
                        tempOptionArrayList.add(to);
                    }
                }
            }
            if (currentProductObject.getCustomOptions().get(x).getMsgType().equals("area")) {
                TextView tv = new TextView(this);
                tv.setTextColor(Color.parseColor("#551e17"));
                tv.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                tv.setText(currentProductObject.getCustomOptions().get(x).getMsgTitle());
                optionLinearLayout.addView(tv);

                et = new EditText(this);
                optionLinearLayout.addView(et);
                et.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                et.setTextColor(Color.parseColor("#7d7d7d"));

                TempOptions to = new TempOptions();
                to.setOptionTitle(tv.getText().toString());
                to.setObject(et);
                if (currentProductObject.getCustomOptions().get(x).isMendatory()) {
                    to.setMendetory(true);
                }
                tempOptionArrayList.add(to);
            }
            if (currentProductObject.getCustomOptions().get(x).getMsgType().equals("file")) {//TODO
                TextView tv = new TextView(this);
                tv.setTextColor(Color.parseColor("#551e17"));
                tv.setTypeface(GlobalApp.getFont(ProductDetails.this, GlobalApp.ROBOTOLITE));
                tv.setText(currentProductObject.getCustomOptions().get(x).getMsgTitle());
                optionLinearLayout.addView(tv);

                uploadButton = new Button(this);
                uploadButton.setText("Choose a file");
                optionLinearLayout.addView(uploadButton);

                uploadButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Choose an image"), 11);
                    }
                });
                TempOptions to = new TempOptions();
                to.setOptionTitle(tv.getText().toString());
                to.setObject(uploadButton);
                if (currentProductObject.getCustomOptions().get(x).isMendatory()) {
                    to.setMendetory(true);
                }
                tempOptionArrayList.add(to);
            }
        }
        BakeryApplication application = (BakeryApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }





    OnSwipeTouchListener swipe = new OnSwipeTouchListener() {
        @Override
        public void onSwipeLeft() {
            super.onSwipeLeft();
            if (countFlag < ImageUrlList.size() && countFlag >= 0) {
                Glide.with(ProductDetails.this).load(ImageUrlList.get(countFlag + 1)).placeholder(R.drawable.product_placeholder).into(div);
                ci.setCurrentPage(countFlag + 1);
                countFlag++;
                //imageLL.showNext();
            }
        }

        @Override
        public void onSwipeRight() {
            super.onSwipeRight();
            if (countFlag >= 0 && countFlag < ImageUrlList.size()) {
                Glide.with(ProductDetails.this).load(ImageUrlList.get(countFlag - 1)).placeholder(R.drawable.product_placeholder).into(div);
                ci.setCurrentPage(countFlag - 1);
                countFlag--;
                //imageLL.showPrevious();
            }
        }

        @Override
        public void onSwipeTop() {
            super.onSwipeTop();
        }

        @Override
        public void onSwipeBottom() {
            super.onSwipeBottom();
        }
    };


    public void ImageViewAnimateChange(Context context, int v) {
        final Animation anim_out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                // v.startAnimation(anim_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
        try {
            Typeface type = Typeface.createFromAsset(getAssets(),
                    "Exo-Regular.otf");
            addToCartButton.setTypeface(type);
            checkAvailability.setTypeface(type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BakeryApplication.getCart().getLength() <= 0) {
            itemsIncartTextView.setVisibility(View.GONE);
        } else {
            itemsIncartTextView.setText("" + BakeryApplication.getCart().getLength());
        }
        mTracker.setScreenName("Image~" + getClass().getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {
            System.out.println("ProductDetails finish" + data.getStringExtra("MESSAGE"));
            if (data.getStringExtra("MESSAGE").equals("finishBack"))
                finish();
        } else if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(ProductDetails.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                Uri selectedImageUri = data.getData();
                String s = getPath(ProductDetails.this, selectedImageUri);
                if (s == null) {
                    Toast.makeText(ProductDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    return;
                }
                new UploadFileToServer(ProductDetails.this, s) {
                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        Log.e("UploadFile", "Response from server: " + result);
                        //showAlert(result);

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String s = jsonObject.getString("file_name");
                            Additionals additionals = new Additionals();
                            additionals.setType("file");
                            additionals.setValue(s);
                            currentProductObject.getOptionSelected().add(additionals);
                            if (uploadButton != null) {
                                uploadButton.setText(s);
                                uploadButton.setTextSize(12);
                                uploadButton.setTextColor(getResources().getColor(R.color.grayCart));
                                uploadButton.setTypeface(null, Typeface.ITALIC);
                                uploadButton.setBackgroundColor(getResources().getColor(R.color.success_stroke_color));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        sweetAlertDialog.setTitleText(AppConstant.UPLOADED)
                                .setContentText(AppConstant.UPLOADED_SUCCESSFUL_MSG)
                                .setConfirmText(AppConstant.OK)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);


                    }
                }.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetails.this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor;
        if (Build.VERSION.SDK_INT > 19) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String sel = MediaStore.Images.Media._ID + "=?";
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, sel, new String[]{id}, null);
        } else {
            cursor = getContentResolver().query(uri, projection, null, null, null);

        }
        String path;
        int column_index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        cursor.moveToFirst();
        path = cursor.getString(column_index);
        System.out.println("Out: " + path);
        cursor.close();
        return path;
    }

    OnClickListener addToCart = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!validate())
                return;
            if (currentProductObject.isInCart() != true) {
                if (tempOptionArrayList.size() > 0)
                    for (int i = 0; i < tempOptionArrayList.size(); i++) {
                        if (tempOptionArrayList.get(i).getObject() instanceof RadioGroup
                                && ((RadioGroup) tempOptionArrayList.get(i)
                                .getObject()).getCheckedRadioButtonId() != -1) {
                            RadioButton rb = (RadioButton) ((RadioGroup) tempOptionArrayList
                                    .get(i).getObject())
                                    .findViewById(((RadioGroup) tempOptionArrayList
                                            .get(i).getObject())
                                            .getCheckedRadioButtonId());
                            String tmp[];
                            if (rb.getText().toString().contains("\t"))
                                tmp = rb.getText().toString().split("\t");
                            else {
                                tmp = new String[1];
                                tmp[0] = rb.getText().toString();
                            }

                            //if (currentProductObject.getOptionSelected().size() < 1) {
                            Additionals additionals = new Additionals();
                            additionals.setType("radio");
                            additionals.setTitle(tempOptionArrayList.get(i).getOptionTitle());
                            additionals.setValue(rb.getText().toString().split("\t")[0]);
                            if (tmp.length == 2 && tmp[1].length() > 0)
                                additionals.setOptionPrice(Math.round(Float.parseFloat(tmp[1])));
                            else
                                additionals.setOptionPrice(0);
                            currentProductObject.getOptionSelected().add(additionals);
                            //}

                        } else if (tempOptionArrayList.get(i).getObject() instanceof CheckBox
                                && ((CheckBox) tempOptionArrayList.get(i).getObject()).isChecked() == true) {
                            System.out.println(((CheckBox) tempOptionArrayList.get(i).getObject()).getText().toString());
                            String[] tmp;
                            if (((CheckBox) tempOptionArrayList.get(i).getObject()).getText().toString().contains("\t"))
                                tmp = ((CheckBox) tempOptionArrayList.get(i).getObject()).getText().toString().split("\t");
                            else {
                                tmp = new String[1];
                                tmp[0] = ((CheckBox) tempOptionArrayList.get(i).getObject()).getText().toString().trim();
                            }

                            //if (currentProductObject.getOptionSelected().size() < 1) {
                            Additionals additionals = new Additionals();
                            additionals.setType("checkbox");
                            additionals.setTitle(tempOptionArrayList.get(i).getOptionTitle());
                            //TODO additionals.setTitle(currentProductObject.getCustomOptions().get);
                            additionals.setValue(tmp[0]);
                            if (tmp.length == 2 && tmp[1].length() > 0)
                                additionals.setOptionPrice(Math.round(Float.parseFloat(tmp[1])));
                            else
                                additionals.setOptionPrice(0);
                            currentProductObject.getOptionSelected().add(additionals);
                            //}

                        } else if (tempOptionArrayList.get(i).getObject() instanceof EditText
                                && ((EditText) tempOptionArrayList.get(i)
                                .getObject()).length() != 0 == true) {
                            String[] tmp;
                            if (((EditText) tempOptionArrayList.get(i)
                                    .getObject()).getText().toString()
                                    .contains("\t"))
                                tmp = ((EditText) tempOptionArrayList.get(i)
                                        .getObject()).getText().toString()
                                        .split("\t");
                            else {
                                tmp = new String[1];
                                tmp[0] = ((EditText) tempOptionArrayList.get(i)
                                        .getObject()).getText().toString()
                                        .trim();
                            }
                            //if (currentProductObject.getOptionSelected().size() < 1) {
                            Additionals additionals = new Additionals();
                            additionals.setValue(tmp[0]);
                            additionals.setType("text");
                            additionals.setTitle(tempOptionArrayList.get(i).getOptionTitle());
                            if (tmp.length == 2 && tmp[1].length() > 0)
                                additionals.setOptionPrice(Math.round(Float.parseFloat(tmp[1])));
                            else
                                additionals.setOptionPrice(0);
                            currentProductObject.getOptionSelected().add(additionals);
                        }
                        //}
                    }
                currentProductObject.setCustomProductPrice();
                BakeryApplication.add2Cart(currentProductObject);
                itemsIncartTextView.setVisibility(View.VISIBLE);
                itemsIncartTextView.setText("" + BakeryApplication.getCart().getLength());
                startActivityForResult(new Intent(ProductDetails.this, UserCartActivity.class), 2);
            } else {
                Toast.makeText(getApplicationContext(), currentProductObject.getProductName()
                        + "is already in cart!", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(ProductDetails.this, UserCartActivity.class), 2);
            }
        }
    };

    public boolean validate() {
        System.out.println("tempOptionArrayList: " + tempOptionArrayList.size());
        for (int i = 0; i < tempOptionArrayList.size(); i++) {
            if (tempOptionArrayList.get(i).isMendetory()) {
                if (tempOptionArrayList.get(i).getObject() instanceof EditText
                        && ((EditText) tempOptionArrayList.get(i).getObject()).getText().toString().length() < 1) {
                    ((EditText) tempOptionArrayList.get(i).getObject()).setError("This field is mendatory");
                    Toast.makeText(ProductDetails.this, "Please fill mendatory fields", Toast.LENGTH_LONG).show();
                    return false;
                } else if (tempOptionArrayList.get(i).getObject() instanceof RadioGroup
                        && ((RadioGroup) tempOptionArrayList.get(i).getObject()).getCheckedRadioButtonId() == -1) {
                    Log.e("Validate", "In to this");
                    Toast.makeText(ProductDetails.this, "Pleae Select One Radio Button", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void onClick(View v) {
        if (v.equals(backButtonImageView)) {
            finish();
        } else if (v.equals(goShopImageView)) {
            if (BakeryApplication.getCart().getLength() > 0) {
                startActivity(new Intent(ProductDetails.this,
                        UserCartActivity.class));
                // finish();
            } else {
                Toast.makeText(getApplicationContext(), "Cart is empty!",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (v.equals(checkAvailability)) {
            if (pincodeEditText.getText().toString().trim().length() < 1) {
                pincodeEditText.setError("PLEASE ENTER PINCODE");
            } else {
                new CheckProductAvailability(ProductDetails.this,
                        currentProductObject.getProductName()).execute(
                        pincodeEditText.getText().toString().trim(),
                        currentProductObject.getProductCode());
            }
        }
    }
}

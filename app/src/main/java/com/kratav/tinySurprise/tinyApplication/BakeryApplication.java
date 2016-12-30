package com.kratav.tinySurprise.tinyApplication;

/** 
 * @author Ashutosh Tiwari
 * ashutiwari4@gmail.com
 */

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.kratav.tinySurprise.R;
import com.kratav.tinySurprise.bean.Cart;
import com.kratav.tinySurprise.bean.Marketing;
import com.kratav.tinySurprise.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class BakeryApplication extends Application {

	private static Cart cart = new Cart();
	private static Product currentProduct = new Product();
	private static List<Product> cartItems = new ArrayList<Product>();
	private static Marketing marketing = new Marketing();


	public static List<Product> getCartItems() {
		return cartItems;
	}

	public static void setCartItems(List<Product> cartItems) {
		BakeryApplication.cartItems = cartItems;
	}

	public static Marketing getMarketing() {
		return marketing;
	}

	public static void setMarketing(Marketing marketing) {
		BakeryApplication.marketing = marketing;
	}

	public static Product getCurrentProduct() {
		return currentProduct;
	}

	public static void setCurrentProduct(Product currentProductToGallery) {
		BakeryApplication.currentProduct = currentProductToGallery;
	}

	public static Cart getCart() {
		return cart;
	}

	public static void setCart(Cart cart) {
		BakeryApplication.cart = cart;
	}

	public static void add2Cart(Product product) {
		getCart().addToCart(product);
	}

	private Tracker mTracker;

	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
			mTracker = analytics.newTracker(R.xml.global_tracker1);
		}
		return mTracker;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}

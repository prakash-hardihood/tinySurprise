package com.kratav.tinySurprise.bean;

import android.annotation.SuppressLint;
import android.util.Log;

import com.kratav.tinySurprise.activities.UserCartActivity;
import com.kratav.tinySurprise.tinyApplication.BakeryApplication;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
public class Product {
	private String productName, productDescription, productSDescription, photoPath, shippingCharges, productImageUrl, permanentProductCategory, giftCategory, availableCities, TimeToDeliver, productCode;
	private int qty = 1;

	/**
	 * @productPrice: Actual price of product
	 * @productCost: price*qty
	 * @customProductPrice productPrice+options
	 */

	private float productPrice, productCost, customProductPrice;
	private boolean isInStock;
	private List<String> galleryImageUrlList = new ArrayList<String>();

	public boolean isInStock() {
		return isInStock;
	}

	public void setIsInStock(boolean isInStock) {
		this.isInStock = isInStock;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public ArrayList<CustomOptions> customOptions = new ArrayList<CustomOptions>();
	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	private ArrayList<Additionals> optionSelected = new ArrayList<Additionals>();
	private int viewCount;

	public ArrayList<Additionals> getOptionSelected() {
		return optionSelected;
	}

	public void setOptionSelected(ArrayList<Additionals> optionSelected) {
		this.optionSelected = optionSelected;
	}

	public void setOptionsEmpty(){
		optionSelected.clear();
	}
	
	public ArrayList<CustomOptions> getCustomOptions() {
		return customOptions;
	}

	public void setCustomOptions(ArrayList<CustomOptions> customOptions) {
		this.customOptions = customOptions;
	}

	public void setCustomProductPrice(float customProductPrice) {
		this.customProductPrice = customProductPrice;
	}

	public float getCustomProductPrice() {
		return customProductPrice;
	}

	public void setCustomProductPrice() {
		customProductPrice = productPrice;
		for (int i = 0; i < optionSelected.size(); i++) {
			System.out.println("ProductPrice is: " + productPrice);
			customProductPrice += optionSelected.get(i).getOptionPrice();
		}
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public List<String> getGalleryImageUrlList() {
		return galleryImageUrlList;
	}

	public void setGalleryImageUrlList(List<String> galleryImageUrlList) {
		this.galleryImageUrlList = galleryImageUrlList;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
		productCost = customProductPrice * qty;
		UserCartActivity.refresh();
	}

	public float getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getProductSDescription() {
		return productSDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public void setProductSDescription(String productSDescription) {
		this.productSDescription = productSDescription;
	}

	public String getShippingCharges() {
		return shippingCharges;
	}

	public void setShippingCharges(String shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public String getPermanentProductCategory() {
		return permanentProductCategory;
	}

	public void setPermanentProductCategory(String permanentProductCategory) {
		this.permanentProductCategory = permanentProductCategory;
	}

	public String getGiftCategory() {
		return giftCategory;
	}

	public void setGiftCategory(String giftCategory) {
		this.giftCategory = giftCategory;
	}

	public String getAvailableCities() {
		return availableCities;
	}

	public void setAvailableCities(String availableCities) {
		this.availableCities = availableCities;
	}

	public String getTimeToDeliver() {
		return TimeToDeliver;
	}

	public void setTimeToDeliver(String timeToDeliver) {
		TimeToDeliver = timeToDeliver;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setQuantity(int qty) {
		this.qty = qty;
		setProductCost(qty * getCustomProductPrice());
	}

	public int getQuantity() {
		return qty;
	}

	public String toString() {
		return productName;
	}

	@SuppressLint("DefaultLocale")
	public boolean isInCart() {
		for (Product p : BakeryApplication.getCart().getCartItems()) {
			if (p.productName.toString().toLowerCase().equals(this.productName.toString().toLowerCase())) {
				return true;
			}
			Log.e("Log", "cart product: " + p.productName + " current Product: " + this.productName);
		}
		return false;
	}
}

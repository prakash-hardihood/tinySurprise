package com.kratav.tinySurprise.bean;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<Product> cartItems;
	private float cost = 0;
	private float costWithCoupon = getCost();
	private boolean couponApplied = false;
	private String appliedCoupon,appliedCouponType,appliedCouponValue;
    private float shippingCost;

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getAppliedCouponType() {
        return appliedCouponType;
    }

    public void setAppliedCouponType(String appliedCouponType) {
        this.appliedCouponType = appliedCouponType;
    }

    public String getAppliedCouponValue() {
        return appliedCouponValue;
    }

    public void setAppliedCouponValue(String appliedCouponValue) {
        this.appliedCouponValue = appliedCouponValue;
    }

    public String getAppliedCoupon() {
		return appliedCoupon;
	}

	public void setAppliedCoupon(String appliedCoupon) {
		this.appliedCoupon = appliedCoupon;
	}

	public boolean isCouponApplied() {
		return couponApplied;
	}

	public void setCouponApplied(boolean couponApplied) {
		this.couponApplied = couponApplied;
	}

	public float getCostWithCoupon() {
		return costWithCoupon;
	}

	public void setCostWithCoupon(float costWithCoupon) {
		this.costWithCoupon = costWithCoupon;
		setCost(costWithCoupon);
	}

	public Cart() {
		cartItems = new ArrayList<Product>();
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float evaluateCost() {
		cost = 0;
		Log.e("Cart", "Total Item: " + cartItems.size());
		for (Product product : cartItems) {
			cost += product.getProductCost();
			Log.e("cost is:", "and: " + cost);
		}
		return cost;
	}

	public List<Product> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<Product> cartItems) {
		this.cartItems = cartItems;
	}

	public int getLength() {
		return cartItems.size();
	}

	public void addToCart(Product product) {
		// product.setInCart(true);
		cartItems.add(product);
	}

	public void removefromCartDS(Product product) {
		// product.setInCart(false);
		cartItems.remove(product);
	}

	public void setCartEmpty() {
		cartItems.clear();
	}

}
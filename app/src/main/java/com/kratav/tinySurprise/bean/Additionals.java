package com.kratav.tinySurprise.bean;

public class Additionals {
	private int optionPrice;
	private String value,type,title;
	public int getOptionPrice() {
		return optionPrice;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {

		this.type = type;
	}

	public void setOptionPrice(int optionPrice) {
		this.optionPrice = optionPrice;
	}


}

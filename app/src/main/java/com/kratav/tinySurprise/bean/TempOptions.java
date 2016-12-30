package com.kratav.tinySurprise.bean;


public class TempOptions {
	private Object object;
	private boolean isMendetory=false;

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	private String optionTitle;
	public Object getObject() {
		return object;
	}

	public boolean isMendetory() {
		return isMendetory;
	}

	public void setMendetory(boolean isMendetory) {
		this.isMendetory = isMendetory;
	}

	public void setObject(Object object) {
		this.object = object;
	}

}

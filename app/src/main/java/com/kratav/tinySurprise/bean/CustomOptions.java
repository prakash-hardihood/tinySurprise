package com.kratav.tinySurprise.bean;

public class CustomOptions {
	private boolean isMendatory = false;
	private String MsgValue, msgType, msgTitle, customPrice;

	public boolean isMendatory() {
		return isMendatory;
	}

	public void setMendatory(boolean isMendatory) {
		this.isMendatory = isMendatory;
	}

	public String getMsgValue() {
		return MsgValue;
	}

	public void setMsgValue(String msgValue) {
		MsgValue = msgValue;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(String customPrice) {
		this.customPrice = customPrice;
	}

}

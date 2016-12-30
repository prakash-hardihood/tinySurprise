package com.kratav.tinySurprise.utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;


import com.kratav.tinySurprise.R;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

import javax.crypto.Cipher;

public class GlobalApp {
	public static final String PARAMETER_SEP = "&";
	public static final String PARAMETER_EQUALS = "=";
	public static final String JSON_URL = "https://secure.ccavenue.com/transaction/transaction.do";
	public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";
	public static final String COMMAND = "command";
	public static final String ACCESS_CODE = "access_code";
	public static final String MERCHANT_ID = "merchant_id";
	public static final String ORDER_ID = "order_id";
	public static final String AMOUNT = "amount";
	public static final String CURRENCY = "currency";
	public static final String REDIRECT_URL = "redirect_url";
	public static final String CANCEL_URL = "cancel_url";
	public static final String LANGUAGE = "language";
	public static final String BILLING_NAME = "billing_name";
	public static final String BILLING_ADDRESS = "billing_address";
	public static final String BILLING_CITY = "billing_city";
	public static final String BILLING_STATE = "billing_state";
	public static final String BILLING_ZIP = "billing_zip";
	public static final String BILLING_COUNTRY = "billing_country";
	public static final String BILLING_TEL = "billing_tel";
	public static final String BILLING_EMAIL = "billing_email";
	public static final String DELIVERY_NAME = "delivery_name";
	public static final String DELIVERY_ADDRESS = "delivery_address";
	public static final String DELIVERY_CITY = "delivery_city";
	public static final String DELIVERY_STATE = "delivery_state";
	public static final String DELIVERY_ZIP = "delivery_zip";
	public static final String DELIVERY_COUNTRY = "delivery_country";
	public static final String DELIVERY_TEL = "delivery_tel";
	public static final String MERCHANT_PARAM1 = "merchant_param1";
	public static final String MERCHANT_PARAM2 = "merchant_param2";
	public static final String MERCHANT_PARAM3 = "merchant_param3";
	public static final String MERCHANT_PARAM4 = "merchant_param4";
	public static final String PAYMENT_OPTION = "payment_option";
	public static final String CARD_TYPE = "card_type";
	public static final String CARD_NAME = "card_name";
	public static final String DATA_ACCEPTED_AT = "data_accept";
	public static final String CARD_NUMBER = "card_number";
	public static final String EXPIRY_MONTH = "expiry_month";
	public static final String EXPIRY_YEAR = "expiry_year";
	public static final String CVV = "cvv_number";
	public static final String ISSUING_BANK = "issuing_bank";
	public static final String ENC_VAL = "enc_val";
	public static final String RSA_KEY_URL = "rsa_key_url";

	
	public static Integer[] occasion = { R.drawable.birthday, R.drawable.anniversary, R.drawable.wedding, R.drawable.congratulate, R.drawable.baby_shower, R.drawable.house_warming, R.drawable.i_love_you,
			R.drawable.i_miss_you, R.drawable.i_am_sorry };
	public static Integer[] occasion1 = { R.drawable.birthday, R.drawable.anniversary, R.drawable.wedding, R.drawable.congratulate, R.drawable.baby_shower, R.drawable.house_warming, R.drawable.i_love_you,
			R.drawable.i_miss_you, R.drawable.i_am_sorry };


 	public static int childnumber;
	public static String[] reltext = { "Boyfriend", "Girlfriend", "Husband", "Wife", "Dad", "Mom", "Brother", "Sister", "Kids", "Couples", "Boss", "Friend" };
	public static String[] occtext = { "Birthday", "Anniversary", "Wedding", "Congratulate", "Baby Shower", "House Warming", "I Love you", "I Miss you", "I am Sorry" };

	public static Integer[] relation = { R.drawable.bof, R.drawable.gif, R.drawable.hubby, R.drawable.wif, R.drawable.fath, R.drawable.moth, R.drawable.bro, R.drawable.sis, R.drawable.kids,
			R.drawable.sweet, R.drawable.boss, R.drawable.friends };
	public static Integer[] relation1 = { R.drawable.bof, R.drawable.gif, R.drawable.hubby, R.drawable.wif, R.drawable.fath, R.drawable.moth, R.drawable.bro, R.drawable.sis, R.drawable.kids,
			R.drawable.sweet, R.drawable.boss, R.drawable.friends };
	public static Integer[] relation2 = { R.drawable.bof, R.drawable.gif, R.drawable.hubby, R.drawable.wif, R.drawable.fath, R.drawable.moth, R.drawable.bro, R.drawable.sis, R.drawable.kids,
			R.drawable.sweet, R.drawable.boss, R.drawable.friends };

	public static String[] relArray = { "Boyfriend", "Girlfriend", "Husband", "Wife", "Dad", "Mom", "Brother", "Sister", "Kids", "Couples", "Boss", "Friend" };
	public static String[] occArray = { "Birthday", "Anniversary", "Weeding", "Congratulate", "Baby Shower", " House Warming", "I Love you", "I Miss you", "I am Sorry" };
	public static String[] cityArray = { "Chennai", "Mumbai", "Bangalore", "Delhi", "Kolkata", "Hyderabad", "Chandigarh", "Pune", "Jaipur", "Shimla" };
	public static String[] cityArray1 = { "CHN", "MUM", "BLR", "DEL", "KOL", "HYD", "CHA", "PUN", "JPR", "SHM" };
	/**
	 * These Two variable are indicating different type of Errors
	 * 
	 * @MSG_CONNECTION_ERROR = There is some problem o client side Which is TimeOut Connection Error IN Async Task this code HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 10000);
	 *                       Defines this Msg which is Used to Show Error Message on Dialog
	 * @MSG_NORMAL_ERROR = The Error Msg which are being retrieved from the Server
	 */

	public static final int MSG_CONNECTION_ERROR = 0;
	public static final int MSG_SERVER_ERROR = 1;
	public static final int MSG_SUCCESS = 2;
	public static final int MSG_GEN_INFO = 3;

	@SuppressWarnings("deprecation")
	public static void giveMeDialog(final Context ctx, String title, final String msg, final int errorType) {
		AlertDialog alertDialog = new AlertDialog.Builder(ctx).create(); // Read
																			// Update
		alertDialog.setTitle(title);
		alertDialog.setIcon(0);
		alertDialog.setMessage(msg);

		alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(DialogInterface dialog, int which) {
				// here you can add functions
				if (errorType == GlobalApp.MSG_SERVER_ERROR) {

				} else if (errorType == GlobalApp.MSG_SUCCESS) {
					((Activity) ctx).finish();
				} else if (errorType == GlobalApp.MSG_GEN_INFO) {
					// DOES NOTHIN' :D
				}
			}
		});
		alertDialog.show(); // <-- See This!
	}

	public static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

	public final static String ROBOTOLITE = "ROBOTO-LIGHT.TTF";
	public final static String ROBOTOMEDIUM = "ROBOTO-MEDIUM.TTF";
	public final static String EXO = "Exo-Regular.otf";
	public final static String LIBERATION = "LiberationSansNarrow-Regulat.ttf";
	public final static String HELVETICA = "HELVETICANEUELTSTD-CN.OTF";
	public final static String ROBOTOBCI = "ROBOTO-BOLDCONDENSEDITALIC.TTF";
	public final static String PROXIMA = "ProximaNova-Light.otf";
	public final static String LATO_REG = "Lato-Regular.ttf";
	public final static String LATO_SEMI = "Lato-Semibold.ttf";
	public final static String LATO_HEAVY = "Lato-Heavy.ttf";
 
	public static Typeface getFont(Context context, String font) {
		if (font.equals(ROBOTOLITE)) {
			return Typeface.createFromAsset(context.getAssets(), ROBOTOLITE);
		}else if (font.equals(ROBOTOMEDIUM)) {
			return Typeface.createFromAsset(context.getAssets(), ROBOTOMEDIUM);
		}else if (font.equals(ROBOTOBCI)) {
			return Typeface.createFromAsset(context.getAssets(), ROBOTOBCI);
		}else if (font.equals(LATO_REG)) {
			return Typeface.createFromAsset(context.getAssets(), LATO_REG);
		}else if (font.equals(LATO_HEAVY)) {
			return Typeface.createFromAsset(context.getAssets(), LATO_HEAVY);
		}else if (font.equals(LATO_SEMI)) {
			return Typeface.createFromAsset(context.getAssets(), LATO_SEMI);
		}else if (font.equals(EXO)) {
			return Typeface.createFromAsset(context.getAssets(), EXO);
		} else if (font.equals(LIBERATION)) {
			return Typeface.createFromAsset(context.getAssets(), LIBERATION);
		} else if (font.equals(HELVETICA)) {
			return Typeface.createFromAsset(context.getAssets(), HELVETICA);
		} else if(font.equals(PROXIMA)){
			return Typeface.createFromAsset(context.getAssets(), PROXIMA);
		}
		return null;
	}

	public static String encrypt(String plainText, String key) {
		try {
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")), Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object chkNull(Object pData) {
		return (pData == null ? "" : pData);
	}

	public static java.util.Map tokenizeToHashMap(String msg, String delimPairValue, String delimKeyPair) throws Exception {
		java.util.Map keyPair = new HashMap();
		ArrayList respList = new ArrayList();
		String part = "";
		StringTokenizer strTkn = new StringTokenizer(msg, delimPairValue, true);
		while (strTkn.hasMoreTokens()) {
			part = (String) strTkn.nextElement();
			if (part.equals(delimPairValue)) {
				part = null;
			} else {
				respList = tokenizeToArrayList(part, delimKeyPair);
				if (respList.size() == 2)
					keyPair.put(respList.get(0), respList.get(1));
				else if (respList.size() == 1)
					keyPair.put(respList.get(0), null);
			}
			if (part == null)
				continue;
			if (strTkn.hasMoreTokens())
				strTkn.nextElement();
		}
		return keyPair.size() > 0 ? keyPair : null;
	}

	public static ArrayList tokenizeToArrayList(String msg, String delim) throws Exception {
		ArrayList respList = new ArrayList();
		String varName = null;
		String varVal = null;
		int index = msg.indexOf(delim);
		varName = msg.substring(0, index);
		if ((index + 1) != msg.length())
			varVal = msg.substring(index + 1, msg.length());
		respList.add(varName);
		respList.add(varVal);
		return respList.size() > 0 ? respList : null;
	}

	public static String addToPostParams(String paramKey, String paramValue) {
		return paramKey.concat(GlobalApp.PARAMETER_EQUALS).concat(paramValue).concat(GlobalApp.PARAMETER_SEP);

	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}


	public static String getDeviceName() {

		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;

		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	public static String getAndroidVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public static String getDeviceId(Context context) {


		String deviceId = "";
		final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (mTelephony.getDeviceId() != null) {
			deviceId = mTelephony.getDeviceId();
		} else {
			deviceId = Settings.Secure.getString(context
					.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		return deviceId;
	}


}

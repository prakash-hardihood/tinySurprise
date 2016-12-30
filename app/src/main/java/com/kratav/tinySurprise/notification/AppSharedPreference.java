package com.kratav.tinySurprise.notification;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by neha on 28/4/15.
 */
public class AppSharedPreference {


    private static AppSharedPreference mPreferences = null;
    private Context mContext = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String SELECTED_SUB_CATEGORIES_LIST = "sub_cat_list";
    private static final String SELECTED_INTRESTS_LIST = "intrests_list";
    private static final String prefName = "Mypref";

    private AppSharedPreference(Context nContext) {
        this.mContext = nContext;
        this.sharedPreferences = nContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public static synchronized AppSharedPreference getInstance(Context nContext) {
        if (mPreferences == null) {
            mPreferences = new AppSharedPreference(nContext);
        }
        return mPreferences;
    }

    public void clearPref(Context context) {

        setUserlId("");
        setIsLoggedIn(false);

    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void commitEditor() {
        editor.commit();
    }


    public String getUserSocialId() {
        return sharedPreferences.getString("user_socialid", "");
    }

    public void setUserSocialId(String userSocialId) {
        editor.putString("user_socialid", userSocialId);
        editor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString("user_id", "");
    }

    public void setUserlId(String userId) {
        editor.putString("user_id", userId);
        editor.commit();
    }

    public void setCurrentLati(String currentLati) {
        editor.putString("current_lati", currentLati);
        editor.commit();
    }

    public String getCurrentLati() {
        return sharedPreferences.getString("current_lati", "");
    }

    public void setCurrentLongi(String currentLongi) {
        editor.putString("current_lobgi", currentLongi);
        editor.commit();
    }

    public String getCurrentLongi() {
        return sharedPreferences.getString("current_lobgi", "");
    }

    public void setRegistrationId(String registrationId) {
        editor.putString("reg_id", registrationId);
        editor.commit();
    }

    public String getRegistrationId() {
        return sharedPreferences.getString("reg_id", "");
    }


    public void setIsLoggedIn(boolean isLoggedIn) {
        editor.putBoolean("is_logged_in", isLoggedIn);
        editor.commit();
    }

    public boolean getIsLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }


    public void setSelectedSubCatList(ArrayList<String> list) {

        int size = sharedPreferences.getInt(SELECTED_SUB_CATEGORIES_LIST + "_size", 0);

        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(SELECTED_SUB_CATEGORIES_LIST + "_" + i);

        // write the current list
        for (int i = 0; i < list.size(); i++)
            editor.putString(SELECTED_SUB_CATEGORIES_LIST + "_" + i, list.get(i));

        editor.putInt(SELECTED_SUB_CATEGORIES_LIST + "_size", list.size());

        editor.commit();
    }

    public ArrayList<String> getSelectedSubCatList() {

        int size = sharedPreferences.getInt(SELECTED_SUB_CATEGORIES_LIST + "_size", 0);

        ArrayList<String> data = new ArrayList<String>(size);
        for (int i = 0; i < size; i++)
            data.add(sharedPreferences.getString(SELECTED_SUB_CATEGORIES_LIST + "_" + i, null));

        return data;
    }


    public String getLoginType() {
        return sharedPreferences.getString("login_type", "f");
    }


    public void setLoginType(String loginType) {
        editor.putString("login_type", loginType);
        editor.commit();
    }


    public String getMobileNumber() {
        return sharedPreferences.getString("user_mobile", "");
    }

    public void setMobileNumber(String user_mobile) {
        editor.remove("user_mobile");
        editor.putString("user_mobile", user_mobile);
        editor.commit();
    }


    public String getAge() {
        return sharedPreferences.getString("user_age", "");
    }

    public void setAge(String user_age) {
        editor.remove("user_age");
        editor.putString("user_age", user_age);
        editor.commit();
    }

    public boolean getAppVisible() {
        return sharedPreferences.getBoolean("AppVisi", false);
    }

    public void setAppVisible(Boolean status) {
        editor.remove("AppVisi");
        editor.putBoolean("AppVisi", status);
        editor.commit();
    }

    public String getGender() {
        return sharedPreferences.getString("user_gender", "2");
    }

    public void setGender(String user_gender) {
        editor.remove("user_gender");
        editor.putString("user_gender", user_gender);
        editor.commit();
    }

    public String getUserAccessToken() {
        return sharedPreferences.getString("userAccessToken", "");
    }

    public void setUserAccessToken(String userAccessToken) {

        editor.remove("userAccessToken");
        editor.putString("userAccessToken", userAccessToken);
        editor.commit();
    }

    private String userAccessToken;

    public void setSelectedCategory(ArrayList<String> category) {


        //Set the values
        if (category != null) {
            Set<String> set = new HashSet<String>();
            set.addAll(category);
            editor.putStringSet("key", set);
            editor.commit();
        } else {
            editor.putStringSet("key", null);
            editor.commit();
        }

    }

    public ArrayList<String> getSelectedCategory() {

        ArrayList<String> list = new ArrayList<String>();
        Set<String> set = sharedPreferences.getStringSet("key", null);
        list = new ArrayList<String>(set);
        if (list.size() > 0) {
            return list;
        } else {

            return null;
        }

    }

    public void setCountryCallingCode(String countryCallingCode) {

        editor.putString("countryCallingCode", countryCallingCode);
        editor.commit();

    }

    public String getCountryCallingCode() {

        return sharedPreferences.getString("countryCallingCode", "");
    }


    public String getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        CurrentLocation = currentLocation;
    }

    private String CurrentLocation;


    public void setCountryCode(String countryCode) {
        editor.putString("countryCode", countryCode);
        editor.commit();
    }

    public String getCountryCode() {

        return sharedPreferences.getString("countryCode", "");

    }

    public void setLatitude(String latitude) {

        editor.putString("latitude", latitude);
        editor.commit();
    }

    public String getLatitude() {

        return sharedPreferences.getString("latitude", "");

    }

    public void setLongitude(String longitude) {

        editor.putString("longitude", longitude);
        editor.commit();
    }

    public String getLongitude() {

        return sharedPreferences.getString("longitude", "");
    }

    public void setTypeid(String typeId) {

        editor.putString("typeId", typeId);
        editor.commit();
    }

    public String getTypeId() {
        return sharedPreferences.getString("typeId", "1");
    }

    public void setAddress(String address) {

        editor.putString("address", address);
        editor.commit();
    }

    public String getAddress() {

        return sharedPreferences.getString("address", "");
    }

    public void setCity(String city) {
        editor.putString("city", city);
        editor.commit();
    }

    public String getCity() {
        return sharedPreferences.getString("city", "");
    }

    public void clearPreference() {

        editor.clear().commit();
    }

    public void setRadius(String radius) {
        editor.putString("radius", radius);
        editor.commit();
    }

    public String getRadius() {

        return sharedPreferences.getString("radius", "5");
    }

    public void setDeviceId(String deviceId) {

        editor.putString("deviceId", deviceId);
        editor.commit();
    }

    public String getDeviceId() {
        return sharedPreferences.getString("deviceId", "");
    }


    public void setGCMToken(String token) {

        editor.putString("gcmToken", token);
        editor.commit();
    }

    public String getGCMToken() {

        return sharedPreferences.getString("gcmToken", "");
    }

    public void setIsMyInteresSelected(boolean selected) {

        editor.putBoolean("selected", selected);
        editor.commit();
    }

    public boolean getIsMyInterestSelected() {

        return sharedPreferences.getBoolean("selected", true);
    }

    public void setPushNotification(String status) {

        editor.putString("pushNotification", status);
        editor.commit();
    }

    public String getPushNotification() {

        return sharedPreferences.getString("pushNotification", "1");
    }


}
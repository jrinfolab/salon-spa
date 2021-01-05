package com.jrinfolab.beautyshop.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static final String PREF_NAME = "salspa_preferences";

    private static final String IS_LOGGED_IN = "logged_in";
    private static final String BRANCH_IMAGES = "branch_images";
    private static final String GST_PERCENTAGE = "gst_percentage";

    public static void setIsLoggedIn(Context context, boolean isLogged) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(IS_LOGGED_IN, isLogged).apply();
    }

    public static boolean isLoggedId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(IS_LOGGED_IN, false);
    }

    public static void setBranchImage(Context context, String[] imageList) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if(imageList == null || imageList.length <= 0){
            sp.edit().remove(BRANCH_IMAGES).commit();
        } else {
            sp.edit().putString(BRANCH_IMAGES, Util.getString(imageList)).apply();
        }
    }

    public static String[] getBranchImage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String list = sp.getString(BRANCH_IMAGES, null);
        return list != null ? Util.getStringArray(list) : null;
    }

    public static void setGstRate(Context context, int percentage) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(GST_PERCENTAGE, percentage).apply();
    }

    public static int getGstRate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getInt(GST_PERCENTAGE, 18);
    }
}

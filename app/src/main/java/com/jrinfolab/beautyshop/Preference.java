package com.jrinfolab.beautyshop;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Preference {

    private static final String PREF_NAME = "salspa_preferences";

    private static final String IS_LOGGED_IN = "logged_in";
    private static final String BRANCH_IMAGES = "branch_images";

    public static void setIsLoggedIn(Context context, boolean isLogged) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(IS_LOGGED_IN, isLogged).apply();
    }

    public static boolean isLoggedId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(IS_LOGGED_IN, false);
    }

    public static void setBranchImage(Context context, String[] imageList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < imageList.length; i++) {
            sb.append(imageList[i]).append(",");
        }
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(BRANCH_IMAGES, sb.toString()).apply();
    }

    public static String[] getBranchImage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String list = sp.getString(BRANCH_IMAGES, null);
        if (list != null) {
            return list.split(",");
        }
        return null;
    }
}

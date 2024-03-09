package com.webmintinfotech.ecom.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


public class SharePreference {
    private static Context mContext;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PREF_NAME = "Ecommerce";
    private static final int PRIVATE_MODE = 0;
    public static final String isLogin = "isLogin";
    public static final String userId = "userid";
    public static final String userMobile = "usermobile";
    public static final String loginType = "loginType";
    public static final String userEmail = "useremail";
    public static final String userName = "userName";
    public static final String userProfile = "userprofile";
    public static final String isTutorial = "tutorial";
    public static final String isCoupon = "coupon";
    public static final String userRefralCode = "referral_code";
    public static final String SELECTED_LANGUAGE = "selected_language";
    public static final String UserLoginType = "isEmailLogin";
    public static final String Currency = "currency";
    public static final String CurrencyPosition = "currencyPosition";
    public static final String ReferralAmount = "referral_amount";

    public static int getIntPref(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, -1);
    }

    public static void setIntPref(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putInt(key, value).apply();
    }

    public static String getStringPref(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void setStringPref(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public static boolean getBooleanPref(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static void setBooleanPref(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putBoolean(key, value).apply();
    }

    @SuppressLint("CommitPrefEdits")
    public SharePreference(Context mContext1) {
        mContext = mContext1;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void mLogout() {
        editor.clear();
        editor.commit();
    }
}

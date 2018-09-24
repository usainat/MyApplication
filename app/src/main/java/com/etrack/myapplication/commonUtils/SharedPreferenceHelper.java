package com.etrack.myapplication.commonUtils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Hussain on 23-02-2018.
 */

public  class SharedPreferenceHelper {
    private final static String PREF_FILE = "PREFS";
    public static final String PREF_APP_COMPANY = "app_company";
    public static final String PREF_APP_BRANCH = "app_branch";
    public static final String PREF_APP_USERID = "app_user_id";
    public static final String PREF_APP_USERROLE = "app_user_role";
    public static final String PREF_APP_USERNAME = "pref_app_username";

    /**
     * Set a string shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Set a integer shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set a Boolean shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Get a string shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getSharedPreferenceString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    /**
     * Get a integer shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static int getSharedPreferenceInt(Context context, String key, int defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getInt(key, defValue);
    }

    /**
     * Get a boolean shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(key, defValue);
    }

    public static boolean clearSharedPreferencence(Context context) {
        /*context.getSharedPreferences(PREF_FILE, 0).edit().clear().apply();
        return true;*/
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
        return true;
    }

    public static boolean clearSharedExptToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        Map<String, ?> prefs = settings.getAll();
        for (Map.Entry<String, ?> prefToReset : prefs.entrySet()) {
            if (prefToReset.getKey().equals("token") || prefToReset.getKey().equals("tokenadded")) {
            } else {
                settings.edit().remove(prefToReset.getKey()).commit();
            }
        }
        return true;
    }
}
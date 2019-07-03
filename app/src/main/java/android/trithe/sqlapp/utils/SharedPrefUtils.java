package android.trithe.sqlapp.utils;


import android.content.Context;
import android.trithe.sqlapp.aplication.MyApplication;

import java.util.List;

public class SharedPrefUtils {

    public SecurePreferences preferences;

    /**
     * @param context your current context.
     */
    public SharedPrefUtils(Context context) {
        this.preferences = new SecurePreferences(context);
    }

//    public static void saveAccessToken(String token) {
//        SharedPrefUtils.putString(AppConstant.ACCESS_TOKEN, token);
//    }
//
//    public static String getAccessToken() {
//        return SharedPrefUtils.getString(AppConstant.ACCESS_TOKEN, "");
//    }

    private void putStringOrReplace(String key, String value) {
        if (value == null) {
            preferences.edit().remove(key).commit();
        } else {
            preferences.edit().putString(key, value).commit();
        }
    }

    private void putStringOrReplaceAsync(String key, String value) {
        if (value == null) {
            preferences.edit().remove(key).apply();
        } else {
            preferences.edit().putString(key, value).apply();
        }
    }

    private String getStringFromSharedPre(String key, String defValue) {
        if (preferences.contains(key)) {
            return preferences.getString(key, defValue);
        }
        return defValue;
    }

    private void putBooleanToShare(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    private Boolean getBooleanFromShare(String key, boolean defValue) {
        if (preferences.contains(key)) {
            return preferences.getBoolean(key, defValue);
        } else {
            return defValue;
        }
    }

    private void putIntToShare(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    private int getIntFromShare(String key, int defValue) {
        if (preferences.contains(key)) {
            return preferences.getInt(key, defValue);
        } else {
            return defValue;
        }
    }

    private void putLongToShare(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    private long getLongFromShare(String key, long defValue) {
        if (preferences.contains(key)) {
            return preferences.getLong(key, defValue);
        } else {
            return defValue;
        }
    }

    private void putFloatToShare(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    private float getFloatFromShare(String key, float defValue) {
        if (preferences.contains(key)) {
            return preferences.getFloat(key, defValue);
        } else {
            return defValue;
        }
    }

    private boolean containKEY(String key) {
        return preferences.contains(key);
    }

    private void deleteKey(String key) {
        if (preferences.contains(key)) {
            preferences.edit().remove(key).commit();
        }
    }

    public static void putList(List<String> key, String value) {
        putList(key, value);
    }

    public static String getList(List<String> key, String defValue) {
        return getList(key, defValue);
    }

    public static void putString(String key, String value) {
        MyApplication.getSharedPreferences().putStringOrReplace(key, value);
    }

    public static void putStringAsync(String key, String value) {
        MyApplication.getSharedPreferences().putStringOrReplaceAsync(key, value);
    }

    public static String getString(String key, String defValue) {
        return MyApplication.getSharedPreferences().getStringFromSharedPre(key, defValue);
    }

    public static void putInt(String key, int value) {
        MyApplication.getSharedPreferences().putIntToShare(key, value);
    }

    public static int getInt(String key, int defValue) {
        return MyApplication.getSharedPreferences().getIntFromShare(key, defValue);
    }

    public static void putLong(String key, int value) {
        MyApplication.getSharedPreferences().putLongToShare(key, value);
    }

    public static long getLong(String key, int defValue) {
        return MyApplication.getSharedPreferences().getLongFromShare(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        MyApplication.getSharedPreferences().putBooleanToShare(key, value);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return MyApplication.getSharedPreferences().getBooleanFromShare(key, defValue);
    }

    public static void putFloat(String key, float value) {
        MyApplication.getSharedPreferences().putFloatToShare(key, value);
    }

    public static float getFloat(String key, float defValue) {
        return MyApplication.getSharedPreferences().getFloatFromShare(key, defValue);
    }

    public static boolean containKey(String key) {
        return MyApplication.getSharedPreferences().containKEY(key);
    }

    public static void removeKey(String key) {
        MyApplication.getSharedPreferences().deleteKey(key);
    }

    public static void clearData() {
        MyApplication.getSharedPreferences().clearAllData();
    }

    private void clearAllData() {
        preferences.edit().clear().commit();
    }

}

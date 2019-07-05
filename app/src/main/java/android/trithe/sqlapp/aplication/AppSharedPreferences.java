package android.trithe.sqlapp.aplication;

import android.content.SharedPreferences;

public class AppSharedPreferences {
    public static final String FIREBASE_TOKEN = "firebase_token";
    private String mFirebaseToken;

    private AppSharedPreferences(SharedPreferences pPref) {
        mFirebaseToken = pPref.getString(FIREBASE_TOKEN, "");
    }

    private static AppSharedPreferences sMAppSharedPreferences;

    public static AppSharedPreferences getInstance(SharedPreferences pPref) {
        if (sMAppSharedPreferences == null) {
            sMAppSharedPreferences = new AppSharedPreferences(pPref);
        }
        return sMAppSharedPreferences;
    }

    public void setAppSharedPreferences(SharedPreferences pPref) {
        SharedPreferences.Editor editor = pPref.edit();
        editor.putString(FIREBASE_TOKEN, mFirebaseToken);
        editor.apply();
    }


    public String getmFirebaseToken() {
        return mFirebaseToken;
    }

    public void setmFirebaseToken(String mFirebaseToken) {
        this.mFirebaseToken = mFirebaseToken;
    }
}

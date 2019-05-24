package android.trithe.sqlapp;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.trithe.sqlapp.utils.SharedPrefUtils;


public class MyApplication extends Application {
    private static SharedPrefUtils sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = new SharedPrefUtils(getApplicationContext());
        MultiDex.install(this);
    }

    public static SharedPrefUtils getSharedPreferences() {
        return sharedPreferences;
    }
}

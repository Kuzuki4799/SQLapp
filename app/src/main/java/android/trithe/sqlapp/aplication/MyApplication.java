package android.trithe.sqlapp.aplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.trithe.sqlapp.activity.LockScreenActivity;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import java.util.concurrent.atomic.AtomicInteger;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static SharedPrefUtils sharedPreferences;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private String strLockPass;
    private final static AtomicInteger c = new AtomicInteger(0);
    private GeoDataClient mGeoDataClient;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = new SharedPrefUtils(getApplicationContext());
        MultiDex.install(this);
        registerActivityLifecycleCallbacks(this);
        MyApplication.with(this).setGeoDataClient(Places.getGeoDataClient(getApplicationContext(), null));
        checkInternet();
    }

    private void checkInternet() {
        if (!isNetworkAvailable()) {
            SharedPrefUtils.putBoolean(Constant.KEY_CHECK_INTERNET, false);
        } else {
            SharedPrefUtils.putBoolean(Constant.KEY_CHECK_INTERNET, true);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        strLockPass = SharedPrefUtils.getString(Constant.KEY_LOCK, "");
        checkInternet();
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            if (!strLockPass.isEmpty()) {
                startActivity(new Intent(MyApplication.this, LockScreenActivity.class));
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        --activityReferences;

    }

    public static int getID() {
        return c.incrementAndGet();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


    public static SharedPrefUtils getSharedPreferences() {
        return sharedPreferences;
    }


    public static MyApplication with(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public SharedPreferences getSharedPreferencesApp() {
        return getApplicationContext().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public AppSharedPreferences getAppSharedPreference() {
        return AppSharedPreferences.getInstance(getSharedPreferencesApp());
    }

    public GeoDataClient getGeoDataClient() {
        return mGeoDataClient;
    }

    public void setGeoDataClient(GeoDataClient mGeoDataClient) {
        this.mGeoDataClient = mGeoDataClient;
    }
}

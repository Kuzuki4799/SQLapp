package android.trithe.sqlapp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.trithe.sqlapp.activity.LockScreenActivity;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.widget.Toast;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static SharedPrefUtils sharedPreferences;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private String strLockPass;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = new SharedPrefUtils(getApplicationContext());
        MultiDex.install(this);
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        strLockPass = SharedPrefUtils.getString(Constant.KEY_LOCK, "");
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            if (!strLockPass.isEmpty()) {
                startActivity(new Intent(MyApplication.this, LockScreenActivity.class));
            }
        }
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

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static SharedPrefUtils getSharedPreferences() {
        return sharedPreferences;
    }
}

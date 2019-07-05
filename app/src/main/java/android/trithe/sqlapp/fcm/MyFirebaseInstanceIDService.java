package android.trithe.sqlapp.fcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.trithe.sqlapp.aplication.AppSharedPreferences;
import android.trithe.sqlapp.aplication.MyApplication;
import android.trithe.sqlapp.config.Config;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences = MyApplication.with(this).getSharedPreferencesApp();
        AppSharedPreferences appSharedPreferences = MyApplication.with(this).getAppSharedPreference();
        appSharedPreferences.setmFirebaseToken(refreshedToken);
        appSharedPreferences.setAppSharedPreferences(sharedPreferences);

        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}


package android.trithe.sqlapp.aplication;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.activity.LockScreenActivity;
import android.trithe.sqlapp.activity.NotificationActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.manager.GetDataFilmManager;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;
import android.trithe.sqlapp.utils.NotificationUtils;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static SharedPrefUtils sharedPreferences;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private String strLockPass;
    private final static AtomicInteger c = new AtomicInteger(0);
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private GeoDataClient mGeoDataClient;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = new SharedPrefUtils(getApplicationContext());
        MultiDex.install(this);
        registerActivityLifecycleCallbacks(this);
        MyApplication.with(this).setGeoDataClient(Places.getGeoDataClient(getApplicationContext(), null));
        checkInternet();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    pushNotification(message);
                }
            }
        };
    }

    private void pushNotification(String message) {
        GetDataFilmManager getDataFilmManager = new GetDataFilmManager(new ResponseCallbackListener<GetDataFilmResponse>() {
            @Override
            public void onObjectComplete(String TAG, GetDataFilmResponse data) {
                if (data.status.equals("200")) {
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(Config.LINK_LOAD_IMAGE + data.result.get(0).imageCover)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    int mNotificationId = 1;
                                    Intent rIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, rIntent, 0);
                                    NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
                                    bpStyle.bigPicture(resource);
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.drawable.movies)
                                            .setContentTitle("Đề xuất")
                                            .addAction(R.drawable.love, "Watch", pendingIntent)
                                            .setContentText(data.result.get(0).name)
                                            .setStyle(bpStyle)
                                            .setAutoCancel(true);
                                    mBuilder.setContentIntent(pendingIntent);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    notificationManager.notify(mNotificationId, mBuilder.build());
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                }
            }

            @Override
            public void onResponseFailed(String TAG, String message) {

            }
        });
        getDataFilmManager.startGetDataFilm(0, SharedPrefUtils.getString(Constant.KEY_USER_ID, ""), message,
                0,1000,Config.API_GET_FILM_BY_ID);
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
        registerReceiver();
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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

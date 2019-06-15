package android.trithe.sqlapp.rest.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.trithe.sqlapp.MyApplication;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.rest.callback.CastRequestCallback;
import android.trithe.sqlapp.rest.callback.FeedBackRequestCallback;
import android.trithe.sqlapp.rest.callback.KindRequestCallback;
import android.trithe.sqlapp.rest.callback.LoginUserRequestCallback;
import android.trithe.sqlapp.rest.callback.FilmRequestCallback;
import android.trithe.sqlapp.rest.callback.LovedCastRequestCallback;
import android.trithe.sqlapp.rest.callback.NotificationRequestCallback;
import android.trithe.sqlapp.rest.callback.RatingFilmRequestCallback;
import android.trithe.sqlapp.rest.callback.SavedRequestCallback;
import android.trithe.sqlapp.utils.SharedPrefUtils;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v4.content.ContextCompat.getSystemService;
import static com.facebook.FacebookSdk.getCacheDir;

public class RestApiManager {

    private static RestApiManager mRestApiManager;

    private static Retrofit mRetrofit;

    private RestApiManager() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(Config.TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Config.TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(Config.TIME_OUT, TimeUnit.MILLISECONDS);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
//                        .header("Content", "multipart/form-data")
//                        .removeHeader("Content-Type")
                    .method(original.method(), original.body());
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }).addNetworkInterceptor(logging);

        OkHttpClient client = httpClient.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Config.API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .serializeNulls()
                        .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                        .create()))
                .client(client)
                .build();
    }

    public static RestApiManager getInstance() {
        if (mRestApiManager == null) {
            mRestApiManager = new RestApiManager();
        }
        return mRestApiManager;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }


    LoginUserRequestCallback loginUserRequestCallback() {
        return mRetrofit.create(LoginUserRequestCallback.class);
    }

    FilmRequestCallback filmRequestCallback() {
        return mRetrofit.create(FilmRequestCallback.class);
    }

    CastRequestCallback castRequestCallback() {
        return mRetrofit.create(CastRequestCallback.class);
    }

    SavedRequestCallback savedRequestCallback() {
        return mRetrofit.create(SavedRequestCallback.class);
    }

    LovedCastRequestCallback lovedCastRequestCallback() {
        return mRetrofit.create(LovedCastRequestCallback.class);
    }

    KindRequestCallback kindRequestCallback() {
        return mRetrofit.create(KindRequestCallback.class);
    }

    RatingFilmRequestCallback ratingFilmRequestCallback() {
        return mRetrofit.create(RatingFilmRequestCallback.class);
    }

    NotificationRequestCallback notificationRequestCallback() {
        return mRetrofit.create(NotificationRequestCallback.class);
    }

    FeedBackRequestCallback feedBackRequestCallback() {
        return mRetrofit.create(FeedBackRequestCallback.class);
    }
}

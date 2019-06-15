package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckSeenNotificationManager {
    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String API_NOTIFICATION = "API_NOTIFICATION";

    public CheckSeenNotificationManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void getDataNotification(String user_id, String film_id) {
        Call<BaseResponse> call = mRestApiManager.notificationRequestCallback()
                .checkSeenNotification(user_id, film_id);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(API_NOTIFICATION, response.body());
                } else {
                    mListener.onResponseFailed(API_NOTIFICATION, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(API_NOTIFICATION, t.getMessage());
            }
        });
}
}

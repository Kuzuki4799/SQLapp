package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetNotificationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_GET_COUNT_NOTIFICATION;
import static android.trithe.sqlapp.config.Config.API_NOTIFICATION;

public class GetDataNotificationManager {
    private ResponseCallbackListener<GetNotificationResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataNotificationManager(ResponseCallbackListener<GetNotificationResponse> mListener) {
        this.mListener = mListener;
    }

    public void getDataNotification(String user_id, String key, int page, int per_page) {
        switch (key) {
            case API_NOTIFICATION:
                Call<GetNotificationResponse> call = mRestApiManager.notificationRequestCallback()
                        .getDataNotification(user_id, page, per_page);
                call.enqueue(new Callback<GetNotificationResponse>() {
                    @Override
                    public void onResponse(Call<GetNotificationResponse> call, Response<GetNotificationResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_NOTIFICATION, response.body());
                        } else {
                            mListener.onResponseFailed(API_NOTIFICATION, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetNotificationResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_NOTIFICATION, t.getMessage());
                    }
                });
                break;
            case API_GET_COUNT_NOTIFICATION:
                Call<GetNotificationResponse> callCount = mRestApiManager.notificationRequestCallback()
                        .getCountNotification(user_id);
                callCount.enqueue(new Callback<GetNotificationResponse>() {
                    @Override
                    public void onResponse(Call<GetNotificationResponse> call, Response<GetNotificationResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_GET_COUNT_NOTIFICATION, response.body());
                        } else {
                            mListener.onResponseFailed(API_GET_COUNT_NOTIFICATION, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetNotificationResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_GET_COUNT_NOTIFICATION, t.getMessage());
                    }
                });
                break;
        }
    }
}

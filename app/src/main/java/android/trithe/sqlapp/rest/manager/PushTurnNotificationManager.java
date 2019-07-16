package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PushTurnNotificationManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String PUSH_NOTIFICATION_TURN = "PUSH_NOTIFICATION_TURN";

    public PushTurnNotificationManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void setPushTokenId(String user_id, int notification) {
        Call<BaseResponse> call = mRestApiManager.loginUserRequestCallback()
                .pushTurnNotification(user_id, notification);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(PUSH_NOTIFICATION_TURN, response.body());
                } else {
                    mListener.onResponseFailed(PUSH_NOTIFICATION_TURN, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mListener.onResponseFailed(PUSH_NOTIFICATION_TURN, t.getMessage());
            }
        });
    }
}

package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_CHECK_FEED_BACK;
import static android.trithe.sqlapp.config.Config.API_FEED_BACK;

public class FeedBackAppManager {
    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public FeedBackAppManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void feedBackApp(String user_id, String content, String key) {
        switch (key) {
            case API_FEED_BACK:
                Call<BaseResponse> call = mRestApiManager.feedBackRequestCallback()
                        .feedBackApp(user_id, content);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_FEED_BACK, response.body());
                        } else {
                            mListener.onResponseFailed(API_FEED_BACK, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_FEED_BACK, t.getMessage());
                    }
                });
                break;
            case API_CHECK_FEED_BACK:
                Call<BaseResponse> callCheck = mRestApiManager.feedBackRequestCallback()
                        .checkFeedBackApp(user_id);
                callCheck.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_CHECK_FEED_BACK, response.body());
                        } else {
                            mListener.onResponseFailed(API_CHECK_FEED_BACK, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_CHECK_FEED_BACK, t.getMessage());
                    }
                });
                break;
        }

    }
}

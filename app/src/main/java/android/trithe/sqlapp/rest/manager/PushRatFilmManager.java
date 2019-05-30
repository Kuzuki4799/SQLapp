package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_CHECK_RAT;
import static android.trithe.sqlapp.config.Config.API_PUSH_RAT;

public class PushRatFilmManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public PushRatFilmManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void pushRatFilm(String rat, String user_id, String film_id, String key) {
        switch (key) {
            case API_PUSH_RAT:
                Call<BaseResponse> call = mRestApiManager.ratingFilmRequestCallback()
                        .pushRatFilm(rat, user_id, film_id);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_PUSH_RAT, response.body());
                        } else {
                            mListener.onResponseFailed(API_PUSH_RAT, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_PUSH_RAT, t.getMessage());
                    }
                });
                break;
            case API_CHECK_RAT:
                Call<BaseResponse> callCheck = mRestApiManager.ratingFilmRequestCallback()
                        .getCheckRat(user_id, film_id);
                callCheck.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_CHECK_RAT, response.body());
                        } else {
                            mListener.onResponseFailed(API_CHECK_RAT, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_CHECK_RAT, t.getMessage());
                    }
                });
                break;
        }
    }
}

package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_CHECK_RAT_CINEMA;
import static android.trithe.sqlapp.config.Config.API_PUSH_INSERT_RAT_CINEMA;
import static android.trithe.sqlapp.config.Config.API_PUSH_RAT;

public class PushRatCinemaManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public PushRatCinemaManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void pushRatCinema(String rat, String user_id, String cinema_id, String key) {
        switch (key) {
            case API_PUSH_INSERT_RAT_CINEMA:
                Call<BaseResponse> callInsert = mRestApiManager.ratingCinemaRequestCallback()
                        .pushRatFilm(user_id, cinema_id, rat);
                callInsert.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_PUSH_INSERT_RAT_CINEMA, response.body());
                        } else {
                            mListener.onResponseFailed(API_PUSH_INSERT_RAT_CINEMA, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_PUSH_INSERT_RAT_CINEMA, t.getMessage());
                    }
                });
                break;
            case API_CHECK_RAT_CINEMA:
                Call<BaseResponse> callCheck = mRestApiManager.ratingCinemaRequestCallback()
                        .pushCheckRatCinema(user_id, cinema_id);
                callCheck.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_CHECK_RAT_CINEMA, response.body());
                        } else {
                            mListener.onResponseFailed(API_CHECK_RAT_CINEMA, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_CHECK_RAT_CINEMA, t.getMessage());
                    }
                });
                break;
        }
    }
}

package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.BaseResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_DELETE_LOVE_CINEMA;
import static android.trithe.sqlapp.config.Config.API_INSERT_LOVE_CINEMA;

public class LovedCinemaManager {

    private ResponseCallbackListener<BaseResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public LovedCinemaManager(ResponseCallbackListener<BaseResponse> mListener) {
        this.mListener = mListener;
    }

    public void pushLovedCinema(String user_id, String cinema_id, String key) {
        switch (key) {
            case API_INSERT_LOVE_CINEMA:
                Call<BaseResponse> callInsert = mRestApiManager.lovedCinemaRequestCallback()
                        .insertLoveCinema(user_id, cinema_id);
                callInsert.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_INSERT_LOVE_CINEMA, response.body());
                        } else {
                            mListener.onResponseFailed(API_INSERT_LOVE_CINEMA, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_INSERT_LOVE_CINEMA, t.getMessage());
                    }
                });
                break;
            case API_DELETE_LOVE_CINEMA:
                Call<BaseResponse> callDelete = mRestApiManager.lovedCinemaRequestCallback()
                        .deleteLoveCinema(user_id, cinema_id);
                callDelete.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_DELETE_LOVE_CINEMA, response.body());
                        } else {
                            mListener.onResponseFailed(API_DELETE_LOVE_CINEMA, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_DELETE_LOVE_CINEMA, t.getMessage());
                    }
                });
                break;
        }
    }
}

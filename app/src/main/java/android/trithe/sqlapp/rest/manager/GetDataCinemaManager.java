package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetAllDataCinemaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_DETAIL_CINEMA;
import static android.trithe.sqlapp.config.Config.API_GET_CINEMA;

public class GetDataCinemaManager {

    private ResponseCallbackListener<GetAllDataCinemaResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataCinemaManager(ResponseCallbackListener<GetAllDataCinemaResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCinema(String user_id, String id, String key) {
        switch (key) {
            case API_GET_CINEMA:
                Call<GetAllDataCinemaResponse> call = mRestApiManager.cinemaRequestCallback()
                        .getAllCinema(user_id);
                call.enqueue(new Callback<GetAllDataCinemaResponse>() {
                    @Override
                    public void onResponse(Call<GetAllDataCinemaResponse> call, Response<GetAllDataCinemaResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_GET_CINEMA, response.body());
                        } else {
                            mListener.onResponseFailed(API_GET_CINEMA, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllDataCinemaResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_GET_CINEMA, t.getMessage());
                    }
                });
                break;
            case API_DETAIL_CINEMA:
                Call<GetAllDataCinemaResponse> callDetailCinema = mRestApiManager.cinemaRequestCallback()
                        .getDetailCinema(user_id, id);
                callDetailCinema.enqueue(new Callback<GetAllDataCinemaResponse>() {
                    @Override
                    public void onResponse(Call<GetAllDataCinemaResponse> call, Response<GetAllDataCinemaResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_DETAIL_CINEMA, response.body());
                        } else {
                            mListener.onResponseFailed(API_DETAIL_CINEMA, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllDataCinemaResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_DETAIL_CINEMA, t.getMessage());
                    }
                });
                break;
        }
    }
}

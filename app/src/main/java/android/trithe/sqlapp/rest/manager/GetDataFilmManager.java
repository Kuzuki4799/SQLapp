package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_FILM;
import static android.trithe.sqlapp.config.Config.API_SEARCH_FILM;


public class GetDataFilmManager {

    private ResponseCallbackListener<GetDataFilmResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataFilmManager(ResponseCallbackListener<GetDataFilmResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataFilm(String name, String key) {
        switch (key) {
            case API_FILM:
                Call<GetDataFilmResponse> call = mRestApiManager.filmRequestCallback()
                        .getFilm();
                call.enqueue(new Callback<GetDataFilmResponse>() {
                    @Override
                    public void onResponse(Call<GetDataFilmResponse> call, Response<GetDataFilmResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_FILM, response.body());
                        } else {
                            mListener.onResponseFailed(API_FILM, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataFilmResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_FILM, t.getMessage());
                    }
                });
                break;
            case API_SEARCH_FILM:
                Call<GetDataFilmResponse> callSearch = mRestApiManager.filmRequestCallback()
                        .getSearchFilm(name);
                callSearch.enqueue(new Callback<GetDataFilmResponse>() {
                    @Override
                    public void onResponse(Call<GetDataFilmResponse> call, Response<GetDataFilmResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_SEARCH_FILM, response.body());
                        } else {
                            mListener.onResponseFailed(API_SEARCH_FILM, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataFilmResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_SEARCH_FILM, t.getMessage());
                    }
                });
                break;
        }

    }
}

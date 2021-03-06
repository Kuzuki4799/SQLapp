package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_FILM;
import static android.trithe.sqlapp.config.Config.API_GET_FILM_BY_ID;
import static android.trithe.sqlapp.config.Config.API_GET_FILM_SAVED;
import static android.trithe.sqlapp.config.Config.API_SEARCH_FILM;

public class GetDataFilmManager {

    private ResponseCallbackListener<GetDataFilmResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetDataFilmManager(ResponseCallbackListener<GetDataFilmResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataFilm(int status, String user_id, String name, int page, int per_page, String key) {
        switch (key) {
            case API_FILM:
                Call<GetDataFilmResponse> call = mRestApiManager.filmRequestCallback()
                        .getFilm(status, user_id, page, per_page);
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
                        .getSearchFilm(user_id, name, page, per_page);
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
            case API_GET_FILM_SAVED:
                Call<GetDataFilmResponse> callFilmBySaved = mRestApiManager.filmRequestCallback()
                        .getSavedFilm(user_id, page, per_page);
                callFilmBySaved.enqueue(new Callback<GetDataFilmResponse>() {
                    @Override
                    public void onResponse(Call<GetDataFilmResponse> call, Response<GetDataFilmResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_GET_FILM_BY_ID, response.body());
                        } else {
                            mListener.onResponseFailed(API_GET_FILM_BY_ID, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataFilmResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_GET_FILM_BY_ID, t.getMessage());
                    }
                });
                break;
        }
    }
}

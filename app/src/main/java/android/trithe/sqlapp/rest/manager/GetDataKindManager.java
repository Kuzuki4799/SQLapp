package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataKindResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_DATA_KIND;
import static android.trithe.sqlapp.config.Config.API_KIND;
import static android.trithe.sqlapp.config.Config.API_KIND_FILM_DETAIL;

public class GetDataKindManager {

    private ResponseCallbackListener<GetDataKindResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_DATA_KIND = "GET_DATA_KIND";

    public GetDataKindManager(ResponseCallbackListener<GetDataKindResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataKind(String id, String key) {
        switch (key) {
            case API_KIND:
                Call<GetDataKindResponse> call = mRestApiManager.kindRequestCallback()
                        .getKind();
                call.enqueue(new Callback<GetDataKindResponse>() {
                    @Override
                    public void onResponse(Call<GetDataKindResponse> call, Response<GetDataKindResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_KIND, response.body());
                        } else {
                            mListener.onResponseFailed(API_KIND, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataKindResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_KIND, t.getMessage());
                    }
                });
                break;
            case API_KIND_FILM_DETAIL:
                Call<GetDataKindResponse> call_detail_film = mRestApiManager.kindRequestCallback()
                        .getKindFilmDetail(id);
                call_detail_film.enqueue(new Callback<GetDataKindResponse>() {
                    @Override
                    public void onResponse(Call<GetDataKindResponse> call, Response<GetDataKindResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_KIND_FILM_DETAIL, response.body());
                        } else {
                            mListener.onResponseFailed(API_KIND_FILM_DETAIL, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataKindResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_KIND_FILM_DETAIL, t.getMessage());
                    }
                });
                break;
            case API_DATA_KIND:
                Call<GetDataKindResponse> call_data = mRestApiManager.kindRequestCallback()
                        .getDataKind(id);
                call_data.enqueue(new Callback<GetDataKindResponse>() {
                    @Override
                    public void onResponse(Call<GetDataKindResponse> call, Response<GetDataKindResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(GET_DATA_KIND, response.body());
                        } else {
                            mListener.onResponseFailed(GET_DATA_KIND, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDataKindResponse> call, Throwable t) {
                        mListener.onResponseFailed(GET_DATA_KIND, t.getMessage());
                    }
                });
                break;
        }
    }
}

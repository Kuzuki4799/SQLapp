package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_GET_ALL_CAST;
import static android.trithe.sqlapp.config.Config.API_SEARCH_CAST;

public class GetAllDataCastManager {

    private ResponseCallbackListener<GetAllDataCastResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetAllDataCastManager(ResponseCallbackListener<GetAllDataCastResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCast(String name, String key) {
        switch (key) {
            case API_GET_ALL_CAST:
                Call<GetAllDataCastResponse> call = mRestApiManager.castRequestCallback()
                        .getAllCast();
                call.enqueue(new Callback<GetAllDataCastResponse>() {
                    @Override
                    public void onResponse(Call<GetAllDataCastResponse> call, Response<GetAllDataCastResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_GET_ALL_CAST, response.body());
                        } else {
                            mListener.onResponseFailed(API_GET_ALL_CAST, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllDataCastResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_GET_ALL_CAST, t.getMessage());
                    }
                });
                break;
            case API_SEARCH_CAST:
                Call<GetAllDataCastResponse> callSearch = mRestApiManager.castRequestCallback()
                        .getDataSearchCast(name);
                callSearch.enqueue(new Callback<GetAllDataCastResponse>() {
                    @Override
                    public void onResponse(Call<GetAllDataCastResponse> call, Response<GetAllDataCastResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_SEARCH_CAST, response.body());
                        } else {
                            mListener.onResponseFailed(API_SEARCH_CAST, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllDataCastResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_SEARCH_CAST, t.getMessage());
                    }
                });
                break;
        }
    }
}

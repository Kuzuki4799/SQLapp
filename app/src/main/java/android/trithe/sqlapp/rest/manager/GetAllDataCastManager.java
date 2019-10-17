package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetAllDataCastResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.trithe.sqlapp.config.Config.API_GET_ALL_CAST_BY_LOVED;
import static android.trithe.sqlapp.config.Config.API_SEARCH_CAST;

public class GetAllDataCastManager {

    private ResponseCallbackListener<GetAllDataCastResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();

    public GetAllDataCastManager(ResponseCallbackListener<GetAllDataCastResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCast(String id, String name, int page, int per_page, String key) {
        switch (key) {
            case API_GET_ALL_CAST_BY_LOVED:
                Call<GetAllDataCastResponse> callLoved = mRestApiManager.castRequestCallback()
                        .getAllCastByLoved(id, page, per_page);
                callLoved.enqueue(new Callback<GetAllDataCastResponse>() {
                    @Override
                    public void onResponse(Call<GetAllDataCastResponse> call, Response<GetAllDataCastResponse> response) {
                        if (response.isSuccessful()) {
                            mListener.onObjectComplete(API_GET_ALL_CAST_BY_LOVED, response.body());
                        } else {
                            mListener.onResponseFailed(API_GET_ALL_CAST_BY_LOVED, response.message());
                            response.code();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAllDataCastResponse> call, Throwable t) {
                        mListener.onResponseFailed(API_GET_ALL_CAST_BY_LOVED, t.getMessage());
                    }
                });
                break;
            case API_SEARCH_CAST:
                Call<GetAllDataCastResponse> callSearch = mRestApiManager.castRequestCallback()
                        .getDataSearchCast(id, name, page, per_page);
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

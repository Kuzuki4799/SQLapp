package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataCountryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDataCastCountryManager {

    private ResponseCallbackListener<GetDataCountryResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_CAST_CONTRY = "GET_CAST_CONTRY";

    public GetDataCastCountryManager(ResponseCallbackListener<GetDataCountryResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCastCountry(String cast_id) {
        Call<GetDataCountryResponse> call = mRestApiManager.castRequestCallback()
                .getCountryCast(cast_id);
        call.enqueue(new Callback<GetDataCountryResponse>() {
            @Override
            public void onResponse(Call<GetDataCountryResponse> call, Response<GetDataCountryResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_CAST_CONTRY, response.body());
                } else {
                    mListener.onResponseFailed(GET_CAST_CONTRY, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataCountryResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_CAST_CONTRY, t.getMessage());
            }
        });
    }
}

package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataCastListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetDataCastListManager {

    private ResponseCallbackListener<GetDataCastListResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_CAST = "GET_CAST";

    public GetDataCastListManager(ResponseCallbackListener<GetDataCastListResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataCast(String user_id, String film_id, int page, int per_page) {
        Call<GetDataCastListResponse> call = mRestApiManager.castRequestCallback()
                .getCast(user_id, film_id, page, per_page);
        call.enqueue(new Callback<GetDataCastListResponse>() {
            @Override
            public void onResponse(Call<GetDataCastListResponse> call, Response<GetDataCastListResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_CAST, response.body());
                } else {
                    mListener.onResponseFailed(GET_CAST, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataCastListResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_CAST, t.getMessage());
            }
        });
    }
}

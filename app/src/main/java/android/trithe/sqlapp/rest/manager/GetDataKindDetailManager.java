package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataFilmResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetDataKindDetailManager {

    private ResponseCallbackListener<GetDataFilmResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_KIND_DETAIL = "GET_KIND_DETAIL";

    public GetDataKindDetailManager(ResponseCallbackListener<GetDataFilmResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataKindDetail(String user_id, String kind_id, int page, int per_page) {
        Call<GetDataFilmResponse> call = mRestApiManager.kindRequestCallback()
                .getKindDetail(user_id, kind_id, page, per_page);
        call.enqueue(new Callback<GetDataFilmResponse>() {
            @Override
            public void onResponse(Call<GetDataFilmResponse> call, Response<GetDataFilmResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_KIND_DETAIL, response.body());
                } else {
                    mListener.onResponseFailed(GET_KIND_DETAIL, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataFilmResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_KIND_DETAIL, t.getMessage());
            }
        });
    }
}

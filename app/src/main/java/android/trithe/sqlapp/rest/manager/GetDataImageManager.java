package android.trithe.sqlapp.rest.manager;

import android.trithe.sqlapp.rest.callback.ResponseCallbackListener;
import android.trithe.sqlapp.rest.response.GetDataPosterImageResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetDataImageManager {

    private ResponseCallbackListener<GetDataPosterImageResponse> mListener;
    private RestApiManager mRestApiManager = RestApiManager.getInstance();
    private static final String GET_POSTER_IMAGE = "GET_POSTER_IMAGE";

    public GetDataImageManager(ResponseCallbackListener<GetDataPosterImageResponse> mListener) {
        this.mListener = mListener;
    }

    public void startGetDataPoster() {
        Call<GetDataPosterImageResponse> call = mRestApiManager.posterImageRequestCallback()
                .getPosterImage();
        call.enqueue(new Callback<GetDataPosterImageResponse>() {
            @Override
            public void onResponse(Call<GetDataPosterImageResponse> call, Response<GetDataPosterImageResponse> response) {
                if (response.isSuccessful()) {
                    mListener.onObjectComplete(GET_POSTER_IMAGE, response.body());
                } else {
                    mListener.onResponseFailed(GET_POSTER_IMAGE, response.message());
                    response.code();
                }
            }

            @Override
            public void onFailure(Call<GetDataPosterImageResponse> call, Throwable t) {
                mListener.onResponseFailed(GET_POSTER_IMAGE, t.getMessage());
            }
        });
    }
}

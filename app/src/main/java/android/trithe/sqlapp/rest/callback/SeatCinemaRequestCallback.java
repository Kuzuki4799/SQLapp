package android.trithe.sqlapp.rest.callback;

import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.response.GetDataSeatResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SeatCinemaRequestCallback {

    @FormUrlEncoded
    @POST(Config.API_GET_SEAT_BY_TIME)
    Call<GetDataSeatResponse> getSeatByTime(@Field("theater_id") int theater_id);
}

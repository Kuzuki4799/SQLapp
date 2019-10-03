package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.FilmDetailModel;

import com.google.gson.annotations.SerializedName;

public class GetDataFilmDetailResponse extends BaseResponse {
    @SerializedName("result")
    public FilmDetailModel result;
}

package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.RatingModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataRatingFilmResponse extends BaseResponse {
    @SerializedName("result")
    public List<RatingModel> result;
}

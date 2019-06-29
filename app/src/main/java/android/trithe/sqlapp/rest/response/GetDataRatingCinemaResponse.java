package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.RatingCinemaModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataRatingCinemaResponse extends BaseResponse {
    @SerializedName("result")
    public List<RatingCinemaModel> result;
}

package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.ShowingFilmCinemaModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllDataFilmCinemaResponse extends BaseResponse {
    @SerializedName("result")
    public List<ShowingFilmCinemaModel> result = new ArrayList<>();
}

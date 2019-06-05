package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.SeriesModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetDataSeriesFilmResponse extends BaseResponse {
    @SerializedName("result")
    public List<SeriesModel> result = new ArrayList<>();
}

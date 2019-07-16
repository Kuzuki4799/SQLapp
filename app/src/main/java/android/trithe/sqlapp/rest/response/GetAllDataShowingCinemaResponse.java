package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.ShowingCinemaModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllDataShowingCinemaResponse extends BaseResponse {
    @SerializedName("result")
    public List<ShowingCinemaModel> result = new ArrayList<>();
}

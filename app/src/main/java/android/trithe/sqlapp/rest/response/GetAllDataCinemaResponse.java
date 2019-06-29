package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.CinemaModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllDataCinemaResponse extends BaseResponse {
    @SerializedName("result")
    public List<CinemaModel> result = new ArrayList<>();
}

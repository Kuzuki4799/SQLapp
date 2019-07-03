package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.LoveCinemaModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataLoveCountCinemaResponse extends BaseResponse {
    @SerializedName("result")
    public List<LoveCinemaModel> result;
}

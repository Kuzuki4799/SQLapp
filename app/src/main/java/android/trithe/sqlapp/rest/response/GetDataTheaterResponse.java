package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.TheaterModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataTheaterResponse extends BaseResponse {
    @SerializedName("result")
    public List<TheaterModel> result;
}

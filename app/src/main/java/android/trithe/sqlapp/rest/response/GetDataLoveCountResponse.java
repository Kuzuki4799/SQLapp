package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.LoveModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataLoveCountResponse extends BaseResponse {
    @SerializedName("result")
    public List<LoveModel> result;
}

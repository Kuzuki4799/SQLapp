package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.CastDetailModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllDataCastResponse extends BaseResponse {
    @SerializedName("result")
    public List<CastDetailModel> result = new ArrayList<>();
}

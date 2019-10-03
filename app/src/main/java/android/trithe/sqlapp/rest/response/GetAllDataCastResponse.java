package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.CastModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllDataCastResponse extends BaseResponse {
    @SerializedName("result")
    public List<CastModel> result = new ArrayList<>();
}

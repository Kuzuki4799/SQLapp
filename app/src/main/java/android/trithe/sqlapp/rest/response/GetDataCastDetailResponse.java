package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.CastDetailModel;
import android.trithe.sqlapp.rest.model.CastModel;

import com.google.gson.annotations.SerializedName;

public class GetDataCastDetailResponse extends BaseResponse {
    @SerializedName("result")
    public CastDetailModel result;
}

package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.CastListModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataCastListResponse extends BaseResponse {
    @SerializedName("result")
    public List<CastListModel> result;
}

package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.KindModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataKindResponse extends BaseResponse {
    @SerializedName("result")
    public List<KindModel> result;
}

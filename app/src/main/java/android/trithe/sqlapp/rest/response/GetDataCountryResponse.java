package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.JobandCountryModel;

import com.google.gson.annotations.SerializedName;


public class GetDataCountryResponse extends BaseResponse {
    @SerializedName("result")
    public JobandCountryModel result;
}

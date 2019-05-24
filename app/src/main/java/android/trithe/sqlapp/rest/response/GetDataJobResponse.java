package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.JobModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataJobResponse extends BaseResponse {
    @SerializedName("result")
    public List<JobModel> result;
}

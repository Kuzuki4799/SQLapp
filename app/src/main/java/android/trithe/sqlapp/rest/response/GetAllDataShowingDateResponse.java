package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.ShowingDateModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllDataShowingDateResponse extends BaseResponse {
    @SerializedName("result")
    public List<ShowingDateModel> result = new ArrayList<>();
}

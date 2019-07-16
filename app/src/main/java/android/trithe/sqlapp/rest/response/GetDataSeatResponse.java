package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.SeatModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetDataSeatResponse extends BaseResponse {
    @SerializedName("result")
    @Expose
    public List<SeatModel> result = new ArrayList<>();
}

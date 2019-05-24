package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.PosterModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDataPosterImageResponse extends BaseResponse  {
    @SerializedName("result")
    public List<PosterModel> result = null;
}

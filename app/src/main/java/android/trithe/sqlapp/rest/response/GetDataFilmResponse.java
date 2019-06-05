package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.FilmModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetDataFilmResponse extends BaseResponse {
    @SerializedName("result")
    public List<FilmModel> result = new ArrayList<>();
}

package android.trithe.sqlapp.rest.response;

import android.trithe.sqlapp.rest.model.CommentFilmModel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetAllDataCommentFilmResponse extends BaseResponse {
    @SerializedName("result")
    public List<CommentFilmModel> result = new ArrayList<>();
}

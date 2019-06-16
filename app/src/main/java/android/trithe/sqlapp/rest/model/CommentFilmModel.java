package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentFilmModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("film_id")
    @Expose
    public String filmId;
}

package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("rat")
    @Expose
    public int rat;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("film_id")
    @Expose
    public String filmId;
}

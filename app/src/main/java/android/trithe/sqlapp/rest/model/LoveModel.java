package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoveModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("cast_id")
    @Expose
    public String castId;
    @SerializedName("user_id")
    @Expose
    public String userId;
}

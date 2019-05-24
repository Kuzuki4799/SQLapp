package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastListModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("figure")
    @Expose
    public String figure;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("cast_id")
    @Expose
    public String castId;
}

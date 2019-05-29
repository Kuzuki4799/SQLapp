package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilmModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("release_date")
    @Expose
    public String releaseDate;
    @SerializedName("detail")
    @Expose
    public String detail;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("image_cover")
    @Expose
    public String imageCover;
    @SerializedName("trailer")
    @Expose
    public String trailer = "";
    @SerializedName("time")
    @Expose
    public int time;
    @SerializedName("format")
    @Expose
    public String format;
}

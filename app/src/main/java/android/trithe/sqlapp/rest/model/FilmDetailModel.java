package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilmDetailModel {
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
    @SerializedName("movie")
    @Expose
    public String movie;
    @SerializedName("trailer")
    @Expose
    public String trailer = "";
    @SerializedName("time")
    @Expose
    public int time;
    @SerializedName("format")
    @Expose
    public String format;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("sizes")
    @Expose
    public int sizes;
    @SerializedName("saved")
    @Expose
    public int saved;
    @SerializedName("series")
    @Expose
    public List<SeriesModel> series = new ArrayList<>();
    @SerializedName("kind")
    @Expose
    public List<KindModel> kind = new ArrayList<>();
    @SerializedName("rat")
    @Expose
    public List<RatingModel> rat = new ArrayList<>();
    @SerializedName("cast")
    @Expose
    public List<CastListModel> cast = new ArrayList<>();
}

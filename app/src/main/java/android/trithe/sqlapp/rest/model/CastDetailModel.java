package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CastDetailModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("date_of_birth")
    @Expose
    public String dateOfBirth;
    @SerializedName("country_id")
    @Expose
    public String countryId;
    @SerializedName("infomation")
    @Expose
    public String infomation;
    @SerializedName("views")
    @Expose
    public int views;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("image_cover")
    @Expose
    public String imageCover;
    @SerializedName("loved")
    @Expose
    public int loved;
    @SerializedName("country")
    @Expose
    public List<CountryModel> country = new ArrayList<>();
    @SerializedName("job")
    @Expose
    public List<JobModel> job = new ArrayList<>();
    @SerializedName("film")
    @Expose
    public List<FilmModel> film = new ArrayList<>();
}

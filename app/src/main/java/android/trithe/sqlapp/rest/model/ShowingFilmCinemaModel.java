package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowingFilmCinemaModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("saved")
    @Expose
    public int saved;
    @SerializedName("film_id")
    @Expose
    public String filmId;
    @SerializedName("cinema_id")
    @Expose
    public String cinemaId;
    @SerializedName("theater_id")
    @Expose
    public String theaterId;
}

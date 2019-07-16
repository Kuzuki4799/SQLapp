package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowingCinemaModel {
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
    @SerializedName("film_id")
    @Expose
    public int filmId;
    @SerializedName("cinema_id")
    @Expose
    public int cinemaId;
    @SerializedName("theater_id")
    @Expose
    public int theater_id;
}

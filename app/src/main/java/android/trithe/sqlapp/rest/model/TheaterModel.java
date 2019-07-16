package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TheaterModel {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("cinema_id")
    @Expose
    public int cinemaId;
    @SerializedName("wide")
    @Expose
    public int wide;
}

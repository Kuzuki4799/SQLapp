package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeatModel {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("score")
    @Expose
    public int score;
    @SerializedName("theater_id")
    @Expose
    public int theaterId;
}

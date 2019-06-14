package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("seen")
    @Expose
    public int seen;
}

package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CinemaModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("lat_location")
    @Expose
    public String latLocation;
    @SerializedName("long_location")
    @Expose
    public String longLocation;
    @SerializedName("detail")
    @Expose
    public String detail;
    @SerializedName("website")
    @Expose
    public String website;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("views")
    @Expose
    public int views;
    @SerializedName("conpany_id")
    @Expose
    public String conpanyId;
}

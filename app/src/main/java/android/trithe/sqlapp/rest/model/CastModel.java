package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CastModel {
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

    public int getLoved() {
        return loved;
    }

    public void setLoved(int loved) {
        this.loved = loved;
    }
}

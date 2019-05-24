package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("image")
    @Expose
    public String image;
}

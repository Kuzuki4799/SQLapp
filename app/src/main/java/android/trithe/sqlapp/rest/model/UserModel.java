package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("token_id")
    @Expose
    public String tokenId;
    @SerializedName("device_token")
    @Expose
    public String deviceToken;
}

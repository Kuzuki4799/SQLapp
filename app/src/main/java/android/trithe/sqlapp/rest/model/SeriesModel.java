package android.trithe.sqlapp.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SeriesModel {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("film_id")
    @Expose
    public String filmId;
    @SerializedName("link")
    @Expose
    public String link;

    public SeriesModel(String id, String name, String filmId, String link) {
        this.id = id;
        this.name = name;
        this.filmId = filmId;
        this.link = link;
    }
}

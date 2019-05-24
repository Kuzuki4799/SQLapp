package android.trithe.sqlapp.model;

import android.trithe.sqlapp.rest.model.FilmModel;

import java.util.List;

public class Header {
    private String sectionLabel;
    private List<FilmModel> models;

    public Header(String sectionLabel, List<FilmModel> models) {
        this.sectionLabel = sectionLabel;
        this.models = models;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public List<FilmModel> getModels() {
        return models;
    }

    public void setModels(List<FilmModel> models) {
        this.models = models;
    }
}

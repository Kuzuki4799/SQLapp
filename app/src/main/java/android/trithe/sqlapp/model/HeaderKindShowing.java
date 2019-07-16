package android.trithe.sqlapp.model;

import java.util.List;

public class HeaderKindShowing {
    private String sectionLabel;
    private List<ShowingFilmByDate> list;

    public HeaderKindShowing() {
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public void setSectionLabel(String sectionLabel) {
        this.sectionLabel = sectionLabel;
    }

    public List<ShowingFilmByDate> getList() {
        return list;
    }

    public void setList(List<ShowingFilmByDate> list) {
        this.list = list;
    }

    public HeaderKindShowing(String sectionLabel, List<ShowingFilmByDate> list) {

        this.sectionLabel = sectionLabel;
        this.list = list;
    }
}

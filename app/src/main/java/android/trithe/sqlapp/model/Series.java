package android.trithe.sqlapp.model;

import android.trithe.sqlapp.rest.model.SeriesModel;


public class Series {
    private SeriesModel list;
    private boolean check;

    public Series(SeriesModel list, boolean check) {
        this.list = list;
        this.check = check;
    }

    public SeriesModel getList() {
        return list;
    }

    public void setList(SeriesModel list) {
        this.list = list;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

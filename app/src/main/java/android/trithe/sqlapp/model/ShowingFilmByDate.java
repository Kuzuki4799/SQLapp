package android.trithe.sqlapp.model;

import android.trithe.sqlapp.rest.model.ShowingCinemaModel;


public class ShowingFilmByDate {
    private ShowingCinemaModel list;
    private boolean check;

    public ShowingFilmByDate(ShowingCinemaModel list, boolean check) {
        this.list = list;
        this.check = check;
    }

    public ShowingCinemaModel getList() {
        return list;
    }

    public void setList(ShowingCinemaModel list) {
        this.list = list;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

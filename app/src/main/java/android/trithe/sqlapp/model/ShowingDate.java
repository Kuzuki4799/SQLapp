package android.trithe.sqlapp.model;

import android.trithe.sqlapp.rest.model.ShowingDateModel;

public class ShowingDate {
    private ShowingDateModel list;
    private boolean check;

    public ShowingDate(ShowingDateModel list, boolean check) {
        this.list = list;
        this.check = check;
    }

    public ShowingDateModel getList() {
        return list;
    }

    public void setList(ShowingDateModel list) {
        this.list = list;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

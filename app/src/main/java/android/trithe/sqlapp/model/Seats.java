package android.trithe.sqlapp.model;

import android.trithe.sqlapp.rest.model.SeatModel;

public class Seats {
    private SeatModel list;
    private boolean check;

    public Seats(SeatModel list, boolean check) {
        this.list = list;
        this.check = check;
    }

    public SeatModel getList() {
        return list;
    }

    public void setList(SeatModel list) {
        this.list = list;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

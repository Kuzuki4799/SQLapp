package android.trithe.sqlapp.callback;

import android.trithe.sqlapp.model.Seats;

import java.util.List;

public interface OnSeatItemClickListener {
    void onBookingSeat(List<Seats> list);
}

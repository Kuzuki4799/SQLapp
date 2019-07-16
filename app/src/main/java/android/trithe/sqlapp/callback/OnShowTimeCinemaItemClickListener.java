package android.trithe.sqlapp.callback;

import android.trithe.sqlapp.model.ShowingFilmByDate;

import java.util.List;

public interface OnShowTimeCinemaItemClickListener {
    void onShowingTime(List<ShowingFilmByDate> list, ShowingFilmByDate showingFilmByDate);
}

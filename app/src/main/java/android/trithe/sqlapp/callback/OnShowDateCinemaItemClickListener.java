package android.trithe.sqlapp.callback;

import android.trithe.sqlapp.model.ShowingDate;

import java.util.List;

public interface OnShowDateCinemaItemClickListener {
    void onShowingDate(List<ShowingDate> list, ShowingDate showingFilmByDate);
}

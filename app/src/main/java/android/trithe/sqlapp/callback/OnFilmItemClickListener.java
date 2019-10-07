package android.trithe.sqlapp.callback;

import android.support.v7.widget.CardView;

public interface OnFilmItemClickListener {
    void onCheckItemFilm(int position, CardView cardView);

    void changSetDataFilm(int position, String key);
}

package android.trithe.sqlapp.callback;

import android.trithe.sqlapp.rest.model.FilmModel;
import android.widget.ImageView;

public interface OnFilmItemClickListener {
    void onFilm(FilmModel filmModel, ImageView imageView);
    void changSetData();
}

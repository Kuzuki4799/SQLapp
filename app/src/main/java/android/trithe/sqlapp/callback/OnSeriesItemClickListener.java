package android.trithe.sqlapp.callback;

import android.trithe.sqlapp.model.Series;

import java.util.List;

public interface OnSeriesItemClickListener {
    void onFilm(List<Series> list, Series seriesModel);
}

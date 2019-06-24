package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.UpComingHolder;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UpComingAdapter extends RecyclerView.Adapter<UpComingHolder> {
    private List<FilmModel> list;
    private OnFilmItemClickListener onItemClickListener;

    public UpComingAdapter(List<FilmModel> albumList, OnFilmItemClickListener onClickItemPopularFilm) {
        this.list = albumList;
        this.onItemClickListener = onClickItemPopularFilm;
    }

    @NonNull
    @Override
    public UpComingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(UpComingHolder.LAYOUT_ID, parent, false);
        return new UpComingHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final UpComingHolder holder, final int position) {
        final FilmModel filmModel = list.get(position);
        holder.setupData(filmModel, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

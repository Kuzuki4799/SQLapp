package android.trithe.sqlapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.FilmHolder;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmHolder> {
    private List<FilmModel> list;
    private OnFilmItemClickListener onItemClickListener;

    public FilmAdapter(List<FilmModel> albumList) {
        this.list = albumList;
    }


    public void setOnClickItemFilm(OnFilmItemClickListener onClickItemPopularFilm) {
        this.onItemClickListener = onClickItemPopularFilm;
    }

    @NonNull
    @Override
    public FilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(FilmHolder.LAYOUT_ID, parent, false);
        return new FilmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilmHolder holder, final int position) {
        final FilmModel filmModel = list.get(position);
        holder.setupData(filmModel, onItemClickListener);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

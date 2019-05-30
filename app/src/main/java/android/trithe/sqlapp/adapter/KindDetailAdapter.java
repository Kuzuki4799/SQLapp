package android.trithe.sqlapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.KindDetailHolder;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class KindDetailAdapter extends RecyclerView.Adapter<KindDetailHolder> {
    private List<FilmModel> list;
    private OnFilmItemClickListener onItemClickListener;

    public KindDetailAdapter(List<FilmModel> albumList) {
        this.list = albumList;
    }


    public void setOnClickItemPopularFilm(OnFilmItemClickListener onClickItemPopularFilm) {
        this.onItemClickListener = onClickItemPopularFilm;
    }

    @NonNull
    @Override
    public KindDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(KindDetailHolder.LAYOUT_ID, parent, false);
        return new KindDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KindDetailHolder holder, final int position) {
        final FilmModel filmModel = list.get(position);
        holder.setupData(filmModel, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

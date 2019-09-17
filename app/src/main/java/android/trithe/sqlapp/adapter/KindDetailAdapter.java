package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.KindDetailHolder;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class KindDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FilmModel> list;
    private OnFilmItemClickListener onItemClickListener;

    public KindDetailAdapter(List<FilmModel> albumList, OnFilmItemClickListener onClickItemPopularFilm) {
        this.list = albumList;
        this.onItemClickListener = onClickItemPopularFilm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(KindDetailHolder.LAYOUT_ID, parent, false);
            return new KindDetailHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof KindDetailHolder) {
            final FilmModel filmModel = list.get(position);
            ((KindDetailHolder) holder).setupData(filmModel, onItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

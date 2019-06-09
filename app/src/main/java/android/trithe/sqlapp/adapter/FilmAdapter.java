package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

    public FilmAdapter(List<FilmModel> albumList) {
        this.list = albumList;
    }

    @NonNull
    @Override
    public FilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(FilmHolder.LAYOUT_ID, parent, false);
        return new FilmHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final FilmHolder holder, final int position) {
        final FilmModel filmModel = list.get(position);
        holder.setupData(filmModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

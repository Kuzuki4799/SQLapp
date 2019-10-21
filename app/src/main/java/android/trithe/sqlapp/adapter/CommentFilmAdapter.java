package android.trithe.sqlapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.CommentFilmHolder;
import android.trithe.sqlapp.rest.model.CommentFilmModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CommentFilmAdapter extends RecyclerView.Adapter<CommentFilmHolder> {
    private List<CommentFilmModel> list;

    public CommentFilmAdapter(List<CommentFilmModel> albumList) {
        this.list = albumList;
    }

    @NonNull
    @Override
    public CommentFilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(CommentFilmHolder.LAYOUT_ID, parent, false);
        return new CommentFilmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentFilmHolder holder, final int position) {
        final CommentFilmModel commentFilmModel = list.get(position);
        holder.setupData(commentFilmModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

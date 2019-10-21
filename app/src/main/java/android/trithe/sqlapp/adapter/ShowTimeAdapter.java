package android.trithe.sqlapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.ShowTimeHolder;
import android.trithe.sqlapp.callback.OnShowTimeCinemaItemClickListener;
import android.trithe.sqlapp.model.ShowingFilmByDate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ShowTimeAdapter extends RecyclerView.Adapter<ShowTimeHolder> {
    private List<ShowingFilmByDate> list;
    private OnShowTimeCinemaItemClickListener onShowTimeCinemaItemClickListener;

    public ShowTimeAdapter(List<ShowingFilmByDate> list, OnShowTimeCinemaItemClickListener onShowTimeCinemaItemClickListener) {
        this.list = list;
        this.onShowTimeCinemaItemClickListener = onShowTimeCinemaItemClickListener;
    }

    @NonNull
    @Override
    public ShowTimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ShowTimeHolder.LAYOUT_ID, parent, false);
        return new ShowTimeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShowTimeHolder holder, final int position) {
        final ShowingFilmByDate castListModel = list.get(position);
        holder.setupData(list, castListModel, onShowTimeCinemaItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

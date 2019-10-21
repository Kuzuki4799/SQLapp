package android.trithe.sqlapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.FilmHolder;
import android.trithe.sqlapp.adapter.holder.LoadingViewHolder;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FilmModel> list;
    private int key;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean onLoadMore = true;

    public FilmAdapter(List<FilmModel> albumList, int key) {
        this.list = albumList;
        this.key = key;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(FilmHolder.LAYOUT_ID, parent, false);
            return new FilmHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(LoadingViewHolder.LAYOUT_ID_HORIZONTAL, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FilmHolder) {
            final FilmModel filmModel = list.get(position);
            ((FilmHolder) holder).setupData(filmModel, key);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (onLoadMore) {
            if (position == list.size() - 1) return VIEW_TYPE_LOADING;
            else{
                return VIEW_TYPE_ITEM;
            }
        } else return VIEW_TYPE_ITEM;
    }

    public boolean isOnLoadMore() {
        return onLoadMore;
    }

    public void setOnLoadMore(boolean onLoadMore) {
        this.onLoadMore = onLoadMore;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

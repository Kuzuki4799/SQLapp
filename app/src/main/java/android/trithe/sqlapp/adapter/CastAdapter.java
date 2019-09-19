package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.CastHolder;
import android.trithe.sqlapp.adapter.holder.LoadingViewHolder;
import android.trithe.sqlapp.callback.OnChangeSetItemClickLovedListener;
import android.trithe.sqlapp.rest.model.CastDetailModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CastDetailModel> list;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean onLoadMore = true;
    private OnChangeSetItemClickLovedListener onChangeSetItemClickLovedListener;

    public CastAdapter(List<CastDetailModel> albumList, OnChangeSetItemClickLovedListener onChangeSetItemClickLovedListener) {
        this.list = albumList;
        this.onChangeSetItemClickLovedListener = onChangeSetItemClickLovedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(CastHolder.LAYOUT_ID, parent, false);
            return new CastHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(LoadingViewHolder.LAYOUT_ID_HORIZONTAL_TWO, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CastHolder) {
            final CastDetailModel castListModel = list.get(position);
            ((CastHolder) holder).setupData(castListModel, onChangeSetItemClickLovedListener);
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

package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.CinemaPlaceHolder;
import android.trithe.sqlapp.callback.OnChangeSetItemClickLovedListener;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CinemaPlaceAdapter extends RecyclerView.Adapter<CinemaPlaceHolder> {
    private List<CinemaModel> list;
    private OnChangeSetItemClickLovedListener onChangeSetItemClickLovedListener;

    public CinemaPlaceAdapter(List<CinemaModel> list, OnChangeSetItemClickLovedListener onChangeSetItemClickLovedListener) {
        this.list = list;
        this.onChangeSetItemClickLovedListener = onChangeSetItemClickLovedListener;
    }

    @NonNull
    @Override
    public CinemaPlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(CinemaPlaceHolder.LAYOUT_ID, parent, false);
        return new CinemaPlaceHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final CinemaPlaceHolder holder, final int position) {
        final CinemaModel castListModel = list.get(position);
        holder.setupData(castListModel, onChangeSetItemClickLovedListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

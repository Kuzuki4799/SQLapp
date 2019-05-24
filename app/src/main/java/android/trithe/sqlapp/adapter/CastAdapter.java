package android.trithe.sqlapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.CastHolder;
import android.trithe.sqlapp.rest.model.CastListModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastHolder> {
    private List<CastListModel> list;

    public CastAdapter(List<CastListModel> albumList) {
        this.list = albumList;
    }

    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(CastHolder.LAYOUT_ID, parent, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CastHolder holder, final int position) {
        final CastListModel castListModel = list.get(position);
        holder.setupData(castListModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

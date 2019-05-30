package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.CastDetailHolder;
import android.trithe.sqlapp.rest.model.CastListModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CastDetailAdapter extends RecyclerView.Adapter<CastDetailHolder> {
    private List<CastListModel> list;

    public CastDetailAdapter(List<CastListModel> albumList) {
        this.list = albumList;
    }

    @NonNull
    @Override
    public CastDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(CastDetailHolder.LAYOUT_ID, parent, false);
        return new CastDetailHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final CastDetailHolder holder, final int position) {
        final CastListModel castListModel = list.get(position);
        holder.setupData(castListModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

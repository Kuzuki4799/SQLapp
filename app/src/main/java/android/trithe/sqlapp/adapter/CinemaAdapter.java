package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.CinemaHolder;
import android.trithe.sqlapp.rest.model.CinemaModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaHolder> {
    private List<CinemaModel> list;

    public CinemaAdapter(List<CinemaModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CinemaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(CinemaHolder.LAYOUT_ID, parent, false);
        return new CinemaHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final CinemaHolder holder, final int position) {
        final CinemaModel castListModel = list.get(position);
        holder.setupData(castListModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

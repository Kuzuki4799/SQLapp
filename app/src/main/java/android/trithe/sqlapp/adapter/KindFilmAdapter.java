package android.trithe.sqlapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.KindFilmHolder;
import android.trithe.sqlapp.callback.OnKindItemClickListener;
import android.trithe.sqlapp.rest.model.KindModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class KindFilmAdapter extends RecyclerView.Adapter<KindFilmHolder> {
    private List<KindModel> list;
    private OnKindItemClickListener onItemClickListener;

    public KindFilmAdapter(List<KindModel> albumList) {
        this.list = albumList;
    }


    public void setOnItemClickListener(OnKindItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public KindFilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(KindFilmHolder.LAYOUT_ID, parent, false);
        return new KindFilmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KindFilmHolder holder, final int position) {
        final KindModel kindModel = list.get(position);
        holder.setupData(kindModel,onItemClickListener);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

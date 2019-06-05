package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.CastHolder;
import android.trithe.sqlapp.callback.OnCastItemClickListener;
import android.trithe.sqlapp.rest.model.CastDetailModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastHolder> {
    private List<CastDetailModel> list;
    private OnCastItemClickListener onCastItemClickListener;

    public CastAdapter(List<CastDetailModel> albumList) {
        this.list = albumList;
    }

    public void setOnClickItemFilm(OnCastItemClickListener onCastItemClickListener) {
        this.onCastItemClickListener = onCastItemClickListener;
    }

    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(CastHolder.LAYOUT_ID, parent, false);
        return new CastHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final CastHolder holder, final int position) {
        final CastDetailModel castListModel = list.get(position);
        holder.setupData(castListModel,onCastItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}

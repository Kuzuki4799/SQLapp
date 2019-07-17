package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.ShowDateHolder;
import android.trithe.sqlapp.rest.model.ShowingDateModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ShowDateAdapter extends RecyclerView.Adapter<ShowDateHolder> {
    private List<ShowingDateModel> list;

    public ShowDateAdapter(List<ShowingDateModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ShowDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ShowDateHolder.LAYOUT_ID, parent, false);
        return new ShowDateHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ShowDateHolder holder, final int position) {
        final ShowingDateModel castListModel = list.get(position);
        holder.setupData(castListModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

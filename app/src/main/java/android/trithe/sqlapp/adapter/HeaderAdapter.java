package android.trithe.sqlapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.HeaderHolder;
import android.trithe.sqlapp.callback.OnHeaderItemClickListener;
import android.trithe.sqlapp.model.Header;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<HeaderHolder> {
    private List<Header> HeaderArrayList;
    private OnHeaderItemClickListener onHeaderItemClickListener;

    public HeaderAdapter(List<Header> HeaderArrayList) {
        this.HeaderArrayList = HeaderArrayList;
    }


    public void setOnClickItemPopularFilm(OnHeaderItemClickListener onHeaderItemClickListener) {
        this.onHeaderItemClickListener = onHeaderItemClickListener;
    }

    @Override
    public HeaderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(HeaderHolder.LAYOUT_ID, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindViewHolder(HeaderHolder holder, int position) {
        Header header = HeaderArrayList.get(position);
        holder.setupData(header, onHeaderItemClickListener);
    }

    @Override
    public int getItemCount() {
        return HeaderArrayList.size();
    }


}

package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.adapter.FilmAdapter;
import android.trithe.sqlapp.callback.OnHeaderItemClickListener;
import android.trithe.sqlapp.model.Header;
import android.trithe.sqlapp.utils.EndlessRecyclerOnScrollListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HeaderHolder extends RecyclerView.ViewHolder {
    private TextView sectionLabel;
    private ImageView btnMore;
    private RecyclerView itemRecyclerView;

    public static final int LAYOUT_ID = R.layout.item_header;

    public HeaderHolder(@NonNull View itemView) {
        super(itemView);
        Context context = itemView.getContext();
        sectionLabel = itemView.findViewById(R.id.section_label);
        btnMore = itemView.findViewById(R.id.btn_more);
        itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        itemRecyclerView.setLayoutManager(linearLayoutManager1);
        itemRecyclerView.setNestedScrollingEnabled(false);
    }

    public void setupData(final Header header, final OnHeaderItemClickListener onHeaderItemClickListener) {
        sectionLabel.setText(header.getSectionLabel());
        FilmAdapter adapter = new FilmAdapter(header.getModels(), 0);
        adapter.setOnLoadMore(false);
        itemRecyclerView.setAdapter(adapter);
        btnMore.setOnClickListener(v -> onHeaderItemClickListener.onFilm(header));
    }
}

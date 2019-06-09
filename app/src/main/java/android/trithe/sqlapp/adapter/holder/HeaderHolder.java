package android.trithe.sqlapp.adapter.holder;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.adapter.FilmAdapter;
import android.trithe.sqlapp.callback.OnFilmItemClickListener;
import android.trithe.sqlapp.callback.OnHeaderItemClickListener;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.model.Header;
import android.trithe.sqlapp.rest.model.FilmModel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HeaderHolder extends RecyclerView.ViewHolder {
    private TextView sectionLabel;
    private ImageView btnMore;
    private RecyclerView itemRecyclerView;
    private Context context;

    public static final int LAYOUT_ID = R.layout.item_header;

    public HeaderHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        sectionLabel = itemView.findViewById(R.id.section_label);
        btnMore = itemView.findViewById(R.id.btn_more);
        itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
    }

    public void setupData(final Header header, final OnHeaderItemClickListener onHeaderItemClickListener) {
        sectionLabel.setText(header.getSectionLabel());
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        itemRecyclerView.setLayoutManager(linearLayoutManager1);
        FilmAdapter adapter = new FilmAdapter(header.getModels());
        itemRecyclerView.setAdapter(adapter);
        btnMore.setOnClickListener(v -> onHeaderItemClickListener.onFilm(header));
    }
}

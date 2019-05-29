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

public class HeaderHolder extends RecyclerView.ViewHolder implements OnFilmItemClickListener {
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
        adapter.setOnClickItemFilm(this);
        itemRecyclerView.setAdapter(adapter);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHeaderItemClickListener.onFilm(header);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onFilm(FilmModel filmModel, ImageView imageView) {
        Intent intent = new Intent(context, DetailFilmActivity.class);
        intent.putExtra(Constant.TITLE, filmModel.name);
        intent.putExtra(Constant.DETAIL, filmModel.detail);
        intent.putExtra(Constant.FORMAT, filmModel.format);
        intent.putExtra(Constant.ID, filmModel.id);
        intent.putExtra(Constant.DATE, filmModel.releaseDate);
        intent.putExtra(Constant.IMAGE, filmModel.image);
        intent.putExtra(Constant.IMAGE_COVER, filmModel.imageCover);
        intent.putExtra(Constant.TRAILER, filmModel.trailer);
        intent.putExtra(Constant.TIME, filmModel.time);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, imageView, "sharedName");
        context.startActivity(intent, options.toBundle());
    }
}

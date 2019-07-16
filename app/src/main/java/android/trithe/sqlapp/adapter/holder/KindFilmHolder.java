package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnKindItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.model.KindModel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class KindFilmHolder extends RecyclerView.ViewHolder {
    private Context context;
    private ImageView imageBg;
    private CardView cardView;
    private ImageView image;
    private TextView txtTitle;

    public static final int LAYOUT_ID = R.layout.item_kind;

    public KindFilmHolder(@NonNull View itemView) {
        super(itemView);
        imageBg = itemView.findViewById(R.id.image_bg);
        image = itemView.findViewById(R.id.image);
        cardView = itemView.findViewById(R.id.card_view);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        context = itemView.getContext();
    }

    public void setupData(final KindModel dataModel, final OnKindItemClickListener onItemClickListener) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(image);
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.imageBg).into(imageBg);
        txtTitle.setText(dataModel.name);
        imageBg.setOnClickListener(v -> onItemClickListener.onKind(dataModel, cardView));
    }
}

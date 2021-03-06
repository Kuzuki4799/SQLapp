package android.trithe.sqlapp.adapter.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.callback.OnNotificationItemClickListener;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.rest.model.NotificationModel;
import android.trithe.sqlapp.utils.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class NotificationHolder extends RecyclerView.ViewHolder {
    private ImageView imgAvatar;
    private TextView txtNameFilm;
    private TextView txtTime;
    private ImageView imgSeen;
    private Context context;

    public static final int LAYOUT_ID = R.layout.item_notification;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
        imgAvatar = itemView.findViewById(R.id.imgAvatar);
        txtNameFilm = itemView.findViewById(R.id.txtNameFilm);
        txtTime = itemView.findViewById(R.id.txtTime);
        imgSeen = itemView.findViewById(R.id.imgSeen);
        context = itemView.getContext();
    }

    public void setupData(NotificationModel dataModel, OnNotificationItemClickListener onNotificationItemClickListener) {
        Glide.with(context).load(Config.LINK_LOAD_IMAGE + dataModel.image).into(imgAvatar);
        txtNameFilm.setText(dataModel.name);
        DateUtils.parseDateFormat(txtTime, dataModel.time);
        if (dataModel.seen == 1) {
            imgSeen.setVisibility(View.VISIBLE);
        } else {
            imgSeen.setVisibility(View.GONE);
        }
        itemView.setOnClickListener(v -> onNotificationItemClickListener.onClickItem(dataModel, getAdapterPosition()));
    }
}

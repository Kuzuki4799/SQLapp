package android.trithe.sqlapp.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.trithe.sqlapp.adapter.holder.NotificationHolder;
import android.trithe.sqlapp.callback.OnNotificationItemClickListener;
import android.trithe.sqlapp.rest.model.NotificationModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {
    private List<NotificationModel> list;
    private OnNotificationItemClickListener onNotificationItemClickListener;

    public NotificationAdapter(List<NotificationModel> notificationModelList, OnNotificationItemClickListener onNotificationItemClickListener) {
        this.list = notificationModelList;
        this.onNotificationItemClickListener = onNotificationItemClickListener;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(NotificationHolder.LAYOUT_ID, parent, false);
        return new NotificationHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final NotificationHolder holder, final int position) {
        NotificationModel notificationModel = list.get(position);
        holder.setupData(notificationModel, onNotificationItemClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

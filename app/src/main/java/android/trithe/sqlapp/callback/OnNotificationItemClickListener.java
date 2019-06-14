package android.trithe.sqlapp.callback;

import android.trithe.sqlapp.rest.model.NotificationModel;

public interface OnNotificationItemClickListener {
    void onClickItem(NotificationModel dataModel, String key);
}

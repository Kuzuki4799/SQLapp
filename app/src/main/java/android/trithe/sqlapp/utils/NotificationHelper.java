package android.trithe.sqlapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.activity.NotificationActivity;
import android.trithe.sqlapp.config.Constant;


public class NotificationHelper extends ContextWrapper {
    private static final int CHANNEL_ID = 1;
    private static final String CHANNEL_NAME = "Kuzuki Dev";
    public NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    private void createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(String.valueOf(CHANNEL_ID), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(notificationChannel);
        }
    }

    public NotificationManager getManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder pushNotification(String id, String image, String name) {
        return new Notification.Builder(getApplicationContext(), String.valueOf(CHANNEL_ID))
                .setAutoCancel(true)
                .setColor(Color.RED)
                .setContentText(name)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent(id))
                .setContentTitle(getString(R.string.recommend))
                .addAction(R.drawable.love, getString(R.string.watch), pendingIntent(id))
                .addAction(R.drawable.unlove, getString(R.string.see_all), pendingIntentAllNotification())
                .setStyle(new Notification.BigPictureStyle().bigPicture(NotificationUtils.getBitmapFromURL(image)));
    }

    private PendingIntent pendingIntent(String id) {
        Intent rIntent = new Intent(getApplicationContext(), DetailFilmActivity.class);
        rIntent.putExtra(Constant.ID, id);
        return PendingIntent.getActivity(getApplicationContext(), 0, rIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent pendingIntentAllNotification() {
        Intent rIntentAll = new Intent(getApplicationContext(), NotificationActivity.class);
        return PendingIntent.getActivity(getApplicationContext(), 0, rIntentAll, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

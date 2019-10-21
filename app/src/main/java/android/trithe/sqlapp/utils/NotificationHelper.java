package android.trithe.sqlapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.activity.NotificationActivity;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.receiver.NotificationOnClickReceiver;

public class NotificationHelper extends ContextWrapper {
    public static final int CHANNEL_ID = 1;
    private static final String CHANNEL_NAME = "Kuzuki Dev";
    public NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(String.valueOf(CHANNEL_ID), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(notificationChannel);
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getApplicationContext().getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alarmSound);
            r.play();
        }
    }

    public NotificationManager getManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    public NotificationManager clear() {
        if (notificationManager != null)
            notificationManager.cancel(CHANNEL_ID);
        return notificationManager;
    }

    public Notification.Builder pushNotification(String id, String image, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), String.valueOf(CHANNEL_ID))
                    .setAutoCancel(true)
                    .setColor(Color.RED)
                    .setContentText(name)
                    .setShowWhen(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent(id))
                    .setContentTitle(getString(R.string.recommend))
                    .addAction(R.drawable.movie, getString(R.string.watch), pendingIntent(id))
                    .addAction(R.drawable.assignment, getString(R.string.see_all), pendingIntentAllNotification())
                    .addAction(R.drawable.ic_close, getString(R.string.dismiss), pendingClearNotification())
                    .setStyle(new Notification.BigPictureStyle().bigPicture(NotificationUtils.getBitmapFromURL(image)));
        }
        return null;
    }

    private PendingIntent pendingIntent(String id) {
        Intent rIntent = new Intent(getApplicationContext(), DetailFilmActivity.class);
        rIntent.putExtra(Constant.ID, id);
        rIntent.putExtra(Constant.NOTIFICATION, 1);
        return PendingIntent.getActivity(getApplicationContext(), 0, rIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent pendingClearNotification() {
        Intent rIntent = new Intent(getApplicationContext(), NotificationOnClickReceiver.class);
        return PendingIntent.getBroadcast(getApplicationContext(), 0, rIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private PendingIntent pendingIntentAllNotification() {
        Intent rIntentAll = new Intent(getApplicationContext(), NotificationActivity.class);
        return PendingIntent.getActivity(getApplicationContext(), 0, rIntentAll, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}

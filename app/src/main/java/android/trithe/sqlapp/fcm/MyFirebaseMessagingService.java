package android.trithe.sqlapp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.activity.NotificationActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.NotificationHelper;
import android.trithe.sqlapp.utils.NotificationUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationManager notificationManager;
    private int mNotificationId = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                handleDataMessage(json);
            } else {
                pushNotification(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleDataMessage(JSONObject json) {
        NotificationHelper notificationHelper = new NotificationHelper(this);
        try {
            JSONObject data = json.getJSONObject("data");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String title = data.getString("title");
//            boolean isBackground = data.getBoolean("is_background");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");

            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            Notification.Builder builder = notificationHelper.pushNotification(title, Config.LINK_LOAD_IMAGE + imageUrl, message);
            notificationHelper.getManager().notify(new Random().nextInt(), builder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushNotification(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject("data");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String title = data.getString("title");

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.movies)
                    .setContentIntent(pendingIntent(title))
                    .setContentTitle(getString(R.string.recommend))
                    .addAction(R.drawable.love, getString(R.string.watch), pendingIntent(title))
                    .addAction(R.drawable.unlove, getString(R.string.see_all), pendingIntentAllNotification())
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(NotificationUtils.getBitmapFromURL(imageUrl)));
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(mNotificationId, mBuilder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private PendingIntent pendingIntent(String id) {
        Intent rIntent = new Intent(getApplicationContext(), DetailFilmActivity.class);
        rIntent.putExtra(Constant.ID, id);
        notificationManager.cancel(mNotificationId);
        return PendingIntent.getActivity(getApplicationContext(), 0, rIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent pendingIntentAllNotification() {
        Intent rIntentAll = new Intent(getApplicationContext(), NotificationActivity.class);
        notificationManager.cancel(mNotificationId);
        return PendingIntent.getActivity(getApplicationContext(), 0, rIntentAll, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

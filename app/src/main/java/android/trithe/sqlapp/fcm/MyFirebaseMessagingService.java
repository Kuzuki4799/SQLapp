package android.trithe.sqlapp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.trithe.sqlapp.R;
import android.trithe.sqlapp.activity.DetailFilmActivity;
import android.trithe.sqlapp.config.Config;
import android.trithe.sqlapp.config.Constant;
import android.trithe.sqlapp.utils.NotificationHelper;
import android.trithe.sqlapp.utils.NotificationUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

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
            notificationHelper.getManager().notify(NotificationHelper.CHANNEL_ID, builder.build());
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

            Notification.Builder mBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder = new Notification.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(NotificationUtils.getBitmapFromURL(imageUrl))
                        .setContentTitle(getString(R.string.recommend))
                        .setShowWhen(true)
                        .setContentText(message)
                        .setColor(Color.RED)
                        .setContentIntent(pendingIntent(title));
            } else {
                mBuilder = new Notification.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(NotificationUtils.getBitmapFromURL(imageUrl))
                        .setContentTitle(getString(R.string.recommend))
                        .setShowWhen(true)
                        .setContentText(message)
                        .setContentIntent(pendingIntent(title));
            }
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotification.notify(NotificationHelper.CHANNEL_ID, mBuilder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private PendingIntent pendingIntent(String id) {
        Intent rIntent = new Intent(getApplicationContext(), DetailFilmActivity.class);
        rIntent.putExtra(Constant.ID, id);
        rIntent.putExtra(Constant.NOTIFICATION, 1);
        return PendingIntent.getActivity(getApplicationContext(), 0, rIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

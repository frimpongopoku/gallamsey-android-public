package com.pongo.zembe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class GallamseyNotificationService extends FirebaseMessagingService {

  private static final String TAG = "NOTIFICATION-MESSAGE";

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    String notificationID = remoteMessage.getData().get("id");
    String title = remoteMessage.getData().get("title");
    String body = remoteMessage.getData().get("body");
    String channelId = remoteMessage.getData().get("channel");
    Log.d(TAG, "onMessageReceived: " + notificationID);
    Log.d(TAG, "onMessageReceived: " + channelId);
    switch (channelId) {
      case NotificationChannelSetup.MESSAGING_CHANNEL_ID: {
        buildAndDeployNotification(notificationID, title, body,
          R.color.deep_orange, R.drawable.gallamsey_msg_notifcation_icon,
          channelId, getChatListPendingIntent());
        break;
      }
      case NotificationChannelSetup.ERRAND_RELATED_CHANNEL_ID: {
        buildAndDeployNotification(notificationID, title,
          body, R.color.appColor, R.drawable.gallamsey_notify_good, channelId, null);
        break;
      }
      case NotificationChannelSetup.OTHER_CHANNEL_ID: {
        buildAndDeployNotification(notificationID, title, body,
          R.color.appColor, R.drawable.gallamsey_notify_good, channelId, null);
        break;
      }
      default:
        buildAndDeployNotification(notificationID, title, body, R.color.appColor,
          R.drawable.gallamsey_notify_good, NotificationChannelSetup.OTHER_CHANNEL_ID, null);
        break;
    }

  }

  private void buildAndDeployNotification(String id, String title, String body, int color, int icon, String channelId, PendingIntent intent) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
      .setSmallIcon(icon)
      .setColor(getColor(color))
      .setContentTitle(title)
      .setContentText(body)
      .setAutoCancel(true)
      .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    if (intent != null) {
      builder.setContentIntent(intent);
    }
    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    if (notificationManager != null) {
      notificationManager.notify(Integer.parseInt(id), builder.build());
    }
  }

  private PendingIntent getChatListPendingIntent() {
    Intent notifyIntent = new Intent(this, Home.class);
   //----------------------Set the Activity to start in a new, empty task----------
    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
      | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notifyIntent.putExtra(Konstants.DEFAULT_PAGE,Konstants.CHAT_LIST_PAGE);
   //------------------------------------Create the PendingIntent------------------
    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
      this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
    );

    return notifyPendingIntent;
  }


}

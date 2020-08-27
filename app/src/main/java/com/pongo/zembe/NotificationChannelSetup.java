package com.pongo.zembe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;


public class NotificationChannelSetup {

  private final Context context;

  public NotificationChannelSetup(Context context) {
    this.context = context;
  }

  private static final String TAG = "NOTIFICATION-CHANNEL";

  public static final String MESSAGING_CHANNEL_ID = "Gallamsey-Messages";
  public static final String MESSAGING_CHANNEL_NAME = "Gallamsey  Messages";
  public static final String MESSAGING_CHANNEL_DESC = "Notifications related to messaging";
  public static final String ERRAND_RELATED_CHANNEL_ID = "Gallamsey-Errands";
  public static final String ERRAND_CHANNEL_NAME = "Gallamsey Errands";
  public static final String ERRAND_CHANNEL_DESC = "Notifications related to errand status";
  public static final String OTHER_CHANNEL_ID = "Gallamsey-Others";
  public static final String OTHER_CHANNEL_NAME = "Other Gallamsey Notifications";
  public static final String OTHER_CHANNEL_DESC = "Notifications related news, upgrades, discounts and more";

  public void createChannels() {
    createNewChannel(MESSAGING_CHANNEL_NAME, MESSAGING_CHANNEL_ID, MESSAGING_CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
    createNewChannel(ERRAND_CHANNEL_NAME, ERRAND_RELATED_CHANNEL_ID, ERRAND_CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
    createNewChannel(OTHER_CHANNEL_NAME, OTHER_CHANNEL_ID, OTHER_CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
  }

  public void createNewChannel(String name, String id, String description, int importance) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel newChannel = new NotificationChannel(id, name, importance);
      newChannel.setDescription(description);
      NotificationManager manager = context.getSystemService(NotificationManager.class);
      if (manager != null) {
        manager.createNotificationChannel(newChannel);
        Log.d(TAG, "createdNewChannel: " + name + "...");
      }
    }
  }
}

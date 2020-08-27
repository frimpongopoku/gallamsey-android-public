package com.pongo.zembe;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class GallamseyCloudMessagingSetup extends FirebaseMessagingService {

  private String TAG = "MESSAGINGSTUFF";
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
  }

  @Override
  public void onNewToken(String s) {
    super.onNewToken(s);
    Log.d(TAG, s);
  }
}

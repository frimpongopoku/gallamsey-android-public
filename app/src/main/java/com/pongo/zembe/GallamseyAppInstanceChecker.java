package com.pongo.zembe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GallamseyAppInstanceChecker {


  String userID;
  ArrayList<DeviceToToken> oldTokens;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference userCollectionRef = db.collection(Konstants.USER_COLLECTION);
  GroundUser authenticatedUser =  new GroundUser();
  private static final String TAG = "INSTANCE-ID--->";
  public static final String ANDROID = "android";

  public GallamseyAppInstanceChecker(ArrayList<DeviceToToken> oldTokens, String userID) {
    this.userID = userID;
    this.oldTokens = oldTokens;
  }


  public void setAuthenticatedUser(GroundUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
  }

  public void checkAndUpdateInstanceTokenOnServer() {
    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
      @Override
      public void onSuccess(InstanceIdResult instanceIdResult) {
        String token = instanceIdResult.getToken();
        if (oldTokens != null) {
          for (int i = 0; i < oldTokens.size(); i++) {
            DeviceToToken item = oldTokens.get(i);
            if(item.getName().equals(ANDROID) && item.getToken().equals(token)){
              // ------- token hasn't changed, so just
              return;
            }
          }
          return;
        }
        // -------- Save token to database
        ArrayList<DeviceToToken> deviceToTokens = new ArrayList<>();
        DeviceToToken android = new DeviceToToken(ANDROID, token);
        deviceToTokens.add(android);
        authenticatedUser.setAppInstanceToken(deviceToTokens);
        userCollectionRef.document(userID).set(authenticatedUser).addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Log.d(TAG, "onSuccess: saved token in user sheet");

          }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.d(TAG, "onFailure: could not save token to user");
          }
        });
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure: " + e.getMessage());
      }
    });
  }

  public void getInstanceToken(final InstanceTokenCollectables callback) {
    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
      @Override
      public void onSuccess(InstanceIdResult instanceIdResult) {
        callback.getToken(instanceIdResult.getToken());
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        callback.error(e.getMessage());
      }
    });
  }

  interface InstanceTokenCollectables {
    void getToken(String token);

    void error(String errorMessage);
  }
}

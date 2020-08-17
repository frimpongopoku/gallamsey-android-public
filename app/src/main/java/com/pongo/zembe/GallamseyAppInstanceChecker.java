package com.pongo.zembe;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class GallamseyAppInstanceChecker {

  String userID;
  String oldToken;
  FirebaseFirestore db = FirebaseFirestore.getInstance();
  CollectionReference userCollectionRef = db.collection(Konstants.USER_COLLECTION);
  private static final String TAG = "INSTANCE-ID--->";

  public GallamseyAppInstanceChecker(String oldToken, String userID) {
    this.userID = userID;
    this.oldToken = oldToken;
  }

  public void checkAndUpdateInstanceTokenOnServer() {
    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
      @Override
      public void onSuccess(InstanceIdResult instanceIdResult) {
        String token = instanceIdResult.getToken();
        if (oldToken != null && oldToken.equals(token)) {
          // ------- token hasn't changed, so just
          return;
        }
        // -------- Save token to database
        userCollectionRef.document(userID).update("appInstanceToken", token).addOnSuccessListener(new OnSuccessListener<Void>() {
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
